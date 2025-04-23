// html 로드 후 init
window.addEventListener("DOMContentLoaded", () => {
  init();
});

const init = async () => {
  syncEraser();
  // await fetchGET("/api/client/list");
}

/******************** 키워드 관련 ********************/
// 키워드 지우고 focus
const eraseKeyword = () => {

  const $keyword = getDom("keyword");
  $keyword.value = "";

  syncEraser();
  $keyword.focus();
}

// 키워드에 따라 X버튼 show | hide
const syncEraser = () => {
  const $eraser = getDom("eraser");
  const keyword = getDom("keyword").value;
  hide($eraser);
  if(keyword) show($eraser);
}

