package com.qf.ithub.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import javax.persistence.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tb_order")
public class TbOrder {
    @Id
    @Column(name = "order_no")
    private Long orderNo;

    @Column(name = "user_id")
    private Integer userId;

    private BigDecimal amount;

    private String subject;

    private String status;


}