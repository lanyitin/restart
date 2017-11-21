package tw.org.ttc.iot.model.script;

import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;

public class LoopAST implements AST {
	private final ObservableList<AST> childNodes;
	private int count;
	
	public LoopAST(AST body, int count) {
		this.childNodes = FXCollections.observableArrayList();
		this.childNodes.add(body);
		this.count = count;
	}
	@Override
	public ObservableList<AST> getChildNodes() {
		return this.childNodes;
	}
	@Override
	public String getTextForTreeCell() {
		return String.format("Repeat for %d times", count);
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
            	    LoopAST.this.count = Integer.valueOf(result.get());
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
		if (value != this) {
			return true;
		} else {
			return false;
		}
	}

}
