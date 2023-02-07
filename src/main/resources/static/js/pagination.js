async function pagination(page, itemsOnPage, funcForURL) {
    $('#paginationButtons').empty();

    await funcForURL(page, itemsOnPage).then(response => {
        response.json().then(element => {
            for (let i = 1; i < element.totalPageCount + 1; i++) {
                if (element.currentPageNumber === i) {
                    let newPage = `<button type="button" class="btn btn-outline-primary active" onclick="pagination(${i}, ${itemsOnPage}, ${funcForURL})">${i}</button>`;
                    $('#paginationButtons').append(newPage);
                } else {
                    let newPage = `<button type="button" class="btn btn-outline-primary" onclick="pagination(${i}, ${itemsOnPage}, ${funcForURL})">${i}</button>`;
                    $('#paginationButtons').append(newPage);
                }
            }
            fillCard(element.items);
        });
    });
}

function getAmountPerPage() {
    return document.getElementById('amountPerPage').value;
}

function imposeMinMax(el) {
    if (el.value != "") {
        if (parseInt(el.value) < parseInt(el.min)) {
            el.value = el.min;
        }
        if (parseInt(el.value) > parseInt(el.max)) {
            el.value = el.max;
        }
    }
}