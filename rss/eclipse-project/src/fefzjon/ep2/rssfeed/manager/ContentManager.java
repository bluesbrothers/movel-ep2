package fefzjon.ep2.rssfeed.manager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import fefzjon.ep2.rssfeed.model.FeedItem;
import fefzjon.ep2.rssfeed.util.Utils;

public class ContentManager {

	public static List<FeedItem> fetchAndParseFeed(final String feedUrl) {
		List<FeedItem> lista = new ArrayList<FeedItem>();

		try {
			URL url = new URL(feedUrl);

			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(false);
			XmlPullParser xpp = factory.newPullParser();

			xpp.setInput(Utils.getInputStream(url), "UTF_8");

			ContentManager.parseXML(lista, xpp);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return lista;
	}

	private static void parseXML(final List<FeedItem> lista, final XmlPullParser xpp) throws XmlPullParserException,
			IOException {
		boolean insideItem = false;

		// Returns the type of current event: START_TAG, END_TAG, etc..
		int eventType = xpp.getEventType();
		FeedItem item = null;
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				if (xpp.getName().equalsIgnoreCase("item")) {
					insideItem = true;
					item = new FeedItem();
					item.setDataBaixado(Utils.formattedToday());
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
						item.setDescription(xpp.nextText());
					}
				} else if (xpp.getName().equalsIgnoreCase("category")) {
					if (insideItem) {
						item.setCategory(xpp.nextText());
					}
				}

			} else if ((eventType == XmlPullParser.END_TAG) && xpp.getName().equalsIgnoreCase("item")) {
				insideItem = false;
			}

			eventType = xpp.next(); // move to next element
		}
	}
}
