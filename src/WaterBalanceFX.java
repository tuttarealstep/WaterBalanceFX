import java.util.Scanner;

public class WaterBalanceFX{
    
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
        
        //1. setup di tutti e 4 i file
        fileManager.stationFileSetup();
        fileManager.fieldFileSetup();
        fileManager.constantFileSetup();
        fileManager.calculationFileSetup();
        
        //2. registrazione dei dati del campo
        fileManager.fieldDatasRegistration();
        //3. report dei dati del campo
        fileManager.fieldDatasReport();
        
        //4. registrazione delle costanti del suolo
        forManager.constant();
        //5. report delle costanti del suolo
        fileManager.constantDatasReport();
        
        //6. registro i dati della stazione meteo
        //7. report dei dati della stazione meteo
        //8. calcolo ete, così da riempire la lista dei calcoli
        //9. report dei calcoli fatti per calcolare ete
        for(int i = 0; i < fileManager.countRows("WSDatas.txt") - 1; i++) {
           fileManager.stationDatasRegistration();
           fileManager.stationDatasReport();
           forManager.ete();
           fileManager.calculationsReport();
        }
                
        long end = System.currentTimeMillis();
        
        System.out.println("\nTime: " + (end-start) + " milliseconds");
    }
}
