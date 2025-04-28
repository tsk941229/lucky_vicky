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

    const {status} = await fetchPOST("/client/news/comment/save", objToFormData(commentParam));

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


// TODO: 리팩토링 (삭제 작업 모듈화 comment, news)
/******************** 댓글 삭제 모달 ********************/
const showDeleteCommentModal = (commentId) => {
    // 모달에 id 넘겨주기
    getDom("comment-delete-modal").setAttribute("data-id", commentId);

    show(getDom("comment-delete-modal"));
    getDom("comment-delete-password").focus();
}
const closeDeleteCommentModal = () => {
    hide(getDom("comment-delete-modal"));
    getDom("comment-delete-password").value = "";
}

// 댓글 삭제
const deleteComment = async () => {
    // 넘겨 받은 id
    const commentId = getDom("comment-delete-modal").dataset.id;
    const $password = getDom("comment-delete-password");

    let param = {
        id: commentId,
        password: $password.value
    }

    const formData = objToFormData(param);

    const {status, data} = await fetchPOST("/client/news/comment/delete", formData);

    if(!status) {
        alert("삭제에 실패했습니다.");
        return;
    }

    if(!data) {
        alert("비밀번호가 일치하지 않습니다.");
        $password.focus();
        return;
    }

    if(!confirm("삭제 하시겠습니까?")) return;

    alert("삭제가 완료되었습니다.");
    // TODO 비동기 업데이트
    reload();

}

/******************** 파일 다운로드 ********************/
const newsFileDownload = async (el) => {

    const {savePath, saveName, extension, originalName} = el.dataset;

    const fileParam = {
        fullPath: `${savePath}${saveName}.${extension}`,
        originalName: originalName
    }

    await fileDownload(fileParam);

}


/******************** 뉴스 삭제 모달 ********************/
const showDeleteNewsModal = (newsId) => {
    // 모달에 id 넘겨주기
    getDom("news-delete-modal").setAttribute("data-id", newsId);

    show(getDom("news-delete-modal"));
    getDom("news-delete-password").focus();
}
const closeDeleteNewsModal = () => {
    hide(getDom("news-delete-modal"));
    getDom("news-delete-password").value = "";
}

// 뉴스 삭제
const deleteNews = async () => {
    // 넘겨 받은 id
    const newsId = getDom("news-delete-modal").dataset.id;
    const $password = getDom("news-delete-password");

    let param = {
        id: newsId,
        password: $password.value
    }

    const formData = objToFormData(param);

    const {status, data} = await fetchPOST("/client/news/delete", formData);

    if(!status) {
        alert("삭제에 실패했습니다.");
        return;
    }

    if(!data) {
        alert("비밀번호가 일치하지 않습니다.");
        $password.focus();
        return;
    }

    if(!confirm("삭제 하시겠습니까?")) return;

    alert("삭제가 완료되었습니다.");
    goTo("/client/news/list");

}

/******************** 뉴스 수정 모달 ********************/
const showUpdateNewsModal = (newsId) => {
    // 모달에 id 넘겨주기
    getDom("news-update-modal").setAttribute("data-id", newsId);

    show(getDom("news-update-modal"));
    getDom("news-update-password").focus();
}
const closeUpdateNewsModal = () => {
    hide(getDom("news-update-modal"));
    getDom("news-update-password").value = "";
}

// 비밀번호 체크, 뉴스 수정폼 이동
const goNewsUpdateForm = async () => {
    // 넘겨 받은 id
    const newsId = getDom("news-update-modal").dataset.id;
    const $password = getDom("news-update-password");

    let param = {
        id: newsId,
        password: $password.value
    }

    const formData = objToFormData(param);

    const {status, data} = await fetchPOST("/client/news/match-password", formData);

    if(!status) {
        alert("조회에 실패했습니다.");
        return;
    }

    if(!data) {
        alert("비밀번호가 일치하지 않습니다.");
        $password.focus();
        return;
    }

    goTo(`/client/news/update/${newsId}`);

}
