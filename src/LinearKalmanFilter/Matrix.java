package LinearKalmanFilter;

/**
 * 矩阵
 */
public class Matrix implements Cloneable {
	protected int nColumn;
	protected int nRow;
	protected double data[][];

//	// 设置列
//	public void setColumn(int newVal) {
//		if (newVal <= 0)
//			throw new IllegalArgumentException("Matrix: set column.");
//
//		nColumn = newVal;
//		data = new double[nRow][newVal];
//	}
//
//	// 设置行
//	public void setRow(int newVal) {
//		if (newVal <= 0)
//			throw new IllegalArgumentException("Matrix: set row.");
//
//		nColumn = newVal;
//		data = new double[nRow][nColumn];
//	}

	// 获得列
	public int getColumn() {
		return nColumn;
	}

	// 获得行
	public int getRow() {
		return nRow;
	}

	// 获得矩阵
	public double[][] getData() {
		return data;
	}

	/**
	 * @param i
	 * @return 如果i是偶数，返回1.是奇数返回-1
	 */
	public int changeSign(int i) {
		return (i % 2 == 0 ? 1 : -1);
	}

	// 矩阵相加
	public void add(Matrix m) {
		for (int j = 0; j < nColumn; j++)
			for (int i = 0; i < nRow; i++)
				data[i][j] += m.data[i][j];
	}

	// 矩阵相减
	public void sub(Matrix m) {
		for (int j = 0; j < nColumn; j++)
			for (int i = 0; i < nRow; i++)
				data[i][j] -= m.data[i][j];
	}

	// 矩阵的每一个元素都乘以一个数
	public void scaleMul(double s) {
		for (int j = 0; j < nColumn; j++)
			for (int i = 0; i < nRow; i++)
				data[i][j] *= s;
	}

	// 矩阵相乘
	public void mul(Matrix m1, Matrix m2) {
		for (int i = 0; i < nRow; i++) {
			for (int j = 0; j < nColumn; j++) {
				data[i][j] = 0.0;
				for (int k = 0; k < nColumn; k++) {
					data[i][j] += m1.data[i][k] * m2.data[k][j];
				}
			}
		}
	}

	// 将本矩阵左乘m矩阵
	public Matrix mul(Matrix m) {
		Matrix r = new Matrix(nRow, m.nColumn);
		r.mul(this, m);
		return r;
	}

	public Vector vectorMul(Vector v) {
		Vector r = new Vector(nRow);

		for (int i = 0; i < nRow; i++) {
			r.data[i][0] = 0.0;
			for (int j = 0; j < nColumn; j++) {
				r.data[i][0] += data[i][j] * v.data[j][0];
			}
		}

		return r;
	}

	/**
	 * 矩阵的转置
	 * 
	 * @return
	 */
	public Matrix transpose() {
		Matrix r = new Matrix(nColumn, nRow);

		for (int j = 0; j < nColumn; j++) {
			for (int i = 0; i < nRow; i++) {
				r.data[j][i] = data[i][j];
			}
		}
		return r;
	}

	/**
	 * 置为零矩阵，Matlab中用zeros函数产生一个零矩阵：zeros(n)即产生一个 n*n 的零矩阵。
	 */
	public void setZero() {
		for (int j = 0; j < nColumn; j++)
			for (int i = 0; i < nRow; i++)
				data[i][j] = 0.0;
	}

	public Matrix(int m, int n) {
		if (m <= 0 || n <= 0)
			throw new IllegalArgumentException("Matrix: invalid size.");
		nRow = m;
		nColumn = n;
		data = new double[m][n];
	}

	public Matrix(double[][] theData) {
		data = theData;
		nRow = data.length;
		nColumn = data[0].length;
	}

	public void copyTo(Matrix m) {
		for (int j = 0; j < nColumn; j++) {
			for (int i = 0; i < nRow; i++) {
				m.data[i][j] = data[i][j];
			}
		}
	}

	public Matrix copy() {
		Matrix m = new Matrix(nRow, nColumn);
		copyTo(m);
		return m;
	}

	public static Matrix zeros(int m, int n) {
		Matrix r = new Matrix(m, n);
		r.setZero();
		return r;
	}

	/**
	 * 向量类
	 */
	public static class Vector extends Matrix {
		public int getColumn() {
			return 1;
		}

		public int getRow() {
			return nRow;
		}

		public double get(int n) {
			return data[n][0];
		}

		public void setColumn(int newVal) {
			throw new IllegalArgumentException("Vector: set column.");
		}

		public void setRow(int newVal) {
			if (newVal <= 0)
				throw new IllegalArgumentException("Vector: set row.");

			nRow = newVal;
			data = new double[newVal][1];
		}

		public void set(int n, double v) {
			data[n][0] = v;
		}

		public void vectorMulOf(Matrix m, Vector v) {
			for (int i = 0; i < m.nRow; i++) {
				data[i][0] = 0.0;
				for (int j = 0; j < m.nColumn; j++) {
					data[i][0] += m.data[i][j] * v.data[j][0];
				}
			}
		}

