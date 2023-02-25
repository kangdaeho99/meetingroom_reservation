package com.meetingroom.reservation.service.posts;

import com.meetingroom.reservation.domain.posts.Posts;
import com.meetingroom.reservation.domain.posts.PostsRepository;
import com.meetingroom.reservation.web.dto.PostsListResponseDto;
import com.meetingroom.reservation.web.dto.PostsResponseDto;
import com.meetingroom.reservation.web.dto.PostsSaveRequestDto;
import com.meetingroom.reservation.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto){
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto){
        Posts posts = postsRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("해당 게시글이 없습니다. id="+ id));
        posts.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    public PostsResponseDto findById(Long id){
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new
                        IllegalArgumentException("해당 게시글이 없습니다. id=" +id));

        return new PostsResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc(){
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id){
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 없습니다. id="+id));

        postsRepository.delete(posts);
        /*
        JPA Reposity에서 이미 delete 메소드를 지원하고 있으니 이를 활용합니다.
        엔티티를 파라미터로 삭제할수도 있고, deleteById메소드를 이용하면 id로 삭제할 수도 있습니다.
        존재하는 Posts인지 확인을 위해 엔티티 조회 후 그대로 삭제합니다.
         */
    }

}

/*
여기서 신기한 기능이 있습니다. update 기능에서 데이터베이스에 쿼리를 날리는 부분이 없습니다.
이게 가능한 이유는 JPA의 영속성 컨텍스트 때문입니다.
영속성 컨텍스트란, 엔티티를 영구 저장하는 환경입니다. 일종의 논리적 개념이라 보시면되며, JPA의 핵심내용은 엔티티가 영속성 컨텍스트에 포함되어있냐
아니냐로 갈립니다.
JPA의 엔티티매니저(Entity Manager)가 활성화된 상태로(Spring Data Jpa를 쓴다면 기본옵션) 트랜잭션 안에서 데이터베이스에서 데이터를 가져오면
이 데이터는 영속성 컨텍스트가 유지된 상태입니다.

이 상태에서 해당 데이터의 값을 변경하면 트랜잭션이 끝나는 시점에 해당 테이블에 변경분을 반영합니다.
즉, Entity 객체의 값만 변경하면 별도로 Update 쿼리를 날릴 필요가 없다는 점입니다.
이 개념을 더티체킹(dirty checking)이라고 합니다. 추가정보는 더티체킹(dirty checking)이란 ? https://jojoldu.tistory.com/415)
자 그럼 실제로 이 코드가 정상적으로 Update 쿼리를 수행하는지 테스트 코드로 확인해보겠습니다.
수정 기능의 테스트 코드는 등록기능과 마찬가지로 PostsApiController-Test에 추가하겠습니다.

---
findAllDesc 메소드의 트랜잭션 어노테이션(@TransActional)에 옵션이 하나 추가되었습니다.
(readOnly = true)를 주면 트랜잭션 범위는 유지하되, 조회 기능만 남겨두어 조회속도가 개선되기때문에
등록, 수정, 삭제 기능이 전혀 없는 서비스 메소드에서 사용하는 것을 추천합니다.

메소드 내부의 코드에선 람다식을 모르시면 조금 생소한 코드가 있을 수 있습니다.
.map(PostsListResponseDto::new) 앞의 코드는 실제로 다음과 같습니다.
.map(posts -> new PostsListResponseDto(posts))
PostsRepository 결과로 넘어온 Posts의 Stream을 map을 통해
PostsListResponseDTO 변환 -> List로 반환하는 메소드입니다.
아직 PostsListResponseDto 클래스가 없기 때문에 이 클래스 역시 생성합니다.
[\src\main\java\com\meetingroom\reservation\web\dto\PostsListResponseDto.java]
@Getter
public class PostsListResponseDto {
    private Long id;
    private String title;
    private String author;
    private LocalDateTime modifiedDate;

    public PostsListResponseDto(Posts entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.modifiedDate = entity.getModifiedDate();
    }
}

위의 작업을 완료했다면 마지막으로 Controller를 변경합니다.
[\src\main\java\com\meetingroom\reservation\web\IndexController.java]
    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("posts", postsService.findAllDesc());
        return "index";
    }

---
서비스에서 만든
public void delete() 메소드를 컨트롤러가 사용하도록 코드를 추가합니다.
[\src\main\java\com\meetingroom\reservation\web\PostApiController.java]
에 추가합니다.


 */