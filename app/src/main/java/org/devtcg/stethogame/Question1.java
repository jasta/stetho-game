package org.devtcg.stethogame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class Question1 extends Fragment {
  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.question_1, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @OnClick(R.id.answer_btn)
  public void onAnswerClicked(Button answerBtn) {
    Achievements.unlock(getActivity(), Achievements.QUESTION_1);
  }
}