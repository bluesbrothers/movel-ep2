package fefzjon.ep2.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	private static SimpleDateFormat	complete	= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private static SimpleDateFormat	semHoras	= new SimpleDateFormat("dd/MM/yyyy");
	private static SimpleDateFormat	anoMesDia	= new SimpleDateFormat("yyyy-MM-dd");

	public static String formatDateComplete(final Date date) {
		return Utils.complete.format(date);
	}

	public static String formatDateSemHoras(final Date date) {
		return Utils.semHoras.format(date);
	}

	public static String formatDateAnoMesDia(final Date date) {
		return Utils.anoMesDia.format(date);
	}

	public static Date parseComplete(final String formattedDate) {
		try {
			return Utils.complete.parse(formattedDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date parseSemHoras(final String formattedDate) {
		try {
			return Utils.semHoras.parse(formattedDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date parseAnoMesDia(final String formattedDate) {
		try {
			return Utils.anoMesDia.parse(formattedDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date today() {
		return Utils.resetClockDay(new Date());
	}

	public static Date resetClockDay(final Date date) {
		return Utils.parseAnoMesDia(Utils.formatDateAnoMesDia(date));
	}

	public static InputStream getInputStream(final URL url) {
		try {
			return url.openConnection().getInputStream();
		} catch (IOException e) {
			return null;
		}
	}

}
