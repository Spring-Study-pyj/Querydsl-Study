package com.example.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import static com.example.querydsl.QMember.member;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Optional<Member> findByUserName(String userName) {
        return jpaQueryFactory
                .selectFrom(member)
                .where(member.name.eq(userName))
                .stream().findFirst();

    }
}
