import java.io.*;
import java.util.*;


public class FilterTool {
	static Integer _x; //Minimum to detect
		
	public static void main(String[] args) throws IOException {
		//Next 5 lines initialize variables that will be given values later.
		String outputFileName = null;
		
		File outputFile1 = null;
		File outputFile2 = null;
		
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
				/*Note, the output files seem the same, but appending 1 and 2 		 
				 * to the end of the files, differentiate them. Coding Strand (1), Complement Strand (2)
				 */
				outputFile1 = new File(args[i] +"_1.fq");
				outputFile2 = new File(args[i] + "_2.fq");
			} 
			else {
				 printHelp = true;
			}
			i++;
		}
		
		if( args.length < 8 || printHelp ) {
			System.out.println("USAGE: extract -f <first-paired-end-read> -s <second-paired-end-read> -n <number-to-detect> -o <output-file-name>");
			return;
		}
		
		//This sets up the creation of the output files.
		Writer outputWriter1 = new BufferedWriter(new FileWriter(outputFile1));	
		Writer outputWriter2 = new BufferedWriter(new FileWriter(outputFile2));
		
		try {
			
			Scanner codingStrandScanner = new Scanner(codingStrand);
			Scanner complementStrandScanner = new Scanner(complementStrand);
			
			/*This will parse the entire 2 files (considering either of them as the delimiter, in this case 
			 * the complementStrand was chosen to be the delimiter).
			 * Note, each time the loop completes, the scanner will check if there is anything next.
			 * What it will find is the header of read. Thanks to how the scanner is changed inside the loop,
			 * the next time scanner is asked hasNext(), it will be conveniently located next to the next read header.
			 */
			while(complementStrandScanner.hasNext()){
				
				/*This module creates a string called firstRead; which contains a read from the coding Strand;
				* which consists of 4 lines, since this is a fastq type file.
				*It also creates a string called firstSequence; which holds in particular, the bases of the read.
				*/
				String firstSequence = null;
				String firstRead = codingStrandScanner.next();
				firstSequence = codingStrandScanner.next();
				firstRead = firstRead + "\n" + firstSequence + "\n";
				firstRead = firstRead + codingStrandScanner.next() + "\n";
				firstRead = firstRead + codingStrandScanner.next();
				
				//Similar to the prior module, it creates a string called secondRead, and another string called secondSequence.
				String secondSequence = null;
				String secondRead =complementStrandScanner.next();
				secondSequence = complementStrandScanner.next();
				secondRead = secondRead + "\n" + secondSequence + "\n";
				secondRead = secondRead + complementStrandScanner.next() + "\n";
				secondRead = secondRead + complementStrandScanner.next();
				
				
				if (tCounter(secondSequence) >= _x) {
					
					outputWriter1.write(firstRead + "\n");
					outputWriter2.write(secondRead + "\n");
				}
			}
			
		} 
		catch (FileNotFoundException e) {
			e.getMessage();
		}
		
		outputWriter1.close();
		outputWriter2.close();		
	}
	
	/*This counter only counts up to _x. (It's designed only for this program)
	 * This checks the first _x bases, and determines how many of them are Ts.
	 * The idea is to quicken the qualification process. If the first _x bases are not Ts then it certainly isn't >= _x.
	 * However, if the first _x are Ts, then it is >= _x Ts. 
	 * There isn't a need to count anymore, once we know the threshold (which is _x) is reached. 
	 * Note, the threshold has to be met within _x bases; this guarantees the T's are consecutive. 
	 */
	public static int tCounter(String sequence) {
		int count = 0;
		for (int i=0; i<_x ;i++){
			Character b = sequence.charAt(i);
			if (b.equals('T') || b.equals('t')) {
				count++;
			}
		}
		return count;
	}

}