package org.devtcg.stethogame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuestionElementsStuff extends Fragment implements QuestionInfoProvider {
  private static final QuestionInfo INFO =
      new QuestionInfo(
          "Elements stuff",
          Achievements.Achievement.QUESTION_ELEMENTS_STUFF);

  @Bind(R.id.height_text)
  TextView mHeightText;

  @Bind(R.id.answer_text)
  EditText mAnswerTextView;

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.question_elements_stuff, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @OnClick(R.id.answer_btn)
  public void onAnswerClicked(Button answerBtn) {
    int answerHeight = Integer.parseInt(mAnswerTextView.getText().toString());
    if (mHeightText.getMeasuredHeight() == answerHeight) {
      Achievements.unlock(getActivity(), Achievements.Achievement.QUESTION_ELEMENTS_STUFF);
    } else {
      Toast.makeText(getActivity(), "Incorrect answer.", Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public QuestionInfo getQuestionInfo() {
    return INFO;
  }
}
