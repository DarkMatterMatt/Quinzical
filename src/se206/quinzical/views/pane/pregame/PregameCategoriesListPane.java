package se206.quinzical.views.pane.pregame;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import se206.quinzical.models.Category;
import se206.quinzical.models.QuinzicalModel;
import se206.quinzical.views.base.ViewBase;

/**
 * This class is Pane type.
 * 
 * It lists possible categories the user can select to be included in the actual
 * game. Multiple selection is possible. The cell's string value is retrieved
 * from the list of categories from the QuinzicalModel.
 * 
 * @author hajinkim
 */
public class PregameCategoriesListPane extends ViewBase {
	private final VBox _container;
	private final ListView<Category> _listView;
	private final QuinzicalModel _model;
	private final HBox _textBox;

	public PregameCategoriesListPane(QuinzicalModel model) {
		_model = model;

		// set up the top header box
		Label topText = new Label("Categories");
		_textBox = new HBox(topText);

		// set up data
		ObservableList<Category> data = FXCollections.observableArrayList();
		data.addAll(model.getCategories());

		// set up list view
		_listView = new ListView<>(data);
		_container = new VBox(_textBox, _listView);
		_listView.setStyle("-fx-padding: 0px;");
		_listView.setMinWidth(300);

		// avoid non-selection thin blue box by preemptively selecting one cell
		_listView.getFocusModel().focus(0);
		_listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		_listView.setCellFactory((ListView<Category> param) -> {
			ListCell<Category> cell = new ListCell<Category>() {
				@Override
				public void updateItem(Category item, boolean empty) {
					super.updateItem(item, empty);
					if (empty || item == null) {
						setText(null);
						setGraphic(null);
					}
					else {
						HBox displayedItem = new CategoriesListItemView(item).getView();
						if (item.isPregameSelected()) {
							displayedItem.getStyleClass().addAll("text-bold", "category", "selected");
						}
						else {
							displayedItem.getStyleClass().addAll("text-bold", "category", "not-selected");
						}
						setGraphic(displayedItem);
					}

				}
			};
			cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
				if (event.getButton() == MouseButton.PRIMARY && (!cell.isEmpty())) {
					Category item = cell.getItem();

					if (item.isPregameSelected()) {
						item.setPregameUnselected();
					} else {
						if (!_model.getPresetModel().pregameCategorySelectionLimitReached()) {
							item.setPregameSelected();
						}
					}
					_model.getPresetModel().notifyPregameCategoryUpdated();
					_listView.refresh();
				}
			});
			return cell;

		});


		// style
		topText.getStyleClass().addAll("text-large", "text-bold", "text-gold");
		_textBox.getStyleClass().addAll("text-container");

		_container.getStyleClass().add("categories-list");
		addStylesheet("category-listview.css");

		_model.getPresetModel().getToBeInitialisedProperty().addListener((obs, n, old) -> _listView.refresh());
	}

	@Override
	public VBox getView() {
		return _container;
	}

	/**
	 * Objects of this type is a graphic (HBox) that prettifies
	 * each category String for each cell.
	 *
	 * @author hajinkim
	 */
	private static class CategoriesListItemView {
		private final Category _category;
		private final HBox _container = new HBox();

		public CategoriesListItemView(Category item) {
			_category = item;
			Label label = new Label(item.getName());
			label.setWrapText(true);
			_container.getChildren().add(label);

			// styling
			if (item.isSelected()) {
				label.getStyleClass().add("text-black");
			}
			else {
				label.getStyleClass().add("text-white");
			}
		}

		public Category getCategory() {
			return _category;
		}

		public HBox getView() {
			return _container;
		}
	}
}
