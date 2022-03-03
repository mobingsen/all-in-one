package org.tea.plus.jooq;

import org.tea.plus.jooq.module.userinfo.model.UserInfoModel;
import org.tea.plus.jooq.module.userinfo.repository.UserInfoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JooqHubApplicationTests {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Test
    void contextLoads() {
        final UserInfoModel model = userInfoRepository.getOne(1);
        System.out.println(model);
    }

}
