package com.jxztev.entity.acs4sql;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "FindSlideRainResponse", description = "FindSlideRain接口响应类")
public class FindSlideRainResponse {
    @ApiModelProperty(value = "stcd,这是一个别名变量。")
    private String stcd;

    public String getStcd() {
        return stcd;
    }

    public void setStcd(String stcd) {
        this.stcd = stcd;
    }

    @ApiModelProperty(value = "stnm,这是一个别名变量。")
    private String stnm;

    public String getStnm() {
        return stnm;
    }

    public void setStnm(String stnm) {
        this.stnm = stnm;
    }

    @ApiModelProperty(value = "cnnm,这是一个别名变量。")
    private String cnnm;

    public String getCnnm() {
        return cnnm;
    }

    public void setCnnm(String cnnm) {
        this.cnnm = cnnm;
    }

    @ApiModelProperty(value = "stlc,这是一个别名变量。")
    private String stlc;

    public String getStlc() {
        return stlc;
    }

    public void setStlc(String stlc) {
        this.stlc = stlc;
    }

    @ApiModelProperty(value = "starttm,这是一个别名变量。")
    private String starttm;

    public String getStarttm() {
        return starttm;
    }

    public void setStarttm(String starttm) {
        this.starttm = starttm;
    }

    @ApiModelProperty(value = "endtm,这是一个别名变量。")
    private String endtm;

    public String getEndtm() {
        return endtm;
    }

    public void setEndtm(String endtm) {
        this.endtm = endtm;
    }

    @ApiModelProperty(value = "pp,这是一个别名变量。")
    private float pp;

    public float getPp() {
        return pp;
    }

    public void setPp(float pp) {
        this.pp = pp;
    }

}
