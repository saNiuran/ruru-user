package com.ruru.plastic.user.dao;

import com.ruru.plastic.user.model.CertificateLog;
import com.ruru.plastic.user.model.CertificateLogExample;

import java.util.List;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateLogMapper {
    int countByExample(CertificateLogExample example);

    int deleteByExample(CertificateLogExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CertificateLog record);

    @Options(useGeneratedKeys = true)
    int insertSelective(CertificateLog record);

    List<CertificateLog> selectByExample(CertificateLogExample example);

    CertificateLog selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CertificateLog record, @Param("example") CertificateLogExample example);

    int updateByExample(@Param("record") CertificateLog record, @Param("example") CertificateLogExample example);

    int updateByPrimaryKeySelective(CertificateLog record);

    int updateByPrimaryKey(CertificateLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table certificate_log
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    CertificateLog selectOneByExample(CertificateLogExample example);
}