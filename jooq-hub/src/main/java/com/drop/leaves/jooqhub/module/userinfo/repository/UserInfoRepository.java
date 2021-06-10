package com.drop.leaves.jooqhub.module.userinfo.repository;

import com.drop.leaves.jooqhub.module.userinfo.model.UserInfoModel;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.drop.leaves.jooqhub.sources.tables.UserInfo.USER_INFO;

/**
 * @author mbs on 2021-06-10 16:59
 */
@Repository
public class UserInfoRepository {

    @Autowired
    private DSLContext dslContext;

    public UserInfoModel getOne(Integer id) {
        return dslContext
                .select(USER_INFO.ID, USER_INFO.NAME, USER_INFO.AGE, USER_INFO.ADDR)
                .from(USER_INFO)
                .where(USER_INFO.ID.eq(id))
                .fetchOneInto(UserInfoModel.class);
    }
}
