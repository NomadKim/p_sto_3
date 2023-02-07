document.getElementById("handleFormSubmit").addEventListener("click", async e => {
    e.preventDefault();

    const response = await fetch("/api/auth/token/", {
        method: "POST",
        headers: {"Accept": "application/json", "Content-Type": "application/json"},
        body: JSON.stringify({
            username: document.getElementById("inputEmail").value,
            password: document.getElementById("inputPassword").value,
            isRemember: document.getElementById("rememberMe").checked
        })
    });

    if (response.ok) {
        let getToken = await response.json();
        document.cookie = "token=;max-age=-1";

        let token = Object.values(getToken);
        document.cookie = "token=" + token;
        window.location.href = '/main';
    }
    if (!response.ok) {
        document.getElementById("errorCode").hidden = false;
    }

});
