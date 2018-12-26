package com.example.jonathan.npmfg_calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ABC";
    TextView topNum;
    TextView bottomNum;
    boolean containsDot = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topNum = findViewById(R.id.top_num);
        bottomNum = findViewById(R.id.bottom_num);
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
        bottomNum.setText("0");
        containsDot = false;
    }

    private void convertNum() {
        String unit1 = ((TextView)findViewById(R.id.unit_1)).getText().toString();
        TextView unit2View = findViewById(R.id.unit_2);
        String unit2 = unit2View.getText().toString();
        String num1 = bottomNum.getText().toString();
        String newNum = topNum.getText().toString();
        switch(unit1){
            case "in":
                if(unit2.equals("cm")){
                    newNum = String.valueOf(inToCm(Double.valueOf(num1), false));
                }
                break;
            case "mm":
                if(unit2.equals("in")){
                    newNum = String.valueOf(inToCm(Double.valueOf(num1), true));
                }
        }
        topNum.setText(newNum);
    }

    private double inToCm(double num1, boolean reverse){
        return reverse ? num1/2.54 : num1*2.54;
    }

    public void switchUnit(View view) {
    }
}
