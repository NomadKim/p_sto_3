class Footer{
    static footer = ` <div class="row footer" style="background-color: #333; padding:2rem;">
        <div class="row">
            <div class="col d-flex align-content-center justify-around">
                <div class="container d-flex">
                    <a class="nav-link" href="mailto:info@kata.academy" style="color: white; font-size: 1.2rem;">
                        <img src="../../images/header-images/icon/icon_svg/phone.svg" alt="phone" style="height:
                     1.5rem; margin-right: .5rem">
                        info@kata.academy
                    </a>
                    <a class="nav-link" href="tel:8 800 350-32-96" style="color: white; font-size: 1.2rem;">
                        <img src="../../images/header-images/icon/icon_svg/envelope.svg" alt="phone"
                             style="height: 1.5rem; margin-right: .5rem;">
                        8 800 350 32 96
                    </a>
                </div>
                <div class="container d-flex align-content-center" style="justify-content: end">
                    <ul class="nav">
                        <li class="nav-item">
                            <a href="https://facebook.com/kataacademy" class="nav-link" target="_blank">
                                <img src="../../images/header-images/icon/icon_svg/facebook.svg" alt="facebook"
                                     style="height: 1.5rem;">
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="https://vk.com/kataacademy" class="nav-link" target="_blank">
                                <img src="../../images/header-images/icon/icon_svg/vk.svg" alt="vk" style="height:
                            1.5rem;">
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="https://www.instagram.com/kata.academy/" class="nav-link" target="_blank">
                                <img src="../../images/header-images/icon/icon_svg/instagram.svg" alt="instagram"
                                     style="height: 1.5rem;">
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="https://www.youtube.com/channel/UCsmPQu0jnkNugG6WXmxC6kw/featured" class="nav-link"
                               target="_blank"
                            >
                                <img src="../../images/header-images/icon/icon_svg/youtube.svg" alt="youtube"
                                     style="height: 1.5rem;">
                            </a></li>
                        <li class="nav-item">
                            <a href="https://t.me/kata_academy" class="nav-link" target="_blank">
                                <img src="../../images/header-images/icon/icon_svg/telegram.svg" alt="telegram"
                                     style="height: 1.5rem;">
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="https://api.whatsapp.com/send/?phone=79959968596&text=%D0%9F%D1%80%D0%B8%D0%B2%D0%B5%D1%82%21+%D0%9E%D1%82%D0%BF%D1%80%D0%B0%D0%B2%D1%8C%2C+%D0%BF%D0%BE%D0%B6%D0%B0%D0%BB%D1%83%D0%B9%D1%81%D1%82%D0%B0%2C+%D0%BD%D0%B0%D0%BC+%D1%8D%D1%82%D0%BE+%D1%81%D0%BE%D0%BE%D0%B1%D1%89%D0%B5%D0%BD%D0%B8%D0%B5+%D1%81+%D0%BD%D0%BE%D0%BC%D0%B5%D1%80%D0%BE%D0%BC+285728.+%D0%9C%D1%8B+%D0%B7%D0%B0%D1%80%D0%B5%D0%B3%D0%B8%D1%81%D1%82%D1%80%D0%B8%D1%80%D1%83%D0%B5%D0%BC+%D1%82%D0%B2%D0%BE%D0%B5+%D0%BE%D0%B1%D1%80%D0%B0%D1%89%D0%B5%D0%BD%D0%B8%D0%B5%2C+%D0%B8+%D0%B2+%D1%81%D0%BB%D0%B5%D0%B4%D1%83%D1%8E%D1%89%D0%B5%D0%BC+%D1%81%D0%BE%D0%BE%D0%B1%D1%89%D0%B5%D0%BD%D0%B8%D0%B8+%D1%82%D1%8B+%D0%BC%D0%BE%D0%B6%D0%B5%D1%88%D1%8C+%D0%B7%D0%B0%D0%B4%D0%B0%D1%82%D1%8C+%D1%81%D0%B2%D0%BE%D0%B9+%D0%B2%D0%BE%D0%BF%D1%80%D0%BE%D1%81%21+%D0%A2%D0%B5%D0%B1%D0%B5+%D1%81%D0%BA%D0%BE%D1%80%D0%BE+%D0%BE%D1%82%D0%B2%D0%B5%D1%82%D0%B8%D1%82+%D0%BF%D0%B5%D1%80%D0%B2%D1%8B%D0%B9+%D1%81%D0%B2%D0%BE%D0%B1%D0%BE%D0%B4%D0%BD%D1%8B%D0%B9+%D0%BA%D0%BE%D0%BE%D1%80%D0%B4%D0%B8%D0%BD%D0%B0%D1%82%D0%BE%D1%80+%3D%29&app_absent=0"
                               class="nav-link" target="_blank">
                                <img src="../../images/header-images/icon/icon_svg/whatsapp.svg" alt="phone"
                                     style="height: 1.5rem;">
                            </a>
                        </li>
                    </ul>
                </div>

            </div>
        </div>
        <div class="row">
            <div class="col">
                <a href="/" class="nav-link" style="color: #9ba1a5; margin-top: 1rem; text-align: center">© 2021 KATA Programming
                    Academy</a>

            </div>
        </div>
    </div>`;

   static createFooter(){
        return this.footer;
    }
}