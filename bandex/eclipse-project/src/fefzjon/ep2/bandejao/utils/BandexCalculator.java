package fefzjon.ep2.bandejao.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class BandexCalculator {

	private static Calendar			calendar				= null;

	private static SimpleDateFormat	formatterDataApresent	= new SimpleDateFormat("dd/MM");

	@SuppressWarnings("deprecation")
	public static int proximaRefeicao() {
		return tipoRefeicao(new Date());
	}

	public static int tipoRefeicao(final Date date) {
		int hours = date.getHours();
		int minutes = date.getMinutes();

		int timeInMinutes = (hours * 60) + minutes;

		int oitoHorasInMinutes = 8 * 60;
		int quinzeHorasInMinutes = 15 * 60;
		if ((0 < timeInMinutes) && (timeInMinutes <= oitoHorasInMinutes)) {
			return BandexConstants.CAFE_DA_MANHA;
		} else if (timeInMinutes <= quinzeHorasInMinutes) {
			return BandexConstants.ALMOCO;
		} else {
			return BandexConstants.JANTA;
		}
	}

	private static Calendar getCalendar() {
		if (calendar == null) {
			calendar = new GregorianCalendar();
			calendar.setFirstDayOfWeek(Calendar.MONDAY);
		}
		return calendar;
	}

	public static int semanaReferente(final Date date) {
		getCalendar().setTime(date);
		int weekOfYear = getCalendar().get(Calendar.WEEK_OF_YEAR);
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

	public static String dataApresentacaoCardapio(final Date dataReferente, final Integer tipoRefeicao) {
		StringBuilder builder = new StringBuilder();
		getCalendar().setTime(dataReferente);
		builder.append(translateDiaDaSemana(getCalendar().get(Calendar.DAY_OF_WEEK)));
		builder.append(" - ");

		builder.append(formatterDataApresent.format(dataReferente));
		builder.append(" ");

		if (tipoRefeicao == BandexConstants.CAFE_DA_MANHA) {
			builder.append("(Café-da-manhã)");
		} else if (tipoRefeicao == BandexConstants.ALMOCO) {
			builder.append("(Almoço)");
		} else if (tipoRefeicao == BandexConstants.JANTA) {
			builder.append("(Janta)");
		}

		return builder.toString();
	}
}
