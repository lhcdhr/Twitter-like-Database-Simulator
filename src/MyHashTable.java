package FinalProject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;


public class MyHashTable<K,V> implements Iterable<HashPair<K,V>>{
    // num of entries to the table
    private int numEntries;
    // num of buckets 
    private int numBuckets;
    // load factor needed to check for rehashing 
    private static final double MAX_LOAD_FACTOR = 0.75;
    // ArrayList of buckets. Each bucket is a LinkedList of HashPair
    private ArrayList<LinkedList<HashPair<K,V>>> buckets; 
    
    // constructor
    public MyHashTable(int initialCapacity) {
        // ADD YOUR CODE BELOW THIS
        this.numBuckets = initialCapacity;
        this.numEntries = 0;
        this.buckets = new ArrayList<LinkedList<HashPair<K,V>>>(this.numBuckets);
        
        for(int i=0; i < this.numBuckets; i++) {
        	this.buckets.add(new LinkedList<HashPair<K,V>>());
        }
        
        //ADD YOUR CODE ABOVE THIS
    }
    
    public int size() {
        return this.numEntries;
    }
    
    public boolean isEmpty() {
        return this.numEntries == 0;
    }
    
    public int numBuckets() {
        return this.numBuckets;
    }
    
    /**
     * Returns the buckets variable. Useful for testing  purposes.
     */
    public ArrayList<LinkedList< HashPair<K,V> > > getBuckets(){
        return this.buckets;
    }
    
    /**
     * Given a key, return the bucket position for the key. 
     */
    public int hashFunction(K key) {
        int hashValue = Math.abs(key.hashCode())%this.numBuckets;
        return hashValue;
    }
    
    /**
     * Takes a key and a value as input and adds the corresponding HashPair
     * to this HashTable. Expected average run time  O(1)
     */
    
	public V put(K key, V value) {
        //  ADD YOUR CODE BELOW HERE
		
		int index = hashFunction(key);
		HashPair<K, V> toAdd = new HashPair<K,V>(key, value);
		LinkedList<HashPair<K, V>>bucket = this.buckets.get(index);
		int bucketSize = bucket.size();
		if(bucketSize != 0) {
			for(int i=0;i<bucketSize;i++) {
				HashPair<K, V> cur = bucket.get(i);
				if(cur.getKey().equals(key)) {
					V oldValue = cur.getValue();
					cur.setValue(value);
					return oldValue;
				}
			}
		}
		double nBuckets = this.numBuckets;
		double factor = ((this.numEntries+1)/nBuckets);
		if(factor>MAX_LOAD_FACTOR) {
			this.rehash();
			int newIndex = hashFunction(key);
			LinkedList<HashPair<K, V>> newbucket = this.buckets.get(newIndex);
			newbucket.add(toAdd);
			this.numEntries++;
		}
		else {
			bucket.add(toAdd);
			this.numEntries++;
		}
		return null;
		/*
		else {
			if(((this.numEntries+1)/this.numBuckets)>MAX_LOAD_FACTOR) {
				this.rehash();
				int newIndex = hashFunction(key);
				LinkedList<HashPair<K, V>> newbucket = this.buckets.get(newIndex);
				newbucket.add(toAdd);
				this.numEntries++;
			}
			else {
				bucket.add(toAdd);
				this.numEntries++;
			}
		}
		*/
				
        //  ADD YOUR CODE ABOVE HERE
    }
    
    
    /**
     * Get the value corresponding to key. Expected average runtime O(1)
     */
    
    public V get(K key) {
        //ADD YOUR CODE BELOW HERE
        int index = hashFunction(key);
        LinkedList<HashPair<K, V>> bucket = this.buckets.get(index);
        int size = bucket.size();
        if(size==0) {
        	return null;
        }
        for(int i=0;i<size;i++) {
        	if(bucket.get(i).getKey().equals(key)) {
        		return bucket.get(i).getValue();
        	}
        }
    	return null;
    	
        //ADD YOUR CODE ABOVE HERE
    }
    
