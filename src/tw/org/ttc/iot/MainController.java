package tw.org.ttc.iot;

import io.github.swagger2markup.GroupBy;
import io.github.swagger2markup.Language;
import io.github.swagger2markup.Swagger2MarkupConfig;
import io.github.swagger2markup.Swagger2MarkupConverter;
import io.github.swagger2markup.builder.Swagger2MarkupConfigBuilder;
import io.github.swagger2markup.markup.builder.MarkupLanguage;
import io.swagger.models.parameters.Parameter;

import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import org.apache.commons.lang3.StringUtils;
import tw.org.ttc.iot.model.EndPoint;
import tw.org.ttc.iot.model.RecursiveTreeItem;
import tw.org.ttc.iot.model.Response;
import tw.org.ttc.iot.model.script.AST;
import tw.org.ttc.iot.model.script.LoopAST;
import tw.org.ttc.iot.model.script.RESTfulRequestAST;
import tw.org.ttc.iot.model.script.ScriptAST;
import tw.org.ttc.iot.model.script.SleepAST;
import tw.org.ttc.iot.utils.EndPointFactory;
import tw.org.ttc.iot.view.EndPointCell;
import tw.org.ttc.iot.view.ScriptTreeCell;

public class MainController {
	@FXML
	private ListView<EndPoint> endPointsView;
	@FXML
	private ListView<String> builtinFunctionsView;
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

	@FXML
	private TreeView<AST> scriptView;

	private ObservableList<EndPoint> endPoints;
	private ObservableList<Parameter> endPointDetailParameters;
	private ObservableList<Response> endPointDetailResponses;

	private TreeItem<AST> script;
	private ScriptAST rootAST;

	private HashMap<String, Callable<AST>> functionMap;

	public MainController() {
		endPoints = FXCollections.observableArrayList();
		endPointDetailParameters = FXCollections.observableArrayList();
		endPointDetailResponses = FXCollections.observableArrayList();
		rootAST = new ScriptAST();
		script = new RecursiveTreeItem<AST>(rootAST, AST::getChildNodes);

		functionMap = new HashMap<String, Callable<AST>>();
		functionMap.put("Loop", new Callable<AST>() {

			@Override
			public AST call() throws Exception {
				return new LoopAST();
			}
		});
		functionMap.put("Sleep", new Callable<AST>() {

			@Override
			public AST call() throws Exception {
				return new SleepAST();
			}
		});
	}

	@FXML
	private void initialize() {
		init_endpoint_list();
		init_endpoint_detail_parameter_view();
		init_endpoint_detail_response_view();
		scriptView.setRoot(script);
		script.setExpanded(true);
		scriptView.setCellFactory(new Callback<TreeView<AST>, TreeCell<AST>>() {

			@Override
			public TreeCell<AST> call(TreeView<AST> arg0) {
				return new ScriptTreeCell(arg0);
			}
		});

		builtinFunctionsView.setItems(FXCollections.observableArrayList(functionMap.keySet()));
		builtinFunctionsView.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() < 2) {
					return;
				}
				try {
					rootAST.addChildNode(
							functionMap.get(builtinFunctionsView.getSelectionModel().getSelectedItem()).call());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private void init_endpoint_detail_response_view() {
		endPointDetailResponseTableView.setItems(endPointDetailResponses);
		endPointDetailResponseCode.setCellValueFactory(new PropertyValueFactory<Response, String>("code"));
		endPointDetailResponseDescription
				.setCellValueFactory(new PropertyValueFactory<Response, String>("description"));
	}

	private void init_endpoint_detail_parameter_view() {
		endPointDetailParameterTableView.setItems(endPointDetailParameters);
		endPointDetailParameterName.setResizable(true);
		endPointDetailParameterName.setCellValueFactory(new PropertyValueFactory<Parameter, String>("name"));
		endPointDetailParameterDescription
				.setCellValueFactory(new PropertyValueFactory<Parameter, String>("description"));
		endPointDetailParameterIn.setCellValueFactory(new PropertyValueFactory<Parameter, String>("in"));
		endPointDetailParameterVendorExtension.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Parameter, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Parameter, String> param) {
						return new SimpleStringProperty(
								StringUtils.join(param.getValue().getVendorExtensions().keySet()));
					}
				});
	}

	private void init_endpoint_list() {
		endPointsView.setCellFactory(new Callback<ListView<EndPoint>, ListCell<EndPoint>>() {

			@Override
			public ListCell<EndPoint> call(ListView<EndPoint> arg0) {
				return new EndPointCell();
			}
		});
		endPointsView.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() < 2) {
					return;
				}
				rootAST.addChildNode(new RESTfulRequestAST(endPointsView.getSelectionModel().getSelectedItem()));
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

					arg2.getOperation().getParameters().forEach((p) -> {
						endPointDetailParameters.add(p);
					});
					arg2.getOperation().getResponses().entrySet().forEach((e) -> {
						endPointDetailResponses.add(new Response(e.getKey(), e.getValue()));
					});
				}
			}
		});
	}

	@FXML
	private void onOpenMenuClicked() {
		TextInputDialog dialog = new TextInputDialog("File Open");
		dialog.setTitle("Open a Swagger config file");
		dialog.setHeaderText(null);
		dialog.setContentText("Please enter the url of swagger config file:");

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();

		// The Java 8 way to get the response value (with lambda expression).
		result.ifPresent(url -> {
			try {
				Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
			            .withMarkupLanguage(MarkupLanguage.ASCIIDOC)
			            .withOutputLanguage(Language.ZH)
			            .withPathsGroupedBy(GroupBy.TAGS)
			            .withGeneratedExamples()
			            .withoutInlineSchema()
			            .build();
			    Swagger2MarkupConverter converter;
			    if (url.startsWith("http")) {
			    	converter = Swagger2MarkupConverter.from((new URL(url)).toURI())
				    		
				            .withConfig(config)
				            .build();
			    } else {
			    	converter = Swagger2MarkupConverter.from(Paths.get(url))
				    		
				            .withConfig(config)
				            .build();
			    }
			    
			    converter.toFile(Paths.get("build/swagger"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			List<EndPoint> data = EndPointFactory.getEndPoints(url);
			endPoints.clear();
			for (int i = 0; i < data.size(); i++) {
				endPoints.add(data.get(i));
			}
		});
	}
}
