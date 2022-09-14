package com.beckytech.english1000commonphrases;

import java.io.Serializable;

public class Model implements Serializable {
    String title, subTitle, content, categories;

    public Model(String title, String subTitle, String content, String categories) {
        this.title = title;
        this.categories = categories;
        this.content = content;
        this.subTitle = subTitle;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getContent() {
        return content;
    }

    public String getCategories() {
        return categories;
    }
}
