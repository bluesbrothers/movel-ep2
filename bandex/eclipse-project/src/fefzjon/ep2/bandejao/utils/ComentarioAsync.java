package fefzjon.ep2.bandejao.utils;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import fefzjon.ep2.bandejao.manager.ComentariosManager;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class ComentarioAsync extends AsyncTask<String, Void, Boolean> {

	private Context	context;

	public ComentarioAsync(final Context context) {
		this.context = context;
	}

	@Override
	protected Boolean doInBackground(final String... params) {
		int mealId = Integer.parseInt(params[0]);
		try {
			return ComentariosManager.postComentario(mealId, params[1], params[2]);
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected void onPostExecute(final Boolean success) {
		if ((success != null) && success) {
			Toast.makeText(this.context, "Comentário postado com sucesso!", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this.context, "Falha na postagem do comentário", Toast.LENGTH_SHORT).show();
		}
	}

}
