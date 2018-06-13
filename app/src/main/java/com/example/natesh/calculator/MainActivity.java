package com.example.natesh.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.ChangeTransform;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.util.Log ;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    Utility utilobj = new Utility() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView editTextRes = (TextView) findViewById(R.id.editTextres) ;

        TableLayout layouttable = (TableLayout)findViewById(R.id.tableLayout) ;
        final Button btDEL = findViewById(R.id.btnDEL) ;
        final Button btEqual = findViewById(R.id.btnEqual) ;


        btDEL.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                editTextRes.setText("");
                return false ;
            }
        });

        btEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editTextRes.getText().toString().isEmpty())
                    return ;

                String postfix  = "" ;
                String result  = ""  ;

                try{

                postfix = utilobj.getPostfix(editTextRes.getText().toString()) ;
                Log.d("Postfix " , postfix) ;

                result = utilobj.evaluatePostfix(postfix) ;
                Log.d("Result" , result) ;

                }
                catch(Exception e){
                    Log.e("TestError" , e.toString()) ;
                    editTextRes.setText("Invalid Expression !");
                    return ;
                }


                if(result.charAt(result.length()-1)=='0' && result.charAt(result.length()-2)=='.')
                    result = result.substring(0 , result.length()-2) ;

                Log.d("Postfix" , postfix) ;
                Log.d("Postfix" , "Result is : " + result) ;
                editTextRes.setText(result);

                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
           }
        });



        btDEL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = editTextRes.getText().toString() ;
                if(temp.toString()=="0")
                    temp = "" ;

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
                            if(editTextRes.getText().toString()=="0")
                                editTextRes.setText("");

                            Log.d( "Button Press : "  , button.getText()+ " pressed ." ) ;
                            editTextRes.append(button.getText()) ;

                        }
                    });
                }
            }
        }
    }
}






class Utility extends AppCompatActivity{

    boolean gotResult_flag = false ;
    String result = new String() ;


    public boolean isGotResult_flag() {
        return gotResult_flag;
    }

    public void setGotResult_flag(boolean gotResult_flag) {
        this.gotResult_flag = gotResult_flag;
    }


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
        String operand = "" ;


        for(int i= 0 ; i< s.length() ;)
        {
            if(s.charAt(i)=='|')
            {
                operand = s.substring(i+1 , s.indexOf('|' , i+1) ) ;
                St.push(Double.parseDouble(operand)) ;
                i+=operand.length()+2 ; continue;
            }


            else{
                double b  = St.pop() ;
                double a  = St.pop() ;
                switch (s.charAt(i))
                {
                    case '+' : res = (a+b) ; break ;
                    case '-' : res = (a-b) ; break ;
                    case '*' : res = (a*b) ; break ;
                    case 'x' : res = (a*b) ; break ;
                    case '/' : res = (a/b) ; break ;
                }

                St.push(res) ;
            }
            i++ ;
        }

        this.result  = St.peek().toString() ;
        return St.pop().toString() ;
    }




    String getPostfix(String s)
    {
//        TODO : check validity of input string

        Stack<String> St = new Stack<String>()  ;
        St.push("#") ;

        if(s.indexOf('E')>0)
        {

        }

        String postfix  = new String() ;

        String temp =""  ;int  runlength = 1 ;
        String operand = "" ;

        for(int i =0 ; i<s.length() ; i+=runlength)
        {
            operand="" ; runlength = 1 ;

            char c =s.charAt(i) ;
            if(c==' ') continue ;

            if(Character.isDigit(c))
                operand+=c ;

            else if ((c=='-' && (i==0 || !Character.isDigit(s.charAt(i-1)))))
            {
                operand+= Character.toString(c)+s.charAt(i+1) ;
                i+=1 ;
            }

            while(i+runlength<s.length() && (Character.isDigit(c) || c=='.' || c=='E') && (Character.isDigit(s.charAt(i+runlength)) || s.charAt(i+runlength)=='.' || s.charAt(i+runlength)=='E'))
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
//                Its an operator or bracket handle it here
                switch(c)
                {

                    case '(' : St.push("(") ; break ;
                    case ')' :  while((temp = St.pop())!="(")
                    {
                        postfix+= St.pop() ;
                    }

                        break ;

                    default:
                        Log.d("Debug" , St.peek()) ;

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

} ;
