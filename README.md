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






