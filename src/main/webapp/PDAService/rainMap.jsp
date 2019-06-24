<%@ page contentType="text/html;charset=utf-8"%> 
<%@ page import="java.util.List"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.lang.Math"%>
<%@ page import="org.apache.commons.lang.time.DateUtils"%>
<%@ page import="org.apache.commons.lang.time.DateFormatUtils"%>
<%@ page import="com.ztev.sl.pda.rmtes.RainMapSvr"%>
<%@ page import="com.ztev.sl.pda.rmtes.CountyRainValue"%>

<%@ include file="commons/taglibs.jsp"%>
<%
	RainMapSvr rainMapSvr = new RainMapSvr();
	String queryHour = request.getParameter("hour") == null?"24":request.getParameter("hour");

	int INT_HOUR = 0;
	try{
		INT_HOUR = Integer.parseInt(queryHour);
	}catch(Exception e){
	}
	
	//开始时间
	String bgTM = DateFormatUtils.format(DateUtils.addHours(new Date(), -INT_HOUR), "M月d日H点");
	String endTM = DateFormatUtils.format(new Date(), "d日H点");//结束时间
	
	//取出县代码、县名、最大雨量、平均雨量
	List<CountyRainValue> list = rainMapSvr.findCountyRainList(queryHour);
	CountyRainValue countyRainValue = null;

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<!-- 设置此页面的刷新时间(单位:秒) -->
<meta http-equiv="refresh" content="300"> 
<title>最新雨情示意图</title>
<link href="styles/default/map.css" rel="stylesheet" type="text/css">
<link href="styles/default/list.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="scripts/rainPos.js"></script>
	<script type="text/javascript">
		function listObj(sStcdt,sStnm,sDyrn){
			this.Stcdt = "S" + sStcdt;
			this.Stnm  = sStnm;
			this.Dyrn  = sDyrn;
		}

		//-----------------------------------------------------------
		//在记录集中查找记录,找到返回值>=0, 否则为-1
		//-----------------------------------------------------------		
		function find(findStr){		
			var str = findStr;
			
			var found=false;
			for (var i = 0; i < cachedList.length; i++) {
				if (cachedList[i].Stcdt==str ){
					found=true;
					break;
				}			
				
			}		
			
			if (found){
				return i;
			}
			else{
				return -1;
			}
		}
		
		var oSource;
		
		function dispRsvrInfo(){
		
			oSource = window.event.srcElement;
			//window.alert (oSource.coords);
			//如果当前窗口的激活元素为area
			if (oSource.tagName == "AREA"){
				var  sId;
				sId = oSource.id;
				
				//找到该站点的信息，改变菜单的内容
				changeMenuContent(sId);
							
			}
			
			//window.event.returnValue = false;
			
		}
		
		//找到该站点的信息，改变菜单的内容
		function changeMenuContent(sId){
			//根据area的ID找到该站点的信息
			var iRet ;  //查询站点结果
			iRet = -1;
			
			iRet = find(sId);
			//window.alert(iRet);
			
			if (iRet >= 0 ){
				tmpstr = sId.replace("S","i");
				tempstcd = tmpstr;
				tempsrc=document.getElementById(tmpstr).src;
				document.getElementById(tmpstr).src='styles/default/images/map/green.gif'
				//改变菜单的内容
				window.stnm.innerText  = cachedList[iRet].Stnm+' '+cachedList[iRet].Dyrn+' 毫米';
				//window.ymdhm.innerText  = cachedList[iRet].Ymdhm;
				//window.dyrn.innerText  = cachedList[iRet].Dyrn+' 毫米';
			
				
				//以菜单形式显示该站的信息
				displayMenu();
			}
			else{
				window.alert("没有找到该站点的信息!"); 
			}
		}
		
		//显示菜单
		function displayMenu(){

		   iY = window.event.y;
		   if ( window.event.y + 90 > document.body.offsetHeight - 20  )
				iY = iY - 60 ;
		   else
				iY = iY + 20;
				
		   //window.status = ' Y=' + iY + ' body height:' + document.body.offsetHeight + ' body width:' + document.body.offsetWidth + ' window.event.x:='+window.event.x+ ' window.event.y:='+window.event.y;

		   iX = window.event.x	- 90;	
		   mesgMenu.style.left = iX  ;
		   mesgMenu.style.top  = iY  ;
		   mesgMenu.style.zIndex = "10";
		   mesgMenu.style.display = "";
		   mesgMenu.setCapture();
		}
		
		function makeMenuHidden() {
			document.getElementById(tempstcd).src=tempsrc;
			mesgMenu.releaseCapture();
			mesgMenu.style.display="none";
		}
		//提交表单
		function onclicks(){
		   document.forms[0].submit();
		}
	</script>
