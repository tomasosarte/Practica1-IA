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
    private static Vector<Integer> Estado = new Vector<Integer>();
    private static Vector<Integer> Garantizados = new Vector<Integer>();
    private static Vector<Integer> NoGarantizados = new Vector<Integer>();

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
        // Cogemos los índices de las centrales
        int indexCentralCliente1 = Estado.get(indexCliente1);
        int indexCentralCliente2 = Estado.get(indexCliente2);
        // Cogemos los clientes
        Cliente cliente1 = clientes.get(indexCliente1);
        Cliente cliente2 = clientes.get(indexCliente2);
        // Condición de que los dos clientes están en centrales
        if(indexCentralCliente1 != -1 && indexCentralCliente2 != -1) {
            // Cogemos centrales
            Central centralCliente1 = centrales.get(indexCentralCliente1);
            Central centralCliente2 = centrales.get(indexCentralCliente2);
            // cogemos las distancias nuevas y las anteriores
            double distanciaCliente1Central2 = distance(cliente1, centralCliente2);
            double distanciaCliente2Central1 = distance(cliente2, centralCliente1);
            double distanciaCliente1Central1 = distance(cliente1, centralCliente1);
            double distanciaCliente2Central2 = distance(cliente2, centralCliente2);
            // Cogemos los consumos totales de las nuevas producciones
            double consumoCliente1Central1 = cliente1.getConsumo() + VEnergia.getPerdida(distanciaCliente1Central1);
            double consumoCliente1Central2 = cliente1.getConsumo() + VEnergia.getPerdida(distanciaCliente1Central2);
            double consumoCliente2Central2 = cliente2.getConsumo() + VEnergia.getPerdida(distanciaCliente2Central2);
            double consumoCliente2Central1 = cliente2.getConsumo() + VEnergia.getPerdida(distanciaCliente2Central1);
            // Actualizamos la producción de las centrales
            double nuevoConsumoCentral1 = centralCliente1.getProduccion() - consumoCliente1Central1 + consumoCliente2Central1;
            double nuevoConsumoCentral2 = centralCliente2.getProduccion() - consumoCliente2Central2 - consumoCliente1Central2;
            // Se ha de cumplir la condición de que las nuevas centrales puedan soportar a los nuevos clientes
            if(centralCanHaveClient(centralCliente1, nuevoConsumoCentral1) && centralCanHaveClient(centralCliente2, nuevoConsumoCentral2)) {
                centrales.get(indexCentralCliente1).setProduccion(nuevoConsumoCentral1);
                centrales.get(indexCentralCliente2).setProduccion(nuevoConsumoCentral2);
                // Cambiamos en el estado las centrales
                Estado.set(indexCliente1, indexCentralCliente2);
                Estado.set(indexCliente2, indexCentralCliente1);
            }
        }
        // Condición de que un cliente1 no tiene central, pero no salimos del espacio de soluciones (cliente2 != GARANTIZADO)
        else if(indexCentralCliente1 == -1 && indexCentralCliente2 != -1 && cliente2.getContrato() != Cliente.GARANTIZADO) {
            // Cogemos centrales
            Central centralCliente2 = centrales.get(indexCentralCliente2);
            // cogemos las distancias nuevas y las anteriores
            double distanciaCliente1Central2 = distance(cliente1, centralCliente2);
            double distanciaCliente2Central2 = distance(cliente2, centralCliente2);
            // Cogemos los consumos totales de las nuevas producciones
            double consumoCliente1Central2 = cliente1.getConsumo() + VEnergia.getPerdida(distanciaCliente1Central2);
            double consumoCliente2Central2 = cliente2.getConsumo() + VEnergia.getPerdida(distanciaCliente2Central2);
            // Actualizamos la producción de las centrales
            double nuevoConsumoCentral2 = centralCliente2.getProduccion() - consumoCliente2Central2 - consumoCliente1Central2;
            if(centralCanHaveClient(centralCliente2, nuevoConsumoCentral2)) {
                centrales.get(indexCentralCliente2).setProduccion(nuevoConsumoCentral2);
                // Cambiamos en el estado las centrales
                Estado.set(indexCliente1, indexCentralCliente2);
                Estado.set(indexCliente2, indexCentralCliente1);
            }
        }
        // Condición de que un cliente2 no tiene central, pero no salimos del espacio de soluciones (cliente1 != GARANTIZADO)
        else if(indexCentralCliente1 != -1 && indexCentralCliente2 == -1 && cliente1.getContrato() != Cliente.GARANTIZADO) {
            // Cogemos centrales
            Central centralCliente1 = centrales.get(indexCentralCliente2);
            // cogemos las distancias nuevas y las anteriores
            double distanciaCliente2Central1 = distance(cliente2, centralCliente1);
            double distanciaCliente1Central1 = distance(cliente1, centralCliente1);
            // Cogemos los consumos totales de las nuevas producciones
            double consumoCliente1Central1 = cliente1.getConsumo() + VEnergia.getPerdida(distanciaCliente1Central1);
            double consumoCliente2Central1 = cliente2.getConsumo() + VEnergia.getPerdida(distanciaCliente2Central1);
            // Actualizamos la producción de las centrales
            double nuevoConsumoCentral1 = centralCliente1.getProduccion() - consumoCliente1Central1 + consumoCliente2Central1;
            if(centralCanHaveClient(centralCliente1, nuevoConsumoCentral1)) {
                centrales.get(indexCentralCliente1).setProduccion(nuevoConsumoCentral1);
                // Cambiamos en el estado las centrales
                Estado.set(indexCliente1, indexCentralCliente2);
                Estado.set(indexCliente2, indexCentralCliente1);
            }
        }
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
    // Determina si el estado és solución o no
    public boolean isSolution() {
        for (Integer garantizado: Garantizados) {
            if (Estado.get(garantizado) == -1) return false;
        }
        return true;
    }
}