/**
 * DOM Element 가져오기 (ById)
 */
const getDom = (id) => {
    return document.getElementById(id);
}

/**
 * Element 보여주기
 */
const show = (el) => {
    el.style.display = "block"
}

/**
 * Element 숨기기
 */
const hide = (el) => {
    el.style.display = "none"
}

/**
 * Element 토글
 */
const toggle = (el) => {
    el.style.display = window.getComputedStyle(el).display == "none" ? "block" : "none";
}