</head>

<body style="MARGIN-TOP: 0px;MARGIN-LEFT: 0px;MARGIN-RIGHT: 0px; MARGIN-BOTTOM: 0px">
<center>
<img border="0" height="580" id="mainMap" src="styles/default/images/map/rainMap.gif" useMap="dispRainMap.asp#SystemMap">

<map NAME="SystemMap">

</map>
</center>

<%

for(int i = 0; i < list.size(); i++){
	countyRainValue = list.get(i);
	boolean isAlarm = false;
	if(countyRainValue.getMaxRain() != null && countyRainValue.getMaxRain() > 0){
    	
    	//判断是否超过50mm
    	if(countyRainValue.getMaxRain()>50 ){
    		isAlarm = true;
    	}


%> 
		<DIV onMouseOut="makeMenuHidden()" onMouseOver="changeMenuContent('S<%=countyRainValue.getAddvcd()%>')" id=W<%=countyRainValue.getAddvcd()%>
		 style="Z-INDEX: 1;WIDTH: 8; POSITION: absolute; HEIGHT: 8;Cursor: hand; Display: none">
		<table cellSpacing=0 cellPadding=0 border=0>
		<tr align="left" width="40">
			<td width="32"><img id=i<%=countyRainValue.getAddvcd()%> border="0" src="styles/default/images/map/<%if(isAlarm){out.print("alarm.gif");}else{out.print("normal.gif");}%>"></td></tr>
		<tr align="left">
			<td >
			<table cellSpacing=0 cellPadding=0 border=0 >
				<tr align="center">
					<td <%if(isAlarm){out.print("class='bodyred'");}else{out.print("class='bodymin'");}%>>
					<%=Math.round(countyRainValue.getMaxRain())%>
					</td>
				</tr>
			</table>
			</td>
		</tr>
		</table>
		</DIV>
<%
    }
}
%>

<div id="mesgMenu" style="position:absolute;display:none;width:140;background-Color:menu; border:0;" onMouseOut="makeMenuHidden()" onMouseOver="makeMenuHidden()">
<table cellSpacing=1 cellPadding=0 bgColor=#FDB771 border=0>
  <tr class="bodytd" bgcolor="#FFFFFF">
    <td width=140 height=24 id="stnm" align=center>
    </td>
</tr></table>
</div>

<div id="construction" style="position:absolute;display:;left:283px;top:490px;width:200;height=50;border:0; font-size:16px ">
<fieldset style='padding:0 10;'>
<legend>操作提示：</legend>
  <table bgcolor="#FFFFFF" height=50>
    <tr><td class="construction">
    　　当鼠标移雨量数字时，系统将自动显示该县名称及最大降雨量。
    
    </td></tr>
 </table>
 </fieldset>
</div>

<div id="mapTitle" style="position:absolute;display:none;left:28px;top:2px;width:390;border:0;" >
  <table>
    <tr><td width="380">
    <span style="font-size:24px;font-family: 黑体;">全省实时降雨量监测</span></td>
	</tr>
 </table>
</div>


<div id="timeDiv" style="position:absolute;display:none; LEFT: 600; width:200; TOP: 280 ;border:0; font-size:16px">
<!--最大雨量列表-->
<fieldset style='padding:0 10;'>
<legend>雨量前20位站点(毫米)</legend>
	<table width=180 border=0 class=list>
	<tr height=24 valign=middle>
	<td width=35% align=center class="listhead">站名</td>
	<td width=35% align=center class="listhead">所在县</td>
	<td width=30% align=center class="listhead">雨量</td>
	</tr></table>
	
	<div style="folat:left;clear:right;width:180px;height:140px;overflow:auto;">
		<table width=100% border=0 cellspacing=0 class=list>
<%
	List<CountyRainValue> aMaxRain = rainMapSvr.getMaxRainOrderRain(queryHour, 20);

	for(int i = 0 ; i < aMaxRain.size(); i ++ ){
		countyRainValue = aMaxRain.get(i);
		if(countyRainValue.getMaxRain() != null && countyRainValue.getMaxRain() > 0){
%>
		<tr>
			<td color=#ffffff width=40% height=24><font style="font-size: 12px; font-family: Arial;color:#222222;">
			<%=countyRainValue.getStnm().trim()%></font></td>
			<td color=#ffffff align=center width=38%><font style="font-size: 12px; font-family: Arial;font-weight:bold;" >
			<%=countyRainValue.getCnnm().trim()%></font></td>
			<td color=#ffffff align=center>
			<%
				if(countyRainValue.getMaxRain() >= 50){
					out.print("<font style='font-size: 12px; font-family: Arial;' color=red>" + Math.round(countyRainValue.getMaxRain()));
				}else{
					out.print("<font style='font-size: 12px; font-family: Arial;' color=blue>" + Math.round(countyRainValue.getMaxRain()));
				}
			%></font></td>
		</tr>
<%
		}
		
	}
