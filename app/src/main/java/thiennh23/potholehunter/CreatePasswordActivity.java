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
import androidx.core.content.ContextCompat;

public class CreatePasswordActivity extends AppCompatActivity {

    private EditText edtPassword, edtRePassword;
    private Button btnConfirmCreatePassword;
    private TextView tvLogin;
    private boolean isPasswordVisible1 = false, isPasswordVisible2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_password);
        init();

        // Set up password visibility toggle
        setToggleVisibilityListener(edtPassword, () -> {
            isPasswordVisible1 = !isPasswordVisible1;
            togglePasswordVisibility(edtPassword, isPasswordVisible1);
        });

        setToggleVisibilityListener(edtRePassword, () -> {
            isPasswordVisible2 = !isPasswordVisible2;
            togglePasswordVisibility(edtRePassword, isPasswordVisible2);
        });

        // Confirm button click
        btnConfirmCreatePassword.setOnClickListener(v -> {
            if (validatePasswords()) {
                Toast.makeText(CreatePasswordActivity.this, "Password created successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CreatePasswordActivity.this, VerificationCodeActivity.class));
            }
        });

        // TextView Login click
        tvLogin.setOnClickListener(v -> {
            Toast.makeText(CreatePasswordActivity.this, "Redirecting to Login", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(CreatePasswordActivity.this, MainActivity.class));
        });
    }

    private void init() {
        edtPassword = findViewById(R.id.edtPassword);
        edtRePassword = findViewById(R.id.edtRePassword);
        btnConfirmCreatePassword = findViewById(R.id.btnConfirmCreatePassword);
        tvLogin = findViewById(R.id.tvLogin);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setToggleVisibilityListener(EditText editText, Runnable toggleAction) {
        editText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                int drawableEnd = 2; // right drawable index
                if (event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[drawableEnd].getBounds().width())) {
                    toggleAction.run();
                    return true;
                }
            }
            return false;
        });
    }

    private void togglePasswordVisibility(EditText editText, boolean isVisible) {
        int cursorPosition = editText.getSelectionStart();
        if (isVisible) {
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            editText.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.visibility_on), null);
        } else {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            editText.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.visibility_off), null);
        }
        editText.setSelection(cursorPosition);
    }

    private boolean validatePasswords() {
        String password = edtPassword.getText().toString().trim();
        String rePassword = edtRePassword.getText().toString().trim();

        // Check if password meets minimum length requirement
        if (password.length() < 6) {
            Toast.makeText(CreatePasswordActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if passwords match
        if (!password.equals(rePassword)) {
            Toast.makeText(CreatePasswordActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
