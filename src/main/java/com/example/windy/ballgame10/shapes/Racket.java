package com.example.windy.ballgame10.shapes;

/**
 * Created by windy on 2017/5/9.
 */

public class Racket {
    private float xPos; //球拍左下角x坐标
    private float yPos; //球拍左下角y坐标
    private int width;  //球拍宽度
    private int height; //球拍高度

    public Racket(float xPos, float yPos, int width, int height) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
    }

    public void moveLeft(int x){
        if (xPos - x >= 0)
            xPos -= x;
        else
            xPos = 0;
    }

    public void moveRight(int x, int tableWidth){
        if (xPos + width + x <= tableWidth)
            xPos += x;
        else
            xPos = tableWidth - width;
    }

    public float getxPos() {
        return xPos;
    }

    public float getyPos() {
        return yPos;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setxPos(float xPos){
        this.xPos = xPos;
    }
}
