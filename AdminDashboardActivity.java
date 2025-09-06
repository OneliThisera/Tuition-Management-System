package com.example.tuitionmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    Button manageUsersBtn, assignStudentsBtn, viewReportsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Button initialization
        manageUsersBtn = findViewById(R.id.buttonManageUsers);
        assignStudentsBtn = findViewById(R.id.buttonAssignStudents);
        viewReportsBtn = findViewById(R.id.buttonViewReports);

        manageUsersBtn.setOnClickListener(v ->
                startActivity(new Intent(this, ManageUsersActivity.class)));

        assignStudentsBtn.setOnClickListener(v ->
                startActivity(new Intent(this, AssignStudentsActivity.class)));

        viewReportsBtn.setOnClickListener(v ->
                startActivity(new Intent(this, AdminReportsActivity.class)));

        ImageView imageLogo = findViewById(R.id.imageLogo);
        imageLogo.setImageResource(R.drawable.admin); // Set image programmatically

    }

}
