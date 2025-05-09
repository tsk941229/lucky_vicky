window.onload = () => {
  init();
}

const init = () => {
  initEditor("#content");
}

const save = async () => {

  // 에디터 내용에 입력된 값을 선택자 html 태그(textarea)에 넣어주기
  tinymce.triggerSave();

  let param = {
    category: getDom("category").value,
    title: getDom("title").value,
    content: getDom("content").value,
    nickname: getDom("nickname").value,
    password: getDom("password").value,
    depth: 0
  }

  // 답글일 때
  const {parentId, parentDepth} = Object.fromEntries(new URLSearchParams(window.location.search));

  if(parentId !== undefined && parentDepth !== undefined) {
    param = {
      ...param,
      parentId: parseInt(parentId),
      depth: parseInt(parentDepth) + 1
    }
  }

  // 파일 처리 (단일)
  const $newsFile = getDom("newsFile");
  if($newsFile.files && $newsFile.files.length > 0) {
    param["newsFile"] = getDom("newsFile").files[0];
  }

  const formData = objToFormData(param);

  if(!validate(param)) return;
  if(!confirm("등록 하시겠습니까?")) return;

  const {status} = await fetchPOST("/client/news/save", formData);

  if(!status) {
    alert("등록에 실패했습니다.");
    return;
  }

  alert("등록이 완료되었습니다.");
  goTo("/client/news/list");

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

// 답글인 경우 (parent_id)
const getParentId = () => {
  return null;
}
