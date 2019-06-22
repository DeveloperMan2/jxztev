package com.jxztev.entity.acs4sql;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "IsReservoirOverTopFLZResponse", description = "IsReservoirOverTopFLZ接口响应类")
public class IsReservoirOverTopFLZResponse{
	@ApiModelProperty(value = "cfsltdz,这是一个别名变量。")
	private String cfsltdz;

	public String getCfsltdz() {
		return cfsltdz;
	}

	public void setCfsltdz(String cfsltdz) {
		this.cfsltdz = cfsltdz;
	}
	
}
