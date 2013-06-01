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
import fefzjon.ep2.bandejao.utils.BandexContants;
import fefzjon.ep2.bandejao.utils.CardapioSemana;
import fefzjon.ep2.exceptions.EpDoisException;
import fefzjon.ep2.utils.Utils;

public class ContentManager {
	private static ContentManager instance = null;

	private Map<Integer, CardapioSemana> infosBandecos;

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
		if (cardapio == null) {
			cardapio = fetchAndParseMeal(bandecoId,
					String.format(BandexContants.URL_PATTERN, bandecoId));
			this.infosBandecos.put(bandecoId, cardapio);
		}
		return cardapio;
	}

	private static CardapioSemana fetchAndParseMeal(final int bandexId,
			final String urlBandex) {
		CardapioSemana cardapio = new CardapioSemana(bandexId);

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
		} catch (EpDoisException e) {
			e.printStackTrace();
		}

		return cardapio;
	}

	private static void saveNewContent(final CardapioSemana cardapio)
			throws EpDoisException {
		Log.i("Bandex", "Deletando conteudo antigo");
		Log.d("Bandex", "Novo conteudo salvo");
	}

	private static void parseXML(final CardapioSemana cardapioSemana,
			final XmlPullParser xpp) throws XmlPullParserException,
			IOException, EpDoisException {
		Log.i("Bandex", "Comecando parse do input XML");

		boolean insideItem = false;

		// Returns the type of current event: START_TAG, END_TAG, etc..
		int eventType = xpp.getEventType();
		CardapioDia cDia = null;
		Date today = new Date();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				if (xpp.getName().equalsIgnoreCase("menu")) {
					insideItem = true;
					cDia = new CardapioDia();
					cDia.setDataBaixado(today);
					cDia.setBandexId(cardapioSemana.getBandexId());

					Calendar calendar = new GregorianCalendar();
					calendar.setTime(today);
					int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
					cDia.setSemanaReferente(weekOfYear);

				} else if (xpp.getName().equalsIgnoreCase("kcal")) {
					if (insideItem) {
						String text = xpp.nextText();
						cDia.setKcal(text == null ? null : Integer
								.parseInt(text));
					}
				} else if (xpp.getName().equalsIgnoreCase("meal-id")) {
					if (insideItem) {
						String text = xpp.nextText();
						cDia.setTipoRefeicao(text == null ? null : Integer
								.parseInt(text));
					}
				} else if (xpp.getName().equalsIgnoreCase("options")) {
					if (insideItem) {
						cDia.setCardapio(xpp.nextText());
					}
				} else if (xpp.getName().equalsIgnoreCase("day")) {
					if (insideItem) {
						String text = xpp.nextText();
						Date date = Utils.parseAnoMesDia(text);
						cDia.setDataReferente(date);
					}
				} else if (xpp.getName().equalsIgnoreCase("restaurant-id")) {
					if (insideItem) {
						String text = xpp.nextText();
						Integer id = text == null ? null : Integer
								.parseInt(text);
						if (id != cardapioSemana.getBandexId()) {
							throw new EpDoisException(
									"Informacao referente a outro bandejao foi recebida no XML");
						}
					}
				}

			} else if ((eventType == XmlPullParser.END_TAG)
					&& xpp.getName().equalsIgnoreCase("menu")) {
				insideItem = false;
				if ((cDia.getDataReferente() != null)
						&& (cDia.getTipoRefeicao() != null)) {
					cardapioSemana.put(cDia.getDataReferente(),
							cDia.getTipoRefeicao(), cDia);
				}
			}

			eventType = xpp.next(); // move to next element
		}
		Log.d("Bandex", "Fim do parse XML");
	}
}
