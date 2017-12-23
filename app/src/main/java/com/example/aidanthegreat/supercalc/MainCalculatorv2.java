package com.example.aidanthegreat.supercalc;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainCalculatorv2 extends AppCompatActivity {
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
        ArrayList<String> modifier = ConvenienceMethodsAndFields.buildModifierList(numbersView.getText().toString());

        if(!superScript) {
            ConvenienceMethodsAndFields.incrementSpans(numbersView.getSelectionStart(), superscriptArray);
        }

        if(ConvenienceMethodsAndFields.inSpan(numbersView.getSelectionStart(), superscriptArray) && superScript) {
            modifier.add(numbersView.getSelectionStart() + 1, addedString);
            int newIndex = ConvenienceMethodsAndFields.getRange(numbersView.getSelectionStart(), superscriptArray);
            superscriptArray.get(newIndex)[1] = superscriptArray.get(newIndex)[1] + 1;
        }
        else if(ConvenienceMethodsAndFields.inSpan(numbersView.getSelectionStart(), subscriptArray) && subScript){
            modifier.add(numbersView.getSelectionStart() + 1, addedString);
            int newIndex = ConvenienceMethodsAndFields.getRange(numbersView.getSelectionStart(), subscriptArray);
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

    public void sqrtClick(View v){
        addTextInView(getString(R.string.sqrt));
        int[] arrayListsAreAssholes = {numbersView.getSelectionStart(), numbersView.getSelectionStart()};
        subscriptArray.add(arrayListsAreAssholes);
        subScript = true;
    }

    //TODO Document this method
    /**Method to delete an item from the view*/
    private void backspace() {
        String finalModified = "";
        int finalPosition = numbersView.getSelectionStart() - 1;

        if (finalPosition < 0) {
            finalPosition = 0;
        }

        if (!instantDelete && !ConvenienceMethodsAndFields.inSpan(numbersView.getSelectionStart(), superscriptArray)) {
            ArrayList<String> modifiedList = ConvenienceMethodsAndFields.buildModifierList(numbersView.getText().toString());
            modifiedList.remove(numbersView.getSelectionStart());
            finalModified = buildString(modifiedList);

        } else if (ConvenienceMethodsAndFields.inSpan(numbersView.getSelectionStart(), superscriptArray) || ConvenienceMethodsAndFields.inSpan(numbersView.getSelectionStart(), subscriptArray)) {
            ArrayList<String> modifiedList = ConvenienceMethodsAndFields.buildModifierList(numbersView.getText().toString());
            modifiedList.remove(numbersView.getSelectionStart());
            finalModified = buildString(modifiedList);
            clearInvalidSpans();
        }


        ConvenienceMethodsAndFields.decrementSpans(finalPosition, superscriptArray);
        ConvenienceMethodsAndFields.decrementSpans(finalPosition, subscriptArray);

        updateNumbersView(finalModified);
        if (numbersView.getText().toString().length() > 0) {
            numbersView.setSelection(finalPosition, finalPosition);
        }
    }

    //-----------Convenience methods------------
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
            else if(item[0] == 0){
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
