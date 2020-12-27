import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
		Scene mainScene= new Scene(root);
		primaryStage.setScene(mainScene);
		primaryStage.setMaximized(true);
		//primaryStage.initStyle(StageStyle.UNDECORATED);
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		primaryStage.setX(primaryScreenBounds.getMinX());
		primaryStage.setY(primaryScreenBounds.getMinY());
		primaryStage.setWidth(primaryScreenBounds.getWidth());
		primaryStage.setHeight(primaryScreenBounds.getHeight());
		primaryStage.show();
		SplitPane form = (SplitPane) mainScene.lookup("#split-pane");
		form.lookupAll(".split-pane-divider").stream()
		.forEach(div -> div.setMouseTransparent(true));
		ResumeSQLHelper db = new ResumeSQLHelper();
		db.createTable();
		db.insertIntoTable("Software Intern", "Google", "ksldfllkdafjlkdsafjlkdajflkdsajflkdafjlkdajfladjflkdasfjlkadjfkajflkdajflkdajflkdajflkdajfl", "www.google.com");
	}
	public static void main(String[] args) {
		launch(args);
		
	}
	 
	

}
