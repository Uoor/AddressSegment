package com.AddressSegment.tool.dao.impl;



import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.LineReader;

import com.AddressSegment.metadata.model.CharDictionary;
import com.AddressSegment.metadata.model.Word;
import com.AddressSegment.metadata.model.WordDictionary;
import com.AddressSegment.tool.dao.service.DictionaryFileOperationInterface;

public class DictionaryFileOperationDAOImpl implements
		DictionaryFileOperationInterface {
	private File fileDictionary = null;
	private File fileChar = null;
	private LineReader dicReader = null;
	private LineReader charReader = null;
	//rivate FileSystem fs = null;
	public InputStream dicReaderin;
	public InputStream charReaderin;
	

	public File getFileDictionary() {
		return fileDictionary;
	}

	public void setFileDictionary(String fileDictionary) throws  IllegalArgumentException, IOException {
		//this.fileDictionary = new File(DictionaryFileOperationDAOImpl.class.getResource(fileDictionary).getFile());
		//dicReaderin = (FSDataInputStream) DictionaryFileOperationDAOImpl.class.getResourceAsStream(fileDictionary);
		
	}

	public File getFileChar() {
		return fileChar;
	}

	public void setFileChar(String fileChar) throws IllegalArgumentException, IOException {
		//this.fileChar = new File(DictionaryFileOperationDAOImpl.class.getResource(fileChar).getFile());
		//charReaderin = (FSDataInputStream) DictionaryFileOperationDAOImpl.class.getResourceAsStream(fileChar);
		
	}

	public LineReader getDicReader() {
		return dicReader;
	}
	
	public void setDicReader() {
		this.dicReader = new LineReader(dicReaderin);
	}

	public void setDicReader(LineReader dicReader) {
		this.dicReader = dicReader;
	}

	public LineReader getCharReader() {
		return charReader;
	}
	
	public void setCharReader() {
		this.charReader = new LineReader(charReaderin);
	}

	public void setCharReader(LineReader charReader) {
		this.charReader = charReader;
	}

	public DictionaryFileOperationDAOImpl(String DictFileURL, String CharFileURL, FileSystem fs) throws URISyntaxException, IOException {
		//Configuration conf  = new Configuration();
		//fs = FileSystem.get(URI.create("hdfs://master:9000"), conf);
		this.setFileDictionary(DictFileURL);
		this.dicReaderin = fs.open(new Path("hdfs://192.168.31.172:9000" + DictFileURL));
		this.charReaderin = fs.open(new Path("hdfs://192.168.31.172:9000" + CharFileURL));
		//this.setDicReader(new LineReader(new FileInputStream(this.getFileDictionary())));
		//this.setCharReader(new LineReader(new FileInputStream(this.getFileChar())));
		this.setDicReader(new LineReader(dicReaderin));
		this.setCharReader(new LineReader(charReaderin));
		//this.setDicReader();
		//this.setCharReader();
	}

	@Override
	public final String readFileByLines(LineReader reader) {
		// 文件一次读一整行
		//String tempString = null;
		Text tempText = new Text();
		// int Line = 1; //

		// 执行一次读取，如果读到内容则返回，若未读到则返回NULL
		try {
			//System.out.println(this.getFileDictionary().getAbsolutePath());
			reader.readLine(tempText);
			//System.out.println(tempText.toString());
			if (tempText.toString().equals("")) {
				// Line++;
				return null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tempText.toString();

	}

	@Override
	public void writeFileByLines(WordDictionary WordDict) {
		// TODO Auto-generated method stub

	}

	@Override
	public void putFileToDict(WordDictionary wordDict) throws IOException {
		// TODO Auto-generated method stub
		int max = 0;
		for (String tempString = this.readFileByLines(this.getDicReader()); null != tempString; tempString = this
				.readFileByLines(this.getDicReader())) {
			String[] keyValue = tempString.split("\\t");
			max = keyValue[0].length() > max ? keyValue[0].length() : max;
			if (1 == keyValue.length) {
				wordDict.appendWord(new Word(keyValue[0]),
						0.0);
			} else {
				wordDict.appendWord(new Word(keyValue[0]),
						Double.parseDouble(keyValue[1]));
			}

		}
		IOUtils.closeStream(dicReaderin);
		IOUtils.closeStream(charReaderin);
		wordDict.setMaxLength(max);
	}

	@SuppressWarnings("unchecked")
	public <E> void putFileToDict(CharDictionary<E> charDict) {
		// TODO Auto-generated method stub
		for (String tempString = this.readFileByLines(this.getCharReader()); null != tempString; tempString = this
				.readFileByLines(this.getCharReader())) {
			// @SuppressWarnings("unchecked")
			// E[] key = (E[]) tempString.split("\\n");
			charDict.appendChar((E) tempString);
		}
	}

	@SuppressWarnings("unchecked")
	public <E> void putFileToDict(WordDictionary wordDict,
			CharDictionary<E> charDict) {
		// TODO Auto-generated method stub
		int max = 0;
		for (String tempString = this.readFileByLines(this.getDicReader()); null != tempString; tempString = this
				.readFileByLines(this.getDicReader())) {
			String[] keyValue = tempString.split("\\t");
			max = keyValue[0].length() > max ? keyValue[0].length() : max;
			if (1 == keyValue.length) {
				wordDict.appendWord(new Word(keyValue[0]),
						0.0);
			} else {
				wordDict.appendWord(new Word(keyValue[0]),
						Double.parseDouble(keyValue[1]));
			}
		}
		for (String tempString = this.readFileByLines(this.getCharReader()); null != tempString; tempString = this
				.readFileByLines(this.getCharReader())) {
			// @SuppressWarnings("unchecked")
			// E[] key = (E[]) tempString.split("\\n");
			charDict.appendChar((E) tempString);
		}
		wordDict.setMaxLength(max);
	}


	/*
	 * public void readerClose(){ try { reader.close(); } catch (IOException e)
	 * { // TODO Auto-generated catch block e.printStackTrace(); } }
	 */

}
