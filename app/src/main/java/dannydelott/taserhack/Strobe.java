package dannydelott.taserhack;

import android.hardware.Camera;
import android.os.Handler;

/**
 * Created by dannydelott on 7/12/14.
 */
public class Strobe {

    // handles strobing flash
    private Camera cam;
    private Camera.Parameters pOn;
    private Camera.Parameters pOff;
    private Handler mCameraHandler = new Handler();
    private boolean mActive = false;
    private boolean mSwap = true;

    // holds user given strobe interval
    private int strobeInterval;

    // holds error flags
    private boolean flagError;


    private Strobe(int i) {

        // sets class variables
        strobeInterval = i;
        flagError = false;

        // opens the camera
        cam = Camera.open();
        if (cam == null) {
            flagError = true;
        } else {
            pOn = cam.getParameters();
            pOn.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            pOff = cam.getParameters();
            pOff.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        }
    }

    public static Strobe newInstance(int i) {

        Strobe s = new Strobe(i);
        if (s.getErrorFlag()) {
            return null;
        }
        return s;
    }

    public void setStrobeOn() {
        mActive = true;
        mCameraHandler.post(mStrobeRunnable);
    }

    public void setStrobeOff() {
        mCameraHandler.removeCallbacks(mStrobeRunnable);
        if (cam != null) {
            cam.setParameters(pOff);
        }
    }

    public void releaseCamera() {

        // releases the camera if not already done
        if (cam != null) {
            cam.release();
            cam = null;
        }
    }

    public boolean getErrorFlag() {
        return flagError;
    }

    private final Runnable mStrobeRunnable = new Runnable() {

        public void run() {
            if (mActive) {
                if (mSwap && cam != null) {

                    // turns on
                    cam.setParameters(pOn);
                    mSwap = false;
                    mCameraHandler.postDelayed(mStrobeRunnable, strobeInterval);
                } else {

                    // turns off

                    cam.setParameters(pOff);
                    mSwap = true;
                    mCameraHandler.postDelayed(mStrobeRunnable, strobeInterval);

                }
            }
        }
    };

}
