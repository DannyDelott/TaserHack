package dannydelott.taserhack;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class ActivityMain extends Activity implements OnTouchListener {

    // ///////////////////
    // GLOBAL VARIABLES //
    // ///////////////////

    //handles strobing flash
    private Camera.Parameters pOn;
    private Camera.Parameters pOff;
    private Camera cam;
    private boolean requestStop = false;
    private boolean isRunning = false;
    private int delay = 10;
    private int delayoff = 10;
    private Thread bw;

    // handles vibration
    private Vibrator vb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // removes title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // sets xml layout (must be done after any requestWindowFeature calls)
        setContentView(R.layout.activity_main);


        // gets background relative layout for handling touch events
        RelativeLayout background = (RelativeLayout) findViewById(R.id.background);

        // initalizes global vibrator
        vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // sets up strobe light
        cam = Camera.open();
        pOn = cam.getParameters();
        pOff = cam.getParameters();
        pOn.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        pOff.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);

        // sets touch listener
        background.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        // --------------
        // ON FINGER DOWN
        // --------------

        if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {

            // vibrates (set to 10 minutes)
            vb.vibrate(1000 * 60 * 10);

            // flashes
            while (!requestStop) {
                try {
                    cam.setParameters(pOn);
                    Thread.sleep(delay);
                    cam.setParameters(pOff);
                    Thread.sleep(delayoff);
                } catch (InterruptedException ex) {

                } catch (RuntimeException ex) {
                    requestStop = true;
                }
            }

            // TODO: plays sound

            return true;
        }

        // ------------
        // ON FINGER UP
        // ------------

        if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {

            // cancels vibration
            vb.cancel();

            // stops flash
            requestStop = true;

            // TODO: stops playing sound

            return false;
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onStop();
        vb.cancel();
        cam.release();
    }
}