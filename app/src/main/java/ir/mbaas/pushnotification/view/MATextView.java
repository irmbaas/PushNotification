package ir.mbaas.pushnotification.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Mahdi on 2/6/2016.
 */
public class MATextView extends TextView {

    public MATextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/IRANSans.ttf"));
    }
}
