package com.qf.ithub.common.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;



public class StringUtils {

	//获得从指定范围到指定范围的数组
	public  static  int getCheckCode(Integer start ,Integer end) {
		//0.0 -1.0
		return (int)(Math.random()*(end-start+1)+start);
	}
	
	
	
	public static void main(String[] args) throws IOException {
		System.out.println(getCheckCode(1000,9999));
	}
}
