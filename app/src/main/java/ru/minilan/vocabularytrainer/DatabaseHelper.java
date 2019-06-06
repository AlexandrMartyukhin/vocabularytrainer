package ru.minilan.vocabularytrainer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "vt_database.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Words";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_WORD1 = "Word1";
    private static final String COLUMN_WORD2 = "Word2";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_WORD1 + " TEXT," + COLUMN_WORD2 + " TEXT);";

    private static DatabaseHelper instance;
    private Context context;

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());

        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public ArrayList<WordCard> query() {
        String[] array = {COLUMN_ID, COLUMN_WORD1, COLUMN_WORD2};
        ArrayList<WordCard> words = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_NAME,
                array,
                null,
                null,
                null,
                null,
                null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    WordCard wordCard = new WordCard();
                    wordCard.setId(cursor.getLong(0));
                    wordCard.setWord1(cursor.getString(1));
                    wordCard.setWord2(cursor.getString(2));
                    words.add(wordCard);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return words;
    }

    public void addWord(String word1, String word2) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_WORD1, word1);
        values.put(DatabaseHelper.COLUMN_WORD2, word2);
        db.insert(TABLE_NAME, null, values);
        Toast.makeText(context, "Word added", Toast.LENGTH_SHORT).show();
    }

    public void deleteWord(WordCard wordCard) {
        long id = wordCard.getId();
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
    }

}
