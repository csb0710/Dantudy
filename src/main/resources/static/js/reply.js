'use strict';

const commentWrite = () => {
    const writer = document.getElementById("commentWriter").value;
    const contents = document.getElementById("commentContents").value;
    console.log(writer);
    console.log(contents);
    const id = [[${board.id}]]; // 게시글 번호

    $.ajax({
        type : "post",
        url : "/comment/save",
        data: {
            "commentWriter" : writer,
            "commentContents" : contents,
            "boardId" : id
        },
        success: function(res) {
            console.log("요청 성공", res);
        },
        error : function(err) {
            console.log("요청실패", err);
        }
    });
}