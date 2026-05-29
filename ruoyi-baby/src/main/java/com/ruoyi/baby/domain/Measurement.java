package com.ruoyi.baby.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

public class Measurement extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long measureId;

    private Long babyId;

    private String measureType;

    private Double value;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date measureDate;

    public Long getMeasureId()
    {
        return measureId;
    }

    public void setMeasureId(Long measureId)
    {
        this.measureId = measureId;
    }

    public Long getBabyId()
    {
        return babyId;
    }

    public void setBabyId(Long babyId)
    {
        this.babyId = babyId;
    }

    public String getMeasureType()
    {
        return measureType;
    }

    public void setMeasureType(String measureType)
    {
        this.measureType = measureType;
    }

    public Double getValue()
    {
        return value;
    }

    public void setValue(Double value)
    {
        this.value = value;
    }

    public Date getMeasureDate()
    {
        return measureDate;
    }

    public void setMeasureDate(Date measureDate)
    {
        this.measureDate = measureDate;
    }
}