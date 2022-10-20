
import java.util.HashMap;

public class FormulaManager {
    
    //dati forniti dalla creazione del campo:
        //sabbia, argilla, corg, profmm, kc
    //dati forniti dalla stazione meteo:
        //minTemp, maxTemp, avgTemp, wind, altitudine, latitudine, julianDay, rs, rhMin, rhMax
    
    public Field field;
    public WeatherStation ws;
    //compilo un'hashMap con tipo di calcolo e valore per evitare ripetizioni
    public HashMap<String,Double> calculations;
    
    public FormulaManager(WeatherStation ws, Field field) {
        
        this.field = field;
        this.ws = ws;
        this.calculations = new HashMap<>();
        
    }
    
    //creazione e calcolo delle costanti che avranno lo stesso valore per tutta l'andata del campo
    //PROFmm va inserita manualmente
    //PA(punto appassimento),Lir(limite intervento irriguo),CC(capacità campo),CIM(capacità idrica max)
    public void constant() {
        //PROFmm
        calculations.put("PROFmm", field.getProfmm());
        
        //PApercVv
        //PA% v/v = (0.0854 - (0.0004*sabbia%) + (0.0044*argilla%) + 
        // + (0.0122*corg%/0.67) - (0.0182*densAppar) * 100)
        double paPerc = (0.0854-(0.0004*field.getSabbia())+(0.0044*field.getArgilla())+
                (0.0122*field.getCorg()/0.67)-(0.0182*densAppar())) * 100;
        calculations.put("PAperc", paPerc);
        
        //CCpercVv
        //CC% v/v = CC v/v * 100
        //CC v/v = 0.3486-(0.0018*sabbia%)+(0.0039*argilla%)+(0.0228*corg%/0.67)-(0.0738*densAppar)
        double ccPerc = (0.3486-(0.0018*field.getSabbia())+(0.0039*field.getArgilla())+
                (0.0228*field.getCorg()/0.67)-(0.0738*densAppar())) * 100;
        calculations.put("CCperc", ccPerc);
        
        //LirPercVv
        //Lir% v/v = Lir v/v * 100
        //Lir v/v = CC - Lir% ADM;
        //CC = 0,3486-(0,0018*sabbia%)+(0,0039*argilla%)+(0,0228*corg%/0,67)-(0,0738*densAppar)
        //Lir% ADM = ADM% * 0.35
        //ADM% = CC v/v - PA v/v
        double ccVv = calculations.get("CCperc") / 100.0;
        double admPerc = ccVv - (calculations.get("PAperc") / 100.0);
        //il valore moltiplicato per Adm varia in base alla coltura e si riferisce alla
        //percentuale di acqua disponibile massima
        //pomodoro: 40, patata: 35, cipolla: 30
        double lirPercVv = (ccVv - (admPerc * 30 / 100)) * 100;
        calculations.put("LirPerc", lirPercVv);
        
        //CIMPercVv
        //1-(DensAppar/2,65) * 100
        calculations.put("CIMper", (1-(densAppar() / 2.65)) * 100);
        
        //tutti i valori in mm vanno moltiplicati per la PROFmm e divisi per 100 (perc * PROFmm / 100)
        
        //PAmm
        calculations.put("PAmm" , (paPerc*calculations.get("PROFmm")/100));
        
        //CCmm
        calculations.put("CCmm", ccPerc * calculations.get("PROFmm") / 100); 
        
        //Lirmm
        calculations.put("Lirmm",calculations.get("LirPerc") * calculations.get("PROFmm")/100);               
        
        //CIMmm
        calculations.put("CIMmm", calculations.get("CIMper") * calculations.get("PROFmm") / 100);
        
    }
    
    
    
    //primo calcolo importante per il calcolo della ETe (evapotraspirazione effettiva)
    public double ks() {
        //Calcolo il Ks del bilancio idrico
        
        //KS = se UMIDVOL% < PA% v/v == 0 altrimenti
        //minore tra 1 e (UMIDVOL% - PA% v/v)/(LIr% v/v - PA% v/v)
        
        double paPerc = calculations.get("PAperc");
        
        if (umidvol() < paPerc) {
            calculations.put("IRRIGutile", 10.0);
            return 0;
        } else if((umidvol() - paPerc)/(calculations.get("LirPerc") - paPerc) < 1) {
            double ks = (umidvol() - paPerc)/(calculations.get("LirPerc") - paPerc);
            if (ks < 0.7) {
                calculations.put("IRRIGutile", 10.0);
            }
            return (umidvol() - paPerc)/(calculations.get("LirPerc") - paPerc);
        } else {
            calculations.put("IRRIGutile", 0.0);
            return 1;
        }
    } 
    
