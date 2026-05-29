package com.ruoyi.baby.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.baby.domain.Measurement;
import com.ruoyi.baby.service.IMeasurementService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;

@RestController
@RequestMapping("/baby/measurement")
public class MeasurementController extends BaseController
{
    @Autowired
    private IMeasurementService measurementService;

    @PreAuthorize("@ss.hasPermi('baby:measurement:list')")
    @GetMapping("/list")
    public TableDataInfo list(Measurement measurement)
    {
        startPage();
        List<Measurement> list = measurementService.selectMeasurementList(measurement);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('baby:measurement:query')")
    @GetMapping("/{measureId}")
    public AjaxResult getInfo(@PathVariable Long measureId)
    {
        return success(measurementService.selectMeasurementById(measureId));
    }

    @PreAuthorize("@ss.hasPermi('baby:measurement:add')")
    @PostMapping
    public AjaxResult add(@RequestBody Measurement measurement)
    {
        measurement.setCreateBy(getUsername());
        return toAjax(measurementService.insertMeasurement(measurement));
    }

    @PreAuthorize("@ss.hasPermi('baby:measurement:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody Measurement measurement)
    {
        measurement.setUpdateBy(getUsername());
        return toAjax(measurementService.updateMeasurement(measurement));
    }

    @PreAuthorize("@ss.hasPermi('baby:measurement:remove')")
    @DeleteMapping("/{measureId}")
    public AjaxResult remove(@PathVariable Long measureId)
    {
        return toAjax(measurementService.deleteMeasurementById(measureId));
    }
}