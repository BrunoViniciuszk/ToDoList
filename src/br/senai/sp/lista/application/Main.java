package br.senai.sp.lista.application;
	
import br.senai.sp.lista.io.TarefaIO;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			TarefaIO.createFiles();
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("../view/Index.fxml"));
			Scene scene = new Scene(root,1064,600);
			scene.getStylesheets().add(getClass().getResource("../view/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Lista de afazeres");
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("../imagens/icone-agenda.png")));
			primaryStage.show();
			primaryStage.setResizable(false);
		} catch(Exception e) {
			e.printStackTrace();
			
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
