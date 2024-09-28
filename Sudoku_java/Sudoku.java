import java.util.ArrayList;
import java.util.List;

public class Sudoku extends Grid implements Cloneable, Comparable<Sudoku> {

    private final String parse;
    // Sudoku构造函数，初始化GRID_SIZE、BOX_SIZE和数独字符串parse
    public Sudoku(int gridSize, int boxSize, String parse) {
        super(gridSize, boxSize);
        this.parse = parse;
    }

    // 解析输入的数独字符串，存进gird里
    public void parseStringToGrid() {
        int idx = 0;
        for (int row = 0; row < this.GRID_SIZE; row++) {
            for (int col = 0; col < this.GRID_SIZE; col++) {
                this.grid[row][col] = Character.getNumericValue(this.parse.charAt(idx++));
            }
        }
    }

    // 回溯法推理棋盘，得到各单元格候选值
    public boolean getInference() {
        for (int row = 0; row < this.GRID_SIZE; row++) {
            for (int col = 0; col < this.GRID_SIZE; col++) {
                if (this.grid[row][col] != 0)  // 找到空格
                    continue;
                for (int num = 1; num <= this.GRID_SIZE; num++) {  // 尝试数字 1 到 9
                    if (this.isValid(num, row, col)) {  // 检查是否可以放置数字
                        this.grid[row][col] = num;
                        if (this.getInference()) {  // 递归求解剩余数独
                            return true;
                        }
                        this.grid[row][col] = 0;
                    }
                }
                return false;  // 当前空格无解，返回上一层
            }
        }
        return true;
    }

    // 检查在特定位置放置某数字是否有效
    private boolean isValid(int num, int row, int col) {
        // 检查行
        if (this.getRow(row).contains(num)) {
            return false;
        }

        // 检查列
        if (this.getColumn(col).contains(num)) {
            return false;
        }

        // 检查 box
        if (this.getBox(row, col).contains(num)) {
            return false;
        }

        return true;
    }

    // 重写clone方法
    @Override
    public Grid clone() {
        try {
            Grid cloned = (Grid) super.clone();
            cloned.grid = new int[this.GRID_SIZE][this.GRID_SIZE];
            for (int i = 0; i < this.GRID_SIZE; i++) {
                cloned.grid[i] = this.grid[i].clone();
            }
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    // 比较两个Sudoku的gird的顺序是否相等
    @Override
    public int compareTo(Sudoku other) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (this.grid[row][col] != other.grid[row][col]) {
                    return Integer.compare(this.grid[row][col], other.grid[row][col]);
                }
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return "{GRID_SIZE: \"" + this.GRID_SIZE + "\", BOX_SIZE: \"" + this.BOX_SIZE + "\", parse: \"" + this.parse + "\"}";
    }
}
