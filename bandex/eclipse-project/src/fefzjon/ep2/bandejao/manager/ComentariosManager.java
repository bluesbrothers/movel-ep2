package fefzjon.ep2.bandejao.manager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;
import fefzjon.ep2.bandejao.utils.BandexComment;
import fefzjon.ep2.bandejao.utils.BandexConstants;
import fefzjon.ep2.exceptions.EpDoisException;
import fefzjon.ep2.utils.Utils;

public class ComentariosManager {
	private ComentariosManager() {
	}

	public static List<BandexComment> fetchAndParseComment(final int mealId) {
		List<BandexComment> list = new ArrayList<BandexComment>();

		String urlToFetch = String.format(BandexConstants.URL_COMMENT_PATTERN, mealId);

		try {
			URL url = new URL(urlToFetch);

			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(false);
			XmlPullParser xpp = factory.newPullParser();

			xpp.setInput(Utils.getInputStream(url), "UTF_8");

			ComentariosManager.parseXML(list, xpp);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (EpDoisException e) {
			e.printStackTrace();
		}

		return list;
	}

	private static void parseXML(final List<BandexComment> listaComentarios, final XmlPullParser xpp)
			throws XmlPullParserException, IOException, EpDoisException {
		Log.i("Comentários Bandex", "Comecando parse do input XML");

		boolean insideItem = false;

		// Returns the type of current event: START_TAG, END_TAG, etc..
		int eventType = xpp.getEventType();
		BandexComment comment = null;

		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				if (xpp.getName().equalsIgnoreCase("comment")) {
					insideItem = true;
					comment = new BandexComment();
				} else if (xpp.getName().equalsIgnoreCase("commenter")) {
					if (insideItem) {
						comment.setCommenter(xpp.nextText());
					}
				} else if (xpp.getName().equalsIgnoreCase("message")) {
					if (insideItem) {
						comment.setMessage(xpp.nextText());
					}
				}

			} else if ((eventType == XmlPullParser.END_TAG) && xpp.getName().equalsIgnoreCase("comment")) {
				insideItem = false;
				listaComentarios.add(comment);
			}

			eventType = xpp.next(); // move to next element
		}
		Log.d("Bandex", "Fim do parse XML");
	}

	public static boolean postComentario(final int mealId, final String commenter, final String mensagem)
			throws NoSuchAlgorithmException, KeyManagementException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(BandexConstants.URL_COMMENT_POST);

		try {
			JSONObject holder = new JSONObject();
			JSONObject toSend = new JSONObject();
			holder.put("menu_id", mealId);
			holder.put("commenter", commenter);
			holder.put("message", mensagem);

			toSend.put("menuscomment", holder);

			//passes the results to a string builder/entity
			StringEntity se = new StringEntity(toSend.toString());

			//sets the post request as the resulting string
			httppost.setEntity(se);
			httppost.setHeader("Content-type", "application/json");

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			return true;

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}
}
