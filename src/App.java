import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
		Scene mainScene= new Scene(root);
		primaryStage.setScene(mainScene);
		primaryStage.setMaximized(true);
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.show();
		SplitPane form = (SplitPane) mainScene.lookup("#split-pane");
		form.lookupAll(".split-pane-divider").stream()
		.forEach(div -> div.setMouseTransparent(true));
		
	}
	public static void main(String[] args) {
		launch(args);
	}
	
	

}
