document.addEventListener("DOMContentLoaded", () => {
  init();
});

const init = () => {

}

const save = async () => {

  const param = {
    parentId: getParentId(),
    category: getDom("category").value,
    title: getDom("title").value,
    content: getDom("content").value,
    nickname: getDom("nickname").value,
    password: getDom("password").value,
    newsFile: getDom("newsFile").files[0]
  }

  // if(!validate(param)) return;

  const formData = objToFormData(param);

  const {status, data} = await fetchPOST("/api/client/news/save", formData);

  console.log("status",status);
  console.log("data",data);

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

const validateFile = (param) => {
  console.log("param", param);
}

// 답글인 경우 (parent_id)
const getParentId = () => {
  return null;
}

// 파일 하나(multiple x)를 가정
const chooseFile = (el) => {
  let {name, size, type} = el.files[0];

  // Byte -> MB
  size = (size / (1024 * 1024)).toFixed(2);

  let originalName = name.split(".")[0];
  let extension = name.split(".")[1];

  const param = {
    originalName: originalName,
    size: size,
    extension: extension,
    type: type
  }

  validateFile(param);

}
