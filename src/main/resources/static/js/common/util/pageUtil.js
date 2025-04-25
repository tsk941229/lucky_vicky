/*
    페이징 공식

    page		// 현재 페이지
    size		// 페이지 크기 (start~end)
    limit		// 페이지당 보여줄 아이템 수
    totalCount	// 총 아이템 수

    totalPages	// 총 페이지 (totalCount -1 / limit) + 1
    startIndex	// 시작번호 (page - 1) * limit
    endIndex	// 끝번호 Math.min(startIndex + limit - 1, totalCount - 1)

    프론트 -> 서버로 보내야 하는 값 : (page, size, limit)
*/



const getPageParam = (page) => {

    return {
        page: page,
        size: 5,
        limit: 10,
    }

}

// 페이징 클릭 시 콜백 함수를 searchPage로 선언 해야 한다
const handlePageClick = (el) => {
    const page = parseInt(el.dataset.page);
    searchPage(page);
}

const prev = (el) => {
    let page = 1;
    const startPage = parseInt(el.dataset.startPage);
    const size = parseInt(el.dataset.size);

    page = Math.max(startPage - size, 1);

    searchPage(page);
}

const next = (el) => {
    let page = 1;
    const totalPages = parseInt(el.dataset.totalPages);
    const endPage = parseInt(el.dataset.endPage);

    page = Math.min(endPage + 1, totalPages);

    searchPage(page);
}
