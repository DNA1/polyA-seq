import java.io.*;
import java.util.*;

public class FilterTool2 {
	int _x; //This is the minimum to detect
	int _y; //This is the maximum to detect.
	
	public FilterTool2(String aCodingStrand, String aComplementStrand, int min, String outputFileName) throws IOException {
		
		_x = min;
		
		/*The following 6 lines initialize the creation of the output Files.
		 * Notice that the output file names are the same, except for the addition of a 1, 2, and "Case4Reads" differentiate
		 * between the coding (1) and complement (2) strand, and a file that has Case 4 type reads.
		 */
		File outputFile1 = new File(outputFileName+"1");
		Writer outputWriter1 = new BufferedWriter(new FileWriter(outputFile1));
		
		File outputFile2 = new File(outputFileName+"2");
		Writer outputWriter2 = new BufferedWriter(new FileWriter(outputFile2));
		
		File outputFile3 = new File(outputFileName+"Case4Reads");
		Writer outputWriter3 = new BufferedWriter(new FileWriter(outputFile3));
		
		/*The following 2 lines create the given strands into File types so that they may be accepted
		 * as input for the Scanner class.
		 */
		File codingStrand = new File(aCodingStrand);
		File complementStrand = new File(aComplementStrand);
		
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
			if (this.tCounter(secondSequence) <= _y) {
				//This block completes the complement strand read.
				secondSequence = trimTs(secondSequence);
				secondRead = secondRead + "\n" + secondSequence + "\n";
				secondRead = secondRead + complementStrandScanner.next() + "\n";
				secondRead = secondRead + complementStrandScanner.next();
				
				//This block completes the coding strand read.
				firstRead = firstRead + "\n" + firstSequence + "\n"; //The second line of a fastq file contains the bases; which is why firstSequence is inserted here.
				firstRead = firstRead + codingStrandScanner.next() + "\n"; //This adds the third line
				firstRead = firstRead + codingStrandScanner.next(); //This adds the quality line score
				
				//These 2 lines add the firstRead, and secondRead to the output files.
				outputWriter1.write(firstRead + "\n");
				outputWriter2.write(secondRead + "\n");
			}
			
			//This will occur only if secondSequence has >y Ts, and firstSequence has >=x and <= y As.
			else if (this.aCounter(firstSequence) >=_x && this.aCounter(firstSequence) <=_y) {
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
	public int tCounter(String sequence) {
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
	public int aCounter(String sequence) {
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
	public String trimTs(String sequence){
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
	
	public String trimAs(String sequence) {
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
	
	public static void main(String[]args) throws IOException {
		FilterTool2 app = new FilterTool2("/home/reynaldo/Documents/RegressionTest/RegressionTestForFilterTool2/Test_s_6_1_sequence.txt", "/home/reynaldo/Documents/RegressionTest/RegressionTestForFilterTool2/Test_s_6_2_sequence.txt", 5, "/home/reynaldo/Desktop/secondTestFile");
	}
}
