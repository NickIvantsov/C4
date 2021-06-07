package ua.yandex.jere184.c4tappydefender;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class PlayerShip {
    //region members
    public static String _userName;// = "User" + c_Public._random.nextInt();
    private Bitmap _shipImg;//_shipImg1,_shipImg2,_shipImg3;
    public Bitmap _fireImg;
    public float x, y, fire_x, fire_y;
    public float touchY = 50;
    private float speed = 0;
    private int GRAVITY = 25;
    private int maxY;
    private int minY;
    private final int MIN_SPEED = 2;
    private int MAX_SPEED = 45;
    private Rect hitBox;
    private int _lifes;
    //public  List<Bitmap> _shipImgList = new ArrayList<Bitmap>();
    //region flags
    public byte isReduceShieldStrength = 0;
    public boolean is_touchSpeed = false;
    public boolean is_touchMove = false;
    public boolean is_touchUP = false;
    //endregion
    //endregion

    //region constructors
    public PlayerShip() {
        f_reInit();
    }
    //endregion

    public void update() {
        nextBitmapStep();
        if (is_touchSpeed)
            s_speed(speed + (float) 0.4);
        else
            s_speed(speed - (float) 0.6);

        //y = touchY;//не плавное ))
        //region плавное перемещение к пальцу по У
        float t_dist = touchY - y;
        if (t_dist > GRAVITY || t_dist < 0 - GRAVITY) {
            if (t_dist > 0)
                y += GRAVITY;
            else y -= GRAVITY;
        } else y += t_dist;
        //endregion

        //region проверка макс и мин
        if (y < minY) {
            y = minY;
        }
        if (y > maxY) {
            y = maxY;
        }
        //endregion
        //region прямоугольник столкновений
        hitBox.left = (int) x;
        hitBox.top = (int) y;
        hitBox.right = (int) x + _shipImg.getWidth();
        hitBox.bottom = (int) y + _shipImg.getHeight();
        //endregion
    }

    public Rect getHitBox() {
        return hitBox;
    }

    public int get_lifes() {
        return _lifes;
    }

    public void f_minusLifes() {
        isReduceShieldStrength = 25;
        _lifes--;
    }

    private void f_reinitLifes() {
        _lifes = 10;
    }

    public void f_reInit() {
        x = 70;//!!! 50;
        y = 250;
        s_speed(1);
        //bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.spaceship);

        switch (c_Public.t_playerShipType) {
            case 0:
                _shipImg = BitmapFactory.decodeResource(c_Public._context.getResources(), R.drawable.spaceship_1);
                this.MAX_SPEED = 60;
                this.GRAVITY = 10;
                break;
            case 1:
                _shipImg = BitmapFactory.decodeResource(c_Public._context.getResources(), R.drawable.spaceship);
                this.MAX_SPEED = 30;
                this.GRAVITY = 40;
                break;
            case 2:
                _shipImg = BitmapFactory.decodeResource(c_Public._context.getResources(), R.drawable.spaceship_2);
                this.MAX_SPEED = 45;
                this.GRAVITY = 20;
                break;
        }
        _shipImg = c_Public.f_scaleBitmap(_shipImg, (byte) 3);
        this.isReduceShieldStrength = 0;

        maxY = c_Public._screanSize.y - _shipImg.getHeight();
        minY = 0;
        hitBox = new Rect((int) x, (int) y, _shipImg.getWidth(), _shipImg.getHeight());
        f_reinitLifes();
    }

    private byte bitmapIndex = 0;

    private void nextBitmapStep() {
        if (bitmapIndex == 120)
            bitmapIndex = 0;
        else
            bitmapIndex++;

        Bitmap returned;
        if (bitmapIndex % 6 > 5) {
            returned = BitmapFactory.decodeResource(c_Public._context.getResources(), R.drawable.fire0);
        } else if (bitmapIndex % 6 > 4) {
            returned = BitmapFactory.decodeResource(c_Public._context.getResources(), R.drawable.fire1);
        } else if (bitmapIndex % 6 > 3) {
            returned = BitmapFactory.decodeResource(c_Public._context.getResources(), R.drawable.fire2);
        } else if (bitmapIndex % 6 > 2) {
            returned = BitmapFactory.decodeResource(c_Public._context.getResources(), R.drawable.fire3);
        } else if (bitmapIndex % 6 > 1) {
            returned = BitmapFactory.decodeResource(c_Public._context.getResources(), R.drawable.fire4);
        } else {// if (newSpeed > 10) {
            returned = BitmapFactory.decodeResource(c_Public._context.getResources(), R.drawable.fire5);
        }
        float udm = (c_Public._screanSize.x / 70) / 2;
        if (speed > 30) {
            _fireImg = c_Public.f_scaleBitmap(returned, (byte) 5);
            fire_x = -(6 * udm) - udm * 4;
            fire_y = udm - udm * 2;// код психапата
        } else if (speed > 15) {
            _fireImg = c_Public.f_scaleBitmap(returned, (byte) 4);
            fire_x = -(6 * udm) - udm * 2;
            fire_y = udm - udm;
        } else {
            _fireImg = c_Public.f_scaleBitmap(returned, (byte) 3);
            fire_x = -(6 * udm);
            fire_y = udm;
        }

    }

    public Bitmap g_bitmap() {
        return _shipImg;
    }

    public float g_speed() {
        return speed;
    }

    public void s_speed(float newSpeed) {
        if (newSpeed > MAX_SPEED) {
            newSpeed = MAX_SPEED;
        }
        if (newSpeed < MIN_SPEED) {
            newSpeed = MIN_SPEED;
        }

//    if (newSpeed > 20) {
//      bitmap = BitmapFactory.decodeResource(c_Public._context.getResources(), R.drawable.spaceship__1);
//    } else if (newSpeed > 10) {
//      bitmap = BitmapFactory.decodeResource(c_Public._context.getResources(), R.drawable.spaceship__2);
//    } else {
//      bitmap = BitmapFactory.decodeResource(c_Public._context.getResources(), R.drawable.spaceship__0);
//    }
        speed = newSpeed;
    }
}
