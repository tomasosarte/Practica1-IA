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
    private static ArrayList<Contrato> Estado;
    private static ArrayList<Integer> Garantizados;
    private static ArrayList<Integer> NoGarantizados;

    public void initializer() {
        // Inicializar estructuras
        Estado = new ArrayList<>();
        Garantizados = new ArrayList<>();
        NoGarantizados = new ArrayList<>();
        for( int i = 0 ; i < clientes.size(); ++i) {
            //Inicializar Estado y estructuras de clientes
            Estado.add(new Contrato(-1,0));
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
            // Índice del cliente garantizado y el cliente
            int indexClient = garantizado;
            Cliente client = clientes.get(indexClient);
            // Cogemos el contrato
            Contrato contrato = Estado.get(indexClient);
            // Calculamos el incremento en la central
            double distance = distance(client, central);
            double consumo = client.getConsumo() + VEnergia.getPerdida(distance);
            if(centralCanHaveClient(central, consumo)) {
                // Cambiamos el contrato y la producción de la central
                contrato.set_central(indexCentral);
                contrato.set_consumo(consumo);
                central.setProduccion(central.getProduccion()+consumo);
            } else {
                int nuevoIndexCentral = 0;
                central = centrales.get(nuevoIndexCentral);
                while (!centralCanHaveClient(central, consumo) && nuevoIndexCentral < centrales.size()) {
                    // Incrementa el índice de central a mirar y coge la central
                    ++nuevoIndexCentral;
                    if(indexCentral < centrales.size()) central = centrales.get(nuevoIndexCentral);
                }
                // SE puede añadir cliente a una central
                if (nuevoIndexCentral < centrales.size()) {
                    // Cambiamos el contrato y la producción de la central
                    contrato.set_central(indexCentral);
                    contrato.set_consumo(consumo);
                    central.setProduccion(central.getProduccion()+consumo);
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
            // Índice del cliente garantizado y el cliente
            int indexClient = noGarantizado;
            Cliente client = clientes.get(indexClient);
            // Cogemos el contrato
            Contrato contrato = Estado.get(indexClient);
            // Calculamos el consumo
            double distance = distance(client, central);
            double consumo = client.getConsumo() + VEnergia.getPerdida(distance);
            if(centralCanHaveClient(central, consumo)) {
                // Cambiamos el contrato y la producción de la central
                contrato.set_central(indexCentral);
                contrato.set_consumo(consumo);
                central.setProduccion(central.getProduccion()+consumo);
            } else {
                int nuevoIndexCentral = 0;
                central = centrales.get(nuevoIndexCentral);
                while (!centralCanHaveClient(central, consumo) && nuevoIndexCentral < centrales.size()) {
                    // Incrementa el índice de central a mirar y coge la central
                    ++nuevoIndexCentral;
                    if(indexCentral < centrales.size()) central = centrales.get(nuevoIndexCentral);
                }
                // SE puede añadir cliente a una central
                if (nuevoIndexCentral < centrales.size()) {
                    // Cambiamos el contrato y la producción de la central
                    contrato.set_central(indexCentral);
                    contrato.set_consumo(consumo);
                    central.setProduccion(central.getProduccion()+consumo);
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
            // Índice del cliente garantizado y el cliente
            int indexClient = garantizado;
            Cliente client = clientes.get(indexClient);
            // Cogemos el contrato
            Contrato contrato = Estado.get(indexClient);
            // Calculamos el consumo
            double distance = distance(client, central);
            double consumo = client.getConsumo() + VEnergia.getPerdida(distance);
            // Encuentra la primera central en el vector que puede soportar el incremento de MW en producción
            while (!centralCanHaveClient(central, consumo) && indexCentral < centrales.size()) {
                // Incrementa el índice de central a mirar y cógela si se puede
                ++indexCentral;
                if(indexCentral < centrales.size()) central = centrales.get(indexCentral);
            }
            // SE puede añadir cliente a una central
            if (indexCentral < centrales.size()) {
                // Cambiamos el contrato y la producción de la central
                contrato.set_central(indexCentral);
                contrato.set_consumo(consumo);
                central.setProduccion(central.getProduccion()+consumo);
            } else { // Si no hay ninguna central que pueda soportar a los clientes garantizados, no hay solución al problema
                System.out.println("We have an Impossible situation");
                break;
            }
        }
        // Iteraciones dentro de la estructura de clientes con contrato NO GARANTIZADO
        for (Integer noGarantizado : NoGarantizados) {
            // Índice de cliente y el cliente
            int indexClient = noGarantizado;
            Cliente client = clientes.get(indexClient);
            // Cogemos el contrato
            Contrato contrato = Estado.get(indexClient);
            // Calculamos el consumo
            double distance = distance(client, central);
            double consumo = client.getConsumo() + VEnergia.getPerdida(distance);
            // Encuentra la primera central en el vector que puede soportar el incremento de MW en producción
            while (!centralCanHaveClient(central, consumo) && indexCentral < centrales.size()) {
                // Incrementa el índice de central a mirar y coge la central
                ++indexCentral;
                if(indexCentral < centrales.size()) central = centrales.get(indexCentral);
            }
            // SE puede añadir cliente a una central
            if (indexCentral < centrales.size()) {
                // Cambiamos el contrato y la producción de la central
                contrato.set_central(indexCentral);
                contrato.set_consumo(consumo);
                central.setProduccion(central.getProduccion()+consumo);
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
    public CentralesBoard(Centrales _centrales, Clientes _clientes, ArrayList<Contrato> _estado,
                          ArrayList<Integer> _Garantizados, ArrayList<Integer> _NoGarantizados) throws Exception {
        centrales = new Centrales(new int[] {5,10,25}, 1234);
        for (int i = 0; i < _centrales.size(); i++) centrales.get(i).setProduccion(_centrales.get(i).getProduccion());
        clientes = _clientes;
        Estado =  new ArrayList<>();
        for (Contrato contrato : _estado) Estado.add(new Contrato(contrato.get_central(), contrato.get_prod()));
        Garantizados = _Garantizados;
        NoGarantizados = _NoGarantizados;
    }
    // OPERADORES
    public void operadorSwap2Centrales(int indexCliente1, int indexCliente2) {
        Cliente cliente1 = clientes.get(indexCliente1);
        Contrato contrato1 = Estado.get(indexCliente1);

        Cliente cliente2 = clientes.get(indexCliente2);
        Contrato contrato2 = Estado.get(indexCliente2);

        int indexC1 = contrato1.get_central();
        int indexC2 = contrato2.get_central();
        Central central1 = centrales.get(indexC1);
        Central central2 = centrales.get(indexC2);

        // cogemos las distancias nuevas y las anteriores
        double distanciaCliente1Central2 = distance(cliente1, central2);
        double distanciaCliente2Central1 = distance(cliente2, central1);

        // Cogemos los consumos totales de las nuevas producciones
        double consumoC1C1 = contrato1.get_prod();
        double consumoC2C2 = contrato2.get_prod();
        double consumoC1C2 = cliente1.getConsumo() + VEnergia.getPerdida(distanciaCliente1Central2);
        double consumoC2C1 = cliente2.getConsumo() + VEnergia.getPerdida(distanciaCliente2Central1);
        // Actualizamos la producción de las centrales
        double increaseC1 = - consumoC1C1 + consumoC2C1;
        double increaseC2 = - consumoC2C2 - consumoC1C2;
        // Se ha de cumplir la condición de que las nuevas centrales puedan soportar a los nuevos clientes
        if (centralCanHaveClient(central1, increaseC1) && centralCanHaveClient(central2, increaseC2)) {
            central1.setProduccion(central1.getProduccion()+increaseC1);
            central2.setProduccion(central2.getProduccion()+increaseC2);

            contrato1.set_consumo(consumoC1C2);
            contrato1.set_central(indexC2);

            contrato2.set_consumo(consumoC2C1);
            contrato2.set_central(indexC1);
        }
    }
    public void operadorSwapCentralNull(int indexCliente1, int indexCliente2) {
        Cliente cliente1 = clientes.get(indexCliente1);
        Contrato contrato1 = Estado.get(indexCliente1);

        Contrato contrato2 = Estado.get(indexCliente2);

        int indexC2 = contrato2.get_central();
        Central central2 = centrales.get(indexC2);

        // cogemos las distancia
        double distanciaCliente1Central2 = distance(cliente1, central2);
        // Cogemos los consumos totales de las nuevas producciones
        double consumoCliente1Central2 = cliente1.getConsumo() + VEnergia.getPerdida(distanciaCliente1Central2);
        double consumoCliente2Central2 = contrato2.get_prod();
        // Actualizamos la producción de las centrales
        double increaseC2 = - consumoCliente2Central2 + consumoCliente1Central2;
        if(centralCanHaveClient(central2, increaseC2)) {
            central2.setProduccion(central2.getProduccion()+increaseC2);

            contrato1.set_consumo(increaseC2);
            contrato1.set_central(indexC2);

            contrato2.set_consumo(0);
            contrato2.set_central(-1);
        }
    }
    /*
    public void operadorSwap2Centrales(int indexCliente1, int indexCliente2) {
        // Cogemos los índices de las centrales
        int indexCentralCliente1 = Estado.get(indexCliente1).get_central();
        int indexCentralCliente2 = Estado.get(indexCliente2).get_central();
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
            Estado.get(indexCliente1).set_central(indexCentralCliente2);
            Estado.get(indexCliente2).set_central(indexCentralCliente1);
        }
    }
    */
    /*
    public void operadorSwapCentralNull(int indexCliente1, int indexCliente2) {
        // Cogemos los índices de las centrales
        int indexCentralCliente1 = Estado.get(indexCliente1).get_central();
        int indexCentralCliente2 = Estado.get(indexCliente2).get_central();
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
            Estado.get(indexCliente1).set_central(indexCentralCliente2);
            Estado.get(indexCliente2).set_central(indexCentralCliente1);
        }
    }
*/
    public boolean operadorShift(int indexCliente, int indexCentralNueva) {
        // Cogemos el índice de la central anterior, el cliente y la central nueva
        Contrato contrato = Estado.get(indexCliente);
        Cliente cliente = clientes.get(indexCliente);
        Central nueva = centrales.get(indexCentralNueva);
        // Calculamos la distancia a la central nueva y el aumento de esta misma
        double distanciaClienteCentralNueva = distance(cliente, nueva);
        double increaseCN = cliente.getConsumo() + VEnergia.getPerdida(distanciaClienteCentralNueva);
        if(centralCanHaveClient(nueva, increaseCN)) {
            if(contrato.get_central() != -1) {
                // Cogemos la central anterior y el consumo del cliente en esta misma y asignamos la nueva producción de la central anterior
                Central anterior = centrales.get(contrato.get_central());
                double decreaseCA = contrato.get_prod();
                anterior.setProduccion(anterior.getProduccion()-decreaseCA);
            }
            // Asignamos el nuevo contrato y la nueva producción
            nueva.setProduccion(nueva.getProduccion()+increaseCN);
            contrato.set_central(indexCentralNueva);
            return true;
        } return false;
    }

    // GETTERS
    public ArrayList<Contrato> getState() {
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
        for (Contrato ce: Estado) contador.set(ce.get_central(), contador.get(ce.get_central())+1);
        StringBuilder ret = new StringBuilder("|");
        for (int i = 0; i < centrales.size(); ++i) {
            ret.append(centrales.get(i).getProduccion()).append("<-->").append(centrales.get(i).getTipo()).
                    append("<-->").append(contador.get(i)).append("|");
        }
        return ret.toString();
    }
}