/**
 *  A Ball Game based on Android canvas
 *  Created by windy - MAY 23, 2017
 */

/**
 * Anti-BUG log
 * isLose无法判定是否该游戏结束
 * 随机生成在边缘的球体无法正常反弹 GET_OVER
 */
package com.example.windy.ballgame10;


import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.windy.ballgame10.shapes.Ball;
import com.example.windy.ballgame10.shapes.Racket;
import com.example.windy.ballgame10.utils.*;


public class MainActivity extends Activity {
    // 桌面的宽度
    private int tableWidth;
    // 桌面的高度
    private int tableHeight;

    //小球
    private final int NUM_OF_BALLS = 5;
    private Ball balls[] = new Ball[NUM_OF_BALLS];
    private final int RADIUS = 34;

    //球拍
    private Racket racketDown;
    private Racket racketTop;

    //球拍高度及宽度
    private final int RACKET_HEIGHT = 30;
    private final int RACKET_WIDTH = 130;

    //赛制局数, 默认五局三胜
    /**
     * rule == 5 五局三胜 || rule == 7 七局五胜
     * cntWin == 3 win
     * cntLose == 3 lose
     * cntGame用于显示正在进行第几局
     * */
    /*private int rule = 5;
    private int cntLose = 0;
    private int cntWin = 0;
    private int cntGame = 1;
    private int loseBallTop = 0;
    private int loseBallDown = 0;*/

    /**
     * difficulty == 2 简单
     *            == 1.2 一般
     *            == 1 困难 默认困难模式无法击败
     */
    private float difficulty = 1.15f;

    //当局游戏是否结束标志
    private boolean isGameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        // 获取窗口管理器
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        // 获得屏幕宽和高
        tableWidth = metrics.widthPixels;
        tableHeight = metrics.heightPixels;

        //生成球，设置初始位置，大小及速度
        Random rand = new Random();
        balls[0] = new Ball(tableWidth / 2 - 50, tableHeight / 2 - 40, RADIUS);
        balls[0].setxSpeed(25);
        balls[0].setySpeed(20);

        balls[1] = new Ball(tableWidth / 2 - 50, tableHeight / 2 + 40, RADIUS);
        balls[1].setxSpeed(-30);
        balls[1].setySpeed(20);

        balls[2] = new Ball(tableWidth / 2 + 50, tableHeight / 2 - 40, RADIUS);
        balls[2].setxSpeed(15);
        balls[2].setySpeed(-20);

        balls[3] = new Ball(tableWidth / 2 + 50, tableHeight / 2 + 40, RADIUS);
        balls[3].setxSpeed(-20);
        balls[3].setySpeed(-25);

        balls[4] = new Ball(tableWidth / 2, tableHeight / 2, RADIUS);
        balls[4].setxSpeed(30);
        balls[4].setySpeed(30);

        //生成球拍，设置大小及位置
        racketDown = new Racket(tableWidth / 2, tableHeight - 80, RACKET_WIDTH, RACKET_HEIGHT);
        racketTop = new Racket(tableWidth / 2, 0, RACKET_WIDTH, RACKET_HEIGHT);

        //生成游戏界面
        final GameView gameView = new GameView(this, balls, racketDown, racketTop);
        setContentView(gameView);

        //Handler + Looper 重绘机制
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 0x233) {
                    isGameOver = gameView.isGameOver();
                    gameView.invalidate();
                }
            }
        };



        final Timer timer = new Timer();

        if (!isGameOver) {
            //球拍控制事件
            gameView.setOnTouchListener(new View.OnTouchListener() {
                Float mXPos = 0f;
                Float curXPos = 0f;
                Float minDis = 15f;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            mXPos = event.getX();
                        case MotionEvent.ACTION_MOVE:
                            curXPos = event.getX();
                        case MotionEvent.ACTION_UP:{
                            if (Math.abs(curXPos - mXPos) >= minDis){
                                if (curXPos > mXPos){
                                    racketDown.moveRight(10, tableWidth);
                                }
                                else{
                                    racketDown.moveLeft(10);
                                }
                            }
                        }
                    }

                    gameView.invalidate();
                    return true;
                }
            });

            timer.schedule(new TimerTask() // ①
            {
                @Override
                public void run() {
                    //Ball数组代替
                    gameView.handlerBalls(tableWidth);

                    if (isGameOver) {
                        timer.cancel();
                    }
                    else {
                        // 发送消息，通知系统重绘组件
                        handler.sendEmptyMessage(0x233);
                    }
                }
            }, 0, 100);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
