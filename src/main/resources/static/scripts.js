
const general_url = './rate/';

function showGif() {
    let code = $("#codes").val();
    $.ajax({
        url: general_url + 'getGif/' + code,
        method: 'GET',
        dataType: "json",
        complete: function (data) {
          let content = JSON.parse(data.responseText);
                      let img = document.createElement("img");
                      let gifName = document.createElement("p");
                      gifName.textContent = content.data.title;
                      let gifKey = document.createElement("p");
                      gifKey.textContent = content.compareResult;
                      img.src = content.data.images.original.url;
                      let out = document.querySelector("#out");
                      out.innerHTML = '';
                      out.insertAdjacentElement("afterbegin", img);
                      out.insertAdjacentElement("afterbegin", gifName);
                      out.insertAdjacentElement("afterbegin", gifKey);
        }
    })
}

//Заполняет select
function showCodes() {
    $.ajax({
        url: general_url + 'getCodes',
        method: 'GET',
        complete: function (data) {
            let codesList = JSON.parse(data.responseText);
            let select = document.querySelector("#codes");
            select.innerHTML = '';
            for (let i = 0; i < codesList.length; i++) {
                let option = document.createElement("option");
                option.value = codesList[i];
                option.text = codesList[i];
                select.insertAdjacentElement("beforeend", option);
            }
        }
    })
}
