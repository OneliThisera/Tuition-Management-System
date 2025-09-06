package com.example.tuitionmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText emailInput, passwordInput;
    Button loginBtn, goToRegisterBtn;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // View initialization
        emailInput = findViewById(R.id.editTextEmail);
        passwordInput = findViewById(R.id.editTextPassword);
        loginBtn = findViewById(R.id.buttonLogin);
        goToRegisterBtn = findViewById(R.id.buttonRegister);

        dbHelper = new DatabaseHelper(this);

        // Handle login
        loginBtn.setOnClickListener(v -> {
            try {
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                String role = dbHelper.checkUser(email, password);

                if (role != null) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();

                    switch (role) {
                        case "Admin":
                            startActivity(new Intent(this, AdminDashboardActivity.class));
                            break;
                        case "Teacher":
                            startActivity(new Intent(this, TeacherDashboardActivity.class));
                            break;
                        case "Student":
                            startActivity(new Intent(this, StudentDashboardActivity.class));
                            break;
                        default:
                            Toast.makeText(this, "Unknown role", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                } else {
                    Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Login failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        goToRegisterBtn.setOnClickListener(v -> {
            try {
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        ImageView imageLogo = findViewById(R.id.imageLogo);
        imageLogo.setImageResource(R.drawable.login);
    }
}