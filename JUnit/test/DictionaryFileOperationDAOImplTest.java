package test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.junit.Test;

import com.AddressSegment.metadata.model.CharDictionary;
import com.AddressSegment.metadata.model.WordDictionary;
import com.AddressSegment.tool.dao.impl.DictionaryFileOperationDAOImpl;
import com.AddressSegment.util.Config;

public class DictionaryFileOperationDAOImplTest {

	@Test
	public void testPutFileToDictionary() throws IOException, URISyntaxException {
		WordDictionary wordDict = new WordDictionary();
		CharDictionary<String> charDict = new CharDictionary<String>();
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create("hdfs://192.168.31.172:9000"), conf);
		
		DictionaryFileOperationDAOImpl DF = new DictionaryFileOperationDAOImpl(
				Config.getDefaultDictionaryURL(), Config.getCharDictionaryURL(), fs);
		
		
		DF.putFileToDict(wordDict);
		DF.putFileToDict(charDict);
		
		wordDict.output();
		charDict.output();
	}

}
