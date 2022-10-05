
import java.util.Scanner;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
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
        fileManager.stationFileSetup();
        fileManager.stationDatasRegistration();
        //fileManager.stationDatasReport();
        
        //Campo
        fileManager.fieldFileSetup();
        fileManager.fieldDatasRegistration();
        //fileManager.fieldDatasReport();
        
        //Costanti del suolo
        fileManager.constantFileSetup();
        //Chiamata funzione costanti per registrarle e salvarle su file
        forManager.constant();
        //fileManager.constantDatasReport();
        
        forManager.ete();
        
        //Risultati delle formule
        fileManager.calculationFileSetup();
        //fileManager.calculationsReport();

        launch(WaterBalanceFX.class);
                
        long end = System.currentTimeMillis();
        
        System.out.println("\nTime: " + (end-start) + " milliseconds");
    }

    @Override
    public void start(Stage window) throws Exception {
        //Creo una schermata con titolo Bilancio Idrico
        //Inserisco una colonna con titolo "setup" e 4 bottoni per il setup
        //Inserisco una colonna con titolo "registration" e 3 bottoni per la registrazione
        //Inserisco una colonna con titolo "report" e 4 bottoni per il report
        //Inserisco un bottone per il calcolo dell'Ete
        
        FlowPane layout = new FlowPane();
        
        //Colonna del setup
        VBox setup = new VBox();
        setup.setSpacing(5);
        setup.setPadding(new Insets(10,10,10,10));
        setup.setAlignment(Pos.CENTER);
        
        Button button = new Button("setup station");
        Button button1 = new Button("setup field");
        Button button2 = new Button("setup constant");
        Button button3 = new Button("setup calculation");
        
        setup.getChildren().add(new Label("Setup:"));
        setup.getChildren().add(button);
        setup.getChildren().add(button1);
        setup.getChildren().add(button2);
        setup.getChildren().add(button3);
        
        //Colonna del register
        VBox register = new VBox();
        register.setSpacing(5);
        register.setPadding(new Insets(10,10,10,10));
        register.setAlignment(Pos.CENTER);
        
        Button button4 = new Button("register station");
        Button button5 = new Button("register field");
        Button button6 = new Button("register constant");
        //Button button7 = new Button("register calculation");
        
        register.getChildren().add(new Label("Register:"));
        register.getChildren().add(button4);
        register.getChildren().add(button5);
        register.getChildren().add(button6);
        //register.getChildren().add(button7);
        
        //Colonna del report
        VBox report = new VBox();
        report.setSpacing(5);
        report.setPadding(new Insets(10,10,10,10));
        report.setAlignment(Pos.CENTER);
        
        Button button8 = new Button("report station");
        Button button9 = new Button("report field");
        Button button10 = new Button("report constant");
        Button button11 = new Button("report calculation");
        
        report.getChildren().add(new Label("Report:"));
        report.getChildren().add(button8);
        report.getChildren().add(button9);
        report.getChildren().add(button10);
        report.getChildren().add(button11);
        
        //Colonna dell'Ete
        VBox ete = new VBox();
        ete.setSpacing(5);
        ete.setPadding(new Insets(10,10,10,10));
        ete.setAlignment(Pos.CENTER);
        
        Button button12 = new Button("ete");
        
        ete.getChildren().add(new Label("Ete:"));
        ete.getChildren().add(button12);
        
        layout.getChildren().addAll(setup,register,report,ete);
        
        Scene scene = new Scene(layout);
        
        window.setScene(scene);
        window.setTitle("Bilancio idrico");
        window.show();
        
    }
}
