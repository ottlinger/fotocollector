package de.aikiit.fotocollector;

import com.google.common.base.Strings;

/**
 * @author hirsch
 * @version 2016-02-20, 14:08
 */
public class OutputResult {

    private final String result;

    public OutputResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return this.result;
    }

    public boolean isEmpty() {
        return Strings.isNullOrEmpty(result);
    }

}
