const header = new Headers();
header.append('Content-Type', 'application/json');
header.append('Authorization', 'Bearer ' + getCookie('token'));

// Переменные для GET запросов
const trackedTags = document.querySelector('#trackedTags');
const ignoredTags = document.querySelector('#ignoredTags');

const trackUrl = '/api/user/tag/tracked';
const ignoreUrl = '/api/user/tag/ignored';

const inputSearchTrackedTag = document.getElementById('inputSearchTrackedTag');
const inputSearchIgnoredTag = document.getElementById('inputSearchIgnoredTag');
const resultSearchTrackedTag = document.getElementById('resultSearchTrackedTag')
const resultSearchIgnoredTag = document.getElementById('resultSearchIgnoredTag')

let resultsWrapper = document.querySelector('.resultSearch')

////////////////////////////////////////////
// Переменная для добавления меток по tag.id
let selectIdTag = 0;

/////////////////////////////////////////////////////////////
// Методы для получения и выбора данных из выпадающего поиска

function startInput(tableInput) {
    tableInput.addEventListener('keyup', (e) => {
        let input = e.target.value;
        if (input.length > 0) {
            resultsWrapper.style.display = "block";
            resultSearchIgnoredTag.style.display = "block"
            document.getElementById('inputSearchTrackedTag').oninput = debounce(getLatterTracked, 350);
            document.getElementById('inputSearchIgnoredTag').oninput = debounce(getLatterIgnored, 350);
        } else {
            resultsWrapper.style.display = "none";
            resultsWrapper.innerHTML = "";
        }
    })
}

function select(element) {
    let selectData = element.textContent.toLowerCase();
    if (inputSearchTrackedTag.value !== "") {
        inputSearchTrackedTag.value = selectData
        selectIdTag = element.value
        resultsWrapper.style.display = "none"
        resultsWrapper.innerHTML = ""
    }
    if (inputSearchIgnoredTag.value !== "") {
        inputSearchIgnoredTag.value = selectData
        selectIdTag = element.value
        resultSearchIgnoredTag.style.display = "none"
        resultSearchIgnoredTag.innerHTML = ""
    }
}

function renderSearch(element, inner) {
    let output = '';
    element.forEach(tag => {
        output += `
                     <li onclick="select(this)" value="${tag.id}" class="${tag.name}" style="padding: 3px">${tag.name}</li>`;
    });
    inner.innerHTML = output;
}

///////////////////////////////////////////
// Запросы для вывода меток в список поиска

function getLatterTracked() {
    let param = document.getElementById('inputSearchTrackedTag').value
    fetch(`/api/user/tag/latter?searchString=${param}`, {
        method: 'GET',
        headers: header
    }).then(res => res.json()).then(data => {
        renderSearch(data, resultSearchTrackedTag)
        let clearInputIgn = document.getElementById('inputSearchIgnoredTag').value = "";
    })
}

function getLatterIgnored() {
    let param = document.getElementById('inputSearchIgnoredTag').value
    fetch(`/api/user/tag/latter?searchString=${param}`, {
        method: 'GET',
        headers: header
    }).then(res => res.json()).then(data => {
        renderSearch(data, resultSearchIgnoredTag);
        let clearInputTr = document.getElementById('inputSearchTrackedTag').value = "";
    })
}

//////////////////////
// Для задержки вывода

function debounce(call, timeout) {
    return function perform(args) {
        let previousCall = this.lastCall;
        this.lastCall = Date.now();
        if (previousCall && this.lastCall - previousCall <= timeout) {
            clearTimeout(this.lastCallTimer);
        }
        this.lastCallTimer = setTimeout(() => call(args), timeout);
    }
}

/////////////////////////
// Метод для вывода меток

function renderTags(element, inner) {
    let output = '';
    element.forEach(tag => {
        output += `
                    <div><a href="#" style="  background-color:lightblue; font-size: .8rem;
                    color: darkblue; padding: .3rem .5rem; border-radius: .2rem;
                    text-decoration: none" >${tag.name}</a></div>`;
    });
    inner.innerHTML = output;
}

//////////////////////////////////
// Запросы на получения всех меток

function getTrackedTags() {
    return fetch(trackUrl, {
        method: 'GET',
        headers: header
    })
        .then(res => res.json())
        .then(data => renderTags(data, trackedTags))
}

function getIgnoredTags() {
    return fetch(ignoreUrl, {
        method: 'GET',
        headers: header
    })
        .then(res => res.json())
        .then(data => renderTags(data, ignoredTags))
}

///////////////////////////////////////
// Для вывода поиска по клику на кнопку

function showAdd(id) {
    let div = document.getElementById(id);
    if (div.style.display === '') {
        div.style.display = 'none';
    } else
        div.style.display = '';
    clearInput()
    resultsWrapper.style.display = "none";
    resultsWrapper.innerHTML = "";
    resultSearchIgnoredTag.style.display = "none";
    resultSearchIgnoredTag.innerHTML = "";
}

/////////////////////
// Очистка поля ввода

function clearInput() {
    let clearInputTr = document.getElementById('inputSearchTrackedTag').value = "";
    let clearInputIgn = document.getElementById('inputSearchIgnoredTag').value = "";
}

//////////////////////////////
// Запросы на добавление меток

function addTrackedTag() {
    if (selectIdTag !== 0) {
        fetch(`/api/user/tag/${selectIdTag}/tracked`, {
            method: 'POST',
            headers: header
        })
            .then(res => res.json())
            .then(data => getTrackedTags(data));
        selectIdTag = 0;
        clearInput();
    } else {
        console.log("Отслеживаемая метка не выбрана")
    }
}

function addIgnoredTag() {
    if (selectIdTag !== 0) {
        fetch(`/api/user/tag/${selectIdTag}/ignored`, {
            method: 'POST',
            headers: header
        })
            .then(res => res.json())
            .then(data => getIgnoredTags(data));
        selectIdTag = 0;
        clearInput();
    } else {
        console.log("Игнорируемая метка не выбрана")
    }
}

/////////////////////////////////////////////////
// Подгрузка скриптов на заполнение карт с метками

window.onload = () => {
    getTrackedTags();
    getIgnoredTags();
}





