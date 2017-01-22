import com.jogamp.nativewindow.util.Dimension;
import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_TEXTURE;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_FLAT;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;

/**
 * Created by bkozyrev on 22.01.2017.
 */
public class Main implements GLEventListener, KeyListener {

    private static final int SCREEN_WIDTH = 1024;
    private static final int SCREEN_HEIGHT = 768;

    private static Dimension windowSize = new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT);
    private static int screenIdx = 0;
    private static GLWindow glWindow;
    private static boolean undecorated = false;
    private static boolean alwaysOnTop = false;
    private static boolean fullscreen = false;
    private static boolean mouseVisible = true;
    private static boolean mouseConfined = false;
    private static String title = "pizdec";

    private static double mPacRadius = 0.1;
    private static double mIncStep = 0.003;
    private static double mPelletRadius = 0.01;
    private static double mAngle = Math.PI / 2;
    private static double mMouthSize = 0.6;
    private static float mDeltaX = 0.0f, mDeltaY = 0.0f;
    private static int mMoveSide = 0;
    private static float mDeltaConst = 0.02f;
    private static double mProbability = 0.1;
    private ArrayList<MyAwesomePointThatSupportsDoubleCoordinatesSukaBlya> mPellets = new ArrayList<MyAwesomePointThatSupportsDoubleCoordinatesSukaBlya>();

    private Main() {

    }

    public static void main(String[] args) {

        Display display = NewtFactory.createDisplay(null);
        Screen screen = NewtFactory.createScreen(display, screenIdx);
        GLProfile glProfile = GLProfile.get(GLProfile.GL2);
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);
        glWindow = GLWindow.create(screen, glCapabilities);

        glWindow.setSize(windowSize.getWidth(), windowSize.getHeight());
        glWindow.setPosition(50, 50);
        glWindow.setUndecorated(undecorated);
        glWindow.setAlwaysOnTop(alwaysOnTop);
        glWindow.setFullscreen(fullscreen);
        glWindow.setPointerVisible(mouseVisible);
        glWindow.confinePointer(mouseConfined);
        glWindow.setTitle(title);
        glWindow.setContextCreationFlags(GLContext.CTX_OPTION_DEBUG);
        glWindow.setVisible(true);

        Main main = new Main();
        glWindow.addGLEventListener(main);
        glWindow.addKeyListener(main);

        try {
            System.in.read();
        } catch (IOException exception) {

        }
    }

    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glShadeModel(GL_FLAT);
        gl.glLoadIdentity();
        //gl.glOrtho(0.0, SCREEN_WIDTH, 0.0, SCREEN_HEIGHT, -1.0, 1.0);
    }

    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        //gl.glClear(GL_COLOR_BUFFER_BIT);

        /*int count = 0;
        while(count != 600) {
            gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f );
            gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            translatePackman(gl);
            count++;
            glAutoDrawable.swapBuffers();
        }*/
        if (mMoveSide == 0)
            drawPackman(gl);
        else
            translatePackman(gl);

        //mPellets.add(new MyAwesomePointThatSupportsDoubleCoordinatesSukaBlya(0.5, 0.5));
        //mPellets.add(new MyAwesomePointThatSupportsDoubleCoordinatesSukaBlya(0.5, 0.5));
        //mPellets.add(new MyAwesomePointThatSupportsDoubleCoordinatesSukaBlya(-0.5, -0.5));
        //mPellets.add(new MyAwesomePointThatSupportsDoubleCoordinatesSukaBlya(-0.9, 0.2));

        //drawPellets(gl);
    }

    private void drawPackman(GL2 gl) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f );
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        double theta;
        gl.glBegin(GL2.GL_POLYGON);
        gl.glColor4d(1, 1, 0.3, 0);
        for (theta = 0.0; theta < 2 * Math.PI; theta += mIncStep) {
            gl.glVertex2d(mPacRadius * Math.cos(theta), mPacRadius * Math.sin(theta));
        }
        gl.glEnd();

        gl.glBegin(GL2.GL_POLYGON);
        gl.glColor4d(0, 0, 0, 0);
        gl.glVertex2d(0, 0);
        for (theta = -mMouthSize; theta <= mMouthSize; theta += mIncStep) {
            gl.glVertex2d(mPacRadius * Math.cos(theta), mPacRadius * Math.sin(theta));
        }
        gl.glEnd();

        gl.glFlush();
    }

    private void drawPellets(GL2 gl) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f );
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        gl.glColor3d(1, 0, 0);

        gl.glBegin(GL2.GL_POLYGON);
            double theta;
            for (MyAwesomePointThatSupportsDoubleCoordinatesSukaBlya point : mPellets) {
                gl.glTranslated(point.getX(), point.getY(), 0);
                for (int i = 0; i < 100; i++) {
                    theta = 2.0f * Math.PI * i / 100f;
                    double x = mPelletRadius * Math.cos(theta);
                    double y = mPelletRadius * Math.sin(theta);
                    gl.glVertex2d(x, y);
                }
            }
        gl.glEnd();

        gl.glFlush();
    }

    private void translatePackman(GL2 gl) {
        int spin = 0;
        if (mMoveSide == 1) { //top
            mDeltaY += mDeltaConst;
            if (mDeltaY > 1)
                mDeltaY = -1;
            spin = 90;
        }
        if (mMoveSide == 2) { //down
            mDeltaY -= mDeltaConst;
            if (mDeltaY < -1)
                mDeltaY = 1;
            spin = 270;
        }
        if (mMoveSide == 3) { //left
            mDeltaX -= mDeltaConst;
            if (mDeltaX < -1)
                mDeltaX = 1;
            spin = 180;
        }
        if (mMoveSide == 4) { //right
            mDeltaX += mDeltaConst;
            if (mDeltaX > 1)
                mDeltaX = -1;
            spin = 0;
        }

        attemptToCreatePellet();

        System.out.println("delta x = " + mDeltaX);
        System.out.println("delta y = " + mDeltaY);

        gl.glPushMatrix();
            //drawPellets(gl);
            gl.glTranslatef(mDeltaX, mDeltaY, 0);
            gl.glRotatef(spin, 0, 0, 1.0f);
            drawPackman(gl);
        gl.glPopMatrix();

        if (mMouthSize > 0.07)
            mMouthSize -= 0.05;
        else
            mMouthSize = 0.7;
    }

    private void attemptToCreatePellet() {
        Random random = new Random();
        double randomValue, min = 0, max = 1;

        randomValue = min + (max - min) * random.nextDouble();

        if (mProbability > randomValue) {
            min = -1;
            double randomX = min + (max - min) * random.nextDouble();
            double randomY = min + (max - min) * random.nextDouble();
            mPellets.add(new MyAwesomePointThatSupportsDoubleCoordinatesSukaBlya(randomX, randomY));
            System.out.println("Created pellet at x = " + randomX + " , y = " + randomY + " . Pellets count = " + mPellets.size());
        }
    }

    public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                mMoveSide = 3;
                glWindow.display();
                break;
            case KeyEvent.VK_RIGHT:
                mMoveSide = 4;
                glWindow.display();
                break;
            case KeyEvent.VK_UP:
                mMoveSide = 1;
                glWindow.display();
                break;
            case KeyEvent.VK_DOWN:
                mMoveSide = 2;
                glWindow.display();
                break;
        }
    }

    public void keyReleased(KeyEvent keyEvent) {

    }

    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }

    public void dispose(GLAutoDrawable glAutoDrawable) {

    }
}
