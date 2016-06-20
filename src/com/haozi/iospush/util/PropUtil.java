/**
 * Copyright (C) 2016 FuZhong
 *
 *
 * @className:com.haozi.iospush.util.PropUtil
 * @description:TODO
 * @date:2016-6-20 下午4:56:27
 * @version:v1.0.0 
 * @author:WangHao
 * 
 * Modification History:
 * Date         Author      Version     Description
 * -----------------------------------------------------------------
 * 2016-6-20     WangHao       v1.0.0        create
 *
 *
 */
package com.haozi.iospush.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @className:com.haozi.iospush.util.PropUtil
 * @description:读取配置文件工具类
 * @version:v1.0.0
 * @date:2016-6-20 下午4:56:35
 * @author:WangHao
 */
public class PropUtil
{
	private static Logger logger = Logger.getLogger(PropUtil.class);

	private final static Properties prop = new Properties();

	static
	{
		// 两种形式都可以
		// InputStream in =
		// PropUtil.class.getResourceAsStream("/com/xtwl/cfg/config.properties");
		InputStream in = PropUtil.class.getClassLoader().getResourceAsStream("config.properties");
		try
		{
			prop.load(in);
			try
			{
				Thread.sleep(400);
			} catch (InterruptedException e)
			{
			}
		} catch (NullPointerException e)
		{
			logger.error("NullPointer异常", e);
		} catch (IOException e)
		{
			logger.error("IO异常", e);
		} finally
		{
			try
			{
				if (in != null)
					in.close();
			} catch (IOException e)
			{
				logger.error("IO异常", e);
			}
		}
	}
	
	public static String getValue(String key) {
		String value = prop.getProperty(key);
		return (value!=null && !"".equals(value))?prop.getProperty(key).trim():null;
	}


	public static void main(String[] args) {
	
		
		System.out.println(getValue("certPassWord"));

	}
}
