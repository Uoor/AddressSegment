package com.AddressSegment.metadata.model;

import java.util.HashSet;
import java.util.Iterator;

public class CharDictionary<E> extends BaseModel {
	private HashSet<E> CharSet = null;
	
	public CharDictionary(){
		CharSet = new HashSet<E>();
	}

	public HashSet<E> getCharSet() {
		return CharSet;
	}

	public void setCharSet(HashSet<E> charSet) {
		CharSet = charSet;
	}

	public int getSize() {
		return this.getCharSet().size();
	}

	public boolean appendChar(E Character) {
		return this.getCharSet().add(Character);
	}

	public boolean searchChar(E Character){
		return this.getCharSet().contains(Character);
	}
	
	public void output(){
		Iterator<?> iter = this.getCharSet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			System.out.println(key);
		}
	}
}
