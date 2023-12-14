package com.ruru.plastic.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.dao.PersonalCertMapper;
import com.ruru.plastic.user.enume.CertStatusEnum;
import com.ruru.plastic.user.model.PersonalCert;
import com.ruru.plastic.user.model.PersonalCertExample;
import com.ruru.plastic.user.request.PersonalCertRequest;
import com.ruru.plastic.user.service.PersonalCertService;
import com.ruru.plastic.user.utils.NullUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PersonalCertServiceImpl implements PersonalCertService {

    @Autowired
    private PersonalCertMapper personalCertMapper;

    @Override
    public PersonalCert getPersonalCertById(Long id){
        return personalCertMapper.selectByPrimaryKey(id);
    }


    @Override
    public PersonalCert getPersonalCertByUserId(Long userId){
        PersonalCertExample example = new PersonalCertExample();
        example.createCriteria().andUserIdEqualTo(userId);
        return personalCertMapper.selectOneByExample(example);
    }

    @Override
    public Msg<PersonalCert> createPersonalCert(PersonalCert personalCert){
        if(personalCert==null || personalCert.getUserId()==null
                || StringUtils.isEmpty(personalCert.getIdCardFrontImg()) || StringUtils.isEmpty(personalCert.getIdCardBackImg())){
            return Msg.error(Constants.ERROR_PARAMETER);
        }

        PersonalCert personalCertByUserId = getPersonalCertByUserId(personalCert.getUserId());
        if(personalCertByUserId!=null){
            return Msg.error(Constants.ERROR_DUPLICATE_INFO);
        }

        personalCert.setId(null);
        if(personalCert.getStatus()==null){
            personalCert.setStatus(CertStatusEnum.待审核.getValue());
        }
        if(personalCert.getCreateTime()==null){
            personalCert.setCreateTime(new Date());
        }
        if(personalCert.getUpdateTime()==null){
            personalCert.setUpdateTime(new Date());
        }

        personalCertMapper.insertSelective(personalCert);
        return Msg.success(getPersonalCertById(personalCert.getId()));
    }

    @Override
    public Msg<PersonalCert> updatePersonalCert(PersonalCert personalCert){
        if(personalCert==null || personalCert.getId()==null){
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        PersonalCert personalCertById = getPersonalCertById(personalCert.getId());
        if(personalCertById==null){
            return Msg.error(Constants.ERROR_NO_INFO);
        }

        BeanUtils.copyProperties(personalCert,personalCertById, NullUtil.getNullPropertyNames(personalCert));
        PersonalCertExample example = new PersonalCertExample();
        example.createCriteria().andIdNotEqualTo(personalCert.getId()).andUserIdEqualTo(personalCertById.getUserId());
        if(personalCertMapper.countByExample(example)>0){
            return Msg.error(Constants.ERROR_DUPLICATE_INFO);
        }

        personalCertById.setUpdateTime(new Date());
        personalCertMapper.updateByPrimaryKeySelective(personalCertById);
        return Msg.success(getPersonalCertById(personalCert.getId()));
    }

    @Override
    public Msg<Integer> deletePersonalCert(PersonalCert personalCert){
        if(personalCert==null || personalCert.getId()==null){
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        PersonalCert personalCertById = getPersonalCertById(personalCert.getId());
        if(personalCertById==null){
            return Msg.error(Constants.ERROR_NO_INFO);
        }

        personalCertMapper.deleteByPrimaryKey(personalCertById.getId());
        return Msg.success(1);
    }

    @Override
    public PageInfo<PersonalCert> filterPersonalCert(PersonalCertRequest request){
        PersonalCertExample example = new PersonalCertExample();
        PersonalCertExample.Criteria criteria = example.createCriteria();

        if(request.getId()!=null){
            criteria.andIdEqualTo(request.getId());
        }
        if(request.getUserId()!=null){
            criteria.andUserIdEqualTo(request.getUserId());
        }
        if(StringUtils.isNotEmpty(request.getName())){
            criteria.andNameEqualTo(request.getName());
        }
        if(StringUtils.isNotEmpty(request.getCertificateNo())){
            criteria.andCertificateNoEqualTo(request.getCertificateNo());
        }
        if(request.getStatus()!=null){
            criteria.andStatusEqualTo(request.getStatus());
        }

        if (request.getStartTime() != null) {
            criteria.andUpdateTimeGreaterThanOrEqualTo(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            criteria.andUpdateTimeLessThanOrEqualTo(request.getEndTime());
        }

        if (StringUtils.isNotEmpty(request.getOrderClause())) {
            example.setOrderByClause(request.getOrderClause());
        } else {
            example.setOrderByClause("update_time desc");
        }
        PageHelper.startPage(request.getPage(), request.getSize());

        return new PageInfo<>(personalCertMapper.selectByExample(example));
    }
}
