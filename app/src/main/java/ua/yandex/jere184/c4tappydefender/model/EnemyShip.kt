package ua.yandex.jere184.c4tappydefender.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.ArrayList;

import ua.yandex.jere184.c4tappydefender.util.Public;
import ua.yandex.jere184.c4tappydefender.R;

public class EnemyShip {
    public Bitmap bitmap;
    public int x, y;
    private int speed = 1;
    private byte size = 2;
    private int maxX;
    private int minX;
    private int maxY;
    private int minY;
    private Rect hitBox;

    public EnemyShip(int screenX, int screenY) {
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;
        f_reInit();

    }

    public void f_reInit() {
        int whichBitmap = Public.random.nextInt(6);
        size = (byte) (whichBitmap + 1);
        speed = 24 - (size * 3);

        bitmap = Public.scaleBitmap(animateImgArray.get(0), (byte) (size * 2));
        //bitmap = BitmapFactory.decodeResource(c_Public._context.getResources(), R.drawable.asteroid_01);
//    switch (whichBitmap) {
//      case 0:
//        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid_01);//enemy_1);//
////        speed = 10;
////        size = 1;
//        break;
//      case 1:
//        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid_01);//enemy_2);//
////        speed = 20;
////        size = 3;
//        break;
//      case 2:
//        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid_01);//enemy_3);//
////        speed = 6;
//        //size = 1;
//        break;
//      case 3:
//        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid_01);//enemy_4);//
////        speed = 18;
//        //size = 1;
//        break;
//      case 4:
//        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid_01);//enemy_5);//
////        speed = 13;
//        //size = 1;
//        break;
//      case 5:
//        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid_01);
////        speed = 13;
//        //size = 3;
//        break;
//    }
        //bitmap = c_Public.f_scaleBitmap(bitmap, (byte) (size * 2));
        hitBox = new Rect(x + 10, y + 10, bitmap.getWidth() - 10, bitmap.getHeight() - 10);

        y = Public.random.nextInt(maxY) - bitmap.getHeight();
        x = maxX + 100;
    }

    public void update(float playerSpeed) {
        nextBitmapStep();
        x -= playerSpeed;
        x -= speed;
        if (x < minX - bitmap.getWidth()) {
            f_reInit();
//      speed = c_Public._random.nextInt(10) + 10;
//      x = maxX;
//      y = c_Public._random.nextInt(maxY) - bitmap.getHeight();
        }

        //region прямоугольник столкновений
        hitBox.left = x + bitmap.getWidth() / 2; // = x; // тестовый вариант
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();
        //endregion
    }

    //region animations
    public static void f_initBitmap() {
        _bigBitmap = BitmapFactory.decodeResource(Public.context.getResources(), R.drawable.asteroid_big);
        int partImgSizeX = _bigBitmap.getHeight() / 8;// 192;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                animateImgArray.add(Bitmap.createBitmap(_bigBitmap, c * partImgSizeX, r * partImgSizeX, partImgSizeX, partImgSizeX));
            }
        }
    }

    public static ArrayList<Bitmap> animateImgArray = new ArrayList<>();
    public static Bitmap _bigBitmap;
    private byte bitmapIndex = 0;
    private byte bitmapIndex2 = 0;

    private void nextBitmapStep() {
        bitmapIndex2++;
        if ((bitmapIndex) == 127)//animateImgArray.size()-1)
            bitmapIndex = 0;
        else {

            if (bitmapIndex2 % 3 == 0) {
                bitmapIndex++;
                bitmap = Public.scaleBitmap(animateImgArray.get(bitmapIndex % animateImgArray.size())/*returned*/, (byte) (size * 2));
            }
        }
//
//    byte pos_r = (byte) (bitmapIndex / 8);
//    byte pos_c = (byte) (bitmapIndex - (pos_r * 8));
//    int partImgSize = 192;
//
//    Bitmap returned /*bitmap*/ = Bitmap.createBitmap(_bigBitmap, pos_c * partImgSize, pos_r * partImgSize, partImgSize, partImgSize);
//    //bitmap= Bitmap.createBitmap(_bigBitmap, 128*0, 128*0, 128, 128);
    }
    //endregion

    public Rect getHitBox() {
        return hitBox;
    }

    public void setX(int x) {
        this.x = x;
    }
}
