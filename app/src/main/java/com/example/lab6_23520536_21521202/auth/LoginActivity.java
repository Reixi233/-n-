package com.example.lab6_23520536_21521202.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lab6_23520536_21521202.databinding.ActivityLoginBinding;
import com.example.lab6_23520536_21521202.avtivity.MainActivity;           // ← Import này
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private GoogleSignInHelper googleSignInHelper;
    private FacebookSignInHelper facebookSignInHelper;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        googleSignInHelper = new GoogleSignInHelper(this);
        facebookSignInHelper = new FacebookSignInHelper(this);

        setupClickListeners();
        checkCurrentUser();
    }

    private void checkCurrentUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            goToMainActivity();
        }
    }

    private void setupClickListeners() {
        // Đăng nhập Email
        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            goToMainActivity();
                        } else {
                            Toast.makeText(this, "Đăng nhập thất bại: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // Google Login
        binding.btnGoogle.setOnClickListener(v -> {
            Intent signInIntent = googleSignInHelper.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

        // Facebook Login
        binding.btnFacebook.setOnClickListener(v -> {
            facebookSignInHelper.signIn();
        });

        // Chuyển sang Đăng ký
        binding.tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookSignInHelper.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInAccount account = GoogleSignInHelper.getAccountFromIntent(data);
            if (account != null) {
                firebaseAuthWithGoogle(account.getIdToken());
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        signInWithCredential(credential, "Google");
    }

    public void firebaseAuthWithFacebook(AuthCredential credential) {
        signInWithCredential(credential, "Facebook");
    }

    private void signInWithCredential(AuthCredential credential, String provider) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, provider + " đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        goToMainActivity();
                    } else {
                        Toast.makeText(this, "Lỗi " + provider + ": " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}