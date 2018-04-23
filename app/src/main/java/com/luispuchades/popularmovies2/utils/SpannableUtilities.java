package com.luispuchades.popularmovies2.utils;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;

/* TODO: COMPLETED*/
public class SpannableUtilities {

    /* private constructor to avoid errors */
    private SpannableUtilities(){
    }

    /*
     * @param string the text to be converted to bold
     * @return the SpannableString converted to bold
     */
    public static SpannableString getBold (String string) {
        SpannableString boldText = new SpannableString(string);
        boldText.setSpan(new StyleSpan(Typeface.BOLD), 0, boldText.length(),0);
        return boldText;
    }
}
