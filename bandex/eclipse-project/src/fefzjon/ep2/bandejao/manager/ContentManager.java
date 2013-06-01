package fefzjon.ep2.bandejao.manager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;
import fefzjon.ep2.bandejao.model.CardapioDia;
import fefzjon.ep2.bandejao.utils.CardapioSemana;
import fefzjon.ep2.exceptions.EpDoisException;
import fefzjon.ep2.utils.Utils;

public class ContentManager {
	public static ContentManager			instance	= null;

	private Map<Integer, CardapioSemana>	infosBandecos;

	private ContentManager() {
		this.infosBandecos = new HashMap<Integer, CardapioSemana>();
	}

	public static ContentManager getIntance() {
		if (instance == null) {
			instance = new ContentManager();
		}
		return instance;
	}

	public CardapioSemana getCardapioSemana(final int bandecoId) {
		CardapioSemana cardapio = this.infosBandecos.get(bandecoId);
		return cardapio;
	}

	public static CardapioSemana fetchAndParseFeed(final String urlBandex) {
		CardapioSemana cardapio = new CardapioSemana();

		try {
			URL url = new URL(urlBandex);

			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(false);
			XmlPullParser xpp = factory.newPullParser();

			xpp.setInput(Utils.getInputStream(url), "UTF_8");

			ContentManager.parseXML(cardapio, xpp);

			try {
				ContentManager.saveNewContent(cardapio);
			} catch (EpDoisException e) {
				Log.e("Bandex", "Problemas ao salvar novo conteudo");
				e.printStackTrace();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return cardapio;
	}

	private static void saveNewContent(final CardapioSemana cardapio) throws EpDoisException {
		Log.i("Bandex", "Deletando conteudo antigo");
		Log.d("Bandex", "Novo conteudo salvo");
	}

	private static void parseXML(final CardapioSemana cardapioSemana, final XmlPullParser xpp)
			throws XmlPullParserException, IOException {
		Log.i("Bandex", "Comecando parse do input XML");

		boolean insideItem = false;

		// Returns the type of current event: START_TAG, END_TAG, etc..
		int eventType = xpp.getEventType();
		CardapioDia cDia = null;
		Date today = new Date();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				if (xpp.getName().equalsIgnoreCase("item")) {
					insideItem = true;
					cDia = new CardapioDia();
					cDia.setDataBaixado(today);
					cardapioSemana.add(cDia);
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(today);
					int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
				} else if (xpp.getName().equalsIgnoreCase("title")) {
					if (insideItem) {
						cDia.setTitle(xpp.nextText());
					}
				} else if (xpp.getName().equalsIgnoreCase("link")) {
					if (insideItem) {
						cDia.setLink(xpp.nextText());
					}
				} else if (xpp.getName().equalsIgnoreCase("description")) {
					if (insideItem) {
						cDia.setDescription(xpp.nextText());
					}
				} else if (xpp.getName().equalsIgnoreCase("category")) {
					if (insideItem) {
						cDia.setCategory(xpp.nextText());
					}
				}

			} else if ((eventType == XmlPullParser.END_TAG) && xpp.getName().equalsIgnoreCase("item")) {
				insideItem = false;
			}

			eventType = xpp.next(); // move to next element
		}
		Log.d("Bandex", "Fim do parse XML");
	}
}
