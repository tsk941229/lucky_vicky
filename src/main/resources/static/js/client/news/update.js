document.addEventListener("DOMContentLoaded", () => {
  init();
});

const init = () => {

}

const update = async (id) => {

  const param = {
    id: id,
    category: getDom("category").value,
    title: getDom("title").value,
    content: getDom("content").value,
    nickname: getDom("nickname").value,
    password: getDom("password").value,
  }

  // 답글이면 parentId 추가
  // param["parentId"] = 1;

  // 파일 처리 (단일)
  const $newsFile = getDom("newsFile");
  if($newsFile.files && $newsFile.files.length > 0) {
    param["newsFile"] = getDom("newsFile").files[0];
  }

  const formData = objToFormData(param);

  if(!validate(param)) return;
  if(!confirm("수정 하시겠습니까?")) return;

  const {status} = await fetchPOST("/client/news/update", formData);

  if(!status) {
    alert("등록에 실패했습니다.");
    return;
  }

  alert("등록이 완료되었습니다.");
  // goTo("/client/news/list");
  goTo(`/client/news/detail/${id}`);

}

const validate = (param) => {

  if(!param.title) {
    alert("제목을 입력해주세요.");
    getDom("title").focus();
    return false;
  }
  if(!param.nickname) {
    alert("닉네임을 입력해주세요.");
    getDom("nickname").focus();
    return false;
  }
  if(!param.password) {
    alert("비밀번호를 입력해주세요.");
    getDom("password").focus();
    return false;
  }

  return true;
}
