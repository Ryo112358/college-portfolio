/** Finds all well ordered numbers, i.e. numbers where each digit < digit to the right
 * e.g. 1234, 2567, 36789
 * The following numbers are not well ordered: 3267, 9254, 14869
 * 
 * @arg Number of digits number should contain (e.g. between 2-9)
 */

public class WellOrderedNumbers {

	public static void main(String[] args) {

		System.out.println();

		printAllWellOrdered(Integer.parseInt(args[0]));
	}

	/* Print all well ordered numbers of given length
	*/
	public static void printAllWellOrdered(int numDigits) {
		// ASCII | 0-9 are chars 48-57 in Dec

		int i = firstWellOrdered(numDigits);
		int count = 0;

		do {
			System.out.println(i);
			count++;
			i = findNextWellOrdered(i);
		} while (i != 0);

		System.out.printf("%d well ordered numbers w/ %d digits \n", count, numDigits);
	}

	/* Given any number, return the next well ordered number w/ same number of digits
	 * If none left, return 0
	 */
	public static int findNextWellOrdered(int num) {
		char[] number = Integer.toString(num).toCharArray();

		if (num == lastWellOrdered(number.length)) {
			return 0; // Signify all combinations have been found
		}

		// Increment the last digit until maxed
		if (number[number.length-1] != '9') {
			number[number.length-1] += 1;
		} else {
			// Last digit of number is 9, so find next incrementable digit
			for (int i = number.length-1; ;i--) {

				if(number[i] != highestDigitAtPosition(i, number.length)) {

					// Increment the found digit
					number[i] += 1; i++;

					// Adjust all digits after found digit
					for(int j = i; j < number.length; j++)
						number[j] = (char) (number[j-1] + 1);

					break;
				}
			}
		}

		return Integer.parseInt(String.valueOf(number));
	}

	/* Returns true if number is well ordered, false if not
	 */
	public static boolean isWellOrdered(int num) {
		char[] number = Integer.toString(num).toCharArray();

		boolean wellOrdered = true;

		for(int i = 0; i < number.length-1; i++) {
			if(number[i] >= number[i+1]){
				wellOrdered = false;
				break;
			}
		}

		return wellOrdered;
	}

	/* Fill a number containing n digits with a filler digit
	 * e.g. 4 digits w/ filler '5' returns 5555
	 */
	public static int fillWithDigit(int numDigits, char filler) {
		char[] digits = new char[numDigits];
		for(int i = 0; i < numDigits; i++)
			digits[i] = filler;

		return Integer.parseInt(String.valueOf(digits));
	}

	/* Fill a number containing n digits with first well ordered number
	 * e.g. 4 digits returns 1234
	 */
	public static int firstWellOrdered(int numDigits) {
		char[] digits = new char[numDigits];
		for(int i = 0; i < numDigits; i++)
			digits[i] = (char) (i+49);

		return Integer.parseInt(String.valueOf(digits));
	}

	/* Fill a number containing n digits with last well ordered number
	 * e.g. 4 digits returns 6789
	 */
	public static int lastWellOrdered(int numDigits) {
		char[] digits = new char[numDigits];
		for(int i = 0; i < numDigits; i++)
			digits[i] = (char) (i+58-numDigits);

		return Integer.parseInt(String.valueOf(digits));
	}

	public static char highestDigitAtPosition(int index, int numDigits) {
		return (char) (index+58-numDigits);
	}
}
