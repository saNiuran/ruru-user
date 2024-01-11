package com.ruru.plastic.user.dao;

import com.ruru.plastic.user.model.UserProperty;
import com.ruru.plastic.user.model.UserPropertyExample;

import java.util.List;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPropertyMapper {
    int countByExample(UserPropertyExample example);

    int deleteByExample(UserPropertyExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserProperty record);

    @Options(useGeneratedKeys = true)
    int insertSelective(UserProperty record);

    List<UserProperty> selectByExample(UserPropertyExample example);

    UserProperty selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserProperty record, @Param("example") UserPropertyExample example);

    int updateByExample(@Param("record") UserProperty record, @Param("example") UserPropertyExample example);

    int updateByPrimaryKeySelective(UserProperty record);

    int updateByPrimaryKey(UserProperty record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_property
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    UserProperty selectOneByExample(UserPropertyExample example);
}