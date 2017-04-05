package com.AddressSegment.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.AddressSegment.logic.service.UndefinedWordRecognizeInterface;
import com.AddressSegment.tool.dao.impl.AddressRegexImpl;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;

public class UndefinedWordRecognize implements UndefinedWordRecognizeInterface {
	protected ArrayList<String> arraylistResult = new ArrayList<String>();
	protected AddressRegexImpl ari = new AddressRegexImpl();
	String strWordString = null;

	@Override
	public ArrayList<String> getUndefinedWord(ArrayList<String> segmentWord) {
		// TODO Auto-generated method stub
		int k = 0;
		int a = 0;

		strWordString = arrayListToCode(segmentWord);
		ari.matchUnidentifiedWord(strWordString);

		for (; ari.findMatchWord();) {
			a++;
		}

		ari.resetMatch();
		ari.findMatchWord();
		for (k = 0; k < strWordString.length(); k++) {
			String strTemp = "";
			char o = '1';
			if (a > 0) {
				if (k == ari.getStart() && o == strWordString.charAt(k)) {
					for (int j = 0; j < ari.getUnidentifiedWord().length() && segmentWord.size() > ari.getStart() + j
							&& k < strWordString.length(); j++, k++) {
						if (segmentWord.get(ari.getStart() + j).equals("(")
								|| segmentWord.get(ari.getStart() + j).equals("(")
								|| segmentWord.get(ari.getStart() + j).equals(")")
								|| segmentWord.get(ari.getStart() + j).equals("（")
								|| segmentWord.get(ari.getStart() + j).equals("）")
								|| segmentWord.get(ari.getStart() + j).equals(" ")
								|| segmentWord.get(ari.getStart() + j).equals("\t")) {
						} else {
							strTemp += segmentWord.get(ari.getStart() + j);
						}
					}
					ari.findMatchWord();
					arraylistResult.add(strTemp);
					a--;
					k--;
					continue;
				}
			}
			if (k < segmentWord.size()) {
				strTemp = segmentWord.get(k);
				if (strTemp.length() <= 1) {
					if (strTemp.equals("(")
							|| strTemp.equals("(")
							|| strTemp.equals(")")
							|| strTemp.equals("（")
							|| strTemp.equals("）")
							|| strTemp.equals(" ")
							|| strTemp.equals("\t")) {
					}
					else {
						arraylistResult.add(strTemp);
					}
				} else {
					arraylistResult.add(strTemp);
				}
			}
		}
		return arraylistResult;
	}

	public ArrayList<String> getUndefinedWordWithHanlp(ArrayList<String> segmentWord) {
		// TODO Auto-generated method stub
		Segment segment = HanLP.newSegment().enableOrganizationRecognize(true).enablePlaceRecognize(true);
		strWordString = arrayListToCode(segmentWord);
		ari.matchUnidentifiedWord(strWordString);

		int k = 0;
		int a = 0;
		for (; ari.findMatchWord();) {
			a++;
		}

		ari.resetMatch();
		ari.findMatchWord();
		for (k = 0; k < strWordString.length(); k++) {
			String strTemp = "";
			char o = '1';
			if (a > 0) {
				if (k == ari.getStart() && o == strWordString.charAt(k)) {
					for (int j = 0; j < ari.getUnidentifiedWord().length() && segmentWord.size() > ari.getStart() + j
							&& k < strWordString.length(); j++, k++) {
						if (segmentWord.get(ari.getStart() + j).equals("(")
								|| segmentWord.get(ari.getStart() + j).equals("(")
								|| segmentWord.get(ari.getStart() + j).equals(")")
								|| segmentWord.get(ari.getStart() + j).equals("（")
								|| segmentWord.get(ari.getStart() + j).equals("）")
								|| segmentWord.get(ari.getStart() + j).equals(" ")
								|| segmentWord.get(ari.getStart() + j).equals("\t")) {
						} else {
							strTemp += segmentWord.get(ari.getStart() + j);
						}
					}
					ari.findMatchWord();
					List<Term> termList = segment.seg(strTemp);
					Iterator<Term> it = termList.iterator();
					while (it.hasNext()) {
						arraylistResult.add(it.next().word);
					}
					a--;
					k--;
					continue;
				}
			}
			if (k < segmentWord.size()) {
				strTemp = segmentWord.get(k);
				if (strTemp.length() <= 1) {
					if (strTemp.equals("(")
							|| strTemp.equals("(")
							|| strTemp.equals(")")
							|| strTemp.equals("（")
							|| strTemp.equals("）")
							|| strTemp.equals(" ")
							|| strTemp.equals("")
							|| strTemp.equals("\t")) {
					}
					else {
						arraylistResult.add(strTemp);
					}
				} else {
					arraylistResult.add(strTemp);
				}
			}
		}
		return arraylistResult;
	}

	@Override
	public String arrayListToCode(ArrayList<String> segmentWord) {
		// TODO Auto-generated method stub
		String strResult = "";
		Iterator<String> it = segmentWord.iterator();
		while (it.hasNext()) {
			strResult += it.next().length();
		}
		return strResult;
	}
}
