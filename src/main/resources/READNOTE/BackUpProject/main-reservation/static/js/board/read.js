
<!-- 댓글목록 보여주는 함수-->
$(document).ready(function() {
    var bno = [[${dto.bno}]];
    <!--댓글이 추가될 영역-->
    var replyList = $(".list-group");

    $(".replyCount").click(function(){
        $.getJSON('/replies/board/'+bno, function(arr){
            console.log(arr);
            loadJSONData();
        }) //end getJSON
    }) //end click



<!--날짜처리를 위한 함수-->
    function formatTime(str){
        var date = new Date(str);

        return date.getFullYear() + '/' +
            (date.getMonth() + 1) + '/' +
            date.getDate() + ' '+
            date.getHours() + ':' +
            date.getMinutes();
    }

    <!--특정한 게시글의 댓글을 처리하는 함수-->
    function loadJSONData(){
        $.getJSON('/replies/board/' + bno, function(arr){
            console.log(arr);
            var str = "";
            $('.replyCount').html(" Reply Count " + arr.length);

            $.each(arr, function(idx, reply){
                console.log(reply);
                str +='                    <li class="list-group-item" data-rno="'+reply.rno+'">';
                str +='                        <div class="d-flex w-100 justify-content-between">';
                str +='                             <h5 class="mb-1 replyer">'+reply.replyer+'</h5>';
<!--                str +='                            <small>'+formatTime(reply.regDate)+'</small>';-->
                str +='                        </div>';
                str +='                        <p class="mb-1 replyContent">'+reply.text+'</p>';
                str +='                    </li>';
            }); // <--- 닫히는 괄호 추가

            replyList.html(str);
        });
    }


<!-- 댓글등록 모달-->
    var addReplyModal = new bootstrap.Modal(document.querySelector(".modal"));

    $(".addReply").click(function () {
        addReplyModal.show();

        <!-- 댓글 입력하는 부분 초기화 -->
        $('input[name="replyText"]').val('');
        $('input[name="replyer"]').val('');

        $(".modal-footer .btn").hide(); //모달내의 모든 버튼 안보이도록
        $(".replySave, .replyClose").show(); //필요한 버튼들만 보이도록

    });

    <!-- 댓글모달창의 SAVE 버튼을 클릭할시 -->
    $(".replySave").click(function() {

        var reply = {
            bno : bno,
            text : $('input[name="replyText"]').val(),
            replyer : $('input[name="replyer"]').val()
        }

        console.log(reply);

        $.ajax({
            url: '/replies/',
            method: 'post',
            data: JSON.stringify(reply),
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            success: function(data){
                console.log(data);

                var newRno = parseInt(data);
                alert(newRno + "번 댓글이 등록되었습니다.");
                addReplyModal.hide();
                loadJSONData();
            }
        })
    });

    $('.replyList').on("click", ".list-group-item", function(){
        var rno = $(this).data("rno");

        $("input[name='replyText']").val( $(this).find('.replyContent').html());
        $("input[name='replyer']").val( $(this).find('.replyer').html());
        $("input[name='rno']").val(rno);

        $(".modal-footer .btn").hide();
        $(".replyRemove, .replyModify, .replyClose").show();

        addReplyModal.show();
    });

    $('.replyRemove').on("click", function() {
        var rno = $("input[name='rno']").val();

        $.ajax({
            url : '/replies/' + rno,
            method : 'delete',

            success : function(result){
                console.log("result : " + result);
                if(result === 'success'){
                    alert('댓글이 삭제되었습니다.');
                    addReplyModal.hide();
                    loadJSONData();
                }
            }
        })
    });

    $(".replyModify").click(function() {
        var rno = $("input[name='rno']").val();

        var reply = {
            rno : rno,
            bno : bno,
            text : $('input[name="replyText"]').val(),
            replyer : $('input[name="replyer"]').val()
        }

        console.log(reply);
    });


});