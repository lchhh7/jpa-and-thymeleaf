package com.jinjin.jintranet;

public abstract class abstractTest {
	
	String title;
	String nm;
	
	public abstract void sendMsg(String content);
	
	public abstract void plus();
	
	public static void hello() {
		System.out.println("hello");
	}
}
