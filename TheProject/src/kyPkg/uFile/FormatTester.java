package kyPkg.uFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class FormatTester {
	public static void main(String[] args) {
		String filename = args[0];
		try {
			File file = new File(filename);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			int i = 1;
			while ((line = br.readLine()) != null) {
				System.out.format("Line %d: %s%n", i++, line);
			}
			br.close();
		} catch (Exception e) {
			System.err.format("Unable to open file named '%s': %s", filename,
					e.getMessage());
		}
	}
}
