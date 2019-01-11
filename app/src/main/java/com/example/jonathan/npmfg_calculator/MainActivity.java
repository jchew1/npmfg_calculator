package com.example.jonathan.npmfg_calculator;

import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ABC";
    private static final int numButtonsX = 4;
    private static final int numButtonsY = 5;
    private static final float gridHeightPerc = 0.5f;
    private static final String[] units = new String[]{"in", "mm", "cm", "ft"};
    TextView topNumView;
    TextView middleNumView;
    TextView bottomNumView;
    TextView operatorView;
    TextView unit1View;
    TextView unit2View;
    Button unit1Button;
    Button unit2Button;
    int unit1Index = 0;
    int unit2Index = 1;
    boolean hasDot = false;
    char hasOperator;
    private static final float minTextSizeSp = 36.0f;
    float minTextSizePx;
    DecimalFormat df = new DecimalFormat("#.####");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topNumView = findViewById(R.id.top_num);
        middleNumView = findViewById(R.id.middle_num);
        bottomNumView = findViewById(R.id.bottom_num);
        operatorView = findViewById(R.id.operator);
        unit1View = findViewById(R.id.unit_1);
        unit2View = findViewById(R.id.unit_2);
        unit1View.setText(units[unit1Index]);
        unit2View.setText(units[unit2Index]);
        unit1Button = findViewById(R.id.button_unit1);
        unit2Button = findViewById(R.id.button_unit2);
        unit1Button.setText(units[unit1Index]);
        unit2Button.setText(units[unit2Index]);

        if(savedInstanceState != null){
            topNumView.setText(savedInstanceState.getString("topNumViewText"));
            middleNumView.setText(savedInstanceState.getString("middleNumViewText"));
            bottomNumView.setText(savedInstanceState.getString("bottomNumViewText"));
            hasDot =  savedInstanceState.getBoolean("hasDot");
            hasOperator = savedInstanceState.getChar("hasOperator");
            operatorView.setText(String.valueOf(hasOperator));
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
        outState.putString("middleNumViewText", middleNumView.getText().toString());
        outState.putString("bottomNumViewText", bottomNumView.getText().toString());
        outState.putBoolean("hasDot", hasDot);
        outState.putChar("hasOperator", hasOperator);
        outState.putInt("unit1Index", unit1Index);
        outState.putInt("unit2Index", unit2Index);
    }

    private void metricsSetup(){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels/numButtonsX;
        int height = displayMetrics.heightPixels - getStatusBarHeight();
        int numberHeight = (int)(height*(1-gridHeightPerc)/2);
        int buttonHeight = (int)(height*gridHeightPerc/numButtonsY);

        topNumView.getLayoutParams().height = middleNumView.getLayoutParams().height = operatorView.getLayoutParams().height = numberHeight/2;
        bottomNumView.getLayoutParams().height = numberHeight;

        GridLayout grid = findViewById(R.id.buttons);
        Button v;
        for(int i=0; i<grid.getChildCount(); i++){
            v = (Button)grid.getChildAt(i);
            GridLayout.LayoutParams params = (GridLayout.LayoutParams) v.getLayoutParams();
            params.bottomMargin = params.topMargin = params.leftMargin = params.rightMargin = 0;
            params.width = width;
            params.height = buttonHeight;
            v.setLayoutParams(params);
        }

        textSizeSetup();
    }
    private void textSizeSetup(){
        minTextSizePx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, minTextSizeSp, getResources().getDisplayMetrics());

        final String testText = "0";
        Paint paint = new Paint();
        Rect bounds = new Rect();
        paint.setTypeface(topNumView.getTypeface());
        paint.setTextSize(topNumView.getTextSize());
        paint.getTextBounds(testText, 0, testText.length(), bounds);
        Log.d(TAG, "initial textHeight: " + bounds.height() + " inital viewHeight: " + topNumView.getLayoutParams().height);

        if(bounds.height() > topNumView.getLayoutParams().height){
            Log.d(TAG, "textSize greater than view");
            while(bounds.height() > topNumView.getLayoutParams().height){
                topNumView.setTextSize(TypedValue.COMPLEX_UNIT_PX, topNumView.getTextSize()-1);
                paint.setTextSize(topNumView.getTextSize());
                paint.getTextBounds(testText, 0, testText.length(), bounds);
                Log.d(TAG, "text height: " + bounds.height() + " view height: " + topNumView.getLayoutParams().height);
            }
        }else if(bounds.height() < topNumView.getLayoutParams().height){
            Log.d(TAG, "textSize smaller than view");
            while(bounds.height() < topNumView.getLayoutParams().height){
                topNumView.setTextSize(TypedValue.COMPLEX_UNIT_PX, topNumView.getTextSize()+1);
                paint.setTextSize(topNumView.getTextSize());
                paint.getTextBounds(testText, 0, testText.length(), bounds);
                Log.d(TAG, "text size: " + topNumView.getTextSize() + " text height: " + bounds.height() + " view height: " + topNumView.getLayoutParams().height);
            }
        }else{
            Log.d(TAG, "textSize same as view");
        }
        middleNumView.setTextSize(TypedValue.COMPLEX_UNIT_PX, topNumView.getTextSize());
        Log.d(TAG, "textSizeSetup finished: topNumView: " + topNumView.getTextSize() + " middleNumView: " + middleNumView.getTextSize());
    }

    private String fitNumber(TextView view, String num){
        Paint paint = new Paint();
        Rect bounds = new Rect();
        paint.setTypeface(view.getTypeface());
        paint.setTextSize(view.getTextSize());
        paint.getTextBounds(num, 0, num.length(), bounds);
        /*
        if text width > view width
            shrink text size
            scientific notation if number too big
         */
        Log.d(TAG, "fitNumber view width: " + String.valueOf(view.getWidth()));
        Log.d(TAG, "fitNumber number width: " + String.valueOf(bounds.width()) + " number height: " + String.valueOf(bounds.height()));
        Log.d(TAG, "fitNumber minTextSize: " + String.valueOf(minTextSizePx));
        Log.d(TAG, "fitNumber textSize: " + String.valueOf(view.getTextSize()));
//        if()
        return num;
    }

    private int getStatusBarHeight(){
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if(resourceId > 0){
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void putNumber(View view) {
        String num = ((TextView)view).getText().toString();
        String currentNum = middleNumView.getText().toString();

        if(currentNum.equals("0")){
            middleNumView.setText(num);
        }else{
            currentNum += num;
            middleNumView.setText(currentNum);
        }
        Log.d(TAG, "putNumber: " + currentNum);
        convertNum();
    }

    public void putDot(View view) {
        if(!hasDot){
            String currentNum = topNumView.getText().toString() + ".";
            topNumView.setText(currentNum);
            hasDot = true;
        }
    }

    public void putOperator(View view){
        if(hasOperator == '\u0000') {
            String op = ((Button)view).getText().toString();
            operatorView.setText(op);
            hasOperator = op.charAt(0);
            topNumView.setText(middleNumView.getText());
            middleNumView.setText("0");
            Log.d(TAG, "hasOperator: " + String.valueOf(hasOperator));
        }
    }

    public void equals(View view){
        if(hasOperator != '\u0000'){
            middleNumView.setText(String.valueOf(simplifyNum()));
            topNumView.setText("");
            operatorView.setText("");
            hasOperator = '\u0000';
        }
    }

    public void clearNum(View view) {
        topNumView.setText("");
        middleNumView.setText("0");
        bottomNumView.setText("0");
        operatorView.setText("");
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
        Double newNum = Double.parseDouble(bottomNumView.getText().toString());
        Double temp = (hasOperator == '\u0000') ? Double.parseDouble(middleNumView.getText().toString()) : simplifyNum();

        switch(unit1){
            case "in":
                newNum = convertIn(temp, unit2);
                break;
            case "mm":
                newNum = convertMm(temp, unit2);
                break;
            case "cm":
                newNum = convertCm(temp, unit2);
                break;
            case "ft":
                newNum = convertFt(temp, unit2);
                break;
        }
        Log.d(TAG, "convertNum newNum: " + newNum);
        bottomNumView.setText(String.valueOf(newNum));
        Log.d(TAG, "convertNum: " + bottomNumView.getText().toString());
        fitNumber(bottomNumView, String.valueOf(newNum));
    }

    private Double simplifyNum(){
        String topNum = topNumView.getText().toString();
        String middleNum = middleNumView.getText().toString();
        Double result = 0.0;

        switch(hasOperator){
            case '+':
                result = Double.parseDouble(topNum) + Double.parseDouble(middleNum);
                break;
            case '-':
                result = Double.parseDouble(topNum) - Double.parseDouble(middleNum);
                break;
            case 'x':
                result = Double.parseDouble(topNum) * Double.parseDouble(middleNum);
                break;
            case '/':
                result = Double.parseDouble(topNum) / Double.parseDouble(middleNum);
                break;
        }
        Log.d(TAG, String.valueOf(result));
        return result;
    }



    private Double convertIn(double num, String unit){
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
        return result;
    }

    private Double convertMm(double num, String unit){
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
        return result;
    }

    private Double convertCm(double num, String unit){
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
        return result;
    }

    private Double convertFt(double num, String unit){
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
        return result;
    }

}
