package com.ruoyi.baby.service;

import java.util.Map;

/**
 * 报表 服务层
 */
public interface IReportService
{
    /**
     * 查询生长曲线数据
     *
     * @param range  时间范围（7/30/90/all）
     * @param babyId 宝宝ID
     * @return {dates, heightList, weightList}
     */
    Map<String, Object> getGrowthData(String range, Long babyId);

    /**
     * 查询喂养间隔数据
     *
     * @param babyId 宝宝ID
     * @return {rows (List<Map>), summary (Map), total}
     */
    Map<String, Object> getFeedingIntervalData(Long babyId);

    /**
     * 查询每日喂养量数据
     *
     * @param beginTime 开始日期
     * @param endTime   结束日期
     * @param babyId    宝宝ID
     * @return {rows (List<Map>), chartData (Map), total}
     */
    Map<String, Object> getDailyFeedingData(String beginTime, String endTime, Long babyId);
}
