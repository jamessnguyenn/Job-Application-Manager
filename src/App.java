import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.JFXTreeView;
import com.jfoenix.skins.JFXTableColumnHeader;
import com.jfoenix.skins.JFXTableHeaderRow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {
	private Scene mainScene;

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
		ResumeSQLHelper db = new ResumeSQLHelper();
		db.createTable();
		db.insertIntoTable("Software Intern", "Google", "ksldfllkdafjlkdsafjlkdajflkdsajflkdafjlkdajfladjflkdasfjlkadjfkajflkdajflkdajflkdajflkdajfl", "www.google.com");
	}
	private void initializePanes(){
		SplitPane form = (SplitPane) mainScene.lookup("#split-pane");
		form.lookupAll(".split-pane-divider").stream().forEach(div -> div.setMouseTransparent(true));
		JFXTreeTableView treeTableView = (JFXTreeTableView) mainScene.lookup("#application-table");
		 treeTableView.lookupAll(".column-header").stream().forEach(header->header.setMouseTransparent(true));;
	}

	public static void main(String[] args) {
		launch(args);
		
	}
	 
	

}
