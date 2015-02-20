package com.softprodigy.librarymsspro;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.widget.Toast;

public class Webservices {

	static SharedPreferences TOKEN_PREF;

	static Toast msg;
	static String result;
	static InputStream is = null;
	static TrustManager[] trustManagers;
	public static String resultString, Exception;

	public static String ApiCall(String url, JSONObject json, Context ct)
			throws IOException {
		URL urlNew = new URL(url);
		InputStream inputStream = null;
		String json2 = json.toString();
		if (url.contains("https")) {//checking if url contains Https url or not..
			allowAllSSL();// allows to pass the ssl certificate 
			HttpsURLConnection conn = (HttpsURLConnection) urlNew
					.openConnection();
			conn.setReadTimeout(60000); // //milliseconds
			conn.setConnectTimeout(60000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-type", "application/json");
			conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			conn.setFixedLengthStreamingMode(json2.getBytes().length);
			conn.setUseCaches(false);
			DataOutputStream outputStream = new DataOutputStream(
					conn.getOutputStream());
			outputStream.writeBytes(json2);
			outputStream.flush();
			outputStream.close();
			conn.connect();
			inputStream = conn.getInputStream();
			if (inputStream != null) {
				result = convertStreamToString(inputStream);//converting inputstream to string 
				System.out.println(url+"-----------------" + result);
				conn.disconnect();
			} else {
			}
		} else {
			HttpParams httpParameters = new BasicHttpParams();
			// int timeoutConnection = 3000;
			int timeoutConnection = 129000;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			int timeoutSocket = 130000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
			HttpPost httpPostRequest = new HttpPost(url);

			StringEntity se;
			se = new StringEntity(json.toString());
			httpPostRequest.setEntity(se);
			httpPostRequest.setHeader("Accept", "application/json");
			httpPostRequest.setHeader("Content-type", "application/json");

			HttpResponse response = (HttpResponse) httpclient
					.execute(httpPostRequest);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				// Read the content stream
				InputStream instream = entity.getContent();
				// convert content stream to a String
				result = convertStreamToString(instream);
				System.out.println(url+"-----------------" + result);
				instream.close();
			}
		}

		return result;
	}

