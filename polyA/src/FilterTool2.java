import java.io.*;
import java.util.*;

public class FilterTool2 {
	static Integer _x; //This is the minimum to detect
	static Integer _y; //This is the maximum to detect.
	
	public static void main(String[] args) throws IOException {
		//Next 6 lines initialize variables that will be given values later.
		String outputFileName = null;
		
		File outputFile1 = null;
		File outputFile2 = null;
		File outputFile3 = null;
		
		File codingStrand = null;
		File complementStrand = null;
		
		int i=0;
		boolean printHelp = false;
		while(i<args.length && args[i].charAt(0)=='-') {
			if("-f".equalsIgnoreCase(args[i])) {
				i++;
				codingStrand = new File(args[i]);
			} else if("-s".equalsIgnoreCase(args[i])) {
				i++;
				complementStrand = new File(args[i]); //age.parseInt(args[i]);
			} else if("-n".equalsIgnoreCase(args[i])) {
				i++;
				_x = _x.parseInt(args[i]);
			} else if("-o".equalsIgnoreCase(args[i])) {
				i++;
				/*Note, the output files seem the same, but appending 1, 2, and "_Case4Reads" 		 
				 * to the end of the files, differentiate them. Coding Strand (1), Complement Strand (2), and file that
				 * contains case 4 reads ("_Case4Reads").
				 */
				outputFile1 = new File(args[i] +"1");
				outputFile2 = new File(args[i] + "2");
				outputFile3 = new File(args[i] + "_Case4Reads");
			} 
			else {
				 printHelp = true;
			}
			i++;
		}
		
		if( args.length < 8 || printHelp ) {
			System.out.println("USAGE: extract -f <first-paired-end-read> -s <second-paired-end-read> -n <min-number-to-detect> -o <output-file-name>");
			return;
		}
		
		//This sets up the creation of the output files.
		Writer outputWriter1 = new BufferedWriter(new FileWriter(outputFile1));	
		Writer outputWriter2 = new BufferedWriter(new FileWriter(outputFile2));
		Writer outputWriter3 = new BufferedWriter(new FileWriter(outputFile3));
		
		//This instantiates the scanners that will parse the two files.
		Scanner codingStrandScanner = new Scanner(codingStrand);
		Scanner complementStrandScanner= new Scanner(complementStrand);
		
		while (complementStrandScanner.hasNext()) {
			
			/*This module creates a string called firstRead; which contains a read from the coding Strand;
			* which consists of 4 lines, since this is a fastq type file.
			*It also creates a string called firstSequence; which holds in particular, the bases of the read.
			*/
			String firstSequence = null;
			String firstRead = codingStrandScanner.next(); //This will add the header.
			firstSequence = codingStrandScanner.next(); //This variable called firstSequence will contain the bases.
			//The rest of the read is conditionally made. You can find the rest of it in the if and else if statements.
			
			/*This module creates a string called secondRead; which contains a read from the complement Strand.
			 * It also creates a string called firstSequence; which holds in particular, the bases of the read.
			 */
			String secondSequence = null;
			String secondRead = complementStrandScanner.next();
			secondSequence = complementStrandScanner.next();
			//The rest of this read is conditionally made. You can find the rest of it in the if and else if statements.
			_y = secondSequence.length() - 20;
			
			//This part trims T's if the complement strand read has <=y Ts
			if (tCounter(secondSequence) <= _y) {
				//This block completes the complement strand read.
				secondSequence = trimTs(secondSequence);
				secondRead = secondRead + "\n" + secondSequence + "\n"; //The second line of a fastq file contains the bases; which is why firstSequence is inserted here.
				secondRead = secondRead + complementStrandScanner.next() + "\n"; //This adds the third line
				secondRead = secondRead + complementStrandScanner.next(); //This adds the quality line score
				
				//This block completes the coding strand read.
				firstRead = firstRead + "\n" + firstSequence + "\n"; 
				firstRead = firstRead + codingStrandScanner.next() + "\n";
				firstRead = firstRead + codingStrandScanner.next();
				
				//These 2 lines add the firstRead, and secondRead to the output files.
				outputWriter1.write(firstRead + "\n");
				outputWriter2.write(secondRead + "\n");
			}
			
			//This will occur only if secondSequence has >y Ts, and firstSequence has >=x and <= y As.
			else if (aCounter(firstSequence) >=_x && aCounter(firstSequence) <=_y) {
				//1.This will just advance the scanner's position, so its in the correct position for the next loop.
				//The complement strand read will not be written to a file. It is dropped.
				complementStrandScanner.next();
				complementStrandScanner.next();
				
				//This block Trims As from end of first sequence, generates the reast of the coding strand read (which prepares it to be written to the file).
				firstSequence = trimAs(firstSequence);
				firstRead = firstRead + "\n" + firstSequence + "\n"; //The second line of a fastq file contains the bases; which is why firstSequence is inserted here.
				firstRead = firstRead + codingStrandScanner.next() + "\n"; //This adds the third line
				firstRead = firstRead + codingStrandScanner.next(); //This adds the quality line score
				
				//This will write to the file that contains Case 4 reads.
				//Notice, only the coding strand read is saved, the complement strand read is dropped.
				outputWriter3.write(firstRead + "\n");
			}
			//This will just advance the scanner's position, so its in the correct position for the next loop.
			else {
				codingStrandScanner.next();
				codingStrandScanner.next();
				
				complementStrandScanner.next();
				complementStrandScanner.next();
			}
		}
		
		outputWriter1.close();
		outputWriter2.close();
		outputWriter3.close();
	}
	
