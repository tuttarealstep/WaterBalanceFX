import java.util.Scanner;

public class WaterBalanceFX{
    
    public static WeatherStation ws;
    public static Field field;
    public static FormulaManager forManager;
    public static TableRow[] pomodoro;
    public static TableRow[] cipolla;
    public static TableRow[] patata;
    public static FileManager fileManager;
    public static Scanner scanner;   
    
    
    public static void main(String[] args) {
        
        ws = new WeatherStation();
        field = new Field();
        forManager = new FormulaManager(ws, field);
        scanner = new Scanner(System.in);
        pomodoro = new TableRow[7];
        cipolla = new TableRow[7];
        patata = new TableRow[7];
        
        //costruzione della tabella del pomodoro
            pomodoro[0] = new TableRow(40, 0.4);
            pomodoro[1] = new TableRow(490, 0.8);
            pomodoro[2] = new TableRow(650, 1);
            pomodoro[3] = new TableRow(970, 1);
            pomodoro[4] = new TableRow(1110, 0.7);
            pomodoro[5] = new TableRow(1910, 0.1);
            pomodoro[6] = new TableRow(1910, 0.1);
        //costruzione della tabella della cipolla
            cipolla[0] = new TableRow(50, 0.4);
            cipolla[1] = new TableRow(600, 0.5);
            cipolla[2] = new TableRow(900, 0.7);
            cipolla[3] = new TableRow(1400, 1);
            cipolla[4] = new TableRow(2400, 0.8);
            cipolla[5] = new TableRow(2600, 0.4);
            cipolla[6] = new TableRow(2600, 0.4);
        //costruzione della tabella della patata
            patata[0] = new TableRow(140, 0.4);
            patata[1] = new TableRow(550, 0.8);
            patata[2] = new TableRow(800, 1);
            patata[3] = new TableRow(1075, 1);
            patata[4] = new TableRow(1559, 0.7);
            patata[5] = new TableRow(1959, 0.1);
            patata[6] = new TableRow(2549, 0.1);
            
            WaterBalanceFX.fileManager = new FileManager();
        
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
                    + "2. Fai il setup dei file(obbligatorio per le simulazioni)\n"
                    + "3. Seleziona la fase fenologica\n"
                    + "4. Simula i calcoli prima della semina\n"
                    + "5. Simula i calcoli dalla semina in poi\n"
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

                    
                    break;
                case 3:
                    System.out.println("Inserire la fase fenologica (0-7)");
                    int phase = Integer.valueOf(scanner.nextLine());
                    field.setPhenophase(phase);
                    break;
                case 4:
                    field.setPhenophase(0);
                    System.out.println("Inserito il campo vuoto");
                    
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
                case 5:
                    field.setPhenophase(1);
                    System.out.println("Inserita la semina");
                    
                    //6. registro i dati della stazione meteo
                    //7. report dei dati della stazione meteo
                    //8. calcolo ete, così da riempire la lista dei calcoli
                    //9. report dei calcoli fatti per calcolare ete
                    for(int i = 0; i < fileManager.countRows("WSDatas1.txt") - 1; i++) {
                       fileManager.stationDatasRegistration();
                       fileManager.stationDatasReport();
                       forManager.ete();
                       fileManager.calculationsReport();
                    }
                    break;
                case 0:
                    break loop;
            }
        }
    }
}
