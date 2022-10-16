import IA.Energia.Centrales;
import IA.Energia.Clientes;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        long inicio = System.currentTimeMillis();
        Centrales centrales = new Centrales(new int[] {5,10,25}, 1234);
        Clientes clientes = new Clientes(1000, new double[] {0.25, 0.3, 0.45}, 0.75, 1234);

        CentralesBoard centralesBoard = new CentralesBoard(centrales, clientes);
        CentralesHillClimbingSearch(centralesBoard);
        //CentralesSimulatedAnnealingSearch(centralesBoard);
        long fin = System.currentTimeMillis();
        double tiempo = (double) ((fin - inicio)/1000);
        System.out.println(tiempo +" segundos");
    }

    private static void CentralesHillClimbingSearch(CentralesBoard centralesBoard) {
        System.out.println("\nTSP HillClimbing  -->");
        try {
            Problem problem =  new Problem(centralesBoard,new CentralesSuccesorFunction(), new CentralesGoalTest(),new CentralesHeuristicFunction());
            Search search =  new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem,search);

            System.out.println();
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void CentralesSimulatedAnnealingSearch(CentralesBoard centralesBoard) {
        System.out.println("\nTSP Simulated Annealing  -->");
        try {
            Problem problem =  new Problem(centralesBoard,new CentralesSuccesorFunction(), new CentralesGoalTest(),new CentralesHeuristicFunction());
            SimulatedAnnealingSearch search =  new SimulatedAnnealingSearch(100,100,5,0.001);
            //search.traceOn();
            SearchAgent agent = new SearchAgent(problem,search);

            System.out.println();
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printInstrumentation(Properties properties) {
        for (Object o : properties.keySet()) {
            String key = (String) o;
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }

    }

    private static void printActions(List<?> actions) {
        for (Object o : actions) {
            String action = o.toString();
            System.out.println(action);
        }
    }

}
