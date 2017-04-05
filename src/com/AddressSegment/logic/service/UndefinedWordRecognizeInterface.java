package com.AddressSegment.logic.service;

import java.util.ArrayList;

public interface UndefinedWordRecognizeInterface {
	public ArrayList<String> getUndefinedWord(ArrayList<String> segmentWord);
	public String arrayListToCode(ArrayList<String> segmentWord);
}
