# 实现Sudoku类

## 1. 业务代码

分别使用静态类型语言Java和动态类型语言Python实现了Suduku项目，主要功能包括：

（1）回溯法推理数独棋盘

（2）克隆（深拷贝）

（3）序列化、反序列化

（4）比较顺序是否一致

（5）外表化

## 2. 测试结果

### 静态类型（Java）

#### （1）推理测试

使用两个测试样例进行测试，推理使用的是回溯法，业务代码如下：

```java
// Sudoku.java

public class Sudoku extends Grid implements Cloneable, Comparable<Sudoku> {
	// ...
    
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
    
    // ...
}
```

测试代码如下：

```java
// SudokuTest.java

public static void testInference(Sudoku sudoku) {
    System.out.println("推理之前的Grid:");
    sudoku.printGrid();

    System.out.println("推理结果: " + sudoku.getInference());

    System.out.println("推理之后的Grid:");
    sudoku.printGrid();
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
    
    // ...
}
```

测试结果如下：

```
============推理测试============
推理之前的Grid:
0 1 7 9 0 3 6 0 0 
0 0 0 0 8 0 0 0 0 
9 0 0 0 0 0 5 0 7 
0 7 2 0 1 0 4 3 0 
0 0 0 4 0 2 0 7 0 
0 6 4 3 7 0 2 5 0 
7 0 1 0 0 0 0 6 5 
0 0 0 0 3 0 0 0 0 
0 0 5 6 0 1 7 2 0 
推理结果: true
推理之后的Grid:
4 1 7 9 5 3 6 8 2 
2 5 6 1 8 7 9 4 3 
9 8 3 2 4 6 5 1 7 
8 7 2 5 1 9 4 3 6 
5 3 9 4 6 2 8 7 1 
1 6 4 3 7 8 2 5 9 
7 9 1 8 2 4 3 6 5 
6 2 8 7 3 5 1 9 4 
3 4 5 6 9 1 7 2 8 
============推理测试============
推理之前的Grid:
0 0 9 0 0 0 3 0 0 
8 0 0 4 0 0 0 0 0 
0 4 5 0 0 0 7 9 0 
0 1 0 7 4 0 0 0 0 
7 0 0 0 6 0 0 0 2 
0 0 0 0 1 5 0 3 0 
0 6 7 0 0 0 4 8 0 
0 0 0 0 0 1 0 0 3 
0 0 8 0 0 0 9 0 0 
推理结果: true
推理之后的Grid:
6 2 9 8 5 7 3 1 4 
8 7 1 4 3 9 2 6 5 
3 4 5 1 2 6 7 9 8 
2 1 6 7 4 3 8 5 9 
7 5 3 9 6 8 1 4 2 
9 8 4 2 1 5 6 3 7 
5 6 7 3 9 2 4 8 1 
4 9 2 6 8 1 5 7 3 
1 3 8 5 7 4 9 2 6 
```

#### （2）克隆测试

对Sudoku类实现了Cloneable接口，并重写clone方法：

```java
// Sudoku.java

public class Sudoku extends Grid implements Cloneable, Comparable<Sudoku> {
    // ...
    
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
    
	// ...
}
```

测试代码如下：

```java
// SudokuTest.java

public static void testClone(Sudoku sudoku) {
    Sudoku clonedSudoku = (Sudoku) sudoku.clone();
    System.out.println("sudoku和clonedSudoku是否相等: "+sudoku.equals(clonedSudoku));
}

public static void main(String[] args) {
    String parse = "017903600000080000900000507072010430000402070064370250701000065000030000005601720";
    Sudoku sudoku = new Sudoku(9, 3, parse);
    sudoku.parseStringToGrid();
    
    // 克隆测试
    System.out.println("============克隆测试============");
    testClone(sudoku);
    
    // ...
}
```

测试结果如下：

```
============克隆测试============
sudoku和clonedSudoku是否相等: false
```

#### （3）序列化测试

对Grid类实现了Serializable接口：

```java
// Grid.java

public class Grid implements Iterable<Integer>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    
    // ...
}
```

测试代码如下：

```java
// SudokuTest.java

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

public static void main(String[] args) {
    String parse = "017903600000080000900000507072010430000402070064370250701000065000030000005601720";
    Sudoku sudoku = new Sudoku(9, 3, parse);
    sudoku.parseStringToGrid();
    
    // 序列化测试
    System.out.println("============序列化测试============");
    testSerialization(sudoku);
    
    // ...
}
```

测试结果如下：

```
============序列化测试============
反序列化结果: {GRID_SIZE: "9", BOX_SIZE: "3", parse: "017903600000080000900000507072010430000402070064370250701000065000030000005601720"}
```

#### （4）比较测试

对Sudoku类实现了Comparable接口，并重写compareTo方法，测试二者的gird中数字的顺序是否相同：

```java
// Sudoku.java

public class Sudoku extends Grid implements Cloneable, Comparable<Sudoku> {
	// ...
    
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
    
    // ...
}
```

测试代码：

```java
// SudokuTest.java

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
    
    // 比较测试
    System.out.println("============比较测试============");
    testCompare(sudoku, sudoku2);
    
    // ...
}
```

