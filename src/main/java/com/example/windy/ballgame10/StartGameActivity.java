package com.example.windy.ballgame10;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.windy.ballgame10.OptionFragment._PassResult;

public class StartGameActivity extends FragmentActivity implements _PassResult{
    private FragmentManager fm = getSupportFragmentManager();
    private final static String DIF = "难度";
    private final static String RULE = "赛制";

    private String difResult = "";
    private String ruleResult = "";

    private final int RULE_FIVE = 5;
    private final int RULE_SEVEN = 7;
    private  final float DIFF_EASY = 2;     //胜率 1/2
    private final float DIFF_NORMAL = 1.5f; //胜率 1/3
    private final float DIFF_GOD = 1;       //胜率 0

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        Button ruleButton = (Button)this.findViewById(R.id.ruleBt);
        Button difButton = (Button)this.findViewById(R.id.difBt);
        Button startButton = (Button)this.findViewById(R.id.startBt);

        ruleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OptionFragment optionFragment = OptionFragment.newInstance(RULE);
                optionFragment.show(fm, "Rule");
            }
        });

        difButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OptionFragment optionFragment = OptionFragment.newInstance(DIF);
                optionFragment.show(fm, "Diff");
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartGameActivity.this, MainActivity.class);

                intent.putExtra("Diff", transDiffResult());
                intent.putExtra("Rule", transRuleResult());

                startActivity(intent);
            }
        });

        /*quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });*/
    }

    @Override
    public void getDifResult(String result) {
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        difResult = result;
    }

    @Override
    public void getRuleResult(String result) {
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        ruleResult = result;
    }

    private int transRuleResult(){
        return ruleResult == "七局五胜" ? RULE_SEVEN : RULE_FIVE; //默认五局三胜
    }

    private float transDiffResult(){
        switch (difResult){
            case "简单" : return DIFF_EASY;
            case "一般" : return DIFF_NORMAL;
            case "困难" : return DIFF_GOD;
        }
        return DIFF_NORMAL; //默认一般
    }
}
