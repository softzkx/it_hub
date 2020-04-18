package com.qf.ithub.entity;

import javax.persistence.*;

@Table(name = "rright")
public class Rright {
    @Id
    private Integer rightid;

    private String righttext;

    /**
     * 0: 父节点 1:子节点
     */
    private Integer righttype;

    private String righturl;

    private Integer parentid;

    /**
     * 1:菜单权限 0:button权限
     */
    @Column(name = "is_menu")
    private Boolean isMenu;

    /**
     * @return rightid
     */
    public Integer getRightid() {
        return rightid;
    }

    /**
     * @param rightid
     */
    public void setRightid(Integer rightid) {
        this.rightid = rightid;
    }

    /**
     * @return righttext
     */
    public String getRighttext() {
        return righttext;
    }

    /**
     * @param righttext
     */
    public void setRighttext(String righttext) {
        this.righttext = righttext;
    }

    /**
     * 获取0: 父节点 1:子节点
     *
     * @return righttype - 0: 父节点 1:子节点
     */
    public Integer getRighttype() {
        return righttype;
    }

    /**
     * 设置0: 父节点 1:子节点
     *
     * @param righttype 0: 父节点 1:子节点
     */
    public void setRighttype(Integer righttype) {
        this.righttype = righttype;
    }

    /**
     * @return righturl
     */
    public String getRighturl() {
        return righturl;
    }

    /**
     * @param righturl
     */
    public void setRighturl(String righturl) {
        this.righturl = righturl;
    }

    /**
     * @return parentid
     */
    public Integer getParentid() {
        return parentid;
    }

    /**
     * @param parentid
     */
    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    /**
     * 获取1:菜单权限 0:button权限
     *
     * @return is_menu - 1:菜单权限 0:button权限
     */
    public Boolean getIsMenu() {
        return isMenu;
    }

    /**
     * 设置1:菜单权限 0:button权限
     *
     * @param isMenu 1:菜单权限 0:button权限
     */
    public void setIsMenu(Boolean isMenu) {
        this.isMenu = isMenu;
    }
}