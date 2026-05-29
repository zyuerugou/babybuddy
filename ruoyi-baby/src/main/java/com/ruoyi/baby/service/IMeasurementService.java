package com.ruoyi.baby.service;

import java.util.List;
import com.ruoyi.baby.domain.Measurement;

public interface IMeasurementService
{
    public List<Measurement> selectMeasurementList(Measurement measurement);

    public Measurement selectMeasurementById(Long measureId);

    public int insertMeasurement(Measurement measurement);

    public int updateMeasurement(Measurement measurement);

    public int deleteMeasurementById(Long measureId);
}