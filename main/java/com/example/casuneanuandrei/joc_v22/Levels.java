package com.example.casuneanuandrei.joc_v22;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.MotionEvent;

public class Levels extends Screen {
    private Graphics graphics;
    private Image background;
    private Bitmap framebuffer;
    private Context context;

    private Buton butonLevel1;

    public Levels(Bitmap framebuffer, Context context){
        super();

        this.framebuffer = framebuffer;
        this.context = context;

        graphics = new Graphics(framebuffer, context);
        background = graphics.openImage("poze/backgroundLevels.png");

        butonLevel1 = new Buton(framebuffer, "poze/meniu_buton.png", 100, 100, context);
    }

    @Override
    protected void onDraw() {
        graphics.resetCanvas();
        graphics.drawImage(background, 0, 0, 0, 0, background.getW(), background.getH());
        butonLevel1.paint();
    }

    @Override
    protected int onTouch(MotionEvent event) {
        int x, y;
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN){
            x = (int) event.getX();
            y = (int) event.getY();

            if (butonLevel1.push(x, y))
                return EventTypes.SCHIMBA_LEVEL1;
        }
        return EventTypes.NONE_OF_YOUR_B;
    }

    @Override
    protected void onUpdate() {

    }
}
