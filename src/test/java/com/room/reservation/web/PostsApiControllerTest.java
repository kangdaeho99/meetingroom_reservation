package com.room.reservation.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.room.reservation.domain.posts.Posts;
import com.room.reservation.domain.posts.PostsRepository;
import com.room.reservation.web.dto.PostsSaveRequestDto;
import com.room.reservation.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    //@Before : 매번 테스트가 시작되기 전에 MockMvc 인스턴스를 생성합니다.
    @Before
    public void setup(){
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @After
    public void tearDown() throws Exception{
        postsRepository.deleteAll();
    }

    @Test
//    WithMockUser(roles="USER") : 인증된 모의(가짜)사용자를 만들어서 사용합니다.
//    : roles에 권한을 추가할 수 있습니다.
//    : 즉, 이 어노테이션으로 인해 ROLE_USER 권한을 가진 사용자가 API를 요청하는 것과 동일한 효과를 가지게 됩니다.
    @WithMockUser(roles="USER")
    public void Posts_등록된다() throws Exception{
        //given
        String title = "title";
        String content = "content";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto
                .builder()
                .title(title)
                .content(content)
                .author("author")
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
//        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);
//        mvc.perform : 생성된 MockMvc를 통해 API를 테스트합니다.
//        :본문(body) 영역은 문자열로 표현하기 위해 ObjecMapper를 통해 문자열 JSON으로 변환합니다.
        mvc.perform(post(url).
                contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
//    WithMockUser(roles="USER") : 인증된 모의(가짜)사용자를 만들어서 사용합니다.
//    : roles에 권한을 추가할 수 있습니다.
//    : 즉, 이 어노테이션으로 인해 ROLE_USER 권한을 가진 사용자가 API를 요청하는 것과 동일한 효과를 가지게 됩니다.
    @WithMockUser(roles="USER")
    public void Posts_수정된다() throws Exception{
        //given
        Posts savedPosts = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        Long updateId = savedPosts.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        String url = "http://localhost:"+port+"/api/v1/posts/"+updateId;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        //when
//        ResponseEntity<Long> responseEntity = restTemplate
//                .exchange(url, HttpMethod.PUT, requestEntity, Long.class);
        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);

    }


}

/*
API Controller를 테스트하는데 HelloController와 달리 @WebMvcTest를 사용하지 않았습니다.
@WebMvcTest의 경우 JPA 기능이 작동하지 않기 때문인데, Controller와 ControllerAdvice 등 외부 연동과 관련된 부분만 활성화되니 지금같이 JPA기능까지
한번에 테스트할때는 @SpringBootTest와 TestRestTemplate을 사용하면됩니다.
테스트를 수행해보면 다음과 같이 성공하는 것을 확인할 수 있습니다.
Tomcat started on port(s): 54877
create table posts (id bigint not null auto_increment, author varchar(255), content TEXT not null, title varchar(500) not null, primary key (id)) engine=InnoDB
insert into posts (author, content, title) values (?, ?, ?)

WebEnvironment.RANDOM_PORT로 인한 랜덤포트 실행과 insert 쿼리가 실행된 것 모두 확인했습니다.
등록 기능 (
-PostsApiController의 "/api/vi/posts" save 함수,
-PostsService의 save 함수,
-PostsSaveRequestDto의 객체, toEntity,
- 그리고 마지막으로 PostApiControllerTests.java에서 Posts_등록된다() 로 테스트)

Posts_등록된다())을 완성했으니 수정/조회 기능도 빠르게 만들어보겠습니다.

수정기능 (
-PostsApiController의 "/api/vi/posts/{id} update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto)
-[src/main/java/com.meetingroom.reservation/web/dto/PostsUpdateResponseDto.java] PostsUpdateReuqestDto
-Posts의 public void update(String title, String content)
-PostsService의 Long update(Long id, PostsUpdateRequestDto requestDto)
)

조회기능 (
-PostsApiController의 "/api/vi/posts/{id} findById(@PathVariable Long id)
-[src/main/java/com.meetingroom.reservation/web/dto/PostsResponseDto.java] PostsResponseDto
-PostsService의 PostsResponseDto findById(Long id)
)


----

테스트 결과를 보면 update 쿼리가 수행되는것을 확인할 수 있습니다.
update posts set author=?, content=?, title=? where id=?
어떤가요? 예전 MyBatis를 쓰던것과 달리 JPA를 씀으로 좀 더 객체지향적으로 코딩할 수 있음이 느껴지나요?
JPA와 테스트 코드에 대해 진행해보았으니, 조회기능은 실제로 톰켓을 실행해서 확인해보겠습니다.
앞서 언급한대로 로컬환경에선 데이터베이스로 H2를 사용합니다.
메모리에서 실행하기 때문에 직접 접근하려면 웹 콘솔을 사용해야만 합니다.
먼저 웹 콘솔 오션을 활성화합니다. application.properties에 다음과 같이 옵션을 추가합니다.
-spring.h2.console.enabled = true

추가한뒤 Application 클래스의 main 메소드를 실행합니다.
정상적으로 실행됐다면 톰캣이 8080포트로 실행됐습니다.
여기서 웹 브라우저에 http://localhost:8080/h2-console로 접근하면 다음과 같이 웹 콘솔 화면이 등장합니다.

이때 JDBC URL이 앞화면과 같이 jdbc:h2:mem:testdb 로 되어있지 않다면 똑같이 작성해주셔야 합니다.
아래 [connect] 버튼을 클릭하면 현재 프로젝트의 H2를 관리할 수 있는 관리페이지로 이동합니다.
다음과 같이 POSTS 테이블이 정상적으로 노출되어야만 합니다.
간단한 쿼리를 실행해봅니다.
SELECT * FROM posts;
현재는 등록된 데이터가 없습니다. 간단하게 insert 쿼리를 실행해보고 이를 API로 조회해보겠습니다.
INSERT 쿼리 : insert into posts (author, content, title) values ('author', 'content', 'title');
등록된 데이터를 확인후 API를 요청해보겠습니다.
브라우저에 http://localhost:8080/api/v1/posts/1 을 입력해 API 조회기능을 테스트해봅니다.

기본적인 등록/수정/조회 기능을 모두 만들고 테스트해보았습니다.
특히 등록/수정은 테스트코드로 보호해주고 있으니 이후 변경 사항이 있어도 안전하게 변경할 수 있습니다.

3.5 JPA Auditing 으로 생성시간/수정시간 자동화하기
보통 엔티티(Entity)에는 해당 데이터의 생성시간과 수정시간을 포함합니다.
언제 만들어졌는지, 언제 수정되었는지 등은 차후 유지보수에 있어 굉장히 중요한 정보이기 때문입니다.
그렇다보니 매번 DB에 삽입(insert)하기 전, 갱신(Update) 하기전에 날짜 데이터를 등록/수정하는 코드가 여기저기 들어가게 됩니다.

//생성일 추가 코드 예제
public void savePosts(){
    ...
    posts.setCreateDate(new LocalDate());
    postsRepository.save(posts);
    ...
}

이런 단순하고 반복적인 코드가 모든 테이블과 서비스 메소드에 포함되어야한다고 생각하면 어마어마하게 귀찮고 코드가 지전분해집니다.
그래서 이 문제를 해결하고자 JPA Auditing를 사용하겠습니다.

LocalDate 사용
여기서부터는 날짜 타입을 사용합니다. Java 8 부터 LocalDate와 LocalDateTime이 등장했습니다.
그간 Java의 기본 날짜 타입인 Date의 문제점을 제대로 고친 타입이라 Java 8일 경우 무조건 써야한다고 생각하면 됩니다.
[참고] Java 8 이 나오기 전까지 사용되었던 Date와 Calender 클래스는 다음과 같은 문제점들이 있었습니다.
1. 불변(변경이 불가능한 )객체가 아닙니다.
- 멀티스레드 환경에서 언제든 문제가 발생할 수 있습니다.

2. Calendar는 월(Month)값 설계가 잘못되었습니다.
- 10월을 나타내는 Calender.OCTOBER의 숫자값은 '9'입니다.
- 당연히 '10'으로 생각했던 개발자들에게는 큰 혼란이 왔습니다.

JodaTime이라는 오픈소스를 사용해서 문제점들을 피했었고, Java8에선 LocalDate를 통해 해결했습니다.
자세한 내용은 Naver D2 - Java의 날짜와 시간 API를 참고하세요.(https://d2.naver.com/helloworld/645609)
LocalDate와 LocalDateTime이 데이터베이스에 제대로 매핑되지 않는 이슈가 Hibernate 5.2.10 버전에서 해결되었습니다.
스프링부트 1.x 를 쓴다면 별도로 Hibernate 5.2.10 버전 이상을 사용하도록 설정이 필요하지만, 스프링 부트 2.x 버전을 사용하면 기본적으로
해당 버전을 사용 중이라 별다른 설정없이 바로 적용하면 됩니다.
domain 패키지에 BaseTimeEntity 클래스를 생성합니다.

--- Spring Security 인증되지 않은 사용자의 요청을 설정 테스트 새로 Test 진행을 위해 추가한 코드들 ---
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    //@Before : 매번 테스트가 시작되기 전에 MockMvc 인스턴스를 생성합니다.
    @Before
    public void setup(){
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

Potss_등록된다
    WithMockUser(roles="USER") : 인증된 모의(가짜)사용자를 만들어서 사용합니다.
    : roles에 권한을 추가할 수 있습니다.
    : 즉, 이 어노테이션으로 인해 ROLE_USER 권한을 가진 사용자가 API를 요청하는 것과 동일한 효과를 가지게 됩니다.
//@WithMockUser(roles="USER")
//when
//        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);
//        mvc.perform : 생성된 MockMvc를 통해 API를 테스트합니다.
//        :본문(body) 영역은 문자열로 표현하기 위해 ObjecMapper를 통해 문자열 JSON으로 변환합니다.
        mvc.perform(post(url).
                contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

   Posts_수정된다
    WithMockUser(roles="USER") : 인증된 모의(가짜)사용자를 만들어서 사용합니다.
    : roles에 권한을 추가할 수 있습니다.
    : 즉, 이 어노테이션으로 인해 ROLE_USER 권한을 가진 사용자가 API를 요청하는 것과 동일한 효과를 가지게 됩니다.
        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

----- 위의 코드등릉ㄹ 추가합니다.

 */