public class Matrix {

	private int m,n; //dimensions
	private double[][] matrix; //the 2D matrix
	
	public Matrix(int m, int n) {
		
		matrix = new double[m][n]; //resize
		
		//store dimensions
		this.m = m;
		this.n = n;
		
		//initialize the matrix to hold all zeros
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				matrix[i][j] = 0;
			}
		}
	}
	
	public double getElement(int i, int j) {
		return matrix[i][j];
	}
	
	public void setElement(int i, int j, double val) {
		matrix[i][j] = val;
	}
	
	public int[] getDim() {
		int[] dim = {m,n};
		return dim;
	}
	
	
}