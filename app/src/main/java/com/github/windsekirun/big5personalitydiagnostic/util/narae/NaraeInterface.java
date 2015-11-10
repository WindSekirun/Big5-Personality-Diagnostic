package com.github.windsekirun.big5personalitydiagnostic.util.narae;

/**
 * NaraeAsynchronous
 * Class: NaraeTask
 * Created by WindSekirun on 15. 6. 22..
 */
@SuppressWarnings("ALL")
public interface NaraeInterface<End> {
    End doInBackground();

    void onPostExecute(End end);
}