package com.ruoyi.baby.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

public class Behavior extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long behaviorId;

    private Long babyId;

    private String behaviorType;

    private String feedMethod;

    private Double feedAmount;

    private String diaperType;

    private String playType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private Integer duration;

    public Long getBehaviorId()
    {
        return behaviorId;
    }

    public void setBehaviorId(Long behaviorId)
    {
        this.behaviorId = behaviorId;
    }

    public Long getBabyId()
    {
        return babyId;
    }

    public void setBabyId(Long babyId)
    {
        this.babyId = babyId;
    }

    public String getBehaviorType()
    {
        return behaviorType;
    }

    public void setBehaviorType(String behaviorType)
    {
        this.behaviorType = behaviorType;
    }

    public String getFeedMethod()
    {
        return feedMethod;
    }

    public void setFeedMethod(String feedMethod)
    {
        this.feedMethod = feedMethod;
    }

    public Double getFeedAmount()
    {
        return feedAmount;
    }

    public void setFeedAmount(Double feedAmount)
    {
        this.feedAmount = feedAmount;
    }

    public String getDiaperType()
    {
        return diaperType;
    }

    public void setDiaperType(String diaperType)
    {
        this.diaperType = diaperType;
    }

    public String getPlayType()
    {
        return playType;
    }

    public void setPlayType(String playType)
    {
        this.playType = playType;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public Integer getDuration()
    {
        return duration;
    }

    public void setDuration(Integer duration)
    {
        this.duration = duration;
    }
}