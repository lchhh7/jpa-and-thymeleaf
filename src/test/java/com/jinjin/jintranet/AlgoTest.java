package com.jinjin.jintranet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

public class AlgoTest {

	@Test
	public void test1() {
		List<Integer> list = new ArrayList<>();
		List<Integer> list2 = new ArrayList<>();
		list.add(3);
		list.add(1);
		list.add(2);
		list.add(2);
		list.add(3);
		list.add(4);
		
		list2.add(3);
		list2.add(1);
		list2.add(2);
		list2.add(2);
		list2.add(3);
		list2.add(4);
		
		System.out.println(list);
		
		list.remove(3);
		
		System.out.println(list);
		
		System.out.println(list2);
		
		list2.remove((Integer) 3);
		
		System.out.println(list2);
		
		int a = Collections.frequency(list, 3);
		System.out.println(a);
		
		Integer[] b = list.toArray(new Integer[0]);
	
	}
}
