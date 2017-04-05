package com.AddressSegment.metadata.model;

public class Algorithm {
	protected String sentenceString = null;
	protected WordDictionary wordDict = null;
	protected CharDictionary<String> charDict = null;

	public String getSentenceString() {
		return sentenceString;
	}

	public void setSentenceString(String sentenceString) {
		this.sentenceString = sentenceString;
	}

	public WordDictionary getWordDict() {
		return wordDict;
	}

	public void setWordDict(WordDictionary wordDict) {
		this.wordDict = wordDict;
	}

	public CharDictionary<String> getCharDict() {
		return charDict;
	}

	public void setCharDict(CharDictionary<String> charDict) {
		this.charDict = charDict;
	}
	
	

}
