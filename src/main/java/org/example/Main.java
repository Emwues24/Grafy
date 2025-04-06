package org.example;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.example.Graph.*;

public class Main {

    public static void main(String[] args) throws IOException {
       // Temat1();

        Graph g = loadFromGraph6("graphbig.txt");

        Map<Integer, Integer> greedyColors = g.greedyColoring();
        System.out.println("Greedy: " + greedyColors);
        System.out.println("najwiekszy kolor: " + getMaxColor(greedyColors));

        Map<Integer, Integer> lfColors = g.lfColoring();
        System.out.println("LF: " + lfColors);
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
