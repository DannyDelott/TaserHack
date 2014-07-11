package dannydelott.taserhack;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

public class ActivityMain extends Activity implements OnTouchListener, SoundPool.OnLoadCompleteListener {

    // ///////////////////
    // GLOBAL VARIABLES //
    // ///////////////////

    // holds clickable background
    private RelativeLayout background;

    // handles strobing flash
    private Camera cam;
    private Camera.Parameters pOn;
    private Camera.Parameters pOff;
    private Handler mHandler = new Handler();
    private boolean mActive = false;
    private boolean mSwap = true;
    private int strobeInterval = 35;

    // holds vibration
    private Vibrator vb;

    // handles sound effects
    private SoundPool sp;
    private int soundId;
    private int streamId;
    private boolean loadedSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // sets xml layout
        setContentView(R.layout.activity_main);

        // gets background relative layout for handling touch events
        background = (RelativeLayout) findViewById(R.id.background);

        // sets up vibrator
        vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // sets up strobe light
        cam = Camera.open();
        pOn = cam.getParameters();
        pOn.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        pOff = cam.getParameters();
        pOff.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);

        // sets max volume
        AudioManager audioManager =
                (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

        // sets up sound effects
        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        sp.setOnLoadCompleteListener(this);
        soundId = sp.load(this, R.raw.taser2, 1);


    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

        loadedSound = true;

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
            mActive = true;
            mHandler.post(mStrobeRunnable);

            // plays sound
            if (loadedSound) {
                streamId = sp.play(soundId, 1, 1, 1, -1, 1.0f);
            }
            return true;
        }

        // ------------
        // ON FINGER UP
        // ------------

        if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {

            // cancels vibration
            vb.cancel();

            // stops flash
            mHandler.removeCallbacks(mStrobeRunnable);
            if (cam != null) {
                cam.setParameters(pOff);
            }

            // stops playing sound
            if (loadedSound) {
                sp.stop(streamId);
            }
            return false;
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onPause();

        // cancels vibration
        vb.cancel();

        // releases the camera if not already done
        if (cam != null) {
            cam.release();
        }

        // stops the sound effects and releases the SoundPool
        if (loadedSound) {
            sp.stop(soundId);
            sp.release();
        }

        // exits the Activity
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {

            case R.id.action_settings:
                //Intent intent = new Intent(this, about.class);
                //startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private final Runnable mStrobeRunnable = new Runnable() {

        public void run() {
            if (mActive) {
                if (mSwap) {

                    // turns on
                    cam.setParameters(pOn);
                    mSwap = false;
                    mHandler.postDelayed(mStrobeRunnable, strobeInterval);
                } else {

                    // turns off
                    cam.setParameters(pOff);
                    mSwap = true;
                    mHandler.postDelayed(mStrobeRunnable, strobeInterval);
                }
            }
        }
    };


}