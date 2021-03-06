package com.example.casuneanuandrei.joc_v22;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

public class TurnTun extends Turn {
    private Bitmap framebuffer;
    private Context context;
    private Graphics graphics;

    private Image imagine_spate, imagine_fata;

    private int nr_tunari;
    private Tunar[] tunari;
    private int upgradeLevel;
    private int maxUpgradeLevel;
    private Buton butonTurn;
    private Buton[] bUpgrade;
    private int[] upgradeCost;

    private Image cerc;
    private int cercW, cercH;
    private boolean cercVisible;

    private int damage, attackSpeed;

    private Image info;


    private int x, y;
    private int w, h;

    private int xbaza, ybaza;

    private int radius;

    TurnTun(Bitmap framebuffer, Context context, int xbaza, int ybaza){
        this.framebuffer = framebuffer;
        this.context = context;

        this.xbaza = xbaza;
        this.ybaza = ybaza;

        graphics = new Graphics(framebuffer, context, false);
        imagine_spate = graphics.openImage("turn_sulitar_spate.png");
        imagine_fata = graphics.openImage("turn_sulitar_fata.png");

        w = imagine_spate.getW();
        h = imagine_spate.getH();

        x = xbaza - w / 2;
        y = ybaza - h;

        maxUpgradeLevel = 3;
        bUpgrade = new Buton[maxUpgradeLevel-1];
        upgradeCost = new int[maxUpgradeLevel];
        upgradeCost[0] = 100;
        upgradeCost[1] = 120;

        bUpgrade[0] = new Buton(framebuffer, "upgrade_tun0.png", x, y, context, false);
        bUpgrade[0].setActive(false);
        bUpgrade[0].setVisible(false);

        bUpgrade[1] = new Buton(framebuffer, "upgrade_tun1.png", x, y, context, false);
        bUpgrade[1].setActive(false);
        bUpgrade[1].setVisible(false);

        butonTurn = new Buton(framebuffer, x, y, w, h, context, false);
        butonTurn.setActive(true);


        tunari = new Tunar[3];

        nr_tunari=1;
        tunari[0] = new Tunar(framebuffer, context, xbaza - Scaler.scale(20), ybaza-h*2/3);
        radius = Scaler.scale(300);
        tunari[0].setRadius(radius);

        Bitmap aux = Bitmap.createBitmap(radius*2, radius*2, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(aux);

        Paint paint = new Paint();
        paint.setColor(Color.CYAN);
        paint.setAlpha(100);
        c.drawCircle(radius, radius, radius, paint);

        cerc = new Image(aux);
        cercW = cerc.getW();
        cercH = cerc.getH();
        cercVisible = false;

        damage = tunari[0].getDamage();
        attackSpeed = (int) tunari[0].getAttackSpeed();
        info = new Image(graphics.drawText("Damage "+ damage+"\n"+"AttackSpeed "+attackSpeed, Scaler.scale(300), Scaler.scale(45)));

    }

    @Override
    protected void onDraw() {
        int i;

        for (i=0; i<nr_tunari; i++)
            tunari[i].paint();


        for (i = 0; i < maxUpgradeLevel-1; ++i){
            bUpgrade[i].paint();
        }

        if (cercVisible) {
            graphics.drawImage(cerc, xbaza - cercW / 2, ybaza - cercH / 2, 0, 0, cercW, cercH);
            graphics.drawImage(info, xbaza - info.getW()/2, ybaza + Scaler.scale(20), 0, 0, info.getW(), info.getW());
        }
    }

    @Override
    protected int onTouch(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN){
            if (butonTurn.push((int)event.getX(), (int)event.getY()) && upgradeLevel < maxUpgradeLevel - 1){
                butonTurn.setActive(false);
                bUpgrade[upgradeLevel].setActive(true);
                bUpgrade[upgradeLevel].setVisible(true);
                cercVisible = true;
                return EventTypes.CLICK_PE_CEVA_TURN;
            }
            else if (butonTurn.push((int)event.getX(), (int)event.getY()) && upgradeLevel == 2){
                cercVisible = true;
            }
            else if (upgradeLevel == 2){
                cercVisible = false;
                return 0;
            }
            else if (bUpgrade[upgradeLevel].push((int) event.getX(), (int) event.getY())){
                if (upgradeLevel == 0 && Player.gold >= upgradeCost[upgradeLevel]){
                    Player.adaugaGold(-upgradeCost[upgradeLevel]);

                    upgradeLevel = 1;

                    tunari[0].addDamage(20);
                    damage = tunari[0].getDamage();
                    attackSpeed = (int) tunari[0].getAttackSpeed();
                    info = new Image(graphics.drawText("Damage "+ damage+"\n"+"AttackSpeed "+attackSpeed, Scaler.scale(300), Scaler.scale(45)));

                    butonTurn.setActive(true);
                    cercVisible = false;
                    bUpgrade[upgradeLevel-1].setActive(false);
                    bUpgrade[upgradeLevel-1].setVisible(false);

                    return EventTypes.CLICK_PE_CEVA_TURN;
                }
                else if (upgradeLevel == 1 && Player.gold >= upgradeCost[upgradeLevel]){
                    Player.adaugaGold(-upgradeCost[upgradeLevel]);

                    upgradeLevel = 2;

                    tunari[0].addDamage(20);
                    damage = tunari[0].getDamage();
                    attackSpeed = (int) tunari[0].getAttackSpeed();
                    info = new Image(graphics.drawText("Damage "+ damage+"\n"+"AttackSpeed "+attackSpeed, Scaler.scale(300), Scaler.scale(45)));

                    butonTurn.setActive(true);
                    cercVisible = false;
                    bUpgrade[upgradeLevel-1].setActive(false);
                    bUpgrade[upgradeLevel-1].setVisible(false);
                    return EventTypes.CLICK_PE_CEVA_TURN;
                }
            }
            else{
                int i;
                for (i = 0; i < maxUpgradeLevel - 1; ++i) {
                    bUpgrade[i].setActive(false);
                    bUpgrade[i].setVisible(false);
                }
                butonTurn.setActive(true);
                cercVisible = false;
            }
        }
        return 0;
    }

    @Override
    protected void onUpdate() {
        int i;
        for (i=0; i<nr_tunari; i++)
            tunari[i].update();
    }

    @Override
    protected void inchide() {
        int i;
        for (i = 0; i < maxUpgradeLevel - 1; ++i){
            //bUpgrade[i].setActive(false);
            //bUpgrade[i].setVisible(false);
            //butonTurn.setActive(true);
        }

    }
}
