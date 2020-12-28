import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.JFXTreeView;
import com.jfoenix.skins.JFXTableColumnHeader;
import com.jfoenix.skins.JFXTableHeaderRow;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {
	private Scene mainScene;
	private ResumeSQLHelper db = new ResumeSQLHelper();
	private TextField titleField;
	private TextField companyField;
	private TextArea descriptionField;
	private TextField linkField;
	private Button submitButton;

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
		mainScene = new Scene(root);
		primaryStage.setScene(mainScene);
		primaryStage.setMaximized(true);
		// primaryStage.initStyle(StageStyle.UNDECORATED);
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		primaryStage.setX(primaryScreenBounds.getMinX());
		primaryStage.setY(primaryScreenBounds.getMinY());
		primaryStage.setWidth(primaryScreenBounds.getWidth());
		primaryStage.setHeight(primaryScreenBounds.getHeight());
		primaryStage.show();
		initializePanes();
		db.createTable();
		initializeForm();
	}

	private void initializePanes() {
		SplitPane form = (SplitPane) mainScene.lookup("#split-pane");
		form.lookupAll(".split-pane-divider").stream().forEach(div -> div.setMouseTransparent(true));
		JFXTreeTableView treeTableView = (JFXTreeTableView) mainScene.lookup("#application-table");
		treeTableView.lookupAll(".column-header").stream().forEach(header -> header.setMouseTransparent(true));
		;
	}

	private void initializeForm() {
		titleField= (TextField) mainScene.lookup("#title-field");
		companyField = (TextField) mainScene.lookup("#company-field");
		descriptionField = (TextArea) mainScene.lookup("#description-field");
		linkField = (TextField) mainScene.lookup("#link-field");
		titleField.textProperty().addListener(new TextValidator());
		companyField.textProperty().addListener(new TextValidator());
		descriptionField.textProperty().addListener(new TextValidator());
		linkField.textProperty().addListener(new TextValidator());
		submitButton = (Button) mainScene.lookup("#submit-button");
		submitButton.setOnAction(new EventHandler<ActionEvent>(){

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
				db.insertIntoTable(title, company, description, link);
			}
			
		});
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
	
	public static void main(String[] args) {
		launch(args);
		
	}
	 
	

}
