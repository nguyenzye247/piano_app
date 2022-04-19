package com.huonglenguyen.piano.model;

import android.graphics.RectF;

import androidx.annotation.Nullable;

public class Key {
    public int sound;
    public RectF rect;
    public boolean isDown;
    public boolean isPressed = false;
    public boolean color; // true - white, false - black

    public Key(int sound, RectF rect, boolean color) {
        this.sound = sound;
        this.rect = rect;
        this.color = color;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        assert obj != null;
        return this.sound == ((Key)obj).sound;
    }
}
