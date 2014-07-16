package dannydelott.taserhack;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class ActivitySettings extends ActionBarActivity implements SeekBar.OnSeekBarChangeListener {

    //////////////////////
    // GLOBAL VARIABLES //
    //////////////////////

    private SeekBar sb_StrobeInterval;
    private TextView tv_StrobeInterval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setWindowContentOverlayCompat();

        // gets the Activity's Views
        initializeViews();


    }

    private void initializeViews() {

        // handles the strobe interval
        tv_StrobeInterval = (TextView) findViewById(R.id.tv_StrobeInterval);
        sb_StrobeInterval = (SeekBar) findViewById(R.id.sb_StrobeInterval);
    }

    private void setListeners(){

        // handles the strobe interval SeekBar
        sb_StrobeInterval.setOnSeekBarChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_settings_actions, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setWindowContentOverlayCompat() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR2) {

            // Get the content view
            View contentView = findViewById(R.id.fl_Container);

            // Make sure it's a valid instance of a FrameLayout
            if (contentView instanceof FrameLayout) {
                TypedValue tv = new TypedValue();

                // Get the windowContentOverlay value of the current theme
                if (getTheme().resolveAttribute(
                        android.R.attr.windowContentOverlay, tv, true)) {

                    // If it's a valid resource, set it as the foreground
                    // drawable for the content view
                    if (tv.resourceId != 0) {

                        ((FrameLayout) contentView)
                                .setForeground(getResources().getDrawable(
                                        tv.resourceId));
                    }
                }
            }
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int value, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
