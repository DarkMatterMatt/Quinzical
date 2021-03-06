package se206.quinzical.views.switches;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import se206.quinzical.models.PresetQuinzicalModel;
import se206.quinzical.models.QuinzicalModel;
import se206.quinzical.views.base.SwitcherBase;
import se206.quinzical.views.pane.AnswerPane;
import se206.quinzical.views.pane.answerstate.CorrectPane;
import se206.quinzical.views.pane.answerstate.GameOverPane;
import se206.quinzical.views.pane.answerstate.IncorrectPane;
import se206.quinzical.views.pane.answerstate.SkipPane;
import se206.quinzical.views.pane.category.CategoriesListPane;
import se206.quinzical.views.pane.category.CategoryPreviewPane;
import se206.quinzical.views.pane.pregame.PregameCategoriesListPane;
import se206.quinzical.views.pane.pregame.PregameCategoryIconsPreviewPane;

/**
 * This is Switch type.
 * Main content layout for the 'real' game
 * <p>
 * Used by QuizContentSwitch
 */
public class GameSwitch extends SwitcherBase {
	private final AnswerPane _answerQuestion;
	private final CorrectPane _correctPane;
	private final GameOverPane _gameOverPane;
	private final IncorrectPane _incorrectPane;
	private final QuinzicalModel _model;
	private final PresetQuinzicalModel _presetModel;
	private final HBox _questionSelectContainer = new HBox();
	private final SkipPane _skipPane;
	private final HBox _pregameCategorySelection = new HBox();

	public GameSwitch(QuinzicalModel model) {
		_model = model;
		_presetModel = _model.getPresetModel();

		// create primary views
		_answerQuestion = new AnswerPane(_presetModel);
		_correctPane = new CorrectPane(_presetModel);
		_incorrectPane = new IncorrectPane(_presetModel);
		_skipPane = new SkipPane(_presetModel);
		_gameOverPane = new GameOverPane(_presetModel);
		generateSelectionContainerContents();
		generateGameModeInitialScreen();

		// add styles
		addStylesheet("game.css");
		getView().getStyleClass().add("game");
		getView().getChildren().addAll(_questionSelectContainer, _answerQuestion.getView(),
				_correctPane.getView(), _incorrectPane.getView(), _skipPane.getView(), _gameOverPane.getView(),
				_pregameCategorySelection);

		// listen for state changes
		onModelStateChange();
		_presetModel.getStateProperty().addListener((obs, old, val) -> onModelStateChange());
		_presetModel.getToBeInitialisedProperty().addListener(e -> onModelStateChange());
	}

	private void generateSelectionContainerContents() {
		CategoriesListPane categoriesListPane = new CategoriesListPane(_presetModel);
		CategoryPreviewPane categoryPreviewPane = new CategoryPreviewPane(_presetModel);

		// categoryPreviewPane is centered inside its container
		VBox categoryPreviewContainer = new VBox(categoryPreviewPane.getView());
		HBox.setHgrow(categoryPreviewContainer, Priority.ALWAYS);
		categoryPreviewContainer.getStyleClass().add("category-preview-container");

		// question selection includes the list of categories & current category preview
		_questionSelectContainer.getChildren().addAll(categoriesListPane.getView(), categoryPreviewContainer);
	}

	private void generateGameModeInitialScreen() {
		_pregameCategorySelection.setSpacing(25);
		VBox multipleSelectionList = new PregameCategoriesListPane(_model).getView();
		VBox pregameCategorySelection = new PregameCategoryIconsPreviewPane(_model).getView();
		pregameCategorySelection.setAlignment(Pos.CENTER);
		HBox.setHgrow(pregameCategorySelection, Priority.ALWAYS);
		_pregameCategorySelection.getChildren().addAll(multipleSelectionList, pregameCategorySelection);
	}

	private void onModelStateChange() {
		switch (_presetModel.getState()) {
			case RESET:
				_questionSelectContainer.getChildren().clear();
				generateSelectionContainerContents();
				break;
			case SELECT_CATEGORY:
			case CATEGORY_PREVIEW:
				if (_presetModel.checkNeedToBeInitialised()) {
					_pregameCategorySelection.getChildren().clear();
					generateGameModeInitialScreen();
					switchToView(_pregameCategorySelection);
				} else {
					_questionSelectContainer.getChildren().clear();
					generateSelectionContainerContents();
					switchToView(_questionSelectContainer);
				}
				break;
			case INCORRECT_ANSWER:
				switchToView(_incorrectPane.getView());
				break;
			case CORRECT_ANSWER:
				switchToView(_correctPane.getView());
				break;
			case SKIP_ANSWER:
				switchToView(_skipPane.getView());
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
