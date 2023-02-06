package com.mywg.core.database.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Table(name = "teacher_exam")
public class TeacherExam {
    @Id
    @Column(name = "id")
    private Integer id;

    /**
     * 标题，学校， 等头信息
     */
    @Column(name = "name")
    private String name;

    /**
     * 报名链接
     */
    @Column(name = "bm_url")
    private String bmUrl;

    /**
     * 开始报名时间
     */
    @Column(name = "start_bm_date")
    private Date startBmDate;

    /**
     * 结束报名时间
     */
    @Column(name = "end_bm_date")
    private Date endBmDate;

    /**
     * 开始确认日期
     */
    @Column(name = "start_qr_date")
    private Date startQrDate;

    /**
     * 结束确认日期
     */
    @Column(name = "end_qr_date")
    private Date endQrDate;

    /**
     * 确认报名链接
     */
    @Column(name = "qr_url")
    private String qrUrl;

    /**
     * 备注
     */
    @Column(name = "remarks")
    private String remarks;

    /**
     * 0 没报名，1已经报名
     */
    @Column(name = "bm_status")
    private Integer bmStatus;

    /**
     * 0:没确认 1:已确认
     */
    @Column(name = "qr_status")
    private Integer qrStatus;

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
     * 获取标题，学校， 等头信息
     *
     * @return name - 标题，学校， 等头信息
     */
    public String getName() {
        return name;
    }

    /**
     * 设置标题，学校， 等头信息
     *
     * @param name 标题，学校， 等头信息
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取报名链接
     *
     * @return bm_url - 报名链接
     */
    public String getBmUrl() {
        return bmUrl;
    }

    /**
     * 设置报名链接
     *
     * @param bmUrl 报名链接
     */
    public void setBmUrl(String bmUrl) {
        this.bmUrl = bmUrl == null ? null : bmUrl.trim();
    }

    /**
     * 获取开始报名时间
     *
     * @return start_bm_date - 开始报名时间
     */
    public Date getStartBmDate() {
        return startBmDate;
    }

    /**
     * 设置开始报名时间
     *
     * @param startBmDate 开始报名时间
     */
    public void setStartBmDate(Date startBmDate) {
        this.startBmDate = startBmDate;
    }

    /**
     * 获取结束报名时间
     *
     * @return end_bm_date - 结束报名时间
     */
    public Date getEndBmDate() {
        return endBmDate;
    }

    /**
     * 设置结束报名时间
     *
     * @param endBmDate 结束报名时间
     */
    public void setEndBmDate(Date endBmDate) {
        this.endBmDate = endBmDate;
    }

    /**
     * 获取开始确认日期
     *
     * @return start_qr_date - 开始确认日期
     */
    public Date getStartQrDate() {
        return startQrDate;
    }

    /**
     * 设置开始确认日期
     *
     * @param startQrDate 开始确认日期
     */
    public void setStartQrDate(Date startQrDate) {
        this.startQrDate = startQrDate;
    }

    /**
     * 获取结束确认日期
     *
     * @return end_qr_date - 结束确认日期
     */
    public Date getEndQrDate() {
        return endQrDate;
    }

    /**
     * 设置结束确认日期
     *
     * @param endQrDate 结束确认日期
     */
    public void setEndQrDate(Date endQrDate) {
        this.endQrDate = endQrDate;
    }

    /**
     * 获取确认报名链接
     *
     * @return qr_url - 确认报名链接
     */
    public String getQrUrl() {
        return qrUrl;
    }

    /**
     * 设置确认报名链接
     *
     * @param qrUrl 确认报名链接
     */
    public void setQrUrl(String qrUrl) {
        this.qrUrl = qrUrl == null ? null : qrUrl.trim();
    }

    /**
     * 获取备注
     *
     * @return remarks - 备注
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * 设置备注
     *
     * @param remarks 备注
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    /**
     * 获取0 没报名，1已经报名
     *
     * @return bm_status - 0 没报名，1已经报名
     */
    public Integer getBmStatus() {
        return bmStatus;
    }

    /**
     * 设置0 没报名，1已经报名
     *
     * @param bmStatus 0 没报名，1已经报名
     */
    public void setBmStatus(Integer bmStatus) {
        this.bmStatus = bmStatus;
    }

    /**
     * 获取0:没确认 1:已确认
     *
     * @return qr_status - 0:没确认 1:已确认
     */
    public Integer getQrStatus() {
        return qrStatus;
    }

    /**
     * 设置0:没确认 1:已确认
     *
     * @param qrStatus 0:没确认 1:已确认
     */
    public void setQrStatus(Integer qrStatus) {
        this.qrStatus = qrStatus;
    }
}