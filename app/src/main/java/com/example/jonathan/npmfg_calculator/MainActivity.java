package com.example.jonathan.npmfg_calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ABC";
    TextView topNum;
    TextView bottomNum;
    boolean containsDot = false;
    DecimalFormat df = new DecimalFormat("#.####");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topNum = findViewById(R.id.top_num);
        bottomNum = findViewById(R.id.bottom_num);
        Log.d(TAG, "oncreateabc");
        metricsSetup();
    }

    private void metricsSetup(){
        DisplayMetrics displayMetrics= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        Log.d(TAG, "metricssetup " + String.valueOf(width));
    }

    public void putNumber(View view) {
        String num = ((TextView)view).getText().toString();
        String currentNum = bottomNum.getText().toString();
        if(currentNum.equals("0")){
            bottomNum.setText(num);
        }else{
            bottomNum.setText(currentNum + num);
        }
        convertNum();
    }

    public void putDot(View view) {
        if(!containsDot){
            String currentNum = bottomNum.getText().toString();
            bottomNum.setText(currentNum + ".");
            containsDot = true;
        }
    }

    public void clearNum(View view) {
        topNum.setText("0");
        bottomNum.setText("0");
        containsDot = false;
    }

    private void convertNum() {
        String unit1 = ((TextView)findViewById(R.id.unit_1)).getText().toString();
        TextView unit2View = findViewById(R.id.unit_2);
        String unit2 = unit2View.getText().toString();
        String num1 = bottomNum.getText().toString();
        String newNum = topNum.getText().toString();
        switch(unit2){
            case "in":
                if(unit1.equals("mm")){
                    newNum = String.valueOf(df.format(inToMm(Double.valueOf(num1), false)));
                }
                break;
            case "mm":
                if(unit1.equals("in")){
                    newNum = String.valueOf(df.format(inToMm(Double.valueOf(num1), true)));
                }
        }
        topNum.setText(newNum);
    }

    private double inToMm(double num1, boolean reverse){
        return reverse ? num1/25.4 : num1*25.4;
    }

    public void switchUnit(View view) {
    }
}
