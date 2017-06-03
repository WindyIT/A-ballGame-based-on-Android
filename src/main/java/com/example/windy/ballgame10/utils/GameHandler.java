package com.example.windy.ballgame10.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.windy.ballgame10.shapes.Ball;
import com.example.windy.ballgame10.shapes.Racket;

import java.util.Random;

/**
 * Created by windy on 2017/5/24.
 */

public class GameHandler {
    private int rule;
    private float difficulty;

    private int loseBallTop;
    private int loseBallDown;

    public GameHandler() {}

    public GameHandler(int rule, float difficulty) {
        this.rule = rule;
        this.difficulty = difficulty;
    }

    public boolean isXBound(Ball ball, int tableWidth){
        return ball.getxPos() - ball.getRadius() <= 0 || ball.getxPos() >= tableWidth - ball.getRadius();
    }


    public boolean isYBounce(Ball ball, Racket racketDown){
        return ball.getyPos() - ball.getRadius() <= 0
                || ball.getyPos() >= racketDown.getyPos() - ball.getRadius();
    }

    //判断是否被挡板反弹，若否则出界
    public boolean isOutOfRacket(Ball ball, Racket racketDown, Racket racketTop){
        boolean outOfRacketD = ball.getyPos() >= racketDown.getyPos() - ball.getRadius()
                && (ball.getxPos() < racketDown.getxPos() || ball.getxPos() > racketDown.getxPos()
                + racketDown.getWidth());
        boolean outOfRacketT = ball.getyPos() < -1 + racketTop.getHeight()
                && (ball.getxPos() < racketTop.getxPos() || ball.getxPos() > racketTop.getxPos()
                + racketDown.getWidth());

        if (outOfRacketT) ++loseBallTop;
        if (outOfRacketD) ++loseBallDown;

        return  outOfRacketD || outOfRacketT;
    }


    public void handleBall(Ball ball, Racket racketDown, Racket racketTop, int tableWidth){
        // 如果小球碰到边框
        if (isXBound(ball, tableWidth)) {
            ball.xBounce();
        }

        // 如果小球高度超出了球拍位置，且横向不在球拍范围之内
        if (isOutOfRacket(ball, racketDown, racketTop)) {
            ball.IS_DELETE = true;
        }
        else if (isYBounce(ball, racketDown) && !isOutOfRacket(ball, racketDown, racketTop)){// 否则小球位于球拍之内，且到达球拍位置，小球反弹
            ball.yBounce();
        }
        // 小球移动并增速
        ball.move();
    }

    public boolean isGameOver(Ball[] ball){
        for (int i = 0; i < ball.length; i++){
            if (!ball[i].IS_DELETE) return false;
        }
        return true;
    }

    public void drawBall(Ball ball, Canvas canvas, Paint paint){
        if (!ball.IS_DELETE) {
            // 设置颜色，并绘制小球
            paint.setColor(Color.rgb(255, 0, 0));
            canvas.drawCircle(ball.getxPos(), ball.getyPos(), ball.getRadius(), paint);
        }
    }

    public float AI_racketXPos(Ball[] ball, Racket racketTop){
        int ballId = 0;
        float smallestYPos = ball[0].getyPos();
        Random rand = new Random();
        for (int i = 1; i < ball.length; i++){
            if (ball[i].getyPos() < smallestYPos){
                ballId = i;
                smallestYPos = ball[i].getyPos();
            }
        }
        //采用随机接收决定上板是否接球
        int randOffset = rand.nextInt(1024);

        //不接球
        if (randOffset >= 1024 / difficulty)
            return racketTop.getxPos();

        return ball[ballId].getxPos() - racketTop.getWidth()/ 2;
    }

    // 球全部出局后，记录输赢，局数加一

}
