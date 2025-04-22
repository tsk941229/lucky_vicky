/**
 * fetch GET 요청
 */
const fetchGET = async (url) => {
    const response = await fetch(url);
    const jsonData = await response.json();
    console.log("jsonData", jsonData);
}

/**
 * fetch POST 요청 (요청 body는 formData로 통일)
 */
const fetchPOST = async (url, formData) => {
    const option = {
        method: "POST",
        body: formData,
    };

    return await (await fetch(url, option)).json();
}

/**
 * Object -> FormData
 */
const objToFormData = (obj) => {
    const formData = new FormData();
    Object.entries(obj).forEach(([key, value]) => {
        formData.append(key, value);
    });
    return formData;
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
