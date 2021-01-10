import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Predicate;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.JFXTreeView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.skins.JFXTableColumnHeader;
import com.jfoenix.skins.JFXTableHeaderRow;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableColumn.CellEditEvent;
import javafx.scene.control.cell.ComboBoxTreeTableCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.Shadow;
import javafx.scene.effect.Light.Distant;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public class App extends Application {
	private Scene mainScene;
	private ResumeSQLHelper db = new ResumeSQLHelper();
	private TextField titleField;
	private TextField companyField;
	private TextArea descriptionField;
	private TextField linkField;
	private TextField searchField;
	private Button submitButton;
	private JFXTreeTableView<Data> treeTableView;
	private TreeTableColumn<Data, String> jobTitle, company, status, dateApplied;
	private ObservableList<Data> data;
	private ComboBox statusBox;
	private SplitPane form;
	private static Stage mainStage;
	private TabPane tabPane;

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
		mainScene = new Scene(root);
		mainStage = primaryStage;
		primaryStage.setScene(mainScene);
		primaryStage.setMaximized(true);
		// primaryStage.initStyle(StageStyle.UNDECORATED);
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		primaryStage.setX(primaryScreenBounds.getMinX());
		primaryStage.setY(primaryScreenBounds.getMinY());
		primaryStage.setWidth(primaryScreenBounds.getWidth());
		primaryStage.setHeight(primaryScreenBounds.getHeight());
		mainScene.getRoot().applyCss();
		initializePanes();
		initializeForm();
		primaryStage.show();
		intializeTableElements();
		initializeTreeTable();
		intializeTableForm();
	}

	private void initializePanes() {
		tabPane = (TabPane) mainScene.lookup("#tab-pane");
		form = (SplitPane) mainScene.lookup("#split-pane");
		// form.lookupAll(".split-pane-divider").stream().forEach(div ->
		// div.setMouseTransparent(true));
	}

	private void initializeForm() {
		searchField = (TextField) mainScene.lookup("#search-field");
		statusBox = (ComboBox) mainScene.lookup("#status-choice");
		titleField = (TextField) mainScene.lookup("#title-field");
		companyField = (TextField) mainScene.lookup("#company-field");
		descriptionField = (TextArea) mainScene.lookup("#description-field");
		linkField = (TextField) mainScene.lookup("#link-field");
		titleField.textProperty().addListener(new TextValidator());
		companyField.textProperty().addListener(new TextValidator());
		descriptionField.textProperty().addListener(new TextValidator());
		linkField.textProperty().addListener(new TextValidator());
		submitButton = (Button) mainScene.lookup("#submit-button");
		submitButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String title = titleField.getText();
				String company = companyField.getText();
				String description = descriptionField.getText();
				String link = linkField.getText();
				titleField.clear();
				companyField.clear();
				descriptionField.clear();
				linkField.clear();
				searchField.clear();
				statusBox.getSelectionModel().selectFirst();
				db.insertIntoTable(title, company, description, link);
				reloadData();
			}

		});

	}

	private void intializeTableForm() {
		statusBox.getItems().add("Any");
		statusBox.getItems().add("Pending");
		statusBox.getItems().add("Interview");
		statusBox.getItems().add("Accepted");
		statusBox.getItems().add("Not Considered");
		statusBox.getSelectionModel().selectFirst();
		searchField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				treeTableView.setPredicate(new Predicate<TreeItem<Data>>() {

					@Override
					public boolean test(TreeItem<Data> data) {
						if (statusBox.getValue().equals("Any")) {
							return data.getValue().getCompany().toLowerCase().contains(newValue.toLowerCase())
									|| data.getValue().getJobTitle().toLowerCase().contains(newValue.toLowerCase());
						} else {
							return data.getValue().getStatus().equals(statusBox.getValue()) && (data.getValue()
									.getCompany().toLowerCase().contains(newValue.toLowerCase())
									|| data.getValue().getJobTitle().toLowerCase().contains(newValue.toLowerCase()));
						}
					}

				});

			}

		});
		statusBox.getSelectionModel().selectedItemProperty().addListener((options, oldValues, newValue) -> {
			treeTableView.setPredicate(new Predicate<TreeItem<Data>>() {

				@Override
				public boolean test(TreeItem<Data> data) {
					if (newValue.equals("Any")) {
						return data.getValue().getCompany().toLowerCase().contains(searchField.getText().toLowerCase())
								|| data.getValue().getJobTitle().toLowerCase()
										.contains(searchField.getText().toLowerCase());
					} else {
						return data.getValue().getStatus().equals(newValue) && (data.getValue().getCompany()
								.toLowerCase().contains(searchField.getText().toLowerCase())
								|| data.getValue().getJobTitle().toLowerCase()
										.contains(searchField.getText().toLowerCase()));
					}
				}
			});
		});

	}

	private void intializeTableElements() {
		treeTableView = (JFXTreeTableView) mainScene.lookup("#application-table");
		jobTitle = (TreeTableColumn<Data, String>) treeTableView.getColumns().get(0);
		company = (TreeTableColumn<Data, String>) treeTableView.getColumns().get(1);
		status = (TreeTableColumn<Data, String>) treeTableView.getColumns().get(2);
		dateApplied = (TreeTableColumn<Data, String>) treeTableView.getColumns().get(3);
		jobTitle.setResizable(false);
		company.setResizable(false);
		status.setResizable(false);
		dateApplied.setResizable(false);
	}

	private void initializeTreeTable() {
		treeTableView.lookupAll(".column-header").stream().forEach(header -> header.setMouseTransparent(true));

		jobTitle.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Data, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Data, String> param) {
						return param.getValue().getValue().jobTitle;
					}

				});
		company.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Data, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Data, String> param) {
						return param.getValue().getValue().company;
					}

				});
		status.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Data, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Data, String> param) {
						return param.getValue().getValue().status;
					}

				});
		dateApplied.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Data, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Data, String> param) {
						return param.getValue().getValue().date;
					}

				});
		ObservableList<String> statusList = FXCollections.observableArrayList();
		statusList.add("Pending");
		statusList.add("Interview");
		statusList.add("Accepted");
		statusList.add("Not Considered");
		status.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(statusList));
		data = FXCollections.observableArrayList();
		status.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<Data, String>>() {

			@Override
			public void handle(CellEditEvent<Data, String> event) {
				TreeItem<Data> currentEditedData = treeTableView.getTreeItem(event.getTreeTablePosition().getRow());
				currentEditedData.getValue().setStatus(event.getNewValue());
				db.updateTable(currentEditedData.getValue().getFullDate(), event.getNewValue());
			}

		});
		jobTitle.setCellFactory(new Callback<TreeTableColumn<Data, String>, TreeTableCell<Data, String>>() {

			@Override
			public TreeTableCell<Data, String> call(TreeTableColumn<Data, String> col) {
				final TreeTableCell<Data, String> cell = new TreeTableCell<Data, String>() {
					@Override
					public void updateItem(String jobTitle, boolean empty) {
						super.updateItem(jobTitle, empty);
						if (empty) {
							setText(null);
						} else {
							setText(jobTitle);
						}
					}
				};
				cell.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent event) {
						if (event.getClickCount() >= 1) {
							System.out.println("double click on" + cell.getTreeTableRow().getItem().getFullDate());
							try {
								Distant light = new Distant();
								light.setColor(Color.web("#4e75a4"));
								Lighting effect = new Lighting(light);
								form.setEffect(effect);
								
								Stage rowView = new Stage();
								rowView.initOwner(mainStage);
								Parent root = FXMLLoader.load(getClass().getResource("applications.fxml"));
								Scene rowScene = new Scene(root);
								rowScene.getRoot().applyCss();
								initializeRowScene(rowScene, cell.getTreeTableRow().getItem());
								rowView.setScene(rowScene);
								rowView.initStyle(StageStyle.UNDECORATED);
								rowView.setWidth(mainStage.getWidth()*0.65);
								rowView.setHeight(mainStage.getHeight()*0.8);
								rowView.focusedProperty().addListener((obs, wasFocused, isFocused)->{
									if(!isFocused){
										rowView.close();
										form.setEffect(null);
									}
								});
								rowView.showAndWait();
							} catch (IOException e) {
								System.out.println(e.getMessage());
							}
						}

					}
					
				});
				return cell;
			}
			
		});
		reloadData();
		treeTableView.getSelectionModel().setCellSelectionEnabled(true);
		TreeItem<Data> root = new RecursiveTreeItem<Data>(data, RecursiveTreeObject::getChildren);
		treeTableView.setShowRoot(false);
		treeTableView.setEditable(true);
		treeTableView.setRoot(root);

	}

	private void reloadData() {
		data.clear();
		ResultSet rs = db.getData();
		
		if (rs == null) {
			return;
		}
		try {
			while (rs.next()) {
				data.add(new Data(rs.getString("job_title"), rs.getString("company"), rs.getString("link"), rs.getString("description"), rs.getString("status"), rs.getString("date")));	
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	private class TextValidator implements javafx.beans.value.ChangeListener<String>{

		@Override
		public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
			if(titleField.getText().trim().isEmpty() || companyField.getText().trim().isEmpty() || descriptionField.getText().trim().isEmpty() || linkField.getText().trim().isEmpty()){
				submitButton.setDisable(true);
			}else{
				submitButton.setDisable(false);
			}

		}

	}
	
	private void initializeRowScene(Scene scene, Data data){
		Button cancelButton = (Button) scene.lookup("#cancel-button");
		Text jobTitle = (Text) scene.lookup("#job-title");
		Text companyTitle = (Text) scene.lookup("#company-title");
		Text jobDescription = (Text)scene.lookup("#job-description");
		Text appliedDate = (Text) scene.lookup("#applied-date");
		jobTitle.setText(data.getJobTitle());
		companyTitle.setText(data.getCompany());
		jobDescription.setText(data.getDescription());
		appliedDate.setText(data.getFullDate());
		cancelButton.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				Stage initializedStage = (Stage)scene.getWindow();
				initializedStage.close();
				form.setEffect(null);
			}
			
		});
	}
	public static void main(String[] args) {
		launch(args);
		
	}
	 
	

}
