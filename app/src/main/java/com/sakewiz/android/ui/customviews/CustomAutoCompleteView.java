package com.sakewiz.android.ui.customviews;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by dilshan_e on 6/11/17.
 */
public class CustomAutoCompleteView extends android.support.v7.widget.AppCompatAutoCompleteTextView {

    public CustomAutoCompleteView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public CustomAutoCompleteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public CustomAutoCompleteView(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void performFiltering(final CharSequence text, final int keyCode) {
        String filterText = "";
        super.performFiltering(filterText, keyCode);
    }
    /**
     * After a selection, capture the new value and append to the existing
     * text
     */
    @Override
    protected void replaceText(final CharSequence text) {
        super.replaceText(text);
    }

}
