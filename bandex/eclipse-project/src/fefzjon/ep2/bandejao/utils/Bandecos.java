package fefzjon.ep2.bandejao.utils;

import com.google.android.gms.maps.model.LatLng;

public enum Bandecos {
	CENTRAL(1, 
			"Bandejão Central", 
			-23.559729,-46.721509, 
			"Praça do Relógio Solar, travessa 8, nº 300",
			"Café da Manhã : 7h às 8h30;Almoço: 11h15 às 14h15;Jantar: 17h30 às 19h45",
			"Café da Manhã : 7h30 às 9h00;Almoço: 11h15 às 14h15",
			"Café da Manhã : 8h às 9h30;Almoço: 12h00 às 14h15"),
	PCO(4, 
		"Bandejão da Prefeitura",
		-23.559736,-46.739354,
		"Av. Prof. Almeida Prado, 1280",
		"Almoço: 11h15 às 14h15",
		"Sem Funcionamento",
		"Sem Funcionamento"),
	FISICA(2,
			"Bandejão da Física",
			-23.560505,-46.735494,
			"Rua do Matão, Travessa R - Instituto de Física",
			"Almoço: 11h15 às 14h15;Jantar: 17h30 às 19h45",
			"Sem Funcionamento",
			"Sem Funcionamento"),
	QUIMICA(3,
			"Bandejão da Química",
			-23.563697,-46.725634,
			"Av. Lineu Prestes, 748 - Instituto de Químicas",
			"Almoço: 11h às 14h;Jantar: 17h30 às 19h45",
			"Sem Funcionamento",
			"Sem Funcionamento");

	public String nome;
	public int id;
	public LatLng pos;
	public String endereco;
	public String expedienteDiaUtil;
	public String expedienteSabado;
	public String expedienteDomingo;

	Bandecos(final int id, final String nome, final double posLatitude,
			final double posLongitude, final String endereco,
			final String expDiaUtil, final String expSabado,
			final String expDomingo) {
		this.id = id;
		this.nome = nome;
		this.pos = new LatLng(posLatitude, posLongitude);
		this.endereco = endereco;
		this.expedienteDiaUtil = expDiaUtil;
		this.expedienteSabado = expSabado;
		this.expedienteDomingo = expDomingo;
	}

	public static Bandecos getById(int bandecoId) {
		for (Bandecos b : Bandecos.values()) {
			if (b.id == bandecoId)	return b;
		}
		return null;
	}
}
