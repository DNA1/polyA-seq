import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import net.sf.samtools.*;
import net.sf.samtools.util.AsciiWriter;
import net.sf.picard.*;
import net.sf.picard.reference.*;
import net.sf.picard.sam.CreateSequenceDictionary;
import net.sf.picard.util.*;

public class polyAextract {
	//--------------Samir--------------
	static String bases;
	static String feed;
	//--------------Samir--------------
	
	public static void main (String[] args) throws IOException {
		File genome = null;
		File polyAfile = null;
		Integer numberToExtract = null;
		Integer tailLength = null; 
		File file = null;
		int i=0;
		boolean printHelp = false;
		while(i<args.length && args[i].charAt(0)=='-') {
			if("-f".equalsIgnoreCase(args[i])) {
				i++;
				genome = new File(args[i]);
			} else if("-b".equalsIgnoreCase(args[i])) {
				i++;
				polyAfile = new File(args[i]); //age.parseInt(args[i]);
			} else if("-l".equalsIgnoreCase(args[i])) {
				i++;
				numberToExtract = numberToExtract.parseInt(args[i]);
			} else if("-a".equalsIgnoreCase(args[i])) {
				i++;
				tailLength = tailLength.parseInt(args[i]);
			} else if("-o".equalsIgnoreCase(args[i])) {
				i++;
				file = new File(args[i]+".fa");
			}
			else {
				 printHelp = true;
			}
			i++;
		}
		if( args.length < 10 || printHelp ) {
			System.out.println("USAGE: extract -f <fasta-file-name> -b <bed-file-name> -l <sequence-length> -a <poly-A-tail-length> -o <output-file-name>");
			return;
		}		
		
		//This retrieves the .fai file, given the .fa file.
		IndexedFastaSequenceFile seqFile = new IndexedFastaSequenceFile(genome);
		//This was left like this just for instantiation
		ReferenceSequence refSeq = null;
		//Reading from the .bed file; which contains the Poly-A sites.
		BasicInputParser polyAsites = new BasicInputParser(true, polyAfile);
		//These 3 variables will serve as inputs in the getSubsequenceAt method above.
		String chromosome = "";
		int start = 0;
		int end = 1;
		int extract = numberToExtract - 1;
		String header = "";
		String tail = tailMaker(tailLength);
		//-------------------------Reynaldo--------------------------------
		String minus = "-";
		boolean isMinus = false;
		//-------------------------Reynaldo--------------------------------
		//These next two lines are responsible for making the file.
        Writer writer = new BufferedWriter(new FileWriter(file));
        //This is used to make the file start off from the first line, rather than starting off on the second line.
        int indicator = 0;
        try {
	        //while (polyAsites.hasNext()) {
        	for (int k=0; k < 4; k++) { //This is just for testing purposes
				//This moves onto the next line in the poly-A site file.
				polyAsites.next();
				//This Scanner reads the line.
				Scanner myScanner = new Scanner(polyAsites.getCurrentLine());
				chromosome = myScanner.next();
				int chromosomeSize = seqFile.getSequence(chromosome).length();
				//This small loop puts the scanner in position to detect if were on the positive or negative strand.
				for (int m=0; m<4 ;m++){
					myScanner.next();
				}
				//-------------------------Reynaldo--------------------------------
				if (myScanner.next().equalsIgnoreCase("-")) { //If the last column contains a "-".
					myScanner= new Scanner(polyAsites.getCurrentLine()); //To reset the scanner to the beginning of the line.
					myScanner.next(); //To skip first column in bed file.
					start = myScanner.nextInt() + 1;
					end = start + extract;
					isMinus = true;
					//Truncates if exceeds bounds
					if (start < 0) {
						start = 1;
					}
					if (end > chromosomeSize){
						end = chromosomeSize;
					}
				}
				else { //If the last column didn't contain a minus, then it must have a "+".
					myScanner= new Scanner(polyAsites.getCurrentLine()); //To reset the scanner to the beginning of the line.
					myScanner.next(); //To skip first column in bed file.
					end = myScanner.nextInt() + 1;
					start = end - extract;
					isMinus = false;
					//Truncates if exceeds bounds
					if (start < 0) {
						start = 1;
					}
					if (end > chromosomeSize){
						end = chromosomeSize;
					}
				}
				//-------------------------Reynaldo--------------------------------
				refSeq = seqFile.getSubsequenceAt(chromosome, start, end);
				bases = new String(refSeq.getBases());
				// -------------------------Samir---------------------------------
				String strand;
				String fwd;
				String nuBases;
				for (int j = 0; j < 4; j++) {
					strand = myScanner.next();
					fwd = "-";
					if (j >= 2 && strand.equals(fwd)) {
						//System.out.println(strand + "original strand:	"); //Just for Testing
						seq();
						//nuBases = bases.replace(bases, feed );
					}
					
				}
				
				// -----------------------Samir-------------------------------
				//-------------------------Reynaldo--------------------------------
				// This part makes the .fa file if the stand is '-'
				if (isMinus){
					if (chromosome.equals(header)) {
						writer.write(">" + header + ":" + start + "-" + end+ "+" + tailLength + "A" + "\n");
						writer.write(feed + tail + "\n");
						System.out.println(">" + header + ":" + start + "-" + end+ "+" + tailLength + "A"); // Testing purposes
						System.out.println(feed + tail); // Testing purposes
					} else {
						header = chromosome;
						writer.write(">" + header + ":" + start + "-"+ end + "+" + tailLength + "A" + "\n");
						writer.write(feed + tail + "\n");
						System.out.println(">" + header + ":" + start + "-"+ end + "+" + tailLength + "A"); // Testing purposes
						System.out.println(feed + tail); // Testing purposes
					}
				}
				//-------------------------Reynaldo--------------------------------
				//-------------------------Reynaldo--------------------------------
				// This part makes the .fa file if the strand is '+'
				else {
					if (chromosome.equals(header)) {
						writer.write(">" + header + ":" + start + "-" + end+ "+" + tailLength + "A" + "\n");
						writer.write(bases + tail + "\n");
						System.out.println(">" + header + ":" + start + "-" + end+ "+" + tailLength + "A"); // Testing purposes
						System.out.println(bases + tail); // Testing purposes
					} 
					else {
						header = chromosome;
						writer.write(">" + header + ":" + start + "-" + end+ "+" + tailLength + "A" + "\n");
						writer.write(bases + tail + "\n");
						System.out.println(">" + header + ":" + start + "-"+ end + "+" + tailLength + "A"); // Testing
						System.out.println(bases + tail); // Testing purposes
						}
					}
				}
				//-------------------------Reynaldo--------------------------------
	        writer.close();
	        polyAsites.close();
        }
        
        catch (net.sf.picard.PicardException e) {
        	System.out.println(e.getMessage());
        	writer.close();
        	//System.out.println(e.toString()); //For error detection
        }
               
	}
	
