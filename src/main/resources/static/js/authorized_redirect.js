$(document).ready(function () {
    authorizedRedirect();
})

function authorizedRedirect() {

    if (document.cookie.split(';').filter((item) => item.trim().startsWith('token=')).length) {
        window.location.href = '/main';
    }
}