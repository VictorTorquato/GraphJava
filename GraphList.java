import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GraphList {

  private int countNodes;
  private int countEdges;
  private ArrayList<ArrayList<Edge>> adjList;
  private ArrayList<Edge> edgeList;
  private static final int INF = 999999999;

  public GraphList(int countNodes) {
    this.countNodes = countNodes;
    adjList = new ArrayList<>(this.countNodes);

    for (int i = 0; i < this.countNodes; i++) {
      adjList.add(new ArrayList<Edge>());
    }

    edgeList = new ArrayList<>();
  }

  public GraphList(String fileName) throws IOException {
    File file = new File(fileName);
    FileReader reader = new FileReader(file);
    BufferedReader bufferedReader = new BufferedReader(reader);

    String[] line = bufferedReader.readLine().split(" ");
    this.countNodes = (Integer.parseInt(line[0]));
    int fileLines = (Integer.parseInt(line[0]));

    adjList = new ArrayList<>(this.countNodes);
    edgeList = new ArrayList<>();

    for (int i = 0; i < this.countNodes; i++) {
      adjList.add(new ArrayList<Edge>());
    }
    for (int i = 0; i < fileLines; i++) {
      String[] edgeInfo = bufferedReader.readLine().split(" ");
      int source = Integer.parseInt(edgeInfo[0]);
      int sink = Integer.parseInt(edgeInfo[1]);
      int weight = Integer.parseInt(edgeInfo[2]);
      this.addEdge(source, sink, weight);
    }
    bufferedReader.close();
    reader.close();
  }

  public void addEdge(int source, int sink, int weight) {
    if (source < 0 || source > this.countNodes - 1
        || sink < 0 || sink > this.countNodes - 1
        || weight <= 0) {
      // System.err.println("Invalid edge: " + source + sink + weight);
      return;
    }
    adjList.get(source).add(new Edge(source, sink, weight));
    edgeList.add(new Edge(source, sink, weight));
    this.countEdges++;
  }

  public void addEdgeUnoriented(int source, int sink, int weight) {
    addEdge(source, sink, weight);
    addEdge(source, sink, weight);
  }

  public String toString() {
    String str = "";
    for (int i = 0; i < adjList.size(); ++i) {
      str += i + ": ";
      for (int j = 0; j < adjList.get(i).size(); ++j) {
        str += adjList.get(i).get(j) + "\t";
      }
      str += "\n";
    }
    return str;
  }

  public int getCountNodes() {
    return this.countNodes;
  }

  public int getCountEdges() {
    return this.countEdges;
  }

  public ArrayList<ArrayList<Edge>> getAdjList() {
    return adjList;
  }

  public void setAdjList(ArrayList<ArrayList<Edge>> adjList) {
    this.adjList = adjList;
  }

  public int degree(int node) {
    if (node < 0 || node > this.countNodes - 1)
      System.err.println("Nó Inválido: " + node);
    return adjList.get(node).size();
  }

  public int highestDegree() {
    int highest = 0;
    for (int i = 0; i < adjList.size(); i++) {
      if (highest < degree(i))
        highest = degree(i);
    }
    return highest;
  }

  public int lowestDegree() {
    int lowest = adjList.size();
    for (int i = 0; i < adjList.size(); i++) {
      if (lowest > degree(i))
        lowest = degree(i);
    }
    return lowest;
  }

  public GraphList complement() {
    GraphList complement = new GraphList(this.countNodes);

    for (int i = 0; i < adjList.size(); i++) {
      for (int j = 0; j < this.countNodes; j++) {
        int aux = 1;
        for (int k = 0; k < adjList.get(i).size(); k++) {
          int v = adjList.get(i).get(k).getSink();
          if (v == j) {
            aux = 0;
            break;
          }
        }
        if (aux == 1 && i != j) {
          complement.addEdge(i, j, 1);
        }
      }
    }
    return complement;
  }

  public boolean subgraph(GraphList g2) {
    if (g2.countNodes > this.countNodes || g2.countEdges > this.countEdges)
      return false;
    for (int i = 0; i < g2.adjList.size(); i++) {
      boolean arestaEncontrada = false;
      for (int j = 0; j < g2.adjList.get(i).size(); ++j) {
        int v = g2.adjList.get(i).get(j).getSink();
        for (int k = 0; k < this.adjList.get(i).size(); ++k) {
          int v2 = this.adjList.get(i).get(k).getSink();
          if (v == v2) {
            arestaEncontrada = true;
            break;
          }
        }
        if (!arestaEncontrada)
          return false;
      }
    }
    return true;
  }

  public float density() {
    return (float) this.countEdges / (this.countNodes * (this.countNodes - 1));
  }

  public ArrayList<Integer> bfs(int s) {
    int[] desc = new int[this.countNodes];
    ArrayList<Integer> Q = new ArrayList<>();
    Q.add(s);
    ArrayList<Integer> R = new ArrayList<>();
    R.add(s);

    desc[s] = 1;
    while (Q.size() > 0) {
      int v = Q.remove(0);
      for (int i = 0; i < this.adjList.get(i).size(); i++) {
        if (desc[this.adjList.get(v).get(i).getSink()] == 0) {
          Q.add(this.adjList.get(v).get(i).getSink());
          R.add(this.adjList.get(v).get(i).getSink());
          desc[this.adjList.get(v).get(i).getSink()] = 1;
        }
      }
    }
    return R;
  }

  public ArrayList<Integer> dfs(int s) {
    int[] desc = new int[this.countNodes];
    ArrayList<Integer> S = new ArrayList<>();
    S.add(s);
    ArrayList<Integer> R = new ArrayList<>();
    R.add(s);

    desc[s] = 1;
    while (S.size() > 0) {
      boolean aux = true;
      int v = S.get(S.size() - 1);
      for (int i = 0; i < this.adjList.get(v).size(); i++) {
        if (desc[this.adjList.get(v).get(i).getSink()] == 0) {
          S.add(this.adjList.get(v).get(i).getSink());
          R.add(this.adjList.get(v).get(i).getSink());
          desc[this.adjList.get(v).get(i).getSink()] = 1;
          aux = false;
          break;
        }
      }
      if (aux) {
        S.remove(S.size() - 1);
      }
    }
    return R;
  }

  public boolean connected() {
    return this.bfs(0).size() == this.countNodes;
  }

  public boolean nonOriented() {
    for (int i = 0; i < this.adjList.size(); i++) {
      for (int j = 0; j < this.adjList.get(i).size(); j++) {
        int sink = this.adjList.get(i).get(j).getSink();
        int source = this.adjList.get(i).get(j).getSource();
        boolean aux = false;
        for (int k = 0; k < this.adjList.get(sink).size(); k++) {
          if (this.adjList.get(sink).get(k).getSink() == source) {
            aux = true;
            break;
          }
        }
        if (!aux) {
          return false;
        }
      }
    }
    return true;
  }

  public ArrayList<Integer> dfsRec(int s) {
    int[] desc = new int[this.countNodes];
    ArrayList<Integer> R = new ArrayList<>();
    dfsRecAux(s, desc, R);
    return R;
  }

  public void dfsRecAux(int u, int[] desc, ArrayList<Integer> R) {
    desc[u] = 1;
    R.add(u);
    for (int i = 0; i < this.adjList.get(u).size(); i++) {
      int v = this.adjList.get(u).get(i).getSink();
      if (desc[v] == 0) {
        dfsRecAux(v, desc, R);
      }
    }
  }

  public int caminhoMin(int[] dist, ArrayList<Integer> q) {
    int menor = dist[q.get(0)];
    int u = q.get(0);

    for (int i = 0; i < this.countNodes; i++) {

      if (q.contains(i)) {

        if (dist[i] < menor) {
          menor = dist[i];
          u = i;
        }

      }
    }
    return u;
  }

  public void dijkstra(int s, int t) {
    int[] pred = new int[this.countNodes];
    int[] dist = new int[this.countNodes];

    for (int i = 0; i < this.countNodes; i++) {
      pred[i] = -1;
      dist[i] = INF;
    }

    dist[s] = 0;
    ArrayList<Integer> q = new ArrayList<Integer>();

    for (int i = 0; i < this.adjList.size(); i++) {
      q.add(i);
    }

    while (q.size() != 0) {
      Integer u = caminhoMin(dist, q);
      q.remove(u);

      for (int i = 0; i < this.adjList.get(u).size(); i++) {
        if (dist[this.adjList.get(u).get(i).getSink()] > dist[u] + this.adjList.get(u).get(i).getWeight()) {
          dist[this.adjList.get(u).get(i).getSink()] = dist[u] + this.adjList.get(u).get(i).getWeight();
          pred[this.adjList.get(u).get(i).getSink()] = u;
        }
      }
    }

    System.out.printf("Distância entre %d e %d: %d   ", s, t, dist[t]);
  }

  public void bellmanFord(int s, int t) {
    int[] pred = new int[this.countNodes];
    int[] dist = new int[this.countNodes];

    for (int i = 0; i < this.countNodes; i++) {
      dist[i] = INF;
      pred[i] = -1;
    }
    dist[s] = 0;

    for (int i = 0; i < this.countNodes; i++) {
      for (Edge edge : edgeList) {
        if (dist[edge.getSink()] > dist[edge.getSource()] + edge.getWeight()) {
          dist[edge.getSink()] = dist[edge.getSource()] + edge.getWeight();
          pred[edge.getSink()] = edge.getSource();
        }
      }
    }

    // System.out.printf("Distância entre %d e %d: %d ", s, t, dist[t]);
  }

  public void bellmanFordFlag(int s, int t) {
    int[] pred = new int[this.countNodes];
    int[] dist = new int[this.countNodes];

    for (int i = 0; i < this.countNodes; i++) {
      dist[i] = INF;
      pred[i] = -1;
    }
    dist[s] = 0;

    for (int i = 0; i < this.countNodes; i++) {
      int trocou = 0;
      for (Edge edge : edgeList) {
        if (dist[edge.getSink()] > dist[edge.getSource()] + edge.getWeight()) {
          dist[edge.getSink()] = dist[edge.getSource()] + edge.getWeight();
          pred[edge.getSink()] = edge.getSource();
          trocou = 1;
        }
      }
      if (trocou == 0)
        break;
    }

    // System.out.printf("Distância entre %d e %d: %d   ", s, t, dist[t]);
  }

  public ArrayList<Edge> kruskal() {
    ArrayList<Edge> T = new ArrayList<Edge>(this.countNodes - 1);
    int[] F = new int[this.countNodes];

    for (int i = 0; i < this.countNodes; i++) {
      F[i] = i;
    }
    edgeList.sort(null);

    for (int j = 0; j < edgeList.size(); j++) {
      int x = edgeList.get(j).getSource();
      int y = edgeList.get(j).getSink();
      if (F[x] != F[y]) {
        T.add(edgeList.get(j));

        if (T.size() == this.countNodes - 1)
          break;

        int aux = F[y];
        for (int i = 0; i < F.length; i++) {
          if (F[i] == aux) {
            F[i] = F[x];
          }
        }
      }
    }
    return T;
  }

  public ArrayList<Edge> prim() {
    ArrayList<Edge> T = new ArrayList<Edge>(this.countNodes - 1);

    int a = 0;

    int[] pred = new int[this.countNodes];
    int[] dist = new int[this.countNodes];

    ArrayList<Integer> Q = new ArrayList<Integer>(this.countNodes);

    for (int i = 0; i < this.countNodes; i++) {
      dist[i] = INF;
      pred[i] = -1;
      Q.add(i);
    }

    dist[a] = 0;

    while (Q.size() != 0) {
      int b = -1;
      int menor = INF;

      for (int j = 0; j < Q.size(); j++) {
        int i = Q.get(j);
        if (dist[i] < menor) {
          menor = dist[i];
          b = i;
        }
      }

      Q.remove((Integer) b);
      for (int k = 0; k < this.adjList.get(b).size(); k++) {
        int x = this.adjList.get(b).get(k).getSink();
        int y = this.adjList.get(b).get(k).getWeight();

        if (Q.contains(x) && y < dist[x]) {
          dist[x] = y;
          pred[x] = b;
        }
      }
    }

    for (int i = 0; i < pred.length; i++) {
      if (pred[i] != -1) {
        T.add(new Edge(i, pred[i], 1));
      }
    }
    return T;
  }

}