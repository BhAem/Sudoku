import java.io.*;

public class SudokuTest {

    public static void testInference(Sudoku sudoku) {
        System.out.println("推理之前的Grid:");
        sudoku.printGrid();

        System.out.println("推理结果: " + sudoku.getInference());

        System.out.println("推理之后的Grid:");
        sudoku.printGrid();
    }

    public static void testClone(Sudoku sudoku) {
        Sudoku clonedSudoku = (Sudoku) sudoku.clone();
        System.out.println("sudoku和clonedSudoku是否相等: "+sudoku.equals(clonedSudoku));
    }

    public static void testSerialization(Sudoku sudoku) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("sudoku.txt"));
             ObjectInputStream ois = new ObjectInputStream(new FileInputStream("sudoku.txt"))) {

            oos.writeObject(sudoku);

            Sudoku deserializedSudoku = (Sudoku) ois.readObject();
            System.out.println("反序列化结果: " + deserializedSudoku);

            oos.close();
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void testCompare(Sudoku sudoku, Sudoku sudoku2) {
        int comparison = sudoku.compareTo(sudoku2);
        if (comparison == 0) {
            System.out.println("sudoku等于sudoku2");
        } else {
            System.out.println("sudoku不等于sudoku2");
        }
    }

    public static void main(String[] args) {
        String parse = "017903600000080000900000507072010430000402070064370250701000065000030000005601720";
        Sudoku sudoku = new Sudoku(9, 3, parse);
        sudoku.parseStringToGrid();

        String parse2 = "009000300800400000045000790010740000700060002000015030067000480000001003008000900";
        Sudoku sudoku2 = new Sudoku(9, 3, parse2);
        sudoku2.parseStringToGrid();

        // 推理测试
        System.out.println("============推理测试============");
        testInference(sudoku);
        System.out.println("============推理测试============");
        testInference(sudoku2);

        // 克隆测试
        System.out.println("============克隆测试============");
        testClone(sudoku);

        // 序列化测试
        System.out.println("============序列化测试============");
        testSerialization(sudoku);

        // 比较测试
        System.out.println("============比较测试============");
        testCompare(sudoku, sudoku2);
    }
}
