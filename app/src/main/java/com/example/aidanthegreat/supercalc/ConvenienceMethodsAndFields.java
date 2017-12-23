package com.example.aidanthegreat.supercalc;

import java.util.Arrays;
import java.util.ArrayList;

/**
 * Created by AidanTheGreat on 12/23/2017.
 */

public class ConvenienceMethodsAndFields {
    /**List of all possible operator symbols*/
    public static String[] operatorsList = {"+", "-", "/", "*", "sin", "cos", "tan", "(", ")"};


    /**Builds an ArrayList out of a string, for the purpose of performing mathematical operations.
     As such, it separates at operators, so that all items are spaced out for a math algorithm*/
    public static ArrayList<String> buildOperationList(String s){
        String copyString = s;
        //Replaces the operators with properly spaced versions, so that the string can be split along spaces
        for(String s1: ConvenienceMethodsAndFields.operatorsList){
            copyString = copyString.replace(s1, " " + s1 + " ");
        }
        String[] stringList = copyString.split(" ");

        return new ArrayList<String>(Arrays.asList(stringList));
    }

    /**Builds an ArrayList out of a string, for the purpose of character-by-character modification.
     As such, it splits along every character*/
    public static ArrayList<String> buildModifierList(String s){
        String[] modified = s.split("");
        return new ArrayList<String>(Arrays.asList(modified));
    }

    public static void incrementSpans(int index, ArrayList<int[]> spanList) {
        for(int[] i : spanList){
            if(i[1] >= index){
                i[0] ++;
                i[1] ++;
            }
        }
    }

    public static void decrementSpans(int index, ArrayList<int[]> spanList) {
        for (int[] i : spanList) {
            if (i[0] > 0) {
                if (i[0] >= index) {
                    System.out.println(i[0] + " " + index);
                    i[0]--;
                    i[1]--;
                } else if (index >= i[0] && index <= i[1]) {
                    i[1]--;
                }
            }
        }
    }

    /**
     * @param index: The index to be tested.
     * @return Boolean, whether or not the provided index is in any of the currently available superscript ranges
     */
    public static boolean inSpan(int index, ArrayList<int[]> spanList){
        //By default, this method will return false
        boolean returnValue = false;
        //Check each array in the list of all superscript locations. If the provided index is larger than the smaller item, and smaller than the larger item, then it is in the range and the
        //method will break the loop, and return true
        for(int[] item : spanList){
            if(index >= item[0] && index <=item[1]){
                returnValue = true;
                break;
            }
        }
        return returnValue;
    }

    /**
     * @param index: The index to be checked
     * @return Index in the superscript array that has provided index in its range. Only call when a value has been found from inSuperscript, otherwise it will return index 0, when nothing is there
     */
    public static int getRange(int index, ArrayList<int[]> spanList){
        //By default, this will return -1. As a result, should only be called after the selected index has been confirmed to be in a superscript range
        int returnValue = -1;
        //For all items in the sueprscript array, we check if the index is larger than the smaller value, and smaller than the larger value. If so, that means that it is a superscript, and we return the value of the current index
        for(int i = 0; i < spanList.size(); i ++){
            if(index >= spanList.get(i)[0] && index <= spanList.get(i)[1]){
                returnValue = i;
                break;
            }
        }

        for(int i = 0; i < spanList.size(); i++){
            if(index >= spanList.get(i)[0] && index <= spanList.get(i)[1]){
                returnValue = i;
                break;
            }
        }
        return returnValue;
    }
}