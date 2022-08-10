public class Graph {

    private int countNodes;
    private int countEdges;
    private int[][] adjMatrix;
  
    public Graph(int countNodes) {
      this.countNodes = countNodes;
      this.adjMatrix = new int[countNodes][countNodes];
    }
  
    public int getCountNodes() {
      return this.countNodes;
    }
   
    public int getCountEdges() {
      return this.countEdges;
    }
  
    public int[][] getAdjMatrix() {
      return this.adjMatrix;
    }
  
    public String toString() {
      String str = "";
      for (int i = 0; i < this.adjMatrix.length; ++i) {
        for (int j = 0; j < this.adjMatrix[i].length; ++j) {
          str += this.adjMatrix[i][j] + "\t";
        }
        str += "\n";
      }
      return str;
    }
  
    public void addEdge(int source, int sink, int weight) {
      if (source < 0 || source > this.countNodes - 1 
          || sink < 0 || sink > this.countNodes - 1 || weight <= 0) {
        System.err.println("Aresta Inválida: " + source + sink + weight);
        return;
      }
      this.adjMatrix[source][sink] = weight;
      this.countEdges++;
    }
  
    public int degree(int node) {
      if (node < 0 || node > this.countNodes - 1)
        System.err.println("Nó Inválido: " + node);
      int degree = 0;
      for (int j = 0; j < this.adjMatrix[node].length; ++j) {
        if (this.adjMatrix[node][j] != 0)
          ++degree;
      }
      return degree;
    }

    public int highestDegree() {
      int highest = 0;
      for (int i = 0; i < this.adjMatrix.length; i++) {
        if (highest < degree(i))
          highest = degree(i);
      }
      return highest;
    }

    public int lowestDegree() {
      int lowest = this.adjMatrix.length;
      for (int i = 0; i < this.adjMatrix.length; i++) {
        if (lowest > degree(i))
          lowest = degree(i);
      }
      return lowest;
    }

    public Graph complement() {
      Graph g2 = new Graph(this.countNodes);
      for (int i = 0; i < this.adjMatrix.length; i++) {
        for (int j = 0; j < this.adjMatrix.length; j++) {
          if (i != j && this.adjMatrix[i][j] == 0)
            g2.addEdge(i, j, 1);
        }
      }
      return g2;
    }
  
  
  }