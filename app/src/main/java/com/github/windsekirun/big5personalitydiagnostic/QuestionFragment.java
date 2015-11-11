package com.github.windsekirun.big5personalitydiagnostic;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.windsekirun.big5personalitydiagnostic.util.Consts;
import com.github.windsekirun.big5personalitydiagnostic.util.Material;
import com.github.windsekirun.big5personalitydiagnostic.util.QuestionPair;
import com.github.windsekirun.big5personalitydiagnostic.util.onFragmentChangeRequest;

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
    String questionPro;
    QuestionPair pair;

    onFragmentChangeRequest listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof onFragmentChangeRequest) {
            listener = (onFragmentChangeRequest) activity;
        } else {
            throw new ClassCastException();
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_question, container, false);
        ButterKnife.bind(this, v);

        questionNum = getArguments().getInt(QuestNum);
        questionPro = getArguments().getString(QuestProgress);
        pair = (QuestionPair) getArguments().getSerializable(QuestPair);

        questionText.setText(pair.first);

        if (questionNum == 1) {
            prevButton.setEnabled(false);
            prevButton.setTextColor(Material.getMaterialCyanColor(700));
        } else {
            prevButton.setEnabled(true);
            prevButton.setTextColor(Material.getWhite());
        }

        if (questionNum == 20) {
            nextButton.setText(R.string.fragment_question_submit);
        } else {
            nextButton.setText(getString(R.string.fragment_question_next));
        }

        questionProgress.setText(getString(R.string.fragment_question_progress) + questionPro);


        if (pair.second == 1 || pair.second == 2 || pair.second == 3 || pair.second == 4 || pair.second == 5) {
            switch (pair.second) {
                case 1:
                    ((RadioButton) questionGroup.getChildAt(4)).setChecked(true);
                    break;
                case 2:
                    ((RadioButton) questionGroup.getChildAt(3)).setChecked(true);
                    break;
                case 3:
                    ((RadioButton) questionGroup.getChildAt(2)).setChecked(true);
                    break;
                case 4:
                    ((RadioButton) questionGroup.getChildAt(1)).setChecked(true);
                    break;
                case 5:
                    ((RadioButton) questionGroup.getChildAt(0)).setChecked(true);
                    break;
                default:
                    break;
            }
        }

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPrev(questionNum, 0);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (questionGroup.getCheckedRadioButtonId()) {
                    case R.id.questionRadio1:
                        listener.onNext(questionNum, 5);
                        break;
                    case R.id.questionRadio2:
                        listener.onNext(questionNum, 4);
                        break;
                    case R.id.questionRadio3:
                        listener.onNext(questionNum, 3);
                        break;
                    case R.id.questionRadio4:
                        listener.onNext(questionNum, 2);
                        break;
                    case R.id.questionRadio5:
                        listener.onNext(questionNum, 1);
                        break;
                    default:
                        Toast.makeText(getActivity(), R.string.fragment_question_notcheck, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
