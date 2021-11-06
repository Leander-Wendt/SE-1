package application;

import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;


public class Application extends javafx.application.Application {

	@Override
	public void start( Stage stage ) {
		try {
			stage.setTitle( "Hello JavaFX" );
			VBox root = new VBox();
			Scene scene = new Scene( root, 400, 400 );
			scene.getStylesheets().add(getClass().getResource("application.css")
				.toExternalForm());
			stage.setScene( scene );
			Button btn = new Button( "Hello World!" );
			btn.setOnAction( e -> System.out.println( "Hello World!" ) );
			root.getChildren().add( btn );
			Button ext = new Button( "Exit" );
			ext.setOnAction( e -> Platform.exit() );
			root.getChildren().add( ext );
			stage.show();

		} catch( Exception e ) {
			e.printStackTrace();
		}
	}

	public static void main( String[] args ) {
		launch( args );
	}
}