    /**
     * Remove the HashPair corresponding to key . Expected average runtime O(1) 
     */
    public V remove(K key) {
        //ADD YOUR CODE BELOW HERE
        int index = hashFunction(key);
        LinkedList<HashPair<K, V>> bucket = this.buckets.get(index);
        int size = bucket.size();
        if(size==0) {
        	return null;
        }
        for(int i=0; i<size; i++) {
        	if(bucket.get(i).getKey().equals(key)) {
        		V value= bucket.get(i).getValue();
        		bucket.remove(i);
        		//this.buckets.set(i, bucket);
        		this.numEntries --;
        		return value;
        	}
        }
    	return null;
    	
        //ADD YOUR CODE ABOVE HERE
    }
    
    
    /** 
     * Method to double the size of the hashtable if load factor increases
     * beyond MAX_LOAD_FACTOR.
     * Made public for ease of testing.
     * Expected average runtime is O(m), where m is the number of buckets
     */
    public void rehash() {
        //ADD YOUR CODE BELOW HERE
    	this.numBuckets = 2*this.numBuckets;
    	int newSize = this.numBuckets;
    	MyHashIterator hashPairs = new MyHashIterator();
    	int sizePairs = hashPairs.pairs.size();
    	this.buckets = new ArrayList<LinkedList<HashPair<K,V>>>(this.numBuckets);
        for(int i=0; i <newSize; i++) {
        	this.buckets.add(new LinkedList<HashPair<K,V>>());
        }
        this.numEntries=0;
        for(int m=0; m<sizePairs; m++) {
    		if(hashPairs.hasNext()) {
    			HashPair<K,V> toAdd = hashPairs.next();
    			this.put(toAdd.getKey(), toAdd.getValue());
    		}
    	}

    	//ADD YOUR CODE ABOVE HERE
    }
    
    
    /**
     * Return a list of all the keys present in this hashtable.
     * Expected average runtime is O(m), where m is the number of buckets
     */
    
    public ArrayList<K> keys() {
        //ADD YOUR CODE BELOW HERE
    	MyHashIterator hashPairs = new MyHashIterator();
    	ArrayList<K> keyList = new ArrayList<K>();
    	while(hashPairs.hasNext()) {
    		keyList.add(hashPairs.next().getKey());
    	}
        
    	return keyList;
    	
        //ADD YOUR CODE ABOVE HERE
    }
    
    /**
     * Returns an ArrayList of unique values present in this hashtable.
     * Expected average runtime is O(m) where m is the number of buckets
     */
    public ArrayList<V> values() {
        //ADD CODE BELOW HERE
        MyHashTable<V, V> valueTable = new MyHashTable<V, V>(this.numBuckets);
        MyHashIterator hashPairs = new MyHashIterator();
        while(hashPairs.hasNext()) {
        	HashPair<K, V> toAdd= hashPairs.next();
        	valueTable.put(toAdd.getValue(), toAdd.getValue());
        }
        
    	return valueTable.keys();
    	
        //ADD CODE ABOVE HERE
    }
    
    
	/**
	 * This method takes as input an object of type MyHashTable with values that 
	 * are Comparable. It returns an ArrayList containing all the keys from the map, 
	 * ordered in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n^2), where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable<V>> ArrayList<K> slowSort (MyHashTable<K, V> results) {
        ArrayList<K> sortedResults = new ArrayList<>();
        for (HashPair<K, V> entry : results) {
			V element = entry.getValue();
			K toAdd = entry.getKey();
			int i = sortedResults.size() - 1;
			V toCompare = null;
        	while (i >= 0) {
        		toCompare = results.get(sortedResults.get(i));
        		if (element.compareTo(toCompare) <= 0 )
        			break;
        		i--;
        	}
        	sortedResults.add(i+1, toAdd);
        }
        return sortedResults;
    }
    
    
	/**
	 * This method takes as input an object of type MyHashTable with values that 
	 * are Comparable. It returns an ArrayList containing all the keys from the map, 
	 * ordered in descending order based on the values they mapped to.
	 * 
	 * The time complexity for this method is O(n*log(n)), where n is the number 
	 * of pairs in the map. 
	 */
    
