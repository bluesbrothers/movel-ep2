package fefzjon.ep2.gps.utilities;

import java.util.ArrayList;
import java.util.List;

import android.content.res.TypedArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import fefzjon.ep2.gps.utilities.TimetableManager.DayType;

public class TimeTableAdapter extends BaseAdapter {

	private int buspCode;
	private DayType dayType;
	private List<String> times;

	public TimeTableAdapter(final int buspCode,
			final TimetableManager.DayType dayType) {
		this.buspCode = buspCode;
		this.dayType = dayType;
		this.setup();
	}

	private void setup() {
		this.times = new ArrayList<String>();
		if (this.buspCode == Constants.BUSP_AMBOS) {
			TypedArray deptTimes1 = TimetableManager.getDepartureTimes(
					Constants.BUSP_1, this.dayType);
			TypedArray deptTimes2 = TimetableManager.getDepartureTimes(
					Constants.BUSP_2, this.dayType);
			int i = 0;
			int j = 0;
			String busp1 = String.valueOf(Constants.BUSP_1) + " - ";
			String busp2 = String.valueOf(Constants.BUSP_2) + " - ";
			int prevDt1 = 0;
			int prevDt2 = 0;
			while (true) {
				if ((i < deptTimes1.length()) && (j < deptTimes2.length())) {
					int dt1 = TimetableManager.getTimeCode(deptTimes1
							.getString(i));
					if (((prevDt1 / 10000) == 23) && ((dt1 / 10000) != 23)) {
						dt1 += 240000;
					} else {
						prevDt1 = dt1;
					}
					int dt2 = TimetableManager.getTimeCode(deptTimes2
							.getString(j));
					if (((prevDt2 / 10000) == 23) && ((dt2 / 10000) != 23)) {
						dt2 += 240000;
					} else {
						prevDt2 = dt2;
					}
					if (dt1 <= dt2) {
						this.times.add(busp1 + deptTimes1.getString(i++));
					} else if (dt2 < dt1) {
						this.times.add(busp2 + deptTimes2.getString(j++));
					}
				} else if (i < deptTimes1.length()) {
					this.times.add(busp1 + deptTimes1.getString(i++));
				} else if (j < deptTimes2.length()) {
					this.times.add(busp2 + deptTimes2.getString(j++));
				} else {
					break;
				}
			}
			deptTimes1.recycle();
			deptTimes2.recycle();
		} else {
			TypedArray deptTimes = TimetableManager.getDepartureTimes(
					this.buspCode, this.dayType);
			for (int i = 0; i < deptTimes.length(); i++) {
				this.times.add(deptTimes.getString(i));
			}
			deptTimes.recycle();
		}
	}

	public int getBuspCode() {
		return this.buspCode;
	}

	public DayType getDayType() {
		return this.dayType;
	}

	@Override
	public int getCount() {
		return this.times.size();
	}

	@Override
	public Object getItem(final int position) {
		return this.times.get(position);
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
		String s = String.valueOf(this.getItem(position));
		text.setText(s);
		if (this.buspCode == Constants.BUSP_AMBOS) {
			String[] parts = s.split(" ");
			int code = Integer.valueOf(parts[0]);
			if (code == Constants.BUSP_1) {
				text.setBackgroundColor(0x80ff0000);
			} else if (code == Constants.BUSP_2) {
				text.setBackgroundColor(0x8000ff00);
			}
		}
		return text;
	}

}
