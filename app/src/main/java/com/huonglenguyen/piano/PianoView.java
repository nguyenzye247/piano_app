package com.huonglenguyen.piano;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.huonglenguyen.piano.model.Key;

import java.util.ArrayList;

public class PianoView extends View {
    private static final int NUMBER_KEYS = 14;
    private Paint black, white, yellow;
    private ArrayList<Key> whites, blacks;
    private int keyWidth, keyHeight;
    private SoundManager soundManager;

    public PianoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        black = new Paint();
        black.setColor(Color.BLACK);
        black.setStyle(Paint.Style.FILL);

        white = new Paint();
        white.setColor(Color.WHITE);
        white.setStyle(Paint.Style.FILL);

        yellow = new Paint();
        yellow.setColor(Color.YELLOW);
        yellow.setStyle(Paint.Style.FILL);

        whites = new ArrayList<Key>();
        blacks = new ArrayList<Key>();

        soundManager = SoundManager.getInstance();
        soundManager.init(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        keyWidth = w / NUMBER_KEYS;
        keyHeight = h;

        int blackCount = 15;

        for(int i = 0; i < NUMBER_KEYS; i++){
            int left_edge = i*keyWidth;
            int right_edge = left_edge + keyWidth;

            RectF rect = new RectF(left_edge, 0, right_edge, keyHeight);
            int whiteSound = R.raw.a2;
            switch(i){
                case 0:{
                    whiteSound = R.raw.c3;
                    break;
                }
                case 1:{
                    whiteSound = R.raw.d3;
                    break;
                }
                case 2:{
                    whiteSound = R.raw.e3;
                    break;
                }
                case 3:{
                    whiteSound = R.raw.f3;
                    break;
                }
                case 4:{
                    whiteSound = R.raw.g3;
                    break;
                }
                case 5:{
                    whiteSound = R.raw.a3;
                    break;
                }
                case 6:{
                    whiteSound = R.raw.b3;
                    break;
                }
                case 7:{
                    whiteSound = R.raw.c4;
                    break;
                }
                case 8:{
                    whiteSound = R.raw.d4;
                    break;
                }
                case 9:{
                    whiteSound = R.raw.e4;
                    break;
                }
                case 10:{
                    whiteSound = R.raw.f4;
                    break;
                }
                case 11:{
                    whiteSound = R.raw.g4;
                    break;
                }
                case 12:{
                    whiteSound = R.raw.a4;
                    break;
                }
                case 13:{
                    whiteSound = R.raw.b4;
                    break;
                }
            }
            whites.add(new Key(whiteSound, rect, true));

            // draw black keys
            if(i != 0 && i!=3 && i!= 7 && i!=10){
                rect = new RectF((float)(i-1)*keyWidth + 0.75f*keyWidth,
                        0,
                        (float)i*keyWidth + 0.24f*keyWidth,
                        keyHeight*0.7f);
                int blackSound = 0;
                switch(blackCount){
                    case 15:{
                        blackSound = R.raw.ct4;
                        break;
                    }
                    case 16:{
                        blackSound = R.raw.dt4;
                        break;
                    }
                    case 17:{
                        blackSound = R.raw.ft4;
                        break;
                    }
                    case 18:{
                        blackSound = R.raw.gt4;
                        break;
                    }
                    case 19:{
                        blackSound = R.raw.at4;
                        break;
                    }
                    case 20:{
                        blackSound = R.raw.ct5;
                        break;
                    }
                    case 21:{
                        blackSound = R.raw.dt5;
                        break;
                    }
                    case 22:{
                        blackSound = R.raw.ft5;
                        break;
                    }
                    case 23:{
                        blackSound = R.raw.gt5;
                        break;
                    }
                    case 24:{
                        blackSound = R.raw.at5;
                        break;
                    }
                }
                blacks.add(new Key(blackSound, rect, false));
                blackCount++;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for(Key key : whites){
            canvas.drawRect(key.rect, key.isDown ? yellow : white);
        }

        for(int i = 0; i < NUMBER_KEYS; i++){
            canvas.drawLine(i*keyWidth, 0, i*keyWidth, keyHeight, black);
        }

        for(Key key : blacks){
            canvas.drawRect(key.rect, key.isDown ? yellow : black);
        }
    }

    public void setKeysUntouchedExcept(Key key){
        for(Key k : whites){
            if(!k.equals(key)){
                k.isDown = false;
                k.isPressed = false;
            }
        }
        for(Key k : blacks){
            if(!k.equals(key)){
                k.isDown = false;
                k.isPressed = false;
            }
        }
    }

    public Key keyForCoord(float x, float y){
        for(Key k : blacks){
            if(k.rect.contains(x,y)){
                return k;
            }
        }
        for(Key k : whites){
            if(k.rect.contains(x,y)){
                return k;
            }
        }
        return null;
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x1, y;
        for(int touchIndex = 0; touchIndex < event.getPointerCount(); touchIndex++){
            x1 = event.getX(touchIndex);
            y = event.getY(touchIndex);
            if(keyForCoord(x1, y).color){
                for(Key key : whites){
                    if(key.rect.contains(x1, y)){
                        if(action == MotionEvent.ACTION_DOWN){
                            key.isDown = true;
                            key.isPressed = true;
                            soundManager.playSound(key.sound);
                        }
                        else if(action == MotionEvent.ACTION_MOVE){
                            key.isDown = true;
                            setKeysUntouchedExcept(key);
                            if(!key.isPressed){
                                soundManager.playSound(keyForCoord(x1, y).sound);
                                key.isPressed = true;
                            }
                        }
                        else{
                            key.isDown = false;
                            key.isPressed = false;
                        }
                        Log.d("PIANO", "Sound: " + key.sound);
                        break;
                    }
                }
            }
            else{
                for(Key key : blacks){
                    if(key.rect.contains(x1, y)){
                        if(action == MotionEvent.ACTION_DOWN){
                            key.isDown = true;
                            key.isPressed = true;
                            soundManager.playSound(key.sound);
                        }
                        else if(action == MotionEvent.ACTION_MOVE){
                            key.isDown = true;
                            setKeysUntouchedExcept(key);
                            if(!key.isPressed){
                                soundManager.playSound(keyForCoord(x1, y).sound);
                                key.isPressed = true;
                            }
                        }
                        else{
                            key.isDown = false;
                            key.isPressed = false;
                        }
                        Log.d("PIANO", "Sound: " + key.sound);
                        break;
                    }
                }
            }
        }
        invalidate();
        return true;
    }
}
