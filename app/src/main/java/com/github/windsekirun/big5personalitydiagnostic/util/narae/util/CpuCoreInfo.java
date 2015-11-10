package com.github.windsekirun.big5personalitydiagnostic.util.narae.util;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

/**
 * NaraeTask
 * Class: CpuCoreInfo
 * Created by WindSekirun on 15. 6. 22..
 */
public class CpuCoreInfo {

    public static int getCoresCount() {
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(final File pathname) {
                return Pattern.matches("cpu[0-9]+", pathname.getName());
            }
        }
        try {
            final File dir = new File("/sys/devices/system/cpu/");
            final File[] files = dir.listFiles(new CpuFilter());
            return files.length;
        } catch (final Exception e) {
            return Math.max(1, Runtime.getRuntime().availableProcessors());
        }
    }
}
