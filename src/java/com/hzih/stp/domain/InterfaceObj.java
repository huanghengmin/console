package com.hzih.stp.domain;

/**
 * Created with IntelliJ IDEA.
 * User: sunny
 * Date: 15-9-5
 * Time: 上午10:54
 */
public class InterfaceObj {
    private long id;                //接口序号
//    private String tableType;      //表类型
//    private String interfaceType; //接口类型
    private String interfaceNumber; //接口类型
//    private String name;           //接口名称
//    private String interfaceDesc; //接口简称
    private String tableName;      //表中文名称

    public String getInterfaceNumber() {
        return interfaceNumber;
    }

    public void setInterfaceNumber(String interfaceNumber) {
        this.interfaceNumber = interfaceNumber;
    }

    private String tableNameEn;    //表英文名称
//    private String containsPerson; //要素属性
//    private String queryField;      //检索字段
    private String conditionsField; //展示字段
    private String url;              //接口地址
    private String status;           //链接状态
    private String apply;            //申请状态
//    private String depart;           //管理部门

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

   /* public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }*/

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableNameEn() {
        return tableNameEn;
    }

    public void setTableNameEn(String tableNameEn) {
        this.tableNameEn = tableNameEn;
    }

 /*   public String getQueryField() {
        return queryField;
    }

    public void setQueryField(String queryField) {
        this.queryField = queryField;
    }
*/
    public String getConditionsField() {
        return conditionsField;
    }

    public void setConditionsField(String conditionsField) {
        this.conditionsField = conditionsField;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApply() {
        return apply;
    }

    public void setApply(String apply) {
        this.apply = apply;
    }

  /*  public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(String interfaceType) {
        this.interfaceType = interfaceType;
    }

    public String getInterfaceDesc() {
        return interfaceDesc;
    }

    public void setInterfaceDesc(String interfaceDesc) {
        this.interfaceDesc = interfaceDesc;
    }

    public String getContainsPerson() {
        return containsPerson;
    }

    public void setContainsPerson(String containsPerson) {
        this.containsPerson = containsPerson;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }*/

    public InterfaceObj(long id) {
        this.id = id;
    }

    public InterfaceObj() {
    }
}
