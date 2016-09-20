package search;

import java.io.*;
import java.util.*;

/**
 * This class encapsulates an occurrence of a keyword in a document. It stores the
 * document name, and the frequency of occurrence in that document. Occurrences are
 * associated with keywords in an index hash table.
 * 
 * @author Sesh Venugopal
 * 
 */


/*class Driver
{
	public static void main(String args[]) throws FileNotFoundException
	{
		LittleSearchEngine search = new LittleSearchEngine(); 
		search.makeIndex("docs.txt","noisewords.txt");
    
		HashMap<String,ArrayList<Occurrence>> add = search.keywordsIndex; 
		
		
		for(Map.Entry<String, ArrayList<Occurrence>> entry : add.entrySet())
		{
			//Here we get the occurrence value 
			String key = entry.getKey();  
			ArrayList<Occurrence> s1 = entry.getValue(); 
		
			System.out.println("key:" + key);
			//System.out.println("Value:" + s1);
	    }  
	   
		System.out.println("");
		ArrayList<String> arrList = search.top5search("time", "indeed"); 
		System.out.println(arrList);
   }
}
*/
 
class Occurrence {
	/**
	 * Document in which a keyword occurs.
	 */
	String document;
	
	/**
	 * The frequency (number of times) the keyword occurs in the above document.
	 */
	int frequency;
	
	/**
	 * Initializes this occurrence with the given document,frequency pair.
	 * 
	 * @param doc Document name
	 * @param freq Frequency
	 */
	public Occurrence(String doc, int freq) {
		document = doc;
		frequency = freq;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(" + document + "," + frequency + ")";
	}
}

