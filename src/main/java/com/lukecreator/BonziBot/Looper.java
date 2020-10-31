package com.lukecreator.BonziBot;

public class Looper<T> {

	int loc = -1;
	T[] arr;
	
	@SafeVarargs
	public Looper(T...arr) {
		this.arr = arr;
	}
	
	public T moveNext() {
		loc++;
		if(loc >= arr.length) {
			loc = 0;
		}
		return arr[loc];
	}
	public T peek() {
		return arr[loc];
	}
	public T first() {
		return arr[0];
	}
	public T last() {
		return arr[arr.length-1];
	}
	public T get(int i) {
		return arr[i];
	}
}
