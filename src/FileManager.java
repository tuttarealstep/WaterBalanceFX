
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class FileManager{
    
    public FormulaManager manager;
    
    public FileManager() {
        this.manager = WaterBalanceFX.forManager;
    }
    
    //Creazione del file per stazione meteo
    public void stationFileSetup() {
        try {
          File myObj = new File("WSreport.txt");
          if (myObj.createNewFile()) {
            System.out.println("File created: " + myObj.getName());
          } else {
            System.out.println("File already exists.");
          }
        } catch (IOException e) {
          System.out.println("Error: " + e.getMessage());
        }
    }
    
    //Dati presi dal file WSDatas.txt, in modo da inserirli automaticamente per ogni giorno
    public void stationDatasRegistration() {
        
        try(Scanner scanFile = new Scanner(Paths.get("WSDatas.txt"))) {
            String row = "";
            
            for(int i = 0; i < manager.ws.getJulianDay() + 2; i++) {
                row = scanFile.nextLine();
            }
            System.out.println(row);
            String[] parts = row.split(";");
            manager.ws.setMinTemp(Double.valueOf(parts[0]));
            manager.ws.setMaxTemp(Double.valueOf(parts[1]));
            manager.ws.setAvgTemp(Double.valueOf(parts[2]));
            manager.ws.setRain(Double.valueOf(parts[3]));
            manager.ws.setRs(Double.valueOf(parts[4]));
            manager.ws.setRhMin(Double.valueOf(parts[5]));
            manager.ws.setRhMax(Double.valueOf(parts[6]));
            manager.ws.setWind(Double.valueOf(parts[7]));
            manager.ws.setAltitudine(Double.valueOf(parts[8]));
            manager.ws.setLatitudine(Double.valueOf(parts[9]));

            manager.ws.setJulianDay(manager.ws.getJulianDay() + 1);

            if(manager.ws.getDate().equals("0")) {
                manager.ws.setDate("2021-01-01");
            } else {
                manager.ws.setDate(LocalDate.parse(manager.ws.getDate()).plusDays(1).toString());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        
    }
    
    //Scrittura dei dati sul file
    public void stationDatasReport() {        
        try(FileWriter writer = new FileWriter("WSreport.txt",true)) {
            writer.write("JulianDay: " + manager.ws.getJulianDay() + "\n" +
                        "date(DD-MM-YYYY): " + manager.ws.getDate() + "\n" +
                        "minTemp(C): " + manager.ws.getMinTemp() + "\n" +
                        "maxTemp(C): " + manager.ws.getMaxTemp() + "\n" +
                        "avgTemp(C): " + manager.ws.getAvgTemp() + "\n" +
                        "rain(mm): " + manager.ws.getRain() + "\n" +
                        "rs(W/m^2): " + manager.ws.getRs() + "\n" +
                        "rhMin(%): " + manager.ws.getRhMin() + "\n" +
                        "rhMax(%): " + manager.ws.getRhMax() + "\n" +
                        "wind(m/s): " + manager.ws.getWind() + "\n" +
                        "altitude(m): " + manager.ws.getAltitudine() + "\n" +
                        "latitude(m): " + manager.ws.getLatitudine() + "\n" +
                        "\n");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    //Creazione e intestazione del file di campo
    public void fieldFileSetup() {
        try {
          File myObj = new File("FieldReport.txt");
          if (myObj.createNewFile()) {
            System.out.println("File created: " + myObj.getName());
          } else {
            System.out.println("File already exists.");
          }
        } catch (IOException e) {
          System.out.println("Error: " + e.getMessage());
        }
    }
    
    //Richiesta dei dati da inserire e settaggio dei parametri
    //Questi valori vengono richiesti una volta per campo, in quanto le costanti
    //del suolo rimangono invariate per tutto il periodo attivo
    public void fieldDatasRegistration() {
        manager.field.setSabbia(31.2);
        manager.field.setArgilla(50.1);
        //Richiesta della componente organica del terreno
        //Se viene scelto SO, si converte dividendo per un valore fisso 1.72
        System.out.println("Scegliere il dato da inserire:\n"
                + "1. Sostanza organica (S.O.)\n"
                + "2. Carbonio organico (Corg)");
        Scanner scanner = new Scanner(System.in);
        int choice = Integer.valueOf(scanner.nextLine());
        System.out.println("Digitare il valore:");
        double value = Double.valueOf(scanner.nextLine());
        if(choice == 1) {
            manager.field.setCorg(value / 1.72);
        } else if (choice == 2) {
           manager.field.setCorg(value); 
        }
        System.out.println("Inserire il tipo di coltura:\n"
                + "cipolla\n"
                + "patata\n"
                + "pomodoro");
        String seed = scanner.nextLine();
        manager.field.setSeed(seed);
        System.out.println(manager.field.getSeed());
        manager.field.setProfmm(150);
        manager.field.setKc(0.3);
    }
    
    //Scrittura dei primi dati sul file
    public void fieldDatasReport() {
        try(FileWriter writer = new FileWriter("FieldReport.txt",true)) {
            writer.write("sabbia(%): " + manager.field.getSabbia() + "\n" +
                        "argilla(%): " + manager.field.getArgilla() + "\n" +
                        "corg(%): " + manager.field.getCorg() + "\n" +
                        "prof(mm): " + manager.field.getProfmm() + "\n" +
                        "kc: " + manager.field.getKc() + "\n" +
                        "\n");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    //Creazione e intestazione del file contenente le costanti del suolo
    public void constantFileSetup() {
        try {
          File myObj = new File("ConsReport.txt");
          if (myObj.createNewFile()) {
            System.out.println("File created: " + myObj.getName());
          } else {
            System.out.println("File already exists.");
          }
        } catch (IOException e) {
          System.out.println("Error: " + e.getMessage());
        }
    }
    
    //non serve un constantDatasRegistration perchè i valori vengono tutti calcolati
    
    //Scrittura su file delle costanti del suolo
    public void constantDatasReport() {
        try(FileWriter writer = new FileWriter("ConsReport.txt",true)) {
            writer.write("PROFmm (mm): " + manager.calculations.get("PROFmm") + "\n" +
                        "PA (%v/v): " + manager.calculations.get("PAperc") + "\n" +
                        "Lir (%v/v): " + manager.calculations.get("LirPerc") + "\n" +
                        "CC (%v/v): " + manager.calculations.get("CCperc") + "\n" +
                        "CIM (%v/v): " + manager.calculations.get("CIMper") + "\n" +
                        "PAmm (mm): " + manager.calculations.get("PAmm") + "\n" +
                        "Lirmm (mm): " + manager.calculations.get("Lirmm") + "\n" +
                        "CCmm (mm): " + manager.calculations.get("CCmm") + "\n" +
                        "CIMmm (mm): " + manager.calculations.get("CIMmm") + "\n" +
                        "\n");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    //Scorrimento di tutti e 4 i file per trovare il valore registrato dell'ultimo giorno
    public double valueFromFile(String str) {
        
        ArrayList<String> files = new ArrayList<>();
        files.add("WSreport.txt");
        files.add("FieldReport.txt");
        files.add("ConsReport.txt");
        files.add("Calculations.txt");
        
        double value = 0;
        
        for(int i = 0; i < files.size(); i++) {
            try(Scanner toScan = new Scanner(Paths.get(files.get(i)))) {
                while(toScan.hasNextLine()) {
                    String[] parts = toScan.nextLine().split(":");
                    if(parts[0].contains(str)) {
                        value = Double.valueOf(parts[1].trim());
                    }
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            } 
        }
        return value;
    }
    
    //Creazione del file per il contenimento dei calcoli
    public void calculationFileSetup() {
        try {
          File myObj = new File("Calculations.txt");
          if (myObj.createNewFile()) {
            System.out.println("File created: " + myObj.getName());
          } else {
            System.out.println("File already exists.");
          }
        } catch (IOException e) {
          System.out.println("Error: " + e.getMessage());
        }
    }
    
    //Scrittura del calcoli su file
    public void calculationsReport() {
        try(FileWriter writer = new FileWriter("Calculations.txt",true)) {
            writer.write(manager.ws.getJulianDay() + "\n");
            writer.write(manager.ws.getDate() + "\n");
            for(String key : this.manager.calculations.keySet()) {
                writer.write(key + ": " + this.manager.calculations.get(key) + "\n");
            }
            writer.write("\n");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    //conteggio delle righe del file
    public int countRows(String file) {
        int rows = 0;
        try(Scanner scanFile = new Scanner(Paths.get(file))) {
            while(scanFile.hasNextLine()) {
                rows++;
                scanFile.nextLine();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return rows;
    }
}