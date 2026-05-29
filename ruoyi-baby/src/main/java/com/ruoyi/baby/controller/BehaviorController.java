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
import com.ruoyi.baby.domain.Behavior;
import com.ruoyi.baby.service.IBehaviorService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;

@RestController
@RequestMapping("/baby/behavior")
public class BehaviorController extends BaseController
{
    @Autowired
    private IBehaviorService behaviorService;

    @PreAuthorize("@ss.hasPermi('baby:behavior:list')")
    @GetMapping("/list")
    public TableDataInfo list(Behavior behavior)
    {
        startPage();
        List<Behavior> list = behaviorService.selectBehaviorList(behavior);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('baby:behavior:query')")
    @GetMapping("/{behaviorId}")
    public AjaxResult getInfo(@PathVariable Long behaviorId)
    {
        return success(behaviorService.selectBehaviorById(behaviorId));
    }

    @PreAuthorize("@ss.hasPermi('baby:behavior:add')")
    @PostMapping
    public AjaxResult add(@RequestBody Behavior behavior)
    {
        behavior.setCreateBy(getUsername());
        return toAjax(behaviorService.insertBehavior(behavior));
    }

    @PreAuthorize("@ss.hasPermi('baby:behavior:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody Behavior behavior)
    {
        behavior.setUpdateBy(getUsername());
        return toAjax(behaviorService.updateBehavior(behavior));
    }

    @PreAuthorize("@ss.hasPermi('baby:behavior:remove')")
    @DeleteMapping("/{behaviorId}")
    public AjaxResult remove(@PathVariable Long behaviorId)
    {
        return toAjax(behaviorService.deleteBehaviorById(behaviorId));
    }
}