		public Vector(int size) {
			super(size, 1);
		}

		public Vector(double[] theData) {
			super(theData.length, 1);
			for (int i = 0; i < nRow; i++)
				data[i][0] = theData[i];
		}

		public void copyTo(Vector v) {
			for (int i = 0; i < nRow; i++) {
				v.data[i][0] = data[i][0];
			}
		}

		public Matrix copy() {
			Vector m = new Vector(nRow);
			copyTo(m);
			return m;
		}

		public void setZero() {
			for (int i = 0; i < nRow; i++)
				data[i][0] = 0.0;
		}

		public static Vector zeros(int n) {
			Vector r = new Vector(n);
			r.setZero();
			return r;
		}
	}

	/**
	 * 
	 * 方阵
	 */
	public static class SquareMatrix extends Matrix {
		// 构造函数，方阵
		public SquareMatrix(int n) {
			super(n, n);
		}

		// 构造函数，方阵。注意传入的data应该为方阵
		public SquareMatrix(double[][] data) {
			super(data);
		}

		// 返回列
		public int getColumn() {
			return nRow;
		}

		// 返回行
		public int getRow() {
			return nRow;
		}

		// 设置列，方阵被初始化
		public void setColumn(int newVal) {
			if (newVal <= 0)
				throw new IllegalArgumentException("SquareMatrix: set column.");

			nRow = newVal;
			data = new double[nRow][nRow];
		}

		// 设置行，方阵被初始化
		public void setRow(int newVal) {
			if (newVal <= 0)
				throw new IllegalArgumentException("SquareMatrix: set row.");

			nRow = newVal;
			data = new double[newVal][nRow];
		}

		// 方阵的相乘
		public Matrix mul(SquareMatrix m) {
			SquareMatrix r = new SquareMatrix(nRow);
			r.mul(this, m);
			return r;
		}

		// 将传入矩阵转置，遍历右上角
		public static void transpose(SquareMatrix m) {
			double t;
			for (int i = 0; i < m.nRow; i++) {
				for (int j = 0; j < i; j++) {
					t = m.data[i][j];
					m.data[i][j] = m.data[j][i];
					m.data[j][i] = t;
				}
			}
		}

		// 获得本转置矩阵
		public Matrix transpose() {
			SquareMatrix r = new SquareMatrix(nColumn);
			double[][] rv = r.getData();

			for (int i = 0; i < nRow; i++) {
				for (int j = 0; j < nColumn; j++) {
					rv[j][i] = data[i][j];
				}
			}
			return r;
		}

		public void setEye() {
			setZero();// 设置为零矩阵
			for (int i = 0; i < nRow; i++)
				data[i][i] = 1.0;
		}

		public static SquareMatrix eyes(int n) {
			SquareMatrix r = new SquareMatrix(n);
			r.setEye();
			return r;
		}

		// 或得零矩阵
		public static SquareMatrix zeros(int n) {
			SquareMatrix r = new SquareMatrix(n);
			r.setZero();
			return r;
		}

		/**
		 * LU分解
		 */
		private LUDecomposition luDecomposition;
		// 逆矩阵
		private SquareMatrix eyeForluInverse;

		/**
		 * 
		 * @param m
		 */
		public void luInverseOf(SquareMatrix m) {
			if (null == luDecomposition)
				luDecomposition = new LUDecomposition(nRow, nColumn);
			luDecomposition.decomposite(m);
			if (null == eyeForluInverse)
				eyeForluInverse = SquareMatrix.eyes(nRow);
			copyFromMatrixWithPivoting(eyeForluInverse, luDecomposition
					.getPivotVector());
			// Solve L*Y = I(piv,:)
			for (int k = 0; k < nColumn; k++) {
				for (int i = k + 1; i < nColumn; i++) {
					for (int j = 0; j < nColumn; j++) {
						data[i][j] -= data[k][j] * luDecomposition.data[i][k];
					}
				}
			}
			// Solve U*X = Y;
			for (int k = nColumn - 1; k >= 0; k--) {
				for (int j = 0; j < nColumn; j++) {
					data[k][j] /= luDecomposition.data[k][k];
				}
				for (int i = 0; i < k; i++) {
					for (int j = 0; j < nColumn; j++) {
						data[i][j] -= data[k][j] * luDecomposition.data[i][k];
					}
				}
			}
		}
        /**
         * 旋转拷贝
         * @param m
         * @param columnIndices
         */
		public void copyFromMatrixWithPivoting(SquareMatrix m,
				int columnIndices[]) {
			for (int i = 0; i < nRow; i++) {
				for (int j = 0; j < nColumn; j++) {
					data[i][j] = m.data[i][columnIndices[j]];
				}
			}
		}

