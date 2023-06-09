package com.ruru.plastic.user.dao;

import com.ruru.plastic.user.model.RoleMatch;
import com.ruru.plastic.user.model.RoleMatchExample;

import java.util.List;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleMatchMapper {
    int countByExample(RoleMatchExample example);

    int deleteByExample(RoleMatchExample example);

    int deleteByPrimaryKey(Long id);

    int insert(RoleMatch record);

    @Options(useGeneratedKeys = true)
    int insertSelective(RoleMatch record);

    List<RoleMatch> selectByExample(RoleMatchExample example);

    RoleMatch selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") RoleMatch record, @Param("example") RoleMatchExample example);

    int updateByExample(@Param("record") RoleMatch record, @Param("example") RoleMatchExample example);

    int updateByPrimaryKeySelective(RoleMatch record);

    int updateByPrimaryKey(RoleMatch record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table role_match
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    RoleMatch selectOneByExample(RoleMatchExample example);
}