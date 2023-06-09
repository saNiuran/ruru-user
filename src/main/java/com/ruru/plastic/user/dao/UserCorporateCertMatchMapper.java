package com.ruru.plastic.user.dao;

import com.ruru.plastic.user.model.UserCorporateCertMatch;
import com.ruru.plastic.user.model.UserCorporateCertMatchExample;

import java.util.List;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCorporateCertMatchMapper {
    int countByExample(UserCorporateCertMatchExample example);

    int deleteByExample(UserCorporateCertMatchExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserCorporateCertMatch record);

    @Options(useGeneratedKeys = true)
    int insertSelective(UserCorporateCertMatch record);

    List<UserCorporateCertMatch> selectByExample(UserCorporateCertMatchExample example);

    UserCorporateCertMatch selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserCorporateCertMatch record, @Param("example") UserCorporateCertMatchExample example);

    int updateByExample(@Param("record") UserCorporateCertMatch record, @Param("example") UserCorporateCertMatchExample example);

    int updateByPrimaryKeySelective(UserCorporateCertMatch record);

    int updateByPrimaryKey(UserCorporateCertMatch record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_corporate_cert_match
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    UserCorporateCertMatch selectOneByExample(UserCorporateCertMatchExample example);
}