		public void inverseOf(SquareMatrix m) {
			cofactorOf(m);
			transpose(this);
			scaleMul(1.0 / m.determinant());
		}

		public SquareMatrix inverse() {
			SquareMatrix mat = (SquareMatrix) (cofactor().transpose());
			mat.scaleMul(1.0 / determinant());
			return mat;
		}

		private SquareMatrix subMatrixForCofactor;

		public void cofactorOf(SquareMatrix m) {
			if (null == subMatrixForCofactor)
				subMatrixForCofactor = new SquareMatrix(nRow - 1);

			for (int i = 0; i < nRow; i++) {
				for (int j = 0; j < nColumn; j++) {
					subMatrixForCofactor.subMatrixOf(m, i, j);
					data[i][j] = m.changeSign(i + j)
							* subMatrixForCofactor.determinant();
				}
			}
		}

		public SquareMatrix cofactor() {
			SquareMatrix m = new SquareMatrix(nRow);
			m.cofactorOf(this);
			return m;
		}

		public void subMatrixOf(SquareMatrix m, int excludingRow,
				int excludingCol) {
			int r = -1;
			for (int i = 0; i < m.nRow; i++) {
				if (i == excludingRow)
					continue;

				r++;
				int c = -1;
				for (int j = 0; j < m.nColumn; j++) {
					if (j == excludingCol)
						continue;
					data[r][++c] = m.data[i][j];
				}
			}
		}

		public SquareMatrix createSubMatrix(int excludingRow, int excludingCol) {
			SquareMatrix m = new SquareMatrix(nRow - 1);
			m.subMatrixOf(this, excludingRow, excludingCol);
			return m;
		}

		private SquareMatrix subMatrixForDeterminat;

		public double determinant() {
			if (3 == nRow)
				return (data[0][0] * data[1][1] * data[2][2] + data[0][1]
						* data[1][2] * data[2][0] + data[0][2] * data[1][0]
						* data[2][1] - data[0][2] * data[1][1] * data[2][0]
						- data[0][1] * data[1][0] * data[2][2] - data[0][0]
						* data[2][1] * data[1][2]);

			if (2 == nRow)
				return ((data[0][0] * data[1][1]) - (data[0][1] * data[1][0]));

			if (null == subMatrixForDeterminat)
				subMatrixForDeterminat = new SquareMatrix(nRow - 1);
			double sum = 0.0;
			for (int i = 0; i < nRow; i++) {
				subMatrixForDeterminat.subMatrixOf(this, 0, i);
				sum += changeSign(i) * data[0][i]
						* (subMatrixForDeterminat.determinant());
			}

			return sum;
		}
	}

	/**
	 * LU分解
	 * 
	 */
	public static class LUDecomposition extends Matrix {
		private int pivotVectorSign;
		private int pivotVector[];
		private double LUcolj[];

		public LUDecomposition(int m, int n) {
			super(m, n);
			pivotVector = new int[m];
			LUcolj = new double[m];
		}

		public int[] getPivotVector() {
			return pivotVector;
		}

		public int getPivotVectorSign() {
			return pivotVectorSign;
		}

		public void decomposite(Matrix m) {
			double LUrowi[];

			m.copyTo(this);
			initPivotVector();

			for (int j = 0; j < nColumn; j++) {
				for (int i = 0; i < nRow; i++)
					LUcolj[i] = m.data[i][j];

				// Apply previous transformations
				for (int i = 0; i < nRow; i++) {
					int kmax = Math.min(i, j);
					double s = 0.0;

					LUrowi = data[i];
					for (int k = 0; k < kmax; k++)
						s += LUrowi[k] * LUcolj[k];

					LUrowi[j] = LUcolj[i] -= s;
				}

				// Find pivot
				int p = j;
				for (int i = j + 1; i < nRow; i++)
					if (Math.abs(LUcolj[i]) > Math.abs(LUcolj[p]))
						p = i;

				if (p != j) {
					for (int k = 0; k < nColumn; k++) {
						double t = data[p][k];
						data[p][k] = data[j][k];
						data[j][k] = t;
					}
					int k = pivotVector[p];
					pivotVector[p] = pivotVector[j];
					pivotVector[j] = k;
					pivotVectorSign = -pivotVectorSign;
				}

				// Compute multipliers
				if (j < nRow & data[j][j] != 0.0)
					for (int i = j + 1; i < nRow; i++)
						data[i][j] /= data[j][j];
			} // for j
		}

		private void initPivotVector() {
			pivotVectorSign = 1;
			for (int i = 0; i < nRow; i++)
				pivotVector[i] = i;
		}

		public double determinant(SquareMatrix m) {
			decomposite(m);

			double d = pivotVectorSign;
			for (int j = 0; j < nRow; j++) {
				d *= data[j][j];
			}
			return d;
		}

		public boolean isNonsingular() {
			for (int i = 0; i < nColumn; i++) {
				if (data[i][i] == 0)
					return false;
			}
			return true;
		}
	}
}
