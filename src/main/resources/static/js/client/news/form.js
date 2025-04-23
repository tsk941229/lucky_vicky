document.addEventListener("DOMContentLoaded", () => {
  init();
});

const init = () => {

}

const save = async () => {

  const param = {
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

  console.log("param", param);

  const formData = objToFormData(param);

  // if(!validate(param)) return;
  if(!confirm("등록 하시겠습니까?")) return;

  const {status} = await fetchPOST("/api/client/news/save", formData);

  if(!status) {
    alert("등록에 실패했습니다.");
    return;
  }

  alert("등록이 완료되었습니다.");

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

// 파일형식 : JPG, PNG, hwp, PDF, 첨부파일 용량제한 2MB
const validateFile = (fileParam) => {

  const allowedExtList = ["jpg", "jpeg", "png", "hwp", "pdf"];

  if(!allowedExtList.find(ext => ext === fileParam.extension)) {
    alert(`${fileParam.extension}은(는) 허용되지 않은 확장자 입니다.`);
    return false;
  }
  if(fileParam.size >= 2) {
    alert("2MB 이상의 파일은 첨부할 수 없습니다.");
    return false;
  }

  return true;
}


// 파일 선택 했을 때
const chooseFile = (el) => {

  if(el.files.length < 1) return;

  for(let file of el.files) {

    let {name, size} = file;

    let fileParam = {
      originalName: name.split(".")[0],
      extension: name.split(".")[1],
      size: (size / (1024 * 1024)).toFixed(2), // Byte -> MB
    };

    if(!validateFile(fileParam)) return;

  }
}

// 답글인 경우 (parent_id)
const getParentId = () => {
  return null;
}
