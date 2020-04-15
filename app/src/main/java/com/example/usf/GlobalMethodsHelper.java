package com.example.usf;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;

public class GlobalMethodsHelper {
    
    public void setCustomActionbar(ActionBar ab, Context c, Resources r, AssetManager a, String text) {
        ab.setTitle(text);
        ab.setBackgroundDrawable(r.getDrawable(R.drawable.background_gradient));
        TextView tv = new TextView(c);
        tv.setTypeface(Typeface.createFromAsset(a, "fonts/raleway.ttf"));
        tv.setText(ab.getTitle());
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(20);
        ab.setDisplayOptions(ab.DISPLAY_SHOW_CUSTOM);
        ab.setCustomView(tv);
    }
}