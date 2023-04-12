package com.prgaillot.revient.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.prgaillot.revient.R;
import com.prgaillot.revient.domain.models.Stuff;

public class CircleTimer {
    private static final String TAG = "CircleTimer";
    Context context;
    Stuff stuff;
    ProgressBar circleTimer;

    public CircleTimer(ProgressBar circleTimer, Context context, Stuff stuff) {
        this.stuff = stuff;
        this.circleTimer = circleTimer;
        this.context = context;
    }

    public void initTimer() {
        if (stuff.getBorrowerId() != null) {
            circleTimer.setVisibility(View.VISIBLE);
            getBackDuration(new Callback<Integer>() {
                @Override
                public void onCallback(Integer result) {
                    String name = stuff.getDisplayName();
                    if (result <= 0) {
                        Log.d(TAG, "stuff late => " + name + " : " + result);
                        circleTimer.setProgressTintList(ColorStateList.valueOf(context.getColor(R.color.rv_warn)));
                    } else {
                        Log.d(TAG, "stuff => " + name + " : " + result);
                    }
                    circleTimer.setProgress(result, true);
                }
            });
        }
    }


    private void getBackDuration(Callback<Integer> callback) {
        long now = System.currentTimeMillis();
        long totalLoanDuration = stuff.getAdditionalDelay() + stuff.getInitialLoanDurationTimestamp();
        long backDate = stuff.getInitialLoanDateTimestamp() + totalLoanDuration;
        long timeLeft = backDate - now;
        double result = (double) timeLeft / totalLoanDuration;
        callback.onCallback((int) (result * 100));
    }
}
