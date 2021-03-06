package se206.quinzical.views.pane.answerstate;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import se206.quinzical.models.Question;
import se206.quinzical.models.QuizModel;
import se206.quinzical.models.util.KeyEventManager;
import se206.quinzical.models.util.TextToSpeech;
import se206.quinzical.views.atom.AnimatedProgressBar;
import se206.quinzical.views.base.ViewBase;

/**
 * This class is Pane type, and contains AnimatedProgressBar atom.
 * View for incorrect answer screen, after an incorrect answer has been submitted
 * <p>
 * Used by GameSwitch and PracticeSwitch.
 */
public class SkipPane extends ViewBase {
	private static final int TIMEOUT_SECS = 4;
	private final TextFlow _answerLabel = new TextFlow();
	private final VBox _container = new VBox();
	private final QuizModel _model;
	private final AnimatedProgressBar _progressBarView;

	public SkipPane(QuizModel model) {
		_model = model;
		_progressBarView = new AnimatedProgressBar(TIMEOUT_SECS, item -> exitView());

		// show incorrect answer
		Label incorrectLabel = new Label("Skipped!");
		Label answerPrefixLabel = new Label("The correct answer was ");
		_answerLabel.setStyle("-fx-wrap-text: true");
		Label answerSuffixLabel = new Label(".");
		TextFlow answerText = new TextFlow(answerPrefixLabel, _answerLabel, answerSuffixLabel);

		answerText.getStyleClass().add("text-flow");
		incorrectLabel.getStyleClass().addAll("text-bold", "text-main");

		Label interactToSkipLabel = new Label("Click or press any key to skip...");
		interactToSkipLabel.getStyleClass().add("interact-to-skip");

		// add elements and styles to container
		_container.getChildren().addAll(incorrectLabel, answerText, interactToSkipLabel, _progressBarView.getView());
		_container.getStyleClass().add("skip-view");
		addStylesheet("skip.css");

		// handle starting / stopping the animated progress bar
		onVisibilityChanged();
		_container.visibleProperty().addListener((observable, oldVal, newVal) -> onVisibilityChanged());

		// click to exit
		_container.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> exitView());

		// press any key to exit
		KeyEventManager.getInstance().addPressListener(ev -> {
			if (_container.isVisible()) {
				exitView();
			}
		}, KeyEventManager.ANY_KEY);
	}

	/**
	 * Called when we are ready to leave this screen
	 */
	private void exitView() {
		_model.finishQuestion();
	}

	public VBox getView() {
		return _container;
	}

	/**
	 * Check if we are visible, then update the question
	 */
	private void onVisibilityChanged() {
		if (!_container.isVisible() || !_model.isActive()) {
			// container is hidden
			_progressBarView.stop();
			return;
		}
		// container was made visible
		_progressBarView.start();
		questionUpdate(_model.getCurrentQuestion());
	}

	/**
	 * Update to show correct answer
	 */
	private void questionUpdate(Question q) {
		if (q != null) {
			// speak correct answer
			TextToSpeech.getInstance().speak("Skipped. The correct answer was " + q.getAnswer().get(0));

			_answerLabel.getChildren().clear();

			q.getAnswer().forEach(a -> {
				_answerLabel.getChildren().add(new Label(" or "));
				Label l = new Label(a);
				l.getStyleClass().add("text-bold");
				_answerLabel.getChildren().add(l);
			});
			_answerLabel.getChildren().remove(0);
		}
	}
}
