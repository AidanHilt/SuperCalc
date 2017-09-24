package com.example.aidanthegreat.supercalc;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;

public class MainCalculatorv2 extends AppCompatActivity {

    private static String[] operatorsList = {"+", "-", "/", "*", "sin", "cos", "tan", "(", ")"};

    private static EditText numbersView;
    private static Button exponentButton;
    private static Button decimalButton;

    //Array that stores the contents of the TextView
    private static ArrayList<String> calculationContents = new ArrayList<String>();

    private static boolean superScript = false;
    private static boolean instantDelete = true;
    private static boolean subScript = false;

    //Method to build a string from an ArrayList
    private static String buildString(ArrayList<String> a){
        String string = "";

        for(String s: a){
            string += s;
        }

        return string;
    }

    //Builds an ArrayList out of a string, for the purpose of performing mathetmatical operations.
    //As such, it separates at operators, so that all items are spaced out for a math algorithim
    private static ArrayList<String> buildOperationList(String s){
        String copyString = s;
        //Replaces the operators with properly spaced versions, so that the string can be split along spaces
        for(String s1: operatorsList){
            copyString = copyString.replace(s1, " "+ s1 + " ");
        }

        copyString = copyString.replace(">", "> ");
        copyString = copyString.replace("</", " </");
        copyString = copyString.replace("<s", " <s");

        String[] stringList = copyString.split(" ");

        return new ArrayList<String>(Arrays.asList(stringList));
    }

    //Builds an ArrayList out of a string, for the purpose of character-by-character modification.
    //As such, it splits along every character
    private static ArrayList<String> buildModifierList(String s){
        String[] modified = s.split("");
        return new ArrayList<String>(Arrays.asList(modified));
    }

    //Method to add text to the view
    private void addTextInView(String addedString){
        instantDelete = false;

        if(numbersView.getText().equals(getString(R.string.defaultMessage)) || numbersView.getText().equals("")){
            numbersView.setText(addedString);
        }else{
            ArrayList<String> modifiedList = buildModifierList(numbersView.getText().toString());
            if(modifiedList.size() != 0) {
                modifiedList.add(numbersView.getSelectionStart() + 1, addedString);
            }else{
                modifiedList.add(addedString);
                numbersView.setSelection(2);
            }

            String finalAdded = buildString(modifiedList);

            numbersView.setText(Html.fromHtml(finalAdded));
            numbersView.setSelection(finalAdded.length() - 1);
        }

    }

    //Method to delete an item from the view
    private void backspace(){
        String finalModified = "";

        int finalPosition = numbersView.getSelectionStart();
        if(finalPosition <= 0){
            finalPosition = 1;
        }

        if(!instantDelete){
            ArrayList<String> modifiedList = buildModifierList(numbersView.getText().toString());
            modifiedList.remove(numbersView.getSelectionStart());
            finalModified = buildString(modifiedList);
            numbersView.setSelection(finalPosition);
            System.out.println(numbersView.getSelectionStart());
        }

        numbersView.setText(Html.fromHtml(finalModified));
    }


    //Method to update the text being displayed in the TextView, in order to sync it with the contents of
    //calculationContents
    private void updateTextView(){
        numbersView.setText(Html.fromHtml(buildString(calculationContents)));
    }

    //-------------------Number button click methods----------------------
    public void oneClick(View v){
        addTextInView(getString(R.string.one));
    }
    public void twoClick(View v){
        addTextInView(getString(R.string.two));
    }
    public void threeClick(View v){
        addTextInView(getString(R.string.three));
    }
    public void fourClick(View v){
        addTextInView(getString(R.string.four));
    }
    public void fiveClick(View v){
        addTextInView(getString(R.string.five));
    }
    public void sixClick(View v){
        addTextInView(getString(R.string.six));
    }
    public void sevenClick(View v){
        addTextInView(getString(R.string.seven));
    }
    public void eightClick(View v){
        addTextInView(getString(R.string.eight));
    }
    public void nineClick(View v){
        addTextInView(getString(R.string.nine));
    }
    public void zeroClick(View v){
        addTextInView(getString(R.string.zero));
    }

    //-----------------Operations and other items click methods----------
    public void addClick(View v){
        addTextInView(getString(R.string.add));
    }
    public void subClick(View v){
        addTextInView(getString(R.string.sub));
    }
    public void multClick(View v){
        addTextInView(getString(R.string.mul));
    }
    public void divClick(View v){
        addTextInView(getString(R.string.div));
    }

    public void sinClick(View v){
        addTextInView(getString(R.string.sin));
    }
    public void cosClick(View v){
        addTextInView(getString(R.string.cos));
    }
    public void tanClick(View v){
        addTextInView(getString(R.string.tan));
    }

    public void leftBracketClick(View v){
        addTextInView(getString(R.string.left));
    }
    public void rightBracketClick(View v){
        addTextInView(getString(R.string.right));
    }

    public void delClick(View v){
        backspace();
    }

    public void decimalClick(View v){
        addTextInView(".");
    }

    public void exponentClick(View v){
        addTextInView("<sup>");
        addTextInView("</sup>");
        superScript=true;
    }

    public void equalsClick(View v){
        if(superScript){
            superScript = false;
        }else if(subScript){
            subScript = false;
        }
    }

    public void sqrtClick(View v){
        addTextInView(getString(R.string.sqrt));
        addTextInView("<sub>");
        addTextInView("</sub>");
        subScript = true;
    }

    //-----------Convenience methods------------
    private int calcContentLast(){
        return calculationContents.size() -1;
    }

    //Determine if a string is an operator or a number
    //TODO Add determination for if a string is a trig function
    private boolean isOperator(String value){
        if(value.equals("+") || value.equals("/") || value.equals("-") || value.equals("*")
                || value.equals("sin") || value.equals("cos") || value.equals("tan")
                || value.equals("(") || value.equals(")") || value.equals("<sup>")
                || value.equals("</sup>") || value.equals("<sub>") || value.equals("</sub>")){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_calculatorv2);

        //Programmatic representation of the TextView
        numbersView = (EditText)(findViewById(R.id.numbersView));


        //Attempts to keep the keyboard from launching when the EditText is clicked
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            numbersView.setShowSoftInputOnFocus(false);
        } else {
            numbersView.setTextIsSelectable(true);
            //N.B. Accepting the case when non editable text will be selectable
        }



        //Programmatic representation of the exponent button
        exponentButton = (Button)(findViewById(R.id.exponentButton));
        exponentButton.setText(Html.fromHtml("x<sup>y</sup>"));

        //Programmatic representation of the decimal button
        decimalButton = (Button)(findViewById(R.id.decimalButton));
        decimalButton.setText(Html.fromHtml("<sup>.</sup>"));

        //Set the text view to have the default message
        calculationContents.add(getString(R.string.defaultMessage));

        updateTextView();

    }

}
