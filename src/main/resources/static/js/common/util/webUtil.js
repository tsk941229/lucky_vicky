/**
 * fetch GET 요청 (queryParam은 js객체 형식으로 보내자)
 */
const fetchGET = async (url, param) => {
    const query = new URLSearchParams(param).toString();
    if(query) url = `${url}?${query}`;
    return await fetch(url);
}

/**
 * fetch POST 요청 (요청 body: formData)
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
        // // 유효한 value만 넘기기
        // if(value)
        formData.append(key, value);
    });
    return formData;
}

/**
 * file DOM을 받아서 객체 리스트로 반환 (파일이 1개여도 리스트로 반환함)
 */
const getFileObjList = (el) => {
    if(!el.files && el.files.length < 1) return null;

    const fileList = Array.from(el.files);
    const fileObjList = [];

    fileList.forEach(file => {
        const {name, size} = file;
        let fileParam = {
            originalName: name.split(".")[0],
            extension: name.split(".")[1],
            size: size,
        }
        fileObjList.push(fileParam);
    });

    return fileObjList;
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
