<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.ztev.sl.pda.map.ReservoirBean"%>
<%@ page import="com.ztev.commons.date.DateUtils"%>
<%@ include file="commons/taglibs.jsp"%>
<%
	String title = "最新水库水情示意图";
	ReservoirBean newReservoir = new ReservoirBean();
	List list = newReservoir.getMapList();
	request.setAttribute("rows",list);
	/** 左上显示的站点**/
	request.setAttribute("upCodes","");
	
	List overTopFlz = newReservoir.getOverTopFLZ(list);
	
	/** 超汛限的水库信息**/
	request.setAttribute("overTopFLZ",overTopFlz);
	/** 超汛限的水库数量**/
	request.setAttribute("overTopFLZSize",overTopFlz.size());
	
	String summarize = "";//保存水库水情概述信息
	StringBuffer sbf = new StringBuffer();
	for( int i = 0; i < overTopFlz.size(); i++ ){
       	Map tMap = (Map) overTopFlz.get(i);
   		if(i > 0){
   			sbf.append("；");//不是第一个则加；分隔
   		}
   		sbf.append(tMap.get("County"))
    	.append(" <font color='red'>").append(tMap.get("STNM")).append("水库</font>")
    	.append(tMap.get("HTM")).append("超汛限水位")
    	.append("<font color='red'>").append(tMap.get("CFSLTDZ")).append("</font>").append("米");
    }
    if( overTopFlz.size() == 1 ){
    	summarize = sbf.toString();
    }else if( overTopFlz.size() > 1 ){
    	summarize = "有<font color='red'>" + overTopFlz.size() + "</font>个水库超汛限，分别是：" + sbf.toString() ;
    }else{
    	if(list.size() > 0){
    		Map tMap = (Map) list.get(0);
        	sbf = new StringBuffer();
        	sbf.append("各大中型水库均在汛限水位以下，其中离汛限水位最近的是")
        	.append(tMap.get("County")).append(" ").append(tMap.get("STNM")).append("水库");
	    	if( tMap.get("CFSLTDZ") != null && tMap.get("STNM") != "三峡"){
	    		sbf.append("，").append(tMap.get("HTM")).append("比汛限水位").append(tMap.get("CFSLTDZ")).append("米");
	    	}
	    	summarize = sbf.toString();
    	}else{
    		summarize = "系统暂无水库水情数据";
    	}
    }
	summarize = DateUtils.getTodayString("M月d日") + summarize + "。" ;
	/**设置水库水情概述 **/
	request.setAttribute("summarize",summarize);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="styles/default/list.css" rel="stylesheet" type="text/css">
<title><%=title%></title>
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
<script language="javascript" src="scripts/reservoirPos.js"></script>
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
	function listObj(sStcdt,sStnm,sYmdhm, sZ, sQ, sW, sS){
		this.Stcdt = "S" + sStcdt;//站号
		this.Stnm  = sStnm;//站名
		this.Ymdhm = sYmdhm;//时间
		this.Z     = sZ;//水位
		this.Q     = sQ;//流量(入库流量/出库流量)
		this.W     = sW;//警戒水位(汛限水位)
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
	   iX = document.body.offsetWidth/2 - 240;  
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
<img border="0" id="mainMap" src="styles/default/images/map/reservoirMap.gif" useMap="#SystemMap">
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
							<fmt:formatNumber value="${row.RZ}" minFractionDigits="2" maxFractionDigits="2"/>
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
			${row.RZ}
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
   
<div id="mesgMenu" style="position:absolute;display:none;z-index:100;width:495;background-Color:menu; border: " 
	onMouseOut="makeMenuHidden()" onMouseOver="makeMenuHidden()">
	
<TABLE cellSpacing=0 cellPadding=0 bgColor=#ffffcc border=0>
  <TBODY>
  <TR bgColor=#003333>
    <TD width=73 height=28 align=center id = "td_stnm">
      <span align=center><FONT color=#ffffff>库名</FONT></span>
      </TD>
    <TD width=130 align=center id = "td_ymdhm">
      <span><FONT color=#ffffff>时间</FONT></span>
      </TD>
    <TD width=62 align=center id = "td_z">
      <span><FONT color=#ffffff>库内水位</FONT></span>
      </TD>
      <TD width=80 align=center id = "td_w">
      <span><FONT color=#ffffff>汛限水位</FONT></span>
      </TD>
	 <TD width=100 align=center id = "td_q">
      <span><FONT color=#ffffff>入库/出库流量</FONT></span>
      </TD>
      <TD width=80 align=center id = "td_s">
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
    <TD bgColor=#ffffe8 height=24 id="q" align=center class="bodyblack">
    </TD>
    <TD bgColor=#ffffe8 height=24 id="s" align=center class="bodyblack">
    </TD>
</TR></TBODY></TABLE>
</div>

<div id="construction" style="position:absolute;display:none;left:520;top:475;width:175;border:1;font-size:16px">
  <table>
    <tr><td class="suoming"><span style="color: #30899F; font-weight:bold;">操作提示：</span><br>
    　　当鼠标移至水库站或水位数据时，系统自动显示该水库的水位、汛限水位、入库/出库流量、水势信息。
    
    </td></tr>
 </table>
</div>

<div id="skgs" style="position:absolute;display:;z-index:10;left:520;top:280;width:240;border:1;font-size:16px ">
<!--水库水情概述-->
<fieldset style='padding:10 10;'>
<legend>　全省重点水库水情概述</legend>
　${summarize}<br>
	<c:if test="${ overTopFLZSize > 0}">
		<table cellpadding="0" cellspacing="1" align="center" class="main" width="210">
		<tr><th>水库</th><th>比汛限水位</th><th>前汛</th><th>后汛</th></tr>
		<c:forEach var="row" items="${overTopFLZ}" varStatus="status">
		<tr align="left" class='default' onfocusin="this.className='selected'" onfocusout="this.className='default'">
		<td>${row.STNM}</td><td>${row.CFSLTDZ}</td><td>${row.FFSLTDZ}</td><td>${row.BFSLTDZ}</td></tr>
		</c:forEach>
		</table>
	</c:if>
</fieldset>
</div>

<div id="tuli" style="position:absolute;display:none;left:540;top:480;width:240;border:1">
  <img src="styles/default/images/map/reservoirtuli.gif">
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
			cachedList[${status.index}] = new listObj('${row.STCD}','${row.STNM}','${row.TM}','${row.RZ}','${row.INQ}/${row.OTQ}','${row.FSLTDZ}','${row.RWPTN}');
	</c:forEach>
	
	function changePos(){
		construction.style.left = document.body.offsetWidth/2 - 340;
		construction.style.display = "";
		tuli.style.left = document.body.offsetWidth/2 + 60;
		tuli.style.display = "";
		skgs.style.pixelLeft = document.body.offsetWidth/2+60;
		skgs.style.display = "";
		
		<c:forEach var="row" items="${rows}" varStatus="status">
			try{
				W${row.STCD}.style.left =  (document.body.offsetWidth - 459)/2 + parseFloat(mlist['${row.STCD}'].split(",")[0]);
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