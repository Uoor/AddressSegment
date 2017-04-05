package com.AddressSegment.logic;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.hadoop.fs.FileSystem;

import com.AddressSegment.logic.service.AlgorithmInterface;
import com.AddressSegment.metadata.model.Algorithm;
import com.AddressSegment.metadata.model.CharDictionary;
import com.AddressSegment.metadata.model.WordDictionary;
import com.AddressSegment.tool.dao.impl.AddressRegexImpl;
import com.AddressSegment.tool.dao.impl.DictionaryFileOperationDAOImpl;
import com.AddressSegment.tool.dao.impl.Stack;
import com.AddressSegment.util.Config;

public class AlgorithmDaoImpl extends Algorithm implements AlgorithmInterface {
	public static FileSystem fs = null;
	
public AlgorithmDaoImpl(WordDictionary inWordDict, CharDictionary<String> inCharDict, FileSystem inFs) throws URISyntaxException, IOException {
		wordDict = inWordDict;
		charDict = inCharDict;
		fs = inFs;
	}

	public AlgorithmDaoImpl(FileSystem inFs) throws URISyntaxException, IOException {
		
		wordDict = new WordDictionary();
		charDict = new CharDictionary<String>();
		DictionaryFileOperationDAOImpl DF = new DictionaryFileOperationDAOImpl(
				Config.getDefaultDictionaryHDFSURL(), Config.getCharDictionaryHDFSURL(), inFs);
		//DictionaryFileOperationDAOImpl DF = new DictionaryFileOperationDAOImpl(
		//		Config.getDefaultDictionaryURL(), Config.getCharDictionaryURL());
		DF.putFileToDict(wordDict, charDict);
	}

	public AlgorithmDaoImpl(String wordDictionaryURL, String charDictionary, FileSystem inFs) throws URISyntaxException, IOException {
		wordDict = new WordDictionary();
		charDict = new CharDictionary<String>();
		DictionaryFileOperationDAOImpl DF = new DictionaryFileOperationDAOImpl(
				wordDictionaryURL, charDictionary, inFs);
		DF.putFileToDict(wordDict, charDict);
	}
	
	@Override
	public ArrayList<String> runRMM(String strTemp) {
		AddressRegexImpl regex = new AddressRegexImpl();
		int sentenceLength = strTemp.length();
		ArrayList<String> segmentWord = new ArrayList<String>(); 
		Stack<String> Word = new Stack<String>();
		for (int i = sentenceLength, j = 2; i > 0; j = 2) {
			Stack<Integer> stack = new Stack<Integer>();
			for (; j <= this.getWordDict().getMaxLength()+4 && i >= j; j++) {
				if (regex.matchAddressRegex(strTemp.substring(i - j, i))) {
					stack.push(j);
				} else {
					if (this.getWordDict().searchWord(
							strTemp.substring(i - j, i))) {
						stack.push(j);
					}
				}
			}
			if (stack.isEmpty()) {
				Word.push(strTemp.substring(i - 1, i));
				i -= 1;
			} else {
				Word.push(strTemp.substring(i - stack.peek(), i));
				i -= stack.peek();
			}
			stack.clear();
		}
		for(int i=Word.getList().size()-1; i >= 0;i--){
			segmentWord.add(Word.getList().get(i));
		}
		/*for(int i = 0; i < segmentWord.length; i++)
			segmentWord[i] = Word.pop();*/
		return segmentWord;
	}
	
	@Override
	public ArrayList<String> runMM(String strTemp) {
		AddressRegexImpl regex = new AddressRegexImpl();
		int sentenceLength = strTemp.length();
		ArrayList<String> segmentWord = new ArrayList<String>(); 
		Stack<String> Word = new Stack<String>();
		for (int i = 0, j = 2; i < sentenceLength; j = 2) {
			Stack<Integer> stack = new Stack<Integer>();
			for (; j <= this.getWordDict().getMaxLength()+4
					&& j <= (sentenceLength - i); j++) {
				if (regex.matchAddressRegex(strTemp.substring(i, i + j))) {
					stack.push(j);
				} else {
					if (this.getWordDict().searchWord(
							strTemp.substring(i, i + j))) {
						stack.push(j);
					}
				}
			}
			if (stack.isEmpty()) {
				Word.push(strTemp.substring(i, i + 1));
				i += 1;
			} else {
				Word.push(strTemp.substring(i, i + stack.peek()));
				i += stack.peek();
			}
			stack.clear();
		}
		segmentWord = Word.getList();
		return segmentWord;
	}

	
}
