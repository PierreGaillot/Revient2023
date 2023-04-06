package com.prgaillot.revient.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ProgressBar;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.prgaillot.revient.R;
import com.prgaillot.revient.domain.models.Stuff;

public class CircleTimer {
    Context context;
    Stuff stuff;
    ProgressBar circleTimer;

    public CircleTimer(ProgressBar circleTimer, Context context, Stuff stuff) {
this.stuff = stuff;
        this.circleTimer = circleTimer;
        this.context = context;
    }

    public void initTimer() {
        circleTimer.setProgress(50);
        circleTimer.setVisibility(View.VISIBLE);
        circleTimer.setProgress(getBackDuration(), true);
        Drawable circle = ContextCompat.getDrawable(context, R.drawable.circle);

        if (getBackDuration() <= 0) {
            DrawableCompat.setTint(circle, ContextCompat.getColor(context, R.color.rv_warn));
        }
        circleTimer.setProgressDrawable(circle);
    }

    private int getBackDuration() {
        long back = stuff.getInitialLoanDateTimestamp() + stuff.getBackTimeTimestamp();
//        if (stuff.getAdditionalDelay() > 0) {
//            backDate += stuff.getAdditionalDelay();
//        }
        long timeLeft = back - System.currentTimeMillis();
        double result = (double) timeLeft / stuff.getBackTimeTimestamp();
        return (int) (result * 100);
    }
}
