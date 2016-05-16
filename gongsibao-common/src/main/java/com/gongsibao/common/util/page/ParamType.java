package com.gongsibao.common.util.page;

/**
 * Created by yangshunkang on 16/3/30.
 */
public class ParamType implements java.io.Serializable{
       //状态码,
    private String dbKey = "";

    private Object dbValue = "";

    private String selectType = "";

    private String andOr = " AND ";

    public ParamType(){
    }

    public String getDbKey() {
        return dbKey;
    }

    public void setDbKey(String dbKey) {
        this.dbKey = dbKey;
    }

    public Object getDbValue() {
        return dbValue;
    }

    public void setDbValue(Object dbValue) {
        this.dbValue = dbValue;
    }

    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }

    public String getAndOr() {
        return andOr;
    }

    public void setAndOr(String andOr) {
        this.andOr = andOr;
    }
}
