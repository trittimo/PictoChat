package rosehulman.edu.pictochat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import rosehulman.edu.pictochat.R;
import rosehulman.edu.pictochat.util.Constants;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 1;


    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private OnCompleteListener mOnCompleteListener;
    private SharedPreferences mPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (mPreferences.contains(Constants.KEY_PREF_USER_ID)) {
            // Skip sign-in
            launchMainActivity();
        }

        final SignInButton signInButton = findViewById(R.id.sign_in_button);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        this.mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        this.mAuth = FirebaseAuth.getInstance();

        this.mOnCompleteListener = new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (!task.isSuccessful()) {
                    showLoginError();
                } else {
                    addDisplayName();
                    saveUid();
                    launchMainActivity();
                }
            }
        };


        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogin();
            }
        });
    }

    public void launchMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void saveUid() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(Constants.KEY_PREF_USER_ID, mAuth.getUid());

        String email = mAuth.getCurrentUser().getEmail();

        editor.putString(Constants.KEY_PREF_USER_EMAIL, email.substring(0, email.indexOf("@")));
        editor.commit();
    }

    public void addDisplayName() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultDisplayName = getString(R.string.no_name_given);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getUid());
        if (preferences.getString(SettingsActivity.KEY_PREF_DISPLAY_NAME, defaultDisplayName).equals(defaultDisplayName)) {
            database.child("display_name").setValue(mAuth.getCurrentUser().getDisplayName());
            database.child("email").setValue(mAuth.getCurrentUser().getEmail());
            String email = mAuth.getCurrentUser().getEmail();
            FirebaseDatabase.getInstance().getReference().child("user_map").child(email.substring(0, email.indexOf("@")))
                    .setValue(mAuth.getCurrentUser().getDisplayName());
        }
    }

    public void onLogin() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                mAuth.signInWithCredential(credential).addOnCompleteListener(this, mOnCompleteListener);
            } catch (ApiException e) {
                Log.w(Constants.TAG, "Google sign-in failed", e);
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        showLoginError();
    }

    public void showLoginError() {
        Snackbar.make(findViewById(R.id.login_activity), getString(R.string.login_failed), Snackbar.LENGTH_LONG).show();
    }
}

