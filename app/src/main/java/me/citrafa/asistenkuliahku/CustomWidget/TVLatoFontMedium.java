package me.citrafa.asistenkuliahku.CustomWidget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by SENSODYNE on 18/04/2017.
 */

public class TVLatoFontMedium extends android.support.v7.widget.AppCompatTextView{
    public TVLatoFontMedium(Context context) {
        super(context);
    }

    public TVLatoFontMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TVLatoFontMedium(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Lato-Medium.ttf");
            setTypeface(tf);
        }
    }

}
