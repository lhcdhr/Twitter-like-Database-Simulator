package FinalProject;

import java.util.ArrayList;

public class Twitter {
	
	//ADD YOUR CODE BELOW HERE
	private ArrayList<Tweet> tweetList;
	private MyHashTable<String, ArrayList<Tweet>> authorTable;
	private MyHashTable<String, ArrayList<Tweet>> dateTable;
	private MyHashTable<String, Integer> trendTable;
	private MyHashTable<String, String> stopWordTable;
	//ADD CODE ABOVE HERE 
	
	// O(n+m) where n is the number of tweets, and m the number of stopWords
	public Twitter(ArrayList<Tweet> tweets, ArrayList<String> stopWords) {
		//ADD YOUR CODE BELOW HERE
		int tweetsSize = tweets.size();
		this.tweetList = new ArrayList<Tweet>(tweetsSize);
		this.authorTable = new MyHashTable<String, ArrayList<Tweet>>(tweetsSize);
		this.dateTable = new MyHashTable<String, ArrayList<Tweet>>(tweetsSize);
		this.trendTable = new MyHashTable<String, Integer>(1000);
		this.stopWordTable = new MyHashTable<String, String>(1000);
		int stopWordSize = stopWords.size();
		for(int m=0; m < stopWordSize; m++) {
			this.stopWordTable.put(stopWords.get(m).toLowerCase(), stopWords.get(m).toLowerCase());
		}
		for(int i=0;i<tweetsSize;i++) {
			Tweet tmp = tweets.get(i);
			String tmpAuthor = tmp.getAuthor();
			String tmpDate = tmp.getDateAndTime().substring(0,10);
			
			this.tweetList.add(new Tweet(tmp.getAuthor(), tmp.getDateAndTime(), tmp.getMessage()));
			if(authorTable.get(tmpAuthor) != null){
				authorTable.get(tmpAuthor).add(tmp);
			}
			else {
				ArrayList<Tweet> toAdd = new ArrayList<Tweet>();
				toAdd.add(tmp);
				authorTable.put(tmpAuthor, toAdd);
			}
			
			if(dateTable.get(tmpDate) != null) {
				dateTable.get(tmpDate).add(tmp);
			}
			else {
				ArrayList<Tweet> toAdd = new ArrayList<Tweet>();
				toAdd.add(tmp);
				dateTable.put(tmpDate, toAdd);
			}
			
			ArrayList<String> rawWord = getWords(tmp.getMessage());
			int rawSize = rawWord.size();
			ArrayList<String> word = new ArrayList<String>();
			for(int a=0; a<rawSize;a++) {
				String toCheck = rawWord.get(a).toLowerCase();
				if(!word.contains(toCheck)){
					word.add(toCheck);
				}
			}
			
			int numWord = word.size();
			int deleteCount = 0;
			for(int n=0; n<numWord;n++) {
				String wordToCheck = word.get(n-deleteCount);
				if(stopWordTable.get(wordToCheck)!=null) {
					word.remove(n-deleteCount);
					deleteCount++;
				}
				else {
					if(trendTable.get(wordToCheck)!=null) {
						Integer count = trendTable.get(wordToCheck);
						trendTable.put(wordToCheck, count+1);
					}
					else {
						Integer count = 1;
						trendTable.put(wordToCheck, count);
					}
				}
			}
		}
			
		

		//ADD CODE ABOVE HERE 
	}
	
	
    /**
     * Add Tweet t to this Twitter
     * O(1)
     */
	public void addTweet(Tweet t) {
		//ADD CODE BELOW HERE
		String tAuthor = t.getAuthor();
		String tDate = t.getDateAndTime().substring(0,10);
		if(this.authorTable.get(tAuthor) != null){
			this.authorTable.get(tAuthor).add(t);
		}
		else {
			ArrayList<Tweet> toAdd = new ArrayList<Tweet>();
			toAdd.add(t);
			this.authorTable.put(tAuthor, toAdd);
		}
		
		if(this.dateTable.get(tDate) != null) {
			this.dateTable.get(tDate).add(t);
		}
		else {
			ArrayList<Tweet> toAdd = new ArrayList<Tweet>();
			toAdd.add(t);
			this.dateTable.put(tDate, toAdd);
		}
		this.tweetList.add(t);
		//ADD CODE ABOVE HERE 
	}
	

    /**
     * Search this Twitter for the latest Tweet of a given author.
     * If there are no tweets from the given author, then the 
     * method returns null. 
     * O(1)  
     */
    public Tweet latestTweetByAuthor(String author) {
        //ADD CODE BELOW HERE
    	ArrayList<Tweet> authorTwitter = this.authorTable.get(author);
    	if(authorTwitter != null) {
        	int size = authorTwitter.size();
        	Tweet latest = authorTwitter.get(0);
        	for(int i=1;i<size;i++) {
        		if(latest.getDateAndTime().compareTo(authorTwitter.get(i).getDateAndTime())<0) {
        			latest = authorTwitter.get(i);
        		}
        	}
        	return latest;
    	}

    	return null;
    	
        //ADD CODE ABOVE HERE 
    }


    /**
     * Search this Twitter for Tweets by `date' and return an 
     * ArrayList of all such Tweets. If there are no tweets on 
     * the given date, then the method returns null.
     * O(1)
     */
    public ArrayList<Tweet> tweetsByDate(String date) {
        //ADD CODE BELOW HERE
    	
    	return this.dateTable.get(date);
    	
    	//return null;
    	
    	
        //ADD CODE ABOVE HERE
    }
    
	/**
	 * Returns an ArrayList of words (that are not stop words!) that
	 * appear in the tweets. The words should be ordered from most 
	 * frequent to least frequent by counting in how many tweet messages
	 * the words appear. Note that if a word appears more than once
	 * in the same tweet, it should be counted only once. 
	 */
    public ArrayList<String> trendingTopics() {
        //ADD CODE BELOW HERE
    	
    	ArrayList<String> result = MyHashTable.fastSort(this.trendTable);
    	
    	return result;
    	
        //ADD CODE ABOVE HERE    	
    }
    
    
    
    /**
     * An helper method you can use to obtain an ArrayList of words from a 
     * String, separating them based on apostrophes and space characters. 
     * All character that are not letters from the English alphabet are ignored. 
     */
    private static ArrayList<String> getWords(String msg) {
    	msg = msg.replace('\'', ' ');
    	String[] words = msg.split(" ");
    	ArrayList<String> wordsList = new ArrayList<String>(words.length);
    	for (int i=0; i<words.length; i++) {
    		String w = "";
    		for (int j=0; j< words[i].length(); j++) {
    			char c = words[i].charAt(j);
    			if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))
    				w += c;
    			
    		}
    		wordsList.add(w);
    	}
    	return wordsList;
    }
    
}
