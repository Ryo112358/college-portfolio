import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ShiftCipher {

	public static void main(String[] args) throws FileNotFoundException {
		
		// Define shift pattern -----------------------------------------------------------------------
		int[] pattern = readPatternFile();
		
		// Example shifts, entire message must be uppercase for complete encoding ---------------------
		System.out.println(shiftString("YFA GU RJGU QQ FCPF.", pattern));
		System.out.println(shiftString("WHY IS THIS SO HARD.", reversePattern(pattern)));
		
		// Example shifts, lowercase characters don't change (use for hints) --------------------------
		System.out.println(shiftString("YFy is VhiQ Uo hYTd.", pattern));
		System.out.println(shiftString("WHy is ThiS So hARd.", reversePattern(pattern)));
	}
	
	public static String shiftString(String message, int[] pattern) {
		
		char[] msg = message.toCharArray();
		
		int[] val = charToAscii(msg);
		asciiToAlphSize(val);
		shiftByExcludeSymbols(val, pattern);
		shiftedToAscii(val);
		msg = asciiToChar(val);
		
		return String.valueOf(msg);
	}
	
	// Helper Methods -------------------------------------------------------
    public static int[] charToAscii(char[] msg) {
    	int[] val = new int[msg.length];
    	
    	for(int i = 0; i < msg.length; i++){
    		val[i] = (int) msg[i];
    	}
    	
        return val;
    }
    
    public static void asciiToAlphSize(int[] values) {
    	
    	for(int i = 0; i < values.length; i++){
    		if(values[i] >= 65 && values[i] <= 90) {
    			values[i] = values[i] - 65;
    		}
    	}
    }

	public static void shiftBy(int[] values, int[] pattern) {
		int shift = 0;
		
		for(int i = 0; i < values.length; i++){
			shift = pattern[i%pattern.length];
			
    		if(values[i] <= 25) {
    			
    			if(shift >= 0) {
        			values[i] = (values[i] + shift)%26;
    			} else {
    				int factor = -1*(shift/26) + 1;
    				values[i] = (values[i] + factor*26 + shift)%26;
    			}
    		}
    	}
	}
	
	public static void shiftedToAscii(int[] values) {
		
		for(int i = 0; i < values.length; i++){
    		if(values[i] <= 25) {
    			values[i] = values[i] + 65;
    		}
    	}
	}
	
	public static char[] asciiToChar(int[] values) {
    	char[] msg = new char[values.length];
    	
    	for(int i = 0; i < values.length; i++){
    		msg[i] = (char) values[i];
    	}
    	
        return msg;
    }
	
	public static void shiftByExcludeSymbols(int[] values, int[] pattern) {
		int shift;
		int count = 0;
		
		for(int i = 0; i < values.length; i++){
			shift = pattern[count%pattern.length];
			
    		if(values[i] <= 25) {
    			
    			if(shift >= 0) {
        			values[i] = (values[i] + shift)%26;
    			} else {
    				int factor = -1*(shift/26) + 1;
    				values[i] = (values[i] + factor*26 + shift)%26;
    			}
    			count++;
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
