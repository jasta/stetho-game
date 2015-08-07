package org.devtcg.stethogame;

import android.content.Context;
import android.support.annotation.IntDef;
import android.widget.Toast;

public class Achievements {
  @IntDef({ QUESTION_1, QUESTION_2 })
  public @interface Achievement {}

  public static final int QUESTION_1 = 1;
  public static final int QUESTION_2 = 2;

  public static void unlock(Context context, @Achievement int achivementId) {
    // TODO: Fill this in...
    Toast.makeText(
        context,
        "TODO: unlock "  + achivementId,
        Toast.LENGTH_SHORT)
        .show();
  }
}