	// ------------------------------Samir------------------------------------

	// String line = null;
	
	public static char replace(char in) {
		Map<Character, Character> charReplacementMap = new HashMap<Character, Character>();

		charReplacementMap.put('G', 'C');
		charReplacementMap.put('C', 'G');
		charReplacementMap.put('A', 'T');
		charReplacementMap.put('T', 'A');

		charReplacementMap.put('a', 't');
		charReplacementMap.put('t', 'a');
		charReplacementMap.put('c', 'g');
		charReplacementMap.put('g', 'c');

		charReplacementMap.put('R', 'Y');
		charReplacementMap.put('Y', 'R');
		charReplacementMap.put('K', 'M');
		charReplacementMap.put('M', 'K');
		charReplacementMap.put('S', 'W');
		charReplacementMap.put('W', 'S');
		
		charReplacementMap.put('N', 'N');
		charReplacementMap.put('-', '-');
		
		char complement =  charReplacementMap.get(in);
		return complement;
	}
	
	public static void seq() {
		String in= bases;
		final StringBuilder b = new StringBuilder(in.length());
		for (int i = in.length(); i > 0; i--)
			b.append(replace(in.charAt(i - 1)));
		feed = b.toString();
				
		//System.out.println(feed); //(Reynaldo)We can comment this out, but leave it like this for testing.
				
				
	}
	
	// --------------------------------Samir----------------------------------
	
	public static String tailMaker(int tailLength) {
		String tail = "";
		for (int i=0; i <tailLength ; i++) {
			tail = tail + "A"; 
		}
		return tail;
	}

}
