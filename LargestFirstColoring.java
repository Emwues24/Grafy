import java.util.*;

public class LargestFirstOrder extends SmallestLastColoring {
    public LargestFirstOrder(int n) {
        super(n);
    }

    @Override
    public List<Integer> LargestFirstOrder() {
        initBuckets();
        boolean[] removed = new boolean[n];
        int remaining = n;
        List<Integer> order = new ArrayList<>(n);

        int maxD = currentMaxDegree;
        for (int k = 0; k < n; k++) {
            // LF: znajdź półkę o największym stopniu niepustą
            int d;
            for (d = maxD; d >= 0; d--) {
                if (!buckets.get(d).isEmpty()) break;
            }
            // weź dowolny wierzchołek z kubełka d
            List<Integer> candidates = new ArrayList<>(buckets.get(d));
            int v = candidates.get(new Random().nextInt(candidates.size()));
            buckets.get(d).remove(v);
            removed[v] = true;
            order.add(v);

            // zaktualizuj sąsiadów
            for (int u : adj.get(v)) {
                if (removed[u]) continue;
                int du = degree[u];
                buckets.get(du).remove(u);
                degree[u]--;
                buckets.get(du - 1).add(u);
            }
            // obniż maxD, jeśli pusty
            while (maxD > 0 && buckets.get(maxD).isEmpty()) {
                maxD--;
            }
        }
        // dla LF nie odwracamy kolejności – order już idzie od największego do najmniejszego
        return order;
    }

    public void runLFColoring() {
        List<Integer> order = LargestFirstOrder();
        colorWithOrder(order);
    }
    public static void testLFColoringStats(LargestFirstOrder graph, int repetitions) {
        List<Integer> results = new ArrayList<>();
        for (int i = 0; i < repetitions; i++) {
            LargestFirstOrder copy = graph.copyLF();
            copy.runLFColoring();

            int maxColor = Arrays.stream(copy.getColors()).max().orElse(-1) + 1;
            results.add(maxColor);
        }

        Collections.sort(results);
        double avg = results.stream().mapToInt(Integer::intValue).average().orElse(0);
        int min = results.get(0);
        int max = results.get(results.size() - 1);
        int median = results.get(results.size() / 2);

        System.out.println("LF: SA(G) = " + avg);
        System.out.println("LF: RA(G) = " + median);
        System.out.println("LF: MA(G) = " + min);
        System.out.println("LF: WA(G) = " + max);
    }
    public LargestFirstOrder copyLF() {
        LargestFirstOrder newGraph = new LargestFirstOrder(n);
        for (int u = 0; u < n; u++) {
            for (int v : adj.get(u)) {
                if (u < v) newGraph.addEdge(u, v);
            }
        }
        return newGraph;
    }
}
