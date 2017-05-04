package me.citrafa.asistenkuliahku.CustomWidget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by SENSODYNE on 18/04/2017.
 */

public class TVLatoFontRegular extends android.support.v7.widget.AppCompatTextView{

    public TVLatoFontRegular(Context context) {
        super(context);
        init();
    }

    public TVLatoFontRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TVLatoFontRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Lato-Regular.ttf");
            setTypeface(tf);
        }
    }

}
