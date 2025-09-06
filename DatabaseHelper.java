package com.example.tuitionmanagementapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tuitionmanagementapp.model.AttendanceRecord;

import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "tuition_management.db";
    public static final int DATABASE_VERSION = 1;

    // Users table constants
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_ROLE = "role";

    // Feedback table constants
    public static final String TABLE_FEEDBACK = "feedback";
    public static final String COLUMN_FEEDBACK_ID = "id";
    public static final String COLUMN_STUDENT_ID = "student_id";
    public static final String COLUMN_COURSE = "course";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DIFFICULTY = "difficulty";
    public static final String COLUMN_SATISFACTION = "satisfaction";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_ACHIEVEMENT_TYPE = "achievement_type";
    public static final String COLUMN_IS_ANONYMOUS = "is_anonymous";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_NAME + " TEXT, " +
                COLUMN_USER_EMAIL + " TEXT UNIQUE, " +
                COLUMN_USER_PASSWORD + " TEXT, " +
                COLUMN_USER_ROLE + " TEXT)");

        // Create other tables (maintaining your existing structure)
        db.execSQL("CREATE TABLE attendance (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id INTEGER, " +
                "course TEXT, " +
                "date TEXT, " +
                "status TEXT)");

        db.execSQL("CREATE TABLE assignments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "teacher_id INTEGER, " +
                "course TEXT, " +
                "title TEXT, " +
                "description TEXT, " +
                "due_date TEXT)");

        db.execSQL("CREATE TABLE submissions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "assignment_id INTEGER, " +
                "student_id INTEGER, " +
                "file_path TEXT)");

        db.execSQL("CREATE TABLE results (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id INTEGER, " +
                "course TEXT, " +
                "score INTEGER)");

        db.execSQL("CREATE TABLE materials (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "teacher_id INTEGER, " +
                "course TEXT, " +
                "title TEXT, " +
                "file_path TEXT)");

        db.execSQL("CREATE TABLE notifications (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "sender_id INTEGER, " +
                "receiver_id INTEGER, " +
                "message TEXT, " +
                "date TEXT)");

        db.execSQL("CREATE TABLE assignments_admin (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id INTEGER, " +
                "teacher_id INTEGER, " +
                "course TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS submitted_assignments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id INTEGER, " +
                "course TEXT, " +
                "submission TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS notifications (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "message TEXT, " +
                "sender TEXT, " +
                "date TEXT)");

        // Create feedback table
        db.execSQL("CREATE TABLE " + TABLE_FEEDBACK + "(" +
                COLUMN_FEEDBACK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_STUDENT_ID + " INTEGER, " +
                COLUMN_COURSE + " TEXT, " +
                COLUMN_TITLE + " TEXT NOT NULL, " +
                COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                COLUMN_DIFFICULTY + " REAL NOT NULL CHECK(" + COLUMN_DIFFICULTY + " >= 1 AND " +
                COLUMN_DIFFICULTY + " <= 5), " +
                COLUMN_SATISFACTION + " REAL NOT NULL CHECK(" + COLUMN_SATISFACTION + " >= 1 AND " +
                COLUMN_SATISFACTION + " <= 5), " +
                COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                COLUMN_ACHIEVEMENT_TYPE + " TEXT, " +
                COLUMN_IS_ANONYMOUS + " INTEGER DEFAULT 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Create the new feedback table if upgrading from version 1
            db.execSQL("CREATE TABLE " + TABLE_FEEDBACK + "(" +
                    COLUMN_FEEDBACK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_STUDENT_ID + " INTEGER, " +
                    COLUMN_COURSE + " TEXT, " +
                    COLUMN_TITLE + " TEXT NOT NULL, " +
                    COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                    COLUMN_DIFFICULTY + " REAL NOT NULL CHECK(" + COLUMN_DIFFICULTY + " >= 1 AND " +
                    COLUMN_DIFFICULTY + " <= 5), " +
                    COLUMN_SATISFACTION + " REAL NOT NULL CHECK(" + COLUMN_SATISFACTION + " >= 1 AND " +
                    COLUMN_SATISFACTION + " <= 5), " +
                    COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    COLUMN_ACHIEVEMENT_TYPE + " TEXT, " +
                    COLUMN_IS_ANONYMOUS + " INTEGER DEFAULT 0)");
        }

        // Drop all tables and recreate them (your existing approach)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS attendance");
        db.execSQL("DROP TABLE IF EXISTS assignments");
        db.execSQL("DROP TABLE IF EXISTS submissions");
        db.execSQL("DROP TABLE IF EXISTS results");
        db.execSQL("DROP TABLE IF EXISTS materials");
        db.execSQL("DROP TABLE IF EXISTS notifications");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDBACK);
        onCreate(db);
    }

    // ================= USER MANAGEMENT METHODS ================= //

    /**
     * Insert a new user into the database
     */
    public boolean insertUser(String name, String email, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PASSWORD, password);
        values.put(COLUMN_USER_ROLE, role);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    /**
     * Check if a user exists with the given email and password
     * @return The user's role if found, null otherwise
     */
    public String checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_USER_ROLE},
                COLUMN_USER_EMAIL + " = ? AND " + COLUMN_USER_PASSWORD + " = ?",
                new String[]{email, password},
                null, null, null);

        if (cursor.moveToFirst()) {
            String role = cursor.getString(0);
            cursor.close();
            db.close();
            return role;
        }

        cursor.close();
        db.close();
        return null;
    }

    /**
     * Check if an email already exists in the database
     */
    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_USER_ID},
                COLUMN_USER_EMAIL + " = ?",
                new String[]{email},
                null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    /**
     * Get all users from the database
     */
    public String getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
        StringBuilder sb = new StringBuilder();

        while (cursor.moveToNext()) {
            sb.append("ID: ").append(cursor.getInt(0)).append("\n");
            sb.append("Name: ").append(cursor.getString(1)).append("\n");
            sb.append("Email: ").append(cursor.getString(2)).append("\n");
            sb.append("Role: ").append(cursor.getString(4)).append("\n\n");
        }

        cursor.close();
        db.close();
        return sb.toString();
    }

    /**
     * Update user information
     */
    public boolean updateUser(String name, String email, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_PASSWORD, password);
        values.put(COLUMN_USER_ROLE, role);

        int rows = db.update(TABLE_USERS, values,
                COLUMN_USER_EMAIL + " = ?", new String[]{email});
        db.close();
        return rows > 0;
    }

    /**
     * Delete a user from the database
     */
    public boolean deleteUser(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("Users", "email = ?", new String[]{email});
        return result > 0;
    }


    // ================= FEEDBACK METHODS ================= //

    public long insertFeedback(int studentId, String course, String title, String description,
                               float difficulty, float satisfaction, String achievementType,
                               boolean isAnonymous) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_STUDENT_ID, studentId);
        values.put(COLUMN_COURSE, course);
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_DIFFICULTY, difficulty);
        values.put(COLUMN_SATISFACTION, satisfaction);
        values.put(COLUMN_ACHIEVEMENT_TYPE, achievementType);
        values.put(COLUMN_IS_ANONYMOUS, isAnonymous ? 1 : 0);

        long id = db.insert(TABLE_FEEDBACK, null, values);
        db.close();
        return id;
    }

    public Cursor getFeedbackByStudent(int studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_FEEDBACK,
                null,
                COLUMN_STUDENT_ID + " = ?",
                new String[]{String.valueOf(studentId)},
                null, null,
                COLUMN_TIMESTAMP + " DESC");
    }

    public Cursor getFeedbackByCourse(String course) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_FEEDBACK,
                null,
                COLUMN_COURSE + " = ?",
                new String[]{course},
                null, null,
                COLUMN_TIMESTAMP + " DESC");
    }

    public Cursor getAllFeedback() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_FEEDBACK,
                null,
                null,
                null,
                null, null,
                COLUMN_TIMESTAMP + " DESC");
    }

    public float[] getAverageRatingsForCourse(String course) {
        SQLiteDatabase db = this.getReadableDatabase();
        float[] averages = new float[2];

        Cursor cursor = db.rawQuery(
                "SELECT AVG(" + COLUMN_DIFFICULTY + "), AVG(" + COLUMN_SATISFACTION + ") " +
                        "FROM " + TABLE_FEEDBACK + " WHERE " + COLUMN_COURSE + " = ?",
                new String[]{course});

        if (cursor.moveToFirst()) {
            averages[0] = cursor.getFloat(0);
            averages[1] = cursor.getFloat(1);
        }
        cursor.close();
        db.close();
        return averages;
    }

    public boolean deleteFeedback(int feedbackId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_FEEDBACK,
                COLUMN_FEEDBACK_ID + " = ?",
                new String[]{String.valueOf(feedbackId)});
        db.close();
        return rows > 0;
    }

    // ================= OTHER METHODS (maintaining your existing functionality) ================= //

    public boolean assignStudentToCourse(int studentId, int teacherId, String course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("student_id", studentId);
        values.put("teacher_id", teacherId);
        values.put("course", course);
        long result = db.insert("assignments_admin", null, values);
        db.close();
        return result != -1;
    }

    // Add these methods to your DatabaseHelper.java


    public String getAllAttendanceRecords() {
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder sb = new StringBuilder();

        Cursor cursor = db.rawQuery("SELECT * FROM attendance ORDER BY date DESC", null);

        if (cursor.moveToFirst()) {
            do {
                sb.append("Student ID: ").append(cursor.getInt(1)).append("\n");
                sb.append("Course: ").append(cursor.getString(2)).append("\n");
                sb.append("Date: ").append(cursor.getString(3)).append("\n");
                sb.append("Status: ").append(cursor.getString(4)).append("\n\n");
            } while (cursor.moveToNext());
        } else {
            sb.append("No attendance records found");
        }

        cursor.close();
        db.close();
        return sb.toString();
    }

