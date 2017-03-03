package LinearKalmanFilter;
public class LinearKalmanFilter {
    private int n, m;
    private Matrix.SquareMatrix A;
    private Matrix B;
    private Matrix.SquareMatrix H;
    private Matrix.Vector X;
    private Matrix.SquareMatrix P;
    private Matrix.SquareMatrix Q;
    private Matrix.SquareMatrix R;
    
    public Matrix.SquareMatrix getA() {
        return A;
    }
    
    public void setA(Matrix.SquareMatrix transitionMatrix) {
        if (transitionMatrix.getRow() != n)
            throw new IllegalArgumentException("LinearKalmanFilter: setA - invalid size.");
        A = transitionMatrix;
    }
    
    public Matrix getB() {
        return B;
    }
    
    public void setB(Matrix controlMatrix) {
        if (controlMatrix.getRow() != n || controlMatrix.getColumn() != m)
            throw new IllegalArgumentException("LinearKalmanFilter: setB - invalid size.");
        B = controlMatrix;
    }
    
    public Matrix.SquareMatrix getH() {
        return H;
    }
    
    public void setH(Matrix.SquareMatrix transitionMatrix) {
        if (transitionMatrix.getRow() != n)
            throw new IllegalArgumentException("LinearKalmanFilter: setH - invalid size.");
        H = transitionMatrix;
    }
    
    public Matrix.Vector getX() {
        return X;
    }
    
    public void setX(Matrix.Vector initState) {
        if (initState.getRow() != n)
            throw new IllegalArgumentException("LinearKalmanFilter: setX0 - invalid size.");
        X = initState;
    }
    
    public Matrix.SquareMatrix getP() {
        return P;
    }
    
    public void setP(Matrix.SquareMatrix covarianceMatrix) {
        if (covarianceMatrix.getRow() != n)
            throw new IllegalArgumentException("LinearKalmanFilter: setP - invalid size.");
        P = covarianceMatrix;
    }
    
    public Matrix.SquareMatrix getQ() {
        return Q;
    }
    
    public void setQ(Matrix.SquareMatrix processErrorMatrix) {
        if (processErrorMatrix.getRow() != n)
            throw new IllegalArgumentException("LinearKalmanFilter: setQ - invalid size.");
        Q = processErrorMatrix;
    }
    
    public Matrix.SquareMatrix getR() {
        return R;
    }
    
    public void setR(Matrix.SquareMatrix measurementsErrorMatrix) {
        if (measurementsErrorMatrix.getRow() != n)
            throw new IllegalArgumentException("LinearKalmanFilter: setR - invalid size.");
        R = measurementsErrorMatrix;
    }

    public LinearKalmanFilter(int stateSize, int controlSize) {
        n = stateSize;
        m = controlSize;
    }

    private Matrix.Vector predictedStateEstimate;
    private Matrix.Vector vectorTemp;
    private Matrix.SquareMatrix predictedProbEstimate;
    private Matrix.SquareMatrix aTranspose;
    private Matrix.SquareMatrix squareMatrixTemp;
    private Matrix.SquareMatrix innovationCovariance;
    private Matrix.SquareMatrix hTranspose;
    private Matrix.SquareMatrix kalmanGain;
    private Matrix.SquareMatrix inverseInnovationCovariance;
    private Matrix.SquareMatrix eyeN;
    
    public void init() {
        if (n <= 0 || m < 0 || null == A)
            throw new IllegalArgumentException("LinearKalmanFilter: init.");

        if ((null == B) && (0 != m))
            B = Matrix.zeros(n, m);
        if (null == H)
            H = Matrix.SquareMatrix.eyes(n);
        if (null == P)
            P = Matrix.SquareMatrix.eyes(n);
        if (null == Q)
            Q = Matrix.SquareMatrix.zeros(n);
        if (null == R)
            R = Matrix.SquareMatrix.eyes(n);
        if (null == X) {
            X = new Matrix.Vector(n);
            X.setZero();
        }

        predictedStateEstimate = new Matrix.Vector(n);
        vectorTemp = new Matrix.Vector(n);
        predictedProbEstimate = new Matrix.SquareMatrix(n);
        aTranspose = (Matrix.SquareMatrix)A.transpose();
        squareMatrixTemp = new Matrix.SquareMatrix(n);
        innovationCovariance = new Matrix.SquareMatrix(n);
        hTranspose = (Matrix.SquareMatrix)H.transpose();
        kalmanGain = new Matrix.SquareMatrix(n);
        inverseInnovationCovariance = new Matrix.SquareMatrix(n);
        eyeN = new Matrix.SquareMatrix(n);
    }
    
    public void filt(Matrix.Vector controlVector, Matrix.Vector measurementVector) {
        // Prediction
        predictedStateEstimate.vectorMulOf(A, X);
        vectorTemp.vectorMulOf(B, controlVector);
        predictedStateEstimate.add(vectorTemp);

        squareMatrixTemp.mul(A, P);
        predictedProbEstimate.mul(squareMatrixTemp, aTranspose);
        predictedProbEstimate.add(Q);

        // Observation
        vectorTemp.vectorMulOf(H, predictedStateEstimate);
        measurementVector.sub(vectorTemp);
        squareMatrixTemp.mul(H, predictedProbEstimate);
        innovationCovariance.mul(squareMatrixTemp, hTranspose);
        innovationCovariance.add(R);

        // Update
        inverseInnovationCovariance.luInverseOf(innovationCovariance);
//        inverseInnovationCovariance.inverseOf(innovationCovariance);
        squareMatrixTemp.mul(predictedProbEstimate, hTranspose);
        kalmanGain.mul(squareMatrixTemp, inverseInnovationCovariance);

        X.vectorMulOf(kalmanGain, measurementVector);
        X.add(predictedStateEstimate);

        eyeN.setEye();
        squareMatrixTemp.mul(kalmanGain, H);
        eyeN.sub(squareMatrixTemp);
        P.mul(eyeN, predictedProbEstimate);
    }
}
