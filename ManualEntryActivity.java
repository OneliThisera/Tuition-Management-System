package com.example.tuitionmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ManualEntryActivity extends AppCompatActivity {

    private EditText editStudentId, editCourse, editDate;
    private Spinner spinnerStatus;
    private Button buttonSubmit;
    private TextView textStatus;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);

        // Initialize views
        editStudentId = findViewById(R.id.editStudentId);
        editCourse = findViewById(R.id.editCourse);
        editDate = findViewById(R.id.editDate);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        textStatus = findViewById(R.id.textStatus);
        dbHelper = new DatabaseHelper(this);

        // Set current date as default
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        editDate.setText(currentDate);

        // Setup status spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.attendance_status_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

        // Submit button click handler
        buttonSubmit.setOnClickListener(v -> submitAttendance());
    }

    private void submitAttendance() {
        // Get input values
        String studentIdStr = editStudentId.getText().toString().trim();
        String course = editCourse.getText().toString().trim();
        String date = editDate.getText().toString().trim();
        String status = spinnerStatus.getSelectedItem().toString();

        // Validate inputs
        if (studentIdStr.isEmpty() || course.isEmpty() || date.isEmpty()) {
            textStatus.setText("Please fill all fields");
            textStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            return;
        }

        try {
            int studentId = Integer.parseInt(studentIdStr);

            // Validate date format (simple check)
            if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                textStatus.setText("Invalid date format (use YYYY-MM-DD)");
                textStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                return;
            }

            // Save to database
            boolean success = dbHelper.markAttendance(studentId, course, date, status);
            if (success) {
                textStatus.setText("Attendance recorded successfully");
                textStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

                // Update attendance page by refreshing it or navigating back with result
                updateAttendancePage();

                clearForm();
            } else {
                textStatus.setText("Failed to record attendance");
                textStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }
        } catch (NumberFormatException e) {
            textStatus.setText("Invalid student ID (must be a number)");
            textStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }
    }

    private void updateAttendancePage() {

        setResult(RESULT_OK);
        finish();

    }

    private void clearForm() {
        editCourse.setText("");
        editStudentId.setText("");
        // Keep the current date
        spinnerStatus.setSelection(0);
    }
}