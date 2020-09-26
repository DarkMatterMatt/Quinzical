package se206.quinzical.views;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import se206.quinzical.models.QuinzicalModel;

/**
 * Main menu which is shown when the game is first loaded.
 */
public class MenuView extends View {
	private final VBox _container = new VBox();

	public MenuView(QuinzicalModel model) {
		// big main title
		Label titleLabel = new Label("Quinzical");
		titleLabel.getStyleClass().addAll("title", "text-bold", "text-gold");

		// play button
		Label playLabel = new Label("Play");
		playLabel.getStyleClass().addAll("btn", "text-gold");
		playLabel.setOnMouseClicked(e -> model.beginGame());

		// practice button
		Label practiceLabel = new Label("Practice");
		practiceLabel.getStyleClass().addAll("btn", "text-gold");
		practiceLabel.setOnMouseClicked(e -> model.beginPracticeGame());

		// quit button
		Label quitLabel = new Label("Quit");
		quitLabel.getStyleClass().addAll("btn", "text-gold");
		quitLabel.setOnMouseClicked(e -> Platform.exit());

		// add styles
		addStylesheet("menu.css");
		_container.getStyleClass().add("menu");
		_container.getChildren().addAll(titleLabel, playLabel, practiceLabel, quitLabel);
	}

	@Override
	public VBox getView() {
		return _container;
	}
}
