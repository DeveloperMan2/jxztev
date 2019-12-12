package com.jxztev.task;

import com.alibaba.fastjson.JSONObject;
import com.jxztev.dao.acs4sql.IMemberDao;
import com.jxztev.entity.acs4sql.Member;
import com.jxztev.entity.acs4sql.RainSummarizeResponse;
import com.jxztev.entity.acs4sql.ReservoirMapResponse;
import com.jxztev.service.acs4sql.IRainMapService;
import com.jxztev.service.acs4sql.IRainSummarizeService;
import com.jxztev.service.acs4sql.IReservoirMapService;
import com.jxztev.service.acs4sql.IRiverMapService;
import com.jxztev.service.impl.acs4sql.FindSlideRainService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class WaterRainTask implements InitializingBean {

    @Autowired
    @Qualifier("rainMapService")
    private IRainMapService rainMapService;

    @Autowired
    @Qualifier("rainSummarizeService")
    private IRainSummarizeService rainSummarizeService;

    @Autowired
    @Qualifier("reservoirMapService")
    private IReservoirMapService reservoirMapService;

    @Autowired
    @Qualifier("riverMapService")
    private IRiverMapService riverMapService;

    @Autowired
    @Qualifier("findSlideRainService")
    private FindSlideRainService findSlideRainService;

    @Autowired
    @Qualifier("memberDao")
    IMemberDao memberDao;

    // 系统启动后执行，后面不执行，解决系统启动，查询没有数据问题
    @Override
    public void afterPropertiesSet() throws Exception {
        try{
            System.out.println("开始系统初始化查询数据");
            //雨情
            rainMapData();
            rainsummarize();
            reservoir();
            river();
            //山洪预警
            rmtesRain();
            System.out.println("结束系统初始化查询数据");
        }catch (Exception e){
            throw  e;
        }
    }

    @Scheduled(cron =  "0 0/5 * * * ? ") // "0 0/5 * * * ? " 间隔5分钟执行
    public void taskCycle() {
        System.out.println("使用SpringMVC框架配置定时任务");
        //雨情
        rainMapData();
        rainsummarize();
        reservoir();
        river();
        //山洪预警
        rmtesRain();
    }

    private void rainMapData() {
        JSONObject jo = null;
        //取出县代码、县名、最大雨量、平均雨量
        List<Integer> hourList = new ArrayList<Integer>();
        hourList.add(6);
        hourList.add(12);
        hourList.add(24);
        hourList.add(48);

        for (Integer hour : hourList) {
            jo = new JSONObject();

            List<RainSummarizeResponse> countRainList = rainMapService.getCountyRainList(String.valueOf(hour));
            List<RainSummarizeResponse> maxRainList = rainMapService.getMaxRainOrderRain(String.valueOf(hour), 20);

            String bgTM = DateFormatUtils.format(DateUtils.addHours(new Date(), -hour), "M月d日H点");
            String endTM = DateFormatUtils.format(new Date(), "d日H点");//结束时间
            String queryTm = bgTM + "~" + endTM;

            JSONObject data = new JSONObject();
            data.put("countRainList", countRainList);
            data.put("maxRainList", maxRainList);
            data.put("queryTm", queryTm);

            jo.put("data", data);
            jo.put("status", 1);// 1-成功， 0-失败
            jo.put("msg", "执行成功");
            String jsonString = jo.toJSONString();
            Member member = new Member();
            String key = "rainquery_" + hour;
            member.setId(key);
            member.setJsonData(jsonString);
            memberDao.delete(key);
            memberDao.add(member);
//            System.out.println(jsonString);
        }
    }

    private void rainsummarize() {
        JSONObject jo = new JSONObject();
        //获取雨情概述
        String rain = rainSummarizeService.getRainSummary("24");
        //获取水库水情
        String reservoir = rainSummarizeService.getReservoirSummary();
        //获取河道水情
        String river = rainSummarizeService.getRiverSummary();
        JSONObject data = new JSONObject();
        data.put("rain", rain);
        data.put("reservoir", reservoir);
        data.put("river", river);
        jo.put("data", data);
        jo.put("status", 1);// 1-成功， 0-失败
        jo.put("msg", "执行成功");

        String jsonString = jo.toJSONString();
        Member member = new Member();
        member.setId("rainSummarizeHandler");
        member.setJsonData(jsonString);
        String key = "rainSummarizeHandler";
        memberDao.delete(key);
        memberDao.add(member);
//        System.out.println(jsonString);
    }

    private void reservoir() {
        JSONObject jo = new JSONObject();
        List<ReservoirMapResponse> reservoirList = reservoirMapService.queryReservoirMapList();
        List<ReservoirMapResponse> overTopFLZ = reservoirMapService.getOverTopFLZ(reservoirList);
        //组装水库地图数据模型

        String summarize;//保存水库水情概述信息
        StringBuffer sbf = new StringBuffer();
        for (int i = 0; i < overTopFLZ.size(); i++) {
            ReservoirMapResponse tMap = overTopFLZ.get(i);
            if (i > 0) {
                sbf.append("；");//不是第一个则加；分隔
            }
            sbf.append(tMap.getCounty() == null ? "" : tMap.getCounty())
                    .append(" <font color='red'>").append(tMap.getStnm()).append("水库</font>")
                    .append(tMap.getHTM()).append("超汛限水位")
                    .append("<font color='red'>").append(tMap.getCfsltdz()).append("</font>").append("米");
        }
        if (overTopFLZ.size() == 1) {
            summarize = sbf.toString();
        } else if (overTopFLZ.size() > 1) {
            summarize = "有<font color='red'>" + overTopFLZ.size() + "</font>个水库超汛限，分别是：" + sbf.toString();
        } else {
            if (reservoirList.size() > 0) {
                ReservoirMapResponse tMap = reservoirList.get(0);
                sbf = new StringBuffer();
                sbf.append("各大中型水库均在汛限水位以下，其中离汛限水位最近的是")
                        .append(tMap.getCounty() == null ? "" : tMap.getCounty()).append(" ").append(tMap.getStnm()).append("水库");
                if (tMap.getCfsltdz() != null && tMap.getStnm() != "三峡") {
                    sbf.append("，").append(tMap.getHTM()).append("比汛限水位").append(tMap.getCfsltdz()).append("米");
                }
                summarize = sbf.toString();
            } else {
                summarize = "系统暂无水库水情数据";
            }
        }
        summarize = com.ztev.commons.date.DateUtils.getTodayString("M月d日") + summarize + "。";

        JSONObject data = new JSONObject();
        data.put("rows", reservoirList);
        data.put("overTopFLZ", overTopFLZ);
        data.put("overTopFLZSize", overTopFLZ.size());
        data.put("upCodes", "");
        data.put("summarize", summarize);
        jo.put("data", data);
        jo.put("status", 1);// 1-成功， 0-失败
        jo.put("msg", "执行成功");

        String jsonString = jo.toJSONString();
        Member member = new Member();
        member.setId("reservoirMapHandler");
        member.setJsonData(jsonString);
        String key = "reservoirMapHandler";
        memberDao.delete(key);
        memberDao.add(member);
//        System.out.println(jsonString);
    }

    private void river() {
        JSONObject jo = riverMapService.riverMapHandler();
        String jsonString = jo.toJSONString();
        Member member = new Member();
        member.setId("riverMapHandler");
        member.setJsonData(jsonString);
        String key = "riverMapHandler";
        memberDao.delete(key);
        memberDao.add(member);
//        System.out.println(jsonString);
    }

    //    山洪预警
    private void rmtesRain() {
        JSONObject jo = findSlideRainService.findSlideRainHandler();
        String jsonString = jo.toJSONString();
        Member member = new Member();
        member.setId("findSlideRainHandler");
        member.setJsonData(jsonString);
        String key = "findSlideRainHandler";
        memberDao.delete(key);
        memberDao.add(member);
    }
}

