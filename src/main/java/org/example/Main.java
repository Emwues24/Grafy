package org.example;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.example.Graph.*;

public class Main {

    public static void main(String[] args) throws IOException {
       // Temat1();

        System.out.println("Random graph:");
        Graph r = Graph.generateRandomGraph(500, 80000);
        Map<Integer, Integer> greedyColors = r.greedyColoring();
        System.out.println("Greedy: " + greedyColors);
        System.out.println("najwiekszy kolor: " + getMaxColor(greedyColors));

        Map<Integer, Integer> lfColors =r.lfColoring();
        System.out.println("LF: " + lfColors);
        System.out.println("najwieksze kolor: " + getMaxColor(lfColors));

        Map<Integer, Integer> slowColors = r.slowColoring();
        System.out.println("SLow: " + slowColors);
        System.out.println("najwieksze kolor: " + getMaxColor(lfColors));

        System.out.println("Dimacs C2000.5:");
        Graph d = Graph.loadFromDimacs("C2000.5.txt");
        greedyColors = d.greedyColoring();
        System.out.println("Greedy: " + greedyColors);
        System.out.println("najwiekszy kolor: " + getMaxColor(greedyColors));

        lfColors = d.lfColoring();
        System.out.println("LF: " + lfColors);
        System.out.println("najwieksze kolor: " + getMaxColor(lfColors));

        slowColors = d.slowColoring();
        System.out.println("SLow: " + slowColors);
        System.out.println("najwieksze kolor: " + getMaxColor(lfColors));

        System.out.println("Dimacs flat_76_0:");
        d = Graph.loadFromDimacs("flat1000_76_0.txt");
        greedyColors = d.greedyColoring();
        System.out.println("Greedy: " + greedyColors);
        System.out.println("najwiekszy kolor: " + getMaxColor(greedyColors));

        lfColors = d.lfColoring();
        System.out.println("LF: " + lfColors);
        System.out.println("najwieksze kolor: " + getMaxColor(lfColors));

        slowColors = d.slowColoring();
        System.out.println("SLow: " + slowColors);
        System.out.println("najwieksze kolor: " + getMaxColor(lfColors));

        System.out.println("Dimacs r1000.5:");
        d = Graph.loadFromDimacs("r1000.5.txt");
        greedyColors = d.greedyColoring();
        System.out.println("Greedy: " + greedyColors);
        System.out.println("najwiekszy kolor: " + getMaxColor(greedyColors));

        lfColors = d.lfColoring();
        System.out.println("LF: " + lfColors);
        System.out.println("najwieksze kolor: " + getMaxColor(lfColors));

        slowColors = d.slowColoring();
        System.out.println("SLow: " + slowColors);
        System.out.println("najwieksze kolor: " + getMaxColor(lfColors));
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
}
