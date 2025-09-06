package com.example.tuitionmanagementapp;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tuitionmanagementapp.DatabaseHelper;

public class AssignStudentsActivity extends AppCompatActivity {

    EditText studentIdInput, teacherIdInput, courseInput;
    Button assignBtn;
    TextView statusText;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_students);

        studentIdInput = findViewById(R.id.editTextStudentId);
        teacherIdInput = findViewById(R.id.editTextTeacherId);
        courseInput = findViewById(R.id.editTextCourseName);
        assignBtn = findViewById(R.id.buttonAssign);
        statusText = findViewById(R.id.textAssignStatus);

        dbHelper = new DatabaseHelper(this);

        assignBtn.setOnClickListener(v -> {
            try {
                int studentId = Integer.parseInt(studentIdInput.getText().toString());
                int teacherId = Integer.parseInt(teacherIdInput.getText().toString());
                String course = courseInput.getText().toString();

                if (course.isEmpty()) {
                    statusText.setText("Please enter a course name");
                    return;
                }

                boolean success = dbHelper.assignStudentToCourse(studentId, teacherId, course);
                if (success) {
                    statusText.setText("Student assigned successfully to " + course);
                    // Clear the input fields for new assignments
                    studentIdInput.setText("");
                    teacherIdInput.setText("");
                    courseInput.setText("");
                    // Set focus back to student ID field
                    studentIdInput.requestFocus();
                } else {
                    statusText.setText("Assignment failed. Please check the IDs.");
                }
            } catch (NumberFormatException e) {
                statusText.setText("Please enter valid numeric IDs");
            }
        });
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}