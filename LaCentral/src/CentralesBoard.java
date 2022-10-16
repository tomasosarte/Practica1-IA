import IA.Energia.Central;
import IA.Energia.Centrales;
import IA.Energia.Cliente;
import IA.Energia.Clientes;
import IA.Energia.VEnergia;

import java.util.ArrayList;

import static java.lang.Math.*;

public class CentralesBoard {

    // VALORES PARA EL ESTADO
    private final Centrales centrales;
    private final Clientes clientes;
    private static ArrayList<Integer> Estado;
    private static ArrayList<Integer> Garantizados;
    private static ArrayList<Integer> NoGarantizados;

    public void initializer() {
        // Inicializar estructuras
        Estado = new ArrayList<>();
        Garantizados = new ArrayList<>();
        NoGarantizados = new ArrayList<>();
        for( int i = 0 ; i < clientes.size(); ++i) {
            //Inicializar Estado y estructuras de clientes
            Estado.add(-1);
            if (clientes.get(i).getContrato() == Cliente.GARANTIZADO) Garantizados.add(i);
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

    public void initState2() {
        // Índice de la central a rellenar y la central
        int indexCentral = 0;
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
            if(centralCanHaveClient(central, increase)) {
                Estado.set(indexClient, indexCentral);
                // Le incrementas la producción a la central correspondiente
                centrales.get(indexCentral).setProduccion(centrales.get(indexCentral).getProduccion() + increase);
            } else {
                int nuevoIndexCentral = 0;
                central = centrales.get(nuevoIndexCentral);
                while (!centralCanHaveClient(central, increase) && nuevoIndexCentral < centrales.size()) {
                    // Incrementa el índice de central a mirar y coge la central
                    ++nuevoIndexCentral;
                    if(indexCentral < centrales.size()) central = centrales.get(nuevoIndexCentral);
                }
                // SE puede añadir cliente a una central
                if (nuevoIndexCentral < centrales.size()) {
                    // Le pones al cliente la central correspondiente
                    Estado.set(indexClient, nuevoIndexCentral);
                    // Le incrementas la producción a la central correspondiente
                    centrales.get(nuevoIndexCentral).setProduccion(centrales.get(nuevoIndexCentral).getProduccion() + increase);
                } else { // Si no hay ninguna central que pueda soportar a los clientes garantizados, no hay solución al problema
                    System.out.println("We have an Impossible situation");
                    break;
                }
            }
            // Augmenter índice central
            indexCentral= ++indexCentral % centrales.size();
            central = centrales.get(indexCentral);
        }
        indexCentral = 0;
        central = centrales.get(indexCentral);
        for (Integer noGarantizado : NoGarantizados) {
            // Índice del cliente garantizado
            int indexClient = noGarantizado;
            // Cliente garantizado
            Cliente client = clientes.get(indexClient);
            // Distancia entre cliente y central
            double distance = distance(client, central);
            // Consumo del cliente garantizado + Pérdida de MW por transporte
            double increase = client.getConsumo() + VEnergia.getPerdida(distance);
            if(centralCanHaveClient(central, increase)) {
                Estado.set(indexClient, indexCentral);
                // Le incrementas la producción a la central correspondiente
                centrales.get(indexCentral).setProduccion(centrales.get(indexCentral).getProduccion() + increase);
            } else {
                int nuevoIndexCentral = 0;
                central = centrales.get(nuevoIndexCentral);
                while (!centralCanHaveClient(central, increase) && nuevoIndexCentral < centrales.size()) {
                    // Incrementa el índice de central a mirar y coge la central
                    ++nuevoIndexCentral;
                    if(indexCentral < centrales.size()) central = centrales.get(nuevoIndexCentral);
                }
                // SE puede añadir cliente a una central
                if (nuevoIndexCentral < centrales.size()) {
                    // Le pones al cliente la central correspondiente e incrementa la producción en la central
                    Estado.set(indexClient, nuevoIndexCentral);
                    central.setProduccion(central.getProduccion()+increase);
                } else break;
            }
            // Augmenter índice central
            indexCentral = ++indexCentral % centrales.size();
            central = centrales.get(indexCentral);
        }
    }
    public void initState() {
        // Índice de la central a rellenar y la central
        int indexCentral = 0;
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
                // Incrementa el índice de central a mirar y cógela si se puede
                ++indexCentral;
                if(indexCentral < centrales.size()) central = centrales.get(indexCentral);
            }
            // SE puede añadir cliente a una central
            if (indexCentral < centrales.size()) {
                // Le pones al cliente la central correspondiente e incrementas la producción en la central
                Estado.set(indexClient, indexCentral);
                central.setProduccion(central.getProduccion()+increase);
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
                // Incrementa el índice de central a mirar y coge la central
                ++indexCentral;
                if(indexCentral < centrales.size()) central = centrales.get(indexCentral);
            }
            // SE puede añadir cliente a una central
            if (indexCentral < centrales.size()) {
                // Incrementa el índice de central a mirar y cógela si se puede
                Estado.set(indexClient, indexCentral);
                central.setProduccion(central.getProduccion()+increase);
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
    public CentralesBoard(Centrales _centrales, Clientes _clientes, ArrayList<Integer> _estado,
                          ArrayList<Integer> _Garantizados, ArrayList<Integer> _NoGarantizados) throws Exception {
        centrales = new Centrales(new int[] {5,10,25}, 1234);
        for (int i = 0; i < _centrales.size(); i++) centrales.get(i).setProduccion(_centrales.get(i).getProduccion());
        clientes = _clientes;
        Estado =  new ArrayList<>(_estado.size());
        Estado.addAll(_estado);
        Garantizados = _Garantizados;
        NoGarantizados = _NoGarantizados;
    }
    // OPERADORES
    public void operadorSwap2Centrales(int indexCliente1, int indexCliente2) {
        // Cogemos los índices de las centrales
        int indexCentralCliente1 = Estado.get(indexCliente1);
        int indexCentralCliente2 = Estado.get(indexCliente2);
        // Cogemos los clientes
        Cliente cliente1 = clientes.get(indexCliente1);
        Cliente cliente2 = clientes.get(indexCliente2);
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
        if (centralCanHaveClient(centralCliente1, consumoCliente2Central1) && centralCanHaveClient(centralCliente2, consumoCliente1Central2)) {
            centrales.get(indexCentralCliente1).setProduccion(nuevoConsumoCentral1);
            centrales.get(indexCentralCliente2).setProduccion(nuevoConsumoCentral2);
            // Cambiamos en el estado las centrales
            Estado.set(indexCliente1, indexCentralCliente2);
            Estado.set(indexCliente2, indexCentralCliente1);
        }
    }

    public void operadorSwapCentralNull(int indexCliente1, int indexCliente2) {
        // Cogemos los índices de las centrales
        int indexCentralCliente1 = Estado.get(indexCliente1);
        int indexCentralCliente2 = Estado.get(indexCliente2);
        // Cogemos los clientes
        Cliente cliente1 = clientes.get(indexCliente1);
        Cliente cliente2 = clientes.get(indexCliente2);
        // Cogemos central
        Central centralCliente2 = centrales.get(indexCentralCliente2);
        // cogemos las distancias nuevas y las anteriores
        double distanciaCliente1Central2 = distance(cliente1, centralCliente2);
        double distanciaCliente2Central2 = distance(cliente2, centralCliente2);
        // Cogemos los consumos totales de las nuevas producciones
        double consumoCliente1Central2 = cliente1.getConsumo() + VEnergia.getPerdida(distanciaCliente1Central2);
        double consumoCliente2Central2 = cliente2.getConsumo() + VEnergia.getPerdida(distanciaCliente2Central2);
        // Actualizamos la producción de las centrales
        double nuevoConsumoCentral2 = centralCliente2.getProduccion() - consumoCliente2Central2 - consumoCliente1Central2;
        if(centralCanHaveClient(centralCliente2, consumoCliente1Central2)) {
            centrales.get(indexCentralCliente2).setProduccion(nuevoConsumoCentral2);
            // Cambiamos en el estado las centrales
            Estado.set(indexCliente1, indexCentralCliente2);
            Estado.set(indexCliente2, indexCentralCliente1);
        }
    }

    public boolean operadorShift(int indexCliente, int indexCentralNueva) {
        // Cogemos el índice de la central anterior
        int indexCentralClienteAnterior = Estado.get(indexCliente);
        // Cogemos el cliente
        Cliente cliente = clientes.get(indexCliente);
        //Cogemos la nueva central
        Central nueva = centrales.get(indexCentralNueva);
        // Calculamos la distancia a la central nueva
        double distanciaClienteCentralNueva = distance(cliente, nueva);
        // Calculamos el consumo a la central nueva
        double consumoClienteCentralNueva = cliente.getConsumo() + VEnergia.getPerdida(distanciaClienteCentralNueva);
        // Calculamos la nueva producción de la central nueva
        double nuevaProdCentralNueva = nueva.getProduccion() + consumoClienteCentralNueva;
        if(indexCentralClienteAnterior != -1 && centralCanHaveClient(nueva, consumoClienteCentralNueva)) {
            // Cogemos la central anterior
            Central anterior = centrales.get(indexCentralClienteAnterior);
            // Calculamos la distancia a la central anterior
            double distanciaClienteCentralAnterior = distance(cliente, anterior);
            // Calculamos el consumo a la central anterior
            double consumoClienteCentralAnterior = cliente.getConsumo() + VEnergia.getPerdida(distanciaClienteCentralAnterior);
            // Calculamos la nueva producción de la central anterior
            double nuevaProdCentralAnterior = anterior.getProduccion() - consumoClienteCentralAnterior;
            // Comprobamos que se pueda aplicar el operador
            anterior.setProduccion(nuevaProdCentralAnterior);
            nueva.setProduccion(nuevaProdCentralNueva);
            Estado.set(indexCliente, indexCentralNueva);
            return true;
        } else if (centralCanHaveClient(nueva, consumoClienteCentralNueva)){
            nueva.setProduccion(nuevaProdCentralNueva);
            Estado.set(indexCliente, indexCentralNueva);
            return true;
        } return false;
    }

    // GETTERS
    public ArrayList<Integer> getState() {
        return Estado;
    }
    public Clientes getClients() {
        return clientes;
    }
    public Centrales getCentrals() {
        return centrales;
    }
    public ArrayList<Integer> getGarantizados() {
        return Garantizados;
    }
    public ArrayList<Integer> getNoGarantizados() {
        return NoGarantizados;
    }
    // para poder sacar las cosas por pantalla
    public String toString(){
        // Rellenar
        ArrayList<Integer> contador = new ArrayList<>();
        for(int i = 0; i < centrales.size(); i++) contador.add(0);
        for (int ce: Estado) contador.set(ce, contador.get(ce)+1);
        StringBuilder ret = new StringBuilder("|");
        for (int i = 0; i < centrales.size(); ++i) {
            ret.append(centrales.get(i).getProduccion()).append("<-->").append(centrales.get(i).getTipo()).
                    append("<-->").append(contador.get(i)).append("|");
        }
        return ret.toString();
    }
}