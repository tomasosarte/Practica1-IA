import IA.Energia.*;
import aima.search.framework.HeuristicFunction;

import java.util.ArrayList;

import static java.lang.Math.max;

public class CentralesHeuristicFunction implements HeuristicFunction {

    public boolean equals(Object obj) {
        boolean retValue;

        retValue = super.equals(obj);
        return retValue;
    }
    public double realProduction (Central central) {
        if(central.getTipo() == Central.CENTRALA) return max(250.0, central.getProduccion());
        else if(central.getTipo() == Central.CENTRALB) return max(100.0, central.getProduccion());
        else return max(10.0, central.getProduccion());
    }

    public double calculateProfit(Clientes clientes, Centrales centrales, ArrayList<Integer> Estado) throws Exception {
        double profit = 0.0;
        for(int i = 0; i < Estado.size(); i++) {
            int indexCentral = Estado.get(i);
            // INDEMNIZAR
            if(indexCentral == -1) {
                profit -= clientes.get(i).getConsumo() * VEnergia.getTarifaClientePenalizacion(clientes.get(i).getTipo());
            } else {
                boolean garantizado = clientes.get(i).getContrato() == Cliente.GARANTIZADO;
                if(garantizado)  profit += clientes.get(i).getConsumo() * VEnergia.getTarifaClienteGarantizada(clientes.get(i).getTipo());
                else profit += clientes.get(i).getConsumo() * VEnergia.getTarifaClienteNoGarantizada(clientes.get(i).getTipo());
            }
        }

        for (Central central : centrales) {
            double production = central.getProduccion();
            int type = central.getTipo();
            if (production <= 0.1) {
                profit -= VEnergia.getCosteParada(type);
            } else {
                profit -= realProduction(central) * VEnergia.getCosteProduccionMW(type) + VEnergia.getCosteMarcha(type);
            }
        }
        //System.out.println(profit);
        return -profit;
    }

    public double getHeuristicValue(Object state) {
        CentralesBoard board = (CentralesBoard) state;
        ArrayList<Integer> Estado = board.getState();
        Clientes clientes = board.getClients();
        Centrales centrales = board.getCentrals();
        try {
            return calculateProfit(clientes, centrales, Estado);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}