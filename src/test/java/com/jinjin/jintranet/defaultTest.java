package com.jinjin.jintranet;

public interface defaultTest{
	public int test1();
	
	public int test2();
	
	public int test3();
	
	public int test4();
	
	default String defaultString() {
		return "default";
	}
}
