package main.algorithms;

@SuppressWarnings("unused")
public class Matrix {
    private final int m;
    private final int n;
    private final long[][] mat;

    public Matrix(long[][] mat) {
        this.m = mat.length;
        this.n = mat[0].length;
        this.mat = mat;
    }

    private Matrix(int m, int n) {
        this.m = m;
        this.n = n;
        this.mat = new long[m][n];
    }

    public static Matrix zero(int m, int n) {
        return new Matrix(m, n);
    }

    public static Matrix identity(int n) {
        Matrix res = new Matrix(n, n);
        for (int i = 0; i < n; i++) res.mat[i][i] = 1;
        return res;
    }

    public Matrix add(Matrix other) {
        long[][] tempMat = new long[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                tempMat[i][j] = mat[i][j] + other.mat[i][j];
            }
        }
        return new Matrix(tempMat);
    }

    public Matrix add(Matrix other, long mod) {
        long[][] tempMat = new long[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                tempMat[i][j] = (mat[i][j] + other.mat[i][j]) % mod;
            }
        }
        return new Matrix(tempMat);
    }

    public Matrix subtract(Matrix other) {
        long[][] tempMat = new long[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                tempMat[i][j] = mat[i][j] - other.mat[i][j];
            }
        }
        return new Matrix(tempMat);
    }

    public Matrix subtract(Matrix other, long mod) {
        long[][] tempMat = new long[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                tempMat[i][j] = (mat[i][j] - other.mat[i][j]) % mod;
                if (tempMat[i][j] < 0) tempMat[i][j] += mod;
            }
        }
        return new Matrix(tempMat);
    }

    public Matrix multiply(Matrix other) {
        long[][] tempMat = new long[m][other.n];

        for (int i = 0; i < m; i++) {
            for (int k = 0; k < n; k++) {
                if (mat[i][k] == 0) continue;
                for (int j = 0; j < other.n; j++) {
                    tempMat[i][j] = tempMat[i][j] + mat[i][k] * other.mat[k][j];
                }
            }
        }
        return new Matrix(tempMat);
    }

    public Matrix multiply(Matrix other, long mod) {
        long[][] tempMat = new long[m][other.n];

        for (int i = 0; i < m; i++) {
            for (int k = 0; k < n; k++) {
                if (mat[i][k] == 0) continue;
                for (int j = 0; j < other.n; j++) {
                    tempMat[i][j] = (tempMat[i][j] + mat[i][k] * other.mat[k][j]) % mod;
                }
            }
        }
        return new Matrix(tempMat);
    }

/*
TODO TEST cached version for performance

    public Matrix multiply(Matrix other) {
        long[][] tempMat = new long[m][other.n];

        for (int i = 0; i < m; i++) {
            long[] tempRow = tempMat[i];
            for (int k = 0; k < n; k++) {
                long val = mat[i][k];
                if (val == 0) continue;
                long[] otherRow = other.mat[k];
                for (int j = 0; j < other.n; j++) {
                    tempRow[j] += val * otherRow[j];
                }
            }
        }
        return new Matrix(tempMat);
    }

    public Matrix multiply(Matrix other, long mod) {
        long[][] tempMat = new long[m][other.n];

        for (int i = 0; i < m; i++) {
            long[] tempRow = tempMat[i];
            for (int k = 0; k < n; k++) {
                long val = mat[i][k];
                if (val == 0) continue;
                long[] otherRow = other.mat[k];
                for (int j = 0; j < other.n; j++) {
                    tempRow[j] = (tempRow[j] + val * otherRow[j]) % mod;
                }
            }
        }
        return new Matrix(tempMat);
    }
*/

    public Matrix pow(long exp) {
        Matrix res = identity(n);
        Matrix base = this;

        while (exp > 0) {
            if ((exp & 1) == 1) res = res.multiply(base);
            exp >>= 1;
            if (exp > 0) base = base.multiply(base);
        }
        return res;
    }

    public Matrix pow(long exp, long mod) {
        Matrix res = identity(n);
        Matrix base = this;

        while (exp > 0) {
            if ((exp & 1) == 1) res = res.multiply(base, mod);
            exp >>= 1;
            if (exp > 0) base = base.multiply(base, mod);
        }
        return res;
    }

    public Matrix transpose() {
        long[][] tempMat = new long[n][m];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                tempMat[j][i] = mat[i][j];
            }
        }

        return new Matrix(tempMat);
    }

    public Matrix concatHorizontally(Matrix other) {
        long[][] tempMat = new long[m][n + other.n];
        for (int i = 0; i < m; i++) {
            System.arraycopy(mat[i], 0, tempMat[i], 0, n);
            System.arraycopy(other.mat[i], 0, tempMat[i], n, other.n);
        }
        return new Matrix(tempMat);
    }

    public Matrix concatVertically(Matrix other) {
        long[][] tempMat = new long[m + other.m][n];
        for (int i = 0; i < m; i++) {
            System.arraycopy(mat[i], 0, tempMat[i], 0, n);
        }
        for (int i = 0; i < other.m; i++) {
            System.arraycopy(other.mat[i], 0, tempMat[m + i], 0, n);
        }
        return new Matrix(tempMat);
    }

    public Matrix copy() {
        long[][] tempMat = new long[m][n];
        for (int i = 0; i < m; i++) System.arraycopy(mat[i], 0, tempMat[i], 0, n);
        return new Matrix(tempMat);
    }

    @Override
    public String toString() {
        return java.util.Arrays.deepToString(mat);
    }
}