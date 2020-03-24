package com.qf.ithub.entity;

import javax.persistence.*;

@Table(name = "share_images")
public class ShareImages {
    private Integer id;

    private String image;

    private Integer shareid;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * @return shareid
     */
    public Integer getShareid() {
        return shareid;
    }

    /**
     * @param shareid
     */
    public void setShareid(Integer shareid) {
        this.shareid = shareid;
    }
}