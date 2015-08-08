package org.devtcg.stethogame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public class QuestionModifySetting extends Fragment implements QuestionInfoProvider {
  private static final QuestionInfo INFO =
      new QuestionInfo(
          "Modify this setting",
          Achievements.Achievement.QUESTION_MODIFY_SETTING);

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.question_modify_setting, container, false);
    ButterKnife.bind(this, view);

    PreferenceManager.getDefaultSharedPreferences(getActivity())
        .registerOnSharedPreferenceChangeListener(mListener);

    return view;
  }

  @Override
  public QuestionInfo getQuestionInfo() {
    return INFO;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    PreferenceManager.getDefaultSharedPreferences(getActivity())
        .unregisterOnSharedPreferenceChangeListener(mListener);
  }

  private final SharedPreferences.OnSharedPreferenceChangeListener mListener =
      new SharedPreferences.OnSharedPreferenceChangeListener() {
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPrefs, String key) {
      if (sharedPrefs.getBoolean("prefYouWin", false)) {
        Achievements.unlock(getActivity(), INFO.achievement);
      }
    }
  };
}
