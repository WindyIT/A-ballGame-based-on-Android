package com.example.windy.ballgame10.shapes;

/**
 * Created by windy on 2017/5/9.
 */

public class Ball {
    private float xSpeed; //水平方向速度
    private float ySpeed; //垂直方向速度
    private float k; //运动斜率
    private float xPos;     //圆心X坐标
    private float yPos;     //圆心Y坐标
    private float radius; //圆半径
    public static final float ACCELERATE_TIME = 1.004f;
    public boolean IS_DELETE = false;


    private void speedUp(){
        //使球慢慢加速
    }

    public Ball(float xPos, float yPos, float radius) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.radius = radius;
    }

    public Ball(int xSpeed, int ySpeed, int xPos, int yPos, float radius) {
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.xPos = xPos;
        this.yPos = yPos;
        this.radius = radius;
        this.k = (float)ySpeed / (float)xSpeed;
    }

    public void move(){
        xPos += xSpeed;
        yPos += ySpeed;
        accelerate();
    }

    private void accelerate(){
        xSpeed *= ACCELERATE_TIME;
        ySpeed *= ACCELERATE_TIME;
    }

    public void xBounce(){
        xSpeed = -xSpeed;
    }

    public void yBounce(){
        ySpeed = -ySpeed;
    }

    public void setxSpeed(float xSpeed) {
        this.xSpeed = xSpeed;
    }

    public void setySpeed(float ySpeed) {
        this.ySpeed = ySpeed;
    }

    public float getxPos() {
        return xPos;
    }

    public float getyPos() {
        return yPos;
    }

    public float getRadius() {
        return radius;
    }
}
