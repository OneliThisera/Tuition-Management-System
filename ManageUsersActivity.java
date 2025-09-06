package com.example.tuitionmanagementapp;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tuitionmanagementapp.DatabaseHelper;

public class ManageUsersActivity extends AppCompatActivity {

    EditText nameInput, emailInput, passwordInput;
    Spinner roleSpinner;
    Button addUserBtn, updateUserBtn, deleteUserBtn;
    TextView userListText;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);


        nameInput = findViewById(R.id.editTextName);
        emailInput = findViewById(R.id.editTextEmail);
        passwordInput = findViewById(R.id.editTextPassword);
        roleSpinner = findViewById(R.id.spinnerRole);
        addUserBtn = findViewById(R.id.buttonAddUser);
        updateUserBtn = findViewById(R.id.buttonUpdateUser);
        deleteUserBtn = findViewById(R.id.buttonDeleteUser);
        userListText = findViewById(R.id.textUserList);

        dbHelper = new DatabaseHelper(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        addUserBtn.setOnClickListener(v -> {
            String name = nameInput.getText().toString();
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();
            String role = roleSpinner.getSelectedItem().toString();

            boolean inserted = dbHelper.insertUser(name, email, password, role);
            if (inserted) {
                Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show();
                showAllUsers();
            } else {
                Toast.makeText(this, "Error adding user", Toast.LENGTH_SHORT).show();
            }
        });

        updateUserBtn.setOnClickListener(v -> {
            String name = nameInput.getText().toString();
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();
            String role = roleSpinner.getSelectedItem().toString();

            boolean updated = dbHelper.updateUser(name, email, password, role);
            if (updated) {
                Toast.makeText(this, "User updated successfully", Toast.LENGTH_SHORT).show();
                showAllUsers();
            } else {
                Toast.makeText(this, "Error updating user", Toast.LENGTH_SHORT).show();
            }
        });

        deleteUserBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean deleted = dbHelper.deleteUser(email);
            if (deleted) {
                Toast.makeText(this, "User deleted successfully", Toast.LENGTH_SHORT).show();
                showAllUsers(); // Refresh user list
            } else {
                Toast.makeText(this, "Error deleting user", Toast.LENGTH_SHORT).show();
            }
        });


        showAllUsers(); // show existing users when page loads
    }

    private void showAllUsers() {
        String all = dbHelper.getAllUsers();
        userListText.setText(all);
    }
}
