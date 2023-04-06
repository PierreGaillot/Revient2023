package com.prgaillot.revient.ui.StuffDetailsFragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.prgaillot.revient.R;
import com.prgaillot.revient.domain.models.Stuff;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.domain.models.DurationObj;
import com.prgaillot.revient.ui.MainActivity.MainActivity;
import com.prgaillot.revient.ui.uiModels.StuffItemUiModel;
import com.prgaillot.revient.utils.Callback;
import com.prgaillot.revient.utils.CircleTimer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StuffDetailsFragment extends Fragment {

    StuffItemUiModel stuffItem;
    private String TAG = "StuffDetailsFragment";

    public StuffDetailsFragment() {
    }

    ImageView stuffImageView;
    TextView displayNameTextView, actionTextView, backDurationTimeTextView, initialLoanDateTextView, loanDurationTextView;
    StuffDetailsViewModel viewModel;
    ProgressBar backDurationProgressBar;
    String userLoggedId;
    Button actionBtn, deleteBtn;
    BottomSheetDialog addDelayBottomSheetDial, deleteStuffBottomSheetDial;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stuff_details_fragment, container, false);
        viewModel = new ViewModelProvider(this).get(StuffDetailsViewModel.class);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userLoggedId = firebaseUser.getUid();


        Bundle data = getArguments();
        if (data != null) {
            stuffItem = (StuffItemUiModel) data.getSerializable("stuffItem");
            stuffImageView = view.findViewById(R.id.stuffDetails_imageView);
            displayNameTextView = view.findViewById(R.id.stuffDetails_name_textView);
            actionTextView = view.findViewById(R.id.stuffDetails_action_textView);
            backDurationTimeTextView = view.findViewById(R.id.stuffDetails_backDurationTime_textView);
            backDurationProgressBar = view.findViewById(R.id.stuffDetails_backDuration_progressBar);
            actionBtn = view.findViewById(R.id.stuffDetails_action_btn);
            deleteBtn = view.findViewById(R.id.stuffDetails_delete_btn);
            initialLoanDateTextView = view.findViewById(R.id.stuffDetails_iniLoanDate_textView);
            loanDurationTextView = view.findViewById(R.id.stuffDetails_loanDuration_textView);

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteStuffBottomSheetDial = new BottomSheetDialog(getContext());
                    View deleteStuffBottomDialView = getLayoutInflater().inflate(R.layout.delete_stuff_bt_dialog, null, false);
                    Button deleteYes, deleteNo;
                    deleteNo = deleteStuffBottomDialView.findViewById(R.id.deleteStuffBtDial_no_btn);
                    deleteYes = deleteStuffBottomDialView.findViewById(R.id.deleteStuffBtDial_yes_btn);
                    deleteYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewModel.deleteStuff(stuffItem.getStuff().getUid(), new Callback<Void>() {
                                @Override
                                public void onCallback(Void result) {
                                    deleteStuffBottomSheetDial.dismiss();
                                    ((MainActivity) getActivity()).openHomeFragment();
                                    Toast.makeText(getContext(), "This stuff as been deleted", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    deleteNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteStuffBottomSheetDial.dismiss();
                        }
                    });
                    deleteStuffBottomSheetDial.setContentView(deleteStuffBottomDialView);
                    deleteStuffBottomSheetDial.show();
                }
            });

            if (stuffItem.getStuff().getBorrowerId() != null) {
                initLoanDurationTextView();

                if (stuffItem.getStuff().getBorrowerId().equals(userLoggedId)) {
                    viewModel.getUser(stuffItem.getStuff().getOwnerId(), new Callback<User>() {
                        @Override
                        public void onCallback(User user) {
                            actionTextView.setVisibility(View.VISIBLE);
                            actionTextView.setText(String.format("Borrowed to %s", user.getDisplayName()));
                        }
                    });
                } else if (stuffItem.getStuff().getBorrowerId() != null) {

                    actionBtn.setText(R.string.add_new_delay);

                    // **** ADD DELAY ACTION ****
                    actionBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addDelayBottomSheetDial = new BottomSheetDialog(getContext());
                            View bottomDialView = getLayoutInflater().inflate(R.layout.add_delay_bt_dialog, null, false);
                            Button submitBtn = bottomDialView.findViewById(R.id.addDelayBtDial_submit_btn);
                            SeekBar delaySeekBar = bottomDialView.findViewById(R.id.addDelayBtDial_seekBar);
                            TextView durationTextView = bottomDialView.findViewById(R.id.addDelayBtDial_duration_textView);
                            List<DurationObj> durationList = new ArrayList<>();
                            durationList.add(new DurationObj("2 heures", 7200000L));
                            durationList.add(new DurationObj("6 heures", 21600000L));
                            durationList.add(new DurationObj("12 heures", 43200000L));
                            durationList.add(new DurationObj("1 journée", 86400000L));
                            durationList.add(new DurationObj("2 jours", 172800000L));
                            durationList.add(new DurationObj("1 semaine", 604800000L));
                            durationList.add(new DurationObj("2 semaines", 1209600000L));
                            durationList.add(new DurationObj("3 semaines", 1814400000L));
                            durationList.add(new DurationObj("1 mois", 2419200000L));
                            durationList.add(new DurationObj("2 mois", 4838400000L));
                            durationList.add(new DurationObj("4 mois", 9676800000L));
                            durationList.add(new DurationObj("6 mois", 19353600000L));
                            durationList.add(new DurationObj("1an", 94608000000L));

                            delaySeekBar.setProgress(5);
                            durationTextView.setText(durationList.get(delaySeekBar.getProgress()).getName());
                            delaySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                    durationTextView.setText(durationList.get(progress).getName());
                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {

                                }

                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {
                                    durationTextView.setText(durationList.get(delaySeekBar.getProgress()).getName());
                                }
                            });
                            submitBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int durationProgress = delaySeekBar.getProgress();

                                    DurationObj durationDelay = durationList.get(durationProgress);

                                    viewModel.addBringDelay(stuffItem.getStuff().getUid(), durationDelay.getValue(), new Callback<Void>() {
                                        @Override
                                        public void onCallback(Void result) {
                                            Toast.makeText(getContext(), "vous avez ajouté un delais de " + durationDelay.getName(), Toast.LENGTH_SHORT).show();
                                            stuffItem.getStuff().setAdditionalDelay(durationDelay.getValue());
                                            refreshTimerDelay();
                                            initLoanDurationTextView();
                                            addDelayBottomSheetDial.dismiss();
                                        }
                                    });
                                }
                            });
                            addDelayBottomSheetDial.setContentView(bottomDialView);
                            addDelayBottomSheetDial.show();
                        }
                    });

                    viewModel.getUser(stuffItem.getStuff().getBorrowerId(), new Callback<User>() {
                        @Override
                        public void onCallback(User user) {
                            actionTextView.setVisibility(View.VISIBLE);
                            actionTextView.setText(String.format("Loan to to %s", user.getDisplayName()));
                        }
                    });
                }
            }


            displayNameTextView.setText(stuffItem.getStuff().getDisplayName());
            long backDurationTime = stuffItem.getStuff().getBackTimeTimestamp();
            int backDurationTimeSeconds = (int) (backDurationTime / 3600);
            int backDurationTimeMinutes = (int) (backDurationTimeSeconds / 60);
            int backDurationTimeHours = (int) (backDurationTimeMinutes / 60);
            int backDurationTimeDays = (int) (backDurationTimeHours / 24);

            StringBuilder backDuration = new StringBuilder();
            if (backDurationTimeDays > 0) {
                backDuration.append(backDurationTimeDays + " days ");
            }

            if ((backDurationTimeHours - backDurationTimeDays * 24) > 0) {
                backDuration.append((backDurationTimeHours - backDurationTimeDays * 24) + " hours");
            }
            backDurationTimeTextView.setText(backDuration.toString());

            initTimerProgress();

            String stuffUrl = stuffItem.getStuff().getImgUrl();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference(stuffUrl);
            final long ONE_MEGABYTE = 1024 * 1024;
            storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Glide.with(getContext()).load(bytes).centerCrop().circleCrop().into(stuffImageView);
                }
            });
        }
        return view;
    }

    private void initLoanDurationTextView() {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.FRANCE);

        initialLoanDateTextView.setText(String.format("%s%s", getString(R.string.loanded), dateFormat.format(stuffItem.getStuff().getInitialLoanDateTimestamp())));
        if (stuffItem.getStuff().getAdditionalDelay() == 0) {
            loanDurationTextView.setText(getString(R.string.initialLoanDurationLabel) + timestampToDuration(stuffItem.getStuff().getBackTimeTimestamp()));
        } else {
            loanDurationTextView.setText(getString(R.string.initialLoanDurationLabel) + String.format("%s%s", timestampToDuration(stuffItem.getStuff().getInitialLoanDurationTimestamp()), String.format("(+ %s)", timestampToDuration(stuffItem.getStuff().getAdditionalDelay()))));
        }
    }

    private void refreshTimerDelay() {
        initTimerProgress();
    }

    private void initTimerProgress() {
        Stuff stuff = stuffItem.getStuff();
        CircleTimer circleTimer = new CircleTimer(backDurationProgressBar, getContext(), stuff);
        circleTimer.initTimer();
    }


    public String timestampToDuration(long timestamp) {
        long hourMs = 3600000;
        long dayMs = 86400000;

        int hoursBackTime = (int) (timestamp / hourMs);
        if (hoursBackTime > 24) {
            int daysBackTime = (int) (timestamp / dayMs);
            if (daysBackTime >= 31) {
                return daysBackTime / 31 + " months";
            } else {
                return timestamp / dayMs + " days";
            }
        } else {
            return hoursBackTime + " hours";
        }
    }

}