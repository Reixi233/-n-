package com.example.lab6_23520536_21521202.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lab6_23520536_21521202.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private GoogleSignInHelper googleSignInHelper;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // KHỞI TẠO FIREBASE - RẤT QUAN TRỌNG
        FirebaseApp.initializeApp(this);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        googleSignInHelper = new GoogleSignInHelper(this);

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
                            Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_LONG).show();
                        }
                    });
        });

        binding.btnGoogle.setOnClickListener(v -> {
            Intent signInIntent = googleSignInHelper.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInAccount account = GoogleSignInHelper.getAccountFromIntent(data);
            if (account != null) {
                // SỬA LỖI Ở ĐÂY: Phải nộp "vé" Google (idToken) cho Firebase
                firebaseAuthWithGoogle(account.getIdToken());
            } else {
                Toast.makeText(this, "Không lấy được tài khoản Google!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // HÀM MỚI BỔ SUNG: Dùng Token của Google để đăng nhập vào hệ thống Firebase
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Firebase đã ghi nhận thành công, bây giờ mới chuyển trang
                        Toast.makeText(this, "Đăng nhập Firebase thành công!", Toast.LENGTH_SHORT).show();
                        goToMainActivity();
                    } else {
                        Toast.makeText(this, "Lỗi đăng nhập Firebase: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, com.example.lab6_23520536_21521202.avtivity.MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}