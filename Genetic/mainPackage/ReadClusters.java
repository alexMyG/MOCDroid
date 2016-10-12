package mainPackage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class ReadClusters {


	public static HashMap<Integer, ArrayList<String>> readFile(String filePath){

		String fileString = null;
		try {
			fileString = new String(Files.readAllBytes(Paths.get(filePath)));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String [] fileArray = fileString.split("\n");

		HashMap<Integer, ArrayList<String>> listClusters = new HashMap<Integer, ArrayList<String>>();

		for (int i = 1; i < fileArray.length; i++){

			String [] lineSplit = fileArray[i].replace("\"", "").split(",");
			Integer cluster = Integer.parseInt(lineSplit[1]) - 1; // Clusters numbers in dataset start in 1
			String importChain = lineSplit[0];



			if (listClusters.containsKey(cluster)){
				ArrayList<String> currentArray = listClusters.get(cluster);
				currentArray.add(importChain);
				listClusters.put(cluster, currentArray);

			}else{

				ArrayList<String> newArray = new ArrayList<String>();
				newArray.add(importChain);
				listClusters.put(cluster, newArray);
			}

		}


		return listClusters;


	}




}
