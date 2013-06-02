package fefzjon.ep2.bandejao;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import fefzjon.ep2.bandejao.manager.StoaManager;
import fefzjon.ep2.bandejao.utils.IntentKeys;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {
	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask	mAuthTask	= null;

	// Values for email and password at the time of the login attempt.
	private String			mEmail;
	private String			mPassword;

	// UI references.
	private EditText		mNUSPView;
	private EditText		mPasswordView;
	private View			mLoginFormView;
	private View			mLoginStatusView;
	private TextView		mLoginStatusMessageView;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.activity_login);

		this.mNUSPView = (EditText) this.findViewById(R.id.nusp_field);

		this.mPasswordView = (EditText) this.findViewById(R.id.password);
		this.mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(final TextView textView, final int id, final KeyEvent keyEvent) {
				if ((id == R.id.login) || (id == EditorInfo.IME_NULL)) {
					LoginActivity.this.attemptLogin();
					return true;
				}
				return false;
			}
		});

		this.mLoginFormView = this.findViewById(R.id.login_form);
		this.mLoginStatusView = this.findViewById(R.id.login_status);
		this.mLoginStatusMessageView = (TextView) this.findViewById(R.id.login_status_message);

		this.findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				LoginActivity.this.attemptLogin();
			}
		});
	}

	public void attemptLogin() {
		if (this.mAuthTask != null) {
			return;
		}

		// Reset errors.
		this.mNUSPView.setError(null);
		this.mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		this.mEmail = this.mNUSPView.getText().toString();
		this.mPassword = this.mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(this.mPassword)) {
			this.mPasswordView.setError(this.getString(R.string.error_field_required));
			focusView = this.mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(this.mEmail)) {
			this.mNUSPView.setError(this.getString(R.string.error_field_required));
			focusView = this.mNUSPView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			this.mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			this.showProgress(true);
			this.mAuthTask = new UserLoginTask();
			this.mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = this.getResources().getInteger(android.R.integer.config_shortAnimTime);

			this.mLoginStatusView.setVisibility(View.VISIBLE);
			this.mLoginStatusView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(final Animator animation) {
							LoginActivity.this.mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
						}
					});

			this.mLoginFormView.setVisibility(View.VISIBLE);
			this.mLoginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(final Animator animation) {
							LoginActivity.this.mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			this.mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			this.mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(final Void... params) {
			// TODO: attempt authentication against a network service.
			try {
				Thread.sleep(1000);
				return StoaManager.getInstance().postLogin("", "");
			} catch (InterruptedException e) {
			} catch (KeyManagementException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			LoginActivity.this.mAuthTask = null;
			LoginActivity.this.showProgress(false);

			if (success) {
				StoaManager.getInstance().loginSuccess();
				Intent intent = new Intent(LoginActivity.this, ComentariosActivity.class);
				intent.putExtra(IntentKeys.DETAILS_BANDECO_ID,
						LoginActivity.this.getIntent().getIntExtra(IntentKeys.DETAILS_BANDECO_ID, 1));
				intent.putExtra(IntentKeys.DETAILS_MEAL_ID,
						LoginActivity.this.getIntent().getIntExtra(IntentKeys.DETAILS_MEAL_ID, 1));
				LoginActivity.this.startActivity(intent);

				LoginActivity.this.finish();
			} else {
				LoginActivity.this.mPasswordView.setError(LoginActivity.this
						.getString(R.string.error_incorrect_user_or_password));
				LoginActivity.this.mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			LoginActivity.this.mAuthTask = null;
			LoginActivity.this.showProgress(false);
		}
	}
}
