package fefzjon.ep2.rssfeed;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import fefzjon.ep2.rssfeed.util.IntentKeys;

public class DetailsActivity extends Activity {

	private Button		registrarButton;
	private Button		linkButton;
	private TextView	titulo;
	private WebView		description;

	private Intent		incomingIntent;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_details);

		this.titulo = (TextView) this.findViewById(R.id.text_titulo);
		this.description = (WebView) this.findViewById(R.id.text_description);
		this.registrarButton = (Button) this.findViewById(R.id.bt_registrar);
		this.linkButton = (Button) this.findViewById(R.id.bt_seguir_link);

		this.incomingIntent = this.getIntent();

		this.setupTextViews();
		this.setupButtons();
	}

	private void setupTextViews() {
		final String title = this.incomingIntent.getStringExtra(IntentKeys.DETAILS_FEED_TITLE);
		final String description = this.incomingIntent.getStringExtra(IntentKeys.DETAILS_FEED_DESCRIPTION);

		this.titulo.setText(title);
		this.description.loadData(description, "text/html; charset=UTF-8", null);
	}

	private void setupButtons() {
		this.setupRegistrarButton();
		this.setupLinkButton();
	}

	private final void handleRegistrarButtonClick() {
		Toast.makeText(this, "Registrou!", Toast.LENGTH_SHORT).show();
	}

	private final void handleLinkButtonClick() {
		final String link = this.incomingIntent.getStringExtra(IntentKeys.DETAILS_FEED_LINK);
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
		this.startActivity(intent);
	}

	private void setupRegistrarButton() {
		this.registrarButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				DetailsActivity.this.handleRegistrarButtonClick();
			}
		});
	}

	private void setupLinkButton() {
		this.linkButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				DetailsActivity.this.handleLinkButtonClick();
			}
		});
	}
}
