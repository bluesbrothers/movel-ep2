package fefzjon.ep2.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Utils {

	private static SimpleDateFormat	formatter	= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private static SimpleDateFormat	semHoras	= new SimpleDateFormat("dd/MM/yyyy");
	private static SimpleDateFormat	anoMesDia	= new SimpleDateFormat("yyyy-MM-dd");

	public static String formatDate(final Date date) {
		return Utils.formatter.format(date);
	}
	
	public static String formatDateWithourTime(final Date date) {
		return Utils.semHoras.format(date);
	}

	public static Date getDateFrom(final String formattedDate) {
		try {
			return Utils.formatter.parse(formattedDate);
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
		Date date = new Date();
		String fmt = anoMesDia.format(date);
		return parseAnoMesDia(fmt);
	}
	
	public static InputStream getInputStream(final URL url) {
		try {
			return url.openConnection().getInputStream();
		} catch (IOException e) {
			return null;
		}
	}

}
