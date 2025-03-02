package com.alignedcookie88.fireclient.serialisation;

public class CodeTemplateData {

    public final String name;

    public final String code;

    public final String author;

    public final Integer version;

    public CodeTemplateData(String name, String code, String author) {
        this.name = name;
        this.code = code;
        this.author = author;
        this.version = 1;
    }
}
