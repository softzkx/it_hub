package com.qf.ithub.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import javax.persistence.*;

@Table(name = "pay_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayInfo {
    @Id
    private Integer id;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 订单号
     */
    @Column(name = "order_no")
    private Long orderNo;

    /**
     * 支付平台:1-支付宝,2-微信
     */
    @Column(name = "pay_platform")
    private Integer payPlatform;

    /**
     * 支付流水号
     */
    @Column(name = "platform_number")
    private String platformNumber;

    /**
     * 支付状态
     */
    @Column(name = "platform_status")
    private String platformStatus;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;


}