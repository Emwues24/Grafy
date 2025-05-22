package org.example;
import java.io.*;
import java.util.*;

class Graph {
    private int[][] adjacencyMatrix;
    private Map<Integer, List<Integer>> adjacencyList;
    private int vertices;
    private int time;

    public Graph(int vertices) {
        this.vertices = vertices;
        this.adjacencyMatrix = new int[vertices][vertices];
        this.adjacencyList = new HashMap<>();
        for (int i = 0; i < vertices; i++) {
            adjacencyList.put(i, new ArrayList<>());
        }
    }

    public void addEdge(int src, int dest) {
        if (adjacencyMatrix[src][dest] == 0 && adjacencyMatrix[dest][src] == 0){
            adjacencyMatrix[src][dest] = 1;
            adjacencyMatrix[dest][src] = 1;
            adjacencyList.get(src).add(dest);
            adjacencyList.get(dest).add(src);
        }
    }

    public void removeEdge(int src, int dest) {
        adjacencyMatrix[src][dest] = 0;
        adjacencyMatrix[dest][src] = 0;
        adjacencyList.get(src).remove(Integer.valueOf(dest));
        adjacencyList.get(dest).remove(Integer.valueOf(src));
    }

    public void addVertex() {
        int[][] newAdjacencyMatrix = new int[vertices + 1][vertices + 1];
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                newAdjacencyMatrix[i][j] = adjacencyMatrix[i][j];
            }
        }
        adjacencyMatrix = newAdjacencyMatrix;

        adjacencyList.put(vertices, new ArrayList<>());
        vertices++;
    }

    public void removeVertex(int vertex) {
        int[][] newAdjacencyMatrix = new int[vertices - 1][vertices - 1];
        for (int i = 0, newI = 0; i < vertices; i++) {
            if (i == vertex) continue;
            for (int j = 0, newJ = 0; j < vertices; j++) {
                if (j == vertex) continue;
                newAdjacencyMatrix[newI][newJ] = adjacencyMatrix[i][j];
                newJ++;
            }
            newI++;
        }
        adjacencyMatrix = newAdjacencyMatrix;

        adjacencyList.remove(vertex);

        for (List<Integer> neighbors : adjacencyList.values()) {
            neighbors.remove(Integer.valueOf(vertex));
        }

        vertices--;
    }

    private void removeVertexSafe(int vertex) {
        for (int i = 0; i < vertices; i++) {
            adjacencyMatrix[vertex][i] = 0;
            adjacencyMatrix[i][vertex] = 0;
        }
        adjacencyList.remove(vertex);
        for (List<Integer> neighbors : adjacencyList.values()) {
            neighbors.remove(Integer.valueOf(vertex));
        }
    }

    public void convertMatrixToList() {
        for (int i = 0; i < vertices; i++) {
            adjacencyList.get(i).clear();
            for (int j = 0; j < vertices; j++) {
                if (adjacencyMatrix[i][j] == 1) {
                    adjacencyList.get(i).add(j);
                }
            }
        }
    }

    public void convertListToMatrix() {
        adjacencyMatrix = new int[vertices][vertices];
        for (int i = 0; i < vertices; i++) {
            for (int neighbor : adjacencyList.get(i)) {
                adjacencyMatrix[i][neighbor] = 1;
            }
        }
    }

    public static Graph generateRandomGraph(int vertices, int edges) {
        if (edges > vertices * (vertices - 1) / 2) {
            throw new IllegalArgumentException("Za dużo krawędzi dla tej liczby wierzchołków!");
        }

        Graph graph = new Graph(vertices);
        Random random = new Random();
        Set<String> existingEdges = new HashSet<>();

        while (graph.getEdgeCount() < edges) {
            int u = random.nextInt(vertices);
            int v = random.nextInt(vertices);
            if (u != v) {
                String edge1 = u + "-" + v;
                String edge2 = v + "-" + u;
                if (!existingEdges.contains(edge1) && !existingEdges.contains(edge2)) {
                    graph.addEdge(u, v);
                    existingEdges.add(edge1);
                    existingEdges.add(edge2);
                }
            }
        }

        return graph;
    }

    public static Graph generateIntervalGraph(List<int[]> intervals) {
        int n = intervals.size();
        Graph graph = new Graph(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (intervalsOverlap(intervals.get(i), intervals.get(j))) {
                    graph.addEdge(i, j);
                }
            }
        }
        return graph;
    }

    private static boolean intervalsOverlap(int[] interval1, int[] interval2) {
        return interval1[0] <= interval2[1] && interval2[0] <= interval1[1];
    }

    public void saveIntervalsToFile(List<int[]> intervals, String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for (int[] interval : intervals) {
            writer.write(interval[0] + "," + interval[1] + "\n");
        }
        writer.close();
    }

    public static List<int[]> loadIntervalsFromFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        List<int[]> intervals = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            intervals.add(new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])});
        }
        reader.close();
        return intervals;
    }

    public void saveToGraph6(String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writer.write(encodeGraph6());
        writer.close();
    }

    public static Graph loadFromGraph6(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line = reader.readLine();
        reader.close();
        return decodeGraph6(line);
    }

    private String encodeGraph6() {
        StringBuilder sb = new StringBuilder();
        sb.append((char) (vertices + 63));
        List<Integer> bits = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < i; j++) {
                bits.add(adjacencyMatrix[i][j]);
            }
        }
        while (bits.size() % 6 != 0) {
            bits.add(0);
        }
        for (int i = 0; i < bits.size(); i += 6) {
            int value = 0;
            for (int j = 0; j < 6; j++) {
                value = (value << 1) | bits.get(i + j);
            }
            sb.append((char) (value + 63));
        }
        return sb.toString();
    }

    private static Graph decodeGraph6(String graph6) {
        int index;
        int n;

        if (graph6.charAt(0) == '~') {
            // Jeśli pierwszy znak to '~', czytamy kolejne 3 bajty jako big-endian int
            n = ((graph6.charAt(1) - 63) << 12) |
                    ((graph6.charAt(2) - 63) << 6) |
                    (graph6.charAt(3) - 63);
            index = 4; // Kolejne dane zaczynają się od pozycji 4
        } else {
            // Standardowy przypadek, kiedy n <= 62
            n = graph6.charAt(0) - 63;
            index = 1;
        }

        Graph graph = new Graph(n);
        List<Integer> bits = new ArrayList<>();

        // Odczytywanie bitów z reszty znaków
        for (int i = index; i < graph6.length(); i++) {
            int value = graph6.charAt(i) - 63;
            for (int j = 5; j >= 0; j--) {
                bits.add((value >> j) & 1);
            }
        }

        // Odtwarzanie macierzy sąsiedztwa
        int bitIndex = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (bitIndex < bits.size() && bits.get(bitIndex) == 1) {
                    graph.addEdge(i, j);
                }
                bitIndex++;
            }
        }

        return graph;
    }

    public static Graph loadFromDimacs(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        Graph graph = null;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("c")) {
                // skip dla komentarzy
                continue;
            }
            if (line.startsWith("p")) {
                // p edge <vertices> <edges> -- definicja grafu
                String[] parts = line.split("\\s+");
                int vertices = Integer.parseInt(parts[2]);
                graph = new Graph(vertices);
            } else if (line.startsWith("e")) {
                // e <node1> <node2> -- krawedz
                if (graph == null) {
                    throw new IOException("Blad formatu");
                }
                String[] parts = line.split("\\s+");
                int u = Integer.parseInt(parts[1]) - 1; // w pliku od 1
                int v = Integer.parseInt(parts[2]) - 1;
                graph.addEdge(u, v);
            }
        }
        reader.close();

        if (graph == null) {
            throw new IOException("Blad formatu.");
        }

        return graph;
    }

    public void printAdjacencyList() {
        for (int i = 0; i < vertices; i++) {
            System.out.println(i + " -> " + adjacencyList.get(i));
        }
    }
    public void printAdjacencyMatrix() {
        for (int[] row : adjacencyMatrix) {
            System.out.println(Arrays.toString(row));
        }
    }

    //obliczanie anihilacji 3a
    public int computeAnnihilationNumber() {
        int[] degrees = new int[vertices];
        for (int i = 0; i < vertices; i++) {
            degrees[i] = adjacencyList.get(i).size();
        }
        Arrays.sort(degrees);

        int sum = 0;
        int a = 0;
        for (int i = 0; i < vertices; i++) {
            sum += degrees[i];
            if (sum > getEdgeCount()) {
                break;
            }
            a = i + 1;
        }
        return a;
    }

    //abc indeks 3c
    public double computeABCIndex() {
        double abcIndex = 0.0;
        for (int u = 0; u < vertices; u++) {
            for (int v : adjacencyList.get(u)) {
                if (u < v) {
                    double degU = adjacencyList.get(u).size();
                    double degV = adjacencyList.get(v).size();
                    abcIndex += Math.sqrt((degU + degV - 2) / (degU * degV));
                }
            }
        }
        return abcIndex;
    }

    private int getEdgeCount() {
        int edges = 0;
        for (int i = 0; i < vertices; i++) {
            edges += adjacencyList.get(i).size();
        }
        return edges / 2;
    }

    //zliczanie cykli dl. 3
    public static int count3Cycles(int[][] adjMatrix) {
        int n = adjMatrix.length;
        int count = 0;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (adjMatrix[i][j] == 1) {
                    for (int k = j + 1; k < n; k++) {
                        if (adjMatrix[j][k] == 1 && adjMatrix[k][i] == 1) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }


    //temat2
    //GREEDY
    public Map<Integer, Integer> greedyColoring() {
        List<Integer> vertexOrder = orderVertices();
        return colorGraphInOrder(vertexOrder);
    }

    //LF
    public Map<Integer, Integer> lfColoring() {
        List<Integer> vertexOrder = orderVertices();
        vertexOrder.sort((a, b) -> Integer.compare(adjacencyList.get(b).size(), adjacencyList.get(a).size()));
        return colorGraphInOrder(vertexOrder);
    }

    //powolny SL bez kubełków
    public Map<Integer, Integer> slowColoring() {
        List<Integer> vertexOrder = slowOrderVertices();
        return colorGraphInOrder(vertexOrder);
    }

    public Map<Integer, Integer> slColoring() {
        int[] degree = new int[vertices];
        for (int i = 0; i < vertices; i++) {
            degree[i] = adjacencyList.get(i).size();
        }

        List<LinkedHashSet<Integer>> buckets = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            buckets.add(new LinkedHashSet<>());
        }

        for (int v = 0; v < vertices; v++) {
            buckets.get(degree[v]).add(v);
        }

        boolean[] removed = new boolean[vertices];
        List<Integer> order = new ArrayList<>();
        Random rand = new Random();

        for (int k = 0; k < vertices; k++) {
            int d = 0;
            while (d < buckets.size() && buckets.get(d).isEmpty()) {
                d++;
            }

            LinkedHashSet<Integer> bucket = buckets.get(d);
            int index = rand.nextInt(bucket.size());
            int selected = bucket.stream().skip(index).findFirst().orElseThrow();
            bucket.remove(selected);

            removed[selected] = true;
            order.add(selected);

            for (int neighbor : adjacencyList.get(selected)) {
                if (!removed[neighbor]) {
                    int deg = degree[neighbor];
                    buckets.get(deg).remove(neighbor);
                    degree[neighbor]--;
                    buckets.get(degree[neighbor]).add(neighbor);
                }
            }
        }

        Collections.reverse(order);
        return colorGraphInOrder(order);
    }

    public Map<Integer, Integer> lfBucketColoring() {
        List<LinkedHashSet<Integer>> buckets = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            buckets.add(new LinkedHashSet<>());
        }

        for (int v = 0; v < vertices; v++) {
            int degree = adjacencyList.get(v).size();
            buckets.get(degree).add(v);
        }

        boolean[] removed = new boolean[vertices];
        List<Integer> order = new ArrayList<>();
        Random rand = new Random();

        for (int k = 0; k < vertices; k++) {
            int d = buckets.size() - 1;
            while (d >= 0 && buckets.get(d).isEmpty()) {
                d--;
            }

            LinkedHashSet<Integer> bucket = buckets.get(d);
            int index = rand.nextInt(bucket.size());
            int selected = bucket.stream().skip(index).findFirst().orElseThrow();
            bucket.remove(selected);

            removed[selected] = true;
            order.add(selected);
        }

        return colorGraphInOrder(order);
    }


    private List<Integer> orderVertices(){
        List<Integer> vertexOrder = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            vertexOrder.add(i);
        }
        return vertexOrder;
    }

    private Map<Integer, Integer> colorGraphInOrder(List<Integer> vertexOrder) {
        Map<Integer, Integer> result = new HashMap<>();

        for (int vertex : vertexOrder) {
            Set<Integer> usedColors = new HashSet<>();
            for (int neighbor : adjacencyList.get(vertex)) {
                if (result.containsKey(neighbor)) {
                    usedColors.add(result.get(neighbor));
                }
            }
            int color = 0;
            while (usedColors.contains(color)) {
                color++;
            }
            result.put(vertex, color);
        }

        return result;
    }

    //ilosc kolorów
    public static int getMaxColor(Map<Integer, Integer> coloring) {
        int maxColor = -1;
        for (int color : coloring.values()) {
            if (color > maxColor) {
                maxColor = color;
            }
        }
        return maxColor;
    }

    public List<Integer> slowOrderVertices() {
        Graph tempGraph = new Graph(vertices);
        for (int i = 0; i < vertices; i++) {
            for (int neighbor : adjacencyList.get(i)) {
                if (i < neighbor) {
                    tempGraph.addEdge(i, neighbor);
                }
            }
        }
        List<Integer> order = new LinkedList<>();

        while (!tempGraph.adjacencyList.isEmpty()) {
            int minDegreeVertex = -1;
            int minDegree = Integer.MAX_VALUE;

            for (int v : tempGraph.adjacencyList.keySet()) {
                int degree = tempGraph.adjacencyList.get(v).size();
                if (degree < minDegree) {
                    minDegree = degree;
                    minDegreeVertex = v;
                }
            }

            order.add(0, minDegreeVertex);
            tempGraph.removeVertexSafe(minDegreeVertex);
        }

        return order;
    }



    // 1.a Rekurencyjny DFS dla macierzy sąsiedztwa z nawracaniem
    public void dfsRecursiveMatrix(int vertex, boolean[] visited) {
        visited[vertex] = true;
        System.out.println("Wejście: " + vertex);
        for (int i = 0; i < vertices; i++) {
            if (adjacencyMatrix[vertex][i] == 1 && !visited[i]) {
                dfsRecursiveMatrix(i, visited);
            }
        }
        System.out.println("Wyjście: " + vertex);
    }

    // 1.b Rekurencyjny DFS dla listy sąsiedztwa z nawracaniem
    public void dfsRecursiveList(int vertex, boolean[] visited) {
        visited[vertex] = true;
        System.out.println("Wejście: " + vertex);
        for (int neighbor : adjacencyList.get(vertex)) {
            if (!visited[neighbor]) {
                dfsRecursiveList(neighbor, visited);
            }
        }
        System.out.println("Wyjście: " + vertex);
    }

    // 2.a Iteracyjny DFS dla macierzy sąsiedztwa
    public void dfsIterativeMatrix(int start) {
        boolean[] visited = new boolean[vertices];
        Stack<Integer> stack = new Stack<>();
        stack.push(start);

        while (!stack.isEmpty()) {
            int vertex = stack.pop();
            if (!visited[vertex]) {
                visited[vertex] = true;
                System.out.println("Odwiedzony: " + vertex);
                // Dodaj sąsiadów do stosu (w odwrotnej kolejności dla naturalnego porządku)
                for (int i = vertices - 1; i >= 0; i--) {
                    if (adjacencyMatrix[vertex][i] == 1 && !visited[i]) {
                        stack.push(i);
                    }
                }
            }
        }
    }

    // 2.b Iteracyjny DFS dla listy sąsiedztwa
    public void dfsIterativeList(int start) {
        boolean[] visited = new boolean[vertices];
        Stack<Integer> stack = new Stack<>();
        stack.push(start);

        while (!stack.isEmpty()) {
            int vertex = stack.pop();
            if (!visited[vertex]) {
                visited[vertex] = true;
                System.out.println("Odwiedzony: " + vertex);
                List<Integer> neighbors = adjacencyList.get(vertex);
                // Dodajemy sąsiadów w odwrotnej kolejności, aby DFS był w porządku naturalnym
                for (int i = neighbors.size() - 1; i >= 0; i--) {
                    int neighbor = neighbors.get(i);
                    if (!visited[neighbor]) {
                        stack.push(neighbor);
                    }
                }
            }
        }
    }

    public boolean isTree() {
        boolean[] visited = new boolean[vertices];

        // Sprawdzenie cyklu i spójności – zaczynamy DFS od wierzchołka 0
        if (hasCycle(0, -1, visited)) {
            return false; // ma cykl
        }

        // Sprawdzenie spójności: czy wszystkie wierzchołki zostały odwiedzone?
        for (boolean v : visited) {
            if (!v) return false; // graf niespójny
        }

        return true; // nie ma cyklu i jest spójny ⇒ drzewo
    }

    // Wersja DFS rekurencyjnego z detekcją cyklu
    private boolean hasCycle(int v, int parent, boolean[] visited) {
        visited[v] = true;

        for (int neighbor : adjacencyList.get(v)) {
            if (!visited[neighbor]) {
                if (hasCycle(neighbor, v, visited)) return true;
            } else if (neighbor != parent) {
                return true; // znaleziono cykl
            }
        }

        return false;
    }

    public void findAllCycles() {
        boolean[] visited = new boolean[vertices];
        List<Integer> path = new ArrayList<>();
        Set<String> uniqueCycles = new HashSet<>();

        for (int i = 0; i < vertices; i++) {
            findCyclesUtil(i, i, visited, path, uniqueCycles);
            visited[i] = true; // żeby nie zaczynać znowu od tych samych
        }

        for (String cycle : uniqueCycles) {
            System.out.println("Cycle: " + cycle);
        }
    }

    private void findCyclesUtil(int start, int current, boolean[] visited, List<Integer> path, Set<String> uniqueCycles) {
        path.add(current);
        visited[current] = true;

        for (int neighbor : adjacencyList.get(current)) {
            if (neighbor == start && path.size() > 2) {
                // cykl znaleziony
                List<Integer> cycle = new ArrayList<>(path);
                Collections.sort(cycle);
                uniqueCycles.add(cycle.toString()); // używamy Set, żeby uniknąć duplikatów
            } else if (!visited[neighbor]) {
                findCyclesUtil(start, neighbor, visited, path, uniqueCycles);
            }
        }

        path.remove(path.size() - 1);
        visited[current] = false;
    }

    public void dfsWithIntervals(int start) {
        boolean[] visited = new boolean[vertices];
        int[][] times = new int[vertices][2]; // [i][0] = otwarcie, [i][1] = zamknięcie
        time = 1;

        dfsIntervalHelper(start, visited, times);

        // Wyświetlenie wyników ASCII
        printIntervals(times);
    }

    private void dfsIntervalHelper(int v, boolean[] visited, int[][] times) {
        visited[v] = true;
        times[v][0] = time++; // otwarcie

        for (int neighbor : adjacencyList.get(v)) {
            if (!visited[neighbor]) {
                dfsIntervalHelper(neighbor, visited, times);
            }
        }

        times[v][1] = time++; // zamknięcie
    }

    private void printIntervals(int[][] times) {
        System.out.println("Przedziały otwarcia/zamknięcia wierzchołków:");
        for (int i = 0; i < vertices; i++) {
            System.out.printf("Wierzchołek %d: %d/%d\n", i, times[i][0], times[i][1]);
        }

        System.out.println("\nWizualizacja ASCII:");
        for (int i = 0; i < vertices; i++) {
            StringBuilder bar = new StringBuilder();
            for (int j = 1; j <= time; j++) {
                if (j >= times[i][0] && j <= times[i][1]) {
                    bar.append("█");
                } else {
                    bar.append(" ");
                }
            }
            System.out.printf("%d: %s (%d/%d)\n", i, bar.toString(), times[i][0], times[i][1]);
        }
    }

    public void dfsIterativeMatrixWithStackVisualization(int start) {
        boolean[] visited = new boolean[vertices];
        Stack<Integer> stack = new Stack<>();
        stack.push(start);

        while (!stack.isEmpty()) {
            printStack(stack); // <<< ASCII wizualizacja
            int vertex = stack.pop();
            if (!visited[vertex]) {
                visited[vertex] = true;
                System.out.println("Odwiedzony: " + vertex);
                for (int i = vertices - 1; i >= 0; i--) {
                    if (adjacencyMatrix[vertex][i] == 1 && !visited[i]) {
                        stack.push(i);
                    }
                }
            }
        }
    }

    private void printStack(Stack<Integer> stack) {
        System.out.println("Stos:");
        for (int i = stack.size() - 1; i >= 0; i--) {
            System.out.print("| " + stack.get(i) + " |");
        }
        System.out.println("-----");
    }
}