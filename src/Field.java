
public class Field {
    
    private double sabbia;
    private double argilla;
    private double corg;
    private double profmm;
    private double kc;  
    private String seed;
    private int phenophase;
    private int minCard;
    
    public Field() {
        
        this.sabbia = 0;
        this.argilla = 0;
        this.corg = 0;
        this.profmm = 0;
        this.kc = 0;
        this.seed = "";
        this.phenophase = 0;
        if(this.getSeed().equalsIgnoreCase("cipolla")) {
            this.minCard = 0;
        } else if (this.getSeed().equalsIgnoreCase("pomodoro") ||
                this.getSeed().equalsIgnoreCase("patata") ) {            
            this.minCard = 2;
        }  
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
    public int getMinCard() {
        return minCard;
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
    public void setMinCard(int minCard) {
        this.minCard = minCard;
    }
}
