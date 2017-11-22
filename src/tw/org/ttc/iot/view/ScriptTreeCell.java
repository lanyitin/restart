package tw.org.ttc.iot.view;

import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

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
				} else if (item instanceof SleepAST) {
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
				} else if (item instanceof RESTfulRequestAST) {
						RESTfulRequestAST ast_item = (RESTfulRequestAST) item;
						MenuItem item2 = new MenuItem("Edit");
						item2.setOnAction(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent event) {
								String content = "";
								if (ast_item.getRequest().getParameters() != null) {
									content = ast_item.getRequest().getParameters().toString();
								}
								TextInputDialog dialog = new TextInputDialog(content);
								dialog.setTitle("the parameters:");
								dialog.setResizable(true);

								// Traditional way to get the response value.
								Optional<String> result = dialog.showAndWait();
								if (result.isPresent()) {
									JsonParser gson = new JsonParser();
									ast_item.getRequest().setParameters(gson.parse(result.get()).getAsJsonObject());
									setText(item.getTextForTreeCell());
								}
							}
						});
						contextMenu.getItems().add(item2);
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
