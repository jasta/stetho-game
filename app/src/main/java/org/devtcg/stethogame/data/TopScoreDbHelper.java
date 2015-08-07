package org.devtcg.stethogame.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.devtcg.stethogame.data.TopScoreContract.TopScore;

public class TopScoreDbHelper extends SQLiteOpenHelper {
  // If you change the database schema, you must increment the database version.
  public static final int DATABASE_VERSION = 1;
  public static final String DATABASE_NAME = "TopScores.db";

  private static final String TEXT_TYPE = " TEXT";
  private static final String INT_TYPE = " INT";
  private static final String COMMA_SEP = ",";
  private static final String SQL_CREATE_TABLE =
      "CREATE TABLE " + TopScoreContract.TopScore.TABLE_NAME + " (" +
          TopScore._ID + " INTEGER PRIMARY KEY," +
          TopScore.COLUMN_NAME_SCORE + INT_TYPE + COMMA_SEP +
          TopScore.COLUMN_NAME_NAME + TEXT_TYPE+
      " )";

  private static final String SQL_DELETE_ENTRIES =
      "DROP TABLE IF EXISTS " + TopScoreContract.TopScore.TABLE_NAME;


  public TopScoreDbHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(SQL_CREATE_TABLE);
    // Create a new map of values, where column names are the keys
    ContentValues values = new ContentValues();
    values.put(TopScore.COLUMN_NAME_SCORE, "1000");
    values.put(TopScore.COLUMN_NAME_NAME, "Billy");

    db.insert(
        TopScore.TABLE_NAME,
        TopScore.COLUMN_NAME_NAME,
        values);

    values = new ContentValues();
    values.put(TopScore.COLUMN_NAME_SCORE, "500");
    values.put(TopScore.COLUMN_NAME_NAME, "Jane");

    db.insert(
        TopScore.TABLE_NAME,
        TopScore.COLUMN_NAME_NAME,
        values);

    values = new ContentValues();
    values.put(TopScore.COLUMN_NAME_SCORE, "400");
    values.put(TopScore.COLUMN_NAME_NAME, "Josh");

    db.insert(
        TopScore.TABLE_NAME,
        TopScore.COLUMN_NAME_NAME,
        values);

    values = new ContentValues();
    values.put(TopScore.COLUMN_NAME_SCORE, "100");
    values.put(TopScore.COLUMN_NAME_NAME, "Suzy");

    db.insert(
        TopScore.TABLE_NAME,
        TopScore.COLUMN_NAME_NAME,
        values);
  }
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // This database is only a cache for online data, so its upgrade policy is
    // to simply to discard the data and start over
    db.execSQL(SQL_DELETE_ENTRIES);
    onCreate(db);
  }
  public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    onUpgrade(db, oldVersion, newVersion);
  }
}
