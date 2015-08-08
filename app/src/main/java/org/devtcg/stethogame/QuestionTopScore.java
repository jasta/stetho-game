package org.devtcg.stethogame;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.devtcg.stethogame.data.TopScoreContract.TopScore;

import org.devtcg.stethogame.data.TopScoreDbHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuestionTopScore extends Fragment implements QuestionInfoProvider {
  private static final QuestionInfo INFO =
      new QuestionInfo(
          "Get the top score",
          Achievements.QUESTION_TOP_SCORE);
  private TopScoreDbHelper mDbHelper;

  @Bind(R.id.top_score_list)
  ListView listView;

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.question_top_score, container, false);
    ButterKnife.bind(this, view);
    refresh();
    return view;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mDbHelper = new TopScoreDbHelper(getActivity());
  }

  @OnClick(R.id.answer_btn)
  public void onAnswerClicked(Button answerBtn) {
    refresh();
  }

  protected void refresh() {
    SQLiteDatabase db = mDbHelper.getReadableDatabase();

    String[] projection = {
        TopScore._ID,
        TopScore.COLUMN_NAME_SCORE,
        TopScore.COLUMN_NAME_NAME
    };

    String sortOrder =
        TopScore.COLUMN_NAME_SCORE + " DESC";

    Cursor c = db.query(
        TopScore.TABLE_NAME,  // The table to query
        projection,                               // The columns to return
        null,                                // The columns for the WHERE clause
        null,                            // The values for the WHERE clause
        null,                                     // don't group the rows
        null,                                     // don't filter by row groups
        sortOrder                                 // The sort order
    );

    // THE DESIRED COLUMNS TO BE BOUND
    String[] columns = new String[] { TopScore.COLUMN_NAME_SCORE, TopScore.COLUMN_NAME_NAME };
    // THE XML DEFINED VIEWS WHICH THE DATA WILL BE BOUND TO
    int[] to = new int[] { R.id.top_score_score, R.id.top_score_name };

    listView.setAdapter(new SimpleCursorAdapter(getActivity(), R.layout.top_score_list_item, c, columns, to));
    SQLiteCursor obj = (SQLiteCursor)listView.getAdapter().getItem(0);
    if (obj.getInt(1) > 1000) {
      Achievements.unlock(getActivity(), INFO.achievementId);
    }
  }

  @Override
  public QuestionInfo getQuestionInfo() {
    return INFO;
  }
}
