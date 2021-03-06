package com.example.casuneanuandrei.joc_v22;

import android.view.MotionEvent;

/**
 * Created by casuneanuandrei on 5/9/15.
 */
public abstract class Screen{
    private Graphics graphics;

    public Screen(){
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public void paint(){
        onDraw();
    }
    public int click(MotionEvent event){
        return onTouch(event);
    }
    public void update(){onUpdate();}

    protected abstract void onDraw();
    protected abstract int onTouch(MotionEvent event);
    protected abstract void onUpdate();

}
