package fefzjon.ep2.bandejao.utils;

import android.os.AsyncTask;
import fefzjon.ep2.bandejao.RefreshableActivity;
import fefzjon.ep2.bandejao.manager.ContentManager;
import fefzjon.ep2.exceptions.EpDoisConnectionException;
import fefzjon.ep2.exceptions.EpDoisException;

public class CardapioAsync extends AsyncTask<Integer, Void, CardapioSemana[]> {

	private RefreshableActivity activity;

	public CardapioAsync(final RefreshableActivity detailsActivity) {
		this.activity = detailsActivity;
	}

	@Override
	protected void onPostExecute(final CardapioSemana[] result) {
		super.onPostExecute(result);
		this.activity.setContent(result);
	}

	@Override
	protected CardapioSemana[] doInBackground(final Integer... params) {
		CardapioSemana[] cardapiosSemana = new CardapioSemana[params.length];

		for (int i = 0; i < params.length; i++) {
			if (this.isCancelled()) {
				break;
			}

			CardapioSemana cardapioSemana;
			int bandexId = params[i];
			boolean isOnline = this.activity.isOnline();
			boolean forceRefresh = this.activity.isForceRefresh();

			try {
				cardapioSemana = ContentManager.getIntance().getCardapioSemana(
						bandexId, isOnline, forceRefresh);
			} catch (EpDoisConnectionException e) {
				e.printStackTrace();
				cardapioSemana = new CardapioSemana(bandexId);
			} catch (EpDoisException e) {
				e.printStackTrace();
				cardapioSemana = new CardapioSemana(bandexId);
			}
			cardapiosSemana[i] = cardapioSemana;
		}

		return cardapiosSemana;
	}

}
