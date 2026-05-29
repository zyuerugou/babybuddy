package com.ruoyi.baby.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.baby.domain.Measurement;
import com.ruoyi.baby.mapper.MeasurementMapper;
import com.ruoyi.baby.service.IMeasurementService;

@Service
public class MeasurementServiceImpl implements IMeasurementService
{
    @Autowired
    private MeasurementMapper measurementMapper;

    @Override
    public List<Measurement> selectMeasurementList(Measurement measurement)
    {
        return measurementMapper.selectMeasurementList(measurement);
    }

    @Override
    public Measurement selectMeasurementById(Long measureId)
    {
        return measurementMapper.selectMeasurementById(measureId);
    }

    @Override
    public int insertMeasurement(Measurement measurement)
    {
        measurement.setCreateTime(new Date());
        return measurementMapper.insertMeasurement(measurement);
    }

    @Override
    public int updateMeasurement(Measurement measurement)
    {
        measurement.setUpdateTime(new Date());
        return measurementMapper.updateMeasurement(measurement);
    }

    @Override
    public int deleteMeasurementById(Long measureId)
    {
        return measurementMapper.deleteMeasurementById(measureId);
    }
}