import IA.Energia.Centrales;
import IA.Energia.Central;
import IA.Energia.Cliente;
import IA.Energia.Clientes;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.util.*;

public class Main {
    public static Vector estado = new Vector();
    public static Vector garantizados = new Vector();
    public static Vector noGarantizados = new Vector();
    public static void main(String[] args) throws Exception {
        Centrales centrales = new Centrales(new int[] {30,30,40}, 0);
        Clientes clientes = new Clientes(100, new double[] { 0.3, 0.4,0.3}, 0.5, 0);

        initializer(centrales, clientes);

        System.out.println(garantizados);
        System.out.println(noGarantizados);
    }
    public static void initializer(Centrales centrales, Clientes clientes) {
        for(int i = 0; i < clientes.size(); i++) {
            if(clientes.get(i).getContrato() == Cliente.GARANTIZADO) garantizados.add(i);
            else noGarantizados.add(i);
        }
    }
    public static double distance(Cliente cliente, Central central) {
        double dist_x = cliente.getCoordX() - central.getCoordX();
        double dist_y = cliente.getCoordY() - central.getCoordY();
        return sqrt(pow(dist_x,2) + pow(dist_y,2));
    }
}