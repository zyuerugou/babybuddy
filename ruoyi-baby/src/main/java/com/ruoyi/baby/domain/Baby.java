package com.ruoyi.baby.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

public class Baby extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long babyId;

    private String name;

    private String gender;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    private String photo;

    public Long getBabyId()
    {
        return babyId;
    }

    public void setBabyId(Long babyId)
    {
        this.babyId = babyId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public Date getBirthDate()
    {
        return birthDate;
    }

    public void setBirthDate(Date birthDate)
    {
        this.birthDate = birthDate;
    }

    public String getPhoto()
    {
        return photo;
    }

    public void setPhoto(String photo)
    {
        this.photo = photo;
    }
}