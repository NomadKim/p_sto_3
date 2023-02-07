$(document).ready(async function () {

    await pagination(1, 10, getUnansweredQuestions);
})

function getUnansweredQuestions(page, itemsOnPage) {
    return fetch(`/api/user/question/noAnswer?page=${page}&items=${itemsOnPage}`,
        {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + getCookie('token'),
                'Accept': 'application/json', 'Content-Type': 'application/json'
            },
        });
}

function fillCard(elementItems) {
    $('#unansweredQuestions_list').empty();

    elementItems.forEach(function (item) {
        let newElement =
            $('<div class="row question-card">')
                .attr("style", "height: 9rem; padding-left: 1.5rem; padding-top: .7rem; border-bottom: 1px solid lightgrey")
                .append($('<div class="col-1 votes-list">')
                    .append($('<div class="d-flex flex-column">')
                        .append($('<span class="count_question text-center">')
                            .attr("style", "color: #7b8185").text(item.countValuable))
                        .append($('<span class="label">')
                            .attr("style", "font-size: .7rem").text('голосов'))
                    )
                    .append($('<div class="d-flex flex-column">')
                        .append($('<span class="count_question text-center">')
                            .attr("style", "color: #7b8185").text(item.countAnswer))
                        .append($('<span class="label">')
                            .attr("style", "font-size: .7rem").text('ответов'))
                    )
                    .append($('<div class="d-flex flex-column">')
                        .append($('<span class="count_question text-center">')
                            .attr("style", "color: #7b8185").text(item.viewCount))
                        .append($('<span class="label">')
                            .attr("style", "font-size: .7rem").text('показов'))
                    )
                )
                .append($('<div class="col-auto question-browser">')
                    .append($('<div class="row question-card-body">')
                        .append($('<h3 class="question-card-title">')
                            .attr("style", "font-weight: 400;word-break: break-word !important;overflow-wrap: break-word !important;display: block;font-size: 1rem;line-height: 1.3;margin: 0 0 1em;")
                            .append($('<a class="question-heading">')
                                .attr("style", "text-decoration: none").text(item.title)
                                .attr("href", "/question/" + item.id)))
                        .append($('<div class="row question-content">')
                            .attr("style", "font-size: .8rem; margin-top: 0.1rem")
                            .append($('<p>').text(item.description))))
                    .append($('<div class="row">')
                        .attr("style", "height: 2rem")
                        .append($('<div class="row">')
                            .append($('<div class="col-md-auto">')
                                .append(
                                    $.map(item.listTagDto, function (itemTag) {
                                            return $('<a class="list-item" href="#">')
                                                .attr("style", "margin-left: 0.2rem; font-size: 12px; background-color: lightblue; color: blue; padding: .3rem .5rem;border-width: 1px;border-style: solid; border-radius: .2rem; text-decoration: none;display: inline-block;white-space: nowrap;text-align: center;line-height: 1;margin-right: 0.5rem")
                                                .text(itemTag.name)
                                        }
                                    )))
                            .append($('<d class="w-100">'))
                            .append($('<div class="col">')
                                .append($('<div class="row">')
                                    .append($('<div class="col-auto user-card--info">')
                                        .attr("style", "flex-direction: row;align-items: center;display: flex;gap: 4px;")
                                        .append($('<a href="#" class="avatar avatar_16 user-card-avatar">')
                                            .attr("style", "display: inline-block; position: relative; width: 16px; height: 16px; border-radius: 3px;font: inherit; background-color: hsl(0,0%,100%); background-repeat: no-repeat; background-size: 100%;align-items: center; vertical-align: bottom; margin-right: 0.2rem")
                                            .append($('<div class="gravatar-wrapper-16">')
                                                .attr("style", "overflow: hidden;align-items: center;border: 0;font: inherit;vertical-align: baseline;")
                                                .append($('<img src="https://www.gravatar.com/avatar/9881fc9963d4fdff1dd454becd37a250?s=32&amp;d=identicon&amp;r=PG&amp;f=1" alt="" width="16" ,="" height="16" class="avatar--image">')
                                                    .attr("style", "width: 16px; height: 16px;border-radius: 3px; display: block;box-sizing: inherit;padding: 0;border: 0;font: inherit;vertical-align: baseline;")
                                                )))
                                        .append($('<div class="user-card--link d-flex gs4">')
                                            .attr("style", "!important;white-space: nowrap;min-width: 0;font-size: 12px;align-items: center;flex-wrap: wrap;overflow-wrap: break-word;")
                                            .append($('<a href="#" class="flex--item">')
                                                .attr("style", "margin: 2px;text-decoration: none; margin-right: 0.4rem")
                                                .text(item.authorName))
                                            .append($('<ul class="user-card--awards">')
                                                .attr("style", "list-style: none;margin: 0;padding: 0;align-items: center;display: flex;gap: 6px;")
                                                .append($('<li class="user-card--rep">')
                                                    .attr("style", "text-align: -webkit-match-parent;")
                                                    .append($('<span class="todo-no-class-here">')
                                                        .attr("style", "direction: ltr;unicode-bidi: isolate;font-size: 100%;font-weight: bold;")
                                                        .attr("title", "reputation score ")
                                                        .attr("dir", "ltr")
                                                        .text(item.authorReputation)
                                                    ))
                                            )))
                                    .append($('<div class="col-auto user-card-time-col">')
                                        .append($('<time class="user-card-time">')
                                            .attr("style", "white-space: nowrap;font-size: 12px;")
                                            .text("asked ")
                                            .append($('<span class="relativetime">')
                                                .attr("title", item.persistDateTime).text(timeDifference(item.persistDateTime))))
                                    ))))))

        $('#unansweredQuestions_list').prepend(newElement);

    });
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