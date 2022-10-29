import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collections;

class Tupla {
    int x;
    int y;

    public Tupla(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("(%d,%d) ", x, y);
    }
}

public class Maze {
    private int[][] matrix;
    private Tupla start;
    private Tupla end;
    private int rows, columns;

    public Maze(String fileName) throws IOException {

        File file = new File(fileName);
        FileInputStream fileStream = new FileInputStream(file);
        BufferedReader fileBuffer = new BufferedReader(new InputStreamReader(fileStream));

        String str;

        int rows = 0;
        int columns = 0;

        while ((str = fileBuffer.readLine()) != null) {
            String[] splitNodes = str.split("");
            columns = splitNodes.length;
            rows++;
        }

        this.rows = rows;
        this.columns = columns;
        matrix = new int[rows][columns];

        fileStream.getChannel().position(0);
        fileBuffer = new BufferedReader(new InputStreamReader(fileStream));

        int i = 0;
        while ((str = fileBuffer.readLine()) != null) {

            String[] splitNodes = str.split("");

            for (int j = 0; j < splitNodes.length; j++) {
                if (splitNodes[j].equals("S")) {
                    matrix[i][j] = 2;
                    start = new Tupla(i, j);
                } else if (splitNodes[j].equals("E")) {
                    matrix[i][j] = 3;
                    end = new Tupla(i, j);
                } else if (splitNodes[j].equals("#") || splitNodes[j].equals("â") || 
                    splitNodes[j].equals("–") || splitNodes[j].equals("ˆ"))
                    matrix[i][j] = 0;
                else {
                    matrix[i][j] = 1;
                }

            }

            i++;
        }

        fileBuffer.close();
        fileStream.close();
    }

    public void printLabirinto() {
        System.out.println("\nLabirinto: \n");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 1)
                    System.out.print(' ');
                else if (matrix[i][j] == 2)
                    System.out.print('S');
                else if (matrix[i][j] == 3)
                    System.out.print('E');
                else
                    System.out.print('#');
            }
            System.out.println();
        }
        System.out.println();
    }

    
    public ArrayList<Tupla> search_largura() {
        Queue<Tupla> Q = new LinkedList<Tupla>();
        Q.add(start);

        boolean[][] visited = new boolean[this.rows][this.columns];
        
        visited[start.x][start.y] = true;
        Tupla[][] paths = new Tupla[this.rows][this.columns];

        Tupla[] d_tuplas = { 
            new Tupla(-1, 0), 
            new Tupla(1, 0), 
            new Tupla(0, -1), 
            new Tupla(0, 1) 
        };

        while (!Q.isEmpty()) {
            Tupla u = Q.poll(); 

            for (Tupla d : d_tuplas) {
                int i = u.x + d.x; 
                int j = u.y + d.y; 

                if ((i >= 0 && i < rows && j >= 0 && j < columns) 
                    && !visited[i][j] && matrix[i][j] == 1) {

                    Q.add(new Tupla(i, j));
                    visited[i][j] = true;
                    paths[i][j] = u;
                }
            }
        }

        if (!visited[end.x][end.y])
            return null;
            
        ArrayList<Tupla> caminho = new ArrayList<Tupla>();
        Tupla aux = end;

        while (paths[aux.x][aux.y] != null) {
            caminho.add(aux);
            aux = paths[aux.x][aux.y];
        }

        caminho.add(start); 
        Collections.reverse(caminho);

        System.out.print("Caminho: ");
        for (Tupla v : caminho) {
            System.out.print(String.format("(%d,%d) ", v.x, v.y));
        }
        System.out.println("/n");

        return caminho;
    }
}
