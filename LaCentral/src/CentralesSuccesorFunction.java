import IA.Energia.Centrales;
import IA.Energia.Cliente;
import IA.Energia.Clientes;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.Objects;

public class CentralesSuccesorFunction implements SuccessorFunction {
    long inicio = System.currentTimeMillis();
    public ArrayList<Successor> getSuccessors (Object aState){

        ArrayList<Successor> retVal = new ArrayList<>();
        CentralesBoard board = (CentralesBoard) aState;

        CentralesHeuristicFunction heuristic = new CentralesHeuristicFunction();

        Centrales centrales = board.getCentrals();
        Clientes clientes = board.getClients();
        ArrayList<Integer> estado = board.getState();
        ArrayList<Integer> Garantizados = board.getGarantizados();
        ArrayList<Integer> NoGarantizados = board.getNoGarantizados();

        double initialProfit = - heuristic.getHeuristicValue(board);
        System.out.println("The initial profit of the node is: " + initialProfit);
        System.out.println(board);
        System.out.println();

        for(int i = 0; i < estado.size(); i++) {
            for(int j = i; j < estado.size(); j++) {
                CentralesBoard newBoard;
                try {
                    newBoard = new CentralesBoard(centrales, clientes, estado, Garantizados, NoGarantizados);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                Cliente clienteI = newBoard.getClients().get(i);
                Cliente clienteJ = newBoard.getClients().get(j);
                if(estado.get(i) != -1 && estado.get(j) != -1 && !Objects.equals(estado.get(i), estado.get(j))) {
                    double profit = -heuristic.getHeuristicValue(newBoard);
                    String message = "Swap client " + i + " with client " + j + " have this total profit: " + profit;
                    newBoard.operadorSwap2Centrales(i, j);
                    retVal.add(new Successor(message, newBoard));
                    //System.out.println(message);
                    //System.out.println(newBoard);
                }
                else if(estado.get(i) == -1 && estado.get(j) != -1 && clienteJ.getContrato() != Cliente.GARANTIZADO) {
                    double profit = -heuristic.getHeuristicValue(newBoard);
                    String message = "Swap client " + i + " with client " + j + " have this total profit: " + profit;
                    newBoard.operadorSwapCentralNull(i, j);
                    retVal.add(new Successor(message, newBoard));
                    // System.out.println(message);
                    // System.out.println(newBoard);
                } else if(estado.get(i) != -1 && estado.get(j) == -1 && clienteI.getContrato() != Cliente.GARANTIZADO){
                    double profit = -heuristic.getHeuristicValue(newBoard);
                    String message = "Swap client " + i + " with client " + j + " have this total profit: " + profit;
                    newBoard.operadorSwapCentralNull(j, i);
                    retVal.add(new Successor(message, newBoard));
                    //System.out.println(message);
                    //System.out.println(newBoard);
                }
            }
            //System.out.println();
        }
        /*
        for(int i = 0; i < estado.size(); i++) {
            for (int j = 0; j < board.getCentrals().size(); j++) {
                if(estado.get(i) != j) {
                    CentralesBoard newBoard;
                    try {
                        newBoard = new CentralesBoard(centrales, clientes, estado, Garantizados, NoGarantizados);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    if(newBoard.operadorShift(i, j)) {
                        int centralAnterior = estado.get(i);
                        double profit = -heuristic.getHeuristicValue(newBoard);
                        String message = "Shift client " + i + " from central " + centralAnterior + " to central " + j + " have this total profit: " + profit;
                        retVal.add(new Successor(message, newBoard));
                        System.out.println(message);
                        System.out.println(board);
                        System.out.println(newBoard);
                    }
                }
            }
            //System.out.println();
        }
         */
        return retVal;
    }
}