/**
 * fetch GET 요청
 */
const fetchGET = async (url) => {
    const response = await fetch(url);
    const jsonData = await response.json();
    console.log("jsonData", jsonData);
}

/**
 * fetch POST 요청
 */
const fetchPOST = async (url, param) => {
    const option = {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(param),
    };

    return await (await fetch(url, option)).json();
}

/**
 * 파라미터 주소로 이동
 */
const goTo = (url) => {
    location.href = url;
}

/**
 * 이건 슬픈 자기소개서
 */
const goBack = () => {
    history.back();
}

/**
 * 새로고침
 */
const reload = () => {
    location.reload();
}
