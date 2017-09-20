package com.exercise.tiger.mylearnapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.exercise.tiger.mylearnapplication.testRetrofit.activity.TestRetrofitActivity;
import com.exercise.tiger.mylearnapplication.testRetrofit.activity.TestRvWithRetrofitActivity;
import com.exercise.tiger.mylearnapplication.utils.AppToast;
import com.exercise.tiger.mylearnapplication.utils.FormatUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = (TextView) findViewById(R.id.tv_clicked);
        tv.setOnClickListener(this);
        Button retrofitBtn = (Button) findViewById(R.id.btn_goto_retrofit_test);
        retrofitBtn.setOnClickListener(this);
        Button rvBtn = (Button) findViewById(R.id.btn_goto_rv_test);
        rvBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_clicked:
//                TestRxJavaActivity.startByIntent(this);
                AppToast.showShortText(this, FormatUtils.decodeFromUnicode("\\u5341\\u4e8c\\u6012\\u6c49"));
                break;
            case R.id.btn_goto_retrofit_test:
                TestRetrofitActivity.startActivityByIntent(this);
                break;
            case R.id.btn_goto_rv_test:
                TestRvWithRetrofitActivity.startActivityByIntent(this);
                break;
            default:
                break;
        }
    }
}
