package com.ruoyi.baby.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.baby.domain.Behavior;
import com.ruoyi.baby.domain.Measurement;
import com.ruoyi.baby.service.IBehaviorService;
import com.ruoyi.baby.service.IMeasurementService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;

@RestController
@RequestMapping("/baby/report")
public class ReportController extends BaseController
{
    private static final Logger log = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private IMeasurementService measurementService;

    @Autowired
    private IBehaviorService behaviorService;

    private Date getStartDate(String range) {
        Calendar cal = Calendar.getInstance();
        if ("7".equals(range)) {
            cal.add(Calendar.DAY_OF_MONTH, -7);
        } else if ("30".equals(range)) {
            cal.add(Calendar.DAY_OF_MONTH, -30);
        } else if ("90".equals(range)) {
            cal.add(Calendar.DAY_OF_MONTH, -90);
        } else {
            cal.set(2000, Calendar.JANUARY, 1);
        }
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    @PreAuthorize("@ss.hasPermi('baby:report:query')")
    @GetMapping("/growth")
    public AjaxResult growth(@RequestParam(defaultValue = "all") String range,
                             @RequestParam(defaultValue = "1") Long babyId)
    {
        Date startDate = getStartDate(range);
        SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");

        Measurement heightParam = new Measurement();
        heightParam.setMeasureType("height");
        heightParam.setBabyId(babyId);
        List<Measurement> heights = measurementService.selectMeasurementList(heightParam);

        Measurement weightParam = new Measurement();
        weightParam.setMeasureType("weight");
        weightParam.setBabyId(babyId);
        List<Measurement> weights = measurementService.selectMeasurementList(weightParam);

        Map<String, Double> heightByDate = new TreeMap<>();
        for (Measurement m : heights) {
            if (m.getMeasureDate() != null && (range.equals("all") || !m.getMeasureDate().before(startDate))) {
                String dateStr = dateFmt.format(m.getMeasureDate());
                if (heightByDate.containsKey(dateStr)) {
                    log.warn("同一天存在多条身高记录: {}", dateStr);
                }
                heightByDate.put(dateStr, m.getValue());
            }
        }

        Map<String, Double> weightByDate = new TreeMap<>();
        for (Measurement m : weights) {
            if (m.getMeasureDate() != null && (range.equals("all") || !m.getMeasureDate().before(startDate))) {
                String dateStr = dateFmt.format(m.getMeasureDate());
                if (weightByDate.containsKey(dateStr)) {
                    log.warn("同一天存在多条体重记录: {}", dateStr);
                }
                weightByDate.put(dateStr, m.getValue());
            }
        }

        Set<String> allDates = new TreeSet<>();
        allDates.addAll(heightByDate.keySet());
        allDates.addAll(weightByDate.keySet());

        List<String> dates = new ArrayList<>(allDates);
        List<Double> heightList = new ArrayList<>();
        List<Double> weightList = new ArrayList<>();
        for (String dateStr : dates) {
            heightList.add(heightByDate.get(dateStr));
            weightList.add(weightByDate.get(dateStr));
        }

        Map<String, Object> data = new HashMap<>();
        data.put("dates", dates);
        data.put("heightList", heightList);
        data.put("weightList", weightList);
        return success(data);
    }

    @PreAuthorize("@ss.hasPermi('baby:report:query')")
    @GetMapping("/feeding-interval")
    public TableDataInfo feedingInterval(@RequestParam(defaultValue = "1") Long babyId)
    {
        Behavior param = new Behavior();
        param.setBehaviorType("feeding");
        param.setBabyId(babyId);
        List<Behavior> feedings = behaviorService.selectBehaviorList(param);

        feedings.sort(Comparator.comparing(Behavior::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())));

        List<Map<String, Object>> intervalList = new ArrayList<>();
        long minInterval = Long.MAX_VALUE;
        long maxInterval = 0;
        long totalInterval = 0;
        int intervalCount = 0;
        SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (int i = 0; i < feedings.size(); i++) {
            Behavior current = feedings.get(i);
            Map<String, Object> item = new LinkedHashMap<>();
            if (current.getStartTime() != null) {
                item.put("feedingTime", dateFmt.format(current.getStartTime()));
            }
            item.put("feedMethod", current.getFeedMethod());
            item.put("feedAmount", current.getFeedAmount());

            if (i > 0) {
                Behavior prev = feedings.get(i - 1);
                if (prev.getStartTime() != null && current.getStartTime() != null) {
                    long interval = (current.getStartTime().getTime() - prev.getStartTime().getTime()) / 1000;
                    item.put("interval", interval);
                    minInterval = Math.min(minInterval, interval);
                    maxInterval = Math.max(maxInterval, interval);
                    totalInterval += interval;
                    intervalCount++;
                }
            }
            intervalList.add(item);
        }

        Map<String, Object> summary = new HashMap<>();
        summary.put("avgInterval", intervalCount > 0 ? (double) totalInterval / intervalCount : 0);
        summary.put("maxInterval", maxInterval);
        summary.put("minInterval", minInterval == Long.MAX_VALUE ? 0 : minInterval);

        TableDataInfo rspData = new TableDataInfo();
        rspData.setRows(intervalList);
        rspData.setTotal(intervalList.size());
        rspData.setCode(200);
        rspData.setMsg("查询成功");
        rspData.setData(summary);
        return rspData;
    }

