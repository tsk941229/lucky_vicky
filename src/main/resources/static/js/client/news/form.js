document.addEventListener("DOMContentLoaded", () => {
  init();
});

const init = () => {

}

const save = () => {

  const param = getParam();

}

const getParam = () => {

  const $form = getDom("form");
  const domList = $form.querySelectorAll("input, select, textarea");

  let param = {};

  for (let el of domList) {
    const {type, name, value, checked} = el;

    console.log("el", el);

    param[name] = value;
  }

  return param;


}