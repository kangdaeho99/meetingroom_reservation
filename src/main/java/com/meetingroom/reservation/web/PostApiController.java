package com.meetingroom.reservation.web;

import com.meetingroom.reservation.service.posts.PostsService;
import com.meetingroom.reservation.web.dto.PostsResponseDto;
import com.meetingroom.reservation.web.dto.PostsSaveRequestDto;
import com.meetingroom.reservation.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostApiController {
    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto){
        return postsService.save(requestDto);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto){
        return postsService.update(id, requestDto);
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto findById(@PathVariable Long id){
        return postsService.findById(id);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public Long delete(@PathVariable Long id){
        postsService.delete(id);
        return id;
    }

}


/*
스프링을 어느정도 써보셨던 분들이라면 Controller와 Service에서 @Autowired가 없는 것이 어색하게 느껴집니다.
스프링에선 Bean을 주입받는 방식들이 다음과 같습니다.
-@Autowired -setter, - 생성자
이 중 가장 권장하는 방식이 생성자로 주입받는 방식입니다.(@Autowired는 권장하지 않습니다.)
즉, 생성자로 Bean 객체를 받도록 하면 @Autowired와 동일한 효과를 볼 수 있다는 것입니다.
그러면 앞에서 생성자는 어디 있을까요?
바로 @RequiredArgsConstructor에서 해결해줍니다. final 이 선언된 모든 필드를 인자값으로 하는 생성자를 롬복의 @RequiredArgsConstructor가 대신 생성해준 것입니다.
생성자를 직접 안쓰고 롬복 어노테이션을 사용한 이유는 간단합니다.
해당 클래스의 의존성 관계가 변경될때마다 생성자 코드를 계속해서 수정하는 번거로움을 해결하기 위함입니다.
[롬복 어노테이션이 있으면 해당 컨트롤러에 새로운 서비스를 추가하거나, 기존 컴포넌트를 제거하는 등의 상황이 발생해도 생성자 코드는 전혀 손대지 않아도 됩니다. 편리하죠?]

자, 이제는 Controller와 Service에서 사용할 Dto 클래스를 생성하겠습니다.
[src/main/java/com.meetingroom.reservation/web/dto/PostsSaveRequestDto.java]


---
delete() 함수가 완성되엇다면 한번 테스트를 해봅니다.
좀 전에 수정한 테스트2 게시글의 수정화면에서 삭제버튼을 클릭합니다.
다음과 같이 삭제 성공메세지를 확인합니다.
자동으로 메인페이지로 이동하면, 기존 게시글이 삭제되있는지 확인합니다.

수정/삭제 기능까지 완성되었습니다!
이번 장에서 진행한 화면 개발뿐만 아니라 웹요청에서의 테스트 코드 작성방법은 필수로 익혀가길 바랍니다.
기본적인 게시판 기능이 완성되었으니 이제 로그인 기능을 만들어보겠습니다.


 */