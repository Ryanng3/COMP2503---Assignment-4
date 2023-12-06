package mru.application;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;
import java.io.File;
import java.io.IOException;

/**
 * COMP 2503 Fall 2023 Assignment 4
 * 
 * This program must read a input stream and keeps track of the frequency at
 * which an avenger is mentioned either by name or alias or performer's last name. The program must use HashMaps
 * for keeping track of the Avenger Objects, and it must use TreeMaps
 * for storing the data. 
 * 
 * @author Maryam Elahi
 * @date Fall 2023
 */

public class A4 {

	public String[][] avengerRoster = { { "captainamerica", "rogers", "evans" }, { "ironman", "stark", "downey" },
			{ "blackwidow", "romanoff", "johansson" }, { "hulk", "banner", "ruffalo" },
			{ "blackpanther", "tchalla", "boseman" }, { "thor", "odinson", "hemsworth" },
			{ "hawkeye", "barton", "renner" }, { "warmachine", "rhodes", "cheadle" },
			{ "spiderman", "parker", "holland" }, { "wintersoldier", "barnes", "stan" } };

	private int topN = 4;
	private int totalWordCount = 0;
	private String FILE_PATH = "res/input4.txt";
	private Scanner input = new Scanner(System.in);
	Map<String, Avenger> hashMap = new HashMap<>();
	TreeMap<Avenger, String> alphabeticalMap = new TreeMap<>();
	TreeMap<Avenger, String> mentionOrderMap = new TreeMap<>(new AvengerMentionComparator());
	TreeMap<Avenger, String> popularAvengerMap = new TreeMap<>(new AvengerComparator());
	TreeMap<Avenger, String> popularPerformerMap = new TreeMap<>(new PerformerComparator());



	/* TODO:
	 * Create the necessary hashMap and treeMap objects to keep track of the Avenger objects 
	 * Remember that a hashtable does not keep any inherent ordering for its contents.
	 * But for this assignment we want to be able to create the sorted lists of avenger objects.
	 * Use TreeMap objects (which are binary search trees, and hence have an
	 * ordering) for creating the following orders: alphabetical, mention order, most popular avenger, and most popular performer
	 * The alphabetical order TreeMap must be constructed with the natural order of the Avenger objects.
	 * The other three orderings must be created by passing the corresponding Comparators to the 
	 * TreeMap constructor. 
	 */
	
	public static void main(String[] args) throws IOException {
		A4 a4 = new A4();
		a4.run();
	}

	public void run() throws IOException {
		readInput();
		createdOrderedTreeMaps();
		printResults();
	}

	private void createdOrderedTreeMaps() {
		/* TODO:
		 * Create an iterator over the key set in the HashMap that keeps track of the avengers
		 * Add avenger objects to the treeMaps with different orderings.
		 * 
		 ** Hint: 
		 * Note that the HashMap and the TreeMap classes do not implement
		 * the Iterable interface at the top level, but they have
		 * methods that return Iterable objects, such as keySet() and entrySet().
		 * For example, you can create an iterator object over 
		 * the 'key set' of the HashMap and use the next() method in a loop
		 * to get each word object. 
		 */		
		 for (Entry<String, Avenger> entry : hashMap.entrySet()) {
		        Avenger foundA = entry.getValue();
		        alphabeticalMap.put(foundA, foundA.getHeroAlias());
		        mentionOrderMap.put(foundA, foundA.getHeroAlias());
		        popularAvengerMap.put(foundA, foundA.getHeroAlias());
		        popularPerformerMap.put(foundA, foundA.getHeroAlias());

		        
		        
		 }
		 
	}

	/**
	 * read the input stream and keep track how many times avengers are mentioned by
	 * alias or last name.
	 * @throws IOException 
	 */
	private void readInput() throws IOException {
		/*
		 * In a loop, while the scanner object has not reached end of stream, - read a
		 * word. - clean up the word - if the word is not empty, add the word count. -
		 * Check if the word is either an avenger alias or last name then - Create a new
		 * avenger object with the corresponding alias and last name. - if this avenger
		 * has already been mentioned, increase the corresponding frequency count for the object
		 * already in the hashMap. - if this avenger has not been mentioned before, add the
		 * newly created avenger to the hashMap, remember to set the frequency, and 
		 * to keep track of the mention order
		 */
		File file = new File(FILE_PATH);
		Scanner scanner = new Scanner(file);
		while(scanner.hasNext()) {
			String word = scanner.next();
			word = cleanWord(word);
			
			if(!word.isEmpty()) {
				totalWordCount++;		
				updateAvengerMap(word);
			}
		}

	}
	
