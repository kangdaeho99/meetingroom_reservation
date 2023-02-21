package com.meetingroom.reservation.web.dto;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/")
    public String index(){
        return "index";
    }

}

/*
머스테치 스타터 덕분에 컨트롤러에서 문자열을 반환할때 앞의 경로와 뒤의 파일 확장자는 자동으로 지정됩니다.
앞의 경로는 src/main/resources/templates로 뒤의 파일 확장자는 .mustache가 붙는 것입니다.
즉 여기선 "index"을 반환하므로 src/main/resources/templates/index.mustache로 전환되어 View Resolver가 처리하게 됩니다.
ViewResolver는 URL 요청의 결과를 전달할 타입과 값을 지정하는 관리자 격으로 볼 수 있습니다.

자 여기까지 코드가 완성되었으니 이번에도 테스트 코드로 검증해보겠습니다.
test 패키지에 IndexControllerTest 클래스를 생성합니다.

 */