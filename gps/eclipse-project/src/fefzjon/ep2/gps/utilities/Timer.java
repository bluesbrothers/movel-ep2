package fefzjon.ep2.gps.utilities;

import android.os.Handler;

public class Timer {
	public interface TimerCallback {
		public void onTick();
	}

	private Handler handler;
	private boolean isRunning;
	private long interval;
	private TimerCallback callback;

	public Timer(final TimerCallback callback, final long delayIntervalMillisecs) {
		this.handler = new Handler();
		this.interval = delayIntervalMillisecs;
		this.isRunning = false;
		this.callback = callback;

	}

	private Runnable updateTask = new Runnable() {
		@Override
		public void run() {
			Timer.this.callback.onTick();
			Timer.this.handler.postDelayed(Timer.this.updateTask,
					Timer.this.interval);
		}
	};

	public void start() {
		if (this.isRunning) {
			return;
		}
		this.handler.removeCallbacks(this.updateTask);
		this.handler.postDelayed(this.updateTask, this.interval);
		this.isRunning = true;
	}

	public void stop() {
		if (!this.isRunning) {
			return;
		}
		this.handler.removeCallbacks(this.updateTask);
		this.isRunning = false;
	}
}
