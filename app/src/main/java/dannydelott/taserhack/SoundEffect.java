package dannydelott.taserhack;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import static android.media.SoundPool.OnLoadCompleteListener;

/**
 * Created by dannydelott on 7/12/14.
 */


public class SoundEffect implements OnLoadCompleteListener {

    // handles sound effects
    private SoundPool sp;
    private int soundId;
    private int streamId;
    private boolean loadedSound;
    private SoundLoadedListener listener;


    public interface SoundLoadedListener {

        void SoundLoadedComplete();
    }


    public static SoundEffect newInstance(Context context, int resId) {
        SoundEffect s = new SoundEffect(context, resId);
        return s;
    }

    private SoundEffect(Context context, int resId) {
        loadedSound = false;
        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundId = sp.load(context, resId, 1);
        sp.setOnLoadCompleteListener(this);
    }

    public boolean isSoundLoaded() {
        return loadedSound;
    }

    public void playSound(boolean loop) {
        // plays sound
        if (loadedSound) {

            if (loop) {
                streamId = sp.play(soundId, 1, 1, 1, -1, 1.0f);
            }

            if (!loop) {
                streamId = sp.play(soundId, 1, 1, 1, 0, 1.0f);
            }
        }
    }

    public void stopSound() {
        if (loadedSound) {
            sp.stop(streamId);
        }
    }

    public void releaseSoundPool() {
        if (loadedSound) {
            sp.release();
        }
    }

    public void setSoundLoadedListener(SoundLoadedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int i, int i2) {
        loadedSound = true;
        listener.SoundLoadedComplete();

    }
}

