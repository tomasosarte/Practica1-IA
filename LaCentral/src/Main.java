import IA.Energia.Centrales;
import IA.Energia.Central;
import IA.Energia.Cliente;
import IA.Energia.Clientes;
import IA.Energia.VEnergia;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;

import java.util.*;

import static java.lang.Math.*;

public class Main {
    public static Vector<Integer> estado;
    public static Vector<Integer> garantizados;
    public static Vector<Integer> noGarantizados;

    public static void main(String[] args) throws Exception {
        Centrales centrales = new Centrales(new int[] {1,1,2}, 1234);
        Clientes clientes = new Clientes(100, new double[] {0.25, 0.3, 0.45}, 0.75, 1234);

        CentralesBoard centralesBoard = new CentralesBoard(centrales, clientes);
        CentralesHeuristicFunction HEUR = new CentralesHeuristicFunction();
        double H = HEUR.getHeuristicValue(centralesBoard);
        System.out.println(H);
        Problem p = new Problem(
                centralesBoard,
                new CentralesSuccesorFunction(),
                new CentralesGoalTest(),
                new CentralesHeuristicFunction()
        );

        Search alg_hill = new HillClimbingSearch();
        //SimulatedAnnealingSearch search = new SimulatedAnnealingSearch();
        SearchAgent agent = new SearchAgent(p,alg_hill);

        System.out.println();
        printActions(agent.getActions());
        printInstrumentation(agent.getInstrumentation());
    }

    private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }

    }

    private static void printActions(List actions) {
        for (int i = 0; i < actions.size(); i++) {
            String action = (String) actions.get(i);
            System.out.println(action);
        }
    }

}
