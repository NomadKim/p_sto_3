const questionId = document.location.href.split("/").slice(-1)[0].replace(/\D/g,'');
const sendAnswer = document.querySelector('#handleQuestion');

$(document).ready(async function () {

    await questionFilling();
    await answersFilling();

})

function questionFilling(){

    fetch(`/api/user/question/` + questionId,
        {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + getCookie('token'),
                'Accept': 'application/json', 'Content-Type': 'application/json'
            },
        })
        .then(response => {
            if (response.status !== 200) {
                document.querySelector('#question-title').textContent =
                    "Ошибка запроса";
                document.querySelector('#question-body p').textContent =
                    "Измените номер вопроса или обновите страницу через 2 минуты.";
            } else {
                response.json().then(data => {
                    document.querySelector('#question-title').textContent = 'asd';
                    document.querySelector('#question-body p').textContent = data.description;
                    document.querySelector('#question-persist-date').textContent =
                        "Дата создания: " + getDateFromDateTime(data.persistDateTime);
                    document.querySelector('#question-update-date').textContent =
                        "Последнее изменение: " + getDateFromDateTime(data.lastUpdateDateTime);
                    document.querySelector('#question-view-count').textContent =
                        "Просмотров: " + data.viewCount;
                    document.querySelector('#countVote').innerHTML = votesNumber(data.countValuable);
                    const commentContainer = document.querySelector('#questionComments');
                    data.listCommentDto.forEach((comment) => {
                        commentContainer.appendChild( createQuestionComment(comment))
                    })
                    let text = "";
                    data.listTagDto.forEach(tag => {
                        text += '<li className = "text-start list-inline-item">' +
                                    '<a href = "#" style = "font-size: .7rem; background-color:lightblue; '+
                                        'color: blue; padding: .2rem .5rem; border-radius: .2rem; text-decoration: none" >' +
                                    tag.name +
                                    '</a>'+
                                '</li>';
                    })

                    document.querySelector('#tag-list').innerHTML = text;
                    document.querySelector('#users-info').innerHTML =
'                                    <div class="row">\n' +
'                                        <div class="col">\n' +
'                                            <div style="display: inline;">\n' +
'                                                <div><a href="#"><small class="text-secondary">Править</small></a></div>\n' +
'                                            </div>\n' +
'                                        </div>\n' +
'                                        <div class="col">\n' +
'                                            <div><small>отредактирован: ' + timeDifference(data.lastUpdateDateTime) + '</small></div>\n' +
'                                            <div></div>\n' +
'                                            <ul class="list-unstyled">\n' +
'                                                <li><i class="fa fa-star"></i><small>' + data.authorName + '</small></li>\n' +
'                                            </ul>\n' +
'                                        </div>\n' +
'                                        <div class="col" style="background-color:lightblue; padding: .2rem .5rem; border-radius: .2rem; text-decoration: none">\n' +
'                                            <div><small>задан: ' + timeDifference(data.persistDateTime) + '</small></div>\n' +
'                                            <div></div>\n' +
'                                            <ul class="list-unstyled">\n' +
'                                                <li><i class="fa fa-star"></i><small>' + data.authorName + '</small></li>\n' +
'                                            </ul>\n' +
'                                        </div>\n' +
'                                    </div>\n' +
'                                </div>'
                });
            }
        });
}

function answersFilling() {
    document.querySelector('#answer-row').innerHTML = "";

    fetch(`/api/user/question/` + questionId + `/answer`,
        {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + getCookie('token'),
                'Accept': 'application/json', 'Content-Type': 'application/json'
            },
        })
        .then(response => {
            if (response.status !== 200) {
                document.querySelector('#count-answers').textContent =
                    "Ответы пока отсутствуют"
            } else {
                response.json().then(data => {
                    let text = "";
                    if (data.length === 0) {
                        document.querySelector('#count-answers').textContent =
                            "Ответы пока отсутствуют"
                    } else {
                        document.querySelector('#count-answers').textContent =
                            data.length + declOfNum(data.length, [" ответ", " ответа", " ответов"]);
                        let mainBlock = document.querySelector('#answer-row');

                        data.forEach(function (item) {
                            let block = document.createElement("div");
                            block.innerHTML = getAnswerHTMLForQuestion(item);
                            mainBlock.appendChild(block);
                            fillCommentsToAnswer("#anwserComments-" + item.id);
                        })
                    }
                })
            }
        })
}

