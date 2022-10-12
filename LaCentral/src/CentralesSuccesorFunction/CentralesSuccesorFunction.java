import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;

public class CentralesSuccesorFunction implements SuccessorFunction {
    public ArrayList<CentralesBoard> getSuccessors (Object aState){
        ArrayList<CentralesBoard> retVal = new ArrayList<>();
        CentralesBoard board  = (CentralesBoard) aState;

        //Implementar generadora de hijos
        /*
            // 0-> NO ASIGNADO
            // 1 -> ASIGNADO TIPO A
            // 2 -> ASIGNADO TIPO B
            // 3 -> ASIGNADO TIPO C
            // 4 ...
            for(int i = 0 ; i < Numero Clientes; ++i){
                Cliente c = cliente.get(i)
                if( producción de la Central(i)+ producción(c) <= Capacidad de la central){
                    aumentar en 1 la central
                 CentralesBoard newboard = new CentralesBoard(board);
                        --newboard.change(i)
                        -- newboard.swap(i)
                 retVal.add(new Succesor(newboard.toString(),newboard)));
            }

        */
        return (retVal);
    }
}