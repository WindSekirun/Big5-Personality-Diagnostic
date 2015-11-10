package com.github.windsekirun.big5personalitydiagnostic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.windsekirun.big5personalitydiagnostic.util.Consts;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Big5 Personality Diagnostic
 * class: QuestionFragment
 * Created by WindSekirun on 2015. 11. 10..
 */
public class QuestionFragment extends Fragment implements Consts {
    @Bind(R.id.questionText)
    TextView questionText;
    @Bind(R.id.questionGroup1)
    RadioGroup questionGroup;
    @Bind(R.id.questionPrev)
    Button prevButton;
    @Bind(R.id.questionNext)
    Button nextButton;
    @Bind(R.id.questionProgress)
    TextView questionProgress;

    int questionNum;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_question, container, false);
        ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
