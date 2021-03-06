package com.zheng.weixin.kit;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.zheng.weixin.json.ErrorMsg;

/**
 * 微信请求的工具类
 *
 * @author zhenglian
 * @data 2016年1月3日 下午8:53:53
 */
public class WeixinReqKit {

	public static String reqPostJson(String url, String data) {
		return reqPost(url, data, "application/json");
	}

	public static String reqPostXml(String url, String data) {
		return reqPost(url, data, "application/xml");
	}

	/**
	 * 微信post请求
	 *
	 * @author zhenglian
	 * @data 2016年1月3日 下午8:54:25
	 * @param url
	 * @param data
	 * @param type
	 * @return
	 */
	public static String reqPost(String url, String data, String type) {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-Type", type + ";charset=UTF-8");
		StringEntity entity = new StringEntity(data, ContentType.create(type,
				Charset.forName("UTF-8")));
		post.setEntity(entity);
		CloseableHttpResponse resp = null;
		try {
			resp = client.execute(post);
			int sc = resp.getStatusLine().getStatusCode();
			if (sc >= 200 && sc < 300) {
				String result = EntityUtils.toString(resp.getEntity());
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (resp != null) {
				try {
					resp.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	/**
	 * 微信get请求
	 *
	 * @author zhenglian
	 * @data 2016年1月3日 下午9:00:56
	 * @param url
	 * @return
	 */
	public static String reqGet(String url) {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet get = new HttpGet(url);
		CloseableHttpResponse resp = null;
		try {
			resp = client.execute(get);
			int sc = resp.getStatusLine().getStatusCode();
			if (sc >= 200 && sc < 300) {
				String result = EntityUtils.toString(resp.getEntity(), Charset.forName("UTF-8"));
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (resp != null) {
				try {
					resp.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}
	
	/**
	 * 判断请求是否成功，返回的数据是否正常
	 * 正常情况分为两种
	 * 1.转化为指定的类型对象出错，但是转化为ErrorResp成功且errorcode=0
	 * 2.转化为指定类型的对象成功
	 *
	 * @author zhenglian
	 * @data 2016年1月7日 下午10:57:09
	 * @param content
	 * @return
	 */
	public static boolean isReqSuc(String content) {
		try {
			if(!content.contains("errcode")) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			ErrorMsg msg = JsonKit.json2Obj(content, ErrorMsg.class); //错误码为0
			if(Integer.parseInt(msg.getErrcode()) == 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

}
