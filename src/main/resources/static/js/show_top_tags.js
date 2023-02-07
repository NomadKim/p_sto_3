const myHeaders = new Headers();
myHeaders.append('Content-Type', 'application/json');
myHeaders.append('Authorization','Bearer '+ getCookie('token'));

const lengthOfTop = 10


function viewTop(topData, topList){
    for(let i = 0; i<topData.items.length; i++){
        topList.innerHTML += '<li className="list-item mb-2" style="line-height: 1.8">'+
                                    '<a href="#" style=" font-size: .7rem; background-color:'+
                                            'lightblue; color: blue; padding: .3rem .5rem; border-radius: .2rem; text-decoration: none">'+
                                            topData.items[i].name+'</a>'+
                                            '<span className="tag-side-count" style="font-size: .8rem"> x' + topData.items[i].questionsCount + '</span>'+
                                    '</li>'
    }
}


function updateTopData(tagsCount){
    fetch('/api/user/tag/popular?page=1&items='+tagsCount,
        {
            method: 'GET',
            headers: myHeaders
        })
        .then((response) => {
            return response.json();
        })
        .then((data) => {
            viewTop(data, document.getElementById("top-tags-list"))
        });
}

updateTopData(lengthOfTop);
document.getElementById("top-tags-title").innerText = 'Топ-' + lengthOfTop + ' меток';







/*<li className="list-item mb-2">
    <a href="#" style=" font-size: .7rem; background-color:
                            lightblue; color: blue; padding: .3rem .5rem; border-radius: .2rem; text-decoration: none">
        Java</a>
    <span className="tag-side-count" style="font-size: .8rem"> x 7</span>
</li>*/

