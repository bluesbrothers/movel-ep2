package fefzjon.ep2.bandejao;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import fefzjon.ep2.bandejao.adapter.ComentariosAdapter;
import fefzjon.ep2.bandejao.manager.ComentariosManager;
import fefzjon.ep2.bandejao.manager.StoaManager;
import fefzjon.ep2.bandejao.utils.BandexComment;
import fefzjon.ep2.bandejao.utils.IntentKeys;

public class ComentariosActivity extends ListActivity {

	private int bandexId;
	private int mealId;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_comentarios);

		if (!StoaManager.getInstance().isLogged()) {
			this.tryLogin();
			this.finish();
		}

		Intent intent = this.getIntent();
		this.bandexId = intent.getIntExtra(IntentKeys.DETAILS_BANDECO_ID, 1);
		this.mealId = intent.getIntExtra(IntentKeys.DETAILS_MEAL_ID, 1);

		this.doRefresh();

		Button btPostar = (Button) this.findViewById(R.id.bt_postar_comentario);
		btPostar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				Intent intent = new Intent(ComentariosActivity.this,
						PostarActivity.class);
				intent.putExtra(IntentKeys.DETAILS_MEAL_ID,
						ComentariosActivity.this.mealId);
				ComentariosActivity.this.startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.comentarios, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case R.id.refresh_comments:
			this.doRefresh();
			return true;
		case R.id.action_deslogar:
			StoaManager.getInstance().deslogar();
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void doRefresh() {
		List<BandexComment> commentList = ComentariosManager
				.fetchAndParseComment(this.mealId);

		ListAdapter adapter = new ComentariosAdapter(this, commentList);

		this.setListAdapter(adapter);
	}

	private void tryLogin() {
		Intent intent = new Intent(this, LoginActivity.class);
		this.startActivity(intent);
	}

}
