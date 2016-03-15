package de.aikiit.fotocollector;

import com.google.common.base.Strings;

/**
 * @author hirsch
 * @version 2016-02-20, 14:08
 */
public class OutputResult {

    private final String result;
    private final String name;

    public OutputResult(String result, String name) {
        this.result = result;
        this.name = name;
    }

    public String getResult() {
        return this.result;
    }

    public String getName() {
        return name;
    }

    public boolean isEmpty() {
        return Strings.isNullOrEmpty(result);
    }

}
