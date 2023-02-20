package com.meetingroom.reservation.service.posts;

import com.meetingroom.reservation.domain.posts.Posts;
import com.meetingroom.reservation.domain.posts.PostsRepository;
import com.meetingroom.reservation.web.dto.PostsResponseDto;
import com.meetingroom.reservation.web.dto.PostsSaveRequestDto;
import com.meetingroom.reservation.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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

 */