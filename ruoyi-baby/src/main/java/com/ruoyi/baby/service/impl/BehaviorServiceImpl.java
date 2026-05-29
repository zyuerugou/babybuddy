package com.ruoyi.baby.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.baby.domain.Behavior;
import com.ruoyi.baby.mapper.BehaviorMapper;
import com.ruoyi.baby.service.IBehaviorService;

@Service
public class BehaviorServiceImpl implements IBehaviorService
{
    @Autowired
    private BehaviorMapper behaviorMapper;

    @Override
    public List<Behavior> selectBehaviorList(Behavior behavior)
    {
        return behaviorMapper.selectBehaviorList(behavior);
    }

    @Override
    public Behavior selectBehaviorById(Long behaviorId)
    {
        return behaviorMapper.selectBehaviorById(behaviorId);
    }

    @Override
    public int insertBehavior(Behavior behavior)
    {
        behavior.setCreateTime(new Date());
        return behaviorMapper.insertBehavior(behavior);
    }

    @Override
    public int updateBehavior(Behavior behavior)
    {
        behavior.setUpdateTime(new Date());
        return behaviorMapper.updateBehavior(behavior);
    }

    @Override
    public int deleteBehaviorById(Long behaviorId)
    {
        return behaviorMapper.deleteBehaviorById(behaviorId);
    }
}