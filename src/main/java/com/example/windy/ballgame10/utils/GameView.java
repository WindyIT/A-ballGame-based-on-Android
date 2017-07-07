package com.example.windy.ballgame10.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.windy.ballgame10.MainActivity;
import com.example.windy.ballgame10.shapes.Ball;
import com.example.windy.ballgame10.shapes.Racket;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by windy on 2017/5/22.
 */

public class GameView extends View {
    private Ball[] balls;
    private Racket racketDown;
    private Racket racketTop;

    private boolean isLose = false; //游戏结束标志

    private int round = 1;

    private GameHandler gameHandler = new GameHandler();
    private Paint paint = new Paint();

    public GameView(Context context, Ball[] balls, Racket racketDown, Racket racketTop) {
        super(context);
        this.balls = balls;
        this.racketDown = racketDown;
        this.racketTop = racketTop;
    }

    public void setRule(int rule){
        gameHandler.setRule(rule);
    }

    public void setDifficulty(float difficulty){
        gameHandler.setDifficulty(difficulty);
    }

    // 重写View的onDraw方法，实现绘画
    public void onDraw(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        // 设置去锯齿
        paint.setAntiAlias(true);
        // 如果游戏已经结束
        if (gameHandler.isGameOver(balls)) {

            paint.setColor(Color.BLUE);
            paint.setTextSize(50);

            String text = "Double tap to Round" + round;
            float textLength = paint.measureText(text);

            canvas.drawText(text, (MainActivity.windowWidth- textLength) / 2, 500, paint);
            // Toast.makeText(getApplicationContext(), "3s 后开始第" + cntGame + "局", Toast.LENGTH_SHORT).show();

            //延时操作
        /*    Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                }
            };
            timer.schedule(task, 3000);*/
        }
        // 如果游戏还未结束
        else {
            for (int i = 0; i < balls.length; i++) {
                gameHandler.drawBall(balls[i], canvas, paint);
            }

            // 设置颜色，并绘制球拍
            paint.setColor(Color.rgb(80, 80, 200));
            canvas.drawRect(racketDown.getxPos(), racketDown.getyPos(), racketDown.getxPos() + racketDown.getWidth(),
                    racketDown.getyPos() + racketDown.getWidth(), paint);

            //获取离上墙距离最近的小球的x坐标
            racketTop.setxPos(gameHandler.AI_racketXPos(balls, racketTop));
            canvas.drawRect(racketTop.getxPos(), racketTop.getyPos(), racketTop.getxPos() + racketDown.getWidth(),
                    racketTop.getyPos() + racketDown.getHeight(), paint);
        }
    }

    public void handlerBalls(int tableWidth) {
        for (int i = 0; i != balls.length; i++)
            gameHandler.handleBall(balls[i], racketDown, racketTop, tableWidth);
    }

    public void intoNextRound() {
        round++;
    }

    public boolean isGameOver() {
        return gameHandler.isGameOver(balls);
    }
}
