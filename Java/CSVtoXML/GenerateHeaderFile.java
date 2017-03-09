import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Ryo112358
 * @title CSV Column Header Extractor
 *
 * @about Extracts the header record (top row) or fields from a CSV file. Generates a file which contains each header in order on a separate line.
 * @input CSV data file with header record
 * @directions Modify string variables on lines 24-25 to specify filenames.
 * @demo Example files included in directory. Have a look!
 *
 * @disclaimer Sample data taken from Veekun's Pokedex project. Check it out for a boatload of Pokemon info!
 * @commandline To create a command line version, replace variables on lines 24-25 with args[0-1]... and remember the order!
 */
public class GenerateHeaderFile {

	public static void main(String[] args) throws FileNotFoundException {
		
		// File variables - change as needed! -------------------------------------
		String csv = "dataH.csv";
		String headerfile = "header-result.txt";
		
		ArrayList<String> columnHeaders = new ArrayList<String>();

		// Parse CSV file for headers ---------------------------------------------
		parseCSVFile(csv, columnHeaders);
		
		// Write header File-------------------------------------------------------
		writeFile(headerfile, columnHeaders);
		
		System.out.print("The headers file was successfully generated!");
	}
	
	public static boolean parseCSVFile(String csv, ArrayList<String> fields) throws FileNotFoundException {
		Scanner parseHeaders = new Scanner(new File(csv));
		Scanner headerRecord = new Scanner(parseHeaders.nextLine());
		headerRecord.useDelimiter(",");

		while (headerRecord.hasNext()) {
			fields.add(headerRecord.next());
		}

		parseHeaders.close();
		headerRecord.close();
		return true;
	}

	public static boolean writeFile(String headerfile, ArrayList<String> fields) throws FileNotFoundException {
		PrintWriter outputFile = new PrintWriter(headerfile);
		
        for (int i = 0; i < fields.size(); i++) {
        	outputFile.println(fields.get(i).toUpperCase());
		}
        
        outputFile.close();
        return true;
	}
}
