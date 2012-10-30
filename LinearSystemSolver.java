
public class LinearSystemSolver {
	
	
	public LinearSystemSolver() {
	}
	
	//recursively computes determinant of a target matrix using cofactor expansion along top row
	private double determinant(Matrix target) {
		
		double detVal = 0;
		
		int[] dim = target.getDim(); //get dimensions of target
		
		//if only one element, the determinant is the element
			if (dim[0] == 1 && dim[1] == 1){
				detVal = target.getElement(0,0);
				return detVal;
			}
		
		//using top row for cofactor expansion, so we only need to loop through j and we hold i = 0
		for (int j = 0; j < dim[1]; j++) {
			
			//create submatrix
			Matrix newTarget = new Matrix(dim[0] - 1, dim[1] - 1);
			
			int offset = 0; //for filling submatrix
			
			//Fill the new submatrix by copying over everything except current row/column
			for (int x = 1; x < dim[0]; x++) {
				//reset offset
				offset = 0;
				for (int y = 0; y < dim[1] - 1; y++) {
					if (y == j){
						offset = 1; //skip the jth column
					}
					newTarget.setElement(x-1, y, target.getElement(x, y + offset));
				}
			}
			
			//determine sign of cofactor
			int signMod = 1;
			
			if (j % 2 == 1) { //changes to -1 on odd j
				signMod = -1;
			}
			
			detVal += signMod * target.getElement(0, j) * determinant(newTarget); //recursively compute determinant
		}
		
		return detVal;
	}
	
	private Matrix transpose(Matrix target) { //transposes a matrix
		int[] dim = target.getDim();
		Matrix returnMatrix = new Matrix(dim[0], dim[1]);
		
		for (int i = 0; i < dim[0]; i++) { //switch around i and j
			for (int j = 0; j < dim[1]; j++) {
				returnMatrix.setElement(j, i, target.getElement(i, j));
			}
		}
		
		return returnMatrix;
	}
	
	private Matrix adjoint(Matrix target) { //gets the classical adjoint matrix (or you might say adjugate)
		//make matrix of cofactors
		int[] dim = target.getDim();
		
		Matrix returnMatrix = new Matrix(dim[0], dim[1]); //the adjugate matrix
		Matrix cofactorMatrix = new Matrix(dim[0] - 1, dim[1] - 1); //the cofactor matrix
		
		for (int i = 0; i < dim[0]; i++) {
			for (int j = 0; j < dim[1]; j++) {
				//set the sign
				int signMod = 1;
				
				if ((i + j) % 2 == 1) { //if sum of i and j is odd, then -1
					signMod = -1;
				}
				
				int xOffset = 0;
				int yOffset = 0; //set offsets
				
				//compute cofactors
				for (int x = 0; x < dim[0] - 1; x++) {
				
					yOffset = 0; //reset yOffset 
					
					if (x == i) {
							xOffset = 1; //set xOffset
					}
					
					for (int y = 0; y < dim[1] - 1; y++) {		
							
						if (y == j) {
							yOffset = 1;
						}
					
						cofactorMatrix.setElement(x, y, target.getElement(x + xOffset, y + yOffset)); //use offsets to store cofactor
						
						
					}
				}
				
				returnMatrix.setElement(i, j, signMod*determinant(cofactorMatrix)); //store the cofactor in the return matrix 
				
			}
		}
		return transpose(returnMatrix);
	}
	
	private Matrix matrixMult(Matrix A, Matrix B) {
		int[] dimA = A.getDim();
		int[] dimB = B.getDim();
		
		Matrix returnMatrix = new Matrix(dimA[0], dimB[1]); //Matrix for result
		
		for (int i = 0; i < dimA[0]; i++) {
			for (int j = 0; j < dimB[1]; j++) {
				double sum = 0;
				
				for (int x = 0; x < dimB[0]; x++) {		
					sum += A.getElement(i, x)*B.getElement(x,j); //sum up for each element in result matrix
				}
				
				returnMatrix.setElement(i, j, sum);
			}
		}
		
		return returnMatrix;
	}
	
	private Matrix scalarMult(double a, Matrix B) { //multiplies a scalar into a matrix
		int[] dimB = B.getDim();
		
		for (int i = 0; i < dimB[0]; i++) {
			for (int j = 0; j < dimB[1]; j++) {
				B.setElement(i, j, a*B.getElement(i, j));
			}
		}
		
		return B;
	}
	
	public Matrix solve(Matrix A, Matrix B) { //returns solution for X matrix
		return matrixMult(scalarMult((1.0/determinant(A)), adjoint(A)), B);
		//return scalarMult(1.0/determinant(A), adjoint(A));
	}
	
	
	//Test functions below
	public double testDet() {
		Matrix A = new Matrix(2,2);
		A.setElement(0,0,1);
		A.setElement(0,1,2);
		A.setElement(1,0,3);
		A.setElement(1,1,4);
		return determinant(A);
	}
	
	public Matrix testAdj() {
		Matrix A = new Matrix(2,2);
		A.setElement(0,0,1);
		A.setElement(0,1,2);
		A.setElement(1,0,3);
		A.setElement(1,1,4);
		return adjoint(A);
	}
	
	public Matrix testMult() {
		Matrix A = new Matrix(2,2);
		A.setElement(0,0,1);
		A.setElement(0,1,2);
		A.setElement(1,0,3);
		A.setElement(1,1,4);
		
		Matrix B = new Matrix(2,1);
		B.setElement(0,0,5);
		B.setElement(1,0,6);	
		
		return matrixMult(A, B);
	}
	
	public Matrix testSolve() {
		Matrix A = new Matrix(2,2);
		A.setElement(0,0,1);
		A.setElement(0,1,2);
		A.setElement(1,0,3);
		A.setElement(1,1,4);
		
		Matrix B = new Matrix(2,1);
		B.setElement(0,0,5);
		B.setElement(1,0,6);	
		
		return solve(A, B);
	}
	
}