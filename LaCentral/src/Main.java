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
        Centrales centrales = new Centrales(new int[] {1,1,1}, 0);
        Clientes clientes = new Clientes(100, new double[] { 0.3, 0.4,0.3}, 0.5, 0);

        CentralesBoard centralesBoard = new CentralesBoard(centrales, clientes);

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
    }

    private static void printInstrumentation(Properties properties) {
        for (Object o : properties.keySet()) {
            String key = (String) o;
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }

    }

    private static void printActions(List actions) {
        for (Object o : actions) {
            String action = (String) o;
            System.out.println(action);
        }
    }

}
