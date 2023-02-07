const UPQUESTION = "/upVote";
const DOWNQUESTION = "/downVote";

async function voteOnQuestion(upOrDown){
    let url = "/api/user" + window.location.pathname + upOrDown;
    return await fetch(url, {
            method: "POST",
            headers: {
                'Authorization': 'Bearer ' + getCookie('token')}
        })
    .then((responce) => {
        if(responce.status === 200){
            return responce.text();
        } else {
            responce.text()
                .then(data=> alert(data));
        }
    })
        .then((data)=>{
            if(data != undefined){
                $("#countVote").text(data);
            }
        });
}
