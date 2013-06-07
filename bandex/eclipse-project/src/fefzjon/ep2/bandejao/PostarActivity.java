package fefzjon.ep2.bandejao;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import fefzjon.ep2.bandejao.manager.StoaManager;
import fefzjon.ep2.bandejao.utils.ComentarioAsync;
import fefzjon.ep2.bandejao.utils.IntentKeys;

public class PostarActivity extends BasicActivity {

	private Button		btPostar;
	private EditText	edComentario;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_postar);

		this.edComentario = (EditText) this.findViewById(R.id.tx_input_comentario);

		Intent intent = this.getIntent();

		final int mealId = intent.getIntExtra(IntentKeys.DETAILS_MEAL_ID, 1);

		final Editable text = PostarActivity.this.edComentario.getText();

		this.btPostar = (Button) this.findViewById(R.id.bt_submit_comentario);
		this.btPostar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				ComentarioAsync asyncTask = new ComentarioAsync(PostarActivity.this);
				asyncTask.execute(new String[] { String.valueOf(mealId), StoaManager.getInstance().getUsername(),
						text.toString() });
				PostarActivity.this.finish();
			}
		});
	}

}
