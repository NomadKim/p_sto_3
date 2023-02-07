async function requestToquestionId_view(){
    return await fetch("/api/user" + window.location.pathname + "/view", {
            method: "POST",
            headers: {
                'Authorization': 'Bearer ' + token}
        }
    ).then(responce => responce.text());
}