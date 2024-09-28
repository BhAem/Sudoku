import java.io.*;
import java.util.*;

public class Grid implements Iterable<Integer>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected int[][] grid;
    protected final int BOX_SIZE;
    protected final int GRID_SIZE;

    // 构造函数初始化GRID_SIZE、BOX_SIZE和grid
    public Grid(int gridSize, int boxSize) {
        this.GRID_SIZE = gridSize;
        this.BOX_SIZE = boxSize;
        this.grid = new int[GRID_SIZE][GRID_SIZE]; // Initialize the grid
    }

    // 打印数独网格
    public void printGrid() {
        for (int[] row : grid) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    // 获取某一行
    public List<Integer> getRow(int row) {
        List<Integer> rowList = new ArrayList<>();
        for (int col = 0; col < GRID_SIZE; col++) {
            rowList.add(grid[row][col]);
        }
        return rowList;
    }

    // 获取某一列
    public List<Integer> getColumn(int col) {
        List<Integer> colList = new ArrayList<>();
        for (int row = 0; row < GRID_SIZE; row++) {
            colList.add(grid[row][col]);
        }
        return colList;
    }

    // 获取某一个Box
    public List<Integer> getBox(int row, int col) {
        List<Integer> boxList = new ArrayList<>();
        int startRow = (row / BOX_SIZE) * BOX_SIZE;
        int startCol = (col / BOX_SIZE) * BOX_SIZE;
        for (int r = startRow; r < startRow + BOX_SIZE; r++) {
            for (int c = startCol; c < startCol + BOX_SIZE; c++) {
                boxList.add(grid[r][c]);
            }
        }
        return boxList;
    }

    @Override
    public java.util.Iterator<Integer> iterator() {
        List<Integer> flatGrid = new ArrayList<>();
        for (int[] row : grid) {
            for (int cell : row) {
                flatGrid.add(cell);
            }
        }
        return flatGrid.iterator();
    }
}
