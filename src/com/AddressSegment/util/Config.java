package com.AddressSegment.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public final class Config {
	private static final Properties props = new Properties();
	
	static{
		try {
			props.load(new FileInputStream("config/config.properties"));
			
		} catch (FileNotFoundException e) {
			System.out.println("Cannot Find the Configuration File, use default.");
			
			try {
				props.load(Config.class.getResourceAsStream("config.properties"));
			} catch (IOException e1) {
				System.out.println("Load default configuration failed.");
				System.exit(-1);
			}
		} catch (IOException e) {
			System.out.println("Load user's configuration failed.");
			System.exit(-1);
		}
	}
		
	public static String getDatabaseDriver(){
		return props.getProperty("DRIVER");
	}
	
	public static String getDatabaseUrl(){
		return props.getProperty("URL");
	}
	
	public static String getDatabaseUsername(){
		return props.getProperty("USERNAME");
	}
	
	public static String getDatabasePassword(){
		return props.getProperty("PASSWORD");
	}
	
	public static String getDefaultDictionaryURL(){
		return props.getProperty("DEFAULTDICURL");
	}
	
	public static String getCharDictionaryURL(){
		return props.getProperty("CHARSETDICURL");
	}
	
	public static String getGaodeUrl(){
		return props.getProperty("GAODE_URL");
	}
	
	public static String getGaodeResponseType(){
		return props.getProperty("GAODE_RESPOND_TYPE");
	}
	
	public static String getGaodeKEY(){
		return props.getProperty("GAODE_KEY");
	}
	
	public static String getDefaultDictionaryHDFSURL(){
		return props.getProperty("DEFAULTDICURLHDFS");
	}
	
	public static String getCharDictionaryHDFSURL(){
		return props.getProperty("CHARSETDICURLHDFS");
	}
	
	public static String getHDFS_DIR(){
		return props.getProperty("HDFS_DIR");
	}

}
