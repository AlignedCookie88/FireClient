package com.alignedcookie88.fireclient.serialisation;

public class CodeTemplateData {

    public String name;

    public String code;

    public String author;

    public Integer version;

    public CodeTemplateData(String name, String code, String author) {
        this.name = name;
        this.code = code;
        this.author = author;
        this.version = 1;
    }
}