%>
		</table>			
	</div>

</fieldset>
</div>

<div id="tuli" style="position:absolute;display:none;left:580;top:470;width:200;border:0 ">
<table>
    <td valign="middle">
    <img src="styles/default/images/map/raintuli.gif">
    </td>
</table>
</div>

<div id="queryToolbar" style="position:absolute;display:none;left:28px;top:206px;width:240;border:0;">
	<table cellpadding="0" cellspacing="0" width="100%" align="center" border="0">
	<form name="form1" action="rainMap.jsp" method="post">
	<tr><td align="center">
	<table width="265">
	<tr class='bodymin'>
		<td align="center">
		<label for="rdo_1" style="cursor:hand">
		<input type="radio" name="hour" value="6" id="rdo_1"
		 <%if(queryHour.equals("6")){out.print("checked onclick='onclicks()'><font color='red'>6</font>");}else{out.print(" onclick='onclicks()'>6");}%></label>
		&nbsp;<label for="rdo_2" style="cursor:hand"><input type="radio" name="hour" value="12" id="rdo_2" 
		<%if(queryHour.equals("12")){out.print("checked onclick='onclicks()'><font color='red'>12</font>");}else{out.print(" onclick='onclicks()'>12");}%></label>
		&nbsp;&nbsp;&nbsp;
		</td>
	</tr>
	<tr class='bodymin'>
		<td align="center">最近
		<label for="rdo_3" style="cursor:hand"><input type="radio" name="hour" value="24" id="rdo_3" 
		<%if(queryHour.equals("24")){out.print("checked onclick='onclicks()'><font color='red'>24</font>");}else{out.print(" onclick='onclicks()'>24");}%></label>
		<label for="rdo_4" style="cursor:hand"><input type="radio" name="hour" value="48" id="rdo_4" 
		<%if(queryHour.equals("48")){out.print("checked onclick='onclicks()'><font color='red'>48</font>");}else{out.print(" onclick='onclicks()'>48");}%></label>
		小时降雨
		</td>
	</tr>
	</form></table>
	<span style="font-size:16px;"><%
		out.print(bgTM + " ~ " + endTM);
	%></span>
</div>


</body>
</html>
<script language="javascript">

	cachedList = new Array();
	
	<%for(int i=0;i<list.size();i++){
		countyRainValue = list.get(i);
		if(countyRainValue.getMaxRain() != null && countyRainValue.getMaxRain() > 0){
	%>
			cachedList[<%=i%>] = new listObj('<%=countyRainValue.getAddvcd()%>','<%=countyRainValue.getCnnm()%>','<%=Math.round(countyRainValue.getMaxRain())%>');
	<%
		}
	}
	%>
	
	function changePos(){
		mapTitle.style.left = document.body.offsetWidth/2 - 250;
		//mapTitle.style.display = '';
		queryToolbar.style.left = document.body.offsetWidth/2 + 120;
		queryToolbar.style.display = '';
		tuli.style.left = document.body.offsetWidth/2 + 150;
		tuli.style.display = '';
		timeDiv.style.left = document.body.offsetWidth/2 + 150;
		<%if(aMaxRain.size() > 0){
			countyRainValue = aMaxRain.get(0);//无雨则不显示雨量表
			if(countyRainValue.getMaxRain()>0){
				out.println("timeDiv.style.display = \"\" ;");
			}
		}%>
		
		<%for(int i=0;i<list.size();i++){
			countyRainValue = list.get(i);
			if(countyRainValue.getMaxRain() != null && countyRainValue.getMaxRain() > 0){
		%>
				try{
					W<%=countyRainValue.getAddvcd()%>.style.left =  (document.body.offsetWidth-396)/2 - 50 + parseFloat(mlist['<%=countyRainValue.getAddvcd()%>'].split(",")[0]);
					W<%=countyRainValue.getAddvcd()%>.style.top  = parseFloat(mlist['<%=countyRainValue.getAddvcd()%>'].split(",")[1]) - 4;
					W<%=countyRainValue.getAddvcd()%>.style.display = '';
				}catch(e){
					//alert(""+e.message);
				}
		<%
			}
		}
		%>
		
		
		construction.style.left = document.body.offsetWidth/2-360;
		construction.style.display = '';

	}
	
	changePos();
	
	var interval; 
	
	interval = setInterval('changePos()', 30);
 
</script>