package com.huonglenguyen.piano;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.util.SparseIntArray;


public class SoundManager {
    private SoundPool mSoundPool;
    private SparseIntArray mSoundPoolMap;
    private boolean mMute = false;
    private Context mContext;

    private static final int MAX_STREAM = 10;
    private static final int STOP_DELAY_MILLIS = 10000;
    private Handler mHandler;

    private static SoundManager instance =null;

    public SoundManager(){
        mSoundPool = new SoundPool.Builder()
        .setMaxStreams(MAX_STREAM)
        .build();
//(MAX_STREAM,
//                AudioManager.STREAM_MUSIC,
//                0)
        mSoundPoolMap = new SparseIntArray();
        mHandler = new Handler();
    }

    public static SoundManager getInstance(){
        if (instance == null){
            instance = new SoundManager();
        }

        return instance;
    }

    public void initStreamTypeMedia(Activity activity){
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    public void addSound(int soundID){
        mSoundPoolMap.put(soundID, mSoundPool.load(mContext, soundID, 1));
    }

    public void playSound(int soundID){
        if(mMute){
            return;
        }

        boolean hasSound = mSoundPoolMap.indexOfKey(soundID) >=0;

        if(!hasSound){
            return;
        }

        final int soundId = mSoundPool.play(mSoundPoolMap.get(soundID),1,1,1, 0, 1f);
        scheduleSoundStop(soundId);
    }

    private void scheduleSoundStop(final int soundID){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSoundPool.stop(soundID);
            }
        }, STOP_DELAY_MILLIS);
    }

    public void init(Context context){
        this.mContext = context;
        instance.initStreamTypeMedia((Activity)mContext);
        instance.addSound(R.raw.c3);
        instance.addSound(R.raw.c4);
        instance.addSound(R.raw.d3);
        instance.addSound(R.raw.d4);
        instance.addSound(R.raw.e3);
        instance.addSound(R.raw.e4);
        instance.addSound(R.raw.f3);
        instance.addSound(R.raw.f4);
        instance.addSound(R.raw.g3);
        instance.addSound(R.raw.g4);
        instance.addSound(R.raw.a3);
        instance.addSound(R.raw.a4);
        instance.addSound(R.raw.b3);
        instance.addSound(R.raw.b4);
        instance.addSound(R.raw.at4);
        instance.addSound(R.raw.at5);
        instance.addSound(R.raw.ct4);
        instance.addSound(R.raw.ct5);
        instance.addSound(R.raw.dt4);
        instance.addSound(R.raw.dt5);
        instance.addSound(R.raw.ft4);
        instance.addSound(R.raw.ft5);
        instance.addSound(R.raw.gt4);
        instance.addSound(R.raw.gt5);
    }

}
