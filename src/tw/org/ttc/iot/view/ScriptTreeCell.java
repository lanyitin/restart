package tw.org.ttc.iot.view;

import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import tw.org.ttc.iot.model.script.AST;
import tw.org.ttc.iot.model.script.LoopAST;
import tw.org.ttc.iot.model.script.RESTfulRequestAST;
import tw.org.ttc.iot.model.script.SleepAST;

public class ScriptTreeCell extends TreeCell<AST> {

	public ScriptTreeCell() {
		super();
		setOnDragDetected(e -> {
			System.out.println(String.format("Drag Detected %d", getIndex()));
			Dragboard db = startDragAndDrop(TransferMode.MOVE);
			ClipboardContent content = new ClipboardContent();
			content.put(DataFormat.PLAIN_TEXT, getItem().getTextForTreeCell());
			db.setContent(content);
			e.consume();
		});
		setOnDragDone(e -> {
			System.out.println(String.format("Drag Done %d", getIndex()));
			e.consume();
		});
		setOnDragDropped(e -> {
			System.out.println(String.format("Drag Dropped %d", getIndex()));
			e.setDropCompleted(true);
			e.consume();
		});
		setOnDragEntered(e -> {
			setText(e.getDragboard().getString());
			System.out.println(String.format("Drag Entered %d", getIndex()));
			e.consume();
		});
		setOnDragExited(e -> {
			setText(getItem().getTextForTreeCell());
			System.out.println(String.format("Drag Exit %d", getIndex()));
			e.consume();
		});
		setOnDragOver(e -> {
			e.acceptTransferModes(TransferMode.ANY);
			System.out.println(String.format("Drag Over %d", getIndex()));
			e.consume();
		});
	}

	@Override
	public void updateItem(AST item, boolean empty) {
		super.updateItem(item, empty);

		if (empty) {
			setText(null);
			setGraphic(null);
			setContextMenu(null);
		} else {
			setText(item.getTextForTreeCell());
			if (item instanceof LoopAST || item instanceof SleepAST || item instanceof RESTfulRequestAST) {
				ContextMenu contextMenu = new ContextMenu();
				MenuItem item1 = new MenuItem("Delete");
				item1.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						ScriptTreeCell.this.getTreeItem().getParent().getValue().getChildNodes()
								.remove(ScriptTreeCell.this.getTreeItem().getValue());
					}
				});
				contextMenu.getItems().add(item1);
				if (item instanceof LoopAST) {
					LoopAST ast_item = (LoopAST) item;
					MenuItem item2 = new MenuItem("Edit");
					item2.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							TextInputDialog dialog = new TextInputDialog(String.valueOf(ast_item.getCount()));
							dialog.setTitle("How many iteration would you like?");
							dialog.setContentText("The number of iterations:");

							// Traditional way to get the response value.
							Optional<String> result = dialog.showAndWait();
							if (result.isPresent()) {
								ast_item.setCount(Integer.valueOf(result.get()));
								setText(item.getTextForTreeCell());
							}
						}
					});
					contextMenu.getItems().add(item2);
				} else {
					if (item instanceof SleepAST) {
						SleepAST ast_item = (SleepAST) item;
						MenuItem item2 = new MenuItem("Edit");
						item2.setOnAction(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent event) {
								TextInputDialog dialog = new TextInputDialog(String.valueOf(ast_item.getDuration()));
								dialog.setTitle("How long would you like?");
								dialog.setContentText("The duration(ms):");

								// Traditional way to get the response value.
								Optional<String> result = dialog.showAndWait();
								if (result.isPresent()) {
									ast_item.setDuration(Integer.valueOf(result.get()));
									setText(item.getTextForTreeCell());
								}
							}
						});
						contextMenu.getItems().add(item2);
					}
				}
				setContextMenu(contextMenu);
				setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
					@Override
					public void handle(ContextMenuEvent event) {
						ScriptTreeCell.this.getContextMenu().show(ScriptTreeCell.this, event.getScreenX(),
								event.getScreenY());
					}
				});
			}
		}
	}
}
