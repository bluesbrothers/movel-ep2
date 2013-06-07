package fefzjon.ep2.rssfeed.utils;

import java.util.Calendar;
import java.util.Date;

public class RSSFeedUtils {
	public static Date getDateFromDescription(final String description) {
		String[] lines = description.split("<br\\s*/>");
		Calendar c = Calendar.getInstance();
		for (String s : lines) {
			if (s.startsWith("Data: ")) {
				s = s.replace("Data: ", "");
				String[] values = s.split("\\s*-\\s*")[0].split("\\.");
				c.set(Calendar.DAY_OF_MONTH, Integer.valueOf(values[0]));
				c.set(Calendar.MONTH, Integer.valueOf(values[1]) - 1);
				c.set(Calendar.YEAR, Integer.valueOf(values[2]));
			} else if (s.startsWith("Hora: ")) {
				s = s.replace("Hora: ", "");
				s = s.replace(" h", "");
				String[] values = s.split("\\.");
				c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(values[0]));
				c.set(Calendar.MINUTE, Integer.valueOf(values[1]));
				c.set(Calendar.SECOND, 0);
			}
		}
		// Log.d("RSSFeed", c.getTime().toString());
		return c.getTime();
	}
}
