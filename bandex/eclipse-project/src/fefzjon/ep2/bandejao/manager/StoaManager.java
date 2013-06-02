package fefzjon.ep2.bandejao.manager;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

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

	public void loginSuccess() {
		this.isLogged = true;
	}

	public boolean postLogin(final String nusp, final String password) throws NoSuchAlgorithmException,
			KeyManagementException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(BandexConstants.URL_LOGIN_STOA);

		SSLSocketFactory sf = (SSLSocketFactory) httpclient.getConnectionManager().getSchemeRegistry()
				.getScheme("https").getSocketFactory();
		sf.setHostnameVerifier(new AllowAllHostnameVerifier());

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			//			nameValuePairs.add(new BasicNameValuePair("usp_id", nusp));
			//			nameValuePairs.add(new BasicNameValuePair("password", password));
			nameValuePairs.add(new BasicNameValuePair("usp_id", "6879613"));
			nameValuePairs.add(new BasicNameValuePair("password", "macaco"));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			// se sucesso, salvar o username da crianca e setar como logado
			//			this.loginSuccess();

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}

		return true;
	}

	public String getUsername() {
		return this.username;
	}

}
