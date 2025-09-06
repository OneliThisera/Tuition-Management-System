package com.example.tuitionmanagementapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminReportsActivity extends AppCompatActivity {

    private Button attendanceBtn, resultsBtn;
    private TextView reportView;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reports);

        // Initialize views
        attendanceBtn = findViewById(R.id.buttonShowAttendance);
        resultsBtn = findViewById(R.id.buttonShowResults);
        reportView = findViewById(R.id.textReportArea);

        dbHelper = new DatabaseHelper(this);

        // Set click listeners
        attendanceBtn.setOnClickListener(v -> showAttendanceReport());
        resultsBtn.setOnClickListener(v -> showResultsReport());
    }

    private void showAttendanceReport() {
        try {
            String report = dbHelper.getAllAttendanceRecords();
            reportView.setText(report);
        } catch (Exception e) {
            reportView.setText("Error loading attendance records");
            Toast.makeText(this, "Database error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showResultsReport() {
        try {
            String report = dbHelper.getAllResultRecords();
            reportView.setText(report);
        } catch (Exception e) {
            reportView.setText("Error loading result records");
            Toast.makeText(this, "Database error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}