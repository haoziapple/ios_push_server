/**
 * Copyright (C) 2016 FuZhong
 *
 *
 * @className:com.haozi.iospush.socket.PushSocket
 * @description:TODO
 * @date:2016-6-20 下午4:52:44
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
package com.haozi.iospush.socket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import com.haozi.iospush.util.Makebyte;
import com.haozi.iospush.util.MyX509TrustManager;
import com.haozi.iospush.util.PropUtil;

/**
 * @className:com.haozi.iospush.socket.PushSocket
 * @description:使用建立socket方式进行推送
 * @version:v1.0.0
 * @date:2016-6-20 下午4:52:55
 * @author:WangHao
 */
public class PushSocket
{
	public static SSLSocket getClientSocekt()
	{

		SSLSocket client = null;
		try
		{
			// ----------加载证书开始----------
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			FileInputStream instream = new FileInputStream(new File(PropUtil.getValue("certPath")));
			try
			{
				keyStore.load(instream, PropUtil.getValue("certPassWord").toCharArray());
			} catch (NoSuchAlgorithmException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CertificateException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally
			{
				instream.close();
			}

			// 创建管理JKS密钥库的X.509密钥管理器
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(keyStore, PropUtil.getValue("certPassWord").toCharArray());

			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			MyX509TrustManager myxtm = new MyX509TrustManager();
			X509TrustManager[] xtmArray = new X509TrustManager[] { myxtm };

			// 构造SSL环境，指定SSL版本为3.0，也可以使用TLSv1，但是SSLv3更加常用
			SSLContext sslContext = SSLContext.getInstance("TLSv1", "SunJSSE");
			// SSLContext sslContext = SSLContext.getInstance("TLSv1");
			sslContext.init(kmf.getKeyManagers(), xtmArray, new SecureRandom());

			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			// ----------加载证书结束----------

			// ----------建立连接开始----------
			// gateway.push.apple.com 生产
			// gateway.sandbox.push.apple.com 测试
			client = (SSLSocket) ssf.createSocket(PropUtil.getValue("pushIp"),
					Integer.parseInt(PropUtil.getValue("pushPort")));

			// new String[]{"SSLv3", "TLSv1"}
			client.setEnabledProtocols(new String[] { "TLSv1" });
			client.setSoTimeout(100000);// 读取数据超时100秒
			client.setSendBufferSize(32 * 1024);// 设置socket发包缓冲为32K
			client.setReceiveBufferSize(32 * 1024);// 设置socket底层接收缓冲为32K
		} catch (KeyStoreException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return client;
	}

	public void pushMessage(String message, String deviceToken)
	{
		SSLSocket client = getClientSocekt();
		DataInputStream in = null;
		DataOutputStream out = null;

		try
		{
			out = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));// 输出
			in = new DataInputStream(new BufferedInputStream(client.getInputStream()));// 输入

			byte[] msgByte = Makebyte.makebyte((byte) 1, deviceToken, message, 53462341);
			out.write(msgByte);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			try
			{
				out.close();
				in.close();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
