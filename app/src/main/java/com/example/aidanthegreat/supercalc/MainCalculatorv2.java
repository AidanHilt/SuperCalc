package com.example.aidanthegreat.supercalc;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MainCalculatorv2 extends AppCompatActivity {

    /**List of all possible operator symbols*/
    private static String[] operatorsList = {"+", "-", "/", "*", "sin", "cos", "tan", "(", ")"};

    /**The ArrayList that stores 2-index arrays with the start and stop location of all superscripts*/
    private static ArrayList<int[]> superscriptArray = new ArrayList<>();

    /**The ArrayList that stores 2-index arrays with the start and stop location of all subscripts*/
    private static ArrayList<int[]> subscriptArray = new ArrayList<>();

    /**Getter for the superscript array, might not currently be necessary*/
    public static ArrayList<int[]> getSuperscriptArray(){
        return superscriptArray;
    }

    /**The view that shows the numbers to be operated on*/
    //TODO Override the click method so it sets to super- or subscript
    private EditTextWithUpdate numbersView;
    /**Button that creates an exponent in the TextView. This is necessary to show the superscript text on the button*/
    private Button exponentButton;
    /**Button that adds a decimal point in the TextView. This is necessary so the point is not cut off by the edge of the button*/
    private Button decimalButton;
    /**Button that deletes an item of text. This is necessary to add a long click listener*/
    //TODO Add a long click method
    private Button deleteButton;

    /**Array that stores the contents of the TextView*/
    private static ArrayList<String> calculationContents = new ArrayList<>();

    /**Boolean to track whether or not the input is in superscript mode*/
    private static boolean superScript = false;

    public static void setSuperScript(boolean b){
        superScript = b;
    }

    /**Boolean to track whether or not the input is in subscript mode*/
    private static boolean subScript = false;

    /**Boolean to track whether or not the delete button clears the entire screen*/
    private static boolean instantDelete = true;

    /**Method to build a string from an ArrayList*/
    private static String buildString(ArrayList<String> a){
        //Initialize a blank string
        String string = "";

        //For every item in the ArrayList, add it to the string, then return it.
        for(String s: a){
            string += s;
        }

        return string;
    }

    /**Builds an ArrayList out of a string, for the purpose of performing mathematical operations.
    As such, it separates at operators, so that all items are spaced out for a math algorithm*/
    private static ArrayList<String> buildOperationList(String s){
        String copyString = s;
        //Replaces the operators with properly spaced versions, so that the string can be split along spaces
        for(String s1: operatorsList){
            copyString = copyString.replace(s1, " " + s1 + " ");
        }
        String[] stringList = copyString.split(" ");

        return new ArrayList<String>(Arrays.asList(stringList));
    }

    /**Builds an ArrayList out of a string, for the purpose of character-by-character modification.
    As such, it splits along every character*/
    private static ArrayList<String> buildModifierList(String s){
        String[] modified = s.split("");
        return new ArrayList<String>(Arrays.asList(modified));
    }

    /**Probably just a test method, should likely be deleted at some point*/
    //TODO Possibly can be deleted
    void print(int[] a){
        for(int i : a){
            System.out.println(i);
        }
    }

    /**
     * Method that adds text to the numbersView
     * @param addedString: The string to be added to numbersView
     */
    private void addTextInView(String addedString){
        //Disables instant deleting so that all entered text isn't suddenly deleted
        instantDelete = false;

        //Position to move the cursor to after
        int finalPosition = numbersView.getSelectionStart() + 1;
        //ArrayList on which the modification operations will be performed
        ArrayList<String> modifier = buildModifierList(numbersView.getText().toString());

        if(!superScript) {
            incrementSuperscriptSpans(numbersView.getSelectionStart());
        }

        if(inSuperscript(numbersView.getSelectionStart()) && superScript) {
            modifier.add(numbersView.getSelectionStart() + 1, addedString);
            int newIndex = getRange(numbersView.getSelectionStart());
            superscriptArray.get(newIndex)[1] = superscriptArray.get(newIndex)[1] + 1;
        }
        else if(inSubscript(numbersView.getSelectionStart()) && subScript){
            modifier.add(numbersView.getSelectionStart() + 1, addedString);
            int newIndex = getRange(numbersView.getSelectionStart());
            subscriptArray.get(newIndex)[1] = subscriptArray.get(newIndex)[1] + 1;

        }
        else{
            modifier.add(numbersView.getSelectionStart() + 1, addedString);
        }

        updateNumbersView(buildString(modifier));
        if(numbersView.getText().toString().length() > 0){
            numbersView.setSelection(finalPosition, finalPosition);
        }
    }

    //TODO Document this method
    /**Method to delete an item from the view*/
    private void backspace(){
        String finalModified = "";
        int finalPosition =  numbersView.getSelectionStart() - 1;

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
            clearInvalidSpans();
        }else if(inSubscript(numbersView.getSelectionStart())){
            ArrayList<String> modifiedList = buildModifierList(numbersView.getText().toString());
            modifiedList.remove(numbersView.getSelectionStart());
            finalModified = buildString(modifiedList);
            clearInvalidSpans();
        }

        decrementSuperscriptSpans(finalPosition);
        decrementSubscriptSpans(finalPosition);

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
        //Initialize an int array, with both indices at the current position of the cursor. We then add the array to the ArrayList that stores all the superscript locations, and set superscript to true
        int[] arrayListsAreAssholes = {numbersView.getSelectionStart(), numbersView.getSelectionStart()};
        superscriptArray.add(arrayListsAreAssholes);
        superScript=true;
    }
    //TODO Document and update this method
    public void equalsClick(View v){
        if(superScript){
            setSuperScript(false);
        }else if(subScript){
           subScript = false;
        }
    }

    //TODO Update this method to the new way of adding super- & subscript
    public void sqrtClick(View v){
        addTextInView(getString(R.string.sqrt));
        int[] arrayListsAreAssholes = {numbersView.getSelectionStart(), numbersView.getSelectionStart()};
        subscriptArray.add(arrayListsAreAssholes);
        subScript = true;
    }

    //-----------Convenience methods------------
    private static void incrementSuperscriptSpans(int index){
        for(int[] i : superscriptArray){
            if(i[1] >= index){
                i[0] ++;
                i[1] ++;
            }
        }
    }

    private static void decrementSuperscriptSpans(int index){
        for(int[] i: superscriptArray){
            if(i[0] > 0) {
                if (i[0] >= index) {
                    System.out.println(i[0] + " " + index);
                    i[0]--;
                    i[1]--;
                }else if(index >= i[0] && index <= i[1]){
                    System.out.println("Called");
                    i[1]--;
                }
            }
        }
    }

    private static void incrementSubscriptSpans(int index){
        for(int[] i: subscriptArray){
            if(i[1] >= index){
                i[0] ++;
                i[1] ++;
            }
        }
    }

    private static void decrementSubscriptSpans(int index){
        for(int[] i: subscriptArray){
            if(i[0] > 0){
                if(i[0] >= index){
                    i[0]--;
                    i[1]--;
                }
            }
        }
    }

    /**
     * @param index: The index to be tested.
     * @return Boolean, whether or not the provided index is in any of the currently available superscript ranges
     */
    protected static boolean inSuperscript(int index){
        //By default, this method will return false
        boolean returnValue = false;
        //Check each array in the list of all superscript locations. If the provided index is larger than the smaller item, and smaller than the larger item, then it is in the range and the
        //method will break the loop, and return true
        for(int[] item :superscriptArray){
            if(index >= item[0] && index <=item[1]){
                returnValue = true;
                break;
            }
        }
        return returnValue;
    }

    protected static boolean inSubscript(int index){
        boolean returnVal = false;
        for(int[] item: subscriptArray){
            if(index >= item[0] && index <= item[1]){
                returnVal = true;
            }
        }
        return returnVal;
    }

    /**
     * @param index: The index to be checked
     * @return Index in the superscript array that has provided index in its range. Only call when a value has been found from inSuperscript, otherwise it will return index 0, when nothing is there
     */
    private int getRange(int index){
        //By default, this will return -1. As a result, should only be called after the selected index has been confirmed to be in a superscript range
        int returnValue = -1;
        //For all items in the sueprscript array, we check if the index is larger than the smaller value, and smaller than the larger value. If so, that means that it is a superscript, and we return the value of the current index
        for(int i = 0; i < superscriptArray.size(); i ++){
            if(index >= superscriptArray.get(i)[0] && index <= superscriptArray.get(i)[1]){
                returnValue = i;
                break;
            }
        }

        for(int i = 0; i < subscriptArray.size(); i++){
            if(index >= subscriptArray.get(i)[0] && index <= subscriptArray.get(i)[1]){
                returnValue = i;
                break;
            }
        }
        return returnValue;
    }

    //TODO Document this method
    //TODO Also add subscript spans
    /**
     * @param addedValue: The new value that numbersView is to display
     */
    private void updateNumbersView(String addedValue){
        //SpannableStringBuilder that will be used to modify the text view
        SpannableStringBuilder addText = new SpannableStringBuilder(addedValue);

        //For every item in the superscriptArray, create a span that sets the text in that span to superscript, and makes it 75% of the size of the normal text
        for(int[] item: superscriptArray){
            addText.setSpan(new SuperscriptSpan(), item[0], item[1], Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            addText.setSpan(new RelativeSizeSpan(0.75f), item[0], item[1], Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }

        for(int[] item: subscriptArray){
            System.out.println("Called");
            System.out.println(item[0]);
            System.out.println(item[1]);
            addText.setSpan(new SubscriptSpan(), item[0], item[1], Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            addText.setSpan(new RelativeSizeSpan(0.75f), item[0], item[1], Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }


        //Then we set the text view
        numbersView.setText(addText);
    }

    //TODO Document this method
    /**
     * Purpose: Clears spans from the sub and superscript arrays that have become invalid, because they end before they begin
     */
    private void clearInvalidSpans(){
        ArrayList<int[]> remove = new ArrayList<>();

        //Checks every item in superscriptArray. If the first value is larger than the second one, then we remove it from the array
        for(int[] item: superscriptArray){
            if(item[0] > item[1]){
                remove.add(item);
            }
        }

        for(int[] item: remove){
            superscriptArray.remove(item);
        }

        remove = new ArrayList<>();

        for(int[] item: subscriptArray){
            if (item[0] > item[1]){
                remove.add(item);
            }
        }

        for(int[] item: remove){
            subscriptArray.remove(item);
        }

    }

    //Determine if a string is an operator or a number
    //TODO Add determination for if a string is a trig function
    //TODO Document this method
    //TODO Determine if this method can be deleted
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


    //TODO Document this method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_calculatorv2);

        //Programmatic representation of the TextView
        numbersView = (EditTextWithUpdate)(findViewById(R.id.numbersView));

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

class EditTextWithUpdate extends AppCompatEditText{
    public EditTextWithUpdate(Context context){
        super(context);
    }

    public EditTextWithUpdate(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public EditTextWithUpdate(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        boolean returnVal = super.onTouchEvent(event);

        if(inSuperscript(this.getSelectionStart())){
            MainCalculatorv2.setSuperScript(true);
        }else{
            MainCalculatorv2.setSuperScript(false);
        }
        return returnVal;
    }

    private boolean inSuperscript(int index){
        //By default, this method will return false
        boolean returnValue = false;
        //Check each array in the list of all superscript locations. If the provided index is larger than the smaller item, and smaller than the larger item, then it is in the range and the
        //method will break the loop, and return true
        for(int[] item : MainCalculatorv2.getSuperscriptArray()){
            if(index >= item[0] + 1 && index <=item[1] - 1){
                returnValue = true;
                break;
            }
        }
        return returnValue;
    }
}
