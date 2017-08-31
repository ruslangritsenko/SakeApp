package com.sakewiz.android.ui.customviews;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by dilshan_e on 07/07/2017.
 */
public class ExpandableTextSpannable extends ClickableSpan {

    private boolean isUnderline = true;

    /**
     * Constructor
     */
    public ExpandableTextSpannable(boolean isUnderline) {
        this.isUnderline = isUnderline;
    }

    @Override
    public void updateDrawState(TextPaint ds) {

//        ds.setUnderlineText(isUnderline);
        ds.setColor(Color.parseColor("#69a574"));

    }

    @Override
    public void onClick(View widget) {

    }
}