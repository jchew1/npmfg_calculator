package com.example.jonathan.npmfg_calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ABC";
    private static final int numButtonsX = 4;
    private static final int numButtonsY = 4;
    private static final String[] BUTTON_LABELS = new String[]{"7", "8", "9", "in", "4", "5", "6", "", "1", "2", "3", "mm", "0", ",", "C", ""};
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
        int width = displayMetrics.widthPixels/4;
        GridLayout grid = findViewById(R.id.buttons);
        Log.d(TAG, String.valueOf(grid.getUseDefaultMargins()));
        Button v = null;
        for(int i=0; i<grid.getChildCount(); i++){
            v = (Button)grid.getChildAt(i);
            v.setWidth(width);
            GridLayout.LayoutParams params = (GridLayout.LayoutParams) v.getLayoutParams();
            params.bottomMargin = params.topMargin = params.leftMargin = params.rightMargin = 0;
            v.setLayoutParams(params);
            Log.d(TAG, "leftmargin: " + ((GridLayout.LayoutParams) v.getLayoutParams()).leftMargin);
        }
//        Log.d(TAG, "metricssetup " + String.valueOf(width));
////        <Button
////        android:id="@+id/button0"
////        android:text="@string/zero"
////        android:onClick="putNumber"
////        android:layout_row="3"
////        android:layout_column="0"/>
//        for(int i=0; i<numButtonsY; i++){
//            for(int j=0; j<numButtonsX; j++){
//                Log.d(TAG, "metricssetup: " + BUTTON_LABELS[i*numButtonsX + j]);
//                Button b = new Button(this);
//                GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(j), GridLayout.spec(i));
//                params.bottomMargin = params.topMargin = params.leftMargin = params.rightMargin = 0;
//                b.setId(this.getResources().getIdentifier("button"+BUTTON_LABELS[i*numButtonsX + j], "string", this.getPackageName()));
//                b.setLayoutParams(params);
//                b.setWidth(width);
//                b.setText(BUTTON_LABELS[i*numButtonsX + j]);
//                grid.addView(b);
//            }
//        }
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
