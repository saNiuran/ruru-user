package com.ruru.plastic.user.service;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.model.PersonalCert;
import com.ruru.plastic.user.request.PersonalCertRequest;
import com.ruru.plastic.user.response.PersonalCertResponse;

public interface PersonalCertService {
    PersonalCert getPersonalCertById(Long id);

    PersonalCert getPersonalCertByUserId(Long userId);

    Msg<PersonalCert> createPersonalCert(PersonalCert personalCert);

    Msg<PersonalCert> updatePersonalCert(PersonalCert personalCert);

    Msg<Integer> deletePersonalCert(PersonalCert personalCert);

    PageInfo<PersonalCert> filterPersonalCert(PersonalCertRequest request);

}
