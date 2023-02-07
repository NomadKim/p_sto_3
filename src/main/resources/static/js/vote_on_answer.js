const UPANSWER = "/upVote";
const DOWNANSWER = "/downVote";

async function voteOnAnswer(upOrDown, answerId){
    let url = "/api/user" + window.location.pathname + "/answer/" + answerId + upOrDown;
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
        .then((data) => {
            if (data != undefined) {
                let tagId = "#countVote-" + answerId;
                $(tagId).text(data);
            }
        });
}