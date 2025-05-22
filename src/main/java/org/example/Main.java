package org.example;

import java.io.IOException;
import java.util.*;

import static org.example.Graph.*;

public class Main {

    public static void main(String[] args) throws IOException {
       // Temat1();
        //Temat2();


        System.out.println("Random graph v50e100");
        Graph graph = Graph.generateRandomGraph(50, 100);
        System.out.println("DFS rekurencyjny (macierz):");
        graph.convertListToMatrix(); // Upewnij się, że masz aktualną macierz
        boolean[] visitedMatrixRec = new boolean[50];
        graph.dfsRecursiveMatrix(0, visitedMatrixRec); // start od wierzchołka 0

        // 2. DFS rekurencyjny na LIŚCIE
        System.out.println("\nDFS rekurencyjny (lista):");
        graph.convertMatrixToList(); // Upewnij się, że masz aktualną listę
        boolean[] visitedListRec = new boolean[50];
        graph.dfsRecursiveList(0, visitedListRec); // start od wierzchołka 0

        // 3. DFS iteracyjny na MACIERZY
        System.out.println("\nDFS iteracyjny (macierz):");
        graph.convertListToMatrix(); // odśwież reprezentację macierzową
        graph.dfsIterativeMatrix(0);

        // 4. DFS iteracyjny na LIŚCIE
        System.out.println("\nDFS iteracyjny (lista):");
        graph.convertMatrixToList(); // odśwież reprezentację listową
        graph.dfsIterativeList(0);

        System.out.println("Czy graf jest drzewem? " + graph.isTree());
//        System.out.println("Cykl(e) w grafie:");
//        graph.findAllCycles();

        graph.dfsIterativeMatrixWithStackVisualization(0);
        //graph.dfsLimitedDepthMatrix(0, 2); // DFS z ograniczeniem głębokości = 2

        Graph g = new Graph(5);
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 0);
        g.addEdge(1, 3);
        g.addEdge(3, 4);
        g.dfsWithIntervals(0);

//        System.out.println("Czy graf jest drzewem? " + g.isTree());
//        System.out.println("Cykl(e) w grafie:");
//        g.findAllCycles();

    }

    public static void Temat1() throws IOException {
        // tworzenie grafu
        Graph graph = new Graph(5);
        graph.addEdge(0, 1);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(4, 0);

        // tworzenie macierzy
        System.out.println("Macierz sąsiedztwa:");
        graph.printAdjacencyMatrix();

        // tworzenie listy
        graph.convertMatrixToList();
        System.out.println("Lista:");
        graph.printAdjacencyList();

        // zapis do graph6
        graph.saveToGraph6("mojgraf.txt");

        // odczyt z graph6, sprawdzenie czy lista się nie zmieni
        graph = Graph.loadFromGraph6("mojgraf.txt");
        System.out.println("Lista:");
        graph.printAdjacencyList();

        // usuwanie krawędzi
        graph.removeEdge(1, 3);
        System.out.println("Lista:");
        graph.printAdjacencyList();

        // dodawanie wierzchołka
        graph.addVertex();
        System.out.println("Lista:");
        graph.printAdjacencyList();

        // usuwanie wierzchołka
        graph.removeVertex(5);
        System.out.println("Lista:");
        graph.printAdjacencyList();

        // odczyt z pliku
        Graph loadedGraph = Graph.loadFromGraph6("graphbig.txt");
        System.out.println("Lista:");
        loadedGraph.printAdjacencyList();
        loadedGraph.convertListToMatrix();
        System.out.println("Macierz:");
        loadedGraph.printAdjacencyMatrix();
        loadedGraph.convertMatrixToList();
        System.out.println("Lista:");
        loadedGraph.printAdjacencyList();

        // anihilacja
        System.out.println("Liczba anihilacji:");
        System.out.println(loadedGraph.computeAnnihilationNumber());

        // indeks ABC
        System.out.println("Indeks ABC:");
        System.out.println(loadedGraph.computeABCIndex());

        // przecięcia przedziałów
        List<int[]> intervals = Arrays.asList(
                new int[]{1, 5}, new int[]{3, 7}, new int[]{6, 10}, new int[]{6, 12}
        );
        graph.saveIntervalsToFile(intervals, "intervals.txt");
        List<int[]> loadedIntervals = Graph.loadIntervalsFromFile("intervals.txt");
        Graph intervalGraph = Graph.generateIntervalGraph(loadedIntervals);
        System.out.println("Graf przedziałów z pliku:");
        intervalGraph.printAdjacencyList();

        // zliczanie cykli długości 3
        int[][] graph2 = {
                {0, 1, 1, 0},
                {1, 0, 1, 1},
                {1, 1, 0, 1},
                {0, 1, 1, 0}
        };
        System.out.println("Liczba cykli długości 3: " + count3Cycles(graph2));
    }

    public static void Temat2() throws IOException{
        Graph r;
        ColoringStats stats;

        System.out.println("Random graph v50e100");
        r = Graph.generateRandomGraph(50, 100);
        stats = new ColoringStats(r);
        stats.runAll();

        System.out.println("Random graph v50e500");
        r = Graph.generateRandomGraph(50, 500);
        stats = new ColoringStats(r);
        stats.runAll();

        System.out.println("Random graph v50e800");
        r = Graph.generateRandomGraph(50, 800);
        stats = new ColoringStats(r);
        stats.runAll();

        System.out.println("Random graph v200e1600");
        r = Graph.generateRandomGraph(200, 1600);
        stats = new ColoringStats(r);
        stats.runAll();

        System.out.println("Random graph v200e8000");
        r = Graph.generateRandomGraph(200, 8000);
        stats = new ColoringStats(r);
        stats.runAll();

        System.out.println("Random graph v200e12800");
        r = Graph.generateRandomGraph(200, 12800);
        stats = new ColoringStats(r);
        stats.runAll();

        System.out.println("Random graph v500e10000");
        r = Graph.generateRandomGraph(500, 10000);
        stats = new ColoringStats(r);
        stats.runAll();

        System.out.println("Random graph v500e50000");
        r = Graph.generateRandomGraph(500, 50000);
        stats = new ColoringStats(r);
        stats.runAll();

        System.out.println("Random graph v500e80000");
        r = Graph.generateRandomGraph(500, 80000);
        stats = new ColoringStats(r);
        stats.runAll();

        System.out.println("Dimacs C2000.5");
        r = Graph.loadFromDimacs("C2000.5.txt");
        stats = new ColoringStats(r);
        stats.runAll();

        System.out.println("Dimacs dsjc250.5");
        r = Graph.loadFromDimacs("dsjc250.5.txt");
        stats = new ColoringStats(r);
        stats.runAll();

        System.out.println("Dimacs dsjc500.5");
        r = Graph.loadFromDimacs("dsjc500.5.txt");
        stats = new ColoringStats(r);
        stats.runAll();

        System.out.println("Dimacs dsjc1000.1");
        r = Graph.loadFromDimacs("dsjc1000.1.txt");
        stats = new ColoringStats(r);
        stats.runAll();

        System.out.println("Dimacs dsjc1000.5");
        r = Graph.loadFromDimacs("dsjc1000.5.txt");
        stats = new ColoringStats(r);
        stats.runAll();

        System.out.println("Dimacs dsjc1000.9");
        r = Graph.loadFromDimacs("dsjc1000.9.txt");
        stats = new ColoringStats(r);
        stats.runAll();

        System.out.println("Dimacs dsjr500.5");
        r = Graph.loadFromDimacs("dsjr500.5.txt");
        stats = new ColoringStats(r);
        stats.runAll();

        System.out.println("Dimacs flat1000_76_0");
        r = Graph.loadFromDimacs("flat1000_76_0.txt");
        stats = new ColoringStats(r);
        stats.runAll();

        System.out.println("Dimacs r1000.5");
        r = Graph.loadFromDimacs("r1000.5.txt");
        stats = new ColoringStats(r);
        stats.runAll();
    }
}
