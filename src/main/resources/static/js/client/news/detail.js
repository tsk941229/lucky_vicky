window.addEventListener("DOMContentLoaded", () => {
    init();
});

const init = () => {

}

const saveComment = async (newsId) => {

    let commentParam = {
        newsId: newsId,
        nickname: getDom("comment-save-nickname").value,
        password: getDom("comment-save-password").value,
        content: getDom("comment-save-content").value,
    }

    if(!validateComment(commentParam)) return;

    if(!confirm("댓글 등록 하시겠습니까?")) return;

    const {status} = await fetchPOST("/client/news/save-comment", objToFormData(commentParam));

    if(!status) {
        alert("등록에 실패했습니다.");
        return;
    }

    alert("등록이 완료되었습니다.");
    // clearCommentInput();
    // TODO 댓글 비동기 업데이트
    reload();
}

const validateComment = (param) => {

    const passwordCheck = getDom("comment-save-password-check").value;

    if(!param.nickname) {
        alert("닉네임을 입력하세요.");
        return false;
    }

    if(!param.password) {
        alert("비밀번호를 입력하세요.");
        return false;
    }

    if(param.password !== passwordCheck) {
        alert("비밀번호가 일치하지 않습니다.");
        return false;
    }

    if(!param.content) {
        alert("댓글 내용을 입력하세요.");
        return false;
    }

    return true;

}

const clearCommentInput = () => {
    getDom("comment-save-nickname").value = "";
    getDom("comment-save-content").value = "";
    getDom("comment-save-password").value = "";
    getDom("comment-save-password-check").value = "";
}