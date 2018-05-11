package com.company.project.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FaultId {
    
    private final static Logger logger = LoggerFactory.getLogger(FaultId.class);

	// 返回错误ID
	public static String generateFaultId(byte[] msg) {

		String firstclass = "0";
		String secondClass = "0";
		for (int i = 0; i < 19; i++) {
			if (msg[i + 11] != 0) {
				// 确定一级故障代码
				if (i == 0) {
					logger.debug( "交流桩出现故障");
					firstclass = 1 + "";
					char[] faultChar = GetByteString.getString(msg[11]).toCharArray();
					for (int j = 1; j < faultChar.length; j++) {
						if (faultChar[j] != 0) {
							secondClass = 0 + "" + j + "" + 1;
						}
					}
				}
				if (i == 2) {
					// 充电机人身安全级别故障
					logger.debug( "出现充电机人身安全级别故障");
					firstclass = 2 + "";
					// 将byte转换成二进制char型数组
					char[] faultChar = GetByteString.getString(msg[13]).toCharArray();
					// 确定二级故障代码
					for (int j = 0; j < faultChar.length; j++) {
						if (faultChar[j] == '1') {

							secondClass = 0 + "" + (j / 2 + 1) + "" + ((j + 1) % 2 + 1);
							break;
						}
					}
				} else if (i > 2 && i < 6) {
					// 充电机设备安全级别故障
					logger.debug( "出现充电机设备安全级别故障");
					firstclass = 3 + "";

					char[] faultChar = (GetByteString.getString(msg[14]) + GetByteString.getString(msg[15])
							+ GetByteString.getString(msg[16])).toCharArray();
					for (int j = 0; j < faultChar.length; j++) {
						if (faultChar[j] == '1') {
							if (j < 18) {
								secondClass = 0 + "" + (j / 2 + 1) + "" + ((j + 1) % 2 + 1);
							} else {
								secondClass = (j / 2 + 1) + "" + ((j + 1) % 2 + 1);
							}
							break;
						}
					}
				} else if (i >= 6 && i < 9) {
					// 充电机告警提示级别故障
					logger.debug( "出现充电机告警提示级别故障");
					firstclass = 4 + "";
					char[] faultChar = (GetByteString.getString(msg[17]) + GetByteString.getString(msg[18])
							+ GetByteString.getString(msg[19])).toCharArray();

					for (int j = 0; j < faultChar.length; j++) {
						if (faultChar[j] == '1') {
							if (j < 18) {
								secondClass = 0 + "" + (j / 2 + 1) + "" + ((j + 1) % 2 + 1);
							} else {
								secondClass = (j / 2 + 1) + "" + ((j + 1) % 2 + 1);
							}
							break;
						}
					}
				} else if (i == 9) {
					// BMS人身安全级别故障
					logger.debug( "出现BMS人身安全级别故障");
					firstclass = 5 + "";
					char[] faultChar = GetByteString.getString(msg[20]).toCharArray();
					for (int j = 0; j < faultChar.length; j++) {
						if (faultChar[j] == '1') {
							secondClass = 0 + "" + (j / 2 + 1) + "" + ((j + 1) % 2 + 1);
							break;
						}
					}
				} else if (i > 9 && i < 13) {
					// BMS设备安全级别故障
					logger.debug( "出现BMS设备安全级别故障");
					firstclass = 6 + "";
					char[] faultChar = (GetByteString.getString(msg[21]) + GetByteString.getString(msg[22])
							+ GetByteString.getString(msg[23])).toCharArray();

					for (int j = 0; j < faultChar.length; j++) {
						if (faultChar[j] == '1') {
							if (j < 18) {
								secondClass = 0 + "" + (j / 2 + 1) + "" + ((j + 1) % 2 + 1);
							} else {
								secondClass = (j / 2 + 1) + "" + ((j + 1) % 2 + 1);
							}
							break;
						}
					}
				} else if (i == 13 || i == 14) {
					// BMS告警提示级别故障
					logger.debug( "出现BMS告警提示级别故障");
					firstclass = 7 + "";
					char[] faultChar = (GetByteString.getString(msg[24]) + GetByteString.getString(msg[25]))
							.toCharArray();
					for (int j = 0; j < faultChar.length; j++) {
						if (faultChar[j] == '1') {
							secondClass = 0 + "" + (j / 2 + 1) + "" + ((j + 1) % 2 + 1);
							break;
						}
					}
				} else if (i == 15 || i == 16) {
					// 充电机模块DC故障
					logger.debug( "出现充电机模块DC故障");
					firstclass = 8 + "";
					char[] faultChar = (GetByteString.getString(msg[26]) + GetByteString.getString(msg[27]))
							.toCharArray();
					for (int j = 0; j < faultChar.length; j++) {
						if (faultChar[j] == '1') {
							if (j < 9) {
								secondClass = 0 + "" + (j + 1) + "" + 1;
							} else {
								secondClass = (j + 1) + "" + 1;
							}
							break;
						}
					}
				} else if(i>16 && i< 21){
					// 充电机模块AC故障
					logger.debug( "出现充电机模块AC故障");
					firstclass = 9 + "";
					char[] faultChar = (GetByteString.getString(msg[28]) + GetByteString.getString(msg[29])
							+ GetByteString.getString(msg[30]) + GetByteString.getString(msg[31])).toCharArray();
					for (int j = 0; j < faultChar.length; j++) {
						if (faultChar[j] == '1') {
							if (j < 9) {
								secondClass = 0 + "" + (j + 1) + "" + 1;
							} else {
								secondClass = (j + 1) + "" + 1;
							}
							break;
						}
					}
				}else {
				    logger.error("未知故障"+msg.toString());
					String faultId = "unkonw:"+(i+1)+","+msg[i+11];
					return faultId;
				}
				break;
			}
		}
		// System.out.println(firstclass + secondClass);
		logger.debug( "生成的故障ID是：" + firstclass + "" + secondClass);
		return firstclass + "" + secondClass;
	}
}
