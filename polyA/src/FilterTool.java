import java.io.*;
import java.util.*;


public class FilterTool {
	int _x;
	
	public FilterTool(String firstEndPath, String secondEndPath, int numberToDetect, String outputFileName) throws IOException {
		
		_x = numberToDetect;
		
		File outputFile1 = new File(outputFileName+"1");
		Writer outputWriter1 = new BufferedWriter(new FileWriter(outputFile1));
		
		File outputFile2 = new File(outputFileName+"2");
		Writer outputWriter2 = new BufferedWriter(new FileWriter(outputFile2));
		
		File firstEnd = new File(firstEndPath);
		File secondEnd = new File(secondEndPath);
		
		Scanner firstEndScanner = new Scanner(firstEnd);
		Scanner secondEndScanner = new Scanner(secondEnd);
		
		
		while(secondEndScanner.hasNext()){
			String secondSequence = null;
			String secondRead =secondEndScanner.next();
			secondSequence = secondEndScanner.next();
			secondRead = secondRead + "\n"+ secondSequence;
			secondRead = secondRead + "\n"+ secondEndScanner.next();
			secondRead = secondRead + "\n"+ secondEndScanner.next();
			
			String firstSequence = null;
				String firstRead =firstEndScanner.next();
				firstSequence = firstEndScanner.next();
				firstRead = firstRead + "\n"+ firstSequence;
				firstRead = firstRead + "\n"+ firstEndScanner.next();
				firstRead = firstRead + "\n"+ firstEndScanner.next();
			
			if (tCounter(secondSequence) == _x) {
				
				outputWriter1.write("\n"+firstRead);
				outputWriter2.write("\n"+secondRead);
			}
		}
		
		
		outputWriter1.close();
		outputWriter2.close();
				
	}
	
	public int tCounter(String sequence) {
		int count = 0;
		for (int i=0; i<_x ;i++){
			Character b = sequence.charAt(i);
			if (b.equals('T')) {
				count++;
			}
		}
		return count;
	}
	
	public static void main(String[] args) throws IOException {
		FilterTool app = new FilterTool("/home/reynaldo/Documents/RegressionTest/RegressionTestForFilterTool/Test_s_6_1_sequence.txt", "/home/reynaldo/Documents/RegressionTest/RegressionTestForFilterTool/Test_s_6_2_sequence.txt", 5, "/home/reynaldo/Desktop/firstTestFile");
	}

}
