package com.andy.androidsample.entity;

import java.io.Serializable;

/**
 * Activity 中ListView item点击跳转内容实例
 * <p>
 * Created by andy on 18-1-12.
 */

public class CenterActivityItem implements Serializable {

    private Object content;

    private Class<?> nextActivity;

    public CenterActivityItem(Object content, Class<?> nextActivity) {
        this.content = content;
        this.nextActivity = nextActivity;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Class<?> getNextActivity() {
        return nextActivity;
    }

    public void setNextActivity(Class<?> nextActivity) {
        this.nextActivity = nextActivity;
    }

    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {
        s.defaultWriteObject();
        s.writeObject(content);
        s.writeObject(nextActivity);
    }

    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        s.defaultReadObject();
        content = s.readObject();
        nextActivity = (Class<?>) s.readObject();
    }
}
