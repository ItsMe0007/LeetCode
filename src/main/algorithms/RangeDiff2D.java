package main.algorithms;

@SuppressWarnings("unused")
public class RangeDiff2D {

    private final int minRow;
    private final int minCol;
    private final int rowSize;
    private final int colSize;
    private final int[][] diff;

    public RangeDiff2D(int minRow, int maxRow, int minCol, int maxCol) {
        this.minRow = minRow;
        this.minCol = minCol;
        this.rowSize = maxRow - minRow + 1;
        this.colSize = maxCol - minCol + 1;
        this.diff = new int[rowSize + 1][colSize + 1];
    }

    public RangeDiff2D(int rowSize, int colSize) {
        this(0, rowSize - 1, 0, colSize - 1);
    }

    public void addRangeInclusive(int row1, int col1, int row2, int col2, int delta) {
        row1 -= minRow;
        col1 -= minCol;
        row2 -= minRow;
        col2 -= minCol;
        diff[row1][col1] += delta;
        diff[row2 + 1][col1] -= delta;
        diff[row1][col2 + 1] -= delta;
        diff[row2 + 1][col2 + 1] += delta;
    }

    public int[][] toArray() {
        int[][] res = new int[rowSize][colSize];

        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                int val = diff[i][j];
                if (i > 0) val += res[i - 1][j];
                if (j > 0) val += res[i][j - 1];
                if (i > 0 && j > 0) val -= res[i - 1][j - 1];
                res[i][j] = val;
            }
        }
        return res;
    }
}
