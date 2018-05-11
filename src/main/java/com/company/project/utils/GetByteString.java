package com.company.project.utils;

public class GetByteString {

	public static String getString(byte msg){
		String returnInfo = "" + (byte) (((msg & 0xff) >> 7) & 0x1) + (byte) (((msg & 0xff) >> 6) & 0x1)
				+ (byte) (((msg & 0xff) >> 5) & 0x1) + (byte) (((msg & 0xff) >> 4) & 0x1)
				+ (byte) ((msg >> 3) & 0x1) + (byte) ((msg >> 2) & 0x1) + (byte) ((msg >> 1) & 0x1)
				+ (byte) ((msg >> 0) & 0x1);
		//System.out.println(returnInfo);
		return returnInfo;
	}

}
