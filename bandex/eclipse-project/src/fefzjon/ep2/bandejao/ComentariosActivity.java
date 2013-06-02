package fefzjon.ep2.bandejao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import fefzjon.ep2.bandejao.manager.StoaManager;

public class ComentariosActivity extends Activity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_comentarios);

		if (!StoaManager.getInstance().isLogged()) {
			this.tryLogin();
			this.finish();
		}
	}

	private void tryLogin() {
		Intent intent = new Intent(this, LoginActivity.class);
		this.startActivity(intent);
	}

}
