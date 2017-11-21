package tw.org.ttc.iot.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import tw.org.ttc.iot.model.script.AST;

public class ScriptTreeCell extends TreeCell<AST> {
	private enum WorkDropType {
		DROP_INTO, REORDER
	}

	/**
	 * Using a static here, it's just too convenient.
	 */
	private static TreeItem<AST> draggedTreeItem;

	private static WorkDropType workDropType;

	public ScriptTreeCell() {
		super();
		setOnDragOver(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				if (isDraggableToParent() && isNotAlreadyChildOfTarget(ScriptTreeCell.this.getTreeItem())
						&& draggedTreeItem.getParent() != getTreeItem()) {
					Point2D sceneCoordinates = ScriptTreeCell.this.localToScene(0d, 0d);

					double height = ScriptTreeCell.this.getHeight();

					// get the y coordinate within the control
					double y = event.getSceneY() - (sceneCoordinates.getY());

					// if the drop is three quarters of the way down the control
					// then the drop will be a sibling and not into the tree item

					// set the dnd effect for the required action
					if (y > (height * .75d)) {
						setEffect(null);

						getStyleClass().add("dnd-below");

						workDropType = WorkDropType.REORDER;
					} else {
						getStyleClass().remove("dnd-below");

						InnerShadow shadow;

						shadow = new InnerShadow();
						shadow.setOffsetX(1.0);
						shadow.setColor(Color.web("#666666"));
						shadow.setOffsetY(1.0);
						setEffect(shadow);

						workDropType = WorkDropType.DROP_INTO;
					}

					event.acceptTransferModes(TransferMode.MOVE);
				}
			}
		});
		setOnDragDetected(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ClipboardContent content;

				content = new ClipboardContent();
				content.putString("TROLOLOL");

				Dragboard dragboard;

				dragboard = getTreeView().startDragAndDrop(TransferMode.MOVE);
				dragboard.setContent(content);

				draggedTreeItem = getTreeItem();

				event.consume();
			}
		});
		setOnDragDropped(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				boolean dropOK = false;

				if (draggedTreeItem != null) {

					TreeItem<AST> draggedItemParent = draggedTreeItem.getParent();

					AST draggedWork = draggedTreeItem.getValue();

					if (workDropType == WorkDropType.DROP_INTO) {

						if (isDraggableToParent() && isNotAlreadyChildOfTarget(ScriptTreeCell.this.getTreeItem())
								&& draggedTreeItem.getParent() != getTreeItem()) {
							draggedWork.removeLinkFrom(draggedItemParent.getValue());
							draggedWork.addLinkTo(getTreeItem().getValue());

							draggedItemParent.getValue().getChildNodes().remove(draggedWork);

							getTreeItem().getValue().getChildNodes().add(draggedWork);

							getTreeItem().setExpanded(true);

							// clickListeners.leftClickListener.get().itemSelected(draggedWork);
						}
					} else if (workDropType == WorkDropType.REORDER) {

					}

					dropOK = true;

					draggedTreeItem = null;
				}

				event.setDropCompleted(dropOK);
				event.consume();
			}
		});
		setOnDragExited(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				// remove all dnd effects
				setEffect(null);
				getStyleClass().remove("dnd-below");
			}
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
			ContextMenu contextMenu = item.getContextMenu();
			if (contextMenu != null) {
				MenuItem item1 = new MenuItem("Delete");
				item1.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						ScriptTreeCell.this.getTreeItem().getParent().getChildren()
								.remove(ScriptTreeCell.this.getTreeItem());
					}
				});
				contextMenu.getItems().add(item1);
				setContextMenu(contextMenu);
				setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
					@Override
					public void handle(ContextMenuEvent event) {
						ScriptTreeCell.this.getContextMenu().show(ScriptTreeCell.this, event.getScreenX(),
								event.getScreenY());
					}
				});
			} else {
				setContextMenu(null);
				setOnContextMenuRequested(null);
			}
		}
	}

	protected boolean isDraggableToParent() {
		return draggedTreeItem.getValue().isDraggableTo(getTreeItem().getValue());
	}

	protected boolean isNotAlreadyChildOfTarget(TreeItem<AST> treeItemParent) {
		if (draggedTreeItem == treeItemParent)
			return false;

		if (treeItemParent.getParent() != null)
			return isNotAlreadyChildOfTarget(treeItemParent.getParent());
		else
			return true;
	}

}
