/* Program modifies a BMP image w/ two options: grayscale, inverted
 * @author Ryo112358 (Jan 2018)
 * Run as follows: ./bmp_edit [--inverted,--grayscale] [FILENAME]
 */
#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <stdlib.h>
#include <math.h>

#define CALC_INDEX(a,b) (a*dibheader.width + b)

#pragma pack(1)

typedef struct BMPHeader {
	char format_id[2];
	int file_size;
	short reserved_v1;
	short reserved_v2;
	int offset;
} BMPHeader;

typedef struct DIBHeader {
	int header_size;
	int width;
	int height;
	short num_color_panes;
	short bits_per_pixel;
	int compression_sch;
	int image_size;
	int x_resolution;
	int y_resolution;
	int num_colors;
	int num_imp_colors;
} DIBHeader;

typedef struct Pixel {
	unsigned char blue;
	unsigned char green;
	unsigned char red;
} Pixel;

// Helper functions -----------------------------------------

int streq_nocase(const char* a, const char* b)
{
	for(; *a && *b; a++, b++)
		if(tolower(*a) != tolower(*b))
			return 0;

	return *a == 0 && *b == 0;
}

int calc_padding(DIBHeader* dibheader)
{
	int padding = 4 - ((dibheader->width*3) % 4);
	return padding == 4 ? 0 : padding;
}

// Image Invert functions--------------------------------------
void invert_pixel(Pixel* p)
{
	p->blue = ~p->blue;
	p->green = ~p->green;
	p->red = ~p->red;
}

void invert_image(FILE* img, BMPHeader* bmpheader, DIBHeader* dibheader, int row_padding)
{
	Pixel cpixel;
	// Position cursor at beginning of pixel data segment
	fseek(img, bmpheader->offset, SEEK_SET);

	// Iterate through file pixels
	for(int i = 0; i < dibheader->height; i++)
	{
		for (int j = 0; j < dibheader->width; j++)
		{
			fread(&cpixel, sizeof(Pixel), 1, img);
			invert_pixel(&cpixel);
			fseek(img, -sizeof(Pixel), SEEK_CUR);
			fwrite(&cpixel, sizeof(Pixel), 1, img);
		}
		// Skip over padded bytes at row end
		fseek(img, row_padding, SEEK_CUR);
	}
}

// Image Grayscale Functions ---------------------------------
void grayscale_pixel(Pixel* p)
{
	float B = (float) p->blue / 255;
	float G = (float) p->green / 255;
	float R = (float) p->red / 255;

	double y = (0.2126 * R) + (0.7152 * G) + (0.0722 * B);

	if(y > 0.0031308)
		y = (1.055 * pow(y, 1/2.4)) - 0.055;
	else
		y = 12.92 * y;

	y *= 255;

	p->blue = (char) y;
	p->green = (char) y;
	p->red = (char) y;
}

void grayscale_image(FILE* img, BMPHeader* bmpheader, DIBHeader* dibheader, int row_padding)
{
	Pixel cpixel;
	fseek(img, bmpheader->offset, SEEK_SET);

	for(int i = 0; i < dibheader->height; i++)
	{
		for (int j = 0; j < dibheader->width; j++)
		{
			fread(&cpixel, sizeof(Pixel), 1, img);
			grayscale_pixel(&cpixel);
			fseek(img, -sizeof(Pixel), SEEK_CUR);
			fwrite(&cpixel, sizeof(Pixel), 1, img);
		}
		fseek(img, row_padding, SEEK_CUR);
	}
}

int main(int argc, char** argv)
{
	char* img_file;
	char* mode;
	BMPHeader bmpheader;
	DIBHeader dibheader;

	// Check for correct number of arguments
	if(argc > 2) {
		mode = argv[1];
		img_file = argv[2];
	} else {
		printf("Required Arguments: ./bmp_edit [--inverted,--grayscale] [FILENAME]\n");
		return 1;
	}

	// Open image file
	FILE* img = fopen(img_file, "rb+");

	// Check file headers to verify bitmap format -------------------------
	fread(&bmpheader, sizeof(bmpheader), 1, img);
	if (bmpheader.format_id[0] != 'B' || bmpheader.format_id[1] != 'M') {
		printf("Incorrect format identifier: %c%c\n", bmpheader.format_id[0], bmpheader.format_id[1]);
		return 1;
	}

	fread(&dibheader, sizeof(dibheader), 1, img);
	if (dibheader.header_size != 40) {
		printf("Incorrect header size: %d\n", dibheader.header_size);
		return 1;
	}
	if (dibheader.bits_per_pixel != 24) {
		printf("Incorrect bits per pixel: %d\n", dibheader.bits_per_pixel);
		return 1;
	}

	// Invert/Grayscale Image ----------------------------------------------

	int row_padding = calc_padding(&dibheader);
	//int num_pixels = dibheader.height * dibheader.width;

	char* invert = "--invert";
	char* grayscale = "--grayscale";

	if (streq_nocase(mode, invert)) {
		invert_image(img, &bmpheader, &dibheader, row_padding);
	} else if (streq_nocase(mode, grayscale)) {
		grayscale_image(img, &bmpheader, &dibheader, row_padding);
	}

	fclose(img);
	return 0;
}
