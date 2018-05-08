package rosehulman.edu.pictochat.firebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import rosehulman.edu.pictochat.R;
import rosehulman.edu.pictochat.util.Constants;

public class FirebaseLoginHelper implements GoogleApiClient.OnConnectionFailedListener, OnCompleteListener {
    private static final int RC_SIGN_IN = 1;

    private FirebaseLoginCallback mCallback;
    private FragmentActivity mActivity;

    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;

    public FirebaseLoginHelper(FragmentActivity activity, FirebaseLoginCallback callback) {
        this.mActivity = activity;
        this.mCallback = callback;
        this.mAuth = FirebaseAuth.getInstance();
    }

    public boolean isLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public void login() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mActivity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        this.mGoogleApiClient = new GoogleApiClient.Builder(mActivity).enableAutoManage(mActivity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        mActivity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void onActivityResult(int requestCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                mAuth.signInWithCredential(credential).addOnCompleteListener(mActivity, this);
            } catch (ApiException e) {
                Log.w(Constants.TAG, "Google sign-in failed", e);
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mCallback.onLoginFail(connectionResult.getErrorMessage());
    }

    @Override
    public void onComplete(@NonNull Task task) {
        if (task.isSuccessful()) {
            mCallback.onLoginSuccess();
        } else {
            mCallback.onLoginFail("Could not login");
        }
    }

    public interface FirebaseLoginCallback {
        void onLoginSuccess();
        void onLoginFail(String message);
    }

}
