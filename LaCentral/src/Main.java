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
        Centrales centrales = new Centrales(new int[] {1,1,1}, 0);
        Clientes clientes = new Clientes(100, new double[] { 0.3, 0.4,0.3}, 0.5, 0);

        initializer(centrales, clientes);

        System.out.println(garantizados);
        System.out.println(noGarantizados);

        double total = 0.0;
        for(int i = 0; i < garantizados.size(); i++) {
            int index = (int) garantizados.get(i);
            System.out.println(clientes.get(index).getConsumo());
            total += clientes.get(index).getConsumo();
        }
        System.out.println(total);

        System.out.println(centrales.size());
    }
    public static void initializer(Centrales centrales, Clientes clientes) {
        for(int i = 0; i < clientes.size(); i++) {
            if(clientes.get(i).getContrato() == Cliente.GARANTIZADO) garantizados.add(i);
            else noGarantizados.add(i);
        }
        for (int i = 0; i < centrales.size(); i++) centrales.get(i).setProduccion(0.0);
    }

    public static void initState(Centrales centrales, Clientes clientes) {
        int indexCentral = 0;
        for (int i = 0; i < garantizados.size(); i++) {
            int indexClient = (int) garantizados.get(i);
            Cliente client = clientes.get(indexClient);
            double increase = client.getConsumo();
            Central central = centrales.get(indexCentral);
            if(centralCanHaveClient(central, increase)) central.setProduccion(central.getProduccion() + increase);
            else indexCentral++; // NO ESTÀ ACABADO, No funciona
        }
    }

    public static boolean centralCanHaveClient(Central central, double increase) {
        if(central.getTipo() == central.CENTRALA) return (central.getProduccion() + increase) <= 750.0;
        else if (central.getTipo() == central.CENTRALB) return (central.getProduccion() + increase) <= 250.0;
        else return (central.getProduccion() + increase) <= 100.0;
    }
    public static double distance(Cliente cliente, Central central) {
        double dist_x = cliente.getCoordX() - central.getCoordX();
        double dist_y = cliente.getCoordY() - central.getCoordY();
        return sqrt(pow(dist_x,2) + pow(dist_y,2));
    }
}