<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.ztev.commons.date.DateUtils"%>
<%@ page import="com.ztev.sl.pda.map.RiverWayBean"%>
<%@ include file="commons/taglibs.jsp"%>
<%
	String title = "最新河道水情示意图";
	RiverWayBean newRiver = new RiverWayBean();
	List list = newRiver.getMapList();
	request.setAttribute("rows",list);
	/** 左上显示的站点**/
	request.setAttribute("upCodes","62304150");
	
	List overTopWrz = newRiver.getOverTopWrz(list);
	/** 超警戒的测站信息**/
	request.setAttribute("overTopWrz",overTopWrz);
	/** 超警戒的测站数量**/
	request.setAttribute("overTopWrzSize",overTopWrz.size());
	String summarize = "";//保存河道水情概述信息
	StringBuffer sbf = new StringBuffer();
	for( int i = 0; i < overTopWrz.size(); i++ ){
       	Map tMap = (Map) overTopWrz.get(i);
   		if(i > 0){
   			sbf.append("；");//不是第一个则加；分隔
   		}
   		sbf.append(tMap.get("County"))
    	.append(" <font color='red'>").append(tMap.get("STNM")).append("站</font>")
    	.append(tMap.get("HTM")).append("超警戒水位")
    	.append("<font color='red'>").append(tMap.get("CWRZ")).append("</font>").append("米");
    }
    if( overTopWrz.size() == 1 ){
    	summarize = sbf.toString();
    }else if( overTopWrz.size() > 1 ){
    	summarize = "有<font color='red'>" + overTopWrz.size() + "</font>个站超警戒，如下";
    }else{
    	if(list.size() > 0){
    		Map tMap = (Map) list.get(0);
        	sbf = new StringBuffer();
        	sbf.append("各江河重点站均在警戒水位以下，其中离警戒水位最近的是")
        	.append(tMap.get("County")).append(" ").append(tMap.get("STNM")).append("站");
	    	if( tMap.get("CWRZ") != null ){
	    		sbf.append("，").append(tMap.get("HTM")).append("比警戒水位").append(tMap.get("CWRZ")).append("米").append("。");
	    	}
	    	summarize = sbf.toString();
    	}else{
    		summarize = "系统暂无河道水情数据！";
    	}
    }
	summarize = DateUtils.getTodayString("M月d日") + summarize ;
	/**设置水库水情概述 **/
	request.setAttribute("summarize",summarize);
	
	
