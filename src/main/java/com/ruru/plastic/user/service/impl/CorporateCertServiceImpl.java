package com.ruru.plastic.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.dao.CorporateCertMapper;
import com.ruru.plastic.user.enume.CertStatusEnum;
import com.ruru.plastic.user.model.CorporateCert;
import com.ruru.plastic.user.model.CorporateCertExample;
import com.ruru.plastic.user.request.CorporateCertRequest;
import com.ruru.plastic.user.service.CorporateCertService;
import com.ruru.plastic.user.utils.NullUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CorporateCertServiceImpl implements CorporateCertService {

    @Autowired
    private CorporateCertMapper corporateCertMapper;

    @Override
    public CorporateCert getCorporateCertById(Long id){
        return corporateCertMapper.selectByPrimaryKey(id);
    }

    @Override
    public CorporateCert getCorporateCertBySocialCode(String socialCode){
        CorporateCertExample example = new CorporateCertExample();
        example.createCriteria().andSocialCodeEqualTo(socialCode);
        return corporateCertMapper.selectOneByExample(example);
    }

    @Override
    public Msg<CorporateCert> createCorporateCert(CorporateCert corporateCert){
        if(corporateCert==null || StringUtils.isEmpty(corporateCert.getSocialCode())
                || StringUtils.isEmpty(corporateCert.getLicenseImgs())){
            return Msg.error(Constants.ERROR_PARAMETER);
        }

        CorporateCert corporateCertBySocialCode = getCorporateCertBySocialCode(corporateCert.getSocialCode());
        if(corporateCertBySocialCode!=null){
            return Msg.error(Constants.ERROR_DUPLICATE_INFO);
        }

        corporateCert.setId(null);
        if(corporateCert.getStatus()==null){
            corporateCert.setStatus(CertStatusEnum.待审核.getValue());
        }
        if(corporateCert.getCreateTime()==null){
            corporateCert.setCreateTime(new Date());
        }
        if(corporateCert.getUpdateTime()==null){
            corporateCert.setUpdateTime(new Date());
        }

        corporateCertMapper.insertSelective(corporateCert);
        return Msg.success(getCorporateCertById(corporateCert.getId()));
    }

    @Override
    public Msg<CorporateCert> updateCorporateCert(CorporateCert corporateCert){
        if(corporateCert==null || corporateCert.getId()==null){
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        CorporateCert corporateCertById = getCorporateCertById(corporateCert.getId());
        if(corporateCertById==null){
            return Msg.error(Constants.ERROR_NO_INFO);
        }

        BeanUtils.copyProperties(corporateCert,corporateCertById, NullUtil.getNullPropertyNames(corporateCert));
        CorporateCertExample example = new CorporateCertExample();
        example.createCriteria().andIdNotEqualTo(corporateCert.getId()).andSocialCodeEqualTo(corporateCertById.getSocialCode());
        if(corporateCertMapper.countByExample(example)>0){
            return Msg.error(Constants.ERROR_DUPLICATE_INFO);
        }

        corporateCertById.setUpdateTime(new Date());
        corporateCertMapper.updateByPrimaryKeySelective(corporateCertById);
        return Msg.success(getCorporateCertById(corporateCert.getId()));
    }

    @Override
    public Msg<Integer> deleteCorporateCert(CorporateCert corporateCert){
        if(corporateCert==null || corporateCert.getId()==null){
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        CorporateCert corporateCertById = getCorporateCertById(corporateCert.getId());
        if(corporateCertById==null){
            return Msg.error(Constants.ERROR_NO_INFO);
        }

        corporateCertMapper.deleteByPrimaryKey(corporateCertById.getId());
        return Msg.success(1);
    }

    @Override
    public PageInfo<CorporateCert> filterCorporateCert(CorporateCertRequest request){
        CorporateCertExample example = new CorporateCertExample();
        CorporateCertExample.Criteria criteria = example.createCriteria();

        if(request.getId()!=null){
            criteria.andIdEqualTo(request.getId());
        }
        if(StringUtils.isNotEmpty(request.getName())){
            criteria.andNameEqualTo(request.getName());
        }
        if(StringUtils.isNotEmpty(request.getSocialCode())){
            criteria.andSocialCodeEqualTo(request.getSocialCode());
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

        return new PageInfo<>(corporateCertMapper.selectByExample(example));
    }

}
