package fefzjon.ep2.gps;

import java.util.Date;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import fefzjon.ep2.gps.dialog.SelectBUSPDialogFragment;
import fefzjon.ep2.gps.dialog.SelectDayTypeDialogFragment;
import fefzjon.ep2.gps.utilities.Constants;
import fefzjon.ep2.gps.utilities.TimeTableAdapter;
import fefzjon.ep2.gps.utilities.Timer;
import fefzjon.ep2.gps.utilities.Timer.TimerCallback;
import fefzjon.ep2.gps.utilities.TimetableManager;
import fefzjon.ep2.gps.utilities.TimetableManager.DayType;

public class TimeTableActivity extends FragmentActivity implements
		SelectDayTypeDialogFragment.DayTypeDialogListener,
		SelectBUSPDialogFragment.BUSPDialogListener, TimerCallback {

	Timer timer;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.timetable_layout);

		Intent intent = this.getIntent();
		int buspCode = intent.getIntExtra(Constants.BUSP_CODE, 8012);

		this.updateContents(buspCode, TimetableManager.getDayType());

		this.timer = new Timer(this, 1000 * 60);
		this.timer.start();
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.time_table, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case R.id.tt_menu_selectDayType:
			DialogFragment dayTypeDialog = new SelectDayTypeDialogFragment();
			dayTypeDialog.show(this.getSupportFragmentManager(),
					"SelectDayTypeDialog");
			return true;
		case R.id.tt_menu_selectBUSP:
			DialogFragment buspDialog = new SelectBUSPDialogFragment();
			buspDialog.show(this.getSupportFragmentManager(),
					"SelectBUSPDialog");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void updateContents(final int buspCode, final DayType dayType) {
		TextView tvName = (TextView) this.findViewById(R.id.busp_name);
		Resources res = this.getResources();
		String busp = "N/A";
		String duration = "N/A";
		if (buspCode == Constants.BUSP_1) {
			busp = res.getString(R.string.busp8012);
			duration = String.valueOf(TimetableManager.getApproxRouteDuration(
					buspCode, dayType)) + "min";
		} else if (buspCode == Constants.BUSP_2) {
			busp = res.getString(R.string.busp8022);
			duration = String.valueOf(TimetableManager.getApproxRouteDuration(
					buspCode, dayType)) + "min";
		} else if (buspCode == Constants.BUSP_AMBOS) {
			busp = res.getString(R.string.buspAmbos);
			duration = String.valueOf(Constants.BUSP_1) + ":";
			duration += String.valueOf(TimetableManager.getApproxRouteDuration(
					Constants.BUSP_1, dayType)) + "min";

			duration += " - " + String.valueOf(Constants.BUSP_2) + ":";
			duration += String.valueOf(TimetableManager.getApproxRouteDuration(
					Constants.BUSP_2, dayType)) + "min";
		}
		String dayTypeStr = "ERRO";
		if (dayType == DayType.UTIL) {
			dayTypeStr = "Dia Útil";
		} else if (dayType == DayType.SATURDAY) {
			dayTypeStr = "Sábado";
		} else if (dayType == DayType.SUNDAY) {
			dayTypeStr = "Domingo";
		}
		tvName.setText(busp + " - " + dayTypeStr);

		TextView tvDuration = (TextView) this.findViewById(R.id.duration);
		tvDuration.setText(duration);

		ListView lvTable = (ListView) this.findViewById(R.id.timetable);
		lvTable.setAdapter(new TimeTableAdapter(buspCode, dayType));
		this.updateNextDepartureText();
	}

	@Override
	public void onBUSPSelected(final int buspCode) {
		ListView lvTable = (ListView) this.findViewById(R.id.timetable);
		TimeTableAdapter adapter = (TimeTableAdapter) lvTable.getAdapter();

		this.updateContents(buspCode, adapter.getDayType());
	}

	@Override
	public void onDayTypeSelected(final DayType dayType) {
		ListView lvTable = (ListView) this.findViewById(R.id.timetable);
		TimeTableAdapter adapter = (TimeTableAdapter) lvTable.getAdapter();

		this.updateContents(adapter.getBuspCode(), dayType);
	}

	@Override
	public void onTick() {
		this.updateNextDepartureText();
	}

	private void updateNextDepartureText() {
		ListView lvTable = (ListView) this.findViewById(R.id.timetable);
		TimeTableAdapter adapter = (TimeTableAdapter) lvTable.getAdapter();
		int current = TimetableManager.getTimeCode(TimetableManager
				.getDateStr(new Date()));
		String time = "N/A";
		for (int i = 0; i < adapter.getCount(); i++) {
			time = String.valueOf(adapter.getItem(i));
			String[] timeParts = time.split(" ");
			time = timeParts[timeParts.length - 1];
			int code = TimetableManager.getTimeCode(time);
			if (current < code) {
				time = String.valueOf(adapter.getItem(i));
				break;
			}
		}

		TextView tvNext = (TextView) this.findViewById(R.id.nextTime);
		tvNext.setText(time);
	}
}
