package com.brother.xml2excel;

/**
 * Created by wule on 2017/05/18.
 */

public class StringDemo {
    private String name;
    private String text;

    public StringDemo() {
    }

    public StringDemo(String name, String text) {
        this.name = name;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "StringDemo{" +
                "name='" + name + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
