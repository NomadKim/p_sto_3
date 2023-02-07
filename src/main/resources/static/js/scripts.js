window.addEventListener('load', () => {
    setGlobalStyles();
    const appHeader = document.getElementById("app-header");
    const appSidebar = document.getElementById("app-side-bar");
    const appFooter = document.getElementById("app-footer");
    const headerProfile = document.getElementById("profile-header");
    if (appHeader != null) {
        appHeader.innerHTML = `${Header.createHeader()}`;

        document.querySelector("#logout").addEventListener("click", logout);
    }
    if (headerProfile != null) {
        headerProfile.innerHTML = `${Header.createHeaderProfile()}`;

    }
    appSidebar.innerHTML = `${SideBar.createSideBar()}`;
    appFooter.innerHTML = `${Footer.createFooter()}`;

    SideBar.activeStyle();
    console.log("Hello from KataAacademy!");
})
function setGlobalStyles() {
    const body = document.body;
    body.style.margin = "0";
    body.style.padding = "0";
    body.style.boxSizing = "border-box";
}


