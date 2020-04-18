package com.qf.ithub.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "share_images")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareImages {
    @Id
    private Integer id;

    private String image;

    private Integer shareid;


}