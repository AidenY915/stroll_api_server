package com.stroll.www.property;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AwsProps {
	public static String s3AccessKeyId = null;
	public static String s3SecretAccessKey = null;
	public static String s3Bucket = null;
	public static String s3Region = null;
	
	static {
        try (InputStream is = AwsProps.class.getClassLoader().getResourceAsStream("config/aws.properties")) { //컴파일 전 프로젝트에서의 경로와 컴파일 이후의 경로(classPath)가 다르기 때문
        	Properties prop = new Properties();
        	prop.load(is);
            s3AccessKeyId = prop.getProperty("s3.accessKeyId");
            s3SecretAccessKey = prop.getProperty("s3.secretAccessKey");
            s3Bucket = prop.getProperty("s3.bucket");
            s3Region = prop.getProperty("s3.region");
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
