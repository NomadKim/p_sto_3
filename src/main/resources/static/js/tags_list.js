const header = new Headers();
header.append('Content-Type', 'application/json');
header.append('Authorization', 'Bearer ' + getCookie('token'));
let listOfTags = document.querySelector('#tagList');

const butCurr = document.getElementById(`currentTags`)
const butNeww = document.getElementById(`newTags`)
const butPop = document.getElementById(`popularTags`)


butNeww.addEventListener (`click`, () => {
    butNeww.className = `btn btn-outline-secondary active`
    butCurr.className = `btn btn-outline-secondary`
    butPop.className = `btn btn-outline-secondary`
})

butCurr.addEventListener (`click`, () => {
    butCurr.className = `btn btn-outline-secondary active`
    butNeww.className = `btn btn-outline-secondary`
    butPop.className = `btn btn-outline-secondary`
})

butPop.addEventListener (`click`, () => {
    butPop.className = `btn btn-outline-secondary active`
    butNeww.className = `btn btn-outline-secondary`
    butCurr.className = `btn btn-outline-secondary`
})



$(document).ready(async function () {
    await pagination(1, 50, getAllTags);
})



let fillCard = (element) => {
    let output = '';
    element.forEach(tag => {
        output += `<div class="col-sm-3">
                   <div class="card">
                        <div class="card-body">
                           <div><a href="#" class="btn btn-outline-secondary" style="background-color: gainsboro">${tag.name}</a></div>
                          <p class="card-text">${tag.description}</p>
                       </div>
                    </div>
                </div>`;
    });
    listOfTags.innerHTML = output;
}

function getAllTags(page, itemNumber) {
    return fetch(`/api/user/tag/name?page=${page}&items=${itemNumber}`, {
        method: 'GET',
        headers: header
    })
}

function getNewTags(page, itemNumber) {
    return fetch(`/api/user/tag/date?page=${page}&items=${itemNumber}`, {
        method: 'GET',
        headers: header
    })
}

function getPopularTags(page, itemNumber) {
    return fetch(`/api/user/tag/popular?page=${page}&items=${itemNumber}`, {
        method: 'GET',
        headers: header
    })
}
