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

	private int topN = 4;																	//Constants and Variables
	private int totalWordCount = 0;
	private Scanner input = new Scanner(System.in);
	Map<String, Avenger> hashMap = new HashMap<>();
	TreeMap<Avenger, String> alphabeticalMap = new TreeMap<>();
	TreeMap<Avenger, String> mentionOrderMap = new TreeMap<>(new AvengerMentionComparator());
	TreeMap<Avenger, String> popularAvengerMap = new TreeMap<>(new AvengerComparator());
	TreeMap<Avenger, String> popularPerformerMap = new TreeMap<>(new PerformerComparator());
	 
	/**
	 * Main Method to run the program
	 * @param args
	 */
	public static void main(String[] args) {
		A4 a4 = new A4();
		a4.run();
	}
	
	/**
	 * Method to run the program
	 */
	public void run() {
		readInput();
		createdOrderedTreeMaps();
		printResults();
	}

	/**
	 * Creates TreeMap objects to keep track avengers in different orderings using external comparators
	 * Different Orderings include Alphabetical, Mention Order, Popular Avenger, and Popular Performer 
	 */
	private void createdOrderedTreeMaps() {	
		 for (Entry<String, Avenger> entry : hashMap.entrySet()) {		//Iterate over the key set in the HashMapand add Avenger objects to the TreeMaps with alternate orderings
		        Avenger foundA = entry.getValue();
		        alphabeticalMap.put(foundA, foundA.getHeroAlias());
		        mentionOrderMap.put(foundA, foundA.getHeroAlias());
		        popularAvengerMap.put(foundA, foundA.getHeroAlias());
		        popularPerformerMap.put(foundA, foundA.getHeroAlias());     	        
		 }
		 
	}

	/**
	 * read the input stream and keep track how many times avengers are mentioned by alias or last name
	 */
	private void readInput() {

		while(input.hasNext()) {
			String word = input.next();
			word = cleanWord(word);
			
			if(!word.isEmpty()) {
				totalWordCount++;		
				updateAvengerMap(word);
			}
		}
	}
	
	
	/**
	 * Cleans up the word by removing non-alphabetical characters
	 * @param next
	 * @return
	 */
	private String cleanWord(String next) {
		String ret;
		int inx = next.indexOf('\'');							
		if (inx != -1)
			ret = next.substring(0, inx).toLowerCase().trim().replaceAll("[^a-z]", "");	//Searches and removes non-alphabetical Character
		else
			ret = next.toLowerCase().trim().replaceAll("[^a-z]", "");
		return ret;
	}
	
	/**
	 * Updates the Avenger map based on the word
	 * @param word
	 */
	private void updateAvengerMap(String word) {
		  for (int i = 0; i < avengerRoster.length; i++) {
		        if (word.equals(avengerRoster[i][0]) || word.equals(avengerRoster[i][1]) || word.equals(avengerRoster[i][2])) {		
		            Avenger newA = new Avenger();
		            newA.setHeroAlias(avengerRoster[i][0]);
		            newA.setHeroName(avengerRoster[i][1]);
		            newA.setPerformer(avengerRoster[i][2]);

		            Avenger a = findA(word);

		            if (a != null) {
		                if (word.equals(avengerRoster[i][0]))			//Increment frequency count
		                    a.setAliasFreq(a.getAliasFreq() + 1);
		                else if (word.equals(avengerRoster[i][1]))
		                    a.setNameFreq(a.getNameFreq() + 1);
		                else if (word.equals(avengerRoster[i][2]))
		                    a.setPerformerFreq(a.getPerformerFreq() + 1);
		            } else {
		                a = newA;											//Add Avenger to map if does not exist

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
	
	
	/**
	 * Searches the map for the Avenger
	 * @param word
	 * @return
	 */
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
		
		System.out.println("Total number of words: " + totalWordCount);
		System.out.println("Number of Avengers Mentioned: " + hashMap.size());
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
		for (Entry<Avenger, String> entry : alphabeticalMap.entrySet()) {
            System.out.println(entry.getKey());
        }
		System.out.println();
	}
	
	
	/**
	 * Prints the Top 4 avengers in the give TreeMap
	 * @param map
	 */
	private void printTopN(TreeMap<Avenger, String> map) {					
		Iterator<Entry<Avenger, String>> i = map.entrySet().iterator();		//Creates an iterator over entry set
		int count = 0;
		while(i.hasNext() && count < topN) {							
			Map.Entry<Avenger, String> e = i.next();
			System.out.println(e.getKey());									
			
			count++;
		}
	}
}

