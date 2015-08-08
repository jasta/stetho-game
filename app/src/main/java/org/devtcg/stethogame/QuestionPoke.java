package org.devtcg.stethogame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class QuestionPoke extends Fragment implements QuestionInfoProvider {
  private static final QuestionInfo INFO =
      new QuestionInfo(
          "Login via Command Line",
          Achievements.Achievement.QUESTION_POKE);


    @Bind(R.id.poke_text)
    TextView mPokeText;

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.question_poke, container, false);
    ButterKnife.bind(this, view);
    PokeManager.get().setListener(new PokeManager.PokeListener() {
      @Override
      public void onPoke(final String message) {
        getActivity().runOnUiThread(new Runnable() {
          @Override
          public void run() {
            mPokeText.setText(message);
            Achievements.unlock(getActivity(), INFO.achievement);
          }
        });
      }
    });
    return view;
  }

  @Override
  public QuestionInfo getQuestionInfo() {
    return INFO;
  }
}
