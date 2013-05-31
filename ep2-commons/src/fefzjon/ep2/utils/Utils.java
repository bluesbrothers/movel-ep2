package fefzjon.ep2.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	private static SimpleDateFormat	formatter	= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	public static String formatDate(final Date date) {
		return Utils.formatter.format(date);
	}

	public static Date getDateFrom(final String formattedDate) {
		try {
			return Utils.formatter.parse(formattedDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String formattedToday() {
		return Utils.formatDate(new Date());
	}

	public static InputStream getInputStream(final URL url) {
		try {
			return url.openConnection().getInputStream();
		} catch (IOException e) {
			return null;
		}
	}

}
