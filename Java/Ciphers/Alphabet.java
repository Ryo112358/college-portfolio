import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Alphabet {
	
	private int id;
    private int alphabetSize;    
    private char[] customAlphabet;
    
    /**
     * Use empty constructor to read alphabet from "alphabet.txt" file. Alphabet file must
     * have each character on a separate line.
     * @throws FileNotFoundException Make sure the "alphabet.txt" exists
     */
    public Alphabet() throws FileNotFoundException {
    	readAlphabetFile();
    	appendSymbols(true);
    }
    
    public Alphabet(String symbolset) {
    	symbolset = symbolset.toLowerCase();
    	
    	if(symbolset.equals("english")){
    		// Roman
    		id = 1;
    		alphabetSize = 26;
    		customAlphabet = new char[26+39];
    		int index = 0;
    		
    		// A-Z (26)
    		for(int i = 65; i <= 90; i++){
        		customAlphabet[index] = (char) i;
        		index++;
        	}
    		appendSymbols(true);
    		
    	} else if(symbolset.equals("hexadecimal")) {
    		// Hexadecimal 0-9, A-F
    		id = 2;
    		alphabetSize = 16;
    		customAlphabet = new char[16+29];
    		int index = 0;
    		
    		// 0-9 (10)
    		for(int i = 48; i <= 57; i++){
        		customAlphabet[index] = (char) i;
        		index++;
        	}
    		// A-F (6)
    		for(int i = 65; i <= 70; i++){
        		customAlphabet[index] = (char) i;
        		index++;
        	}
    		appendSymbols(false);
    		
    	} else {
    		// ASCII
    		id = 0;
    		alphabetSize = 256;
    		customAlphabet = new char[256];
    		for(int i = 0; i < 256; i++){
    			customAlphabet[i] = (char) i;
    		}
    	}
    }
    
    public int getIndexOf(char c) {
    	for(int i = 0; i < customAlphabet.length; i++) {
    		if(customAlphabet[i] == c) {
    			return i;
    		}
    	}
    	
    	throw new NoSuchElementException("Could not find \"" + c + "\" in alphabet.");
    }
    
    public int getID() {
    	return id;
    }
    
    public int getSize() {
    	return alphabetSize;
    }
    
    public char getIndex(int i) {
    	return customAlphabet[i];
    }
    
    public char[] getAlphabet() {
    	return customAlphabet;
    }
    
    public String toString() {
    	return Arrays.toString(customAlphabet);
    }
    
    private void readAlphabetFile() throws FileNotFoundException {
    	int numElements = 0;
    	
    	Scanner alph = new Scanner(new File("alphabet.txt"));
    	
    	// Count number of elements in alphabet ----------------
    	while(alph.hasNextLine()) {
    		alph.nextLine();
    		numElements++;
    	}
    	
    	alphabetSize = numElements;
    	alph = new Scanner(new File("alphabet.txt"));
    	
    	// Create alphabet character array ----------------------
    	char[] alphabet = new char[numElements+39];
    	
    	for(int i = 0; alph.hasNextLine(); i++) {
    		alphabet[i] = alph.nextLine().charAt(0);
    	}
    	alph.close();
    	
    	customAlphabet = alphabet;
    	//System.out.println(Arrays.toString(alphabet));
    }
    
    /**
     * Append the 39 (or 29) common symbols to the alphabet array after the main alphabet.
     * @param numbers Whether or not to add numbers to symbols section of array
     */
    private void appendSymbols(boolean numbers) {
    	int index = alphabetSize;
    	
    	// Symbols (16)
		for(int i = 32; i <= 47; i++){
    		customAlphabet[index] = (char) i;
    		index++;
    	}
		if(numbers) {
			// Numbers 0-9 (10)
			for(int i = 48; i <= 57; i++){
	    		customAlphabet[index] = (char) i;
	    		index++;
	    	}
		}
		// More Symbols (7)
		for(int i = 58; i <= 64; i++){
    		customAlphabet[index] = (char) i;
    		index++;
    	}
		// More Symbols (6)
		for(int i = 91; i <= 96; i++){
    		customAlphabet[index] = (char) i;
    		index++;
		}
    }
}
