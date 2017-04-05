package com.AddressSegment.tool.dao.impl;

import java.util.ArrayList;


public class Stack<E> {
	private ArrayList<E> List = null; 
	int cursor = 0;
	
	public Stack() {
		List = new ArrayList<E>();
	}
	
	
	
	public ArrayList<E> getList() {
		return List;
	}



	public boolean isEmpty() {
		return this.getList().isEmpty();
	}
	
	public void push(E item) {
		this.getList().add(item);
	}
	
	public E pop() {
		//E popdata = ;
		return List.remove(this.getList().size()-1);
	}
	
	public E peek() {
		return this.getList().get(this.getList().size()-1);
	}
	
	public E now(){
		return this.getList().get(cursor);
	}
	
	public void next(){
		cursor += 1;
	};
	
	public void clear(){
		this.getList().clear();
		cursor = 0;
		//System.out.println("clear!");
	}

}
