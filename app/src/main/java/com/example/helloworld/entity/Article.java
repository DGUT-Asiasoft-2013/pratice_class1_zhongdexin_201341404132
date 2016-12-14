package com.example.helloworld.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/14.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class Article implements Serializable {

    Integer id;
    Date createDate;
    Date editDtae;

    String title;
    String text;

    User author;

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getEditDtae() {
        return editDtae;
    }

    public void setEditDtae(Date editDtae) {
        this.editDtae = editDtae;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