	public static String ApiCallGet(String url) {
		try {

			Exception = "false";
			String urlnew = url;

			if (url.contains("https")) {//checking if url contains Https url or not..
				allowAllSSL();// allows to pass the ssl certificate 
				HttpsURLConnection conn = null;
				URL url2 = new URL(url);
				conn = (HttpsURLConnection) url2.openConnection();
				conn.setReadTimeout(60000); // //milliseconds
				conn.setConnectTimeout(60000);
				conn.setRequestMethod("GET");
				conn.setDoInput(true);
				conn.connect();
				is = conn.getInputStream();
				if (is != null) {
					result = convertStreamToString(is);
					System.out.println(url+"-----------------" + result);
					conn.disconnect();
				}
			} else {
				HttpParams httpParameters = new BasicHttpParams();
				int timeoutConnection = 30000;
				HttpConnectionParams.setConnectionTimeout(httpParameters,
						timeoutConnection);
				int timeoutSocket = 50000;
				HttpConnectionParams
						.setSoTimeout(httpParameters, timeoutSocket);
				DefaultHttpClient httpclient = new DefaultHttpClient(
						httpParameters);
				HttpGet httpget = new HttpGet(urlnew);
				httpget.setHeader("Accept", "application/json");
				httpget.setHeader("Content-type", "application/json");
				// httpget.setHeader("Authorization",
				// "ApiKey "+TOKEN_PREF.getString("DEVICE_USER_NAME","NOTHING")+":"+TOKEN_PREF.getString("API_KEY","NOTHING"));
				// httpget.setHeader("Authorization",
				// "ApiKey testing_application:13ec21837c64890527713e8f4cd86e1a8dac646a");
				HttpResponse response = (HttpResponse) httpclient
						.execute(httpget);
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream instream = entity.getContent();
					result = convertStreamToString(instream);
					System.out.println(url+"-----------------" + result);
					instream.close();
				}
			}

		} catch (Exception e) {
			result = "true";
			e.printStackTrace();
		}
		return result;
	}

	public static String ApiCallGet(String url, Context ctx) {

		try {
			// TOKEN_PREF= ctx.getSharedPreferences("SAVING_TOKEN_PREF",
			// ctx.MODE_WORLD_READABLE);
			// url="http://tourvox.herokuapp.com/api/v1/waypoints/?lat1=37.819586&lon1=-122.478532&lat2=37.802358&lon2=-122.405806&limit=5";
			// url="http://tourvox.herokuapp.com/api/v1/waypoints/?lat1=4.879&lon1=52.3519&lat2=4.7976&lon2=52.4068&limit=5";
			// url="http://tourvox.herokuapp.com/api/v1/waypoints/?lat1=52.095&lon1=4.8791&lat2=53&lon2=5&limit=5";
			// url="http://tourvox.herokuapp.com/api/v1/waypoints/4f96605a3ad10aa9620a7358";
			Exception = "false";
			String urlnew = url;
			if (url.contains("https")) {//checking if url contains Https url or not..
				allowAllSSL();// allows to pass the ssl certificate 
				HttpsURLConnection conn = null;
				URL url2 = new URL(url);
				conn = (HttpsURLConnection) url2.openConnection();
				conn.setReadTimeout(60000); // //milliseconds
				conn.setConnectTimeout(60000);
				conn.setRequestMethod("GET");
				conn.setDoInput(true);
				conn.connect();
				is = conn.getInputStream();
				if (is != null) {
					result = convertStreamToString(is);
					System.out.println(url+"-----------------" + result);
					conn.disconnect();
				}
			} else {
				HttpParams httpParameters = new BasicHttpParams();
				int timeoutConnection = 30000;
				HttpConnectionParams.setConnectionTimeout(httpParameters,
						timeoutConnection);
				int timeoutSocket = 50000;
				HttpConnectionParams
						.setSoTimeout(httpParameters, timeoutSocket);
				DefaultHttpClient httpclient = new DefaultHttpClient(
						httpParameters);
				HttpGet httpget = new HttpGet(urlnew);
				httpget.setHeader("Accept", "application/json");
				httpget.setHeader("Content-type", "application/json");
				// httpget.setHeader("Authorization",
				// "ApiKey Goel:bf66107cc07a10deb546d9fe533360a3722cca1f");
				// httpget.setHeader("Authorization",
				// "ApiKey "+TOKEN_PREF.getString("DEVICE_USER_NAME","NOTHING")+":"+TOKEN_PREF.getString("API_KEY","NOTHING"));
				HttpResponse response;
				response = (HttpResponse) httpclient.execute(httpget);
				System.out.println("----RESPONSE----" + response);
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream instream = entity.getContent();

					result = convertStreamToString(instream);
					instream.close();
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static void toastmessage(Context tourMapTest, int string) {
		msg = Toast.makeText(tourMapTest, string, Toast.LENGTH_LONG);
		msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
				msg.getYOffset() / 2);
		msg.show();

	}

	public static final boolean isInternetOn(Context c) {
		ConnectivityManager connec = (ConnectivityManager) c
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		try {

			// NetworkInfo wifiNetwork =
			// connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			// if (wifiNetwork != null)
			// {
			// if(wifiNetwork.isConnected()) {
			// return true;
			// }
			// }
			//
			// NetworkInfo mobileNetwork =
			// connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			// if (mobileNetwork != null)
			// {
			// if(mobileNetwork.isConnected()) {
			// return true;
			// }
			// }

			NetworkInfo activeNetwork = connec.getActiveNetworkInfo();
			if (activeNetwork != null) {
				if (activeNetwork.isConnected()) {
					return true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return false;

	}

	public static void allowAllSSL() {

		javax.net.ssl.HttpsURLConnection
				.setDefaultHostnameVerifier(new HostnameVerifier() {
					@Override
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
				});

		javax.net.ssl.SSLContext context = null;

		if (trustManagers == null) {
			trustManagers = new javax.net.ssl.TrustManager[] { new _FakeX509TrustManager() };
		}

		try {
			context = javax.net.ssl.SSLContext.getInstance("TLS");
			context.init(null, trustManagers, new SecureRandom());
		} catch (NoSuchAlgorithmException e) {

		} catch (KeyManagementException e) {

		}
		javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(context
				.getSocketFactory());
	}

	public static class _FakeX509TrustManager implements
			javax.net.ssl.X509TrustManager {
		private java.security.cert.X509Certificate[] _AcceptedIssuers = {};

		public void checkClientTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
		}

		public boolean isClientTrusted(X509Certificate[] chain) {
			return (true);
		}

		public boolean isServerTrusted(X509Certificate[] chain) {
			return (true);
		}

		@Override
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return (_AcceptedIssuers);
		}

		@Override
		public void checkClientTrusted(
				java.security.cert.X509Certificate[] chain, String authType)
				throws java.security.cert.CertificateException {

		}

		@Override
		public void checkServerTrusted(
				java.security.cert.X509Certificate[] chain, String authType)
				throws java.security.cert.CertificateException {

		}
	}

	public static void StringToastMessage(Context tourMapTest, String string) {
		msg = Toast.makeText(tourMapTest, string, Toast.LENGTH_LONG);
		msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
				msg.getYOffset() / 2);
		msg.show();
	}
}
