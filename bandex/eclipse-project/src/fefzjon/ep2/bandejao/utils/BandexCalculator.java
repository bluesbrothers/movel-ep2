package fefzjon.ep2.bandejao.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import fefzjon.ep2.bandejao.model.CardapioDia;

public class BandexCalculator {

	private static Calendar			calendar				= null;

	private static SimpleDateFormat	formatterDataApresent	= new SimpleDateFormat("dd/MM");

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

	private static String translateDiaDaSemana(final int dia) {
		if (dia == Calendar.MONDAY) {
			return "Segunda";
		} else if (dia == Calendar.TUESDAY) {
			return "Terça";
		} else if (dia == Calendar.WEDNESDAY) {
			return "Quarta";
		} else if (dia == Calendar.THURSDAY) {
			return "Quinta";
		} else if (dia == Calendar.FRIDAY) {
			return "Sexta";
		} else if (dia == Calendar.SATURDAY) {
			return "Sábado";
		} else {
			return "Domingo";
		}
	}

	public static String dataApresentacaoCardapio(final CardapioDia cDia) {
		StringBuilder builder = new StringBuilder();

		calendar.setTime(cDia.getDataReferente());
		builder.append(translateDiaDaSemana(calendar.get(Calendar.DAY_OF_WEEK)));
		builder.append(" - ");

		builder.append(formatterDataApresent.format(cDia.getDataReferente()));
		builder.append(" ");

		if (cDia.getTipoRefeicao() == BandexContants.CAFE_DA_MANHA) {
			builder.append("(Café-da-manhã)");
		} else if (cDia.getTipoRefeicao() == BandexContants.ALMOCO) {
			builder.append("(Almoço)");
		} else if (cDia.getTipoRefeicao() == BandexContants.JANTA) {
			builder.append("(Janta)");
		}

		return builder.toString();

	}
}
