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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
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

    //赛制及难度
    private int rule;
    private float diff;

    public static float windowWidth;


    //当局游戏是否结束标志
    private boolean isGameOver = false;

    //帧管理器
    private Timer timer;

    private int tapCount = 0;

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
        windowWidth=tableWidth = metrics.widthPixels;
        tableHeight = metrics.heightPixels;

        //生成球，设置初始位置，大小及速度
        initBalls();

        //生成球拍，设置大小及位置
        racketDown = new Racket(tableWidth / 2, tableHeight - 80, RACKET_WIDTH, RACKET_HEIGHT);
        racketTop = new Racket(tableWidth / 2, 0, RACKET_WIDTH, RACKET_HEIGHT);

        //获取赛制和难度
        Intent intent = new Intent();
        rule = intent.getIntExtra("Rule", 5);
        diff = intent.getFloatExtra("Diff", 1.5f);

        //生成游戏界面
        final GameView gameView = new GameView(this, balls, racketDown, racketTop);
        //设置赛制和难度
        gameView.setRule(rule);
        gameView.setDifficulty(diff);
        setContentView(gameView);

//        //Test
//        Toast.makeText(getApplicationContext(), rule, Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(), (int)diff, Toast.LENGTH_SHORT).show();

        //Handler + Looper 重绘机制
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 0x233) {
                    isGameOver = gameView.isGameOver();
                    gameView.invalidate();
                }
            }
        };


        timer = new Timer();


        //球拍控制事件
        gameView.setOnTouchListener(new View.OnTouchListener() {
            Float mXPos = 0f;
            Float curXPos = 0f;
            Float minDis = 15f;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gameView.isGameOver() && event.getAction()==MotionEvent.ACTION_UP) {
                    tapCount++;
                    if (tapCount==2) {
                        initBalls();
                        gameView.intoNextRound();
                        tapCount=0;
                    }
                } else {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mXPos = event.getX();
                        case MotionEvent.ACTION_MOVE:
                            curXPos = event.getX();
                        case MotionEvent.ACTION_UP: {
                            if (2 * minDis > Math.abs(curXPos - mXPos) && Math.abs(curXPos - mXPos) >= minDis) {
                                if (curXPos > mXPos) {
                                    racketDown.moveRight(10, tableWidth);
                                } else {
                                    racketDown.moveLeft(10);
                                }
                            }
                            else if (Math.abs(curXPos - mXPos) > 2 * minDis){
                                if (curXPos > mXPos) {
                                    racketDown.moveRight(25, tableWidth);
                                } else {
                                    racketDown.moveLeft(25);
                                }
                            }
                        }
                    }
                }
                gameView.invalidate();
                return true;
            }
        });


        //计时器一般情况下不应该停下来，它是一直运行着的，除非退出游戏
        timer.schedule(new TimerTask() // ①
        {
            @Override
            public void run() {
                //Ball数组代替
                gameView.handlerBalls(tableWidth);


                // 发送消息，通知系统重绘组件
                handler.sendEmptyMessage(0x233);

            }
        }, 0, 50);
    }


    private void initBalls() {
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
    }

    @Override
    public void onBackPressed() {
        timer.cancel();
        finish();
    }
}
