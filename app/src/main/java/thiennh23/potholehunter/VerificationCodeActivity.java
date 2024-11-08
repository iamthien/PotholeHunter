package thiennh23.potholehunter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class VerificationCodeActivity extends AppCompatActivity {

    private EditText block1, block2, block3, block4, block5;
    private Button btnConfirmVerifyCode;
    private TextView tvResend, tvResendCountdown, tvLogin;
    private boolean isResendEnabled = false;
    private final int RESEND_DELAY_MS = 30000; // 30 seconds delay

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verification_code);
        init();

        // Set up code input focus movement
        setCodeInputFocus();

        // Confirm button click listener
        btnConfirmVerifyCode.setOnClickListener(v -> {
            if (isCodeValid()) {
                Toast.makeText(this, "Verification code confirmed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(VerificationCodeActivity.this, HomeActivity.class)); // replace NextActivity with the next activity class
            } else {
                Toast.makeText(this, "Please enter a valid verification code", Toast.LENGTH_SHORT).show();
            }
        });

        // Resend TextView click listener
        tvResend.setOnClickListener(v -> {
            if (isResendEnabled) {
                startResendCountdown();
                Toast.makeText(this, "Verification code resent", Toast.LENGTH_SHORT).show();
                // Logic to resend code can be added here
            }
        });

        // Login TextView click listener
        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(VerificationCodeActivity.this, MainActivity.class));
        });

        // Start the resend countdown
        startResendCountdown();
    }

    private void init() {
        block1 = findViewById(R.id.block1);
        block2 = findViewById(R.id.block2);
        block3 = findViewById(R.id.block3);
        block4 = findViewById(R.id.block4);
        block5 = findViewById(R.id.block5);
        btnConfirmVerifyCode = findViewById(R.id.btnConfirmVerifyCode);
        tvResend = findViewById(R.id.tv_resend);
        tvResendCountdown = findViewById(R.id.tvResendCountdown);
        tvLogin = findViewById(R.id.tvLogin);
    }

    private void setCodeInputFocus() {
        // Add TextWatcher to each block to move focus automatically
        block1.addTextChangedListener(createTextWatcher(block1, block2));
        block2.addTextChangedListener(createTextWatcher(block2, block3));
        block3.addTextChangedListener(createTextWatcher(block3, block4));
        block4.addTextChangedListener(createTextWatcher(block4, block5));
        block5.addTextChangedListener(createTextWatcher(block5, null)); // No next EditText for the last block
    }

    private TextWatcher createTextWatcher(EditText currentBlock, EditText nextBlock) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1 && nextBlock != null) {
                    nextBlock.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 1) {
                    currentBlock.setText(String.valueOf(s.charAt(0))); // Keep only first character
                    if (nextBlock != null) nextBlock.requestFocus();
                }
            }
        };
    }


    private boolean isCodeValid() {
        String code = block1.getText().toString() + block2.getText().toString() +
                block3.getText().toString() + block4.getText().toString() +
                block5.getText().toString();
        return code.length() == 5; // Adjust validation logic as needed
    }

    private void startResendCountdown() {
        tvResend.setEnabled(false);
        tvResendCountdown.setVisibility(View.VISIBLE);
        isResendEnabled = false;

        new Handler().postDelayed(() -> {
            tvResendCountdown.setVisibility(View.GONE);
            tvResend.setEnabled(true);
            isResendEnabled = true;
            Toast.makeText(this, "You can now resend the code", Toast.LENGTH_SHORT).show();
        }, RESEND_DELAY_MS);
    }
}
