import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class replace {
  String str = "";

	public replace() {
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

		// Put more here.

		String originalString = "AAA";
		
		StringBuilder builder = new StringBuilder();

		for (char currentChar : originalString.toCharArray()) {
			Character replacementChar = charReplacementMap.get(currentChar);
			builder.append(replacementChar != null ? replacementChar
					: currentChar);
		}

		String newString = builder.toString();
		System.out.println(originalString);
		System.out.println(newString);
	}
		
		public static void main(String[] args){
			new replace();
		}

}
