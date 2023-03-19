package com.prgaillot.revient.ui.MainActivity;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.prgaillot.revient.R;
import com.prgaillot.revient.databinding.ActivityMainBinding;
import com.prgaillot.revient.domain.models.Stuff;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.utils.Callback;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private MainActivityViewModel viewModel;
    private AppBarConfiguration appBarConfiguration;

    private String userLogId;

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        startSignInActivity();


        com.prgaillot.revient.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        fab = binding.getRoot().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle newStuffBundle = new Bundle();
                newStuffBundle.putString("ownerId", userLogId);
                navController.navigate(R.id.action_HomeFragment_to_newStuffFragment, newStuffBundle);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_logout) {
            signOut();
            startSignInActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


    private void startSignInActivity() {
        createSignInIntent();
    }

    private void createSignInIntent() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build()
        );


        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.ic_launcher_rv_foreground)
                .setTheme(R.style.revientTheme)
                .setIsSmartLockEnabled(false, true)
                .build();
        signInLauncher.launch(signInIntent);
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            Toast.makeText(getBaseContext(), getBaseContext().getString(R.string.logged_as) + firebaseUser.getDisplayName(), Toast.LENGTH_LONG).show();
            User user = new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getPhotoUrl(), firebaseUser.getEmail());

            viewModel.userIsRegistered(firebaseUser.getUid(), new Callback<Boolean>() {
                @Override
                public void onCallback(Boolean result) {
                    if(result){
                        viewModel.getCurrentUserData(new Callback<User>() {
                            @Override
                            public void onCallback(User user) {
                                Log.d(TAG,  user.getDisplayName() + " is already a user.\n uid : " + user.getDisplayName() + "\n email : " +user.getEmail() + "\n friendsUid : " + user.getFriendsUid());
                                userLogId = user.getUid();

                                viewModel.getUserStuffCollection(user.getUid(), new Callback<List<Stuff>>() {
                                    @Override
                                    public void onCallback(List<Stuff> stuffCollection) {
                                        for (Stuff stuff :
                                                stuffCollection) {
                                            Log.d(TAG, user.getDisplayName() +  "'s Stuff : "+  stuff.getDisplayName());
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        Log.d(TAG, "new user is created : " + user.getDisplayName());
                        viewModel.createUser(user);
                        userLogId = user.getUid();
                    }
                }
            });

        } else {
            Toast.makeText(getBaseContext(), getBaseContext().getString(R.string.error_occurred), Toast.LENGTH_LONG).show();
        }
    }

    public void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getBaseContext(), getBaseContext().getString(R.string.logout_message), Toast.LENGTH_LONG).show();
                    }
                });
    }


}