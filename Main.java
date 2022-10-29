import java.io.IOException;

class Main {
  public static void main(String[] args) throws IOException {
    GraphList g1 = new GraphList("cm/rome99c.txt");
    GraphMatrix g2 = new GraphMatrix("cm/rome99c.txt");
    // m1.search_largura();
    // System.out.println(g1);
    // System.out.println(g1);

    long start = System.currentTimeMillis();
    // g1.dijkstra(7, 100);
    System.out.println("\nBellman Ford: ");
    g1.bellmanFord(2,4);
    long elapsed = System.currentTimeMillis() - start;
  
    System.out.println("\nTempo de execução: " + elapsed + " ms\n");

    start = System.currentTimeMillis();
    System.out.println("\nBellman Ford Com Flag: ");
    g1.bellmanFordFlag(2,4);

    elapsed = System.currentTimeMillis() - start;
    System.out.println("\nTempo de execução: " + elapsed + " ms\n");

    Maze m1 = new Maze("maze/maze3.txt");
    m1.printLabirinto();

  }
}