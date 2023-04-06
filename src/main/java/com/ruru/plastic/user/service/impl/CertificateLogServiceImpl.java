package com.ruru.plastic.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.dao.CertificateLogMapper;
import com.ruru.plastic.user.model.CertificateLog;
import com.ruru.plastic.user.model.CertificateLogExample;
import com.ruru.plastic.user.request.CertificateLogRequest;
import com.ruru.plastic.user.service.CertificateLogService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CertificateLogServiceImpl implements CertificateLogService {

    @Autowired
    private CertificateLogMapper certificateLogMapper;

    @Override
    public CertificateLog getCertificateLogById(Long id) {
        return certificateLogMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<CertificateLog> queryCertificateLog(CertificateLog log){
        CertificateLogRequest request = new CertificateLogRequest();
        BeanUtils.copyProperties(log,request);

        CertificateLogExample example = new CertificateLogExample();
        CertificateLogExample.Criteria criteria = example.createCriteria();

        queryCertificateLog(request, criteria);

        example.setOrderByClause("create_time desc");
        return certificateLogMapper.selectByExample(example);
    }

    @Override
    public Msg<CertificateLog> createCertificateLog(CertificateLog log) {
        if (log == null || log.getCertStatus()==null || log.getLordId() == null || log.getLordType()==null || log.getCertLevel() == null) {
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        log.setId(null);
        if(log.getCreateTime()==null) {
            log.setCreateTime(new Date());
        }
        certificateLogMapper.insertSelective(log);
        return Msg.success(getCertificateLogById(log.getId()));
    }

    @Override
    public PageInfo<CertificateLog> filterCertificateLog(CertificateLogRequest request){
        CertificateLogExample example = new CertificateLogExample();
        CertificateLogExample.Criteria criteria = example.createCriteria();

        queryCertificateLog(request, criteria);

        example.setOrderByClause("create_time desc");
        PageHelper.startPage(request.getPage(),request.getSize());

        return new PageInfo<>(certificateLogMapper.selectByExample(example));
    }

    private static void queryCertificateLog(CertificateLogRequest request, CertificateLogExample.Criteria criteria) {
        if(request.getId()!=null){
            criteria.andIdEqualTo(request.getId());
        }
        if(request.getLordId()!=null){
            criteria.andLordIdEqualTo(request.getLordId());
        }
        if(request.getLordType()!=null){
            criteria.andLordTypeEqualTo(request.getLordType());
        }
        if(request.getCertStatus()!=null){
            criteria.andCertStatusEqualTo(request.getCertStatus());
        }
        if(request.getCertLevel()!=null){
            criteria.andCertLevelEqualTo(request.getCertLevel());
        }
        if(request.getStartTime()!=null){
            criteria.andCreateTimeGreaterThanOrEqualTo(request.getStartTime());
        }
        if(request.getEndTime()!=null){
            criteria.andCreateTimeLessThanOrEqualTo(request.getEndTime());
        }
    }

}
