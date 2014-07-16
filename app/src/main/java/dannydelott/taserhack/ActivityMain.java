package dannydelott.taserhack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

public class ActivityMain extends Activity implements OnTouchListener {

    // ///////////////////
    // GLOBAL VARIABLES //
    // ///////////////////

    // holds clickable background
    private RelativeLayout background;

    // handles strobing flash
    private Strobe strobe;

    // holds vibration
    private Vibrator vb;

    // handles volume
    private Volume volume;

    // handles sound effects
    private SoundEffect soundEffect;
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
        background = (RelativeLayout) findViewById(R.id.rl_Background);

        // sets up vibrator
        vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // sets up strobe light
        strobe = Strobe.newInstance(35);
        if (strobe == null) {
            Log.d("error", "fuck");
        }

        // sets up volume
        volume = new Volume(this);
        volume.setToMaxVolume();

        // sets up sound effects
        soundEffect = SoundEffect.newInstance(this, R.raw.taser3);

        // makes background clickable
        background.setOnTouchListener(this);



    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        // --------------
        // ON FINGER DOWN
        // --------------

        if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {

            // plays sound
            soundEffect.playSound(true);

            // vibrates (set to 10 minutes)
            vb.vibrate(1000 * 60 * 10);

            // flashes
            strobe.setStrobeOn();

            return true;
        }

        // ------------
        // ON FINGER UP
        // ------------

        if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {

            // stops playing sound
            soundEffect.stopSound();

            // cancels vibration
            vb.cancel();

            // stops flash
            strobe.setStrobeOff();

            return false;
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();

        // cancels vibration
        vb.cancel();

        // releases the camera if not already done
        strobe.releaseCamera();

        // stops the sound effects and releases the SoundPool
        if (soundEffect.isSoundLoaded()) {
            soundEffect.stopSound();
            soundEffect.releaseSoundPool();
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
                Intent intent = new Intent(this, ActivitySettings.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}