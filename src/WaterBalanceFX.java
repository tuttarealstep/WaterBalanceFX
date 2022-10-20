import java.util.Scanner;

public class WaterBalanceFX{
    
    public static WeatherStation ws;
    public static Field field;
    public static FormulaManager forManager;
    public static FileManager fileManager;
    public static Scanner scanner;   
    public static TableRow table;
    
    public static void main(String[] args) {
        
        ws = new WeatherStation();
        field = new Field();
        forManager = new FormulaManager(ws, field);
        scanner = new Scanner(System.in);
        WaterBalanceFX.fileManager = new FileManager();
        //costruzione delle tabelle di appoggio
        TableRow[] pomodoro = {
            new TableRow(40, 0.4),
            new TableRow(490, 0.8),
            new TableRow(650, 1),
            new TableRow(970, 1),
            new TableRow(1110, 0.7),
            new TableRow(1910, 0.1),
            new TableRow(1910, 0.1),
        };
        TableRow[] cipolla = {
            new TableRow(50, 0.4),
            new TableRow(600, 0.5),
            new TableRow(900, 0.7),
            new TableRow(1400, 1),
            new TableRow(2400, 0.8),
            new TableRow(2600, 0.4),
            new TableRow(2600, 0.4),
        };
        TableRow[] patata = {
            new TableRow(140, 0.4),
            new TableRow(550, 0.8),
            new TableRow(800, 1),
            new TableRow(1075, 1),
            new TableRow(1559, 0.7),
            new TableRow(1959, 0.1),
            new TableRow(2549, 0.1),
        };
        
        //Test dei metodi e delle tempistiche
        long start = System.currentTimeMillis();
        
        start();
        
        
        long end = System.currentTimeMillis();
        
        System.out.println("\nTime: " + (end-start) + " milliseconds");
    }
    
    public static void start() {
        
        loop : while(true) {
            System.out.println("Scegliere tra le seguenti opzioni:\n"
                    + "1. Sovrascrivi la densAppar\n"
                    + "2. Simula le giornate\n"
                    + "3. Seleziona la fase fenologica"
                    + "0. Esci");
            
            int choice = Integer.valueOf(scanner.nextLine());
            switch(choice) {
                case 1:
                    System.out.println("Inserire il nuovo valore:");
                    double value = Double.valueOf(scanner.nextLine());
                    forManager.calculations.put("densAppar", value);
                    break;
                case 2:
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
                    break;
                case 3:
                    System.out.println("Inserire la fase fenologica (0-7)");
                    int phase = Integer.valueOf(scanner.nextLine());
                    field.setPhenophase(phase);
                case 0:
                    break loop;
            }
        }
    }
}
