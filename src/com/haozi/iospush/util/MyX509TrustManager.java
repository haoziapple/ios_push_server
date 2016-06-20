/**
 * Copyright (C) 2016 FuZhong
 *
 *
 * @className:com.haozi.iospush.util.MyX509TrustManager
 * @description:TODO
 * @date:2016-6-20 下午5:07:48
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

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class MyX509TrustManager implements X509TrustManager
{

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
	{

		System.out.println("cert size -------------------------------------------- " + chain.length);
		for (X509Certificate value : chain)
		{
			System.out.println("cert -------------------------------------------- " + value.toString()
					+ ", authType -------------------------------------------- " + authType);
		}

	}

	@Override
	public X509Certificate[] getAcceptedIssuers()
	{
		return null;
	}

}
