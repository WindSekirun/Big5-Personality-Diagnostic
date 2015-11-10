package com.github.windsekirun.big5personalitydiagnostic.util;

import java.io.Serializable;

/**
 * Big5 Personality Diagnostic
 * class: DiagnosticModel
 * Created by WindSekirun on 2015. 11. 10..
 */
public class DiagnosticModel implements Consts, Serializable {
    int C;
    int A;
    int N;
    int O;
    int E;

    public DiagnosticModel(int c, int a, int n, int o, int e) {
        C = c;
        A = a;
        N = n;
        O = o;
        E = e;
    }

    public int getA() {
        return A;
    }

    public int getC() {
        return C;
    }

    public int getE() {
        return E;
    }

    public int getN() {
        return N;
    }

    public int getO() {
        return O;
    }
}
