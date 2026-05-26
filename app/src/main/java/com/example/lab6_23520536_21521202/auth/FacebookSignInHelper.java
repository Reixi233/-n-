package com.example.lab6_23520536_21521202.auth;

import android.app.Activity;
import android.content.Intent;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import java.util.Arrays;

public class FacebookSignInHelper {

    private final CallbackManager callbackManager;
    private final Activity activity;

    public FacebookSignInHelper(Activity activity) {
        this.activity = activity;
        this.callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                // User canceled
            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
            }
        });
    }

    public void signIn() {
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("email", "public_profile"));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(String token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token);
        ((LoginActivity) activity).firebaseAuthWithFacebook(credential);
    }
}