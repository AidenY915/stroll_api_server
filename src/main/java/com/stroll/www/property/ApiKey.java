package com.stroll.www.property;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApiKey {
	public static String kakaoApiKey;
	static {
        try (InputStream is = ApiKey.class.getClassLoader().getResourceAsStream("config/api_key.properties")) {
        	Properties prop = new Properties();
            prop.load(is);
            kakaoApiKey = prop.getProperty("kakao_api_key");
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
