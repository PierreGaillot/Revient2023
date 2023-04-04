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
import com.prgaillot.revient.domain.models.Stuff;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.ui.MainActivity.MainActivity;
import com.prgaillot.revient.utils.Callback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
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
        durationTextView = view.findViewById(R.id.newStuff_loanDuration_textView);
        durationSeekbar = view.findViewById(R.id.newStuff_loanDuration_seekBar);

        durationSeekbar.setMax(12);
        durationSeekbar.setProgress(3, true);

        HashMap<Integer, String> durationDisplayScale = new HashMap<>();
        durationDisplayScale.put(0, "2 heures");
        durationDisplayScale.put(1, "6 heures");
        durationDisplayScale.put(2, "12 heures");
        durationDisplayScale.put(3, "1 journée");
        durationDisplayScale.put(4, "2 jours");
        durationDisplayScale.put(5, "1 semaine");
        durationDisplayScale.put(6, "2 semaines");
        durationDisplayScale.put(7, "3 semaines");
        durationDisplayScale.put(8, "1 mois");
        durationDisplayScale.put(9, "2 mois");
        durationDisplayScale.put(10, "4 mois");
        durationDisplayScale.put(11, "6 mois");
        durationDisplayScale.put(12, "1an");


        HashMap<Integer, Long> durationTimeScale = new HashMap<>();
        durationTimeScale.put(0, 7200000L);
        durationTimeScale.put(1, 21600000L);
        durationTimeScale.put(2, 43200000L);
        durationTimeScale.put(3, 86400000L);
        durationTimeScale.put(4, 172800000L);
        durationTimeScale.put(5, 604800000L);
        durationTimeScale.put(6, 1209600000L);
        durationTimeScale.put(7, 1814400000L);
        durationTimeScale.put(8, 2419200000L);
        durationTimeScale.put(9, 4838400000L);
        durationTimeScale.put(10, 9676800000L);
        durationTimeScale.put(11, 19353600000L);
        durationTimeScale.put(12, 94608000000L);

        durationTextView.setText(durationDisplayScale.get(durationSeekbar.getProgress()));

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
                                    stuff.setInitialLoanDurationTimestamp(durationTimeScale.get(durationSeekbar.getProgress()));
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
                durationTextView.setText(durationDisplayScale.get(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                durationTextView.setText(durationDisplayScale.get(durationSeekbar.getProgress()));
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