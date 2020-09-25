package com.example.calculator_secret;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TextView textViewInput;
    private String stringInput="";
    private TextView textViewOutput;
    private String stringOutput="0";
    private HorizontalScrollView horizontalScrollViewInput;

    private long timeUpButtonEqually;
    private long timePressButtonEqually;
    private boolean isReadyToSecret=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewInput=findViewById(R.id.TextView_input);
        horizontalScrollViewInput=findViewById(R.id.HorizontalScrollView_input);

        textViewOutput=findViewById(R.id.TextView_output);
        final Button button_equally=findViewById(R.id.Button_equally);
        button_equally.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(v==button_equally){
                    if(event.getAction()==MotionEvent.ACTION_DOWN){
                        timePressButtonEqually= new Date().getTime();
//                        textViewInput.setText(String.valueOf(timePressButtonEqually));
                    }

                    if(event.getAction()==MotionEvent.ACTION_UP){
                        timeUpButtonEqually=new Date().getTime();
                        timePressButtonEqually=timeUpButtonEqually-timePressButtonEqually;
                        if(timePressButtonEqually>4000){
                            isReadyToSecret=true;
                        }
//                        Toast.makeText(getApplicationContext(), String.valueOf(timePressButtonEqually), Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });

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

    private ArrayList<String> getPRN(String input){
        ArrayList<String> result = new ArrayList<>();
        ArrayList<Character> stack=new ArrayList<>();
        String tmp="";
        for (char c : stringInput.toCharArray()){
            if (c=='+'||c=='-'||c=='*'||c=='/'||c=='('||c==')'){
                if(tmp.length()>0){
                    result.add(tmp);
                    tmp="";
                }
                Boolean is_close_bracket=false;
                for (int i=stack.size()-1; i>=0; --i){
                    if(is_close_bracket||c==')'){
                        Character sc=stack.get(i);
                        if(sc=='('){
                            stack.remove(i);
                            break;
                        }
                        is_close_bracket=true;
                        result.add(sc.toString());
                        stack.remove(i);
                        continue;
                    }
                    if(c=='+'||c=='-'){
                        Character sc=stack.get(i);
                        if(sc=='+'||sc=='-'||sc=='*'||sc=='/'){
                            result.add(sc.toString());
                            stack.remove(i);
                            continue;
                        }
                    }
                    if(c=='*'||c=='/'){
                        Character sc=stack.get(i);
                        if(sc=='*'||sc=='/'){
                            result.add(sc.toString());
                            stack.remove(i);
                            continue;
                        }
                    }
                    break;
                }
                if(c!=')'){
                    stack.add(c);
                }
                continue;
            }
            tmp+=c;//число
        }
        if(tmp.length()>0){
            result.add(tmp);
        }
        for (int i=stack.size()-1; i>=0; --i){
            Character sc=stack.get(i);
            result.add(sc.toString());
            stack.remove(i);
        }

        return result;
    }


    private Object getAnswerPRN(ArrayList<String> PRN){
        Boolean is_find=false;
        while (PRN.size()>1){
            is_find=false;
            for (int i=2; i<PRN.size(); ++i){
                char c=PRN.get(i).toCharArray()[0];
                if(PRN.get(i).length()==1&&(c=='+'||c=='-'||c=='*'||c=='/')){
                    double val2;
                    double val1;
                    try {
                        val1=Double.parseDouble(PRN.get(i-2));
                        val2=Double.parseDouble(PRN.get(i-1));
//                        val2=Double.parseDouble("loh");
                    }catch (Exception e){
                        break;
                    }
                    is_find=true;
                    if(c=='+'){
                        val1+=val2;
                    }else if(c=='-'){
                        val1-=val2;
                    }else if(c=='*'){
                        val1*=val2;
                    }else if(c=='/'){
                        val1/=val2;
                    }
                    PRN.set(i, String.valueOf(val1));
                    PRN.remove(i-1);
                    PRN.remove(i-2);
                    break;
                }
            }
            if(!is_find){
                break;
            }
        }
        if(!is_find){
            return null;
        }

        return Double.parseDouble(PRN.get(0));

    }

    public void onClick_equally(View view) {

        ArrayList<String> PRN=getPRN(stringInput);
        Object answer=getAnswerPRN(PRN);
        if(answer==null){
            stringOutput="error";
        }else {
            stringOutput= String.valueOf((double)answer);
        }

        stringInput="";
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
        if(isReadyToSecret){
            isReadyToSecret=false;
            if(new Date().getTime()-timeUpButtonEqually<5000){
                int length=stringInput.length();
                if(length>=3){
                    String tmp = stringInput.substring(length-3, length);
                    if(tmp.equals("123")){
                        Intent intent=new Intent(this, SecretActivity.class);
                        startActivity(intent);
//                        Toast.makeText(this, "ogo", Toast.LENGTH_LONG).show();
                    }
                }

            }
        }
    }

    public void onClick_C(View view) {
        stringInput="";
        stringOutput="0";
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