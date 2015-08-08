package org.devtcg.stethogame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuestionImageType extends Fragment
    implements QuestionInfoProvider, RevealListener {
  private static final QuestionInfo INFO =
      new QuestionInfo(
          "Interesting Image Type",
          Achievements.Achievement.QUESTION_IMAGE_TYPE);

  @Bind(R.id.imageView)
  ImageView mImageView;

  @Bind(R.id.answer_text)
  EditText mAnswer;

  private volatile String mFormat;

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.question_image_type, container, false);
    ButterKnife.bind(this, view);

    return view;
  }

  @Override
  public QuestionInfo getQuestionInfo() {
    return INFO;
  }

  @OnClick(R.id.answer_btn)
  public void onAnswerClicked(Button answerBtn) {
    if (mAnswer.getText().toString().trim().equalsIgnoreCase(mFormat)) {
      Achievements.unlock(getActivity(), INFO.achievement);
    } else {
      Toast.makeText(getActivity(), "Incorrect answer.", Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public void onRevealed() {
    new SetImageTask().execute();
  }

  private class SetImageTask extends AsyncTask<Void, Integer, Bitmap> {
    @Override
    protected Bitmap doInBackground(Void... params) {
      Request request = new Request.Builder()
          .url("https://www.gstatic.com/webp/gallery/5.sm.webp")
          .build();
      try {
        Response response = StethoGameApplication.getClient().newCall(request).execute();

        String contentType = response.header("Content-Type");
        mFormat = contentType.replace("image/", "");

        return (BitmapFactory.decodeStream(response.body().byteStream()));
      } catch (IOException e) {
        e.printStackTrace();
        return null;
      }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
      mImageView.setImageDrawable(new BitmapDrawable(getResources(), bitmap));
    }
  }
}
