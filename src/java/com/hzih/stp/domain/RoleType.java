package com.hzih.stp.domain;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-7
 * Time: 上午10:58
 * 角色类型
 */
public class RoleType {
    long id;
    String name;
    String permission;//权限
    int autoUnLock;//自动解锁

    String read;
    String rename;
    String delete;
    String unLock;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public int getAutoUnLock() {
        return autoUnLock;
    }

    public void setAutoUnLock(int autoUnLock) {
        this.autoUnLock = autoUnLock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getRename() {
        return rename;
    }

    public void setRename(String rename) {
        this.rename = rename;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public String getUnLock() {
        return unLock;
    }

    public void setUnLock(String unLock) {
        this.unLock = unLock;
    }
}
