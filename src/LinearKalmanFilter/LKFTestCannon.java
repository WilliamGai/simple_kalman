package LinearKalmanFilter;
import java.util.Random;
import java.util.ArrayList;

public class LKFTestCannon {
	//角度
    private double angle = 45 * Math.PI / 180.0;
    //口径速度
    private double muzzleVel = 100.0;
    //重力加速度
    private double[] graityData = { 0, -9.81 };
    private Matrix.Vector gravity;
    //速度
    private double velocityData[] = { muzzleVel * Math.cos(angle), muzzleVel * Math.sin(angle) };
    private Matrix.Vector velocity;
    private Matrix.Vector loc;
    private Random rand = new Random();
    //时间片 
    private double timeSlice;
    //噪音
    private double noiseLevel;
    
    public LKFTestCannon(double theTimeSlice, double theNoiseLevel) {
        timeSlice = theTimeSlice;
        noiseLevel = theNoiseLevel;

        gravity = new Matrix.Vector(graityData);
        gravity.scaleMul(timeSlice);
        loc = new Matrix.Vector(2);
        loc.setZero();
        velocity = new Matrix.Vector(velocityData);
    }
    
    public double getSpeedX() { return velocity.get(0); }
    public double getSpeedY() { return velocity.get(1); }
    public double getX() { return loc.get(0); }
    public double getY() { return loc.get(1); }
    public double getXWithNoise() { return getX() + (rand.nextDouble() - 0.5) * noiseLevel; }
    public double getYWithNoise() { return getY() + (rand.nextDouble() - 0.5) * noiseLevel; }
    
    public void step() {
        velocity.add(gravity);
        Matrix.Vector slicedVelocity = (Matrix.Vector)(velocity.copy());
        slicedVelocity.add(gravity);
        slicedVelocity.scaleMul(timeSlice);
        loc.add(slicedVelocity);
    }
    
    public static class TestResult {
        public double x, y;           // position
        public double nx, ny;         // position with noise
        public double kx, ky;         // output of lkf
    }

    public static ArrayList test() {
        int iterations = 144;
        double ts = 0.1;
        double nl = 50.0;

        ArrayList r = new ArrayList();

        LKFTestCannon cannon = new LKFTestCannon(ts, nl);

        double stateTransitionData[][] = {
            { 1, ts, 0,  0 },
            { 0,  1, 0,  0 },
            { 0,  0, 1, ts },
            { 0,  0, 0,  1 }
        };
        double contorlMatrixData[][] = { 
            { 0,  0, 0,  0 },
            { 0,  0, 0,  0 },
            { 0,  0, 1,  0 },
            { 0,  0, 0,  1 }
        };
        double controlVectorData[] = { 0, 0, 0.5 * (-9.81) * ts * ts, -9.81 * ts };
        double initState[] = { 0, cannon.getSpeedX(), 500, cannon.getSpeedY() };

        LinearKalmanFilter lkf = new LinearKalmanFilter(4, 4);
        lkf.setA(new Matrix.SquareMatrix(stateTransitionData));
        lkf.setB(new Matrix(contorlMatrixData));
        lkf.setX(new Matrix.Vector(initState));
        lkf.init();
        lkf.getR().scaleMul(0.2);

        Matrix.Vector controlVector = new Matrix.Vector(controlVectorData);        
        Matrix.Vector measurementVector = new Matrix.Vector(4);

        for (int i = 0; i < iterations; i++) {
            double[][] kp = lkf.getX().getData();

            TestResult tr = new TestResult();
            tr.x = cannon.getX();
            tr.y = cannon.getY();
            tr.nx = cannon.getXWithNoise();
            tr.ny = cannon.getYWithNoise();
            tr.kx = kp[0][0];
            tr.ky = kp[2][0];

            cannon.step();

            measurementVector.set(0, tr.nx);
            measurementVector.set(1, cannon.getSpeedX());
            measurementVector.set(2, tr.ny);
            measurementVector.set(3, cannon.getSpeedY());
            lkf.filt(controlVector, measurementVector);

            r.add(tr);
        }
        return r;
    }
}
