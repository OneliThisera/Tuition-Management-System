package com.example.tuitionmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRScanAttendanceActivity extends AppCompatActivity {

    private TextView statusText;
    private LinearLayout resultLayout;
    private TextView textStudentId, textCourse, textDate, textStatus;
    private Button buttonScan, buttonManualEntry;
    private DatabaseHelper dbHelper;
    private IntentIntegrator integrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        // Initialize views
        statusText = findViewById(R.id.textScanStatus);
        resultLayout = findViewById(R.id.layoutResult);
        textStudentId = findViewById(R.id.textStudentId);
        textCourse = findViewById(R.id.textCourse);
        textDate = findViewById(R.id.textDate);
        textStatus = findViewById(R.id.textStatus);
        buttonScan = findViewById(R.id.buttonScan);
        buttonManualEntry = findViewById(R.id.buttonManualEntry);

        dbHelper = new DatabaseHelper(this);
        integrator = new IntentIntegrator(this);

        buttonScan.setOnClickListener(v -> startScan());
        buttonManualEntry.setOnClickListener(v -> {
            // Launch manual entry activity
            startActivity(new Intent(this, ManualEntryActivity.class));
        });
    }

    private void startScan() {
        integrator.setPrompt("Scan Student QR Code");
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                statusText.setText("Scan cancelled");
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_SHORT).show();
            } else {
                processScannedData(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void processScannedData(String scannedData) {
        statusText.setText("Scanned: " + scannedData);
        String[] parts = scannedData.split(",");

        if (parts.length == 3) {
            try {
                int studentId = Integer.parseInt(parts[0].trim());
                String course = parts[1].trim();
                String date = parts[2].trim();

                // Display the scanned data
                resultLayout.setVisibility(View.VISIBLE);
                textStudentId.setText("Student ID: " + studentId);
                textCourse.setText("Course: " + course);
                textDate.setText("Date: " + date);

                // Record attendance
                boolean success = dbHelper.markAttendance(studentId, course, date, "Present");
                if (success) {
                    textStatus.setText("Status: Attendance recorded successfully");
                    textStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                } else {
                    textStatus.setText("Status: Failed to record attendance");
                    textStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                }

                Toast.makeText(this, "Attendance processed", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                showError("Invalid student ID format");
            }
        } else {
            showError("Invalid QR format. Expected: studentId,course,date");
        }
    }

    private void showError(String message) {
        statusText.setText(message);
        resultLayout.setVisibility(View.GONE);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}