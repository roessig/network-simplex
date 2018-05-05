import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Input {

	private int nodeNumber;
	private int arcNumber;
	private Map<Integer, Long> specialNodes;


	Input(String filename) {

		File f = new File(filename);

		nodeNumber = 0;
		arcNumber = 0;
		specialNodes = new HashMap<Integer, Long>();

		BufferedReader br;

		try {
			br = new BufferedReader(new FileReader(f));

			String line;

			while ((line = br.readLine()) != null) {

				char x;
				if (line.length() > 0) {
					x = line.charAt(0);
				} else {
					continue;
				}
				String[] strings = line.split(" ");

				switch (x) {
				case 'n':
					specialNodes.put(Integer.parseInt(strings[1]), Long.parseLong(strings[2]));
					break;
				case 'a':

					Arc arc = new Arc(Integer.parseInt(strings[1]), Integer.parseInt(strings[2]),
							Integer.parseInt(strings[3]), Long.parseLong(strings[4]),
							Long.parseLong(strings[5]));
					nsimplex.lower.add(arc);
					break;
				case 'c':
					// irrelevant comment
					break;
				case 'p':
					nodeNumber = Integer.parseInt(strings[2]);
					arcNumber = Integer.parseInt(strings[3]);
					break;


				}


			}

			br.close();


		} catch (FileNotFoundException e) {

			System.out.println("File not found");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("IOexecption");
		}


	} // end of constructor


	public int getNodeNumber() {
		return nodeNumber;
	}

	public int getArcNumber() {
		return arcNumber;
	}

	public Map<Integer, Long> getSpecialNodes() {
		return specialNodes;
	}

}
