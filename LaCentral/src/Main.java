import IA.Energia.Centrales;
import IA.Energia.Clientes;

public class Main {

    public static void main(String[] args) throws Exception {
        Centrales centrales = new Centrales(new int[] {30,30,40}, 0);
        Clientes clientes = new Clientes(100, new double[] { 0.3, 0.4,0.3}, 0.5, 0);

        System.out.println(centrales);
        System.out.println(clientes);
        System.out.println("Hola");
    }
}