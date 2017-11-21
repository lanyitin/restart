package tw.org.ttc.iot.model.script;

import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;

public interface AST {
	public ObservableList<AST> getChildNodes();

	public String getTextForTreeCell();

	public ContextMenu getContextMenu();

	public void removeLinkFrom(AST value);

	public void addLinkTo(AST value);

	public boolean isDraggableTo(AST value);
}
