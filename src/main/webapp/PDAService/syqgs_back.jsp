<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.ztev.sl.pda.map.RainForMapBean"%>
<%@ page import="com.ztev.sl.pda.map.RiverWayBean"%>
<%@ page import="com.ztev.sl.pda.map.ReservoirBean"%>
<%@ page import="com.ztev.commons.date.DateUtils"%>
<%
	String rain = "";//保存雨情概述
	String riverway = "";//保存河道概述
	String reservoir = "";//保存水库概述
	
	RainForMapBean rainBean = new RainForMapBean();
	//取出县代码、县名、最大雨量、平均雨量
	String[][] result = rainBean.getNewRain("24");
	String bgTM = DateUtils.getSpaceTime("d日H时",0,-24);//开始时间
	String endTM = DateUtils.getTodayString("d日H时");//结束时间
	
	/** 统计雨量的代码很绰**/
	
	StringBuffer brsbf = new StringBuffer("");//保存暴雨信息
	StringBuffer dbrsbf = new StringBuffer("");//保存大暴雨信息
	
	String detail = "";
	int zr = 0;//保存中雨的站点数
	int dr = 0;//保存大雨的站点数
	int br = 0;//保存暴雨的站点数
	int dbr = 0;//保存大暴雨的站点数
	int flag = 0;//标志位
	
     for(int i=0; i < result.length; i++){
     	if(result[i][2]!=null){
     		result[i][2] = String.valueOf(Math.round(Float.parseFloat(result[i][2])));//取一位小数

     		if(Float.parseFloat(result[i][2])>0){
     			flag = 1;//有雨的标志
     		}
			if(Float.parseFloat(result[i][2])>=10 && Float.parseFloat(result[i][2])<24){	
				zr++;//中雨
			}
		  	if(Float.parseFloat(result[i][2])>=25 && Float.parseFloat(result[i][2])<49){	
				dr++;//大雨
		  	}
			if(Float.parseFloat(result[i][2])>=50 && Float.parseFloat(result[i][2])<99){	
				br++;//暴雨
				if(br>1){
					brsbf = brsbf.append("、");
				}
				brsbf = brsbf.append("<font color='red'>").append(result[i][1]).append("</font>").append(result[i][2]).append("毫米");
			}
			if(Float.parseFloat(result[i][2])>=100 ){	
				dbr++;//大暴雨
				if(dbr>1){
					dbrsbf = dbrsbf.append("、");
				}
				dbrsbf = dbrsbf.append("<font color='red'>").append(result[i][1]).append("</font>").append(result[i][2]).append("毫米");
			}
		}
	}

  	if(dbr>0){
  		detail=detail+"有<font color='red'>"+dbr+"</font>个县下了大暴雨，"+dbrsbf;
		flag = flag + 1000;
	}
	if(br>0){
		if(flag>1){
			detail=detail+"；<br>";
		}
		detail=detail+"有<font color='red'>"+br+"</font>个县下了暴雨，"+brsbf;
		flag = flag + 1000;
	}
	if(dr>0){
		if(flag>1)
			detail=detail+";<br>";
		detail=detail+"有"+dr+"个县下了大雨";
		flag = flag + 100;
	}
	if(zr>0){
		if(flag>1){
			detail=detail+"；";
		}
		detail=detail+"有"+zr+"个县下了中雨";
		flag = flag + 10;
	}
	if(flag==1){
		detail = detail+"部分地方下了小雨";
	}
	if(flag==0){
		detail = detail+"无雨";
	}
 	if(flag < 1000 && flag > 0){
  		detail=detail+"；其中";
		detail = detail + result[0][1] + result[0][2] + "毫米最大";
  	}
	rain = bgTM + "~" + endTM +"全省" + detail + "。";
	
	
	RiverWayBean newRiver = new RiverWayBean();
	List riverList = newRiver.getMapList();
	List overTopWrz = newRiver.getOverTopWrz();
	
	/** 产生河道概述**/
	StringBuffer sbf = new StringBuffer();
	for( int i = 0; i < overTopWrz.size(); i++ ){
       	Map tMap = (Map) overTopWrz.get(i);
   		if(i > 0){
   			sbf.append("；<br>　　　　　");//不是第一个则加；分隔
   		}
   		sbf.append(tMap.get("County"))
    	.append(" <font color='red'>").append(tMap.get("STNM")).append("站</font>").append(tMap.get("TM"))
    	.append("超警戒水位").append("<font color='red'>").append(tMap.get("CWRZ")).append("</font>").append("米");
    }
    if( overTopWrz.size() == 1 ){
    	riverway = sbf.toString();
    }else if( overTopWrz.size() > 1 ){
    	riverway = "有<font color='red'>" + overTopWrz.size() + "</font>个站超警戒，" + sbf.toString() ;
    }else{
    	if(riverList.size() > 0){
    		Map tMap = (Map) riverList.get(0);
        	sbf = new StringBuffer();
        	sbf.append("各江河重点站均在警戒水位以下，其中离警戒水位最近的是")
        	.append(tMap.get("County")).append(" ").append(tMap.get("STNM")).append("站");
	    	if( tMap.get("CWRZ") != null ){
	    		sbf.append("，").append(tMap.get("TM")).append("比警戒水位").append(tMap.get("CWRZ")).append("米");
	    	}
	    	riverway = sbf.toString();
    	}else{
    		riverway = "系统暂无河道水情数据";
    	}
    }
    riverway = riverway + "。";
    
    /** 产生水库概述**/
	ReservoirBean newReservoir = new ReservoirBean();
	List reservoirList = newReservoir.getMapList();
	List overTopFlz = newReservoir.getOverTopFLZ();
	sbf = new StringBuffer("");
	
	for( int i = 0; i < overTopFlz.size(); i++ ){
       	Map tMap = (Map) overTopFlz.get(i);
   		if(i > 0){
   			sbf.append("；<br>　　　　　");//不是第一个则加；分隔
   		}
   		sbf.append(tMap.get("County"))
    	.append(" <font color='red'>").append(tMap.get("STNM")).append("水库</font>").append(tMap.get("TM"))
    	.append("超汛限水位").append("<font color='red'>").append(tMap.get("CFSLTDZ")).append("</font>").append("米");
    }
    if( overTopFlz.size() == 1 ){
    	reservoir = sbf.toString();
    }else if( overTopFlz.size() > 1 ){
    	reservoir = "有<font color='red'>" + overTopFlz.size() + "</font>个水库超汛限，"  + sbf.toString() ;
    }else{
    	if(reservoirList.size() > 0){
    		Map tMap = (Map) reservoirList.get(0);
        	sbf = new StringBuffer();
        	sbf.append("各大中型水库均在汛限水位以下，其中离汛限水位最近的是")
        	.append(tMap.get("County")).append(" ").append(tMap.get("STNM")).append("水库");
	    	if( tMap.get("CFSLTDZ") != null ){
	    		sbf.append("，").append(tMap.get("TM")).append("比汛限水位").append(tMap.get("CFSLTDZ")).append("米");
	    	}
	    	reservoir = sbf.toString();
    	}else{
    		reservoir = "系统暂无水库水情数据";
    	}
    }
    reservoir = reservoir + "。";
    
	request.setAttribute("dateTime",DateUtils.getTodayString("yyyy月M月d日"));
	request.setAttribute("rain",rain);
	request.setAttribute("riverway",riverway);
	request.setAttribute("reservoir",reservoir);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Pragma" content="no-cache"> 
<title>全省水雨情概述</title>
</head>
<body>
<table cellpadding="0" cellspacing="0" width="95%" align="center">
<tr><td align="center" width="100%">
<BR>全省水雨情概述<BR>
${dateTime}
</td>
</tr>
<tr>
	<td>
      <table width="100%" border="0">
        <tr>
          <td><BR><fieldset style='padding:10 10;'>
			雨情：${rain}<br><br>
			江河水情：${riverway}<br><br>
			水库水情：${reservoir}<br><br>
			</fieldset><BR>
			</td>
        </tr>
      </table>
	</td>
</tr>
</table>	
</body>
</html>	