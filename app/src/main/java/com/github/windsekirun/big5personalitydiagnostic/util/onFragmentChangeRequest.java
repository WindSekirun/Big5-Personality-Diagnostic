package com.github.windsekirun.big5personalitydiagnostic.util;

/**
 * Big5 Personality Diagnostic
 * class: onFragmentChangeRequest
 * Created by WindSekirun on 2015. 11. 10..
 */
public interface onFragmentChangeRequest {

    void onPrev(int nowNum, int checked);

    void onNext(int nowNum, int checked);
}
