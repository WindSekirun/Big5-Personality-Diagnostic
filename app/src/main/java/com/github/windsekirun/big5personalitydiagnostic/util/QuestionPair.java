package com.github.windsekirun.big5personalitydiagnostic.util;

import java.io.Serializable;

/**
 * Big5 Personality Diagnostic
 * class: QuestionPair
 * Created by WindSekirun on 2015. 11. 10..
 */
public class QuestionPair implements Serializable {
    public String first;
    public int second;
    
    public QuestionPair(String text, int selectNum) {
        first = text;
        second = selectNum;
    }
}
