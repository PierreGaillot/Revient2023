package com.prgaillot.revient.ui.StuffDetailsFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

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


    ImageView stuffImageView;
    TextView displayNameTextView, actionTextView, backDurationTimeTextView, initialLoanDateTextView, loanDurationTextView, comeBackDateTextView;
    StuffDetailsViewModel viewModel;
    ProgressBar backDurationProgressBar;
    String userLoggedId;
    Button actionBtn, deleteBtn;
    BottomSheetDialog addDelayBottomSheetDial, deleteStuffBottomSheetDial;

    public StuffDetailsFragment() {
    }

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
            initialLoanDateTextView = view.findViewById(R.id.stuffDetails_initialLoanDate_textView);
            loanDurationTextView = view.findViewById(R.id.stuffDetails_initialLoanDuration_textView);
            comeBackDateTextView = view.findViewById(R.id.stuffDetails_comeBackDate_textView);

            if (stuffItem.getStuff().getBorrowerId() != null) {

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

                    // **** DELETE ACTION ****
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

                    // **** ADD DELAY ACTION ****
                    actionBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addDelayBottomSheetDial = new BottomSheetDialog(getContext());
                            View bottomDialView = getLayoutInflater().inflate(R.layout.add_delay_bt_dialog, null, false);
                            Button submitBtn = bottomDialView.findViewById(R.id.addDelayBtDial_submit_btn);
                            SeekBar delaySeekBar = bottomDialView.findViewById(R.id.addDelayBtDial_seekBar);
                            TextView durationTextView = bottomDialView.findViewById(R.id.addDelayBtDial_duration_textView);

                            long second = 1000;
                            long min = second * 60;
                            long hour = min * 60;
                            long day = hour * 24;
                            long week  = day * 7;
                            long month = day *30;
                            long year = day * 365;

                            List<DurationObj> durationList = new ArrayList<>();
                            durationList.add(new DurationObj("2 heures", 2*hour));
                            durationList.add(new DurationObj("6 heures", 6*hour));
                            durationList.add(new DurationObj("12 heures", 12*hour));
                            durationList.add(new DurationObj("1 journée", day));
                            durationList.add(new DurationObj("2 jours", 2*day));
                            durationList.add(new DurationObj("1 semaine", week));
                            durationList.add(new DurationObj("2 semaines", 2*week));
                            durationList.add(new DurationObj("3 semaines", 3*week));
                            durationList.add(new DurationObj("1 mois", month));
                            durationList.add(new DurationObj("2 mois", 2*month));
                            durationList.add(new DurationObj("4 mois", 4*month));
                            durationList.add(new DurationObj("6 mois", 6*month));
                            durationList.add(new DurationObj("1an", year));

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
                                            initDelays();
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

            initDelays();

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

    private void initDelays() {
        viewModel.initDelays(stuffItem.getStuff(), new Callback<Void>() {
            @Override
            public void onCallback(Void result) {

                initTimerProgress();

                DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.FRANCE);

                comeBackDateTextView.setText("come back " +  dateFormat.format(stuffItem.getStuff().getBackTimeTimestamp()));
                initialLoanDateTextView.setText(String.format("%s%s", getString(R.string.loanded), dateFormat.format(stuffItem.getStuff().getInitialLoanDateTimestamp())));

                viewModel.getComeBackDuration();

                if (stuffItem.getStuff().getAdditionalDelay() == 0) {
                    loanDurationTextView.setText(getString(R.string.initialLoanDurationLabel) +viewModel.getInitialLoanDuration());
                } else {
                    loanDurationTextView.setText(getString(R.string.initialLoanDurationLabel) + String.format("%s%s", viewModel.getInitialLoanDuration(), String.format("(+ %s)", viewModel.getAdditionalDelay())));
                }

                viewModel.getBackDuration(new Callback<Long>() {
                    @Override
                    public void onCallback(Long result) {
                        if (result <= 0) {
                            backDurationTimeTextView.setTextColor(getContext().getColor(R.color.rv_warn));
                            backDurationTimeTextView.setText("Watch out, you're late. You must return this object as soon as possible or ask its owner for a delay.");
                        } else {
                            backDurationTimeTextView.setText(viewModel.getComeBackDuration());
                        }
                    }
                });

            }
        });
    }


    private void initTimerProgress() {
        Stuff stuff = stuffItem.getStuff();
        CircleTimer circleTimer = new CircleTimer(backDurationProgressBar, getContext(), stuff);
        circleTimer.initTimer();
    }

}