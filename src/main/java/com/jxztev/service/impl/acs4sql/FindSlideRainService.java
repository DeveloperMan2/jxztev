package com.jxztev.service.impl.acs4sql;

import com.alibaba.fastjson.JSONObject;
import com.jxztev.dao.acs4sql.IFindSlideRainDao;
import com.jxztev.entity.acs4sql.FindSlideRainRequest;
import com.jxztev.entity.acs4sql.FindSlideRainResponse;
import com.jxztev.service.acs4sql.IFindSlideRainService;
import com.jxztev.utils.DataFormatUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @desc FindSlideRain接口
 */
@Service("findSlideRainService")
@Transactional(rollbackFor = Exception.class)
public class FindSlideRainService implements IFindSlideRainService {
    @Autowired
    @Qualifier("findSlideRainDao")
    private IFindSlideRainDao findSlideRainDao;

    public static Integer DEFAULT_PERIOD = Integer.valueOf(0);

    public JSONObject findSlideRainHandler() {
        JSONObject jo = new JSONObject();
        try {
            Map<String, List<FindSlideRainResponse>> allRainData = new HashMap();
            FindSlideRainRequest findSlideRainRequestParams = new FindSlideRainRequest();
            findSlideRainRequestParams.setHours(1);
            findSlideRainRequestParams.setPeriod(DEFAULT_PERIOD);
            findSlideRainRequestParams.setDrp(Float.valueOf(30.0F));
            allRainData.put("rs1", findSlideRainDao.findSlideRainHandler(findSlideRainRequestParams));

            findSlideRainRequestParams.setHours(3);
            findSlideRainRequestParams.setPeriod(DEFAULT_PERIOD);
            findSlideRainRequestParams.setDrp(Float.valueOf(50.0F));
            allRainData.put("rs3", findSlideRainDao.findSlideRainHandler(findSlideRainRequestParams));

            findSlideRainRequestParams.setHours(3);
            findSlideRainRequestParams.setPeriod(DEFAULT_PERIOD);
            findSlideRainRequestParams.setDrp(Float.valueOf(80.0F));
            allRainData.put("rs24", findSlideRainDao.findSlideRainHandler(findSlideRainRequestParams));

            List<FindSlideRainResponse> oneHourList = new ArrayList();
            List<FindSlideRainResponse> twoHourList = new ArrayList();
            List<FindSlideRainResponse> twfHourList = new ArrayList();
            StringBuffer sb = new StringBuffer("");
            if (allRainData != null) {
                oneHourList = (List) allRainData.get("rs1");
                if ((oneHourList != null) && (oneHourList.size() > 0)) {
                    sb.append("<h1 class='title'>最近24小时内，<font color='red'>1小时</font>雨量超过<font color='red'>30毫米</font>\n");
                    sb.append("的测站（<font color='red'>").append(oneHourList.size() + "条</font>）</h1>\n");
                    sb.append(getOneTypeSlideHtml(oneHourList, Boolean.valueOf(false)));
                } else {
                    sb.append("<h1 class='title'>暂无最近24小时内，1小时雨量超过30毫米的测站</h1>\n");

                    twoHourList = (List) allRainData.get("rs3");
                    if ((twoHourList != null) && (twoHourList.size() > 0)) {
                        sb.append("<h1 class='title'>最近24小时内，<font color='red'>3小时</font>雨量超过<font color='red'>50毫米</font>\n");
                        sb.append("的测站（<font color='red'>").append(twoHourList.size() + "条</font>）</h1>\n");
                        sb.append(getOneTypeSlideHtml(twoHourList, Boolean.valueOf(false)));
                    } else {
                        sb.append("<h1 class='title'>暂无最近24小时内，3小时雨量超过50毫米的测站</h1>\n");
                    }
                    twfHourList = (List) allRainData.get("rs24");
                    if ((twfHourList != null) && (twfHourList.size() > 0)) {
                        sb.append("<h1 class='title'>最近<font color='red'>24小时</font>内雨量超过<font color='red'>80毫米</font>");
                        sb.append("的测站（<font color='red'>").append(twfHourList.size() + "条</font>）</h1>");
                        sb.append(getOneTypeSlideHtml(twfHourList, Boolean.valueOf(true)));
                    } else {
                        sb.append("<h1 class='title'>暂无24小时内雨量超过80毫米的测站</h1>\n");
                    }
                }
                sb.append("<br><center>更新时间: ");
                sb.append(DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
                sb.append("</center>");
            }
            String content = sb.toString();
            jo.put("data", content);
            jo.put("status", 1);// 1-成功， 0-失败
            jo.put("msg", "执行成功");
        } catch (Exception e) {
            jo.put("data", null);
            jo.put("status", 0);// 1-成功， 0-失败
            jo.put("msg", e.getMessage());// msg-失败信息
            e.printStackTrace();
        }
        return jo;

    }

    public String getOneTypeSlideHtml(List<FindSlideRainResponse> list, Boolean isTwf) {
        StringBuffer sb = new StringBuffer("");
        FindSlideRainResponse rainValue = null;
        String tm = "";
        sb.append("<table cellpadding='0' cellspacing='0' width='776' align='center'>\n");
        sb.append(" <tr><td align='center'>\n");

        sb.append(" <table class='main' width='99%' border='0' align='center' cellspacing='1'>\n");

        sb.append("  <tr bgcolor='336699' align='center'>\n");
        sb.append("   <th width='10%'>县　名</th>\n");
        sb.append("   <th width='28%'>详细地址</th>\n");
        sb.append("   <th width='15%'>站　名</th>\n");
        sb.append("  <th>雨量及发生时间段</th></tr>\n");


        tm = DateFormatUtils.format(new Date(System.currentTimeMillis() - 86400000L), "M月d日H点") + "~" + DateFormatUtils.format(new Date(System.currentTimeMillis() + 3600000L), "d日H点");

        for (int i = 0; i < list.size(); i++) {
            rainValue = list.get(i);
            String cnnm = rainValue.getCnnm() == null ? "--" : rainValue.getCnnm();
            sb.append("  <tr height='22' align='left' onfocusin=\"this.className='selected'\" onfocusout=\"this.className='unselected'\">\n");
            sb.append("   <td align='center'>" + cnnm + "</td>\n");
            sb.append("   <td>　" + DataFormatUtils.formatString(rainValue.getStlc()) + "</td>\n");
            sb.append("   <td>　" + DataFormatUtils.formatString(rainValue.getStnm()) + "</td>\n");
            if (!isTwf.booleanValue()) {
                tm = rainValue.getStarttm() + "~" + rainValue.getEndtm();
            }
            sb.append("   <td align='center' style='cursor: hand;'>\n").append(tm).append("　:　").append(DataFormatUtils.getRoundString(rainValue.getPp(), 1) + "毫米");
            sb.append("   </td></tr>\n");
        }
        sb.append(" </table>\n");
        sb.append(" </td></tr>\n");
        sb.append("</table>\n");
        return sb.toString();
    }
}

