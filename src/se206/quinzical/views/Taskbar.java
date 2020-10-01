package se206.quinzical.views;

import javafx.application.Platform;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import se206.quinzical.DragAndResizeHelper;
import se206.quinzical.models.QuinzicalModel;

/**
 * This is Atom type.
 * View for taskbar. Contains reset & quit buttons
 * 
 * Used by HeaderView.
 */
public class Taskbar extends ViewBase {
	private final HBox _container = new HBox();
	private final QuinzicalModel _model;
	private final ImageView _reset;
	private final StackPane _toggleText;

	public Taskbar(QuinzicalModel model) {
		_model = model;

		// exit button view
		ImageView exit = createButton("../assets/exit.png");
		exit.setOnMouseClicked(e -> {
			Alert exitAlert = new Alert(AlertType.NONE);
			exitAlert.setHeaderText("Sad to see you go!");
			exitAlert.setContentText("Are you sure you want to exit Quinzical?");
			
			// style alert
			DialogPane dialogue = exitAlert.getDialogPane();
			ButtonType yes = new ButtonType("Yup",ButtonBar.ButtonData.YES);
			ButtonType no = new ButtonType("Actually nah",ButtonBar.ButtonData.NO);
			dialogue.getButtonTypes().addAll(yes, no);
			dialogue.getStylesheets().add(getClass().getResource("../styles/dialogue.css").toExternalForm());
			// stage of dialogue
			Stage diaStage = (Stage) dialogue.getScene().getWindow();
			diaStage.initStyle(StageStyle.UNDECORATED);
			DragAndResizeHelper.addResizeListener(diaStage);
			
			exitAlert.showAndWait().filter(res -> res==yes).ifPresent(res -> {
				Platform.exit();
			});


		});
		Tooltip.install(exit, new Tooltip("Quit"));

		// reset button view
		_reset = createButton("../assets/reset.png");
		_reset.setOnMouseClicked(e -> {
			
			model.reset();
			
		});
		Tooltip.install(_reset, new Tooltip("Reset game"));

		// home button view
		ImageView home = createButton("../assets/home.png");
		home.setOnMouseClicked(e->{
			//change state to menu
			model.backToMainMenu();
		});
		Tooltip.install(home, new Tooltip("Main Menu"));

		// text and notext icons
		HBox text = new HBox(createButton("../assets/text.png"));
		HBox noText = new HBox(createButton("../assets/notext.png"));

		SwitcherBase s = new SwitcherBase() {};
		s.getView().getChildren().addAll(text,noText);

		// enable text
		_toggleText = s.getView();
		_toggleText.setOnMouseClicked(e -> {
			model.toggleTextVisibility();
			s.switchToView(model.textVisible() ? text : noText);
		});
		Tooltip.install(text, new Tooltip("Text Currently Visible"));
		Tooltip.install(noText, new Tooltip("Text Currently Invisible"));

		_container.getChildren().addAll(_toggleText, home, _reset, exit);
		_container.setSpacing(10);
		_container.getStyleClass().add("taskbar");
        addStylesheet("taskbar.css");

		// show reset button ONLY during game state
		onModelStateChange();
		_model.getStateProperty().addListener((obs, old, val) -> onModelStateChange());
	}

	private ImageView createButton(String filename) {
		ImageView v = new ImageView(new Image(getClass().getResourceAsStream(filename)));
		v.setFitHeight(32);
		v.setPreserveRatio(true);
		v.setSmooth(true);
		v.setCache(true);
		v.setPickOnBounds(true);
		v.getStyleClass().add("btn");
		return v;
	}

	private void onModelStateChange() {
		// show reset button ONLY during game state
		switch (_model.getState()) {
			case GAME:
				_reset.setVisible(true);
				_reset.setManaged(true);
				break;
			case MENU:
			case PRACTICE:
				_reset.setVisible(false);
				_reset.setManaged(false);
				break;
			default:
				throw new UnsupportedOperationException("Unexpected model state");
		}
	}
	public Pane getView() {
		return _container;
	}
}