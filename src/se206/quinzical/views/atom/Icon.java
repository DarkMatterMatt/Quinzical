package se206.quinzical.views.atom;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import se206.quinzical.views.base.ViewBase;

/**
 * This class is Atom type.
 * Shows an image.
 * <p>
 * Used by TaskBar, CategoryPreview.
 */
public class Icon extends ViewBase {
	private final ImageView _image = new ImageView();
	private final HBox _container = new HBox(_image);

	public Icon(String pathRelativeToIconView) {
		this();
		setImage(pathRelativeToIconView);
	}

	public Icon(Image img) {
		this();
		setImage(img);
	}

	public Icon() {
		_image.setPreserveRatio(true);
		_image.setSmooth(true);
		_image.setCache(true);
		_image.getStyleClass().add("icon-view-image");
		_container.getStyleClass().add("icon-view");
		_container.setAlignment(Pos.CENTER);
	}

	public Icon addClass(String... class_) {
		_container.getStyleClass().addAll(class_);
		return this;
	}

	@Override
	public HBox getView() {
		return _container;
	}

	public Icon setAlignment(Pos pos) {
		_container.setAlignment(pos);
		return this;
	}

	public Icon setContainerSize(int width, int height) {
		_container.setPrefWidth(width);
		_container.setPrefHeight(height);
		return this;
	}

	public Icon setImage(String pathRelativeToIconView) {
		_image.setImage(new Image(Icon.class.getResourceAsStream(pathRelativeToIconView)));
		return this;
	}

	public Icon setImage(Image img) {
		_image.setImage(img);
		return this;
	}

	public Icon setImageSize(int width, int height) {
		_image.setFitWidth(width);
		_image.setFitHeight(height);
		return this;
	}

	public Icon setSize(int width, int height) {
		setContainerSize(width, height);
		setImageSize(width, height);
		return this;
	}
}
