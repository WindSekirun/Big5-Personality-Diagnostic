package com.github.windsekirun.big5personalitydiagnostic.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.github.windsekirun.big5personalitydiagnostic.R;
import com.github.windsekirun.big5personalitydiagnostic.util.narae.NaraeMap;
import com.github.windsekirun.big5personalitydiagnostic.util.narae.NaraePreference;

import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Big5 Personality Diagnostic
 * class: QuestionStorage
 * Created by WindSekirun on 2015. 11. 10..
 */
@SuppressWarnings("unchecked")
public class QuestionStorage implements Consts {
    Context c;
    NaraeMap<Integer, QuestionPair> questionList;
    NaraePreference np;

    public QuestionStorage(Context c) {
        this.c = c;
        np = new NaraePreference(c);
        if (np.getValue(tempSave, false)) {
            questionList = new NaraeMap<>();
            loadPairs();
        } else {
            if (questionList == null) {
                questionList = new NaraeMap<>();
                questionList.put(1, new QuestionPair(getQuestionTextByNum(1), 0));
                questionList.put(2, new QuestionPair(getQuestionTextByNum(2), 0));
                questionList.put(3, new QuestionPair(getQuestionTextByNum(3), 0));
                questionList.put(4, new QuestionPair(getQuestionTextByNum(4), 0));
                questionList.put(5, new QuestionPair(getQuestionTextByNum(5), 0));
                questionList.put(6, new QuestionPair(getQuestionTextByNum(6), 0));
                questionList.put(7, new QuestionPair(getQuestionTextByNum(7), 0));
                questionList.put(8, new QuestionPair(getQuestionTextByNum(8), 0));
                questionList.put(9, new QuestionPair(getQuestionTextByNum(9), 0));
                questionList.put(10, new QuestionPair(getQuestionTextByNum(10), 0));
                questionList.put(11, new QuestionPair(getQuestionTextByNum(11), 0));
                questionList.put(12, new QuestionPair(getQuestionTextByNum(12), 0));
                questionList.put(13, new QuestionPair(getQuestionTextByNum(13), 0));
                questionList.put(14, new QuestionPair(getQuestionTextByNum(14), 0));
                questionList.put(15, new QuestionPair(getQuestionTextByNum(15), 0));
                questionList.put(16, new QuestionPair(getQuestionTextByNum(16), 0));
                questionList.put(17, new QuestionPair(getQuestionTextByNum(17), 0));
                questionList.put(18, new QuestionPair(getQuestionTextByNum(18), 0));
                questionList.put(19, new QuestionPair(getQuestionTextByNum(19), 0));
                questionList.put(20, new QuestionPair(getQuestionTextByNum(20), 0));
            }
        }
    }

    public void savePairs() {
        try {
            FileOutputStream fileOutputStream = c.openFileOutput("naraepair.db", Context.MODE_PRIVATE);
            FSTObjectOutput objectOutputStream = new FSTObjectOutput(fileOutputStream);
            objectOutputStream.writeObject(questionList);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPairs() {
        Toast.makeText(c, R.string.storage_question_load_temped, Toast.LENGTH_SHORT).show();
        np.getValue(tempSave, false);
        FileInputStream fileInputStream;
        FSTObjectInput objectInputStream;

        try {
            fileInputStream = c.openFileInput("naraepair.db");
            objectInputStream = new FSTObjectInput(fileInputStream);
            questionList = (NaraeMap<Integer, QuestionPair>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
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
