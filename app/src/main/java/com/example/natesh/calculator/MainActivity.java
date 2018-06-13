package com.example.natesh.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.util.Log ;
import android.widget.Toast;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

int getpreced(String c)
{
    switch(c){
        case "#" :
        case "(" : return 0 ;
        case "+" :
        case "-" : return 1 ;
        case "*" :
        case "x" :
        case "/" : return 2 ;
        case "^" :
        case "$" : return 3 ;
        default  : return -1 ;
    }
}



    String evaluatePostfix(String s)
    {
        double res = 0.00 ;
        Stack<Double> St = new Stack<Double>() ;

        for(int i= 0 ; i< s.length() ; i++)
        {
            if(Character.isDigit(s.charAt(i)))
                St.push((double)Character.digit(s.charAt(i) , 10 )) ;

            else{
                double b  = St.pop() ;
                double a  = St.pop() ;
                switch (s.charAt(i))
                {
                    case '+' : res = (a+b) ; break ;
                    case '-' : res = (a-b) ; break ;
                    case '*' : res = (a*b) ; break ;
                    case '/' : res = (a/b) ; break ;
                }

                St.push(res) ;
            }
        }

        return St.pop().toString() ;
    }




    String getPostfix(String s)
    {
//        TODO : check validity of input string and make it work for longer lenth numbers

        Stack<String> St = new Stack<String>()  ;
        St.push("#") ;

        String postfix  = new String() ;

        String temp =""  ;int  runlength = 1  ;
        String operand = "" ;


        for(int i =0 ; i<s.length() ; i+=runlength)
        {
            operand="" ;

            runlength = 1 ;

            char c =s.charAt(i) ;
            if(c==' ') continue ;


            if(Character.isDigit(c))
                operand+=c ;

            while(i+runlength < s.length() && operand.length()>0 && Character.isDigit(s.charAt(i+runlength)))
            {
                operand+=s.charAt(i+runlength) ;
                runlength++ ;
            }


            if(operand.length()>0)
            {
//                Its a number : handle here
                postfix+=("|" + operand + "|") ;
            }

            else{
//                Its an operator of bracket handle it here
                switch(c)
              {

                case '(' : St.push("(") ; break ;
                case ')' :  while((temp = St.pop())!="(")
                {
                    postfix+= St.pop() ;
                }

                break ;

                default:
                    while(getpreced(St.peek())>=getpreced(""+c))
                        postfix+=St.pop() ;

                    St.push(c+"") ;

              }
            }
        }


        while(St.peek()!="#")
            postfix+=St.pop() ;

        return postfix ;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String postfix = getPostfix("8+7*1+2-2*8") ;
//        String result = evaluatePostfix(postfix) ;

        Log.d("Postfix" , postfix) ;
//        Log.d("Postfix" , "Result is : " + result) ;



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText editTextRes = (EditText) findViewById(R.id.editTextres) ;

        editTextRes.setEnabled(false);

        TableLayout layouttable = (TableLayout)findViewById(R.id.tableLayout) ;

        final Button btDEL = findViewById(R.id.btnDEL) ;

        final Button btEqual = findViewById(R.id.btnEqual) ;
        btEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d("Postfix" , getPostfix(btEqual.getText().toString())) ;

            }
        });



        btDEL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = editTextRes.getText().toString() ;
                if(temp.length()>0)
                    editTextRes.setText(temp.substring(0 , temp.length()-1));

            }
        });





        for(int i =0 ; i< layouttable.getChildCount() ; i++ )
        {
            View v =  layouttable.getChildAt(i) ;

            if(v.getClass()==TableRow.class )
            {

                for(int j =0 ; j < ((TableRow)v).getChildCount() ; j++ )
                {

                    final Button button = (Button)((TableRow)v).getChildAt(j) ;
                    Log.d("Button : " , button.getText().toString()) ;



                    if(button.getText().toString().equals("DEL"))
                        continue;



                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Log.d( "Button Press : "  , button.getText()+ " pressed ." ) ;
                            editTextRes.append(button.getText()) ;

                            Toast.makeText(MainActivity.this, ((Button)view).getText(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
    }
}




class utility extends AppCompatActivity{

};
