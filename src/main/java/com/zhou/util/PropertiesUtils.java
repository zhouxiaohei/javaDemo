package com.zhou.util;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {

	public static Properties readProperties(String fileName) {
		Properties props = new Properties();
		try {
			InputStream fis = PropertiesUtils.class.getClassLoader().getResourceAsStream(fileName);
			props.load(fis);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return props;
	}

	public static void main(String[] args) {
		Properties props = readProperties("jdbc.properties");
		System.out.println(props.getProperty("jdbc.url"));
		System.out.println(props.getProperty("jdbc.username"));
		System.out.println(props.getProperty("jdbc.password"));
	}
}
