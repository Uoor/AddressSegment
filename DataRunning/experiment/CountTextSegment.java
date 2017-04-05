package experiment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CountTextSegment {

	public static int Count(String src) throws IOException {
		FileReader fr = null;
		int result = 0;
		fr = new FileReader(src);
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(src),"GBK"));
		String line = "";
		String[] arrs = null;
		while ((line = br.readLine()) != null) {
			arrs = line.split("/");
			result += arrs.length;
		}
		br.close();
		fr.close();

		return result;
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String line = "C:/Users/HYFrank/Desktop/standard.txt";
		System.out.println(Count(line));

	}

}
