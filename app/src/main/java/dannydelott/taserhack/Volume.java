package dannydelott.taserhack;

import android.content.Context;
import android.media.AudioManager;

/**
 * Created by dannydelott on 7/12/14.
 */
public class Volume {

    AudioManager audioManager;

    public Volume(Context context) {
        audioManager =
                (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

    }

    public void setToMaxVolume() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
    }
}
