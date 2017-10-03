package com.example.aidanthegreat.supercalc;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;

public class MainCalculatorv2 extends AppCompatActivity {

    private static String[] operatorsList = {"+", "-", "/", "*", "sin", "cos", "tan", "(", ")"};
    private static ArrayList<int[]> superscriptArray = new ArrayList<>();

    private static EditText numbersView;
    private static Button exponentButton;
    private static Button decimalButton;

    //Array that stores the contents of the TextView
    private static ArrayList<String> calculationContents = new ArrayList<String>();

    private static boolean superScript = false;
    private static boolean instantDelete = true;
    private static boolean subScript = false;

    //Storage for numberView contents, necessary because it does not preserve HTML tags when they are added to the view
    private static String textViewContents = "";

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
            copyString = copyString.replace(s1, " " + s1 + " ");
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
        int finalPosition = numbersView.getSelectionStart() + 1;

        ArrayList<String> modifier = buildModifierList(numbersView.getText().toString());

        if(inSuperscript(numbersView.getSelectionStart())){
            modifier.add(numbersView.getSelectionStart() + 1, addedString);
            int newIndex = getRange(numbersView.getSelectionStart());
            superscriptArray.get(newIndex)[1] = superscriptArray.get(newIndex)[1] + 1;
        }else{
            modifier.add(numbersView.getSelectionStart() + 1, addedString);
        }

        updateNumbersView(buildString(modifier));
        if(numbersView.getText().toString().length() > 0){
            numbersView.setSelection(finalPosition, finalPosition);
        }


        }

    //Method to delete an item from the view
    private void backspace(){
        String finalModified = "";
        int finalPosition =  numbersView.getSelectionStart() - 1; //numbersView.getSelectionStart();

        if(finalPosition < 0){
            finalPosition = 0;
        }

        if(!instantDelete && !inSuperscript(numbersView.getSelectionStart())){
            ArrayList<String> modifiedList = buildModifierList(numbersView.getText().toString());
            modifiedList.remove(numbersView.getSelectionStart());
            finalModified = buildString(modifiedList);
        }else if(inSuperscript(numbersView.getSelectionStart())){
            ArrayList<String> modifiedList = buildModifierList(numbersView.getText().toString());
            modifiedList.remove(numbersView.getSelectionStart());
            finalModified = buildString(modifiedList);
            superscriptArray.get(getRange(numbersView.getSelectionStart()))[1] -= 1;
            clearInvalidSpans();
        }

        updateNumbersView(finalModified);
        if(numbersView.getText().toString().length() > 0){
            numbersView.setSelection(finalPosition, finalPosition);
        }

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
        int[] arrayListsAreAssholes = {numbersView.getSelectionStart(), numbersView.getSelectionStart()};
        superscriptArray.add(arrayListsAreAssholes);
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
    private boolean inSuperscript(int index){
        boolean returnValue = false;
        for(int[] item :superscriptArray){
            if(index >= item[0] && index <=item[1]){
                returnValue = true;
                break;
            }
        }
        return returnValue;
    }

    private void updateNumbersView(String addedValue){
        SpannableStringBuilder addText = new SpannableStringBuilder(addedValue);
        for(int[] item: superscriptArray){
            addText.setSpan(new SuperscriptSpan(), item[0], item[1], Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            addText.setSpan(new RelativeSizeSpan(0.75f), item[0], item[1], Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
        numbersView.setText(addText);
    }

    private int getRange(int index){
        int returnValue = 0;
        for(int i = 0; i < superscriptArray.size(); i ++){
            if(index >= superscriptArray.get(i)[0] && index <= superscriptArray.get(i)[1]){
                returnValue = i;
                break;
            }

            if(superscriptArray.get(i)[0] > superscriptArray.get(i)[1]){
                superscriptArray.remove(i);
            }
        }
        return returnValue;
    }

    private void clearInvalidSpans(){
        for(int[] item: superscriptArray){
            if(item[0] > item[1]){
                superscriptArray.remove(item);
            }
        }
    }

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
    }

}
