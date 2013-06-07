package fefzjon.ep2.bandejao;

import fefzjon.ep2.bandejao.utils.CardapioSemana;

public interface RefreshableActivity {
	void setContent(CardapioSemana... cardapiosSemana);

	boolean isForceRefresh();

	boolean isOnline();
}