    public double umidvol() {
        //Calcolo la umidvol del bilancio idrico
        
        //Umidvol% = UMIDmm mm/PROFmm mm * 100
        
        //PROFmm è un valore fisso a 150 sulle cipolle
        //a 500 sui pomodori
        //a 400 sulla patata
        //calcolo
        return umidmm()/field.getProfmm() * 100;
    }
    
    public double umidmm() {
        String name = "UMIDmm";
        
        //gp = giorno precedente
        //UMIDmm(gp)+rain(gp)+IRRIGutile(gp)-ete(gp)-PERCmm-RUSCmm
        
        double umidMm = WaterBalanceFX.fileManager.valueFromFile("UMIDmm") +
                        WaterBalanceFX.fileManager.valueFromFile("rain") +
                        WaterBalanceFX.fileManager.valueFromFile("IRRIGutile") + 
                        WaterBalanceFX.fileManager.valueFromFile("Ete") - 
                        percMm() - ruscMm();
        
        calculations.put(name,umidMm);
        
        return umidMm;
    }
    
    public double ruscMm() {
        
        //gp = giorno precedente
        //SE(UMIDmm(gp)+rain(gp)+IRRIGutile(gp)-Ete(gp)<CIMmm(gp)) allora 0
        //altrimenti (UMIDmm(gp)+rain(gp)+IRRIGutile(gp)-Ete(gp)-CIMmm(gp))
        
        double umidMm = WaterBalanceFX.fileManager.valueFromFile("UMIDmm");
        double rain = WaterBalanceFX.fileManager.valueFromFile("rain");
        double irrigUtile = WaterBalanceFX.fileManager.valueFromFile("IRRIGutile");
        double ete = WaterBalanceFX.fileManager.valueFromFile("Ete");
        double cimMm = WaterBalanceFX.fileManager.valueFromFile("CIMmm");
        
        if(umidMm + rain + irrigUtile - ete < cimMm) {
            return 0;
        } else {
            return umidMm + rain + irrigUtile - ete - cimMm;
        }
    }
    
    public double percMm() {
        
        //gp = giorno precedente
        //SE(UMIDmm(gp)+rain(gp)+IRRIGutile(gp)-ete(gp)<CCmm(gp)) allora 0
        //altrimenti (UMIDmm(gp)+rain(gp)+IRRIGutile(gp)-ete(gp)-RUSCmm-CCmm(gp))
        
        double umidMm = WaterBalanceFX.fileManager.valueFromFile("UMIDmm");
        double rain = WaterBalanceFX.fileManager.valueFromFile("rain");
        double irrigUtile = WaterBalanceFX.fileManager.valueFromFile("IRRIGutile");
        double ete = WaterBalanceFX.fileManager.valueFromFile("Ete");
        double ccMm = WaterBalanceFX.fileManager.valueFromFile("CCmm");
        
        if(umidMm + rain + irrigUtile - ete < ccMm) {
            return 0;
        } else {
            return umidMm + rain + irrigUtile - ete - ruscMm() - ccMm;
        }
    }
    
    public double kc() {
        //se la fase fenologica è ancora a 0 (suolo nudo), il kc rimane a 0.3
        
        //Per cipolla:
        //SE(gradiGiorno < 50 allora 0.4 ;
        //Altrimenti SE(E(gradiGiorno >= 50; gradiGiorno < 600) allora 0.5 ;
        //SE(E(gradiGiorno >= 600 ; gradiGiorno < 900) allora 0.7 ;
        //SE(E(gragiGiorno >= 900 ; gradiGiorno < 1400) allora 1 ;
        //SE(E(gradiGiorno >= 1400 ; gradiGiorno < 2400) allora 0.8 ;
        //SE(E(gradiGiorno >= 2400 ; gradiGiorno <= 2600) allora 0.4 ;
        //altrimenti 0,01)))))))
        
        if(field.getPhenophase() != 0) {
           if(field.getSeed().equalsIgnoreCase("cipolla")) {
            
           }
           if(field.getSeed().equalsIgnoreCase("patata")) {
               
           }
           if(field.getSeed().equalsIgnoreCase("pomodoro")) {
               
           }
        }
        
        return 0.3;
    }
    
