package fefzjon.ep2.bandejao.enums;

public enum Bandecos {
	CENTRAL(1, "Central"), PCO(4, "PCO"), FISICA(2, "Física"), QUIMICA(3, "Química");

	public String	nome;
	public int		id;

	Bandecos(final int id, final String nome) {
		this.id = id;
		this.nome = nome;
	}

}
