package com.example.aidanthegreat.supercalc;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by AidanTheGreat on 12/23/2017.
 */

public class EditTextWithUpdate extends AppCompatEditText {
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
