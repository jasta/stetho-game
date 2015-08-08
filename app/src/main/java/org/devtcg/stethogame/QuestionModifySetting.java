package org.devtcg.stethogame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuestionModifySetting extends Fragment implements QuestionInfoProvider {
  private static final QuestionInfo INFO =
      new QuestionInfo(
          "Modify this setting",
          Achievements.Achievement.QUESTION_MODIFY_SETTING);
    private static final int RESULT_SETTINGS = 1;

    @Nullable
  @Override
  public View onCreateView(
      LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.question_modify_setting, container, false);
    ButterKnife.bind(this, view);
    checkSharedPref();
    return view;
  }

  @OnClick(R.id.answer_btn)
  public void onAnswerClicked(Button answerBtn) {
      Intent i = new Intent(getActivity(), UserSettingActivity.class);
      startActivityForResult(i, RESULT_SETTINGS);
  }

  @Override
  public QuestionInfo getQuestionInfo() {
    return INFO;
  }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SETTINGS:
                checkSharedPref();
                break;
        }
    }

    private void checkSharedPref() {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity());

        if (sharedPrefs.getBoolean("prefYouWin", false)) {
            Achievements.unlock(getActivity(), INFO.achievement);
        }
    }
}
