package com.example.lykia.roommate.DTOs;

import java.sql.Date;

public class ArticleDTO {

    private int article_id;
    private AdminDTO admin;
    private String image_path;
    private String header;
    private String text;
    private Date addition_date;

    public ArticleDTO() {
    }

    public ArticleDTO(AdminDTO admin, String image_path, String header, String text) {
        this.admin = admin;
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

    public AdminDTO getAdmin() {
        return admin;
    }

    public void setAdmin(AdminDTO admin) {
        this.admin = admin;
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