//  ATTENDANCE METHODS

    public boolean markAttendance(int studentId, String course, String date, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // First check if the attendance already exists
        if (isAttendanceRecorded(studentId, course, date)) {
            // Update existing record
            values.put("status", status);
            int rowsAffected = db.update("attendance", values,
                    "student_id = ? AND course = ? AND date = ?",
                    new String[]{String.valueOf(studentId), course, date});
            db.close();
            return rowsAffected > 0;
        } else {
            // Insert new record
            values.put("student_id", studentId);
            values.put("course", course);
            values.put("date", date);
            values.put("status", status);

            long result = db.insert("attendance", null, values);
            db.close();
            return result != -1;
        }
    }

    public boolean isAttendanceRecorded(int studentId, String course, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT 1 FROM attendance WHERE student_id = ? AND course = ? AND date = ?",
                new String[]{String.valueOf(studentId), course, date});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public Cursor getStudentAttendance(int studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("attendance",
                null,
                "student_id = ?",
                new String[]{String.valueOf(studentId)},
                null, null,
                "date DESC"); // Sort by date descending
    }

    public Cursor getCourseAttendance(String course) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("attendance",
                null,
                "course = ?",
                new String[]{course},
                null, null,
                "date DESC");
    }


    public Cursor getDateAttendance(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("attendance",
                null,
                "date = ?",
                new String[]{date},
                null, null,
                "student_id ASC"); // Sort by student ID
    }


    public int[] getAttendanceSummary(int studentId, String course) {
        SQLiteDatabase db = this.getReadableDatabase();
        int[] summary = new int[4];

        // Total classes
        Cursor totalCursor = db.rawQuery(
                "SELECT COUNT(*) FROM attendance WHERE student_id = ? AND course = ?",
                new String[]{String.valueOf(studentId), course});
        if (totalCursor.moveToFirst()) summary[0] = totalCursor.getInt(0);
        totalCursor.close();

        // Present
        Cursor presentCursor = db.rawQuery(
                "SELECT COUNT(*) FROM attendance WHERE student_id = ? AND course = ? AND status = 'Present'",
                new String[]{String.valueOf(studentId), course});
        if (presentCursor.moveToFirst()) summary[1] = presentCursor.getInt(0);
        presentCursor.close();

        // Absent
        Cursor absentCursor = db.rawQuery(
                "SELECT COUNT(*) FROM attendance WHERE student_id = ? AND course = ? AND status = 'Absent'",
                new String[]{String.valueOf(studentId), course});
        if (absentCursor.moveToFirst()) summary[2] = absentCursor.getInt(0);
        absentCursor.close();

        // Late
        Cursor lateCursor = db.rawQuery(
                "SELECT COUNT(*) FROM attendance WHERE student_id = ? AND course = ? AND status = 'Late'",
                new String[]{String.valueOf(studentId), course});
        if (lateCursor.moveToFirst()) summary[3] = lateCursor.getInt(0);
        lateCursor.close();

        db.close();
        return summary;
    }




    public int bulkImportAttendance(List<AttendanceRecord> records) {
        SQLiteDatabase db = this.getWritableDatabase();
        int successCount = 0;

        try {
            db.beginTransaction();

            for (AttendanceRecord record : records) {
                ContentValues values = new ContentValues();
                values.put("student_id", record.getStudentId());
                values.put("course", record.getCourse());
                values.put("date", record.getDate());
                values.put("status", record.getStatus());

                // Insert or update existing record
                if (isAttendanceRecorded(record.getStudentId(), record.getCourse(), record.getDate())) {
                    db.update("attendance", values,
                            "student_id = ? AND course = ? AND date = ?",
                            new String[]{
                                    String.valueOf(record.getStudentId()),
                                    record.getCourse(),
                                    record.getDate()
                            });
                } else {
                    db.insert("attendance", null, values);
                }
                successCount++;
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }

        return successCount;
    }

    public String getAllResultRecords() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM results", null);
        StringBuilder sb = new StringBuilder();
        while (cursor.moveToNext()) {
            sb.append("Student ID: ").append(cursor.getInt(1)).append("\n");
            sb.append("Course: ").append(cursor.getString(2)).append("\n");
            sb.append("Score: ").append(cursor.getInt(3)).append("\n\n");
        }
        cursor.close();
        db.close();
        return sb.toString();
    }

    public String getAttendanceData(int studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder data = new StringBuilder();

        Cursor cursor = db.rawQuery(
                "SELECT course, date, status FROM attendance WHERE student_id = ? ORDER BY date DESC",
                new String[]{String.valueOf(studentId)}
        );

        if (cursor.moveToFirst()) {
            do {
                String course = cursor.getString(0);
                String date = cursor.getString(1);
                String status = cursor.getString(2);

                data.append("Course: ").append(course)
                        .append(", Date: ").append(date)
                        .append(", Status: ").append(status)
                        .append("\n\n");
            } while (cursor.moveToNext());
        }

        cursor.close();
        return data.toString().trim();
    }

    public boolean insertAssignment(int teacherId, String course, String title, String description, String dueDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("teacher_id", teacherId);
        values.put("course", course);
        values.put("title", title);
        values.put("description", description);
        values.put("due_date", dueDate);
        long result = db.insert("assignments", null, values);
        db.close();
        return result != -1;
    }

    public boolean insertResult(int studentId, String course, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("student_id", studentId);
        values.put("course", course);
        values.put("score", score);
        long result = db.insert("results", null, values);
        db.close();
        return result != -1;
    }


    public boolean insertMaterial(int teacherId, String course, String title, String description, String filePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("teacher_id", teacherId);
        values.put("course", course);
        values.put("title", title);
        values.put("description", description);
        values.put("file_path", filePath);

        long result = db.insert("materials", null, values);
        return result != -1;
    }

    public Cursor getAllAssignments() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM assignments", null);
    }

    public Cursor getAttendanceByStudent(int studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM attendance WHERE student_id = ?",
                new String[]{String.valueOf(studentId)});
    }

    public boolean submitAssignment(int studentId, String course, String submission) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("student_id", studentId);
        values.put("course", course);
        values.put("submission", submission);
        long result = db.insert("submitted_assignments", null, values);
        db.close();
        return result != -1;
    }

    public Cursor getResultsByStudent(int studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM results WHERE student_id = ?",
                new String[]{String.valueOf(studentId)});
    }

    public Cursor getAllMaterials() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM materials", null);
    }


    public Cursor getAllNotifications() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(
                "notifications",
                new String[]{"message", "sender", "date"},
                null, null, null, null,
                "date DESC"
        );
    }
}