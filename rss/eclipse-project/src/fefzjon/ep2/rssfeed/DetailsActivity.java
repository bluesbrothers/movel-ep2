package fefzjon.ep2.rssfeed;

import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import fefzjon.ep2.rssfeed.utils.IntentKeys;
import fefzjon.ep2.rssfeed.utils.RSSFeedUtils;

public class DetailsActivity extends Activity {

	private Button registrarButton;
	private Button linkButton;
	private TextView titulo;
	private WebView description;

	private Intent incomingIntent;

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
		final String title = this.incomingIntent
				.getStringExtra(IntentKeys.DETAILS_FEED_TITLE);
		final String description = this.incomingIntent
				.getStringExtra(IntentKeys.DETAILS_FEED_DESCRIPTION);

		this.titulo.setText(title);
		this.description
				.loadData(description, "text/html; charset=UTF-8", null);
	}

	private void setupButtons() {
		this.setupRegistrarButton();
		this.setupLinkButton();
	}

	@SuppressLint("InlinedApi")
	private final void handleRegistrarButtonClick() {
		final String title = this.incomingIntent
				.getStringExtra(IntentKeys.DETAILS_FEED_TITLE);
		final String feedDescription = this.incomingIntent
				.getStringExtra(IntentKeys.DETAILS_FEED_DESCRIPTION);
		Date date = RSSFeedUtils.getDateFromDescription(feedDescription);
		final String category = this.incomingIntent
				.getStringExtra(IntentKeys.DETAILS_FEED_CATEGORY);
		String description = "Categoria: " + category;

		Intent intent = new Intent();
		intent.setType("vnd.android.cursor.item/event");
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			intent.setAction(Intent.ACTION_INSERT);
			intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
					date.getTime());
			intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
					date.getTime() + (1000 * 60 * 60 * 2));
			intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
			// just included for completeness
			intent.putExtra(Events.TITLE, title);
			intent.putExtra(Events.DESCRIPTION, description);
			intent.putExtra(Events.EVENT_LOCATION, "IME-USP");
			intent.putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);
			intent.putExtra(Events.ACCESS_LEVEL, Events.ACCESS_PRIVATE);
			intent.putExtra(Events.ALLOWED_REMINDERS, "METHOD_DEFAULT");
			intent.putExtra(Intent.EXTRA_EMAIL, "");
		} else {
			intent.setAction(Intent.ACTION_EDIT);
			intent.putExtra("beginTime", date.getTime());
			intent.putExtra("allDay", false);
			intent.putExtra("endTime", date.getTime() + (2 * 60 * 60 * 1000));
			intent.putExtra("title", title);
			intent.putExtra("description", description);
			intent.putExtra("eventLocation", "IME-USP");
		}
		this.startActivity(intent);
	}

	private final void handleLinkButtonClick() {
		final String link = this.incomingIntent
				.getStringExtra(IntentKeys.DETAILS_FEED_LINK);
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