	/*This method counts how many consecutive T's (despite capitalization) there are in 
	*the beginning of the complement strand.
	*/
	public static int tCounter(String sequence) {
		int count = 0;
		int i = 0;
		Character base = sequence.charAt(i);
		//This will keep incrementing the count, until the next character (base) doesn't equal T or t.
		while (base.equals('T') || base.equals('t')) {
			count++;
			i++;
			base = sequence.charAt(i);
		}
		return count;
	}
	
	//Counts how many consecutive As (despite capitalization) are at the end of a coding Strand.
	public static int aCounter(String sequence) {
		int count = 0;
		int i = sequence.length() - 1; //Will serve as character index. This index is the end of the String. Since index is 0 based, we must subtract 1.
		Character base = sequence.charAt(i);
		while (base.equals('A') || base.equals('a')){
			i--;
			count++;
			base = sequence.charAt(i);
		}
		return count;
	}
	
	//This method trims T's from a complement strand.
	public static String trimTs(String sequence){
		String read = "";
		int i = 0;
		Character base = sequence.charAt(i);
		//This loop will stop when the next character is not a T.
		while (base.equals('T') || base.equals('t')){
			i++;
			base = sequence.charAt(i);
		}
		/*This loop will continue where the other loop left off (notice j = i). This means the
		 * character that wasn't equal to T or t (which caused the prior loop to end) is used here to continue.
		 * This loop will save the rest of the characters (bases) in the string (sequence).
		 */
		for (int j = i; j<sequence.length(); j++){
			read = read + base;
			base = sequence.charAt(j);
		}
		return read;
	}
	
	public static String trimAs(String sequence) {
		String read = "";
		int i = sequence.length() - 1;
		Character base = sequence.charAt(i);
		//This loop will stop when the next character is not an A.
		while (base.equals('A') || base.equals('a')) {
			i--;
			base = sequence.charAt(i);
		}
		/*This loop will continue where the other loop left off (notice j = i). This means the
		 * character that wasn't equal to A or a (which caused the prior loop to end) is used here to continue.
		 * This loop will save the rest of the characters (bases) in the string (sequence).
		 */
		for (int j = i; j >= 0; j--) {
			read = base + read;
			base = sequence.charAt(j);
		}
		return read;
	}
}
