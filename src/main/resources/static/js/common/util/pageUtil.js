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
        size: 10,
        limit: 10,
    }

}