/**
 * This class builds an index of keywords. Each keyword maps to a set of documents in
 * which it occurs, with frequency of occurrence in each document. Once the index is built,
 * the documents can searched on for keywords.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in descending
	 * order of occurrence frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash table of all noise words - mapping is from word to itself.
	 */
	HashMap<String,String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashMap<String,String>(100,2.0f);
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) 
		{
			String word = sc.next();
			noiseWords.put(word,word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext())
		{
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeyWords(docFile); 
			mergeKeyWords(kws);
		}
		
	}

	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeyWords(String docFile) 
	throws FileNotFoundException 
	{
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE 
		
		Scanner sc = new Scanner(new File(docFile)); 
		HashMap<String,Occurrence> s1 = new HashMap<String,Occurrence>(); 
		
		while(sc.hasNext())
		{  
			String doc=sc.next(); 
			
			String isKeyWord=getKeyWord(doc); 
			
			//if isKeyWord is a noiseWord
			if(isKeyWord==null)
			{
				//do nothing 
			}
			
			else 
			{
				Occurrence ptr = s1.get(isKeyWord); 
				if(ptr==null)
				{
					Occurrence val = new Occurrence(docFile,1); 
					s1.put(isKeyWord, val);
				}
			
				else 
				{
					ptr.frequency+=1; 
				}
			}
			
		}
		
		return s1; 
		
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeyWords(HashMap<String,Occurrence> kws) 
	{
		// COMPLETE THIS METHOD
		
		//we have to add the occurrence that we are going to add to the end of the list 
		// we access the value by using the get method on the keyWordsIndex hashMap for each key(which is each string in the kws Hashmap) and getting the value (which is the arrayList of Occurrence objects and we add to the end of that arrayList and send it to insertLastOccurrence)
		
		//insertLastOccurrence just gets the very last item of the value(arrayList) of that certain key(string) and sorts it accordingly using binary search 
		//we return the numbers we had to traverse through
		
		HashMap<String,Occurrence> add = kws;  
		for(Map.Entry<String, Occurrence> entry : add.entrySet())
		{
			//Here we get the occurrence value 
			String key = entry.getKey();  
			Occurrence s1 = entry.getValue(); 
			
			//We get the value(arrayList) for the certain key of the keyWordsIndex 
			ArrayList<Occurrence> arrList = keywordsIndex.get(key); 
			
			//means that word is not found within the keywordsIndex hashTable
			if(arrList==null)
			{
				arrList = new ArrayList<Occurrence>();
				arrList.add(s1);
				
				insertLastOccurrence(arrList);
				
				keywordsIndex.put(key, arrList); 
				
				
			}
			
			//otherwise the word is within the keywordsIndex hashTable 
			else
			{
				//We add it to the end of the arrayList 
				arrList.add(s1);
			
				//then we send it to insertLastOccurrence
				insertLastOccurrence(arrList);
			}  
			
		}
			
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * TRAILING punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyWord(String word) 
	{
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		
		String s1 = word.toLowerCase(); 
		int length = s1.length()-1;  
		
		if(length==0)
		{
			return null; 
		}
		
		//if the word contains a number
		if(s1.matches(".*\\d.*"))  
		{
			return null; 
		}
		
		//We check to see if there are any punctuations in the front and we remove them 
		while(length >-1 && (Character.toString(s1.charAt(length)).equals(".") || Character.toString(s1.charAt(length)).equals(",") || Character.toString(s1.charAt(length)).equals("?") || Character.toString(s1.charAt(length)).equals(":") || Character.toString(s1.charAt(length)).equals(";") || Character.toString(s1.charAt(length)).equals("!"))  )
		{ 
			s1=s1.substring(0,length); 
			length--; 
		}
	
		if(length==0)
		{
			return null; 
		}
		//Means there is punctuations in the middle of the word 
		if(s1.contains(".") || s1.contains(",") || s1.contains("?") || s1.contains(":") || s1.contains(";") || s1.contains("!"))
		{
			return null;
		}
		//Means that the word we are looking for is in the noiseWords hashTable meaning it is a noise word then we return null 
		
		String get = null; 
		get = noiseWords.get(s1);
		if(get!=null)
		{
			return null; 
		}
		
		else 
		{
			return s1; 
		}
		
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * same list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion of the last element
	 * (the one at index n-1) is done by first finding the correct spot using binary search, 
	 * then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) 
	{
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		if(occs.size()==1)
		{
			return null; 
		}
		else
		{
	     	return binarySearch(occs); 
		}
		
	}
	
	
	private ArrayList<Integer> binarySearch(ArrayList<Occurrence> occs) 
	 {
		//We get the last value of the arrayList which is the Occurrence object which we are going to properly sort 
		Occurrence s1 = occs.get(occs.size()-1); 
		 occs.remove(occs.size()-1);
		 
		 int size = occs.size(); 
	     int low = 0;
	     int high = size - 1;
	     //the arrayList to add the indexes that we go through 
	     ArrayList<Integer> arrList = new ArrayList<Integer>(); 
	      
	     //if the high is less than 0 then we add to front 
	      
	     //if the low is equal to the size then add to rear or the low is greater than the largest index (size()-1) 
	     
	     //general case : if the last middle is greater than the point we are trying to add then mid+1 if it is less than then we add at that index
	     
	     int middle=0; 
         while(high >= low) 
         {
	             middle=(low + high) / 2;
	             arrList.add(middle); 
	             
	             if(occs.get(middle).frequency == s1.frequency)
	             {
	            	break;
	             }
	             
	             if(occs.get(middle).frequency < s1.frequency) {
	                 high = middle - 1;
	             }
	             if(occs.get(middle).frequency > s1.frequency) {
	                 low = middle + 1;
	             }
	      }
           
         if(occs.get(middle).frequency == s1.frequency)
         {
        	occs.add(middle,s1); 
         }
         
        //we add to the front 
        if(high<0)
        {
        	occs.add(0, s1);
        }
        
        //we add to the end 
        else if(low==occs.size())
        {
        	occs.add(s1);
        }
        
        //general case 
        else
        {
          if(occs.get(middle).frequency>s1.frequency)
          {
           occs.add(middle+1,s1); 
          }
          else if(occs.get(middle).frequency<s1.frequency)
          {
           occs.add(middle,s1); 
          }
        }
         
         return arrList; 
 	  }
	
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of occurrence frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will appear before doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matching documents, the result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of NAMES of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matching documents,
	 *         the result is null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) 
	{
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		
		//get the two arrayLists and sort them in order from greatest to least 
		//get the first 5 items and output them into the arrayList. If two of the same document exist ignore the second. 
		
		ArrayList<Occurrence> words = new ArrayList<Occurrence>(); 
		
		//This is our reference pointer for our first keyword 
		ArrayList<Occurrence> arrList1 = new ArrayList<Occurrence>(); 
		
		//we assign our reference to the matching value(arrayList)
		arrList1=keywordsIndex.get(kw1); 
		 
		//We add the entire arrayList onto the main names ArrayList (it is already in descending order) 
		int num = 0; 
		while(num<arrList1.size())
		{
			words.add(arrList1.get(num)); 
			insertLastOccurrence(words);
			num++; 
		}
	
		//This is our reference pointer for our second keyword
		ArrayList<Occurrence> arrList2 = new ArrayList<Occurrence>(); 

		//we assign our reference to the matching value(arrayList)
		arrList2=keywordsIndex.get(kw2); 
		
		//we iterate through keyword 2's arrayList and we insert it to our words arrayList
		num=0;
		while(num<arrList2.size())
		{
			//we add the word to the end and do an insertLastOccurrence on it 
			words.add(arrList2.get(num)); 
			insertLastOccurrence(words); 
			num++;
		}
		
	
		
		//I am changing the order so that if two documents have the same frequency then kw1 is before kw2 
		int iterator = 0; 
		while(iterator<words.size()-1)
		{	//I am checking all but the second to last item in the words arrayList if it's frequecy is equal to the next item's frequency 
			if(words.get(iterator).frequency==words.get(iterator+1).frequency)
			{	//the item in iterator + 1 is located in the arrayList for keyword 1 and the item in iterator is located in arrList 2 
			    Occurrence one = words.get(iterator+1); 
			    Occurrence two = words.get(iterator);
			   
			    boolean boo=false;
			    for(int x=0; x<arrList1.size();x++)
			    {
			    	if(arrList1.get(x)==one)
			    	{
			    		boo=true;
			    		break;
			    	}

			    }
			    boolean boo2=false;
			    for(int x=0; x<arrList2.size();x++)
			    {
			    	if(arrList2.get(x)==two)
			    	{
			    		boo2=true;
			    		break;
			    	}
			    }
 
			    
				if(boo && boo2)
			    {
			    	Occurrence remove = words.get(iterator+1); 
			    	words.remove(iterator+1); 
			    	words.add(iterator, remove);
			    }
			}			
			iterator++; 		
		}
		
		//what I am using to store the name of the documents and what I am going to return
		ArrayList<String> namesOfDocs = new ArrayList<String>(); 
		namesOfDocs.add(words.get(0).document);
		
		int iterate = 1; 
		//if the occurrence objects are a size of less than 5 then we 
	if(words.size()<5)
	{
	     while(iterate<words.size())
    	 {
		   if(namesOfDocs.contains(words.get(iterate).document))
		   {
			 words.remove(iterate);
			 iterate=iterate-1; 
		   }
		   else 
		   {
			 namesOfDocs.add(words.get(iterate).document);
		   }
		
			iterate++;
		 }
			 
	}
	
	else 
	{
		while(iterate<words.size() && namesOfDocs.size()!=5)
		{
			if(namesOfDocs.contains(words.get(iterate).document))
			{
			  words.remove(iterate);
			  iterate=iterate-1; 
			}
			else 
		    {
		   	  namesOfDocs.add(words.get(iterate).document);
		    }
			
			iterate++;			
		}
	}
		
	
		return namesOfDocs;
		
	

	}
  }	
