
public class CaesarianShift {

	public static void main(String[] args) {

		String message = "Decode me!";
		
		System.out.println(shiftString(message, 5));
		System.out.println(shiftString(shiftString(message, 5), (-26*37)-5));
		System.out.println(shiftString(shiftString(message, 5), 21));
		
		System.out.println();
		
		listAllShifts("Penguins are cool");

	}
	
	/**
	 * This method shifts the characters of a message by a specified value and returns the shifted String.
	 * @param message String to be shifted by cipher
	 * @param shift Integer specifying shift value
	 * @return Shifted String object
	 */
	public static String shiftString(String message, int shift) {
		
		char[] msg = message.toUpperCase().toCharArray();
		
		int[] val = charToAscii(msg);
		asciiToAlphSize(val);
		shiftBy(val, shift);
		shiftedToAscii(val);
		msg = asciiToChar(val);
		
		return String.valueOf(msg);
		
	}
	
	public static void listAllShifts(String message) {
		
		for(int i = 1; i <= 25; i++){
			System.out.println("+" + i + ": " + shiftString(message, i));			
		}
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

	public static void shiftBy(int[] values, int shift) {
		
		for(int i = 0; i < values.length; i++){
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
	
}
