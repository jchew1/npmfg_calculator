package com.example.jonathan.npmfg_calculator;

import android.graphics.Point;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ABC";
    private static final int numButtonsX = 4;
    private static final int numButtonsY = 5;
    private static final String[] units = new String[]{"in", "mm", "cm", "ft"};
    TextView topNumView;
    TextView bottomNumView;
    TextView unit1View;
    TextView unit2View;
    Button unit1Button;
    Button unit2Button;
    int unit1Index = 0;
    int unit2Index = 1;
    boolean hasDot = false;
    char hasOperator;
    DecimalFormat df = new DecimalFormat("#.####");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topNumView = findViewById(R.id.top_num);
        bottomNumView = findViewById(R.id.bottom_num);
        unit1View = findViewById(R.id.unit_1);
        unit2View = findViewById(R.id.unit_2);
        unit1View.setText(units[unit1Index]);
        unit2View.setText(units[unit2Index]);
        unit1Button = findViewById(R.id.button_unit1);
        unit2Button = findViewById(R.id.button_unit2);
        unit1Button.setText(units[unit1Index]);
        unit2Button.setText(units[unit2Index]);

        Log.d(TAG, "density: " + getResources().getDisplayMetrics().density);
        Log.d(TAG, String.valueOf(topNumView.getTextSize()));
        Log.d(TAG, String.valueOf(bottomNumView.getTextSize()));
        Log.d(TAG, String.valueOf(unit1View.getTextSize()));
        Log.d(TAG, String.valueOf(unit2View.getTextSize()));
        Log.d(TAG, String.valueOf(unit1Button.getTextSize()));
        Log.d(TAG, String.valueOf(unit2Button.getTextSize()));

        if(savedInstanceState != null){
            topNumView.setText(savedInstanceState.getString("topNumViewText"));
            bottomNumView.setText(savedInstanceState.getString("bottomNumViewText"));
            hasDot =  savedInstanceState.getBoolean("hasDot");
            hasOperator = savedInstanceState.getChar("hasOperator");
            unit1Index = savedInstanceState.getInt("unit1Index");
            unit2Index = savedInstanceState.getInt("unit2Index");
            unit1View.setText(units[unit1Index]);
            unit2View.setText(units[unit2Index]);
            unit1Button.setText(units[unit1Index]);
            unit2Button.setText(units[unit2Index]);
        }

        Log.d(TAG, "oncreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        metricsSetup();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("topNumViewText", topNumView.getText().toString());
        outState.putString("bottomNumViewText", bottomNumView.getText().toString());
        outState.putBoolean("hasDot", hasDot);
        outState.putChar("hasOperator", hasOperator);
        outState.putInt("unit1Index", unit1Index);
        outState.putInt("unit2Index", unit2Index);
    }

    private void metricsSetup(){
        DisplayMetrics displayMetrics= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels/numButtonsX;

        GridLayout grid = findViewById(R.id.buttons);
        Button v;
        for(int i=0; i<grid.getChildCount(); i++){
            v = (Button)grid.getChildAt(i);
            GridLayout.LayoutParams params = (GridLayout.LayoutParams) v.getLayoutParams();
            params.bottomMargin = params.topMargin = params.leftMargin = params.rightMargin = 0;
            params.width = width;
            v.setLayoutParams(params);
        }
    }

    public void putNumber(View view) {
        String num = ((TextView)view).getText().toString();
        String currentNum = bottomNumView.getText().toString();
        if(currentNum.equals("0")){
            bottomNumView.setText(num);
        }else{
            currentNum += num;
            bottomNumView.setText(currentNum);
        }
        convertNum();
    }

    public void putDot(View view) {
        if(!hasDot){
            String currentNum = bottomNumView.getText().toString() + ".";
            bottomNumView.setText(currentNum);
            hasDot = true;
        }
    }

    public void putOperator(View view){
        String currentNum = bottomNumView.getText().toString();
        if(hasOperator == '\u0000') {
            currentNum += ((Button)view).getText().toString();
            bottomNumView.setText(currentNum);
            hasOperator = currentNum.charAt(currentNum.length()-1);
        }
    }

    public void equals(View view){
        if(hasOperator != '\u0000'){
            bottomNumView.setText(simplifyNum(bottomNumView.getText().toString()));
            hasOperator = '\u0000';
        }
    }

    public void clearNum(View view) {
        topNumView.setText("0");
        bottomNumView.setText("0");
        hasDot = false;
        hasOperator = '\u0000';
    }

    public void switchUnit(View view) {
        int id = view.getId();
        if(id == R.id.button_unit1){
            unit1Index++;
            unit1Index = unit1Index >= units.length ? 0 : unit1Index;
            unit1Button.setText(units[unit1Index]);
            unit1View.setText(units[unit1Index]);
        }else{
            unit2Index++;
            unit2Index = unit2Index >= units.length ? 0 : unit2Index;
            unit2Button.setText(units[unit2Index]);
            unit2View.setText(units[unit2Index]);
        }
        convertNum();
    }

    private void convertNum() {
        String unit1 = unit1View.getText().toString();
        String unit2 = unit2View.getText().toString();
        String num1 = bottomNumView.getText().toString();
        String newNum = topNumView.getText().toString();
        String temp = hasOperator == '\u0000' ? num1 : simplifyNum(num1);

        switch(unit2){
            case "in":
                newNum = convertIn(Double.valueOf(temp), unit1);
                break;
            case "mm":
                newNum = convertMm(Double.valueOf(temp), unit1);
                break;
        }
        topNumView.setText(newNum);
    }

    private String simplifyNum(String expr){
        String[] nums = expr.split(String.valueOf(hasOperator));
        Double result = 0.0;

        switch(hasOperator){
            case '+':
                result = Double.parseDouble(nums[0]) + Double.parseDouble(nums[1]);
                break;
            case '-':
                result = Double.parseDouble(nums[0]) - Double.parseDouble(nums[1]);
                break;
            case 'x':
                result = Double.parseDouble(nums[0]) * Double.parseDouble(nums[1]);
                break;
            case '/':
                result = Double.parseDouble(nums[0]) / Double.parseDouble(nums[1]);
                break;
        }
        return String.valueOf(result);
    }

    private String convertIn(double num, String unit){
        Double result = num;
        switch(unit){
            case "mm":
                result = num*25.4;
                break;
            case "cm":
                result = num*2.54;
                break;
            case "ft":
                result = num/12;
                break;
        }
        return String.valueOf(df.format(result));
    }

    private String convertMm(double num, String unit){
        Double result = num;
        switch(unit){
            case "in":
                result = num/25.4;
                break;
            case "cm":
                result = num/10;
                break;
            case "ft":
                result = num/304.8;
                break;
        }
        return String.valueOf(df.format(result));
    }

    private String convertCm(double num, String unit){
        Double result = num;
        switch(unit){
            case "in":
                result = num/2.54;
                break;
            case "mm":
                result = num*10;
                break;
            case "ft":
                result = num/30.48;
                break;
        }
        return String.valueOf(df.format(result));
    }

    private String convertFt(double num, String unit){
        Double result = num;
        switch(unit){
            case "in":
                result = num*12;
                break;
            case "mm":
                result = num*304.8;
                break;
            case "cm":
                result = num*30.48;
        }
        return String.valueOf(df.format(result));
    }

}