	private String cleanWord(String next) {
		String ret;
		int inx = next.indexOf('\'');
		if (inx != -1)
			ret = next.substring(0, inx).toLowerCase().trim().replaceAll("[^a-z]", "");
		else
			ret = next.toLowerCase().trim().replaceAll("[^a-z]", "");
		return ret;
	}
	
	private void updateAvengerMap(String word) {
		  for (int i = 0; i < avengerRoster.length; i++) {
		        if (word.equals(avengerRoster[i][0]) || word.equals(avengerRoster[i][1]) || word.equals(avengerRoster[i][2])) {
		            Avenger newA = new Avenger();
		            newA.setHeroAlias(avengerRoster[i][0]);
		            newA.setHeroName(avengerRoster[i][1]);
		            newA.setPerformer(avengerRoster[i][2]);

		            Avenger a = findA(word);

		            if (a != null) {
		                if (word.equals(avengerRoster[i][0]))
		                    a.setAliasFreq(a.getAliasFreq() + 1);
		                else if (word.equals(avengerRoster[i][1]))
		                    a.setNameFreq(a.getNameFreq() + 1);
		                else if (word.equals(avengerRoster[i][2]))
		                    a.setPerformerFreq(a.getPerformerFreq() + 1);
		            } else {
		                a = newA;

		                if (word.equals(avengerRoster[i][0]))
		                    a.setAliasFreq(1);
		                else if (word.equals(avengerRoster[i][1]))
		                    a.setNameFreq(1);
		                else if (word.equals(avengerRoster[i][2]))
		                    a.setPerformerFreq(1);
		                
		                a.setMentionOrder(hashMap.size() + 1);


		                hashMap.put(a.getHeroAlias(), a);

		            }
		        }
		    }

	}
	
	
	private Avenger findA(String word) {
	    for (Entry<String, Avenger> entry : hashMap.entrySet()) {
	        Avenger foundA = entry.getValue();

	        if (foundA.getHeroName().equalsIgnoreCase(word) ||
	            foundA.getHeroAlias().equalsIgnoreCase(word) ||
	            foundA.getPerformer().equalsIgnoreCase(word)) {
	            return foundA;
	        }
	    }
	    return null;
	}

	/**
	 * print the results
	 */
	private void printResults() {
		/*
		 * Please first read the documentation for TreeMap to see how to 
		 * iterate over a TreeMap data structure in Java.
		 *  
		 * Hint for printing the required list of avenger objects:
		 * Note that the TreeMap class does not implement
		 * the Iterable interface at the top level, but it has
		 * methods that return Iterable objects.
		 * You must either create an iterator over the 'key set',
		 * or over the values 'collection' in the TreeMap.
		 * 
		 */
		
		
		System.out.println("Total number of words: " + totalWordCount);
		System.out.println("Number of Avengers Mentioned: " + alphabeticalMap.size());
		System.out.println();

		System.out.println("All avengers in the order they appeared in the input stream:");
		
		for (Entry<Avenger, String> entry : mentionOrderMap.entrySet()) {
            System.out.println(entry.getKey());
        }
		System.out.println();

		System.out.println("Top " + topN + " most popular avengers:");
		printTopN(popularAvengerMap);

		System.out.println();

		System.out.println("Top " + topN + " most popular performers:");

		printTopN(popularPerformerMap);
		
		System.out.println();

		System.out.println("All mentioned avengers in alphabetical order:");
		// Todo: Print the list of avengers in alphabetical order
		for (Entry<Avenger, String> entry : alphabeticalMap.entrySet()) {
            System.out.println(entry.getKey());
        }
		System.out.println();
	}
	
	private void printTopN(TreeMap<Avenger, String> map) {
		Iterator<Entry<Avenger, String>> i = map.entrySet().iterator();
		int count = 0;
		while(i.hasNext() && count < topN) {
			Map.Entry<Avenger, String> e = i.next();
			System.out.println(e.getKey());
			
			count++;
		}
	}
}

