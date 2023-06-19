// 삭제 기능
const deleteBtn = document.querySelector("#delete-btn");

if(deleteBtn){
    deleteBtn.addEventListener("click",(event) => {
        let id = document.querySelector("#article-id").value;
        fetch(`/api/articles/${id}`,{
            method: "DELETE"
        })
            .then(() => {
                alert("삭제 완료");
                location.replace("/articles");
            });
    });
}

// 수정 기능
const modifyBtn = document.querySelector("#modify-btn");
if(modifyBtn){
    modifyBtn.addEventListener("click", (event) => {
        let params = new URLSearchParams(location.search);
        let id = params.get("id");
        fetch(`/api/articles/${id}`, {
            method: "PUT",
            headers: {
                "Content-Type" : "application/json",
            },
            body:JSON.stringify({
                title: document.querySelector("#title").value,
                content: document.querySelector("#content").value
            })
        })
            .then(() => {
                alert("수정 완료");
                location.replace(`/articles/${id}`);
            });
    });
}

// 등록 기능
const createBtn = document.querySelector("#create-btn");

if(createBtn){
    // 클릭 이벤트가 감지되면 생성 API 요청
    createBtn.addEventListener("click", (event) => {
        fetch("/api/articles", {
            method: "POST",
            headers:{
                "Content-Type" : "application/json",
            },
            body: JSON.stringify({
                title: document.querySelector("#title").value,
                content: document.querySelector("#content").value,
            }),
        })
            .then(() => {
                alert("등록 완료");
                location.replace("/articles");
            });
    });
}