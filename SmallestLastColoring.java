import java.util.*;

public class SmallestLastColoring {
    // liczba wierzchołków
    protected final int n;
    // lista sąsiedztwa
    protected final List<Set<Integer>> adj;
    // stopień aktualny każdego wierzchołka
    protected final int[] degree;
    // kolor każdego wierzchołka
    protected final int[] color;

    // kubełki: dla stopni 0..maxDegree trzymamy LinkedHashSet dla O(1) usuwania i iteracji
    protected final List<LinkedHashSet<Integer>> buckets;
    protected int currentMaxDegree;

    public SmallestLastColoring(int n) {
        this.n = n;
        this.adj = new ArrayList<>(n);
        this.degree = new int[n];
        this.color = new int[n];
        for (int i = 0; i < n; i++) {
            adj.add(new HashSet<>());
            color[i] = -1;
        }
        this.buckets = new ArrayList<>();
        this.currentMaxDegree = 0;
    }

    public void addEdge(int u, int v) {
        if (adj.get(u).add(v)) {
            adj.get(v).add(u);
            degree[u]++;
            degree[v]++;
            currentMaxDegree = Math.max(currentMaxDegree, Math.max(degree[u], degree[v]));
        }
    }

    protected void initBuckets() {
        // przygotuj kubełki od 0 do currentMaxDegree
        buckets.clear();
        for (int d = 0; d <= currentMaxDegree; d++) {
            buckets.add(new LinkedHashSet<>());
        }
        // wypełnij wierzchołkami
        for (int v = 0; v < n; v++) {
            buckets.get(degree[v]).add(v);
        }
    }

    public List<Integer> smallestLastOrder() {
        initBuckets();
        boolean[] removed = new boolean[n];
        int remaining = n;
        List<Integer> order = new ArrayList<>(n);

        int maxD = currentMaxDegree;
        for (int k = 0; k < n; k++) {
            // znajdź najmniejszy kubełek niepusty
            int d;
            for (d = 0; d <= maxD; d++) {
                if (!buckets.get(d).isEmpty()) break;
            }
            // weź dowolny wierzchołek z kubełka d
            List<Integer> candidates = new ArrayList<>(buckets.get(d));
            int v = candidates.get(new Random().nextInt(candidates.size()));
            buckets.get(d).remove(v);
            removed[v] = true;
            order.add(v);
            remaining--;

            // zaktualizuj sąsiadów: stopnie i kubełki
            for (int u : adj.get(v)) {
                if (removed[u]) continue;
                int du = degree[u];
                buckets.get(du).remove(u);
                degree[u]--;
                buckets.get(du - 1).add(u);
            }
            // opcjonalnie obniż maxD
            while (maxD > 0 && buckets.get(maxD).isEmpty()) {
                maxD--;
            }
        }
        // kolejność jest od najmniej „trudnego” (smallest-last kończy najtrudniejszym)
        Collections.reverse(order);
        return order;
    }
    public void colorWithOrder(List<Integer> order) {
        int maxColor = 0;
        for (int v : order) {
            // zbiór kolorów sąsiadów
            Set<Integer> used = new HashSet<>();
            for (int u : adj.get(v)) {
                if (color[u] != -1) {
                    used.add(color[u]);
                }
            }
            // najmniejszy wolny kolor
            int c = 0;
            while (used.contains(c)) c++;
            color[v] = c;
            maxColor = Math.max(maxColor, c);
        }
        System.out.println("Użyto kolorów 0..." + maxColor);
    }

    public void runSLColoring() {
        List<Integer> order = smallestLastOrder();
        colorWithOrder(order);
    }

    public int[] getColors() {
        return color;
    }
    public static void testSLColoringStats(SmallestLastColoring graph, int repetitions) {
        List<Integer> results = new ArrayList<>();
        for (int i = 0; i < repetitions; i++) {
            // Skopiuj graf, by zachować oryginalne dane (kopiowanie wymagane!)
            SmallestLastColoring copy = graph.copy();
            copy.runSLColoring();

            // Zlicz ile kolorów zostało użytych
            int maxColor = Arrays.stream(copy.getColors()).max().orElse(-1) + 1;
            results.add(maxColor);
        }

        Collections.sort(results);
        double avg = results.stream().mapToInt(Integer::intValue).average().orElse(0);
        int min = results.get(0);
        int max = results.get(results.size() - 1);
        int median = results.get(results.size() / 2);

        System.out.println("SA(G) = " + avg);
        System.out.println("RA(G) = " + median);
        System.out.println("MA(G) = " + min);
        System.out.println("WA(G) = " + max);
    }
    public SmallestLastColoring copy() {
        SmallestLastColoring newGraph = new SmallestLastColoring(n);
        for (int u = 0; u < n; u++) {
            for (int v : adj.get(u)) {
                if (u < v) newGraph.addEdge(u, v);
            }
        }
        return newGraph;
    }
}
