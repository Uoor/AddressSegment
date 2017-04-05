package com.AddressSegment.tool.dao.service;




import java.io.IOException;

import org.apache.hadoop.util.LineReader;

import com.AddressSegment.metadata.model.WordDictionary;

public interface DictionaryFileOperationInterface extends FileOperationInterface  {
	
	public String readFileByLines(LineReader reader);
	public void writeFileByLines(WordDictionary WordDict);
	public void putFileToDict(WordDictionary WordDict) throws IOException;
}