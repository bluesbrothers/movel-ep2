package fefzjon.ep2.rssfeed.manager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;
import fefzjon.ep2.exceptions.EpDoisException;
import fefzjon.ep2.persist.DBManager;
import fefzjon.ep2.rssfeed.model.FeedItem;
import fefzjon.ep2.rssfeed.utils.RSSFeedUtils;
import fefzjon.ep2.utils.Utils;

public class ContentManager {

	public static List<FeedItem> getLastList() {
		List<FeedItem> list = new ArrayList<FeedItem>();
		try {
			list = DBManager.getInstance().getAll(new FeedItem());
		} catch (EpDoisException e) {
			Log.e("RSSFeed", "Problemas ao recuperar ultima lista");
			e.printStackTrace();
		}
		return list;
	}

	public static List<FeedItem> fetchAndParseFeed(final List<String> feedUrls) {
		List<FeedItem> lista = new ArrayList<FeedItem>();

		for (String feedUrl : feedUrls) {
			try {
				URL url = new URL(feedUrl);

				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				factory.setNamespaceAware(false);
				XmlPullParser xpp = factory.newPullParser();

				xpp.setInput(Utils.getInputStream(url), "UTF_8");

				ContentManager.parseXML(lista, xpp);

				try {
					ContentManager.saveNewContent(lista);
				} catch (EpDoisException e) {
					Log.e("RSSFeed", "Problemas ao salvar novo conteudo");
					e.printStackTrace();
				}

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				Log.e("RSSFeed", "Erro ao tentar baixar " + feedUrl
						+ " - dispositivo está conectado?");
			}
		}

		return lista;
	}

	private static void saveNewContent(final List<FeedItem> lista)
			throws EpDoisException {
		Log.i("RSSFeed", "Deletando conteudo antigo");
		DBManager.getInstance().deleteAll(new FeedItem());
		Log.i("RSSFeed", "Salvando conteudo novo");
		for (FeedItem item : lista) {
			DBManager.getInstance().create(item);
		}
		Log.d("RSSFeed", "Novo conteudo salvo");
	}

	private static void parseXML(final List<FeedItem> lista,
			final XmlPullParser xpp) throws XmlPullParserException, IOException {
		Log.i("RSSFeed", "Comecando parse do input XML");

		boolean insideItem = false;

		// Returns the type of current event: START_TAG, END_TAG, etc..
		int eventType = xpp.getEventType();
		FeedItem item = null;
		Date today = new Date();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				if (xpp.getName().equalsIgnoreCase("item")) {
					insideItem = true;
					item = new FeedItem();
					item.setDataBaixado(today);
					lista.add(item);
				} else if (xpp.getName().equalsIgnoreCase("title")) {
					if (insideItem) {
						item.setTitle(xpp.nextText());
					}
				} else if (xpp.getName().equalsIgnoreCase("link")) {
					if (insideItem) {
						item.setLink(xpp.nextText());
					}
				} else if (xpp.getName().equalsIgnoreCase("description")) {
					if (insideItem) {
						String description = xpp.nextText();
						item.setDescription(description);

						Date dataPalestra = RSSFeedUtils
								.getDateFromDescription(description);
						item.setDataPalestra(dataPalestra);
					}
				} else if (xpp.getName().equalsIgnoreCase("category")) {
					if (insideItem) {
						item.setCategory(xpp.nextText());
					}
				}

			} else if ((eventType == XmlPullParser.END_TAG)
					&& xpp.getName().equalsIgnoreCase("item")) {
				insideItem = false;
			}

			eventType = xpp.next(); // move to next element
		}
		Log.d("RSSFeed", "Fim do parse XML");
	}

}