    //secondo calcolo importante per il calcolo della ETe con ETc (evapotraspirazione coltura)
    public double etc() {
        //ETc = Kc * ET0 (evapotraspirazione di riferimento)
        //Kc = costante che varia in base alla coltura
        //ET0 = (0.408 * slopeSat * (Rn - G) + psicrCost * 900 / (avgTemp + 273) * wind *
            //(es - ea))/(slopeSat + psicrCost * (1 + 0,34 * wind))
        //G è un valore che nei modelli è fissato a 0 
        //Il valore di G indica il flusso di calore nel suolo. Questo è molto piccolo di solito e quindi possiamo trascurare.
        System.out.println("etc: " + (field.getKc() * et0()));
        
        return field.getKc() * et0();
    }
    
    public double densAppar() {
        String name = "densAppar";
        
        //la densAppar dipende dal Corg (carbonio organico), quindi se non c'è si fa la conversione
        //densAppar = 1,66-0,318*RADQ(Corg)
        
        double densAppar = 1.66 - 0.318 * Math.sqrt(field.getCorg());
        
        calculations.put(name,densAppar);
        
        System.out.println(name + ": " + densAppar);
        
        return densAppar;
    }
    
    public double slopeSat() {
        String name = "slopeSat";
        
        //Slope Sat vap press def = 4098 * (0.6108 * EXP(17.27 * avgTemp /
            //(avgTemp + 237.3))) / (avgTemp + 237.3)^2            
            double slopeSat = 4098 * (0.6108 * Math.exp(17.27 * ws.getAvgTemp() /
                    (ws.getAvgTemp() + 237.3))) / Math.pow(ws.getAvgTemp() + 237.3, 2);
            
            calculations.put(name, slopeSat);
            
            System.out.println(name + ": " + slopeSat);
            
            return slopeSat;
    }
    
    public double rn() {
        String name = "rn";
        
        //Rn = Rns - Rnl
        double rn = rns() - rnl();
        
        System.out.println(name + ": " + rn);
        
        calculations.put(name, rn);
        
        return rns() - rnl();
    }
    
    public double rns() {
        String name = "rns";
        
        //Rns = Rs(MJ m^-2 d^-1) * (1-0.23)
        double rns = ws.getRs() * (1-0.23);
        
        calculations.put(name, rns);
        
        System.out.println(name + ": " + rns);        
        
        return ws.getRs() * (1-0.23);
    }
    
    public double rnl() {
        String name = "rnl";
        
        //Rnl = =4,903*10^(-9)*
            //MEDIA((maxTemp + 273.16)^4;(minTemp + 273.16)^4) * (0.34-0.14*RADQ(ea))*
            //(1.35 * rs / rso- 0.35)
        
        double rnl = 4.903 * Math.pow(10, -9) * ((Math.pow(ws.getMaxTemp() + 273.16, 4) +
                Math.pow(ws.getMinTemp() + 273.16, 4)) / 2.0) * (0.34 - 0.14 * Math.sqrt(ea())) *
                (1.35 * ws.getRs() / rso() - 0.35);
        
        calculations.put(name, rnl);
        
        System.out.println(name + ": " + rnl);
        
        return rnl;
    }
    
    public double psiCost() {
        String name = "psiCost";
        
        //psicr. costant = 0.000665 * atm pressure (kPa)
        
        double psiCost = 0.000665 * atmPressure();
        
        calculations.put(name, psiCost);
        
        System.out.println(name + ": " + psiCost);
        
        return 0.000665 * atmPressure();  
    }
    
    public double atmPressure() {
        String name = "atmPressure";
        
        //101.3 * (((293 - 0.0065 * altitude) / 293) ^ 5.26)        
        double atmPressure = 101.3 * Math.pow(((293 - 0.0065 * ws.getAltitudine()) / 293.0), 5.26);
        
        calculations.put(name, atmPressure);
        
        System.out.println(name + ": " + atmPressure);
        
        return atmPressure;        
    }
    
    public double es() {
        String name = "es";
        
        //es = media tra e^0Tmax, e^0Tmin
        //e0Tmax = 0.6108*Math.exp((17.27*Tmax)/(Tmax+237.3))
        //e0Tmin = 0.6108*Math.exp((17,27*minTemp)/(minTemp+237,3))
        
        double es = (eTmax() + eTmin()) / 2.0;
        
        calculations.put(name, es);
        
        System.out.println(name + ": " + es);
        
        return (eTmax() + eTmin()) / 2.0;     
    }
    
    public double eTmax() {
        String name = "eTmax";
        
        //e0Tmax = 0.6108*Math.exp((17.27*Tmax)/(Tmax+237.3))
        double eTmax = 0.6108*Math.exp((17.27*ws.getMaxTemp())/(ws.getMaxTemp()+237.3));
        
        calculations.put(name, eTmax);
        
        System.out.println(name + ": " + eTmax);

        return eTmax;
    }
    
