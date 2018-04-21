package com.example.wanqianhw9;

import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.URLSpan;

/**
 * Created by anwanqi on 4/21/18.
 */

public class URLSpanNoUnderline extends URLSpan{

    public URLSpanNoUnderline(String url) {
        super(url);
    }
    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }

    public static void removeUnderlines(Spannable p_Text) {
        URLSpan[] spans = p_Text.getSpans(0, p_Text.length(), URLSpan.class);

        for(URLSpan span:spans) {
            int start = p_Text.getSpanStart(span);
            int end = p_Text.getSpanEnd(span);
            p_Text.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            p_Text.setSpan(span, start, end, 0);
        }
    }
}
