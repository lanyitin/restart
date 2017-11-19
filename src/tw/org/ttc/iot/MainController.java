package tw.org.ttc.iot;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import io.swagger.models.parameters.Parameter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import tw.org.ttc.iot.model.EndPoint;
import tw.org.ttc.iot.model.Response;
import tw.org.ttc.iot.view.EndPointCell;

public class MainController {
	@FXML
	private ListView<EndPoint> endPointsView;
	@FXML
	private Label endPointDetailMethodLabel;
	@FXML
	private Label endPointDetailUrlLabel;
	@FXML
	private Label endPointDetailSummaryLabel;
	@FXML
	private TableView<Parameter> endPointDetailParameterTableView;
	@FXML
	private TableColumn<Parameter, String> endPointDetailParameterName;
	@FXML
	private TableColumn<Parameter, String> endPointDetailParameterDescription;
	@FXML
	private TableColumn<Parameter, String> endPointDetailParameterIn;
	@FXML
	private TableColumn<Parameter, String> endPointDetailParameterVendorExtension;
	
	@FXML
	private TableView<Response> endPointDetailResponseTableView;
	@FXML
	private TableColumn<Response, String> endPointDetailResponseCode;
	@FXML
	private TableColumn<Response, String> endPointDetailResponseDescription;
	
	private ObservableList<EndPoint> endPoints;
	private ObservableList<Parameter> endPointDetailParameters;
	private ObservableList<Response> endPointDetailResponses;

	public MainController() {
		endPoints = FXCollections.observableArrayList();
		endPointDetailParameters = FXCollections.observableArrayList();
		endPointDetailResponses = FXCollections.observableArrayList();
	}

	@FXML
	private void initialize() {
		init_endpoint_list();
		init_endpoint_detail_parameter_view();
		init_endpoint_detail_response_view();
	}

	private void init_endpoint_detail_response_view() {
		endPointDetailResponseTableView.setItems(endPointDetailResponses);
		endPointDetailResponseCode.setCellValueFactory(new PropertyValueFactory<Response, String>("code"));
		endPointDetailResponseDescription.setCellValueFactory(new PropertyValueFactory<Response, String>("description"));
	}

	private void init_endpoint_detail_parameter_view() {
		endPointDetailParameterTableView.setItems(endPointDetailParameters);
		endPointDetailParameterName.setResizable(true);
		endPointDetailParameterName.setCellValueFactory(new PropertyValueFactory<Parameter, String>("name"));
		endPointDetailParameterDescription.setCellValueFactory(new PropertyValueFactory<Parameter, String>("description"));
		endPointDetailParameterIn.setCellValueFactory(new PropertyValueFactory<Parameter, String>("in"));
		endPointDetailParameterVendorExtension.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Parameter,String>,ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Parameter, String> param) {
				return new SimpleStringProperty(StringUtils.join(param.getValue().getVendorExtensions().keySet()));
			}});
	}

	private void init_endpoint_list() {
		endPointsView.setCellFactory(new Callback<ListView<EndPoint>, ListCell<EndPoint>>() {

			@Override
			public ListCell<EndPoint> call(ListView<EndPoint> arg0) {
				return new EndPointCell();
			}
		});
		
		endPointsView.setItems(endPoints);		
		endPointsView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<EndPoint>() {

			@Override
			public void changed(ObservableValue<? extends EndPoint> arg0, EndPoint arg1, EndPoint arg2) {
				if (arg2 != null) {
					endPointDetailMethodLabel.setText(arg2.getMethod());
					endPointDetailUrlLabel.setText(arg2.getUrl());
					endPointDetailSummaryLabel.setText(arg2.getOperation().getSummary());
					endPointDetailParameters.clear();
					endPointDetailResponses.clear();

					arg2.getOperation().getParameters().forEach((p) -> {endPointDetailParameters.add(p);});
					arg2.getOperation().getResponses().entrySet().forEach((e) -> {endPointDetailResponses.add(new Response(e.getKey(), e.getValue()));});
				}
			}});
	}

	@FXML
	private void onOpenMenuClicked() {
		List<EndPoint> data = EndPointFactory.getEndPoints("http://petstore.swagger.io/v2/swagger.json");
		endPoints.clear();
		for (int i = 0; i < data.size(); i++) {
			endPoints.add(data.get(i));
		}
	}	
}
