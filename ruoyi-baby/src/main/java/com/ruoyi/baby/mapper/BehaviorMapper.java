package com.ruoyi.baby.mapper;

import java.util.List;
import com.ruoyi.baby.domain.Behavior;

public interface BehaviorMapper
{
    public List<Behavior> selectBehaviorList(Behavior behavior);

    public Behavior selectBehaviorById(Long behaviorId);

    public int insertBehavior(Behavior behavior);

    public int updateBehavior(Behavior behavior);

    public int deleteBehaviorById(Long behaviorId);
}