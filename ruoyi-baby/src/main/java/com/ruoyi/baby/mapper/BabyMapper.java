package com.ruoyi.baby.mapper;

import com.ruoyi.baby.domain.Baby;
import java.util.List;

public interface BabyMapper
{
    public List<Baby> selectBabyList(Baby baby);

    public Baby selectBabyById(Long babyId);

    public Baby selectBaby();

    public int insertBaby(Baby baby);

    public int updateBaby(Baby baby);

    public int deleteBaby(Long babyId);

    public int deleteBabyByIds(Long[] babyIds);
}
