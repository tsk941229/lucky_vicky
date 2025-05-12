window.onload = async () => {
    await init();
}

const init = async () => {
    await initLikes();
}

const saveComment = async (newsId) => {

    const commentParam = {
        newsId: newsId,
        nickname: getDom("comment-save-nickname").value,
        password: getDom("comment-save-password").value,
        content: getDom("comment-save-content").value,
    }

    const passwordCheck = getDom("comment-save-password-check").value;

    if(!validateComment(commentParam, passwordCheck)) return;

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

const validateComment = (param, passwordCheck) => {

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

const goReplyForm = async (id, depth) => {
    const param = {
        parentId: id,
        parentDepth: depth
    }
    const query = new URLSearchParams(param).toString();
    goTo(`/client/news/form?${query}`);
}

/******************** 대댓글 (댓글의 답글) ********************/
const saveCommentReply = async (newsId, commentId) => {

    const param = {
        newsId: newsId,
        parentId: commentId,
        content: getDom(`comment-reply-${commentId}-content`).value,
        nickname: getDom(`comment-reply-${commentId}-nickname`).value,
        password: getDom(`comment-reply-${commentId}-password`).value,
    };

    const passwordCheck = getDom(`comment-reply-${commentId}-password-check`).value;

    if(!validateComment(param, passwordCheck)) return;

    const {status} = await fetchPOST("/client/news/comment/save", objToFormData(param));

    if(!confirm("등록 하시겠습니까?")) return;

    if(!status) {
        alert("등록에 실패했습니다.");
        return;
    }

    alert("등록이 완료되었습니다.");

    // 카운트 up (임시)
    const $replyCount = getDom(`replyCount-${commentId}`);
    let replyCount = isNaN(parseInt($replyCount.textContent)) ? 0 : parseInt($replyCount.textContent);
    $replyCount.textContent = replyCount + 1;

    clearCommentReplyInput(commentId);

    await getCommentReplyList(commentId);

}

const clearCommentReplyInput = (commentId) => {
    getDom(`comment-reply-${commentId}-content`).value = "";
    getDom(`comment-reply-${commentId}-nickname`).value = "";
    getDom(`comment-reply-${commentId}-password`).value = "";
    getDom(`comment-reply-${commentId}-password-check`).value = "";
}

const getCommentReplyList = async (commentId) => {

    const param = {
        commentId: commentId
    }

    const response = await fetchGET('/client/news/comment/comment-reply-list-inner', param);

    const container = getDom(`comment-reply-${commentId}-list-container`);

    container.innerHTML = await response.text();

}

const toggleCommentReply = async (commentId) => {
    const $commentReply = getDom(`comment-reply-${commentId}`);
    toggle($commentReply);

    if($commentReply.getAttribute("isFound") !== "Y") {
        await getCommentReplyList(commentId);
    }

    // 이미 조회 한번 했으면 조회 안하도록
    $commentReply.setAttribute("isFound", "Y");

}

/******************** 좋아요 ********************/
const initLikes = async () => {

    // id 가져오기 임시 TODO: url에 의존하기 때문에 별로 좋지 않음 나중에 수정해보자
    const pathParts = window.location.pathname.split("/");
    const id = pathParts.at(-1);

    const {status, data} = await fetch(`/client/news/check-likes/${id}`);

    if(!status) {
        alert("좋아요 여부 확인에 실패했습니다.");
        return;
    }

    setLikesFalse();

    if(data) setLikesTrue();

}

const toggleLikes = async (id) => {

    const $likes = getDom("likes");

    const isLiked = $likes.getAttribute("isLiked");

    /*
    * isLiked == "Y": 좋아요 이미 누른상태
    * isLiked == "N": 좋아요 안누른 상태
    * */

    let isUp = false;
    setLikesFalse();

    if(isLiked === "N") {
        isUp = true;
        setLikesTrue();
    }

    const param = {
        id: id,
        isUp: isUp
    }

    const formData = objToFormData(param);

    const {status} = await fetchPOST("/client/news/toggle-likes", formData);

    if(!status) {
        // 원상복귀
        setLikesFalse();
        if(isLiked === "Y") setLikesTrue();

        alert("좋아요 적용에 실패했습니다.");
    }

}

const setLikesTrue = () => {
    const $likes = getDom("likes");
    $likes.style.backgroundColor = "hotpink";
    $likes.setAttribute("isLiked", "Y");
}

const setLikesFalse = () => {
    const $likes = getDom("likes");
    $likes.style.backgroundColor = "gray";
    $likes.setAttribute("isLiked", "N");
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

    closeDeleteCommentModal();
    // TODO 비동기 업데이트
    reload();

}

/******************** 대댓글(답글) 삭제 모달 ********************/
const showDeleteCommentReplyModal = (commentReplyId) => {
    // 모달에 id 넘겨주기
    getDom("comment-reply-delete-modal").setAttribute("data-id", commentReplyId);
    // getDom("comment-reply-delete-modal").setAttribute("data-comment-id", commentId);

    show(getDom("comment-reply-delete-modal"));
    getDom("comment-reply-delete-password").focus();
}

const closeDeleteCommentReplyModal = () => {
    hide(getDom("comment-reply-delete-modal"));
    getDom("comment-reply-delete-password").value = "";
}

// 대댓글 삭제
const deleteCommentReply = async () => {
    // 넘겨 받은 id
    const commentReplyId = getDom("comment-reply-delete-modal").dataset.id;
    // const commentId = getDom("comment-reply-delete-modal").dataset.commentId;
    const $password = getDom("comment-reply-delete-password");

    let param = {
        id: commentReplyId,
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

    closeDeleteCommentModal();
    // TODO 비동기 업데이트
    reload();

    // await getCommentReplyList(commentId);

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

    closeDeleteNewsModal();
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

    closeUpdateNewsModal();
    goTo(`/client/news/update/${newsId}`);

}
