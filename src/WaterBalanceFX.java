

import java.util.Scanner;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WaterBalanceFX extends Application{
    
    public static WeatherStation ws;
    public static Field field;
    public static FormulaManager forManager;
    public static FileManager fileManager;
    public static Scanner scanner;
       
    
    public static void main(String[] args) {
        
        ws = new WeatherStation();
        field = new Field();
        forManager = new FormulaManager(ws, field);
        scanner = new Scanner(System.in);
        WaterBalanceFX.fileManager = new FileManager();
        
        //Test dei metodi e delle tempistiche
        long start = System.currentTimeMillis();
        
        //Guida per l'utilizzo del programma:
        //1. fare il setup del file del campo, stazione meteo, costanti e calcoli
        //2. registrare e scrivere i dati del campo una sola volta
        //3. registrare i dati della stazione meteo, una volta per giorno
        //4. avviare la funzione per il calcolo delle costanti e inserirle su file
        //5. calcolare ete
        //6. inserire i calcoli fatti per ete in calculations
        //7. richiedere i valori della ws e ripartire dal punto 5
        
        
        //Stazione meteo
        //fileManager.stationFileSetup();
        //fileManager.stationDatasRegistration();
        //fileManager.stationDatasReport();
        
        //Campo
        //fileManager.fieldFileSetup();
        //fileManager.fieldDatasRegistration();
        //fileManager.fieldDatasReport();
        
        //Costanti del suolo
        //fileManager.constantFileSetup();
        //Chiamata funzione costanti per registrarle e salvarle su file
        //forManager.constant();
        //fileManager.constantDatasReport();
        
        //forManager.ete();
        
        //Risultati delle formule
        //fileManager.calculationFileSetup();
        //fileManager.calculationsReport();

        launch(WaterBalanceFX.class);
                
        long end = System.currentTimeMillis();
        
        System.out.println("\nTime: " + (end-start) + " milliseconds");
    }

    @Override
    public void start(Stage window) throws Exception {
        
        FlowPane layout = new FlowPane();
        
        
        VBox box = new VBox();
        box.setSpacing(5);
        box.setAlignment(Pos.CENTER);
        
        Button setupButton = new Button("SETUP");
        setupButton.setOnAction((event) -> {
            fileManager.stationFileSetup();
            fileManager.fieldFileSetup();
            fileManager.constantFileSetup();
            fileManager.calculationFileSetup();
        });
        
        Button registerStation = new Button("REGISTER STATION");
        registerStation.setOnAction((event) -> {
            fileManager.stationDatasRegistration();            
        });
        
        Button registerField = new Button("REGISTER FIELD");
        registerField.setOnAction((event) -> {
            fileManager.fieldDatasRegistration();      
        });  
        
        Button registerConstant = new Button("REGISTER CONSTANT");
        registerConstant.setOnAction((event) -> {
            forManager.constant();      
        });  
        
        Button eteButton = new Button("ETE");
        eteButton.setOnAction((event) -> {
            forManager.ete();
        });
        
        Button reportButton = new Button("REPORT");
        reportButton.setOnAction((event) -> {
            fileManager.stationDatasReport();
            fileManager.fieldDatasReport();
            fileManager.constantDatasReport();
            fileManager.calculationsReport();
        });
        
        Label text = new Label("Da eseguire in ordine:");
        
        box.getChildren().addAll(text,setupButton,registerStation,registerField,registerConstant,eteButton,reportButton);
        
        layout.getChildren().add(box);
        
        Scene scene = new Scene(layout);
        
        window.setScene(scene);
        window.setTitle("Bilancio idrico");
        window.show();
        
    }
}
