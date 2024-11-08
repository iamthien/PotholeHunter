package thiennh23.potholehunter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText edtEmail;
    private Button btnConfirm;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        init();

        // Confirm button
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ForgotPasswordActivity.this, "Button confirm clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ForgotPasswordActivity.this, CreatePasswordActivity.class));

            }
        });

        // Login textview
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ForgotPasswordActivity.this, "Confirm button clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ForgotPasswordActivity.this, MainActivity.class));

            }
        });

    }

    void init() {
        // Find views by ID
        edtEmail = findViewById(R.id.edtEmail);
        btnConfirm = findViewById(R.id.btnConfirm);
        tvLogin = findViewById(R.id.tvLogin);
    }
}