function getAnswerHTMLForQuestion(item) {
    let answerCommentsId = "anwserComments-" + item.id;
    let buttonCommentsId = "buttonAnswerComments-" + item.id;

    return '                <div class="flex-row">\n' +
        '                    <div class="col">\n' +
        '                        <div class="row">\n' +
        '                            <div class="col-1 text-center" id="voteplace-1">\n' +
        '                                <ul class="list-unstyled">\n' +
        '                                    <li>' +
        '                                       <a id="voteUp-1" onclick="voteOnAnswer(UPANSWER,' + item.id + ')">' +
        '                                           <i class="fas fa-arrow-circle-up" style="color: rgb(121,135,155);"></i>' +
        '                                       </a>' +
        '                                    </li>\n' +
        '                                    <li id="countVote-' + item.id + '">' + votesNumber(item.countValuable) + '</li>\n' +
        '                                    <li id="voteDown-1">' +
        '                                       <a onclick="voteOnAnswer(DOWNANSWER, ' + item.id + ')">' +
        '                                           <i class="fa fa-arrow-circle-down" style="color: rgb(121,135,155);"></i>' +
        '                                       </a>' +
        '                                    </li>\n' +
        '                                    <li>' +
        '                                       <a href="#">' +
        '                                           <i class="fa fa-rotate-right" style="color: rgb(121,135,155);"></i>' +
        '                                       </a>' +
        '                                    </li>\n' +
        '                                </ul>\n' +
        '                            </div>\n' +
        '                            <div class="col" id="questionplace-1">\n' +
        '                                <div id="answer-body">\n' +
        '                                    <p>' + item.body + '<br></p>\n' +
        '                                </div>\n' +
        '                                <div id="users-info-1">\n' +
        '                                    <div class="row">\n' +
        '                                        <div class="col">\n' +
        '                                            <div style="display: inline;">\n' +
        '                                                <div><a href="#"><small class="text-secondary">Править</small></a></div>\n' +
        '                                            </div>\n' +
        '                                        </div>\n' +
        '                                        <div class="col"></div>\n' +
        '                                        <div class="col" style="background-color:lightblue; padding: .2rem .5rem; border-radius: .2rem; text-decoration: none">\n' +
        '                                            <div><small>Ответ: ' + timeDifference(item.persistDate) + '</small></div>\n' +
        '                                            <div></div>\n' +
        '                                            <ul class="list-unstyled">\n' +
        '                                                <li><i class="fa fa-star"></i><small>' + item.nickName + '</small></li>\n' +
        '                                            </ul>\n' +
        '                                        </div>\n' +
        '                                    </div>\n' +
        '                                </div>\n' +
                                                //комментарии для js
        '                                    <div className="post-layout--right js-post-comments-component" id="question-comments">'+
        '                                        <div>'+
        '                                            <small>'+
        '                                                <ul className="comments-list js-comments-list" id="' + answerCommentsId + '">'+
        '                                                </ul>'+
        '                                            </small>'+
        '                                        </div>'+
        '                                    </div>'+
        '                                <div id="' + buttonCommentsId + '" data-rep="50" data-reg="true">'+
        '                                   <button type="button" className="btn btn-link btn-sm"'+
        '                                            onClick="openCommentArea(`#' + buttonCommentsId + '`, false)">Добавить комментарий'+
        '                                    </button>'+
        '                                </div>'+
        '                            </div>' +
        '                        </div>' +
        '                    </div>' +
        '                    <hr>' +
        '                </div>';
}

function getDateFromDateTime(dateTime) {
    return dateTime.substr(0, 10)
}

function timeDifference(previous) {

    let msPerMinute = 60 * 1000;
    let msPerHour = msPerMinute * 60;
    let msPerDay = msPerHour * 24;
    let msPerMonth = msPerDay * 30;
    let msPerYear = msPerDay * 365;

    previous = new Date(previous);
    let current = new Date();
    let elapsed = current.getTime() - previous.getTime();

    if (elapsed < msPerMinute) {
        return Math.round(elapsed / 1000) + ' seconds ago';
    }
    if (elapsed < msPerHour) {
        return Math.round(elapsed / msPerMinute) + ' minutes ago';
    }
    if (elapsed < msPerDay) {
        return Math.round(elapsed / msPerHour) + ' hours ago';
    }
    if (elapsed < msPerMonth) {
        return Math.round(elapsed / msPerDay) + ' days ago';
    }
    if (elapsed < msPerYear) {
        return Math.round(elapsed / msPerMonth) + ' months ago';
    } else {
        return Math.round(elapsed / msPerYear) + ' years ago';
    }
}

