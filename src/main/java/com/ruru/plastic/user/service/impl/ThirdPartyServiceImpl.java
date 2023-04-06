package com.ruru.plastic.user.service.impl;

import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.dao.ThirdPartyMapper;
import com.ruru.plastic.user.model.ThirdParty;
import com.ruru.plastic.user.model.ThirdPartyExample;
import com.ruru.plastic.user.service.ThirdPartyService;
import com.ruru.plastic.user.utils.NullUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThirdPartyServiceImpl implements ThirdPartyService {

    @Autowired
    private ThirdPartyMapper thirdPartyMapper;

    @Override
    public ThirdParty getThirdPartyById(Long id) {
        return thirdPartyMapper.selectByPrimaryKey(id);
    }

    @Override
    public ThirdParty getThirdPartyByTypeAndUid(ThirdParty thirdParty) {
        if (thirdParty == null || thirdParty.getType() == null || StringUtils.isEmpty(thirdParty.getUid())) {
            return null;
        }
        ThirdPartyExample example = new ThirdPartyExample();
        example.createCriteria().andTypeEqualTo(thirdParty.getType()).andUidEqualTo(thirdParty.getUid());
        return thirdPartyMapper.selectOneByExample(example);
    }

    @Description("只返回有效的")
    @Override
    public List<ThirdParty> getThirdPartyByTypeAndUserId(ThirdParty thirdParty) {
        if (thirdParty == null || thirdParty.getType() == null || thirdParty.getUserId() == null) {
            return null;
        }
        ThirdPartyExample example = new ThirdPartyExample();
        example.createCriteria().andTypeEqualTo(thirdParty.getType()).andUserIdEqualTo(thirdParty.getUserId());
        return thirdPartyMapper.selectByExample(example);
    }

    @Override
    public Msg<ThirdParty> createThirdParty(ThirdParty thirdParty) {
        if (thirdParty == null || StringUtils.isEmpty(thirdParty.getUid()) || thirdParty.getType() == null) {
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        ThirdParty dbThirdParty = getThirdPartyByTypeAndUid(thirdParty);
        if (dbThirdParty == null) {
            dbThirdParty = new ThirdParty();
            BeanUtils.copyProperties(thirdParty, dbThirdParty);
            thirdPartyMapper.insertSelective(dbThirdParty);
            return Msg.success(getThirdPartyById(dbThirdParty.getId()));
        } else {
            thirdParty.setId(dbThirdParty.getId());
            return updateThirdParty(thirdParty);
        }
    }

    @Override
    public Msg<ThirdParty> updateThirdParty(ThirdParty thirdParty) {
        if (thirdParty == null || thirdParty.getId() == null) {
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        ThirdParty dbThirdParty = getThirdPartyById(thirdParty.getId());
        if (dbThirdParty == null) {
            return Msg.error(Constants.ERROR_NO_INFO);
        }

        BeanUtils.copyProperties(thirdParty, dbThirdParty, NullUtil.getNullPropertyNames(thirdParty));

        ThirdPartyExample example = new ThirdPartyExample();
        example.createCriteria().andTypeEqualTo(thirdParty.getType()).andUidEqualTo(thirdParty.getUid())
                .andIdNotEqualTo(thirdParty.getId());

        if (thirdPartyMapper.countByExample(example) > 0) {
            return Msg.error(Constants.ERROR_DUPLICATE_INFO);
        }
        thirdPartyMapper.updateByPrimaryKeySelective(dbThirdParty);
        return Msg.success(getThirdPartyById(thirdParty.getId()));
    }

    @Override
    public Msg<ThirdParty> unBindingThirdParty(ThirdParty thirdParty) {
        if (thirdParty == null || thirdParty.getId() == null) {
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        ThirdParty thirdPartyById = getThirdPartyById(thirdParty.getId());
        if (thirdPartyById == null) {
            return Msg.error(Constants.ERROR_NO_INFO);
        }

        thirdPartyById.setUserId(null);
        thirdPartyMapper.updateByPrimaryKey(thirdPartyById);
        return Msg.success(thirdPartyById);
    }
}
