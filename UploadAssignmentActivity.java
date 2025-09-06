package com.example.tuitionmanagementapp;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tuitionmanagementapp.DatabaseHelper;

public class UploadAssignmentActivity extends AppCompatActivity {

    EditText teacherIdInput, courseInput, titleInput, descriptionInput, dueDateInput;
    Button uploadBtn;
    TextView statusText;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_assignment);

        teacherIdInput = findViewById(R.id.editTextTeacherId);
        courseInput = findViewById(R.id.editTextCourse);
        titleInput = findViewById(R.id.editTextTitle);
        descriptionInput = findViewById(R.id.editTextDescription);
        dueDateInput = findViewById(R.id.editTextDueDate);
        uploadBtn = findViewById(R.id.buttonUploadAssignment);
        statusText = findViewById(R.id.textAssignmentStatus);

        dbHelper = new DatabaseHelper(this);

        uploadBtn.setOnClickListener(v -> {
            int teacherId = Integer.parseInt(teacherIdInput.getText().toString());
            String course = courseInput.getText().toString();
            String title = titleInput.getText().toString();
            String description = descriptionInput.getText().toString();
            String dueDate = dueDateInput.getText().toString();

            boolean success = dbHelper.insertAssignment(teacherId, course, title, description, dueDate);
            if (success) {
                statusText.setText("Assignment uploaded successfully.");
            } else {
                statusText.setText("Failed to upload assignment.");
            }
        });
    }

}
