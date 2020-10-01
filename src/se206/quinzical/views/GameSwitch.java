package se206.quinzical.views;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import se206.quinzical.models.PresetQuinzicalModel;
import se206.quinzical.models.QuinzicalModel;

/**
 * This is Pane type.
 * Main content layout for the 'real' game
 * 
 * Used by QuizContentSwitch
 */
public class GameSwitch extends SwitcherBase {
	private final AnswerPane _answerQuestion;
	private final CorrectPane _correctPane;
	private final IncorrectPane _incorrectPane;
	private final GameOverPane _gameOverPane;
	private final QuinzicalModel _model;
	private final PresetQuinzicalModel _presetModel;
	private final HBox _questionSelectContainer = new HBox();

	public GameSwitch(QuinzicalModel model) {
		_model = model;
		_presetModel = _model.getPresetModel();

		// create primary views
		_answerQuestion = new AnswerPane(_presetModel);
		_correctPane = new CorrectPane(_presetModel);
		_incorrectPane = new IncorrectPane(_presetModel);
		_gameOverPane = new GameOverPane(_presetModel);

		CategoriesList categoriesListPane = new CategoriesList(_presetModel);
		CategoryPreviewPane categoryPreviewPane = new CategoryPreviewPane(_presetModel);

		// categoryPreviewPane is centered inside its container
		VBox categoryPreviewContainer = new VBox(categoryPreviewPane.getView());
		HBox.setHgrow(categoryPreviewContainer, Priority.ALWAYS);
		categoryPreviewContainer.getStyleClass().add("category-preview-container");

		// question selection includes the list of categories & current category preview
		_questionSelectContainer.getChildren().addAll(categoriesListPane.getView(), categoryPreviewContainer);

		// add styles
		addStylesheet("game.css");
		getView().getStyleClass().add("game");
		getView().getChildren().addAll(_questionSelectContainer, _answerQuestion.getView(),
				_correctPane.getView(), _incorrectPane.getView(), _gameOverPane.getView());

		// listen for state changes
		onModelStateChange();
		_presetModel.getStateProperty().addListener((obs, old, val) -> onModelStateChange());
	}

	private void onModelStateChange() {
		switch (_presetModel.getState()) {
			case SELECT_CATEGORY:
			case CATEGORY_PREVIEW:
			case RESET:
				switchToView(_questionSelectContainer);
				break;
			case INCORRECT_ANSWER:
				switchToView(_incorrectPane.getView());
				break;
			case CORRECT_ANSWER:
				switchToView(_correctPane.getView());
				break;
			case ANSWER_QUESTION:
				switchToView(_answerQuestion.getView());
				break;
			case GAME_OVER:
				switchToView(_gameOverPane.getView());
				break;
			default:
				throw new UnsupportedOperationException("Unexpected model state: " + _presetModel.getState());
		}
	}
}