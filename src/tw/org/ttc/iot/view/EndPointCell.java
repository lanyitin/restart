package tw.org.ttc.iot.view;

import javafx.scene.control.ListCell;
import tw.org.ttc.iot.model.EndPoint;

public class EndPointCell extends ListCell<EndPoint> {

	@Override
	protected void updateItem(EndPoint item, boolean empty) {
		// TODO Auto-generated method stub
		super.updateItem(item, empty);
		if (empty || item == null) {

		} else {
			this.setText(String.format("%s %s", item.getMethod(), item.getUrl()));
		}
	}

}
