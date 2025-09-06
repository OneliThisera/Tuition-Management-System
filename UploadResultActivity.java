package com.example.tuitionmanagementapp;


import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tuitionmanagementapp.DatabaseHelper;

public class UploadResultActivity extends AppCompatActivity {

    EditText studentIdInput, courseInput, scoreInput;
    Button uploadBtn;
    TextView statusText;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_result);

        studentIdInput = findViewById(R.id.editTextStudentId);
        courseInput = findViewById(R.id.editTextCourse);
        scoreInput = findViewById(R.id.editTextScore);
        uploadBtn = findViewById(R.id.buttonUploadResults);
        statusText = findViewById(R.id.textResultsStatus);

        dbHelper = new DatabaseHelper(this);

        uploadBtn.setOnClickListener(v -> {
            int studentId = Integer.parseInt(studentIdInput.getText().toString());
            String course = courseInput.getText().toString();
            int score = Integer.parseInt(scoreInput.getText().toString());

            boolean success = dbHelper.insertResult(studentId, course, score);
            if (success) {
                statusText.setText("Result uploaded successfully.");
            } else {
                statusText.setText("Failed to upload result.");
            }
        });
    }

        }


