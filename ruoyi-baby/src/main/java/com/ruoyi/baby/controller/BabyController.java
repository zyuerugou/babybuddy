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
import com.ruoyi.baby.domain.Baby;
import com.ruoyi.baby.service.IBabyService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;

@RestController
@RequestMapping("/baby")
public class BabyController extends BaseController
{
    @Autowired
    private IBabyService babyService;

    @PreAuthorize("@ss.hasPermi('baby:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(Baby baby)
    {
        startPage();
        List<Baby> list = babyService.selectBabyList(baby);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('baby:info:export')")
    @Log(title = "婴儿信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(Baby baby)
    {
        List<Baby> list = babyService.selectBabyList(baby);
        ExcelUtil<Baby> util = new ExcelUtil<Baby>(Baby.class);
        return util.exportExcel(list, "婴儿信息数据");
    }

    @PreAuthorize("@ss.hasPermi('baby:info:query')")
    @GetMapping(value = "/{babyId}")
    public AjaxResult getInfo(@PathVariable("babyId") Long babyId)
    {
        return success(babyService.selectBabyById(babyId));
    }

    @PreAuthorize("@ss.hasPermi('baby:info:add')")
    @Log(title = "婴儿信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Baby baby)
    {
        baby.setCreateBy(getUsername());
        return toAjax(babyService.insertBaby(baby));
    }

    @PreAuthorize("@ss.hasPermi('baby:info:edit')")
    @Log(title = "婴儿信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Baby baby)
    {
        baby.setUpdateBy(getUsername());
        return toAjax(babyService.updateBaby(baby));
    }

    @PreAuthorize("@ss.hasPermi('baby:info:remove')")
    @Log(title = "婴儿信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{babyIds}")
    public AjaxResult remove(@PathVariable Long[] babyIds)
    {
        return toAjax(babyService.deleteBabyByIds(babyIds));
    }
}
