package game2048;

import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.RadioMenuItemBuilder;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


/**
 * @author Giacomo Forresu
 */
public class Game2048 extends Application {

    private GameManager gameManager;
    private Bounds gameBounds;
    private int isAuto = 0;        
       
    
    @Override
    public void start(Stage primaryStage) {
        gameManager = new GameManager();
        gameBounds = gameManager.getLayoutBounds();

        StackPane root = new StackPane(gameManager);
        root.setPrefSize(gameBounds.getWidth(), gameBounds.getHeight());
        ChangeListener<Number> resize = (ov, v, v1) -> {
            gameManager.setLayoutX((root.getWidth() - gameBounds.getWidth()) / 2d);
            gameManager.setLayoutY((root.getHeight() - gameBounds.getHeight()) / 2d);
        };
        root.widthProperty().addListener(resize);
        root.heightProperty().addListener(resize);

        Scene scene = new Scene(root, 600, 720);
        MenuBar menuBar = new MenuBar();

        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());

        menuBar.getStyleClass().add("tendina");
        
       Menu menu = new Menu("Gioco");
       
       ToggleGroup toggle = new ToggleGroup();
                     
        
         RadioMenuItem automaticPlayer = RadioMenuItemBuilder.create()
        .toggleGroup(toggle)
        .text("Automatico")
        .selected(false)
        .build();
        
        
        menu.getItems().add(automaticPlayer);
        
        
        
        EventHandler automatico = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
                //Il giocatore automatico era gia` stato selezionato
                //viene quindi disattivato
                if( isAuto == 1 ){
                    
                    automaticPlayer.setSelected(false);
                    isAuto = 0;
                    
                    
                    
                }
                    
                //Giocatore automatico attivato
                else {
                   automaticPlayer.setSelected(true);
                   isAuto = 1;
                }
                
            }
            
        };
              
        menuBar.getMenus().add(menu);

        HBox tendina = new HBox();
        tendina.getChildren().add(menuBar);

        root.getChildren().add(tendina);
       
        scene.getStylesheets().add("game2048/game.css");
              
        addKeyHandler(scene);
        
        menu.getItems().get(0).addEventHandler(ActionEvent.ANY, automatico);
                      
        addSwipeHandlers(scene);

        if (isARMDevice()) {
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitHint("");
        }

        if (Platform.isSupported(ConditionalFeature.INPUT_TOUCH)) {
            scene.setCursor(Cursor.NONE);
        }

        primaryStage.setTitle("2048FX");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(gameBounds.getWidth());
        primaryStage.setMinHeight(gameBounds.getHeight());
        primaryStage.show();
    }

    private boolean isARMDevice() {
        return System.getProperty("os.arch").toUpperCase().contains("ARM");
    }
    
   
    private void addKeyHandler(Scene scene) {
                  
            scene.setOnKeyPressed(ke -> {
                
                if( isAuto == 0 ){
                
                        KeyCode keyCode = ke.getCode();
                        if (keyCode.equals(KeyCode.S)) {
                            gameManager.saveSession();
                            return;
                        }
                        if (keyCode.equals(KeyCode.R)) {
                            gameManager.restoreSession();
                            return;
                        }
                        if (keyCode.isArrowKey() == false) {
                            return;
                        }
                       Direction direction = Direction.valueFor(keyCode);
                       gameManager.move(direction);

                }

            });

        
        
  
    }

    private void addSwipeHandlers(Scene scene) {
        scene.setOnSwipeUp(e -> gameManager.move(Direction.UP));
        scene.setOnSwipeRight(e -> gameManager.move(Direction.RIGHT));
        scene.setOnSwipeLeft(e -> gameManager.move(Direction.LEFT));
        scene.setOnSwipeDown(e -> gameManager.move(Direction.DOWN));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
