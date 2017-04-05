package com.AddressSegment.metadata.model;

import java.util.List;

public class Segment extends BaseModel {
	private String ID = null;
	private String sentence = null;
	private List<Word> wordList;

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public List<Word> getWordList() {
		return wordList;
	}

	public void setWordList(List<Word> wordList) {
		this.wordList = wordList;
	}
	
	public Segment() {
		
	}

	public Segment(String sentenceInput) {
		this.setSentence(sentenceInput);
	}

	public int getWordListSize() {
		return wordList.size();
	}

	public boolean appendWord(Word inputWord) {
		if (null != inputWord) {
			System.out.println("No words any more. So quit");
		} else {
			wordList.add(inputWord);
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		String Sentence = "";
		for (int i = getWordListSize() - 1; i < 0; i--)
			Sentence += wordList.get(i).getName();
		return Sentence;
	}

}
