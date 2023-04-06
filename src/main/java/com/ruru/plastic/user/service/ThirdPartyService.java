package com.ruru.plastic.user.service;


import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.model.ThirdParty;

import java.util.List;

public interface ThirdPartyService {
    ThirdParty getThirdPartyById(Long id);

    ThirdParty getThirdPartyByTypeAndUid(ThirdParty thirdParty);

    List<ThirdParty> getThirdPartyByTypeAndUserId(ThirdParty thirdParty);

    Msg<ThirdParty> createThirdParty(ThirdParty thirdParty);

    Msg<ThirdParty> updateThirdParty(ThirdParty thirdParty);

    Msg<ThirdParty> unBindingThirdParty(ThirdParty thirdParty);
}
