package com.ruoyi.baby.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.baby.service.IReportService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;

@RestController
@RequestMapping("/baby/report")
public class ReportController extends BaseController
{
    @Autowired
    private IReportService reportService;

    @PreAuthorize("@ss.hasPermi('baby:report:query')")
    @GetMapping("/growth")
    public AjaxResult growth(@RequestParam(defaultValue = "all") String range,
                             @RequestParam(defaultValue = "1") Long babyId)
    {
        Map<String, Object> data = reportService.getGrowthData(range, babyId);
        return success(data);
    }

    @PreAuthorize("@ss.hasPermi('baby:report:query')")
    @GetMapping("/feeding-interval")
    public TableDataInfo feedingInterval(@RequestParam(defaultValue = "1") Long babyId)
    {
        Map<String, Object> data = reportService.getFeedingIntervalData(babyId);

        @SuppressWarnings("unchecked")
        TableDataInfo rspData = new TableDataInfo();
        rspData.setRows((List<?>) data.get("rows"));
        rspData.setTotal((long) data.get("total"));
        rspData.setCode(200);
        rspData.setMsg("查询成功");
        rspData.setData(data.get("summary"));
        return rspData;
    }

    @PreAuthorize("@ss.hasPermi('baby:report:query')")
    @GetMapping("/daily-feeding")
    public TableDataInfo dailyFeeding(
            @RequestParam(required = false) String beginTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(defaultValue = "1") Long babyId)
    {
        Map<String, Object> data = reportService.getDailyFeedingData(beginTime, endTime, babyId);

        @SuppressWarnings("unchecked")
        TableDataInfo rspData = new TableDataInfo();
        rspData.setRows((List<?>) data.get("rows"));
        rspData.setTotal((long) data.get("total"));
        rspData.setCode(200);
        rspData.setMsg("查询成功");
        rspData.setData(data.get("chartData"));
        return rspData;
    }
}
