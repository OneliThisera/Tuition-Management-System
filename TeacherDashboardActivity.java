package com.example.tuitionmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class TeacherDashboardActivity extends AppCompatActivity {

    Button attendanceBtn, assignmentBtn, resultBtn, materialBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

        attendanceBtn = findViewById(R.id.buttonAttendance);
        assignmentBtn = findViewById(R.id.buttonAssignments);
        resultBtn = findViewById(R.id.buttonResults);
        materialBtn = findViewById(R.id.buttonMaterials);

        attendanceBtn.setOnClickListener(v ->
                startActivity(new Intent(this, QRScanAttendanceActivity.class)));

        assignmentBtn.setOnClickListener(v ->
                startActivity(new Intent(this, UploadAssignmentActivity.class)));

        resultBtn.setOnClickListener(v ->
                startActivity(new Intent(this, UploadResultActivity.class)));

        materialBtn.setOnClickListener(v ->
                startActivity(new Intent(this, UploadMaterialActivity.class)));

        ImageView imageLogo = findViewById(R.id.imageLogo);
        imageLogo.setImageResource(R.drawable.student); // Set image programmatically

    }
}
