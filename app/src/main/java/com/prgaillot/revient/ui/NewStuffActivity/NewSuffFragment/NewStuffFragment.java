package com.prgaillot.revient.ui.NewStuffActivity.NewSuffFragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.prgaillot.revient.R;
import com.prgaillot.revient.domain.models.DurationObj;
import com.prgaillot.revient.domain.models.Stuff;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.ui.MainActivity.MainActivity;
import com.prgaillot.revient.ui.NewStuffActivity.NewStuffActivity;
import com.prgaillot.revient.utils.Callback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class NewStuffFragment extends Fragment {


    private static final String TAG = "NewStuffFragment";
    private User borrower;
    private EditText displayNameEditText, borrowerEmailEditText;
    private Button submitBtn, photoBtn, loanToBtn;
    private TextView durationTextView;
    private ImageView image;
    private SeekBar durationSeekbar;
    private NewStuffFragmentViewModel viewModel;
    private HashMap<String, String> friendsHashMap;
    private LinearLayout delayLL, borrowerEmailLL;
    List<String> friendsIdList, friendNameList;


    FragmentContainerView borrowerFragmentContainerView;

    private final int CAMERA_REQUEST_CODE = 102;
    private final int PERMISSION_CAMERA_REQUEST_CODE = 101;
    Bitmap photo;
    private FragmentManager fragmentManager;

    private boolean isEmailBorrower = false;


    public NewStuffFragment() {
        // Required empty public constructor
    }

    public NewStuffFragment(User borrower) {
        this.borrower = borrower;
    }

    public NewStuffFragment(boolean isEmailBorrower) {
        this.isEmailBorrower = isEmailBorrower;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            if (bundle.getSerializable("userKey") != null) {
                borrower = (User) bundle.getSerializable("userKey");
            } else if (bundle.getBoolean("isEmailBorrowerKey")) {
                isEmailBorrower = true;
            }

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_stuff, container, false);

        viewModel = new ViewModelProvider(this).get(NewStuffFragmentViewModel.class);

        displayNameEditText = view.findViewById(R.id.newStuff_editTextText_displayName);
        submitBtn = view.findViewById(R.id.newStuff_btn_submit);
        borrowerEmailEditText = view.findViewById(R.id.newStuff_borrowerEmail_editText);
        photoBtn = view.findViewById(R.id.newStuff_photo_btn);
        image = view.findViewById(R.id.newStuff_imageView);
        durationTextView = view.findViewById(R.id.delaySelection_textView);
        durationSeekbar = view.findViewById(R.id.delaySelection_seekBar);
        loanToBtn = view.findViewById(R.id.newStuff_loanTo_btn);
        delayLL = view.findViewById(R.id.newStuff_delay_LL);
        borrowerEmailLL = view.findViewById(R.id.newStuff_borrower_LL);
        fragmentManager = getParentFragmentManager();


        delayLL.setVisibility(View.GONE);
        borrowerEmailLL.setVisibility(View.GONE);


        if (borrower != null) {
            borrowerFragmentContainerView = view.findViewById(R.id.newStuffFragment_fragmentBorrower_container_view);
            BorrowerFragment borrowerFragment = new BorrowerFragment();
            Bundle userBundle = new Bundle();
            userBundle.putSerializable("userKey", borrower);
            borrowerFragment.setArguments(userBundle);
            borrowerFragmentContainerView.setVisibility(View.VISIBLE);
            delayLL.setVisibility(View.VISIBLE);
            fragmentManager.beginTransaction().replace(R.id.newStuffFragment_fragmentBorrower_container_view, borrowerFragment, null).commit();
        }

        if (isEmailBorrower) {
            borrowerEmailLL.setVisibility(View.VISIBLE);
            delayLL.setVisibility(View.VISIBLE);
        }

        durationSeekbar.setMax(12);
        durationSeekbar.setProgress(3, true);

        long second = 1000;
        long min = second * 60;
        long hour = min * 60;
        long day = hour * 24;
        long week = day * 7;
        long month = day * 30;
        long year = day * 365;

        List<DurationObj> durationList = new ArrayList<>();
        durationList.add(new DurationObj("2 heures", 2 * hour));
        durationList.add(new DurationObj("6 heures", 6 * hour));
        durationList.add(new DurationObj("12 heures", 12 * hour));
        durationList.add(new DurationObj("1 journée", day));
        durationList.add(new DurationObj("2 jours", 2 * day));
        durationList.add(new DurationObj("1 semaine", week));
        durationList.add(new DurationObj("2 semaines", 2 * week));
        durationList.add(new DurationObj("3 semaines", 3 * week));
        durationList.add(new DurationObj("1 mois", month));
        durationList.add(new DurationObj("2 mois", 2 * month));
        durationList.add(new DurationObj("4 mois", 4 * month));
        durationList.add(new DurationObj("6 mois", 6 * month));
        durationList.add(new DurationObj("1an", year));

        durationTextView.setText(durationList.get(durationSeekbar.getProgress()).getName());

//        image.setVisibility(View.VISIBLE);
//        Glide.with(this)
//                .load("https://images.squarespace-cdn.com/content/v1/5311122ee4b0c281596e75ce/1522025636043-OBEYDMLJJTCD6M23CZ3Q/001c-ww.jpg")
//                .circleCrop()
//                .centerInside().into(image);


        loanToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NewStuffActivity) getActivity()).openLoanToBottomSheetDial();
            }
        });

        viewModel.getCurrentUser(new Callback<User>() {
            @Override
            public void onCallback(User user) {

                photoBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                                ((NewStuffActivity)getActivity()).askPermissionsCamera();
                        askPermissionsCamera();
                    }
                });


                submitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (displayNameEditText.getText() != null) {
                            Stuff stuff = new Stuff(user.getUid(), displayNameEditText.getText().toString());
                            if (borrower != null) {
                                stuff.setBorrowerId(borrower.getUid());
                            } else if (isEmailBorrower) {
                                stuff.setBorrowerEmail(borrowerEmailEditText.getText().toString());
                            }
                            stuff.setInitialLoanDurationTimestamp(durationList.get(durationSeekbar.getProgress()).getValue());
                            viewModel.createStuff(stuff, new Callback<String>() {
                                @Override
                                public void onCallback(String result) {
                                    createImgRef(result);
                                    Toast.makeText(getContext(), stuff.getDisplayName() + "has been added", Toast.LENGTH_SHORT).show();
                                    Intent mainActIntent = new Intent(getContext(), MainActivity.class);
                                    startActivity(mainActIntent);
                                }
                            });
                        }
                    }
                });
            }
        });

//            }
//        });

        durationSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                durationTextView.setText(durationList.get(progress).getName());

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                durationTextView.setText(durationList.get(durationSeekbar.getProgress()).getName());
            }
        });


        return view;
    }

    public void createImgRef(String stuffId) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference stuffImgRef = storageRef.child("stuffImg/" + stuffId + ".jpeg");
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = stuffImgRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getContext(), "L'image à était upload !", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Erreur de l'upload de l'image !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void switchIsEmailBorrower() {
        isEmailBorrower = !isEmailBorrower;
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    public void askPermissionsCamera() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA_REQUEST_CODE);
        } else {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(getContext(), "Permission camera is requiered", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && data != null) {
            Bundle bundle = data.getExtras();
            photo = (Bitmap) bundle.get("data");

            Glide.with(this)
                    .load(photo)
                    .centerInside()
                    .circleCrop()
                    .into(image);
            image.setVisibility(View.VISIBLE);
        }

    }
}