package com.hzih.stp.domain;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: sunny
 * Date: 15-9-5
 * Time: 上午11:01
 */
public class Tables {

    private long id;
    private String name;
    private String nameEn;
    private int countAll;
    private int countNew;
    private Date updateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public int getCountAll() {
        return countAll;
    }

    public void setCountAll(int countAll) {
        this.countAll = countAll;
    }

    public int getCountNew() {
        return countNew;
    }

    public void setCountNew(int countNew) {
        this.countNew = countNew;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
