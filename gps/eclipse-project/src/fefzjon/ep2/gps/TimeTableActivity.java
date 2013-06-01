package fefzjon.ep2.gps;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import fefzjon.ep2.gps.dialog.SelectBUSPDialogFragment;
import fefzjon.ep2.gps.dialog.SelectDayTypeDialogFragment;
import fefzjon.ep2.gps.utilities.TimetableManager;
import fefzjon.ep2.gps.utilities.TimetableManager.DayType;

public class TimeTableActivity extends FragmentActivity implements
		SelectDayTypeDialogFragment.DayTypeDialogListener,
		SelectBUSPDialogFragment.BUSPDialogListener {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.timetable_layout);

		Intent intent = this.getIntent();
		int buspCode = intent.getIntExtra(MainActivity.BUSP_CODE, 8012);

		this.updateContents(buspCode, TimetableManager.getDayType());
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
		String busp = "ERRO";
		if (buspCode == 8012) {
			busp = res.getString(R.string.busp8012);
		} else if (buspCode == 8022) {
			busp = res.getString(R.string.busp8022);
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
		int duration = TimetableManager.getApproxRouteDuration(buspCode,
				dayType);
		tvDuration.setText(String.valueOf(duration) + "min");

		ListView lvTable = (ListView) this.findViewById(R.id.timetable);
		lvTable.setAdapter(new TimeTableAdapter(buspCode, dayType));
	}

	private class TimeTableAdapter extends BaseAdapter {

		private int buspCode;
		private DayType dayType;

		public TimeTableAdapter(final int buspCode,
				final TimetableManager.DayType dayType) {
			this.buspCode = buspCode;
			this.dayType = dayType;
		}

		public int getBuspCode() {
			return this.buspCode;
		}

		public DayType getDayType() {
			return this.dayType;
		}

		@Override
		public int getCount() {
			TypedArray times = TimetableManager.getDepartureTimes(
					this.buspCode, this.dayType);
			int count = times.length();
			times.recycle();
			return count;
		}

		@Override
		public Object getItem(final int position) {
			TypedArray times = TimetableManager.getDepartureTimes(
					this.buspCode, this.dayType);
			String obj = times.getString(position);
			times.recycle();
			return obj;
		}

		@Override
		public long getItemId(final int position) {
			return 0;
		}

		@Override
		public View getView(final int position, final View convertView,
				final ViewGroup parent) {
			TextView text = new TextView(parent.getContext());
			text.setGravity(Gravity.CENTER);
			text.setText((String) this.getItem(position));
			return text;
		}

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
}
