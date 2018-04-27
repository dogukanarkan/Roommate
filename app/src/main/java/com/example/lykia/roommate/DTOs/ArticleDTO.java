package com.example.lykia.roommate.DTOs;

import java.sql.Date;
import java.sql.Timestamp;

public class ArticleDTO {

    private int articleId;
    private AdminDTO admin;
    private String imagePath;
    private String header;
    private String text;
    private Timestamp additionDate;

    public ArticleDTO() {
    }

    public ArticleDTO(AdminDTO admin, String imagePath, String header, String text) {
        this.admin = admin;
        this.imagePath = imagePath;
        this.header = header;
        this.text = text;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public AdminDTO getAdmin() {
        return admin;
    }

    public void setAdmin(AdminDTO admin) {
        this.admin = admin;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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

    public Timestamp getAdditionDate() {
        return additionDate;
    }

    public void setAdditionDate(Timestamp additionDate) {
        this.additionDate = additionDate;
    }
}
