package com.qf.ithub.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "role_right")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleRight {
    @Id
    private Integer rrid;

    private Integer roleid;
    
    private Integer rightid;


}