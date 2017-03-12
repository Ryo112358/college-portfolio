import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ShiftCipher_Custom {

	public static void main(String[] args) throws FileNotFoundException {
		
		// Define custom alphabet ----------------------------
		//Alphabet english = new Alphabet();
		Alphabet english = new Alphabet("English");
		
		// Define pattern ------------------------------------
		//int[] pattern = readPatternFile();
		int[] pattern = {-5, 7, 13, -19};
		
		// Example shifts ------------------------------------
		System.out.println(shiftString("why IS this So HaRd.", pattern, english));
		System.out.println(shiftString("ROL PN AUPN ZB OVYQ.", reversePattern(pattern), english));
		
		
		Alphabet hex = new Alphabet("Hexadecimal");
		System.out.println(hex.toString());
	}
	
	public static String shiftString(String message, int[] pattern, Alphabet alph) {
		
		// If English, make message uppercase
		if(alph.getID() == 1){
			message = message.toUpperCase();
		}
		
		char[] msg = message.toCharArray();
		
		int[] val = charToInt(msg, alph);
		shiftByExcludeSymbols(val, pattern, alph.getSize());
		msg = intToChar(val, alph);
		
		return String.valueOf(msg);
	}
	
	// Helper Methods -------------------------------------------------------
    public static int[] charToInt(char[] msg, Alphabet alph) {
    	int[] val = new int[msg.length];
    	
    	for(int i = 0; i < msg.length; i++){
    		val[i] = alph.getIndexOf(msg[i]);
    	}
    	
        return val;
    }
	
	public static char[] intToChar(int[] values, Alphabet alph) {
    	char[] msg = new char[values.length];
    	
    	for(int i = 0; i < values.length; i++){
    		msg[i] = alph.getIndex(values[i]);
    	}
    	
        return msg;
    }
	
	public static void shiftByExcludeSymbols(int[] values, int[] pattern, int alphSize) {
		int shift;
		int count = 0;
		
		for(int i = 0; i < values.length; i++){
			shift = pattern[count%pattern.length];
			
    		if(values[i] <= alphSize-1) {
    			
    			if(shift >= 0) {
        			values[i] = (values[i] + shift)%alphSize;
    			} else {
    				int factor = -1*(shift/26) + 1;
    				values[i] = (values[i] + factor*26 + shift)%alphSize;
    			}
    			count++;
    		}
    	}
	}
	
	public static void shiftBy(int[] values, int[] pattern, int alphSize) {
		int shift = 0;
		
		for(int i = 0; i < values.length; i++){
			shift = pattern[i%pattern.length];
			
    		if(values[i] <= alphSize-1) {
    			
    			if(shift >= 0) {
        			values[i] = (values[i] + shift)%alphSize;
    			} else {
    				int factor = -1*(shift/alphSize) + 1;
    				values[i] = (values[i] + factor*alphSize + shift)%alphSize;
    			}
    		}
    	}
	}
	
	public static int[] reversePattern(int[] pattern) {
		int[] newPattern = new int[pattern.length];
		
		for(int i = 0; i < pattern.length; i++){
			newPattern[i] = -pattern[i];
		}
		
		return newPattern;
	}
	
	public static int[] readPatternFile() throws FileNotFoundException {
		int numElements = 0;
		
		// Determine pattern length ----------------
		Scanner parsePattern = new Scanner(new File("pattern.csv"));
		parsePattern.useDelimiter(",");
		while(parsePattern.hasNext()) {
			parsePattern.next();
			numElements++;
		}
		parsePattern.close();
		
		// Read in pattern -------------------------
		int[] pattern = new int[numElements];
		
		parsePattern = new Scanner(new File("pattern.csv"));
		parsePattern.useDelimiter(",|\r\n");
		
		for(int i = 0; parsePattern.hasNext(); i++) {
			pattern[i] = Integer.parseInt(parsePattern.next());
		}
		parsePattern.close();
		
		return pattern;
	}
}
