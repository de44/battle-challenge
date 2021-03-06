package battlechallenge.maps;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import battlechallenge.Coordinate;
import battlechallenge.server.GameManagerWindow;
import battlechallenge.structures.Barrier;
import battlechallenge.structures.Base;
import battlechallenge.structures.City;
import battlechallenge.structures.Structure;

public class MapImporter {
	
	public static BattleMap getMap() {
		return getMap(GameManagerWindow.getSelectedMap().getFilepath());
	}
	
	public static BattleMap getMap(String filename) {
		return getMap(new File(filename));
	}
	
	public static BattleMap getMap(File file) {
		String name = file.getName();
		int playerNum = 0; // Start at 0 increase every time map finds a base
		try {
			Scanner scanner = new Scanner(file);
			String[] arr = scanner.nextLine().split(",");
			int boardWidth = Integer.parseInt(arr[0]);
			int boardHeight = Integer.parseInt(arr[1]);
			if (arr.length >= 3)
				name = arr[2];
			List<Structure> structures = new LinkedList<Structure>();
			List<Base> bases = new LinkedList<Base>();
			for (int row=0;row<boardHeight;row++) {
		      String line = scanner.nextLine();
		      for (int col = 0; col < boardWidth; col++) { // Go through the column in the map
					if (line.charAt(col) == ('C'))
						structures.add(new City(new Coordinate(row, col)));
					if (line.charAt(col) == ('B'))
						bases.add(new Base(playerNum++, new Coordinate(row, col))); 
					if (line.charAt(col) == 'W') // barriers
						structures.add(new Barrier(new Coordinate(row, col)));
				}
			}
			return new BattleMap(name, boardWidth, boardHeight, structures, bases);
		} catch (Exception e) {
			System.err.println(e.getMessage()); 
		}
		// cannot import map
		return null;
	}
	
}
