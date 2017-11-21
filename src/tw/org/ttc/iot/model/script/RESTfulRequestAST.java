package tw.org.ttc.iot.model.script;

import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import tw.org.ttc.iot.model.EndPoint;

public class RESTfulRequestAST implements AST {
	
	private final EndPoint endPoint;
	private final ObservableList<AST> childNodes;

	public RESTfulRequestAST(EndPoint endPoint) {
		this.endPoint = endPoint;
		childNodes = FXCollections.emptyObservableList();
	}

	@Override
	public ObservableList<AST> getChildNodes() {
		return childNodes;
	}

	@Override
	public String getTextForTreeCell() {
		return String.format("%s %s", endPoint.getMethod(), endPoint.getUrl());
	}
	
	@Override
	public ContextMenu getContextMenu() {
		ContextMenu contextMenu = new ContextMenu();
		MenuItem item1 = new MenuItem("Edit");
        item1.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
            	TextInputDialog dialog = new TextInputDialog("walter");
            	dialog.setTitle("Text Input Dialog");
            	dialog.setHeaderText("Look, a Text Input Dialog");
            	dialog.setContentText("Please enter your name:");

            	// Traditional way to get the response value.
            	Optional<String> result = dialog.showAndWait();
            	if (result.isPresent()){
            	    
            	}
            }
        });
        contextMenu.getItems().add(item1);
		return contextMenu;
	}
	@Override
	public void removeLinkFrom(AST value) {
		this.childNodes.remove(value);
	}
	@Override
	public void addLinkTo(AST value) {
		this.childNodes.add(value);
	}
	@Override
	public boolean isDraggableTo(AST value) {
		return false;
	}
}