function declOfNum(n, text_forms) {
    n = Math.abs(n) % 100;
    let n1 = n % 10;
    if (n > 10 && n < 20) {
        return text_forms[2];
    }
    if (n1 > 1 && n1 < 5) {
        return text_forms[1];
    }
    if (n1 === 1) {
        return text_forms[0];
    }
    return text_forms[2];
}

function votesNumber(data) {
    return !data ? 0 : data;
}

function createQuestionComment(data) {
    let li = document.createElement("li");
    li.appendChild(document.createTextNode(`${data.comment} - ${data.fullName} ${new Date(data.dateAdded).toLocaleString()}`));
    li.setAttribute("id", "comment-" + data.id);
    return li;
}

function fillCommentsToAnswer(answerId){
    //Дополнить после появления API для вывода комментариев
    // Пример реализации в addCommentToQuestionPOST
    // console.log(answerId);
    // let ul = document.querySelector(answerId);
    // let li = document.createElement("li");
    // li.appendChild(document.createTextNode(answerId));
    // ul.appendChild(li);
}

function openCommentArea(commentAreaId, isQuestion){
    if (isQuestion) {
        document.querySelector(commentAreaId).innerHTML =
            '<div>' +
            '   <br>' +
            '   <textarea className="editor" id="comment-to-question-body" rows="2" required style="width: 100%"></textarea>' +
            '</div>' +
            '<div id="add-comment-to-question" data-rep="50" data-reg="true">' +
            '   <button type="button" class="btn btn-primary btn-sm" onclick="addCommentToQuestionPOST()">Добавить комментарий</button>' +
            '</div>'
    } else {
        let answerId = commentAreaId.split("-").slice(-1)[0];
        document.querySelector(commentAreaId).innerHTML =
            '<div>' +
            '   <br>' +
            '   <textarea className="editor" id="comment-to-answer-body-' + answerId + '" rows="2" required style="width: 100%"></textarea>' +
            '</div>' +
            '<div id="add-comment-to-question" data-rep="50" data-reg="true">' +
            '   <button type="button" class="btn btn-primary btn-sm" onclick="addCommentToAnswerPOST(' + answerId + ')">Добавить комментарий</button>' +
            '</div>'
    }
}

function addCommentToQuestionPOST() {
    let bodytext = document.querySelector('#comment-to-question-body').value;
    fetch('/api/user/question/' + questionId + '/comment',
        {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + getCookie('token'),
                'Accept': 'application/json', 'Content-Type': 'application/json'
            },
            body: bodytext
        }).then(response => {
            if (response.status != 200) {
                if (response.status === 400) {
                    alert("Пустой комментарий")
                } else if (response.status === 404) {
                    alert("Вопрос не найден в системе")
                }
            } else {
                response.json().then(data => {
                    let ul = document.querySelector("#questionComments");
                    ul.appendChild(createQuestionComment(data));
                })
            }
        })
    document.querySelector("#add-comment-to-question").innerHTML =
        '<button type="button" class="btn btn-link btn-sm" ' +
        'onclick="openCommentArea(`#add-comment-to-question`, true)">' +
        'Добавить комментарий' +
        '</button>'
}

function addCommentToAnswerPOST(answerNumber) {
    let textBodyId = "#comment-to-answer-body-" + answerNumber;
    let bodytext = document.querySelector(textBodyId).value;
    fetch('/api/user/question/' + questionId + '/answer/' + answerNumber + '/comment',
        {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + getCookie('token'),
                'Accept': 'application/json', 'Content-Type': 'application/json'
            },
            body: JSON.stringify(bodytext)
        }).then(response => {
        if (response.status != 200) {
            if (response.status === 400) {
                alert("Пустой комментарий")
            } else if (response.status === 404) {
                alert("Вопрос или ответ не найдены в системе")
            }
        } else {
            response.json().then(data => {
                let commentsId = "#anwserComments-" + answerNumber;
                let ul = document.querySelector(commentsId);
                let li = document.createElement("li");
                li.appendChild(document.createTextNode(data.comment));
                li.setAttribute("id", "comment-" + data.id);
                ul.appendChild(li);
            })
        }
    })
    let answerId = "#buttonAnswerComments-" + answerNumber;
    document.querySelector(answerId).innerHTML =
        '<button type="button" class="btn btn-link btn-sm" ' +
        'onclick="openCommentArea(`' + answerId + '`, false, )">' +
        'Добавить комментарий' +
        '</button>'
}
