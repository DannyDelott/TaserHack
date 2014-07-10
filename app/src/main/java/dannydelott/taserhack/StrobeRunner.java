package dannydelott.taserhack;

import android.hardware.Camera;

public class StrobeRunner implements Runnable {

    private static StrobeRunner instance;
    public volatile boolean requestStop = false;
    public volatile boolean isRunning = false;
    public volatile int delay = 10;
    public volatile int delayoff = 10;
    public volatile String errorMessage = "";

    protected StrobeRunner() {

    }

    public static StrobeRunner getInstance() {
        return (instance == null ? instance = new StrobeRunner() : instance);
    }

    @Override
    public void run() {
        if (isRunning)
            return;

        requestStop = false;
        isRunning = true;

        Camera cam = Camera.open();

        Camera.Parameters pon = cam.getParameters(), poff = cam.getParameters();

        pon.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        poff.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);

        while (!requestStop) {
            try {
                cam.setParameters(pon);
                Thread.sleep(delay);
                cam.setParameters(poff);
                Thread.sleep(delayoff);
            } catch (InterruptedException ex) {

            } catch (RuntimeException ex) {
                requestStop = true;
                errorMessage = "Error setting camera flash status. Your device may be unsupported.";
            }
        }

        cam.release();

        isRunning = false;
        requestStop = false;


    }

}
