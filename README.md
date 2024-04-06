# Querydsl 사용하는 이유
JPA가 sql을 사용하지 않고도 데이터베이스 연산을 수행할 수 있게 도와주지만, 복잡한 쿼리나 동적 쿼리에는 한계가 존재합니다. 그리고 이러한 한계로 인해 Querydsl이 유용한 해결책이 됩니다.			

## JPA의 한계점과 Querydsl의 등장 배경
1. JPA의 Criteria API를 이용해 동적 쿼리를 구성할 수 있지만, 쿼리가 복잡해질 경우 가독성이 떨어집니다.
2. JPQL은 문자열 기반 쿼리 언어로, 컴파일 시점에 타입 검사를 수행할 수 없어 오류가 발생할 수 있습니다.
3. JPQL은 직관적이지만 최적화된 SQL을 작성하는데 한계가 존재합니다.

## Querydsl의 장점
1. 컴파일시 타입 체크를 진행해 타입 세이프 쿼리를 작성할 수 있게 해줍니다.
2. 플루언트 API를 제공해 쿼리를 자연어처럼 읽히게 만들어 가독성을 높입니다.
3. 동적 쿼리를 간결하게 표현할 수 있습니다.

# Querydsl 적용방법

# 환경
- spring boot 3.x.x
- java 17
- Querydsl 5.0.0


# 1. build.gradle dependencies 추가
```groovy
dependencies {
	// QueryDsl 디펜던시
	implementation 'com.querydsl:querydsl-jpa:5.0.0:jakarta'
	implementation "com.querydsl:querydsl-apt:5.0.0:jakarta"
	annotationProcessor "com.querydsl:querydsl-apt:5.0.0:jakarta"
	annotationProcessor "jakarta.annotation:jakarta.annotation-api"
	annotationProcessor "jakarta.persistence:jakarta.persistence-api"
}
```

# 2. Member 엔티티 생성 그리고 build해 QMember 생성시키기
```java
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String password;

    @ManyToOne
    private Team team;
}
```

# 3. QueryDslConfig 생성 후 entityManager 등록
```java
@Configuration
@RequiredArgsConstructor
public class QueryDslConfig {
    private final EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}

```

# 4. MemberRepositoryCustom 생성 (QueryDsl을 적용할 메서드를 추상화하는 곳)
```java
public interface MemberRepositoryCustom {
    Member findByUserName(String userName);
}
```

# 5. MemberRepositoryImpl 생성 (추상화한 메서드를 QueryDsl로 구체화 시키는 곳)
```java
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
```
# 6. MemberRepository 생성(JpaRepository, MemberRepositoryImpl을 구현해 두 기술 사용할 수 있도록)
```java
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
}

```






