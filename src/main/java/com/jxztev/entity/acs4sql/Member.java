package com.jxztev.entity.acs4sql;


import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

public class Member implements Serializable {

    private static final long serialVersionUID = -1959528436584592183L;

    private String id;

    private String jsonData;

    public Member(){}

    public Member(String id, String jsonData){
        this.setId(id);
        this.setJsonData(jsonData);
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

}