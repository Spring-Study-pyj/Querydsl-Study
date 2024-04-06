package com.example.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static com.example.querydsl.QMember.member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class QuerydslApplicationTests {

	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private JPAQueryFactory jpaQueryFactory;
	@Autowired
	private EntityManager entityManager;


	@Test
	@DisplayName("간단한 조회")
	void test() {
		Member member = jpaQueryFactory
				.selectFrom(QMember.member)
						.where(QMember.member.name.eq("user1"))
								.fetchOne();
		assertThat(member.getId()).isEqualTo(1L);
	}

	@Test
	@DisplayName("And 연산자 적용")
	void test2() {
		Member members = jpaQueryFactory
				.selectFrom(member)
				.where(member.id.eq(1L)
				, member.name.eq("user1"))
				.fetchOne();

		assertThat(members.getId()).isEqualTo(1L);
	}

	@Test
	@DisplayName("Or 연산자 적용")
	void test3() {
		List<Member> members = jpaQueryFactory
				.selectFrom(member)
				.where(member.id.eq(1L).or(member.id.eq(2L)))
				.orderBy(member.id.asc())
				.fetch();

		System.out.println(member);
		assertThat(members.get(0).getId()).isEqualTo(1L);
	}
	/*
	정렬 asc(), desc(), nullFirst(), nullLast
	 */
	@Test
	@DisplayName("asc() 테스트")
	void test4() {
		List<Member> members = jpaQueryFactory
				.selectFrom(member)
				.orderBy(member.id.asc())
				.fetch();

		assertThat(members.get(0).getId()).isEqualTo(1L);
	}

	@Test
	@DisplayName("desc() 테스트")
	void test5() {
		List<Member> members = jpaQueryFactory
				.selectFrom(member)
				.orderBy(member.id.desc())
				.fetch();

		assertThat(members.get(0).getId()).isEqualTo(12L);
	}

	@Test
	@DisplayName("asc 중 nullLast")
	void test6() {
		List<Member> members = jpaQueryFactory
				.selectFrom(member)
				.orderBy(member.team.id.asc().nullsLast())
				.fetch();

		assertThat(members.get(11).getTeam()).isEqualTo(null);
	}

	@Test
	@DisplayName("asc 중 nullFirst")
	void test7() {
		List<Member> members = jpaQueryFactory
				.selectFrom(member)
				.orderBy(member.team.id.asc().nullsFirst())
				.fetch();

		assertThat(members.get(0).getTeam()).isEqualTo(null);
	}



}
