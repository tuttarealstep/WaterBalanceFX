
public class Field {
    
    private double sabbia;
    private double argilla;
    private double corg;
    private double profmm;
    private double kc;  
    private String seed;
    private int phenophase;
    
    public Field() {
        
        this.sabbia = 0;
        this.argilla = 0;
        this.corg = 0;
        this.profmm = 0;
        this.kc = 0;
        this.seed = "";
        this.phenophase = 0;
            
    }

    public double getSabbia() {
        return sabbia;
    }
    public double getArgilla() {
        return argilla;
    }
    public double getCorg() {
        return corg;
    }
    public double getProfmm() {
        return profmm;
    }
    public double getKc() {
        return kc;
    } 
    public String getSeed() {
        return seed;
    }
    public int getPhenophase() {
        return phenophase;
    }    
    
    
    public void setSabbia(double sabbia) {
        this.sabbia = sabbia;
    }
    public void setArgilla(double argilla) {
        this.argilla = argilla;
    }
    public void setCorg(double corg) {
        this.corg = corg;
    }
    public void setProfmm(double profmm) {
        this.profmm = profmm;
    }
    public void setKc(double kc) {
        this.kc = kc;
    }
    public void setSeed(String seed) {
        this.seed = seed;
    }
    public void setPhenophase(int phenophase) {
        this.phenophase = phenophase;
    }
    
    public double tables() {
        
        double[][] cipolla = {
            {1.0, 50.0, 0.4},
            {2.0, 600.0, 0.5},
            {3.0, 900.0, 0.7},
            {4.0, 1400.0, 1},
            {5.0, 2400.0, 0.8},
            {6.0, 2600.0, 0.4},
            {7.0, 2600.0, 0.4}
        };
        
        double[][] patata = {
            {1.0, 140.0, 0.4},
            {2.0, 550.0, 0.5},
            {3.0, 800.0, 0.7},
            {4.0, 1075.0, 0.95},
            {5.0, 1559.0, 0.95},
            {6.0, 1959.0, 0.85},
            {7.0, 2549.0, 0.01}
        };
        
        double[][] pomodoro = {
            {1.0, 40.0, 0.4},
            {2.0, 490.0, 0.8},
            {3.0, 650.0, 1},
            {4.0, 970.0, 1},
            {5.0, 1110.0, 0.7},
            {6.0, 1910.0, 0.1},
            {7.0, 1910.0, 0.1}
        };
        
        return 0;
    }
}
