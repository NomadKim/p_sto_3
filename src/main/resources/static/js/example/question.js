const myHeaders = new Headers();
myHeaders.append('Content-Type', 'application/json');
myHeaders.append('Authorization','Bearer '+ document.cookie);


function getAllQuestions(page, itemsOnPage) {

    return fetch(`/api/user/question/sortedQuestions?page=${page}&items=${itemsOnPage}`,
        {method: 'GET',
            headers: myHeaders});
}

async function fillCard(elementItems) {
    $('#questionTable > tbody').empty();
    await elementItems.forEach(function (item) {

        let allTag = '';
        (item.listTagDto).forEach(function (itemTag) {
            allTag += itemTag.name + " ";
        })

        let newElement = `<tr>
                                <td> ${item.id} </td>
                                <td> ${item.title} </td>
                                <td> ${item.authorId} </td>
                                <td> ${item.authorImage} </td>
                                <td> ${item.authorName} </td>
                                <td> ${item.authorReputation}</td>
                                <td> ${item.description}</td>
                                <td> ${item.lastUpdateDateTime}</td>
                                <td> ${item.persistDateTime}</td>
                                <td> ${item.countAnswer}</td>
                                <td> ${item.countValuable}</td>
                                <td> ${item.viewCount}</td>
                                <td> ${allTag}</td>
                            </tr>`;
        $('#questionTable > tbody').append(newElement);
    });
}