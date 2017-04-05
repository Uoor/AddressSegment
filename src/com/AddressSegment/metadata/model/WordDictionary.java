package com.AddressSegment.metadata.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WordDictionary extends BaseModel {
	private HashMap<String, Double> wordHashMap = null;
	private int maxLength = 0;

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public WordDictionary() {
		wordHashMap = new HashMap<String, Double>();
	}

	public void loadDefautSetting() {

	}

	public HashMap<String, Double> getWordHashMap() {
		return wordHashMap;
	}

	public void setWordHashMap(HashMap<String, Double> wordHashMap) {
		this.wordHashMap = wordHashMap;
	}

	public int getWordHashMapSize() {
		return wordHashMap.size();
	}

	public boolean appendWord(Word word) {
		if (null != word) {
			wordHashMap.put(word.getName(), 1.0);
			return true;
		}
		return false;
	}

	public boolean appendWord(Word word, double value) {
		if (null != word) {
			wordHashMap.put(word.getName(), value);
			return true;
		}
		return false;
	}

	public boolean fixFrequency(Word word) {
		if (null != word) {
			if (wordHashMap.isEmpty()
					|| wordHashMap.containsKey(word.getName())) {
				wordHashMap.put(word.getName(),
						wordHashMap.get(word.getName()) + 1);
				return true;
			}
			System.out.println("Nopoint of word.");
		}
		return false;
	}
	
	public boolean searchWord(String Character){
		return this.getWordHashMap().containsKey(Character);
	}

	public void output(){
		Iterator<?> iter = this.getWordHashMap().entrySet().iterator();
		while (iter.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			Object val = entry.getValue();
			System.out.println(key + "\t" + val);
		}
	}
	
	

}
