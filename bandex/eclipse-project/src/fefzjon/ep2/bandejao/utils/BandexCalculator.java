package fefzjon.ep2.bandejao.utils;

import java.util.Date;

public class BandexCalculator {
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
}
