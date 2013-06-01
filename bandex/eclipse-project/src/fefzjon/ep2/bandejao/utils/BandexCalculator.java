package fefzjon.ep2.bandejao.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class BandexCalculator {

	private static Calendar	calendar	= null;

	@SuppressWarnings("deprecation")
	public static int nextMeal() {
		Date date = new Date();
		int hours = date.getHours();
		int minutes = date.getMinutes();

		int timeInMinutes = (hours * 60) + minutes;

		int oitoInMinutes = 8 * 60;
		int quatorzeInMinutes = 14 * 60;
		if ((0 < timeInMinutes) && (timeInMinutes <= oitoInMinutes)) {
			return BandexContants.CAFE_DA_MANHA;
		} else if (timeInMinutes <= quatorzeInMinutes) {
			return BandexContants.ALMOCO;
		} else {
			return BandexContants.JANTA;
		}
	}

	public static int semanaReferente(final Date date) {
		if (calendar == null) {
			calendar = new GregorianCalendar();
			calendar.setFirstDayOfWeek(Calendar.MONDAY);
		}
		calendar.setTime(date);
		int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
		return weekOfYear;
	}
}
