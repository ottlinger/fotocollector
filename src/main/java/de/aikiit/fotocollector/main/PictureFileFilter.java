package de.aikiit.fotocollector.main;

import com.google.common.base.Strings;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;

/**
 * @author hirsch
 * @version 2016-03-07, 22:50
 */
class PictureFileFilter implements FileFilter {

    static final List<String> PICTURE_ENDINGS = Arrays.asList("jpg", "gif", "jpeg", "png");

    @Override
    public boolean accept(final File pathname) {
        return pathname != null && !pathname.isDirectory() && matchesEnding(pathname.toString());
    }

    private static boolean matchesEnding(String fileName) {
        if (!Strings.isNullOrEmpty(fileName)) {

            String lowercase = fileName.toLowerCase();
            for (String ending : PICTURE_ENDINGS) {
                if (lowercase.endsWith(ending)) {
                    return true;
                }
            }
        }

        return false;
    }
}
