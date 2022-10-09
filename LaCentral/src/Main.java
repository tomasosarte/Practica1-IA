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

        System.out.println(estado.size());
        System.out.println(estado);

        initState(centrales, clientes);

        System.out.println(estado);
    }
    public static void initializer(Centrales centrales, Clientes clientes) {
        estado.setSize(clientes.size());
        for (int i = 0; i < estado.size(); i++) estado.set(i, -1);

        for(int i = 0; i < clientes.size(); i++) {
            if(clientes.get(i).getContrato() == Cliente.GARANTIZADO) garantizados.add(i);
            else noGarantizados.add(i);
        }
        for (int i = 0; i < centrales.size(); i++) centrales.get(i).setProduccion(0.0);
    }

    public static void initState(Centrales centrales, Clientes clientes) {
        int indexCentral = 0;
        Central central = centrales.get(indexCentral);
        for (int i = 0; i < garantizados.size(); i++) {
            int indexClient = (int) garantizados.get(i);
            Cliente client = clientes.get(indexClient);
            double increase = client.getConsumo();
            while(!centralCanHaveClient(central, increase) && indexCentral < centrales.size()) {
                ++indexCentral;
                central = centrales.get(indexCentral);
            }
            if (indexCentral < centrales.size()) {
                estado.set(indexClient, indexCentral);
            } else {
                System.out.println("We have an Impossible situation");
                break;
            }
        }

        for (int i = 0; i < noGarantizados.size(); i++) {
            int indexClient = (int) noGarantizados.get(i);
            Cliente client = clientes.get(indexClient);
            double increase = client.getConsumo();
            while(!centralCanHaveClient(central, increase) && indexCentral < centrales.size()) {
                ++indexCentral;
                central = centrales.get(indexCentral);
            }
            if (indexCentral < centrales.size()) {
                estado.set(indexClient, indexCentral);
            } else break;

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

    public static void operadorSwap(int indexCliente1, int indexCliente2) {
        int indexCentralCliente1 = (int) estado.get(indexCliente1);
        int indexCentralCliente2 = (int) estado.get(indexCliente2);
        estado.set(indexCliente1, indexCentralCliente2);
        estado.set(indexCliente2, indexCentralCliente1);
    }
}