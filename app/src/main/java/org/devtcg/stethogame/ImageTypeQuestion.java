package org.devtcg.stethogame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageTypeQuestion extends Fragment implements QuestionInfoProvider {
  private static final QuestionInfo INFO =
      new QuestionInfo(
          "5th question",
          Achievements.Achievement.QUESTION_IMAGE_TYPE);

  @Bind(R.id.imageView)
  ImageView mImageView;

  @Bind(R.id.answer_text)
  EditText mAnswer;

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.image_type_question, container, false);
    ButterKnife.bind(this, view);

    try {
      mImageView.setImageBitmap(new SetImageTask().execute().get());
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }

    return view;
  }

  @Override
  public QuestionInfo getQuestionInfo() {
    return INFO;
  }

  @OnClick(R.id.answer_btn)
  public void onAnswerClicked(Button answerBtn) {
    if (mAnswer.getText().toString().equals("webp")) {
      Achievements.unlock(getActivity(), INFO.achievement);
    } else {
      Toast.makeText(getActivity(), "Incorrect answer.", Toast.LENGTH_SHORT).show();
    }
  }

  private class SetImageTask extends AsyncTask<Void, Integer, Bitmap> {
    @Override
    protected Bitmap doInBackground(Void... params) {
      Request request = new Request.Builder().url("https://www.gstatic.com/webp/gallery/5.sm.webp").build();

      Response response = null;
      try {
        response = StethoGameApplication.getClient().newCall(request).execute();
      } catch (IOException e) {
        e.printStackTrace();
      }

      try {
        return (BitmapFactory.decodeStream(response.body().byteStream()));
      } catch (IOException e) {
        e.printStackTrace();
      }

      return null;
    }
  }
}
