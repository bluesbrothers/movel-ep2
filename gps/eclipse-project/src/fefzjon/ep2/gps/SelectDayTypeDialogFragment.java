package fefzjon.ep2.gps;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import fefzjon.ep2.gps.TimetableManager.DayType;

public class SelectDayTypeDialogFragment extends DialogFragment {

	private DayTypeDialogListener listener;

	@Override
	public Dialog onCreateDialog(final Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				this.getActivity());
		builder.setTitle(R.string.selectDayTypeDialog);
		String[] dayTypes = new String[] { "Dia Útil", "Sábado", "Domingo" };
		builder.setItems(dayTypes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				// The 'which' argument contains the index position
				// of the selected item
				DayType dayType = null;
				if (which == 0) {
					dayType = DayType.UTIL;
				} else if (which == 1) {
					dayType = DayType.SATURDAY;
				} else if (which == 2) {
					dayType = DayType.SUNDAY;
				}
				SelectDayTypeDialogFragment.this.listener
						.onDayTypeSelected(dayType);
				SelectDayTypeDialogFragment.this.dismiss();
			}
		});
		return builder.create();
	}

	public interface DayTypeDialogListener {
		public void onDayTypeSelected(DayType dayType);
	}

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);
		try {
			this.listener = (DayTypeDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement DayTypeDialogListener");
		}
	}
}
