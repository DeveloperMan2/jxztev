package com.jxztev.entity.acs4sql;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "IsRiverOverTopWLResponse", description = "IsRiverOverTopWL接口响应类")
public class IsRiverOverTopWLResponse{
	@ApiModelProperty(value = "cwrz,这是一个别名变量。")
	private String cwrz;

	public String getCwrz() {
		return cwrz;
	}

	public void setCwrz(String cwrz) {
		this.cwrz = cwrz;
	}
	
}
