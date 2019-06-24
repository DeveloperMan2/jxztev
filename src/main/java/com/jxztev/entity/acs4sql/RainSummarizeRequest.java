package com.jxztev.entity.acs4sql;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "RainSummarizeRequest", description = "RainSummarize接口请求类")
public class RainSummarizeRequest {
    @ApiModelProperty(value = "tm,这是一个别名变量。")
    private String tm;

    public String getTm() {
        return tm;
    }

    public void setTm(String tm) {
        this.tm = tm;
    }

    @ApiModelProperty(value = "maxrain,这是一个别名变量。")
    private Float maxrain;

    public Float getMaxrain() {
        return maxrain;
    }

    public void setMaxrain(Float maxrain) {
        this.maxrain = maxrain;
    }
}
