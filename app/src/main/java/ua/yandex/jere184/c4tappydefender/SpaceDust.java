package ua.yandex.jere184.c4tappydefender;

public class SpaceDust {
    public short x, y;
    private short speed;
    public byte counter=0;
    public boolean downLight=true;

    //Detect dust leaving the screen
    private short maxX;
    private short maxY;
    private short minX;
    private short minY;

    public SpaceDust(short screenX,short screenY){
        counter = (byte)c_Public._random.nextInt(110);
        maxX = screenX;
        maxY = screenY;
        minX=0;
        minY=0;

        //Set a speed between 0 and 9
        speed =(short) c_Public._random.nextInt(10);

        //Set the starting coordinates
        x =(short) c_Public._random.nextInt(maxX);
        y =(short) c_Public._random.nextInt(maxY);
    }

    public void update(float playerSpeed){
        //Speed up when the player does
        x-=playerSpeed;
        x-=speed;
        //respawn space dust
        if (x<0){
            x = maxX;
            y =(short) c_Public._random.nextInt(maxY);
            speed =(short) c_Public._random.nextInt(15);
        }
    }
}

