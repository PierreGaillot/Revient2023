package com.prgaillot.revient.ui.NewStuffActivity.StuffDetailsFragment;

import androidx.lifecycle.ViewModel;

import com.prgaillot.revient.domain.models.Stuff;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.domain.usecases.AddStuffDelayUseCase;
import com.prgaillot.revient.domain.usecases.DeleteStuffUseCase;
import com.prgaillot.revient.domain.usecases.GetUserByIdUserCase;
import com.prgaillot.revient.utils.Callback;

import java.util.Calendar;
import java.util.Date;

public class StuffDetailsViewModel extends ViewModel {
    private static final String TAG = "StuffDetailsVM";
    private final GetUserByIdUserCase getUserByIdUserCase = GetUserByIdUserCase.instance;
    private final AddStuffDelayUseCase addStuffDelayUseCase = AddStuffDelayUseCase.instance;
    private final DeleteStuffUseCase deleteStuffUseCase = DeleteStuffUseCase.instance;

    public Date startLoan, endLoan, now;

    private Stuff stuff;

    void getUser(String userId, Callback<User> callback) {
        getUserByIdUserCase.getUserById(userId, callback);
    }

    void addBringDelay(String stuffId, long delay, Callback<Void> callback) {
        addStuffDelayUseCase.addStuffDelay(stuffId, delay, callback);
    }

    public void deleteStuff(String stuffId, Callback<Void> callback) {
        deleteStuffUseCase.deleteStuff(stuffId, callback);
    }

    public void getBackDelayOld(Stuff stuff, Callback<String> callback) {
        long backDurationTime = stuff.getBackTimeTimestamp();
        int backDurationTimeSeconds = (int) (backDurationTime / 3600);
        int backDurationTimeMinutes = (int) (backDurationTimeSeconds / 60);
        int backDurationTimeHours = (int) (backDurationTimeMinutes / 60);
        int backDurationTimeDays = (int) (backDurationTimeHours / 24);
        int backDurationTimeMonths = (int) (backDurationTimeDays / 30);

        StringBuilder backDuration = new StringBuilder();
        if (backDurationTimeDays > 0) {
            backDuration.append(backDurationTimeDays + " days ");
        }

        if ((backDurationTimeHours - backDurationTimeDays * 24) > 0) {
            backDuration.append((backDurationTimeHours - backDurationTimeDays * 24) + " hours");
        }

        if ((backDurationTimeDays - backDurationTimeMonths * 30) > 0) {
            backDuration.append((backDurationTimeDays - backDurationTimeMonths * 30) + " months");
        }

        callback.onCallback(backDuration.toString());
    }

    public void initDelays(Stuff stuff, Callback<Void> callback){
        this.stuff = stuff;

        Calendar calendarNow = Calendar.getInstance();
        Calendar calendarStartLoan = Calendar.getInstance();
        Calendar calendarEndLoan = Calendar.getInstance();

        calendarNow.getTime();
        calendarStartLoan.setTimeInMillis(stuff.getInitialLoanDateTimestamp());
        calendarEndLoan.setTimeInMillis(stuff.getBackTimeTimestamp());

        now = calendarNow.getTime();
        startLoan = calendarStartLoan.getTime();
        endLoan = calendarEndLoan.getTime();

        callback.onCallback(null);
    }

    public void getBackDuration(Callback<Long> callback) {
        callback.onCallback(endLoan.getTime() - startLoan.getTime());
    }

    public String getComeBackDuration(){
        return getDifference(endLoan, now);
    }

    public String getInitialLoanDuration(){
       return getDifference(endLoan, startLoan);
    }

    public String getAdditionalDelay(){
        Calendar calendarAdditionalDelay = Calendar.getInstance();
        calendarAdditionalDelay.setTimeInMillis(now.getTime() + stuff.getAdditionalDelay());
        Date nowWithAddDelay = calendarAdditionalDelay.getTime();
        return getDifference(nowWithAddDelay, now);
    }

    public String getDifference(Date start, Date end) {
        long different = start.getTime() - end.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        String output = "";

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;
        if (elapsedDays > 0) output += elapsedDays + " days";

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;
        if (elapsedHours > 0) output += elapsedHours + " hours";

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;
        if (elapsedMinutes > 0) output += elapsedMinutes + " min";

        return output;
    }


}
