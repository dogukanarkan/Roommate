package com.example.lykia.roommate.DTOs;

import java.sql.Date;

public class ArticleDTO {

    private int article_id;
    private AdminDTO addition_id;
    private String image_path;
    private String header;
    private String text;
    private Date addition_date;

    public ArticleDTO(AdminDTO addition_id, String image_path, String header, String text) {
        this.addition_id = addition_id;
        this.image_path = image_path;
        this.header = header;
        this.text = text;
    }

    public int getArticle_id() {
        return article_id;
    }

    public void setArticle_id(int article_id) {
        this.article_id = article_id;
    }

    public AdminDTO getAddition_id() {
        return addition_id;
    }

    public void setAddition_id(AdminDTO addition_id) {
        this.addition_id = addition_id;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getAddition_date() {
        return addition_date;
    }

    public void setAddition_date(Date addition_date) {
        this.addition_date = addition_date;
    }
}
