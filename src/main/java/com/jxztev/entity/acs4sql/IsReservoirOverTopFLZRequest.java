package com.jxztev.entity.acs4sql;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "IsReservoirOverTopFLZRequest", description = "IsReservoirOverTopFLZ接口请求类")
public class IsReservoirOverTopFLZRequest {
    @ApiModelProperty(value = "md,月日,如：0608。")
    private String md;

    public String getMd() {
        return md;
    }

    public void setMd(String md) {
        this.md = md;
    }

    @ApiModelProperty(value = "bgTm,起始时间。")
    private String bgTm;

    public String getBgTm() {
        return bgTm;
    }

    public void setBgTm(String bgTm) {
        this.bgTm = bgTm;
    }

    @ApiModelProperty(value = "endTm,截至时间。")
    private String endTm;

    public String getEndTm() {
        return endTm;
    }

    public void setEndTm(String endTm) {
        this.endTm = endTm;
    }

    @ApiModelProperty(value = "stationsList,测站编码列表")
    private List<String> stationsList;

    public List<String> getStationsList() {
        return stationsList;
    }

    public void setStationsList(List<String> stationsList) {
        this.stationsList = stationsList;
    }
}
