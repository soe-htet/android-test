package com.example.soehtet.myapplication.model;

/**
 * Created by soehtet on 6/10/16.
 */

public class Result {

    String wordname;
    String wordtype;
    String definition;

    public Result(String wordname, String wordtype, String definition) {
        this.wordname = wordname;
        this.wordtype = wordtype;
        this.definition = definition;
    }

    public String getWordname() {
        return wordname;
    }

    public void setWordname(String wordname) {
        this.wordname = wordname;
    }

    public String getWordtype() {
        return wordtype;
    }

    public void setWordtype(String wordtype) {
        this.wordtype = wordtype;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
