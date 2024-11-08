package thiennh23.potholehunter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private EditText edtUsername, edtEmail, edtPhone, edtPassword;
    private Button btnSignup;
    private TextView tvLogin;
    private boolean isPasswordVisible = false;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        initializeViews();
        setUpPasswordVisibilityToggle();
        setUpSignupButton();
        setUpLoginLink();
    }

    private void initializeViews() {
        // Initialize all view elements
        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignup = findViewById(R.id.btnSignup);
        tvLogin = findViewById(R.id.tvLogin);
    }

    private void setUpPasswordVisibilityToggle() {
        // Toggle visibility of password on drawable touch
        edtPassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int drawableEnd = 2; // Right drawable index
                if (event.getRawX() >= (edtPassword.getRight() - edtPassword.getCompoundDrawables()[drawableEnd].getBounds().width())) {
                    togglePasswordVisibility();
                    return true;
                }
            }
            return false;
        });
    }

    private void setUpSignupButton() {
        // Handle sign-up button click
        btnSignup.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SignupActivity.this, "Signing up...", Toast.LENGTH_SHORT).show();
                // Proceed with sign-up logic
            }
        });
    }

    private void setUpLoginLink() {
        // Handle login link click
        tvLogin.setOnClickListener(v -> {
            Toast.makeText(SignupActivity.this, "Log in clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void togglePasswordVisibility() {
        // Toggle password visibility and update drawable
        int cursorPosition = edtPassword.getSelectionStart();

        if (isPasswordVisible) {
            edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            edtPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.visibility_off, null), null);
            isPasswordVisible = false;
        } else {
            edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            edtPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.visibility_on, null), null);
            isPasswordVisible = true;
        }

        edtPassword.setSelection(cursorPosition); // Restore cursor position
    }
}
