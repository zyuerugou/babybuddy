package com.ruoyi.baby.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.baby.domain.Baby;
import com.ruoyi.baby.mapper.BabyMapper;
import com.ruoyi.baby.service.IBabyService;

@Service
public class BabyServiceImpl implements IBabyService
{
    @Autowired
    private BabyMapper babyMapper;

    @Override
    public List<Baby> selectBabyList(Baby baby)
    {
        return babyMapper.selectBabyList(baby);
    }

    @Override
    public Baby selectBabyById(Long babyId)
    {
        return babyMapper.selectBabyById(babyId);
    }

    @Override
    public Baby selectBaby()
    {
        return babyMapper.selectBaby();
    }

    @Override
    public int insertBaby(Baby baby)
    {
        baby.setCreateTime(new Date());
        return babyMapper.insertBaby(baby);
    }

    @Override
    public int updateBaby(Baby baby)
    {
        baby.setUpdateTime(new Date());
        return babyMapper.updateBaby(baby);
    }

    @Override
    public int deleteBaby(Long babyId)
    {
        return babyMapper.deleteBaby(babyId);
    }

    @Override
    public int deleteBabyByIds(Long[] babyIds)
    {
        return babyMapper.deleteBabyByIds(babyIds);
    }
}
