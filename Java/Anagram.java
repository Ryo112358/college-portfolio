import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/** Provided a phrase, program generates all legal permutations
 *     - Only lowercase letters are shifted, all others stay in place
 * @author 	Ryo112358
 * @arg 	Word or phrase enclosed in double quotation marks (e.g. "Hello")
 * 
 *                     java Anagram "HElLO woRLd!"
 *
 * Phrase to permutate: HElLO woRLd!
 * HElLO woRLd!
 * HElLO wdRLo!
 * HElLO owRLd!
 * HElLO odRLw!
 * HElLO doRLw!
 * HElLO dwRLo!
 * HEwLO loRLd!
 * HEwLO ldRLo!
 * HEwLO olRLd!
 * HEwLO odRLl!
 * HEwLO doRLl!
 * HEwLO dlRLo!
 * HEoLO wlRLd!
 * HEoLO wdRLl!
 * HEoLO lwRLd!
 * HEoLO ldRLw!
 * HEoLO dlRLw!
 * HEoLO dwRLl!
 * HEdLO woRLl!
 * HEdLO wlRLo!
 * HEdLO owRLl!
 * HEdLO olRLw!
 * HEdLO loRLw!
 * HEdLO lwRLo!
 */
public class Anagram {

	public static void main(String[] args) {

		// Tests ---------------------------

		final boolean RUNTESTS = true;

		if(!RUNTESTS){
			manualTests();
		}

		// Main Program --------------------

		char[] phrase = args[0].toCharArray();
		boolean[] moveableChar = new boolean[phrase.length];

		System.out.println("\nPhrase to permutate: "  + args[0]);

		short[] indicesToPermute = parsePhrase(phrase, moveableChar, false);
		permute(indicesToPermute, 0, args[0], moveableChar);
	}

	// Helper methods --------------------------------------------------------

	/** Recursively builds array from first to last index of moveable indices, printing each permutation
	 * @param arr 					Array of movable indices
	 * @param index 				Start at 0
	 * @param phrase 				User provided phrase as String
	 * @param isMoveableElement 	Boolean[] which stores whether each char can be moved
	 */
	private static void permute(short[] arr, int index, String phrase, boolean[] isMoveableElement){

		if(index >= arr.length - 1){ // At the last element

			// Print array of permutated indices
			// System.out.println(Arrays.toString(arr));

			// Print complete, assembled phrase
			System.out.println(reassemble(phrase, arr, isMoveableElement, false));
			return;
		}

		short tempShort = 0;

		for(int i = index; i < arr.length; i++){ // For each index in the sub array arr[index...end]

			// Swap the elements at indices index and i
			tempShort = arr[index];
			arr[index] = arr[i];
			arr[i] = tempShort;

			// Recurse on the sub array arr[index+1...end]
			permute(arr, index+1, phrase, isMoveableElement);

			// Swap the elements back
			tempShort = arr[index];
			arr[index] = arr[i];
			arr[i] = tempShort;
		}
	}

	/** Find indices of moveable characters in a given phrase
	 * @param phrase 	Phrase in the form of char[]
	 * @param test 		If true, method will print output to terminal
	 * @return			Short[] of indices with moveable characters
	 */
	public static short[] parsePhrase(char[] phrase, boolean[] moveable, boolean test){
		// ASCII a-z | 97-122
		short[] moveableIndices = new short[countSwitchableCharacters(phrase, false)];
		short currentIndex = 0;

		// Find indices of moveable characters
		for(short i = 0; i < phrase.length; i++) {

			if(phrase[i] > 96 && phrase[i] < 123){
				moveableIndices[currentIndex] = i;
				currentIndex += 1;
				moveable[i] = true; // Declare character moveable
			} else {
				moveable[i] = false; // Declare character static
			}
		}

		if(test) {
			System.out.println("Char moveable? " + Arrays.toString(moveable));
			System.out.println("Indices of moveable: " + Arrays.toString(moveableIndices));
		}

		return moveableIndices;
	}

	/** Assemble shuffled indices back into String
	 * @param phrase 				User entered string
	 * @param shuffled 				Short[] of shuffled indices
	 * @param isMoveableElement 	Boolean[] which stores whether each chars can be moved
	 * @param test 					If true, method will print output to terminal
	 * @return Assembled string
	 */
	public static String reassemble(String phrase, short[] shuffled, boolean[] isMoveableElement, boolean test){
		char[] assembled = new char[isMoveableElement.length];
		int indexShuffled = 0;

		for(int i = 0; i < isMoveableElement.length; i++){
			if(isMoveableElement[i] == true) {
				assembled[i] = phrase.charAt(shuffled[indexShuffled]);
				indexShuffled += 1;
			} else {
				assembled[i] = phrase.charAt(i);
			}
		}

		if(test)
			System.out.println(phrase + " -> " + new String(assembled));

		return new String(assembled);
	}

	/** Count moveable characters in a given phrase
	 * @param phrase 	Phrase in the form of char[]
	 * @param test 		If true, method will print output to terminal
	 * @return 			Number of moveable characters in phrase (i.e. lowercase letters only)
	 */
	public static int countSwitchableCharacters(char[] phrase, boolean test){
		int count = 0;

		for(int i = 0; i < phrase.length; i++) {
			if(phrase[i] > 96 && phrase[i] < 123)
				count += 1;
		}

		if(test)
			System.out.println(count + " moveable characters");

		return count;
	}

	/** Shuffle array of shorts
	 * @param arr 	Array of shorts
	 * @param test 	If true, method will print output to terminal
	 */
	public static void shuffle(short[] arr, boolean test) {
		Random rand = ThreadLocalRandom.current();

		int index = 0;
		short tempShort = 0;

		for(int i = arr.length; i > 0; i--) {
			index = rand.nextInt(i);

			tempShort = arr[index];
			arr[index] = arr[i-1];
			arr[i-1] = tempShort;
		}

		if(test)
			System.out.println("Shuffled:            " + Arrays.toString(arr));
	}

	// Test Method ------------------------------------------------------------

	public static void manualTests(){
		System.out.println("\nMethod tests below: \n\n");

		String testString1 = "Hello";
		String testString2 = "This is @ test!";
		String testString3 = "ShouLd cOuNt #7";

		boolean[] moveableChar1 = new boolean[testString1.length()];
		boolean[] moveableChar2 = new boolean[testString2.length()];
		boolean[] moveableChar3 = new boolean[testString3.length()];

		countSwitchableCharacters(testString1.toCharArray(), true);
		countSwitchableCharacters(testString2.toCharArray(), true);
		countSwitchableCharacters(testString3.toCharArray(), true);
		System.out.println();
		short[] moveableIndices1 = parsePhrase(testString1.toCharArray(), moveableChar1, true);
		short[] moveableIndices2 = parsePhrase(testString2.toCharArray(), moveableChar2, true);
		short[] moveableIndices3 = parsePhrase(testString3.toCharArray(), moveableChar3, true);
		System.out.println();
		shuffle(moveableIndices1, true);
		shuffle(moveableIndices2, true);
		shuffle(moveableIndices3, true);
		System.out.println();
		reassemble(testString1, moveableIndices1, moveableChar1, true);
		reassemble(testString2, moveableIndices2, moveableChar2, true);
		reassemble(testString3, moveableIndices3, moveableChar3, true);
	}
}
