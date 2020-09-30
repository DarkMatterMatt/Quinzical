package se206.quinzical.views;

import javafx.scene.Parent;

/**
 * This class is Base type.
 * Provides the base for Pane type AND Atom type.
 * It provides a method to add stylesheet, and ensures
 * that every class that extends this base will return
 * javaFX parent type (usually javaFX's Pane/Box type),
 * upon getView() call.
 * 
 */
public abstract class ViewBase {
    public abstract Parent getView();

    protected void addStylesheet(String filename) {
        String stylesheet = getClass().getResource("../styles/" + filename).toExternalForm();
        getView().getStylesheets().add(stylesheet);
    }
}
