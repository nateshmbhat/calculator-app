package com.example.natesh.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.util.Log ;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {


    int getpreced(char c)
    {
        switch(c){
            case '#' :
            case '(' : return 0 ;
            case '+' :
            case '-' : return 1 ;
            case '*' :
            case '/' : return 2 ;
            case '^' :
            case '$' : return 3 ;

            default : return -1 ;
        }
    }


    String getPostfix(String s)
    {

        Stack<Character> St = new Stack<Character>()  ;
        String postfix  = new String() ;

        char temp ;

        for(int i =0 ; i<s.length() ; i++)
        {
            char c = s.charAt(i) ;
            if(Character.isDigit(c))
                postfix+=c ;
            else{
                switch(c)
              {
                case '(' : St.push('(') ; break ;
                case ')' :  while((temp = St.pop())!='(')
                {
                    postfix+= St.pop() ;
                }
                break ;

                default:
                    while(getpreced(St.peek())>=getpreced(c))
                        postfix+=St.pop() ;
              }
            }
        }

        while(St.isEmpty()==false)
            postfix+=St.pop() ;

        return postfix ;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText editTextRes = (EditText) findViewById(R.id.editTextres) ;

        editTextRes.setEnabled(false);

        TableLayout layouttable = (TableLayout)findViewById(R.id.tableLayout) ;

//        Button bt1 = findViewById(R.id.btn1) ;
//        Button bt2 = findViewById(R.id.btn2) ;
//        Button bt3 = findViewById(R.id.btn3) ;
//        Button bt4 = findViewById(R.id.btn4) ;
//        Button bt5 = findViewById(R.id.btn5) ;
//        Button bt6 = findViewById(R.id.btn6) ;
//        Button bt7 = findViewById(R.id.btn7) ;
//        Button bt8 = findViewById(R.id.btn8) ;
//        Button bt9 = findViewById(R.id.btn9) ;
//        Button bt0 = findViewById(R.id.btn0) ;
//        Button btPlus = findViewById(R.id.btnAdd) ;
//        Button btDiv = findViewById(R.id.btnDivide) ;
//        Button btMul = findViewById(R.id.btnMultiply) ;
        Button btDEL = findViewById(R.id.btnDEL) ;


        final Button btEqual = findViewById(R.id.btnEqual) ;
        btEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Button Equals : " , btEqual.getText().toString()) ;
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

                    Log.d("Table Row : " ,  "Inside table with id" + v.toString()) ;
                    final Button button = (Button)((TableRow)v).getChildAt(j) ;
                    Log.d("Button : " , button.getText().toString()) ;

                    if(button.getText().toString().equals("DEL"))
                        continue;

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Log.d( "Button Press : "  , button.getText()+ " pressed ." ) ;
                            editTextRes.append(button.getText()) ;

                        }
                    });
                }
            }
        }
    }
}




class utility extends AppCompatActivity{

};