测试结果如下：

```
============比较测试============
sudoku不等于sudoku2
```

#### （5）外表化测试

对Sudoku类重写了toString方法：

```java
// Sudoku.java

public class Sudoku extends Grid implements Cloneable, Comparable<Sudoku> {
	// ...
    
    @Override
    public String toString() {
        return "{GRID_SIZE: \"" + this.GRID_SIZE + "\", BOX_SIZE: \"" + this.BOX_SIZE + "\", parse: \"" + this.parse + "\"}";
    }
    
    // ...
}
```

测试代码：

```java
// SudokuTest.java

public static void testExternalization(Sudoku sudoku, Sudoku sudoku2) {
    System.out.println("sudoku: " + sudoku);
    System.out.println("sudoku2: " + sudoku2);
}

public static void main(String[] args) {
    String parse = "017903600000080000900000507072010430000402070064370250701000065000030000005601720";
    Sudoku sudoku = new Sudoku(9, 3, parse);
    sudoku.parseStringToGrid();

    String parse2 = "009000300800400000045000790010740000700060002000015030067000480000001003008000900";
    Sudoku sudoku2 = new Sudoku(9, 3, parse2);
    sudoku2.parseStringToGrid();
    
    // 外表化测试
    System.out.println("============外表化测试============");
    testExternalization(sudoku, sudoku2);
    
    // ...
}
```

测试结果如下：

```
============外表化测试============
sudoku: {GRID_SIZE: "9", BOX_SIZE: "3", parse: "017903600000080000900000507072010430000402070064370250701000065000030000005601720"}
sudoku2: {GRID_SIZE: "9", BOX_SIZE: "3", parse: "009000300800400000045000790010740000700060002000015030067000480000001003008000900"}
```

### 动态类型（Python）

#### （1）推理测试

使用两个测试样例进行测试，推理使用的是回溯法，业务代码如下：

```python
class Sudoku(Grid):
    // ...
    
    def get_inference(self):
        """回溯法推理棋盘，得到各单元格候选值"""
        for row in range(self.GRID_SIZE):
            for col in range(self.GRID_SIZE):
                if self.grid[row][col] != 0:
                    continue
                for num in range(1, self.GRID_SIZE + 1):  # 尝试数字 1 到 9
                    if self.is_valid(row, col, num):      # 检查是否可以放置数字
                        self.grid[row][col] = num
                        if self.get_inference():          # 递归求解剩余数独
                            return True
                        self.grid[row][col] = 0  # 回溯
                return False
        return True

    def is_valid(self, row, col, num):
        """检查在特定位置放置某数字是否有效"""
        # 检查行
        if num in self.get_row(row):
            return False
        # 检查列
        if num in self.get_column(col):
            return False
        # 检查 3x3 宫格
        if num in self.get_box(row, col):
            return False
        return True
    
    // ...
```

测试代码如下：

```python
def test_inference(sudoku):
    print("推理之前的Grid:")
    sudoku.print_grid()

    print("推理结果: ", sudoku.get_inference())

    print("推理之后的Grid:")
    sudoku.print_grid()

if __name__ == "__main__":
    parse = "017903600000080000900000507072010430000402070064370250701000065000030000005601720"
    sudoku = Sudoku(9, 3, parse)
    sudoku.parse_string_to_grid()  # 解析数独字符串

    parse2 = "009000300800400000045000790010740000700060002000015030067000480000001003008000900"
    sudoku2 = Sudoku(9, 3, parse2)
    sudoku2.parse_string_to_grid()

    print("============推理测试============")
    test_inference(sudoku)
    print("============推理测试============")
    test_inference(sudoku2)
    
    // ...
```

测试结果如下：

```
============推理测试============
推理之前的Grid:
0 1 7 9 0 3 6 0 0
0 0 0 0 8 0 0 0 0
9 0 0 0 0 0 5 0 7
0 7 2 0 1 0 4 3 0
0 0 0 4 0 2 0 7 0
0 6 4 3 7 0 2 5 0
7 0 1 0 0 0 0 6 5
0 0 0 0 3 0 0 0 0
0 0 5 6 0 1 7 2 0
推理结果:  True
推理之后的Grid:
4 1 7 9 5 3 6 8 2
2 5 6 1 8 7 9 4 3
9 8 3 2 4 6 5 1 7
8 7 2 5 1 9 4 3 6
5 3 9 4 6 2 8 7 1
1 6 4 3 7 8 2 5 9
7 9 1 8 2 4 3 6 5
6 2 8 7 3 5 1 9 4
3 4 5 6 9 1 7 2 8
============推理测试============
推理之前的Grid:
0 0 9 0 0 0 3 0 0
8 0 0 4 0 0 0 0 0
0 4 5 0 0 0 7 9 0
0 1 0 7 4 0 0 0 0
7 0 0 0 6 0 0 0 2
0 0 0 0 1 5 0 3 0
0 6 7 0 0 0 4 8 0
0 0 0 0 0 1 0 0 3
0 0 8 0 0 0 9 0 0
推理结果:  True
推理之后的Grid:
6 2 9 8 5 7 3 1 4
8 7 1 4 3 9 2 6 5
3 4 5 1 2 6 7 9 8
2 1 6 7 4 3 8 5 9
7 5 3 9 6 8 1 4 2
9 8 4 2 1 5 6 3 7
5 6 7 3 9 2 4 8 1
4 9 2 6 8 1 5 7 3
1 3 8 5 7 4 9 2 6
```

