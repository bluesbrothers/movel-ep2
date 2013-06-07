package fefzjon.ep2.gps.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import fefzjon.ep2.gps.R;

public class SelectBUSPDialogFragment extends DialogFragment {

	private BUSPDialogListener listener;

	@Override
	public Dialog onCreateDialog(final Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				this.getActivity());
		builder.setTitle(R.string.selectBUSPDialog);
		Resources res = this.getResources();
		String[] buspTypes = new String[] { res.getString(R.string.busp8012),
				res.getString(R.string.busp8022),
				res.getString(R.string.buspAmbos) };
		builder.setItems(buspTypes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int which) {

				SelectBUSPDialogFragment.this.listener
						.onBUSPSelected(8012 + (which * 10));
				SelectBUSPDialogFragment.this.dismiss();
			}
		});
		return builder.create();
	}

	public interface BUSPDialogListener {
		public void onBUSPSelected(int buspCode);
	}

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);
		try {
			this.listener = (BUSPDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement BUSPDialogListener");
		}
	}
}