    public static <K, V extends Comparable<V>> ArrayList<K> fastSort(MyHashTable<K, V> results) {
        //ADD CODE BELOW HERE
    	 ArrayList<LinkedList<HashPair<K,V>>> buckets= results.buckets;
    	 ArrayList<HashPair<K,V>> toSort = new ArrayList<HashPair<K,V>>();
    	 for(int i=0;i<results.numBuckets;i++) {
    		 LinkedList<HashPair<K,V>> tmp = buckets.get(i);
    		 if(tmp!=null) {
    			 int tmpSize = tmp.size();
    			 for(int m=0;m<tmpSize;m++) {
    				 toSort.add(tmp.get(m));
    			 }
    		 }
    	 }
    	 
    	 ArrayList<HashPair<K,V>> sorted = mergeSort(toSort);
    	 int size = sorted.size();
    	 ArrayList<K> keyList = new ArrayList<K>();
    	 for(int i=0;i<size;i++) {
    		 keyList.add(sorted.get(i).getKey());
    	 }
    	
    	
    	return keyList;
		
        //ADD CODE ABOVE HERE
    }
    private static <K, V extends Comparable<V>> ArrayList<HashPair<K,V>> mergeSort(ArrayList<HashPair<K,V>> toSort) {
        //ADD CODE BELOW HERE
    	int size = toSort.size();
    	if(size ==1) {
    		return toSort;
    	}
    	else {
    		int mid = (size-1)/2;
    		ArrayList<HashPair<K,V>> toMerge1 = new ArrayList<HashPair<K,V>>(mid+1);
    		ArrayList<HashPair<K,V>> toMerge2 = new ArrayList<HashPair<K,V>>(size-mid-1);
    		for(int i=0;i<size;i++) {
    			if(i<=mid) {
    				toMerge1.add(toSort.get(i));
    			}
    			else {
    				toMerge2.add(toSort.get(i));
    			}
    		}
    		toMerge1 = mergeSort(toMerge1);
    		toMerge2 = mergeSort(toMerge2);
    		return merge(toMerge1, toMerge2);
    	}
    	
    	
		
        //ADD CODE ABOVE HERE
    }
    
    private static <K, V extends Comparable<V>> ArrayList<HashPair<K,V>> merge(ArrayList<HashPair<K,V>> toMerge1,ArrayList<HashPair<K,V>> toMerge2) {
        //ADD CODE BELOW HERE
    	ArrayList<HashPair<K,V>> result = new ArrayList<HashPair<K,V>>();
    	while(!toMerge1.isEmpty()&& !toMerge2.isEmpty()) {
    		
    		if(toMerge1.get(0).getValue().compareTo(toMerge2.get(0).getValue())>0) {
    			result.add(toMerge1.get(0));
    			toMerge1.remove(0);
    			
    		}
    		else {
    			result.add(toMerge2.get(0));
    			toMerge2.remove(0);
    		}
    	}
    	
    	while(!toMerge1.isEmpty()) {
    		result.add(toMerge1.get(0));
    		toMerge1.remove(0);
    	}
    	while(!toMerge2.isEmpty()) {
    		result.add(toMerge2.get(0));
    		toMerge2.remove(0);
    	}
    	
    	return result;
		
        //ADD CODE ABOVE HERE
    }
    

    
    
    
    @Override
    public MyHashIterator iterator() {
        return new MyHashIterator();
    }   
    
    private class MyHashIterator implements Iterator<HashPair<K,V>> {
        //ADD YOUR CODE BELOW HERE
    	private LinkedList<HashPair<K, V>> pairs;
        //ADD YOUR CODE ABOVE HERE
    	
    	/**
    	 * Expected average runtime is O(m) where m is the number of buckets
    	 */
        private MyHashIterator() {
            //ADD YOUR CODE BELOW HERE
        	
        	this.pairs = new LinkedList<HashPair<K, V>>();
        	ArrayList<LinkedList<HashPair<K, V>>> itBucket = buckets;
        	int sizeBucket = itBucket.size();
        	if(sizeBucket != 0) {
            	for(int i=0; i<sizeBucket; i++){
            		LinkedList<HashPair<K, V>> bucketList = itBucket.get(i);
            		int sizeList = bucketList.size();
            		if(sizeList != 0) {
            			for(int m=0 ; m<sizeList; m++){
            				this.pairs.add(bucketList.get(m));
            			}
            		}
            	}
        	}

            //ADD YOUR CODE ABOVE HERE
        }
        
        @Override
        /**
         * Expected average runtime is O(1)
         */
        public boolean hasNext() {
            //ADD YOUR CODE BELOW HERE
        	if(pairs.size()>0) {
        		return true;
        	}
        	return false;
        	
            //ADD YOUR CODE ABOVE HERE
        }
        
        @Override
        /**
         * Expected average runtime is O(1)
         */
        public HashPair<K,V> next() {
            //ADD YOUR CODE BELOW HERE
        	
        	return pairs.remove();
        	
            //ADD YOUR CODE ABOVE HERE
        }
        
    }
}
