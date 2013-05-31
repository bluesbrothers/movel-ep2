package fefzjon.ep2.bandejao.enums;

public enum Bandecos {
	CENTRAL(1, "Central"), PCO(2, "PCO"), FISICA(3, "Física"), QUIMICA(4, "Química");

	public String	nome;
	public int		id;

	Bandecos(final int id, final String nm) {
		this.id = id;
		this.nome = nm;
	}

}
