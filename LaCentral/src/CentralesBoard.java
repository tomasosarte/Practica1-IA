import IA.Energia.Central;
import IA.Energia.Centrales;
import IA.Energia.Cliente;
import IA.Energia.Clientes;
import IA.Energia.VEnergia;

import java.util.Collections;
import java.util.Vector;

import static java.lang.Math.*;

public class CentralesBoard {

    // VALORES PARA EL ESTADO
    private final Centrales centrales;
    private final Clientes clientes;
    private static Vector<Integer> Estado;
    private static Vector<Integer> Garantizados;
    private static Vector<Integer> NoGarantizados;

    public void initializer() {
        // Size del estado a número de clientes
        Estado.setSize(clientes.size());
        // Inicializar el estado con cada cliente asignado a ninguna central
        Collections.fill(Estado, -1);
        // Dividir clientes según su contrato
        for(int i = 0; i < clientes.size(); i++) {
            if(clientes.get(i).getContrato() == Cliente.GARANTIZADO) Garantizados.add(i);
            else NoGarantizados.add(i);
        }
        // Set de la producción de las centrales a 0
        for (Central central : centrales) central.setProduccion(0.0);
    }
    public boolean centralCanHaveClient(Central central, double increase) {
        if(central.getTipo() == Central.CENTRALA) return (central.getProduccion() + increase) <= 750.0;
        else if (central.getTipo() == Central.CENTRALB) return (central.getProduccion() + increase) <= 250.0;
        else return (central.getProduccion() + increase) <= 100.0;
    }
    public double distance(Cliente cliente, Central central) {
        double dist_x = cliente.getCoordX() - central.getCoordX();
        double dist_y = cliente.getCoordY() - central.getCoordY();
        return sqrt(pow(dist_x,2) + pow(dist_y,2));
    }
    public void initState() {
        // Índice de la central a rellenar
        int indexCentral = 0;
        // Central a rellenar
        Central central = centrales.get(indexCentral);
        // Iteraciones dentro de la estructura de clientes con contrato garantizado
        for (Integer garantizado : Garantizados) {
            // Índice del cliente garantizado
            int indexClient = garantizado;
            // Cliente garantizado
            Cliente client = clientes.get(indexClient);
            // Distancia entre cliente y central
            double distance = distance(client, central);
            // Consumo del cliente garantizado + Pérdida de MW por transporte
            double increase = client.getConsumo() + VEnergia.getPerdida(distance);
            // Encuentra la primera central en el vector que puede soportar el incremento de MW en producción
            while (!centralCanHaveClient(central, increase) && indexCentral < centrales.size()) {
                // Incrementa el índice de central a mirar
                ++indexCentral;
                // Coge la central
                central = centrales.get(indexCentral);
            }
            // SE puede añadir cliente a una central
            if (indexCentral < centrales.size()) {
                // Le pones al cliente la central correspondiente
                Estado.set(indexClient, indexCentral);
                // Le incrementas la producción a la central correspondiente
                centrales.get(indexCentral).setProduccion(centrales.get(indexCentral).getProduccion() + increase);
            } else { // Si no hay ninguna central que pueda soportar a los clientes garantizados, no hay solución al problema
                System.out.println("We have an Impossible situation");
                break;
            }
        }
        // Iteraciones dentro de la estructura de clientes con contrato NO GARANTIZADO
        for (Integer noGarantizado : NoGarantizados) {
            // Índice de cliente
            int indexClient = noGarantizado;
            // Cliente
            Cliente client = clientes.get(indexClient);
            // Distancia entre cliente y central
            double distance = distance(client, central);
            // Consumo del cliente garantizado + Pérdida de MW por transporte
            double increase = client.getConsumo() + VEnergia.getPerdida(distance);
            // Encuentra la primera central en el vector que puede soportar el incremento de MW en producción
            while (!centralCanHaveClient(central, increase) && indexCentral < centrales.size()) {
                // Incrementa el índice de central a mirar
                ++indexCentral;
                // Coge la central
                central = centrales.get(indexCentral);
            }
            // SE puede añadir cliente a una central
            if (indexCentral < centrales.size()) {
                // Le pones al cliente la central correspondiente
                Estado.set(indexClient, indexCentral);
                // Le incrementas la producción a la central correspondiente
                centrales.get(indexCentral).setProduccion(centrales.get(indexCentral).getProduccion() + increase);
            } else break; // Para de iterar porque no puedes poner más clientes
        }
    }
    // CREADORA => CREADORA DEL ESTADO INICIAL
    public CentralesBoard(Centrales _centrales, Clientes _clientes) {
        centrales = _centrales;
        clientes = _clientes;
        // INICIALIZAR ESTRUCTURAS NECESARIAS
        initializer();
        // CREADORA del estado inicial:
        initState();
    }
    // CREADORA => COPIA DEL ESTADO ANTERIOR
    public CentralesBoard(Centrales _centrales, Clientes _clientes, Vector<Integer> _estado, Vector<Integer> _Garantizados , Vector<Integer> _NoGarantizados){
        centrales = _centrales;
        clientes = _clientes;
        // COPIA DEL ANTERIOR
        Estado = _estado;
        Garantizados = _Garantizados;
        NoGarantizados = _NoGarantizados;
    }
    // OPERADORES
    public void operadorSwap(int indexCliente1, int indexCliente2) {
        int indexCentralCliente1 = Estado.get(indexCliente1);
        int indexCentralCliente2 = Estado.get(indexCliente2);
        Estado.set(indexCliente1, indexCentralCliente2);
        Estado.set(indexCliente2, indexCentralCliente1);
    }

    // GETTERS
    public Vector<Integer> getState() {
        return Estado;
    }
    public Clientes getClients() {
        return clientes;
    }
    public Centrales getCentrals() {
        return centrales;
    }

    // para poder sacar las cosas por pantalla
    public String toString(){
        // Rellenar
        return ("|");
    }
}