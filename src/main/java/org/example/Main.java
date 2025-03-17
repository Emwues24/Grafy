package org.example;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.example.Graph.count3Cycles;

public class Main {
    public static void main(String[] args) throws IOException {
        //tworzenie grafu
        Graph graph = new Graph(5);
        graph.addEdge(0, 1);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(4, 0);

        //tworzenie macierzy
        System.out.println("Macierz sąsiedztwa:");
        graph.printAdjacencyMatrix();

        //tworzenie listy
        graph.convertMatrixToList();
        System.out.println("Lista:");
        graph.printAdjacencyList();

        //zapis do graph6
        graph.saveToGraph6("mojgraf.txt");
        //odczyt z graph6, sprawdzenie czy lista sie nie zmieni
        graph = Graph.loadFromGraph6("mojgraf.txt");
        System.out.println("Lista:");
        graph.printAdjacencyList();

        //usuwanie krawedzi
        graph.removeEdge(1,3);
        System.out.println("Lista:");
        graph.printAdjacencyList();

        //dodawanie wierzcholka
        graph.addVertex();
        System.out.println("Lista:");
        graph.printAdjacencyList();

        //usuwanie wierzcholka
        graph.removeVertex(5);
        System.out.println("Lista:");
        graph.printAdjacencyList();

        //odczyt z pliku
        Graph loadedGraph = Graph.loadFromGraph6("graph6.txt");//      F?aN_
        System.out.println("Lista:");
        loadedGraph.printAdjacencyList();
        loadedGraph.convertListToMatrix();
        System.out.println("Macierz:");
        loadedGraph.printAdjacencyMatrix();
        //anihilacja
        System.out.println("Liczba anihilacji:");
        System.out.println(loadedGraph.computeAnnihilationNumber());
        //abc
        System.out.println("Indeks ABC:");
        System.out.println(loadedGraph.computeABCIndex());

        //przeciecia przedzialow
        List<int[]> intervals = Arrays.asList(new int[]{1, 5}, new int[]{3, 7}, new int[]{6, 10}, new int[]{6, 12});
        graph.saveIntervalsToFile(intervals, "intervals.txt");
        List<int[]> loadedIntervals = Graph.loadIntervalsFromFile("intervals.txt");
        Graph intervalGraph = Graph.generateIntervalGraph(loadedIntervals);
        System.out.println("Graf przedziałów z pliku:");
        intervalGraph.printAdjacencyList();

        //zliczanie cykli dlugosci 3
        int[][] graph2 = {
                {0, 1, 1, 0},
                {1, 0, 1, 1},
                {1, 1, 0, 1},
                {0, 1, 1, 0}
        };

        System.out.println("Liczba cykli długości 3: " + count3Cycles(graph2));
    }
}