package com.jxztev.entity.acs4sql;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "FindSlideRainRequest", description = "FindSlideRain接口请求类")
public class FindSlideRainRequest {
    @ApiModelProperty(value = "hours,小时。", example = "1")
    private Integer hours;

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    @ApiModelProperty(value = "period,周期。", example = "1")
    private Integer period;

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    @ApiModelProperty(value = "drp。", example = "1")
    private Float drp;

    public Float getDrp() {
        return drp;
    }

    public void setDrp(Float drp) {
        this.drp = drp;
    }
}
