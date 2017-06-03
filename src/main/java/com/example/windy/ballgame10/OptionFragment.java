package com.example.windy.ballgame10;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public class OptionFragment extends DialogFragment{
    private String difOptions[] = {"简单", "一般", "困难"};
    private String ruleOptions[] = {"五局三胜", "七局四胜"};

    private String title = "";

    private static final String ARG_PARAM1 = "";
    public static final int REQUEST_CODE = 0x0001;

    //所点击的事件
    private final String DIF = "难度";
    private final String RULE = "赛制";


    //用于传递参数给Activity的接口
    public interface _PassResult{
        void getRuleResult(String result);
        void getDifResult(String result);
    }

    public OptionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_PARAM1);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        //创建窗口
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        if (getArguments() != null) {
            title = getArguments().getString(ARG_PARAM1);
        }

        if (title.equals(DIF)) {
            dialogBuilder.setTitle(title).setItems(difOptions,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            _PassResult passResult = (_PassResult)getActivity();
                            passResult.getDifResult(difOptions[which]);
                        }
                    });
        }else{
            dialogBuilder.setTitle(title).setItems(ruleOptions,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            _PassResult passResult = (_PassResult)getActivity();
                            passResult.getRuleResult(ruleOptions[which]);
                        }
                    });
        }

        return dialogBuilder.create();
    }

    public static OptionFragment newInstance(String param1) {
        OptionFragment fragment = new OptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }
}
