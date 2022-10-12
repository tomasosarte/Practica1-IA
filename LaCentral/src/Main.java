import IA.Energia.Centrales;
import IA.Energia.Central;
import IA.Energia.Cliente;
import IA.Energia.Clientes;
import IA.Energia.VEnergia;

import java.util.*;

import static java.lang.Math.*;

public class Main {
    public static Vector<Integer> estado;
    public static Vector<Integer> garantizados;
    public static Vector<Integer> noGarantizados;

    public static void main(String[] args) throws Exception {
        Centrales centrales = new Centrales(new int[] {1,1,1}, 0);
        Clientes clientes = new Clientes(100, new double[] { 0.3, 0.4,0.3}, 0.5, 0);
    }
}