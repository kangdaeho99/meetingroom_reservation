
var main = {
    init : function (){
        var _this = this;
        $('#btn-save').on('click', function(){
            _this.save();
        });

//      btn-update란 id를 가진 HTML 엘리먼트에 click 이벤트가 발생할때 update function을 실행하도록 이벤트를 등록합니다.
        $('#btn-update').on('click', function(){
            _this.update();
        });

        $('#btn-delete').on('click', function () {
            _this.delete();
        });
    },

    save : function(){
        console.log("hello")
        var data = {
            title : $('#title').val(),
            author : $('#author').val(),
            content: $('#content').val()
        };

        $.ajax({
            type: 'POST',
            url : '/api/v1/posts', //url 오타 안나게 해야하니다.
            dataType : 'json',
            contentType : 'application/json; charset=utf-8',
            data : JSON.stringify(data)
        }).done(function(){
            alert('글이 등록되었습니다.');
            window.location.href = '/'; // 글 등록이 성공하면 메인페이지(/)로 이동합니다.
        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    },

//  신규로 추가될 update function 입니다.
    update : function(){
        var data = {
            title : $('#title').val(),
            content : $('#content').val()
        };

        var id = $('#id').val();

/* type 'PUT'
- 여러 HTTP Method 중 PUT 메소드를 선택합니다.
- PostApiController에 있는 API에서 이미 @PutMapping으로 선언했기때문에 PUT을 사용해야합니다.
참고로 이는 REST 규약에 맞게 설정된 것입니다.
REST에서 CRUD 는 다음과 같이 HTTP Method에 매핑됩니다.
생성(Creat) - Post
읽기(Read) - GET
수정(Update) - PUT
삭제(Delete) - Delete
-url : '/api/v1/posts/'+id
-어느 게시글을 수정할지 URL Path로 구분하기 위해 Path에 id를 추가합니다.
*/
        $.ajax({
            type: 'PUT',
            url : '/api/v1/posts/' + id,
            dataType : 'json',
            contentType : 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(){
            alert('글이 수정되었습니다.');
            window.location.href = '/';
        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    },

    delete : function () {
        var id = $('#id').val();

        $.ajax({
            type: 'DELETE',
            url: '/api/v1/posts/'+id,
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function() {
            alert('글이 삭제되었습니다.');
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }

};

main.init();

/*
index.js의 첫 문장에 var main ={...} 라는 코드를 선언했습니다.
굳이 index라는 변수의 속성으로 function을 추가한 이유는 뭘까요?
예를 들어 설명하겠습니다. index.js가 다음과 같이 function을 작성한 상황이라고 가정하겠습니다.

var init = function(){
        ...
    };

var save = function(){
        ...
    };

init();

index.mustache에서 a.js가 추가되어 a.js도 a.js 만의 init과 save function이 있다면 어떻게 될까요?
브라우저의 스코프(scope)는 공용공간으로 쓰이기 때문에 나중에 로딩된 js의 init, save가 먼저 로딩된
js의 function을 덮어쓰게 됩니다.
여러 사람이 참여하는 프로젝트에서는 중복된 함수(function)이름은 자주 발생할 수 있습니다.
모든 function 이름을 확인하면서 만들 수는 없습니다.
그러다보니 이런 문제를 피하려고 index.js만의 유효범위(scope)를 만들어 사용합니다.
방법은 var index이란 객체를 만들어 해당 객체에서 필요한 모든 function을 선언하는 것입니다.
이렇게 하면 index 객체 안에서만 function 이 유효하기 때문에 다른 JS와 겹칠 위험이 사라집니다.
이런 식의 프론트엔드의 의존성 관리, 스코프 관리 등의 문제들로 최근에는 자바스크립트 개발환경이 급변했습니다.
ES6를 비롯한 최신 자바스크립트 버전이나 앵귤러, 리액트, 뷰 등은 이미 이런 기능을 프레임워크 레벨에서 지원하고 있습니다.

자 그럼 생성된 index.js를 머스테치 파일이 쓸 수 있게 footer.mustache에 추가하겠습니다.
index.js 호출 코드를 보면 절대 경로(/)로 바로 시작합니다.
스프링부트는 기본적으로 src/main/resources/static에 위치한 자바스크립트, CSS, 이미지 등 정적 파일등은 URL에서 / 로 설정됩니다.
그래서 다음과 같이 파일이 위치하면 위치에 맞게 호출이 가능합니다.
-src/main/resources/static/js/...(http://도메인/js/...)
-src/main/resources/static/css/...(http://도메인/css/...)
-src/main/resources/static/image/...(http://도메인/image/...)

모든 코드가 완성되었습니다! 등록기능을 브라우저에서 직접 테스트 해보게습니다.
동작 버튼을 클릭하면 다음과 같이 '글이 등록되었습니다' 라는 Alert이 노출됩니다.
localhost:8080/h2-console에 접속해서 실제로 DB에 데이터가 등록되었는지도 확인합니다.

등록기능이 정상적으로 작동하는 것을 확인했습니다. 다음으로 전체 조회화면을 만들어 보겠습니다.

---

btn-update버튼을 클릭하면 update 기능을 호출할 수 있게 index.js 파일에도
update function 을 하나 추가하겠습니다.
완성을 했다면,
마지막으로 전체목록에서 수정페이지로 이동할 수 있게 페이지 이동 기능을 추가해보겠습니다.
[\src\main\resources\templates\index.mustache] 코드를 다음과 같이 '살짝' 수정합니다.


--삭제이벤트를 진행할 JS코드도 추가합니다.
[\src\main\java\com\meetingroom\reservation\service\posts\PostsService.java]에서
삭제 API를 만듭니다. 먼저 서비스 메소드입니다.




*/