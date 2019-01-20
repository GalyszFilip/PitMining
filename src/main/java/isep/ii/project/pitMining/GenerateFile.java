package isep.ii.project.pitMining;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GenerateFile {

	public void generateArray() {
		BufferedWriter out;
		Random rand = new Random();
		int row = rand.nextInt(10) + 5;
		int columns = rand.nextInt(20) + row;
		int range = rand.nextInt(120) + 20;

		try {
			out = new BufferedWriter(new FileWriter("pitMining.txt"));

			for (int j = 0; j < row; j++) {
				String tabString = "";
				for (int i = 0; i < columns; i++) {
					int n = (int) (rand.nextInt(range) - Math.floor(range / 2));
					if (i != columns - 1) {
						tabString += n + " ";
					} else {
						tabString += n;
					}
				}
				out.write(tabString);
				if (j != row - 1)
					out.newLine();
			}
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
