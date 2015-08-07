package org.devtcg.stethogame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Question2 extends Fragment {
  @Bind(R.id.answer_btn)
  Button mAnswerBtn;

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.question_2, container, false);
    ButterKnife.bind(this, view);

    mAnswerBtn.setOnClickListener(mAnswerClicked);

    return view;
  }

  private final View.OnClickListener mAnswerClicked = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      Achievements.unlock(getActivity(), Achievements.QUESTION_2);
    }
  };
}
