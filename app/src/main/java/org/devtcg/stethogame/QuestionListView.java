package org.devtcg.stethogame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.devtcg.stethogame.dummy.DummyContent;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuestionListView extends Fragment implements QuestionInfoProvider {
  private static final QuestionInfo INFO =
      new QuestionInfo(
          "What's the height?",
          Achievements.Achievement.QUESTION_LIST_ITEM_HEIGHT);

  @Bind(R.id.randomlist)
  ListView listView;

  @Bind(R.id.answer_text)
  EditText answerTextView;

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.question_3, container, false);
    ButterKnife.bind(this, view);

    listView.setAdapter(new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
        android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS));
    return view;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @OnClick(R.id.answer_btn)
  public void onAnswerClicked(Button answerBtn) {
    for (int i = 0; i < listView.getChildCount(); i++) {
      int itemNum = i + listView.getFirstVisiblePosition();
      if (itemNum == 376) {
        View view = listView.getChildAt(i);
        if (view.getMeasuredHeight() == Integer.parseInt(answerTextView.getText().toString())) {
          Achievements.unlock(getActivity(), Achievements.Achievement.QUESTION_LIST_ITEM_HEIGHT);
        } else {
          break;
        }
        return;
      }
    }
    Toast.makeText(getActivity(), "Incorrect answer.", Toast.LENGTH_SHORT).show();
  }

  @Override
  public QuestionInfo getQuestionInfo() {
    return INFO;
  }
}
