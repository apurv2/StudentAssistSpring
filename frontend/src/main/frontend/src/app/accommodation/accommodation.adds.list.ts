
export class AccommodationAddsList {
    showingDetails: boolean;
    paginating: boolean;
    stopPagination: boolean;

    showList() {
        if (window.innerWidth < 767) {
            this.showingDetails = false;
            document.getElementById("detailCard").style.display = 'none';
            document.getElementById("addsListId").style.display = 'block';
        }
    }


    addClickMobile() {
        if (window.innerWidth < 767) {
            this.showingDetails = true;
            document.getElementById("detailCard").style.display = 'block';
            document.getElementById("addsListId").style.display = 'none';
        }
    }

}