package se206.quinzical.views;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import se206.quinzical.models.Question;
import se206.quinzical.models.QuizModel;
import se206.quinzical.models.util.KeyEventManager;

/**
 * This class is Pane type, and contains AnimatedProgressBar atom.
 * View for incorrect answer screen, after an incorrect answer has been submitted
 * 
 * Used by GameSwitch and PracticeSwitch.
 * 
 */
public class IncorrectPane extends ViewBase {
	private static final int TIMEOUT_SECS = 4;
	private final Label _answerLabel = new Label();
	private final VBox _container = new VBox();
	private final QuizModel _model;
	private final AnimatedProgressBar _progressBarView;

	public IncorrectPane(QuizModel model) {
		_model = model;
		_progressBarView = new AnimatedProgressBar(TIMEOUT_SECS, item -> exitView());

		// show incorrect answer
		Label incorrectLabel = new Label("Incorrect!");
		Label answerPrefixLabel = new Label("The correct answer was ");
		Label answerSuffixLabel = new Label(".");
		TextFlow answerText = new TextFlow(answerPrefixLabel, _answerLabel, answerSuffixLabel);
		
		answerText.getStyleClass().add("text-flow");
		incorrectLabel.getStyleClass().addAll("text-bold", "text-main");
		_answerLabel.getStyleClass().add("text-bold");

		Label interactToSkipLabel = new Label("Click or press any key to skip...");
		interactToSkipLabel.getStyleClass().add("interact-to-skip");

		// add elements and styles to container
		_container.getChildren().addAll(incorrectLabel, answerText, interactToSkipLabel, _progressBarView.getView());
		_container.getStyleClass().add("incorrect-view");
		addStylesheet("incorrect.css");

		// handle starting / stopping the animated progress bar
		onVisibilityChanged();
		_container.visibleProperty().addListener((observable, oldVal, newVal) -> onVisibilityChanged());

		// click to exit
		_container.setOnMouseClicked(e -> exitView());

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
	 * Called when we are ready to leave this screen
	 */
	private void onVisibilityChanged() {
		if (!_container.isVisible()) {
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
			
			_answerLabel.setText(q.getAnswer().get(0));
		}
	}
}
