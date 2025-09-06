package com.example.tuitionmanagementapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UploadMaterialActivity extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 1;
    private EditText teacherIdInput, courseInput, titleInput, descriptionInput;
    private Button uploadBtn;
    private TextView statusText;
    private DatabaseHelper dbHelper;
    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_material);

        teacherIdInput = findViewById(R.id.editTextTeacherId);
        courseInput = findViewById(R.id.editTextCourse);
        titleInput = findViewById(R.id.editTextTitle);
        descriptionInput = findViewById(R.id.editTextDescription);
        uploadBtn = findViewById(R.id.buttonUploadMaterial);
        statusText = findViewById(R.id.textMaterialStatus);

        dbHelper = new DatabaseHelper(this);

        uploadBtn.setOnClickListener(v -> {
            // Open file picker
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*"); // Allow all file types
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, PICK_FILE_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            fileUri = data.getData();

            // Get the form data
            int teacherId = Integer.parseInt(teacherIdInput.getText().toString());
            String course = courseInput.getText().toString();
            String title = titleInput.getText().toString();
            String description = descriptionInput.getText().toString();

            // Get the file path
            String filePath = fileUri.getPath();

            // Insert into database (modify your DatabaseHelper accordingly)
            boolean success = dbHelper.insertMaterial(teacherId, course, title, description, filePath);

            if (success) {
                statusText.setText("Material uploaded successfully.");
            } else {
                statusText.setText("Failed to upload material.");
            }
        }
    }
}