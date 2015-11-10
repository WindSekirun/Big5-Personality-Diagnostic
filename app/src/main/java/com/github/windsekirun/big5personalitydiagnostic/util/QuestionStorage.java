package com.github.windsekirun.big5personalitydiagnostic.util;

import android.content.Context;
import android.util.Log;

import com.github.windsekirun.big5personalitydiagnostic.R;
import com.github.windsekirun.big5personalitydiagnostic.util.narae.NaraeMap;

/**
 * Big5 Personality Diagnostic
 * class: QuestionStorage
 * Created by WindSekirun on 2015. 11. 10..
 */
public class QuestionStorage implements Consts {
    Context c;
    NaraeMap<Integer, QuestionPair> questionList;

    public QuestionStorage(Context c) {
        this.c = c;

        if (questionList == null) {
            questionList = new NaraeMap<>();
            questionList.put(1, new QuestionPair(getQuestionTextByNum(1), 5));
            questionList.put(2, new QuestionPair(getQuestionTextByNum(2), 5));
            questionList.put(3, new QuestionPair(getQuestionTextByNum(3), 5));
            questionList.put(4, new QuestionPair(getQuestionTextByNum(4), 5));
            questionList.put(5, new QuestionPair(getQuestionTextByNum(5), 5));
            questionList.put(6, new QuestionPair(getQuestionTextByNum(6), 5));
            questionList.put(7, new QuestionPair(getQuestionTextByNum(7), 5));
            questionList.put(8, new QuestionPair(getQuestionTextByNum(8), 5));
            questionList.put(9, new QuestionPair(getQuestionTextByNum(9), 5));
            questionList.put(10, new QuestionPair(getQuestionTextByNum(10), 5));
            questionList.put(11, new QuestionPair(getQuestionTextByNum(11), 5));
            questionList.put(12, new QuestionPair(getQuestionTextByNum(12), 5));
            questionList.put(13, new QuestionPair(getQuestionTextByNum(13), 5));
            questionList.put(14, new QuestionPair(getQuestionTextByNum(14), 5));
            questionList.put(15, new QuestionPair(getQuestionTextByNum(15), 5));
            questionList.put(16, new QuestionPair(getQuestionTextByNum(16), 5));
            questionList.put(17, new QuestionPair(getQuestionTextByNum(17), 5));
            questionList.put(18, new QuestionPair(getQuestionTextByNum(18), 5));
            questionList.put(19, new QuestionPair(getQuestionTextByNum(19), 5));
            questionList.put(20, new QuestionPair(getQuestionTextByNum(20), 5));
        }
    }

    public QuestionPair getPair(int posNum) {
        return questionList.get(posNum);
    }

    public void writePair(int posNum, int checked) {
        questionList.put(posNum, new QuestionPair(getQuestionTextByNum(posNum), checked));
    }

    public DiagnosticModel analyze() {
        int c = getCheckedNum(3) + reverseInt(getCheckedNum(8)) + getCheckedNum(13) + reverseInt(getCheckedNum(18));
        int a = getCheckedNum(2) + reverseInt(getCheckedNum(7)) + getCheckedNum(12) + reverseInt(getCheckedNum(17));
        int n = getCheckedNum(4) + reverseInt(getCheckedNum(9)) + getCheckedNum(14) + reverseInt(getCheckedNum(19));
        int o = getCheckedNum(5) + reverseInt(getCheckedNum(10)) + reverseInt(getCheckedNum(15)) + reverseInt(getCheckedNum(20));
        int e = getCheckedNum(1) + reverseInt(getCheckedNum(6)) + getCheckedNum(11) + reverseInt(getCheckedNum(16));
        if (isDebug) {
            Log.d("Big5-C", c + "");
            Log.d("Big5-A", a + "");
            Log.d("Big5-N", n + "");
            Log.d("Big5-O", o + "");
            Log.d("Big5-E", e + "");
        }
        return new DiagnosticModel(c, a, n, o, e);
    }

    public int getCheckedNum(int num) {
        return questionList.get(num).second;
    }

    public int reverseInt(int num) {
        int result = 0;
        if (num == 1) result = 5;
        else if (num == 2) result = 4;
        else if (num == 3) result = 3;
        else if (num == 4) result = 2;
        else if (num == 5) result = 1;
        return result;
    }

    public String getQuestionTextByNum(int num) {
        switch (num) {
            case 1:
                return c.getString(R.string.question01);
            case 2:
                return c.getString(R.string.question02);
            case 3:
                return c.getString(R.string.question03);
            case 4:
                return c.getString(R.string.question04);
            case 5:
                return c.getString(R.string.question05);
            case 6:
                return c.getString(R.string.question06);
            case 7:
                return c.getString(R.string.question07);
            case 8:
                return c.getString(R.string.question08);
            case 9:
                return c.getString(R.string.question09);
            case 10:
                return c.getString(R.string.question10);
            case 11:
                return c.getString(R.string.question11);
            case 12:
                return c.getString(R.string.question12);
            case 13:
                return c.getString(R.string.question13);
            case 14:
                return c.getString(R.string.question14);
            case 15:
                return c.getString(R.string.question15);
            case 16:
                return c.getString(R.string.question16);
            case 17:
                return c.getString(R.string.question17);
            case 18:
                return c.getString(R.string.question18);
            case 19:
                return c.getString(R.string.question19);
            case 20:
                return c.getString(R.string.question20);
        }
        return "";
    }
}
