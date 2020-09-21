package se206.quinzical.views;

import javafx.scene.layout.VBox;
import se206.quinzical.models.QuinzicalModel;

public class QuizView extends View {
	private final VBox _container = new VBox();

	public QuizView(QuinzicalModel model) {
		HeaderView headerView = new HeaderView(model);
		QuizContentView quizContentView = new QuizContentView(model);

		_container.getChildren().addAll(headerView.getView(), quizContentView.getView());
		_container.getStyleClass().add("quiz-view");
		addStylesheet("quiz.css");
	}

	@Override
	public VBox getView() {
		return _container;
	}
}