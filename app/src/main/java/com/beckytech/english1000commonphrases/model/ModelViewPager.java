package com.beckytech.english1000commonphrases.model;

import java.io.Serializable;

public class ModelViewPager implements Serializable {
    private final int image;
    private final String tag;

    public ModelViewPager(String tag, int image) {
        this.image = image;
        this.tag = tag;
    }

    public int getImage() {
        return image;
    }

    public String getTag() {
        return tag;
    }
}
