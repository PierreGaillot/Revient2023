package com.prgaillot.revient.ui.NewStuffActivity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.prgaillot.revient.R;
import com.prgaillot.revient.databinding.ActivityNewStuffBinding;
import com.prgaillot.revient.domain.models.DurationObj;
import com.prgaillot.revient.domain.models.Stuff;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.ui.MainActivity.MainActivity;
import com.prgaillot.revient.utils.Callback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewStuffActivity extends AppCompatActivity {

    TextView durationTextView;
    AutoCompleteTextView borrowerACTextView;
    EditText displayNameEditText;
    Button submitBtn, photoBtn;
    ImageView image;

    SeekBar durationSeekbar;
    private NewStuffActivityViewModel viewModel;
    private HashMap<String, String> friendsHashMap;
    private final int CAMERA_REQUEST_CODE = 102;
    private final int PERMISSION_CAMERA_REQUEST_CODE = 101;
    Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.prgaillot.revient.databinding.ActivityNewStuffBinding binding = ActivityNewStuffBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(NewStuffActivityViewModel.class);

        View view = binding.getRoot();
        setContentView(view);

        displayNameEditText = view.findViewById(R.id.newStuff_editTextText_displayName);
        submitBtn = view.findViewById(R.id.newStuff_btn_submit);
        borrowerACTextView = view.findViewById(R.id.newStuff_borrower_AutoCompleteTV);
        photoBtn = view.findViewById(R.id.newStuff_photo_btn);
        image = view.findViewById(R.id.newStuff_imageView);
        durationTextView = view.findViewById(R.id.delaySelection_textView);
        durationSeekbar = view.findViewById(R.id.delaySelection_seekBar);

        durationSeekbar.setMax(12);
        durationSeekbar.setProgress(3, true);

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


        durationTextView.setText(durationList.get(durationSeekbar.getProgress()).getName());

        viewModel.getCurrentUser(new Callback<User>() {
            @Override
            public void onCallback(User user) {

                viewModel.getUserFriend(user.getUid(), new Callback<List<User>>() {
                    @Override
                    public void onCallback(List<User> result) {
                        viewModel.prepareFriendsSearchList(result, new Callback<HashMap<String, String>>() {
                            @Override
                            public void onCallback(HashMap<String, String> result) {
                                friendsHashMap = result;
                                List<String> friendNameList = new ArrayList<>(result.keySet());
                                ArrayAdapter<String> friendListAdapter = new ArrayAdapter<>(getBaseContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, friendNameList);
                                Log.d(TAG, friendNameList.toString());
                                borrowerACTextView.setAdapter(friendListAdapter);
                            }
                        });

                        photoBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                askPermissionsCamera();

                            }
                        });


                        submitBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (displayNameEditText.getText() != null) {
                                    Stuff stuff = new Stuff(user.getUid(), displayNameEditText.getText().toString());
                                    if (!borrowerACTextView.getText().toString().isEmpty() && !friendsHashMap.isEmpty()) {
                                        stuff.setBorrowerId(friendsHashMap.get(borrowerACTextView.getText().toString()));
                                    }
                                    stuff.setInitialLoanDurationTimestamp(durationList.get(durationSeekbar.getProgress()).getValue());
                                    viewModel.createStuff(stuff, new Callback<String>() {
                                        @Override
                                        public void onCallback(String result) {
                                            createImgRef(result);
                                            Toast.makeText(getBaseContext(), stuff.getDisplayName() + "has been added", Toast.LENGTH_SHORT).show();
                                            Intent mainActIntent = new Intent(getBaseContext(), MainActivity.class);
                                            startActivity(mainActIntent);
                                        }
                                    });
                                }
                            }
                        });
                    }
                });

            }
        });


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


    }


    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    public void askPermissionsCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA_REQUEST_CODE);
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
                Toast.makeText(getBaseContext(), "Permission camera is requiered", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && data != null) {
//            imgPath = data.getData().getPath();
            Bundle bundle = data.getExtras();
            photo = (Bitmap) bundle.get("data");
            image.setImageBitmap(photo);

        }
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
                Toast.makeText(getBaseContext(), "L'image à était upload !", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getBaseContext(), "Erreur de l'upload de l'image !", Toast.LENGTH_SHORT).show();
            }
        });


    }

}