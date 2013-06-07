package fefzjon.ep2.bandejao.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

import junit.framework.Assert;

import org.junit.Test;

public class BandexCalculatorTest {

	@Test
	public void tipoRefeicaoTest() {
		Calendar calendar = new GregorianCalendar();
		calendar.set(2013, Calendar.JUNE, 7, 5, 0);
		Assert.assertEquals(BandexConstants.CAFE_DA_MANHA,
				BandexCalculator.tipoRefeicao(calendar.getTime()));

		calendar.set(2013, Calendar.JUNE, 7, 7, 59);
		Assert.assertEquals(BandexConstants.CAFE_DA_MANHA,
				BandexCalculator.tipoRefeicao(calendar.getTime()));

		calendar.set(2013, Calendar.JUNE, 7, 8, 00);
		Assert.assertEquals(BandexConstants.CAFE_DA_MANHA,
				BandexCalculator.tipoRefeicao(calendar.getTime()));

		calendar.set(2013, Calendar.JUNE, 7, 8, 01);
		Assert.assertEquals(BandexConstants.ALMOCO,
				BandexCalculator.tipoRefeicao(calendar.getTime()));

		calendar.set(2013, Calendar.JUNE, 7, 14, 59);
		Assert.assertEquals(BandexConstants.ALMOCO,
				BandexCalculator.tipoRefeicao(calendar.getTime()));

		calendar.set(2013, Calendar.JUNE, 7, 15, 00);
		Assert.assertEquals(BandexConstants.ALMOCO,
				BandexCalculator.tipoRefeicao(calendar.getTime()));

		calendar.set(2013, Calendar.JUNE, 7, 15, 01);
		Assert.assertEquals(BandexConstants.JANTA,
				BandexCalculator.tipoRefeicao(calendar.getTime()));

		calendar.set(2013, Calendar.JUNE, 7, 23, 59);
		Assert.assertEquals(BandexConstants.JANTA,
				BandexCalculator.tipoRefeicao(calendar.getTime()));

		calendar.set(2013, Calendar.JUNE, 7, 00, 00);
		Assert.assertEquals(BandexConstants.JANTA,
				BandexCalculator.tipoRefeicao(calendar.getTime()));

		calendar.set(2013, Calendar.JUNE, 7, 00, 01);
		Assert.assertEquals(BandexConstants.CAFE_DA_MANHA,
				BandexCalculator.tipoRefeicao(calendar.getTime()));
	}

	/* segunda feira é o primeiro dia da semana */
	@Test
	public void semanaReferenteTest() {
		Calendar calendar = new GregorianCalendar();
		calendar.set(2013, Calendar.JANUARY, 1);
		Assert.assertEquals(1,
				BandexCalculator.semanaReferente(calendar.getTime()));

		calendar.set(2013, Calendar.JANUARY, 6);
		Assert.assertEquals(1,
				BandexCalculator.semanaReferente(calendar.getTime()));

		/* primeira segunda feira do ano */
		calendar.set(2013, Calendar.JANUARY, 7);
		Assert.assertEquals(2,
				BandexCalculator.semanaReferente(calendar.getTime()));

		calendar.set(2013, Calendar.JANUARY, 13);
		Assert.assertEquals(2,
				BandexCalculator.semanaReferente(calendar.getTime()));

		calendar.set(2013, Calendar.JANUARY, 14);
		Assert.assertEquals(3,
				BandexCalculator.semanaReferente(calendar.getTime()));

		calendar.set(2013, Calendar.JANUARY, 14);
		Assert.assertEquals(3,
				BandexCalculator.semanaReferente(calendar.getTime()));

		calendar.set(2013, Calendar.DECEMBER, 29);
		Assert.assertEquals(52,
				BandexCalculator.semanaReferente(calendar.getTime()));

		/*
		 * a última semana incompleta do ano é colocada junto com a primeira
		 * semana do ano seguinte
		 */
		calendar.set(2013, Calendar.DECEMBER, 30);
		Assert.assertEquals(1,
				BandexCalculator.semanaReferente(calendar.getTime()));

		/* ano bissexto */
		calendar.set(2016, Calendar.JANUARY, 3);
		Assert.assertEquals(1,
				BandexCalculator.semanaReferente(calendar.getTime()));

		calendar.set(2016, Calendar.JANUARY, 4);
		Assert.assertEquals(2,
				BandexCalculator.semanaReferente(calendar.getTime()));

		calendar.set(2016, Calendar.FEBRUARY, 28);
		Assert.assertEquals(9,
				BandexCalculator.semanaReferente(calendar.getTime()));

		calendar.set(2016, Calendar.FEBRUARY, 29);
		Assert.assertEquals(10,
				BandexCalculator.semanaReferente(calendar.getTime()));

		calendar.set(2016, Calendar.MARCH, 1);
		Assert.assertEquals(10,
				BandexCalculator.semanaReferente(calendar.getTime()));
	}
}
