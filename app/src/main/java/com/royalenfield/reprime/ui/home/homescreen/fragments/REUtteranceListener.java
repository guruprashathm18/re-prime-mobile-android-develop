package com.royalenfield.reprime.ui.home.homescreen.fragments;

import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import androidx.fragment.app.FragmentActivity;

/**
 * UtteranceProgressListener is for events triggered by your own app,
 * like whenever your speech is done or your speech is about to start
 * <p>
 * Created by @author Kiran Dani 30-10-2020
 */
public class REUtteranceListener extends UtteranceProgressListener {

    private String mUtteranceId = "";
    private AudioManager mAudioManager;

    public REUtteranceListener(FragmentActivity mContext) {
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
    }

    /**
     * OnAudioFocusChangeListener is for events triggered by other apps
     */
    private AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                // Pause playback
                mAudioManager.abandonAudioFocus(afChangeListener);
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                //  we don't duck, just abandon focus
                mAudioManager.abandonAudioFocus(afChangeListener);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // Resume playback
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                mAudioManager.abandonAudioFocus(afChangeListener);
            }
        }
    };

    /**
     * Called when an utterance "starts" as perceived by the caller. This will
     * be soon before audio is played back in the case of a {@link TextToSpeech#speak}
     * or before the first bytes of a file are written to the file system in the case
     * of {@link TextToSpeech#synthesizeToFile}.
     *
     * @param utteranceId The utterance ID of the utterance.
     */
    @Override
    public void onStart(String utteranceId) {
        mUtteranceId = utteranceId;
        int result = mAudioManager.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
    }

    /**
     * Called when an utterance has successfully completed processing.
     * All audio will have been played back by this point for audible output, and all
     * output will have been written to disk for file synthesis requests.
     * <p>
     * This request is guaranteed to be called after {@link #onStart(String)}.
     *
     * @param utteranceId The utterance ID of the utterance.
     */
    @Override
    public void onDone(String utteranceId) {
        mUtteranceId = utteranceId;
        mAudioManager.abandonAudioFocus(afChangeListener);
    }

    /**
     * Called when an error has occurred during processing. This can be called
     * at any point in the synthesis process. Note that there might be calls
     * to {@link #onStart(String)} for specified utteranceId but there will never
     * be a call to both {@link #onDone(String)} and {@link #onError(String)} for
     * the same utterance.
     *
     * @param utteranceId The utterance ID of the utterance.
     * @deprecated Use {@link #onError(String, int)} instead
     */
    @Override
    public void onError(String utteranceId) {
        mUtteranceId = utteranceId;
        mAudioManager.abandonAudioFocus(afChangeListener);
    }

    public String getUtteranceId() {
        return mUtteranceId;
    }
}