    @PreAuthorize("@ss.hasPermi('baby:report:query')")
    @GetMapping("/daily-feeding")
    public TableDataInfo dailyFeeding(
            @RequestParam(required = false) String beginTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(defaultValue = "1") Long babyId)
    {
        Behavior param = new Behavior();
        param.setBehaviorType("feeding");
        param.setBabyId(babyId);
        if (beginTime != null) {
            param.getParams().put("beginTime", beginTime);
        }
        if (endTime != null) {
            param.getParams().put("endTime", endTime);
        }
        List<Behavior> feedings = behaviorService.selectBehaviorList(param);

        SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");

        Map<String, Map<String, Object>> dailyMap = new TreeMap<>();
        for (Behavior b : feedings) {
            if (b.getStartTime() == null) {
                continue;
            }
            String dateStr = dateFmt.format(b.getStartTime());
            dailyMap.putIfAbsent(dateStr, new HashMap<>());
            Map<String, Object> day = dailyMap.get(dateStr);

            day.put("date", dateStr);
            int count = (Integer) day.getOrDefault("count", 0);
            double totalAmount = (Double) day.getOrDefault("totalAmount", 0.0);
            int breastCount = (Integer) day.getOrDefault("breastCount", 0);
            int formulaCount = (Integer) day.getOrDefault("formulaCount", 0);

            day.put("count", count + 1);
            if (b.getFeedAmount() != null) {
                day.put("totalAmount", totalAmount + b.getFeedAmount());
            }
            if ("breast".equals(b.getFeedMethod())) {
                day.put("breastCount", breastCount + 1);
            } else if ("formula".equals(b.getFeedMethod())) {
                day.put("formulaCount", formulaCount + 1);
            }
        }

        List<Map<String, Object>> rows = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        List<Double> amounts = new ArrayList<>();

        for (Map.Entry<String, Map<String, Object>> entry : dailyMap.entrySet()) {
            Map<String, Object> day = entry.getValue();
            dates.add((String) day.get("date"));
            amounts.add((Double) day.getOrDefault("totalAmount", 0.0));
            day.putIfAbsent("breastCount", 0);
            day.putIfAbsent("formulaCount", 0);
            rows.add(day);
        }

        Map<String, Object> chartData = new HashMap<>();
        chartData.put("dates", dates);
        chartData.put("amounts", amounts);

        TableDataInfo rspData = new TableDataInfo();
        rspData.setRows(rows);
        rspData.setTotal(rows.size());
        rspData.setCode(200);
        rspData.setMsg("查询成功");
        rspData.setData(chartData);
        return rspData;
    }
}