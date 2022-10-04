
import java.util.Scanner;
import javafx.application.Application;
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
        window.setTitle("Hello World!");
        window.show();
    }
}