#### （2）克隆测试

使用copy库进行深拷贝：

```python
class Sudoku(Grid):
	// ...
    
    def clone(self):
        """深度复制数独对象"""
        return copy.deepcopy(self)
    
    // ...
```

测试代码如下：

```python
def test_clone(sudoku):
    sudoku_clone = sudoku.clone()
    result = (sudoku is sudoku_clone)
    print("sudoku和sudoku_clone是否相等: ", result)

    
if __name__ == "__main__":
    parse = "017903600000080000900000507072010430000402070064370250701000065000030000005601720"
    sudoku = Sudoku(9, 3, parse)
    sudoku.parse_string_to_grid()  # 解析数独字符串

    print("============克隆测试============")
    test_clone(sudoku)
    
    // ...
```

测试结果如下：

```
============克隆测试============
sudoku和sudoku_clone是否相等:  False
```

#### （3）序列化测试

使用pickle库实现序列化和反序列化：

```python
class Sudoku(Grid):
	// ...
    
    def serialize(self, file_path):
        """序列化数独对象到文件"""
        with open(file_path, 'wb') as file:
            pickle.dump(self, file)

    def deserialize(file_path):
        """从文件反序列化数独对象"""
        with open(file_path, 'rb') as file:
            return pickle.load(file)
        
	// ...
```

测试代码如下：

```python
def test_serialization(sudoku):
    sudoku.serialize('sudoku.txt')
    loaded_sudoku = Sudoku.deserialize('sudoku.txt')
    print("反序列化结果: ", loaded_sudoku)


if __name__ == "__main__":
    parse = "017903600000080000900000507072010430000402070064370250701000065000030000005601720"
    sudoku = Sudoku(9, 3, parse)
    sudoku.parse_string_to_grid()  # 解析数独字符串

    print("============序列化测试============")
    test_serialization(sudoku)
    
    // ...
```

测试结果如下：

```
============序列化测试============
反序列化结果:  {GRID_SIZE: "9", BOX_SIZE: "3", parse: "017903600000080000900000507072010430000402070064370250701000065000030000005601720"}
```

#### （4）比较测试

使用python类的magic方法\__eq__实现比较：

```python
class Sudoku(Grid):
	// ...
    
    def __eq__(self, other):
        """检查两个数独是否相等"""
        if isinstance(other, Sudoku):
            return self.grid == other.grid
        return False
    
    // ...
```

测试代码如下：

```python
def test_compare(sudoku, sudoku2):
    comparison = (sudoku == sudoku2)
    if comparison:
        print("sudoku等于sudoku2")
    else:
        print("sudoku不等于sudoku2")


if __name__ == "__main__":
    parse = "017903600000080000900000507072010430000402070064370250701000065000030000005601720"
    sudoku = Sudoku(9, 3, parse)
    sudoku.parse_string_to_grid()  # 解析数独字符串

    parse2 = "009000300800400000045000790010740000700060002000015030067000480000001003008000900"
    sudoku2 = Sudoku(9, 3, parse2)
    sudoku2.parse_string_to_grid()

    print("============比较测试============")
    test_compare(sudoku, sudoku2)
    
    // ...
```

测试结果如下：

```
============比较测试============
sudoku不等于sudoku2
```

#### （5）外表化测试

对Sudoku类重写了\__str__方法：

```java
class Sudoku(Grid):
	// ...
    
    def __str__(self):
        return "{GRID_SIZE: \"" + str(self.GRID_SIZE) + "\", BOX_SIZE: \"" + str(self.BOX_SIZE) + "\", parse: \"" + self.parse + "\"}"

    // ...
```

测试代码：

```java
def test_externalization(sudoku, sudoku2):
    print("sudoku: ", sudoku)
    print("sudoku2: ", sudoku2)


if __name__ == "__main__":
    parse = "017903600000080000900000507072010430000402070064370250701000065000030000005601720"
    sudoku = Sudoku(9, 3, parse)
    sudoku.parse_string_to_grid()  # 解析数独字符串

    parse2 = "009000300800400000045000790010740000700060002000015030067000480000001003008000900"
    sudoku2 = Sudoku(9, 3, parse2)
    sudoku2.parse_string_to_grid()

    print("============外表化测试============")
    test_externalization(sudoku, sudoku2)
    
    // ...
```

测试结果如下：

```
============外表化测试============
sudoku:  {GRID_SIZE: "9", BOX_SIZE: "3", parse: "017903600000080000900000507072010430000402070064370250701000065000030000005601720"}
sudoku2:  {GRID_SIZE: "9", BOX_SIZE: "3", parse: "009000300800400000045000790010740000700060002000015030067000480000001003008000900"}
```