    public double eTmin() {
        String name = "eTmin";
        
        //e0Tmin = 0.6108*Math.exp((17,27*minTemp)/(minTemp+237,3))
        double eTmin = 0.6108*Math.exp((17.27*ws.getMinTemp())/(ws.getMinTemp()+237.3));
        
        calculations.put(name, eTmin);
        
        System.out.println(name + ": " + eTmin);
        
        return eTmin;
    }
    
    public double ea() {
        String name = "ea";
        
        //ea = (eTmin * RHmax / 100 + eTmax * RHmin / 100) / 2        
        double ea = (eTmin() * ws.getRhMax() / 100.0 + eTmax() * ws.getRhMin() / 100.0) / 2.0;
        
        calculations.put(name, ea);
        
        System.out.println(name + ": " + ea);
        
        return ea;        
    }
    
    public double rso() {
        String name = "rso";
        
        //Rso = (0.75 + 0.00002 * this.altitudine) * Ra
        
        double rso = (0.75 + 0.00002 * ws.getAltitudine()) * ra();
        
        calculations.put(name, rso);
        
        System.out.println(name + ": " + rso);
        
        return rso;
    }
    
    public double ra() {
        String name = "ra";
        
        //Ra = 24 * 60 / PI.GRECO() * 0.082 * invRelDist *(sunsetHourAngle *
            //SEN(latitudine) * SEN(solarDeclination) + COS(latitudine)*
            //COS(solarDeclination) * SEN(sunsetHourAngle))
            
        double ra = 24 * 60 / Math.PI * 0.082 * invRelDist() *(sunsetHourAngle() * 
                Math.sin(ws.getLatitudine()) * Math.sin(solarDeclination()) + Math.cos(ws.getLatitudine())*
                Math.cos(solarDeclination()) * Math.sin(sunsetHourAngle()));
        
        calculations.put(name, ra);
        
        System.out.println(name + ": " + ra);
        
        return ra;
    }
    
    public double invRelDist() {
        String name = "invRelDist";
        
        //invRelDist = 1 + 0.033 * COS(2 * PI.GRECO() * julianDay / 365)        
        double invRelDist = 1 + 0.033 * Math.cos(2 * Math.PI * ws.getJulianDay() / 365.0);
        
        calculations.put(name, invRelDist);
        
        System.out.println(name + ": " + invRelDist);
        
        return invRelDist;
    }
    
    public double sunsetHourAngle() {
        String name = "sunsetHourAngle";
        
        //sunset hour angle = ARCCOS(-TAN(latitudine)*TAN(solarDeclination))
        double sunsetHourAngle = Math.acos(-Math.tan(ws.getLatitudine()) * Math.tan(solarDeclination()));
        
        calculations.put(name, sunsetHourAngle);
        
        System.out.println("lat: " + ws.getLatitudine());
        System.out.println(name + ": " + sunsetHourAngle);
        
        return sunsetHourAngle;
    }
    
    public double solarDeclination() {
        String name = "solarDeclination";
        
        //solarDeclination = 0.409 * Math.sen(2 * Math.PI * this.julianDay / 365 - 1.39)
        double solarDeclination = 0.409 * Math.sin(2 * Math.PI * ws.getJulianDay() / 365.0 - 1.39);
        
        //dopo aver fatto il calcolo, lo inserisco nell'HashMap per non doverlo ripetere
        calculations.put(name, solarDeclination);
        
        System.out.println(name + ": " + solarDeclination);
        
        return solarDeclination;
    }
    
    public double et0() {
        String name = "et0";
        
        //(0.408 * slopeSat * (Rn - G) + psicrCost * 900 / (avgTemp + 273) * wind *
            //(es - ea))/(slopeSat + psicrCost * (1 + 0.34 * wind))
        //G è un valore messo fisso a 0 nei modelli
        double G = 0;     
            
        double et0 = (0.408 * slopeSat() * (rn() - G) + psiCost() * 900 /
                (ws.getAvgTemp() + 273) * ws.getWind() * (es() - ea()))/(slopeSat() + psiCost() *
                (1 + 0.34 * ws.getWind()));
        
        calculations.put(name, et0);
        
        System.out.println(name + ": " + et0);
        
        return et0;
    }
    
    //calcolo finale per il calcolo dell'evapotraspirazione 
    public double ete() {
        String name = "ete";
        
        //ETe = Etc * Ks
        double ete = etc() * ks();
        
        calculations.put(name, ete);
        
        System.out.println(name + ": " + ete);
        
        return ete;
    }
    
    public boolean alreadyDone(String str) {
        return calculations.containsKey(str);
    }
}
