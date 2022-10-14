import IA.Energia.Centrales;
import IA.Energia.Cliente;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.Vector;

public class CentralesSuccesorFunction implements SuccessorFunction {
    public ArrayList<CentralesBoard> getSuccessors (Object aState){
        ArrayList retVal = new ArrayList<>();
        CentralesBoard board = (CentralesBoard) aState;
        CentralesHeuristicFunction HEUR = new CentralesHeuristicFunction();
        Vector<Integer> estado = board.getState();
/*
        for(int i = 0; i < estado.size(); i++) {
            for(int j = i; j < estado.size(); j++) {
                CentralesBoard newboard = new CentralesBoard(board.getCentrals(), board.getClients(), board.getState(),
                        board.getGarantizados(), board.getNoGarantizados());
                Cliente clientei = newboard.getClients().get(i);
                Cliente clientej = newboard.getClients().get(j);
                if(estado.get(i) != -1 && estado.get(j) != -1 && estado.get(i) != estado.get(j)) {
                    newboard.operadorSwap2Centrales(i, j);
                    retVal.add(new Successor(newboard.toString(), newboard));
                    System.out.println(newboard.toString());
                }
                else if(estado.get(i) == -1 && estado.get(j) != -1 && clientej.getContrato() != Cliente.GARANTIZADO) {
                    newboard.operadorSwapCentralVacio(i, j);
                    retVal.add(new Successor(newboard.toString(), newboard));
                    System.out.println(newboard.toString());
                } else if(estado.get(i) != -1 && estado.get(j) == -1 && clientei.getContrato() != Cliente.GARANTIZADO){
                    newboard.operadorSwapCentralVacio(j, i);
                    retVal.add(new Successor(newboard.toString(), newboard));
                    System.out.println(newboard.toString());
                }
            }
        }
*/
        for(int i = 0; i < estado.size(); i++) {
            if(estado.get(i) != -1) {
                for (int j = 0; j < board.getCentrals().size(); j++) {
                    CentralesBoard newboard = new CentralesBoard(board.getCentrals(), board.getClients(), board.getState(),
                            board.getGarantizados(), board.getNoGarantizados());
                    newboard.operadorShift(i, j);
                    double H = HEUR.getHeuristicValue(newboard);
                    String s = newboard.toString() + "--->" + H;
                    retVal.add(new Successor(s, newboard));
                    System.out.println(s);
                }
            }
        }
        return (retVal);
    }
}