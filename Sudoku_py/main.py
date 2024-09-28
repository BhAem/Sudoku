import copy
import pickle

class Grid:
    def __init__(self, grid_size=9, box_size=3):
        """构造函数初始化GRID_SIZE、BOX_SIZE和grid"""
        self.GRID_SIZE = grid_size
        self.BOX_SIZE = box_size
        self.grid = [[0] * grid_size for _ in range(grid_size)]

    def get_row(self, row):
        """获取某一行"""
        return self.grid[row]

    def get_column(self, col):
        """获取某一列"""
        return [self.grid[row][col] for row in range(self.GRID_SIZE)]

    def get_box(self, row, col):
        """获取某一个Box"""
        box_list = []
        start_row = (row // self.BOX_SIZE) * self.BOX_SIZE
        start_col = (col // self.BOX_SIZE) * self.BOX_SIZE
        for r in range(start_row, start_row + self.BOX_SIZE):
            for c in range(start_col, start_col + self.BOX_SIZE):
                box_list.append(self.grid[r][c])
        return box_list

    def print_grid(self):
        """打印数独网格"""
        for row in self.grid:
            print(' '.join(map(str, row)))


class Sudoku(Grid):
    def __init__(self, grid_size=9, box_size=3, parse=None):
        """Sudoku构造函数，初始化GRID_SIZE、BOX_SIZE和数独字符串parse"""
        super().__init__(grid_size, box_size)
        self.parse = parse

    def parse_string_to_grid(self):
        """解析输入的数独字符串，存进gird里"""
        for i in range(self.GRID_SIZE):
            for j in range(self.GRID_SIZE):
                self.grid[i][j] = int(self.parse[i * self.GRID_SIZE + j])

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

    def __eq__(self, other):
        """检查两个数独是否相等"""
        if isinstance(other, Sudoku):
            return self.grid == other.grid
        return False

    def clone(self):
        """深度复制数独对象"""
        return copy.deepcopy(self)

    def serialize(self, file_path):
        """序列化数独对象到文件"""
        with open(file_path, 'wb') as file:
            pickle.dump(self, file)

    def deserialize(file_path):
        """从文件反序列化数独对象"""
        with open(file_path, 'rb') as file:
            return pickle.load(file)

    def __str__(self):
        return "{GRID_SIZE: \"" + str(self.GRID_SIZE) + "\", BOX_SIZE: \"" + str(self.BOX_SIZE) + "\", parse: \"" + self.parse + "\"}"


def test_inference(sudoku):
    print("推理之前的Grid:")
    sudoku.print_grid()

    print("推理结果: ", sudoku.get_inference())

    print("推理之后的Grid:")
    sudoku.print_grid()


def test_clone(sudoku):
    sudoku_clone = sudoku.clone()
    result = (sudoku is sudoku_clone)
    print("sudoku和sudoku_clone是否相等: ", result)


def test_serialization(sudoku):
    sudoku.serialize('sudoku.txt')
    loaded_sudoku = Sudoku.deserialize('sudoku.txt')
    print("反序列化结果: ", loaded_sudoku)


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

    print("============推理测试============")
    test_inference(sudoku)
    print("============推理测试============")
    test_inference(sudoku2)

    print("============克隆测试============")
    test_clone(sudoku)

    print("============序列化测试============")
    test_serialization(sudoku)

    print("============比较测试============")
    test_compare(sudoku, sudoku2)
