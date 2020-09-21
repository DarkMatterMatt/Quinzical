package se206.quinzical.models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import se206.quinzical.models.util.FileBrowser;
import se206.quinzical.models.util.MyScanner;
import se206.quinzical.models.util.TextToSpeech;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class QuinzicalModel {
	private final GameModel _gameModel;
	private final PracticeModel _practiceModel;
	private final ObjectProperty<State> _state = new SimpleObjectProperty<>(State.MENU);
	private final TextToSpeech _textToSpeech = new TextToSpeech();
	List<Category> _categories;
	PresetQuinzicalModel _presetModel;
	private Question _currentQuestion = null;

	public QuinzicalModel() {
		//read files from directory
		this(FileBrowser.filesInDirectory("./categories"));
	}

	public QuinzicalModel(File[] categories) {
		_categories = new ArrayList<Category>();
		for (File category : categories) {
			//list of questions
			List<String> questions = MyScanner.readFileOutputString(category);
			//make category out of that
			Category newCategory = new Category(questions, category.getName());
			_categories.add(newCategory);
		}

		_presetModel = new PresetQuinzicalModel(this);
		_practiceModel = new PracticeModel(this);
		_gameModel = new GameModel(this);
	}

	public Question getCurrentQuestion() {
		return _currentQuestion;
	}

	public void setCurrentQuestion(Question q) {
		_currentQuestion = q;
	}

	public GameModel getGameModel() {
		return _gameModel;
	}

	public PracticeModel getPracticeModel() {
		return _practiceModel;
	}

	public State getState() {
		return _state.get();
	}

	public ObjectProperty<State> getStateProperty() {
		return _state;
	}

	public TextToSpeech getTextToSpeech() {
		return _textToSpeech;
	}

	public void reset() {
		if (_state.get() != State.GAME) {
			throw new IllegalStateException("Can only reset when in the GAME state");
		}
		_gameModel.reset();
	}

	/**
	 * Main screen being shown
	 */
	public enum State {
		MENU,
		GAME,
		PRACTICE,
	}
}
