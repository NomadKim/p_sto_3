function clearCookie() {
    document.cookie = 'token=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

function getCookie(cookieName) {
    let cookie = {};
    document.cookie.split(';').forEach(function (el) {
        let [key, value] = el.split('=');
        cookie[key.trim()] = value;
    })
    return cookie[cookieName] !== undefined
        ? cookie[cookieName]
        : '';
}



