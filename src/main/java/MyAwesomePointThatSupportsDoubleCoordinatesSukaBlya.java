/**
 * Created by bkozyrev on 23.01.2017.
 */
public class MyAwesomePointThatSupportsDoubleCoordinatesSukaBlya {

    //fuck you java
    private double mX, mY;

    public MyAwesomePointThatSupportsDoubleCoordinatesSukaBlya(double mX, double mY) {
        this.mX = mX;
        this.mY = mY;
    }

    public MyAwesomePointThatSupportsDoubleCoordinatesSukaBlya() {
        this(0, 0);
    }

    public double getX() {
        return mX;
    }

    public double getY() {
        return mY;
    }
}
