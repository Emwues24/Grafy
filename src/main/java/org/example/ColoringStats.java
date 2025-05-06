package org.example;

import java.util.*;
import java.util.function.Supplier;

public class ColoringStats {

    private final Graph graph;
    private final int repetitions = 100;

    public ColoringStats(Graph graph) {
        this.graph = graph;
    }

    public void runAll() {
        System.out.println("===== WYNIKI ALGORYTMÓW KOLORYZACJI =====");

        // Deterministyczne algorytmy
        analyzeDeterministic("GREEDY", graph.greedyColoring());
        analyzeDeterministic("LF", graph.lfColoring());
        analyzeDeterministic("SLOW", graph.slowColoring());

        // Losowe kubełkowe
        analyzeRandomized("LF_BUCKET", () -> graph.lfBucketColoring());
        analyzeRandomized("SL_BUCKET", () -> graph.slColoring());
    }

    private void analyzeDeterministic(String name, Map<Integer, Integer> coloring) {
        int colorsUsed = new HashSet<>(coloring.values()).size();
        System.out.printf("[%s] Kolory użyte: %d %n", name, colorsUsed, colorsUsed);
    }

    private void analyzeRandomized(String name, Supplier<Map<Integer, Integer>> algorithm) {
        List<Integer> results = new ArrayList<>();

        for (int i = 0; i < repetitions; i++) {
            Map<Integer, Integer> coloring = algorithm.get();
            int colorsUsed = new HashSet<>(coloring.values()).size();
            results.add(colorsUsed);
        }

        double avg = results.stream().mapToInt(i -> i).average().orElse(0);
        int min = results.stream().mapToInt(i -> i).min().orElse(0);
        int max = results.stream().mapToInt(i -> i).max().orElse(0);
        int median = getMedian(results);

        System.out.printf("[%s] Średnia: %.2f, Mediana: %d, Min: %d, Max: %d%n",
                name, avg, median, min, max);
    }

    private int getMedian(List<Integer> list) {
        Collections.sort(list);
        int n = list.size();
        if (n % 2 == 1) {
            return list.get(n / 2);
        } else {
            return (list.get(n / 2 - 1) + list.get(n / 2)) / 2;
        }
    }
}