%>
<%@ page contentType="text/html;charset=utf-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><%=title%></title>
<link href="styles/default/list.css" rel="stylesheet" type="text/css">
<style>
	body{background-color:#FFFFFF;margin-top: 0px;margin-left: 0px;margin-right: 0px; margin-bottom: 0px}
	TD{font-size: 14px;}
	TR.headtd{font-size: 14px; color: #ffffff;}
	TR.bodytd{font-size: 14px; color: #000000; font-weight: bold;}
	TD.warn{font-family: "Fixedsys"; font-size: 14px; color: #ff0000;}
	div.warn{font-family: "Fixedsys"; font-size: 14px; color: #ff0000;}
	TD.bodyblack{font-size: 14px; color: #000000; font-weight: bold;}
	TD.bodyhui{font-size: 14px; color: #333333; font-weight: bold;}
	TD.normal{font-family: "Fixedsys"; font-size: 14px; color: #222222;}
	div.normal{font-family: "Fixedsys"; font-size: 14px; color: #222222;}
	.suoming{line-height:24px; font-size: 14px; color: #000000;}
</style>
<script language="javascript" src="scripts/riverwayPos.js"></script>
<script language="javascript">
	var oSource;
	var tempsrc="";//保存样式
	var tempstcd="";//保存站号

	function isIE(){ //ie? 
	    if (window.navigator.userAgent.toLowerCase().indexOf("msie")>=1) 
	        return true; 
	    else 
	        return false; 
	} 
	
	if(!isIE()){ //firefox innerText define
	    HTMLElement.prototype.__defineGetter__(    "innerText", 
	        function(){ 
	            return this.textContent.replace(/(^\s*)|(\s*$)/g, "");
	        } 
	    ); 
	    HTMLElement.prototype.__defineSetter__(    "innerText", 
	        function(sText){ 
	            this.textContent=sText; 
	        } 
	    ); 
	}
	function listObj(sStcdt,sStnm,sYmdhm, sZ, sQ, sW, sH, sS){
		this.Stcdt = "S" + sStcdt;//站号
		this.Stnm  = sStnm;//站名
		this.Ymdhm = sYmdhm;//时间
		this.Z     = sZ;//当前水位
		this.Q     = sQ;//流量
		this.W     = sW;//警戒水位
		this.H     = sH;//历史最高水位
		this.S     = sS;//水势
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
			document.getElementById("stnm").innerText  = cachedList[iRet].Stnm;
			document.getElementById("ymdhm").innerText = cachedList[iRet].Ymdhm;
			document.getElementById("z").innerText = cachedList[iRet].Z;
			document.getElementById("q").innerText = cachedList[iRet].Q;
			document.getElementById("w").innerText = cachedList[iRet].W;
			document.getElementById("h").innerText = cachedList[iRet].H;
			document.getElementById("s").innerText = cachedList[iRet].S;
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
	   if ( window.event.y + document.body.scrollTop + 52 + 40 > document.body.offsetHeight - 200  ){
			iY = iY + document.body.scrollTop - 100;
		}else{
			iY = iY + 40;
		}
	   //window.status = ' Y=' + iY + ' body height:' + document.body.offsetHeight + ' body width:' + document.body.offsetWidth + ' window.event.x:='+window.event.x+ ' window.event.y:='+window.event.y + ' document.body.scrollTop='+document.body.scrollTop;
	   iX = document.body.offsetWidth/2 - 260;  
	   mesgMenu.style.left = iX  ;
	   mesgMenu.style.top  = iY  ;
	   mesgMenu.style.zIndex = "100";
	   mesgMenu.style.display = "";
	}

	function makeMenuHidden() {
	   document.getElementById(tempstcd).src=tempsrc;
	   mesgMenu.style.display = 'none';
	}
	</script>
</head>
<body>
<center>
<img border="0" id="mainMap" src="styles/default/images/map/riverMap.gif" useMap="#SystemMap">
  <map name="SystemMap">
  </map>
</center>
	<c:forEach var="row" items="${rows}" varStatus="status">
		<c:if test="${fn:indexOf(upCodes,row.STCD) == -1}">
		<!-- 右下显示 -->
		<DIV onMouseOut="makeMenuHidden()" onMouseOver="changeMenuContent('S${row.STCD}')"   
		 id=W${row.STCD} style=" POSITION: absolute; Z-INDEX: 1; WIDTH: 8;HEIGHT: 8; CURSOR:hand; DISPLAY:none">
			<TABLE cellSpacing=0 cellPadding=0 border=0>
				<tr align="left">
					<td><img width="10" id=i${row.STCD} border="0" src="styles/default/images/map/${row.img}"></td>
				</tr>
				<tr align="left">
					<td >
					<TABLE cellSpacing=0 cellPadding=0 border=0 >
						<tr align="center">
							<td class="${row.style}">
							${row.Z}
							</td>
						</tr>
					</TABLE>
				</td></tr>
			</TABLE>
		</DIV>
		</c:if>
		
		<c:if test="${fn:indexOf(upCodes,row.STCD) != -1}">
		<!-- 左上显示 -->
		<DIV onMouseOut="makeMenuHidden()" onMouseOver="changeMenuContent('S${row.STCD}')"   
		 id=W${row.STCD} style=" POSITION: absolute; Z-INDEX: 1; WIDTH: 8; HEIGHT: 8; CURSOR:hand; DISPLAY:none">
			<DIV style="position:absolute;text-align:right;" class="${row.style}">
			<fmt:formatNumber value="${row.Z}" minFractionDigits="2" maxFractionDigits="2"/>
			</DIV>
			<TABLE cellSpacing=0 cellPadding=0 border=0 width='40' height='28'>
				<tr align="right" valign="bottom">
					<td><img width="10" id=i${row.STCD} border="0" src="styles/default/images/map/${row.img}"></td>
				</tr>
			</TABLE>
		</DIV>
		
		</c:if>
	</c:forEach>
	
   <div class="space"></div>
   
<div id="mesgMenu" style="position:absolute;display:none;z-index:100;width:520;background-Color:menu; border: 1"
	onMouseOut="makeMenuHidden()" onMouseOver="makeMenuHidden()">
	
<TABLE cellSpacing=0 cellPadding=0 bgColor=#ffffcc border=0>
  <TBODY>
  <TR bgColor=#003333>
    <TD width=75 height=28 align=center id = "td_stnm">
      <span align=center><FONT color=#ffffff>站名</FONT></span>
      </TD>
    <TD width=140 align=center id = "td_ymdhm">
      <span><FONT color=#ffffff>时间</FONT></span>
      </TD>
    <TD width=60 align=center id = "td_z">
      <span><FONT color=#ffffff>水位</FONT></span>
      </TD>
      <TD width=75 align=center id = "td_w">
      <span><FONT color=#ffffff>警戒水位</FONT></span>
      </TD>
      <TD width=75 align=center id = "td_h">
      <span><FONT color=#ffffff>历史最高</FONT></span>
      </TD>
	 <TD width=60 align=center id = "td_q">
      <span><FONT color=#ffffff>流量</FONT></span>
      </TD>
      <TD width=40 align=center id = "td_s">
      <span><FONT color=#ffffff>水势</FONT></span>
      </TD>
      
</TR>
  <TR>
    <TD height=24 id="stnm" align=center class="bodyblack">
    </TD>
    <TD bgColor=#ffffe8 height=24 id="ymdhm" align=center class="bodyhui">
    </TD>
    <TD bgColor=#ffffe8 height=24 id="z" align=center class="bodyblack">
    </TD>
    <TD bgColor=#ffffe8 height=24 id="w" align=center class="bodyblack">
    </TD>
    <TD bgColor=#ffffe8 height=24 id="h" align=center class="bodyblack">
    </TD>
    <TD bgColor=#ffffe8 height=24 id="q" align=center class="bodyblack">
    </TD>
    <TD bgColor=#ffffe8 height=24 id="s" align=center class="bodyblack">
    </TD>
</TR></TBODY></TABLE>
</div>

<div id="construction" style="position:absolute;display:none;left:129px;top:475px;width:175;border:1 ">
  <table>
    <tr><td class="suoming"><span style="color: #30899F; font-weight:bold;">操作提示：</span><br>
    　　鼠标移至水位站点或水位数据时，系统自动显示该站水位、警戒水位、流量、水势等信息。
    
    </td></tr>
 </table>
</div>

<div id="hdgs" style="position:absolute;display:none;z-index:10;left:540;top:275;width:240;border:1;font-size:16px ">
	<!--河道水情概述-->
	<fieldset style='padding:10 10;'>
	<legend>　　全省江河水情概述</legend>
	　${summarize}<br>
		<c:if test="${ overTopWrzSize > 0}">
		<table cellpadding="0" cellspacing="1" align="center" class="main" width="210">
		<tr><th>站名</th><th>当前水位</th><th>超警戒水位</th><th>警戒水位</th></tr>
		<c:forEach var="row" items="${overTopWrz}" varStatus="status">
		<tr align="left" class='default' onfocusin="this.className='selected'" onfocusout="this.className='default'">
		<td>${row.STNM}</td><td>${row.Z}</td><td>${row.CWRZ}</td><td>${row.WRZ}</td></tr>
		</c:forEach>
		</table>
	</c:if>
	</fieldset>
</div>

<div id="tuli" style="position:absolute;display:none;left:540;top:480;width:240;border:1 ">
  <img src="styles/default/images/map/rivertuli.gif">
  <table>
    <td align="center" valign="middle">&nbsp;
    </td>
 </table>
</div>

<div class="space"></div>
</body>
<script language="javascript">

	cachedList = new Array();
	
	<c:forEach var="row" items="${rows}" varStatus="status">
			cachedList[${status.index}] = new listObj('${row.STCD}','${row.STNM}','${row.TM}','${row.Z}','${row.Q}','${row.WRZ}','${row.OBHTZ}','${row.WPTN}');
	</c:forEach>
	function changePos(){
		construction.style.left = document.body.offsetWidth/2 - 360;
		construction.style.display = "";
		tuli.style.left =  document.body.offsetWidth/2+58;
		tuli.style.display = "";
		hdgs.style.pixelLeft = document.body.offsetWidth/2+60;
		hdgs.style.display = "";
		
		<c:forEach var="row" items="${rows}" varStatus="status">
			try{
				W${row.STCD}.style.left =  (document.body.offsetWidth-394)/2 - 40 + parseFloat(mlist['${row.STCD}'].split(",")[0]);
				W${row.STCD}.style.top  = parseFloat(mlist['${row.STCD}'].split(",")[1]) - 5;
				W${row.STCD}.style.display = '';
			}catch(e){
				//alert(""+e.message);
			}
		</c:forEach>	
	}
	
	changePos();
	
	var interval; 
	
	interval = setInterval('changePos()', 100);
 
</script>
</html>