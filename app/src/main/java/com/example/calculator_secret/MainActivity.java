package com.example.calculator_secret;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textViewInput;
    private String stringInput="";
    private TextView textViewOutput;
    private String stringOutput="";
    private HorizontalScrollView horizontalScrollViewInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewInput=findViewById(R.id.TextView_input);
        horizontalScrollViewInput=findViewById(R.id.HorizontalScrollView_input);

        textViewOutput=findViewById(R.id.TextView_output);
    }

    private void autoScrollInput(){
        ViewTreeObserver vto = horizontalScrollViewInput.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
            @Override
            public void onGlobalLayout() {
                horizontalScrollViewInput.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                horizontalScrollViewInput.scrollTo(textViewInput.getWidth(), 0);
            }
        });
    }

    private void onClickButtonNumeral(String value){
        stringInput+=value;
        textViewInput.setText(stringInput);
        autoScrollInput();
    }

    private void onClickButtonSign(String value){
        if(stringInput.length()==0){
            stringInput+=stringOutput;
        }
        onClickButtonNumeral(value);
    }

    public void onClick_plus(View view) {
        onClickButtonSign("+");
    }

    public void onClick_minus(View view) {
        onClickButtonSign("-");
    }

    public void onClick_multiply(View view) {
        onClickButtonSign("*");
    }

    public void onClick_divide(View view) {
        onClickButtonSign("/");
    }

    public void onClick_equally(View view) {
        stringInput="";
        stringOutput="557";
        textViewInput.setText(stringInput);
        textViewOutput.setText(stringOutput);
    }

    public void onClick_open_bracket(View view) {
        onClickButtonNumeral("(");
    }

    public void onClick_close_bracket(View view) {
        onClickButtonNumeral(")");
    }

    public void onClick_del(View view) {
        if(stringInput.length()>0){
            stringInput = stringInput.substring(0, stringInput.length() - 1);
        }
        textViewInput.setText(stringInput);
    }

    public void onClick_7(View view) {
        onClickButtonNumeral("7");
    }

    public void onClick_8(View view) {
        onClickButtonNumeral("8");
    }

    public void onClick_9(View view) {
        onClickButtonNumeral("9");
    }

    public void onClick_4(View view) {
        onClickButtonNumeral("4");
    }

    public void onClick_5(View view) {
        onClickButtonNumeral("5");
    }

    public void onClick_6(View view) {
        onClickButtonNumeral("6");
    }

    public void onClick_1(View view) {
        onClickButtonNumeral("1");
    }

    public void onClick_2(View view) {
        onClickButtonNumeral("2");
    }

    public void onClick_3(View view) {
        onClickButtonNumeral("3");
    }

    public void onClick_C(View view) {
        stringInput="";
        stringOutput="";
        textViewInput.setText(stringInput);
        textViewOutput.setText(stringOutput);
    }

    public void onClick_0(View view) {
        onClickButtonNumeral("0");
    }

    public void onClick_dot(View view) {
        onClickButtonNumeral(".");
    }
}