const myHeaders = new Headers();
myHeaders.append('Content-Type', 'application/json');
myHeaders.append('Authorization','Bearer '+ getCookie('token'));


function getUsersSortedByReputation(page, itemsOnPage) {
    const filter = document.getElementById('inputFilterUser').value
    return fetch(`/api/user/reputation?page=${page}&items=${itemsOnPage}&filter=${filter}`,
        {method: 'GET',
            headers: myHeaders});
}

function getUsersSortedByNew(page, itemsOnPage) {
    const filter = document.getElementById('inputFilterUser').value
    return fetch(`/api/user/new?page=${page}&items=${itemsOnPage}&filter=${filter}`,
        {method: 'GET',
            headers: myHeaders});
}

function getUsersSortedByVoteSum(page, itemsOnPage) {
    const filter = document.getElementById('inputFilterUser').value
    return fetch(`/api/user/vote?page=${page}&items=${itemsOnPage}&filter=${filter}`,
        {method: 'GET',
            headers: myHeaders});
}

function fillCard(elementItems) {
    $('#usersCardDeck').empty();

    elementItems.forEach(function (item) {
        let newElement = `<div class="col mb-4">
                            <div class="user-card">
                              <div><img src="../images/userPage/noUserAvatar.png" class="avatar card-img-top" alt="..."></div>
                              <div class="card-body">
                                <div class="card-title"><a href="#">${item.fullName}</a></div>
                                <div class="card-text small">${item.city}</div>
                                <div class="card-text font-weight-bolder small">${item.reputation}</div>
                              </div>
                            </div>
                          </div>`;

        $('#usersCardDeck').append(newElement);
    });
}
