package org.devtcg.stethogame.data;

import android.provider.BaseColumns;

public class TopScoreContract {
  public TopScoreContract() {}

  /* Inner class that defines the table contents */
  public static abstract class TopScore implements BaseColumns {
    public static final String TABLE_NAME = "top_scores";
    public static final String COLUMN_NAME_SCORE = "score";
    public static final String COLUMN_NAME_NAME = "name";
  }

}
