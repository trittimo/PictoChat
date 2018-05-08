package rosehulman.edu.pictochat.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.gms.common.SignInButton;

import rosehulman.edu.pictochat.R;
import rosehulman.edu.pictochat.firebase.FirebaseLoginHelper;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements FirebaseLoginHelper.FirebaseLoginCallback {


    private FirebaseLoginHelper mLoginHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.mLoginHelper = new FirebaseLoginHelper(this, this);

        if (mLoginHelper.isLoggedIn()) {
            launchMainActivity();
            return;
        }

        final SignInButton signInButton = findViewById(R.id.sign_in_button);

        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoginHelper.login();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mLoginHelper.onActivityResult(requestCode, data);
    }

    public void launchMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginSuccess() {
        launchMainActivity();
    }

    @Override
    public void onLoginFail(String message) {
        if (message == null) {
            Snackbar.make(findViewById(R.id.login_activity), getString(R.string.login_failed), Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(findViewById(R.id.login_activity), message, Snackbar.LENGTH_LONG).show();
        }
    }
}

