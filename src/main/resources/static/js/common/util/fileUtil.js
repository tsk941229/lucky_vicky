// 파일형식 : JPG, PNG, hwp, PDF, 첨부파일 용량제한 2MB
// 필요하면 (제한 용량, 형식 사용처에서 넘기는걸로)
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