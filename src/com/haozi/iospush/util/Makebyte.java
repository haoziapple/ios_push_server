/**
 * Copyright (C) 2016 FuZhong
 *
 *
 * @className:com.haozi.iospush.util.Makebyte
 * @description:TODO
 * @date:2016-6-20 下午5:15:17
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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

/**
 * @className:com.haozi.iospush.util.Makebyte
 * @description:将输入转换为字节类型
 * @version:v1.0.0
 * @date:2016-6-20 下午5:15:22
 * @author:WangHao
 */
public class Makebyte
{
	public static byte[] makebyte(byte command, String deviceToken, String payload, int identifer)
	{

		byte[] deviceTokenb = decodeHex(deviceToken);
		byte[] payloadBytes = null;
		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(boas);

		try
		{
			payloadBytes = payload.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			return null;
		}

		try
		{
			dos.writeByte(command);
			dos.writeInt(identifer);// identifer
			dos.writeInt(Integer.MAX_VALUE);// 过去时间
			dos.writeShort(deviceTokenb.length);
			dos.write(deviceTokenb);
			dos.writeShort(payloadBytes.length);
			dos.write(payloadBytes);
			return boas.toByteArray();
		} catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private static final Pattern pattern = Pattern.compile("[ -]");

	private static byte[] decodeHex(String deviceToken)
	{
		String hex = pattern.matcher(deviceToken).replaceAll("");

		byte[] bts = new byte[hex.length() / 2];
		for (int i = 0; i < bts.length; i++)
		{
			bts[i] = (byte) (charval(hex.charAt(2 * i)) * 16 + charval(hex.charAt(2 * i + 1)));
		}
		return bts;
	}

	private static int charval(char a)
	{
		if ('0' <= a && a <= '9')
			return (a - '0');
		else if ('a' <= a && a <= 'f')
			return (a - 'a') + 10;
		else if ('A' <= a && a <= 'F')
			return (a - 'A') + 10;
		else
		{
			throw new RuntimeException("Invalid hex character: " + a);
		}
	}
}
