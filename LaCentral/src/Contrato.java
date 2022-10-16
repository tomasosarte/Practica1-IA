public class Contrato {
    private int central;
    private double consumo;

    public Contrato( int _central , double _consumo){
        this.central = _central;
        this.consumo = _consumo;
    }

    public int get_central(){
        return this.central;
    }

    public double get_prod(){
        return this.consumo;
    }

    public void set_central(int _central){
        this.central = _central;
    }

    public void set_consumo(double _prod){
        this.consumo = _prod;
    }
}
