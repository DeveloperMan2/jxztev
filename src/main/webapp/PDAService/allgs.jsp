<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Date"%>
<%@ page import="org.apache.commons.lang.time.DateFormatUtils"%>
<%@ page import="com.ztev.sl.pda.rmtes.RainMapSvr"%>
<%@ page import="com.ztev.sl.pda.map.RiverWayBean"%>
<%@ page import="com.ztev.sl.pda.map.ReservoirBean"%>
<%
	String rain = "";//保存雨情概述
	String riverway = "";//保存河道概述
	String reservoir = "";//保存水库概述
	RainMapSvr rainMapSvr = new RainMapSvr();
	
	RiverWayBean newRiver = new RiverWayBean();
	List riverList = newRiver.getMapList();
	List overTopWrz = newRiver.getOverTopWrz(riverList);
	
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
	List overTopFlz = newReservoir.getOverTopFLZ(reservoirList);
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
	    	if( tMap.get("CFSLTDZ") != null && tMap.get("STNM") != "三峡"){
	    		sbf.append("，").append(tMap.get("TM")).append("比汛限水位").append(tMap.get("CFSLTDZ")).append("米");
	    	}
	    	reservoir = sbf.toString();
    	}else{
    		reservoir = "系统暂无水库水情数据";
    	}
    }
    reservoir = reservoir + "。";
    
	request.setAttribute("dateTime",DateFormatUtils.format(new Date(), "yyyy月M月d日"));
	request.setAttribute("rain",rainMapSvr.getRainSummary("24"));
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
			<iframe MARGINHEIGHT=0 MARGINWIDTH=0
							FRAMEBORDER=0 width="500" height="200" SCROLLING=NO
							src=/latestnews2.jsp></iframe><br><br>
</fieldset><BR>
	</td>
        </tr>
      </table>
	</td>
</tr>
</table>	
</body>
</html>	