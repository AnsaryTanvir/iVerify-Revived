document.addEventListener('DOMContentLoaded', (event) => {

    const users = document.querySelectorAll(".user");

    const search = document.querySelector(".search");
    const searchBtn = document.querySelector(".search-btn");

    searchBtn.addEventListener("click", (e)=>{
        if(search.value){
            find(search.value);
        }
    })

    function find(value){
        //console.log(value);
        users.forEach(user => {
            //console.log(user.textContent);
            const content = user.textContent.toLowerCase();

            if(content.includes(value.toLowerCase())){
                user.classList.remove('hide');
            }else{
                user.classList.add('hide');
            }

        })

        clear();
    }
    
    function clear(){
        search.value = '';
    }

})


