
public class LinearSystemSolver { //solves an Ax = B system
	
	
	public LinearSystemSolver() {} //Empty constructor
	
	//recursively computes determinant of a target matrix using cofactor expansion along top row
	private double determinant(Matrix target) {
		
		double detVal = 0;
		
		int[] dim = target.getDim(); //get dimensions of target
		
		//if only one element, the determinant is the element
                    if (dim[0] == 1 && dim[1] == 1){
                            detVal = target.getElement(0,0);
                            return detVal;
                    }
		
                Matrix cofactorMatrix = cofactor(target); //create cofactor matrix
                
                //expand along top row
                for (int j = 0; j < dim[1]; j++) {
                    detVal += target.getElement(0,j)*cofactorMatrix.getElement(0,j);
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
        
        private Matrix cofactor (Matrix target) { //computes the cofactor of a matrix
                
            //make matrix of cofactors
                int[] dim = target.getDim();

                Matrix returnMatrix = new Matrix(dim[0], dim[1]); //the cofactor matrix
                Matrix cofactorMatrix = new Matrix(dim[0] - 1, dim[1] - 1); //the individual cofactor matrix

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
            
                return returnMatrix;
        }
	
	private Matrix adjoint(Matrix target) { //gets the classical adjoint matrix (or you might say adjugate)
                
                return transpose(cofactor(target));
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
        }

}