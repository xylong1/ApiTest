package com.len.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class GetConfigUtil {
	static String app_path = null;
	public static ResourceBundle testProperties = null;

	public static ResourceBundle getTestProperties(String file) {
		if (testProperties != null)
			return testProperties;
		testProperties = getProperties("/config/" + file + ".properties");
		return testProperties;
	}

	/**
	 * Returns the property defined in test.properties with the specified key.
	 * 
	 * @throws Exception
	 */
	public static String getConfigPropertyByNameAndKey(String file, String key) {
		ResourceBundle bundle = getTestProperties(file);
		return bundle.getString(key);
	}

	public static String getBasePath() {
		if (app_path == null) {
			app_path = System.getProperty("user.dir");
		}
		return app_path;
	}

	public static ResourceBundle getProperties(String location) {

		PropertyResourceBundle bundle = null;
		try {
			String path = getBasePath();
			InputStream in = new BufferedInputStream(new FileInputStream(path + location));
			bundle = new PropertyResourceBundle(in);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return bundle;
	}

	public static JSONObject getJsonFromResource(String fileName) {
		try {
			return JSONObject.parseObject(GetConfigUtil.class.getClassLoader().getResourceAsStream(fileName),
					JSONObject.class);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 读取文件返回数组
	 *
	 */
	public static JSONArray getJsonArrayFromResource(String fileName) {
		try {
			return JSONArray.parseObject(GetConfigUtil.class.getClassLoader().getResourceAsStream(fileName),
					JSONArray.class);

		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static Properties getPropertiesFromResource(String fileName) {
		Properties properties;
		try {
			properties = new Properties();
			InputStream in = GetConfigUtil.class.getClassLoader().getResourceAsStream(fileName + ".properties");
			properties.load(in);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		return properties;
	}

}
