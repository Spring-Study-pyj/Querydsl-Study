package com.example.querydsl;

import java.util.Optional;

public interface MemberRepositoryCustom {
    Optional<Member> findByUserName(String userName);
}
