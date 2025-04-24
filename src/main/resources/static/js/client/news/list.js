// html 로드 후 init
window.addEventListener("DOMContentLoaded", () => {
  init();
});

const init = async () => {
  // 검색창
  syncEraser();

  // 이전 검색 정보 가져올 수 있음
  let page = 1;
  await search(page);
}


/******************** 검색 관련 ********************/
const search = async (page) => {

  let searchParam = {
    page: page,
    // n개씩 보기 추가될 수 있음
  }

  // 검색 키워드 있으면 추가
  const keyword = getDom("keyword").value;
  if(keyword) searchParam["keyword"] = keyword;

  const response = await fetchGET("/api/client/news/list", searchParam);
  const container = getDom("news-container");

  // 비동기로 그리기
  container.innerHTML = await response.text();

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


