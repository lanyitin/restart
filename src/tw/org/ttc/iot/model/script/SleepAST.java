package tw.org.ttc.iot.model.script;

import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;

public class SleepAST implements AST {
	
	private long duration;
	
	public SleepAST() {
		this.duration = 0;
	}
	
	public void setDuration(long d) {
		this.duration = d;
	}
	
	public long getDuration() {
		return this.duration;
	}

	@Override
	public ObservableList<AST> getChildNodes() {
		return FXCollections.emptyObservableList();
	}

	@Override
	public String getTextForTreeCell() {
		return String.format("Sleep for %s ms", this.duration);
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
            		SleepAST.this.duration = Long.valueOf(result.get());
            	}
            }
        });
        contextMenu.getItems().add(item1);
		return contextMenu;
	}

	@Override
	public void removeLinkFrom(AST value) {
	}

	@Override
	public void addLinkTo(AST value) {
	}

	@Override
	public boolean isDraggableTo(AST value) {
		return false;
	}

}
