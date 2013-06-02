package fefzjon.ep2.bandejao.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import fefzjon.ep2.bandejao.utils.BandexConstants;

public class StoaManager {
	private static StoaManager	instance	= null;

	private boolean				isLogged;
	private String				username	= null;

	private StoaManager() {
		this.isLogged = false;
	}

	public static StoaManager getInstance() {
		if (instance == null) {
			instance = new StoaManager();
		}
		return instance;
	}

	public boolean isLogged() {
		return this.isLogged;
	}

	public void deslogar() {
		this.username = null;
		this.isLogged = false;
	}

	public boolean postLogin(final String nusp, final String password) throws NoSuchAlgorithmException,
			KeyManagementException {
		HttpClient httpclient = this.getNewHttpClient(); // new
															// DefaultHttpClient();
		HttpPost httppost = new HttpPost(BandexConstants.URL_LOGIN_STOA);
		Log.i("Bandex", "Tentando logar com " + nusp + " [" + password + "]");
		/*
		 * SSLSocketFactory sf = (SSLSocketFactory) httpclient
		 * .getConnectionManager().getSchemeRegistry().getScheme("https")
		 * .getSocketFactory(); sf.setHostnameVerifier(new
		 * AllowAllHostnameVerifier());
		 */

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("usp_id", nusp));
			nameValuePairs.add(new BasicNameValuePair("password", password));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			InputStream respData = response.getEntity().getContent();
			String result = this.convertStreamToString(respData);

			JSONObject json = new JSONObject(result);
			if (json.has("username")) {
				this.username = json.getString("username");
				Log.i("Bandex", "Username [" + this.username + "]");
				this.isLogged = true;
				return true;
			}

		} catch (ClientProtocolException e) {
			Log.e("Bandex", "Erro ao tentar logar", e);
		} catch (IOException e) {
			Log.e("Bandex", "Erro ao tentar logar", e);
		} catch (JSONException e) {
			Log.e("Bandex", "Erro ao tentar logar", e);
		}

		return false;
	}

	public String getUsername() {
		return this.username;
	}

	public String convertStreamToString(final InputStream inputStream) throws IOException {
		if (inputStream != null) {
			StringBuilder sb = new StringBuilder();
			String line;
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				while ((line = reader.readLine()) != null) {
					sb.append(line).append("\n");
				}
			} finally {
				inputStream.close();
			}
			return sb.toString();
		} else {
			return "";
		}
	}

	// MASTER GAMBZ TO THE RESCUE!
	// isso resolve nosso problema do STOA não ter um certificado decente, mas
	// introduz outros:
	// usando isso nós iremos aceitar qualquer certificado, fazendo o SSL ser
	// meio inutil e deixando o app vulnerável a ataques 'Man-In-The-Middle'
	public HttpClient getNewHttpClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			Log.e("Bandex", "Incapaz de pegar cliente aceita-todo-mundo", e);
			return new DefaultHttpClient();
		}
	}

	public class MySSLSocketFactory extends SSLSocketFactory {
		SSLContext	sslContext	= SSLContext.getInstance("TLS");

		public MySSLSocketFactory(final KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {
				@Override
				public void checkClientTrusted(final X509Certificate[] chain, final String authType)
						throws CertificateException {
				}

				@Override
				public void checkServerTrusted(final X509Certificate[] chain, final String authType)
						throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};

			this.sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(final Socket socket, final String host, final int port, final boolean autoClose)
				throws IOException, UnknownHostException {
			return this.sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return this.sslContext.getSocketFactory().createSocket();
		}
	}
}
