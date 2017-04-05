package com.AddressSegment.tool.dao.declare;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.hadoop.fs.FileSystem;
import com.AddressSegment.logic.AlgorithmDaoImpl;
import com.AddressSegment.logic.UndefinedWordRecognize;
import com.AddressSegment.logic.service.AddressTageMaking;
import com.AddressSegment.metadata.model.CharDictionary;
import com.AddressSegment.metadata.model.WordDictionary;

public class DoubleLevenshteinAlgorithm {
	public static AlgorithmDaoImpl a = null;
	public static FileSystem fs = null;
	public static WordDictionary wordDict = null;
	public static CharDictionary<String> charDict = null;
	
	public DoubleLevenshteinAlgorithm(WordDictionary WDInput, CharDictionary<String> CDInput, FileSystem FSInput) throws URISyntaxException, IOException {
		wordDict = WDInput;
		charDict = CDInput;
		fs = FSInput;
		a = new AlgorithmDaoImpl(wordDict, charDict, fs);
	}

	public float run(String S, String T) {
		float result = 0;
		UndefinedWordRecognize uwr1 = new UndefinedWordRecognize();
		UndefinedWordRecognize uwr2 = new UndefinedWordRecognize();
		ArrayList<String> arrayS = uwr1.getUndefinedWord(a.runRMM(S));
		ArrayList<String> arrayT = uwr2.getUndefinedWord(a.runRMM(T));
		result = this.Levenshtein(arrayS, arrayT);
		return result;
	}

	public float Levenshtein(ArrayList<?> S, ArrayList<?> T) {
		int intS = S.size();
		int intT = T.size();
		float temp = 0;

		float[][] floatMatrix = new float[intS + 1][intT + 1];

		/*
		 * for (int i = 0; i <= intS; i++) floatMatrix[i][0] = i; for (int i =
		 * 0; i <= intT; i++) floatMatrix[0][i] = i;
		 */

		for (int i = 0; i <= intS; i++) {
			if (0 != i) {
				floatMatrix[i][0] = floatMatrix[i - 1][0] + this.getTageWeight(S.get(i - 1).toString());
			} else {
				floatMatrix[i][0] = 0;
			}
		}
		for (int i = 0; i <= intT; i++) {
			if (0 != i) {
				floatMatrix[0][i] = floatMatrix[0][i - 1] + this.getTageWeight(T.get(i - 1).toString());
			} else {
				floatMatrix[0][i] = 0;
			}
		}

		for (int j = 1; j <= intS; j++) {
			for (int k = 1; k <= intT; k++) {
				int intAddressWeight = getTageWeight(S.get(j - 1).toString());
				if (S.get(j - 1).toString().length() == T.get(k - 1).toString().length()) {
					if (S.get(j - 1).toString().equals(T.get(k - 1).toString())) {
						temp = 0;
					} else {
						temp = (1 - Levenshtein(S.get(j - 1).toString(), T.get(k - 1).toString())) * intAddressWeight;
					}
				} else {
					temp = (1 - Levenshtein(S.get(j - 1).toString(), T.get(k - 1).toString())) * intAddressWeight;
				}
				floatMatrix[j][k] = min(floatMatrix[j - 1][k - 1] + temp, floatMatrix[j][k - 1] + intAddressWeight,
						floatMatrix[j - 1][k] + intAddressWeight);
			}
		}
		float similarity = 1
				- ((float) floatMatrix[intS][intT] / Math.max(this.CountAddressWeight(S), this.CountAddressWeight(T)));
		return similarity;
	}

	public float Levenshtein(String S, String T) {
		int intS = S.length();
		int intT = T.length();
		int temp = 0;

		int[][] intMatrix = new int[intS + 1][intT + 1];

		for (int i = 0; i <= intS; i++)
			intMatrix[i][0] = i;
		for (int i = 0; i <= intT; i++)
			intMatrix[0][i] = i;

		for (int j = 1; j <= intS; j++) {
			for (int k = 1; k <= intT; k++) {
				if (S.charAt(j - 1) == T.charAt(k - 1)) {
					temp = 0;
				} else {
					temp = 1;
				}
				intMatrix[j][k] = min(intMatrix[j - 1][k - 1] + temp, intMatrix[j][k - 1] + 1, intMatrix[j - 1][k] + 1);
			}
		}
		float similarity = 1 - ((float) intMatrix[intS][intT] / Math.max(intS, intT));
		return similarity;
	}

	private static int min(int... is) {
		int min = Integer.MAX_VALUE;
		for (int i : is) {
			if (min > i) {
				min = i;
			}
		}
		return min;
	}

	private static float min(float... is) {
		float min = Integer.MAX_VALUE;
		for (float i : is) {
			if (min > i) {
				min = i;
			}
		}
		return min;
	}

	public int getTageWeight(String strInput) {
		AddressTageMaking Tage = new AddressTageMaking();
		switch (Tage.TageMaking(strInput)) {
		case "RE":
			return 200;
		case "RO":
			return 300;
		case "NU":
			return 100;
		case "NUB":
			return 50;
		case "NUM":
			return 20;
		case "NUS":
			return 10;
		case "POI":
			return 150;
		case "OT":
			return 5;
		default:
			return 0;
		}

	}

	public int CountAddressWeight(ArrayList<?> AddressList) {
		int intResult = 0;
		for (Object it : AddressList) {
			intResult += this.getTageWeight(it.toString());
		}
		return intResult;
	}

}
