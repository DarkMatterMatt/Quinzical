package se206.quinzical.views;

import javafx.animation.AnimationTimer;
import javafx.scene.control.ProgressBar;

import java.util.function.Consumer;

public class AnimatedProgressBarView extends View {
	private final double _timeoutSecs;
	private final Consumer<AnimatedProgressBarView> _onFinishedListener;
	private final ProgressBar _progressBar = new ProgressBar(0);
	private final AnimationTimer _progressBarTimer;

	public AnimatedProgressBarView(double timeoutSecs, Consumer<AnimatedProgressBarView> onFinishedListener) {
		_timeoutSecs = timeoutSecs;
		_onFinishedListener = onFinishedListener;
		_progressBarTimer = new AnimationTimer() {
			private long startTime;

			@Override
			public void handle(long now) {
				double elapsedSecs = (now - startTime) / 1e9;
				double progress = elapsedSecs / _timeoutSecs;
				_progressBar.setProgress(progress);

				if (progress >= 1) {
					// progress bar is full
					stop();
					onAnimatedFinished();
				}
			}

			@Override
			public void start() {
				super.start();
				startTime = System.nanoTime();
			}
		};
		addStylesheet("animated-progress-bar.css");
	}

	/**
	 * Called when progress bar is full
	 */
	private void onAnimatedFinished() {
		_onFinishedListener.accept(this);
	}

	/**
	 * Start animation
	 */
	public void start() {
		_progressBarTimer.start();
	}

	/**
	 * Stop and reset animation
	 */
	public void stop() {
		_progressBarTimer.stop();
	}

	@Override
	public ProgressBar getView() {
		return _progressBar;
	}
}
