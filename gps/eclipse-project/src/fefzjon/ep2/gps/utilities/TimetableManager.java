package fefzjon.ep2.gps.utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.content.res.TypedArray;
import fefzjon.ep2.gps.MainActivity;
import fefzjon.ep2.gps.R;

public class TimetableManager {

	public enum DayType {
		UTIL, SATURDAY, SUNDAY
	};

	private static MainActivity parent;

	public static void setMainActivity(final MainActivity activity) {
		parent = activity;
	}

	public static DayType getDayType() {
		return getDayType(new Date());
	}

	public static DayType getDayType(final Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == Calendar.SUNDAY) {
			return DayType.SUNDAY;
		} else if (dayOfWeek == Calendar.SATURDAY) {
			return DayType.SATURDAY;
		}
		return DayType.UTIL;
	}

	public static int getApproxRouteDuration() {
		return getApproxRouteDuration(parent.getBuspCode(), getDayType());
	}

	public static int getApproxRouteDuration(final int buspCode,
			final DayType dayType) {
		Resources res = parent.getResources();
		if (buspCode == 8012) {
			if (dayType == DayType.UTIL) {
				return Integer.valueOf(res.getString(R.string.duracaoUtil8012));
			} else if (dayType == DayType.SATURDAY) {
				return Integer.valueOf(res
						.getString(R.string.duracaoSabado8012));
			} else if (dayType == DayType.SUNDAY) {
				return Integer.valueOf(res
						.getString(R.string.duracaoDomingo8012));
			}
		} else if (buspCode == 8022) {
			if (dayType == DayType.UTIL) {
				return Integer.valueOf(res.getString(R.string.duracaoUtil8022));
			} else if (dayType == DayType.SATURDAY) {
				return Integer.valueOf(res
						.getString(R.string.duracaoSabado8022));
			} else if (dayType == DayType.SUNDAY) {
				return Integer.valueOf(res
						.getString(R.string.duracaoDomingo8022));
			}
		}

		return 0;
	}

	public static TypedArray getDepartureTimes() {
		return getDepartureTimes(parent.getBuspCode(), getDayType());
	}

	public static TypedArray getDepartureTimes(final int buspCode,
			final DayType dayType) {
		Resources res = parent.getResources();
		if (buspCode == 8012) {
			if (dayType == DayType.UTIL) {
				return res.obtainTypedArray(R.array.horariosUtil8012);
			} else if (dayType == DayType.SATURDAY) {
				return res.obtainTypedArray(R.array.horariosSabado8012);
			} else if (dayType == DayType.SUNDAY) {
				return res.obtainTypedArray(R.array.horariosDomingo8012);
			}
		} else if (buspCode == 8022) {
			if (dayType == DayType.UTIL) {
				return res.obtainTypedArray(R.array.horariosUtil8022);
			} else if (dayType == DayType.SATURDAY) {
				return res.obtainTypedArray(R.array.horariosSabado8022);
			} else if (dayType == DayType.SUNDAY) {
				return res.obtainTypedArray(R.array.horariosDomingo8022);
			}
		}
		return null;
	}

	public static Date getDateMinus(final int minutes) {
		return getDateAdd(new Date(), -minutes);
	}

	public static Date getDateAdd(final int minutes) {
		return getDateAdd(new Date(), minutes);
	}

	public static Date getDateAdd(final Date date, final int minutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, minutes);
		return cal.getTime();
	}

	public static Date getDateForDeparture(final String departureTime) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		String[] parts = departureTime.split(":");
		cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(parts[0]));
		cal.set(Calendar.MINUTE, Integer.valueOf(parts[1]));
		cal.set(Calendar.SECOND, Integer.valueOf(parts[2]));
		return cal.getTime();
	}

	@SuppressLint("SimpleDateFormat")
	public static String getDateStr(final Date date) {
		return new SimpleDateFormat("HH:mm:ss").format(date);
	}

	public static int getTimeCode(final String deptTime) {
		String[] parts = deptTime.split(":");
		return (Integer.valueOf(parts[0]) * 10000)
				+ (Integer.valueOf(parts[1]) * 100) + Integer.valueOf(parts[2]);
	}

	public static String getDepartureAfter(final Date time) {
		TypedArray times = getDepartureTimes(parent.getBuspCode(),
				getDayType(time));
		Calendar cal = Calendar.getInstance();
		for (int i = 0; i < times.length(); i++) {
			String dep = times.getString(i);
			String[] parts = dep.split(":");

			cal.setTime(time);
			cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(parts[0]));
			cal.set(Calendar.MINUTE, Integer.valueOf(parts[1]));
			cal.set(Calendar.SECOND, Integer.valueOf(parts[2]));

			if (time.before(cal.getTime())) {
				return dep;
			}
		}
		times.recycle();
		// if we reached this point is because TIME is after all departure
		// times in this day, so we go and seek in the next day.
		cal.setTime(time);
		cal.add(Calendar.DAY_OF_YEAR, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return getDepartureAfter(cal.getTime());
	}
}
