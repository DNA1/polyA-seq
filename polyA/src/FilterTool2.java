import java.io.*;
import java.util.*;

public class FilterTool2 {
	int _x; //This is the minimum to detect
	int _y; //This is the maximum to detect.
	
	public FilterTool2(String aCodingStrand, String aComplementStrand, int min, String outputFileName) throws IOException {
		
		_x = min;
		
		/*The following 4 lines initialize the creation of the output Files.
		 * Notice that the output file names are the same, except for the addition of a 1 and 2 differentiate
		 * between the coding (1) and complement (2) strand.
		 */
		File outputFile1 = new File(outputFileName+"1");
		Writer outputWriter1 = new BufferedWriter(new FileWriter(outputFile1));
		
		File outputFile2 = new File(outputFileName+"2");
		Writer outputWriter2 = new BufferedWriter(new FileWriter(outputFile2));
		
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
			firstRead = firstRead + "\n" + firstSequence + "\n"; //The second line of a fastq file contains the bases; which is why firstSequence is inserted here.
			firstRead = firstRead + codingStrandScanner.next() + "\n"; //This adds the third line
			firstRead = firstRead + codingStrandScanner.next(); //This adds the quality line score
			
			/*This module creates a string called secondRead; which contains a read from the complement Strand.
			 * Note, [Finish note; mention the following: how this read is written is conditional]
			 */
			String secondSequence = null;
			String secondRead = complementStrandScanner.next();
			secondSequence = complementStrandScanner.next();
			_y = secondSequence.length() - 20;
			//This part trims T's if the read has <=y Ts
			if (tCounter(secondSequence) <= _y) {
				secondSequence = trimTs(secondSequence);
				secondRead = secondRead + "\n" + secondSequence + "\n";
				secondRead = secondRead + complementStrandScanner.next() + "\n";
				secondRead = secondRead + complementStrandScanner.next();
				
				//These 2 lines add the firstRead, and secondRead to the output files.
				outputWriter1.write(firstRead + "\n");
				outputWriter2.write(secondRead + "\n");
			}
			//This will occur only if secondSequence has >y Ts.
			else if (/*firstSequence has >=x As and <=y As*/ /*The "true" value is temporary*/true) {
				/*1.Drop second read
				 * 2.Trim As from the end of the coding strand read, and save first read
				 * I think the last thing is strange. If we drop the second read, the two output files will have unbalanced pairs.
				 */
			}
		}
		
		outputWriter1.close();
		outputWriter2.close();
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
			//Do not add this character to the read
			i++;
			base = sequence.charAt(i);
		}
		/*This loop will continue where the other loop left off (notice j = i). This means the
		 * character that wasn't equal to T or t (which cause the prior loop to end) is used here to continue.
		 * This loop will save the rest of the characters (bases) in the string (sequence).
		 */
		for (int j = i; j<sequence.length(); j++){
			read = read + base;
			base = sequence.charAt(i);
		}
		return read;
	}
	
	public static void main(String[]args) throws IOException {
		FilterTool2 app = new FilterTool2("/home/reynaldo/Documents/RegressionTest/RegressionTestForFilterTool2/Test_s_6_1_sequence.txt", "/home/reynaldo/Documents/RegressionTest/RegressionTestForFilterTool2/Test_s_6_2_sequence.txt", 5, "/home/reynaldo/Desktop/secondTestFile");
	}
}
