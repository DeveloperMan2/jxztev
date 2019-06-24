package com.jxztev.entity.acs4sql;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "RainSummarizeResponse", description = "RainSummarize接口响应类")
public class RainSummarizeResponse{
	@ApiModelProperty(value = "stcd,这是一个别名变量。")
	private String stcd;

	public String getStcd() {
		return stcd;
	}

	public void setStcd(String stcd) {
		this.stcd = stcd;
	}
	
	@ApiModelProperty(value = "cnnm,这是一个别名变量。")
	private String cnnm;

	public String getCnnm() {
		return cnnm;
	}

	public void setCnnm(String cnnm) {
		this.cnnm = cnnm;
	}
	
	@ApiModelProperty(value = "stnm,这是一个别名变量。")
	private String stnm;

	public String getStnm() {
		return stnm;
	}

	public void setStnm(String stnm) {
		this.stnm = stnm;
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
