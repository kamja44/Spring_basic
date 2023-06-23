// 삭제 기능
const deleteBtn = document.querySelector("#delete-btn");
/*
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
*/
if(deleteBtn){
    deleteBtn.addEventListener("click", event => {
        let id = document.querySelector("#article-id").value;
        function success(){
            alert("삭제 완료");
            location.replace("/articles");
        }
        function fail(){
            alert("삭제 실패");
            location.replace("/articles");
        }
        httpRequest("DELETE", "/api/articles/" + id, null, success, fail);
    });
}

// 수정 기능
const modifyBtn = document.querySelector("#modify-btn");
/*
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
*/
if(modifyBtn){
    modifyBtn.addEventListener("click", event => {
        let params = new URLSearchParams(location.search);
        let id = params.get("id");
        body = JSON.stringify({
            title : document.querySelector("#title").value,
            content : document.querySelector("#content").value,
        });
        function success(){
            alert("수정 완료");
            location.replace("/articles/"+id);
        }
        function fail(){
            alert("수정 실패");
            location.replace("/articles/"+id);
        }
        httpRequest("PUT", "/api/articles/"+id, body, success, fail);
    });
}
// 등록 기능
const createBtn = document.querySelector("#create-btn");
/*
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
 */
if(createBtn){
    // 등록 버튼을 클릭하면 /api/articles로 요청을 보낸다.
    createBtn.addEventListener("click", event => {
        body = JSON.stringify({
            title : document.querySelector("#title").value,
            content : document.querySelector("#content").value,
        });
        function success(){
            alert("등록 완료");
            location.replace("/articles");
        }
        function fail(){
            alert("등록 실패");
            location.replace("/articles");
        }
        httpRequest("POST", "/api/articles", body, success, fail);
    });
}

// 쿠키를 가져오는 함수
function getCookie(key){
    let result = null;
    let cookie = document.cookie.split(";");
    cookie.some(function(item){
        item = item.replace(" ", "");
        let dic = item.split("=");
        if(key === dic[0]){
            result = dic[1];
            return true;
        }
    });
    return result;
}

// HTTP 요청을 보내는 함수
function httpRequest(method, url, body, success, fail){
    fetch(url, {
        method,
        headers: {
            // 로컬 스토리지에서 액세스 토큰 값을 가져와 헤더에 추가
            Authorization: "Bearer " + localStorage.getItem("access_token"),
            "Content-Type" : "application/json",
        },
        body,
    }).then((response) => {
        if(response.status === 200 || response.status === 201){
            return success();
        }
        const refresh_token = getCookie("refresh_token");
        if(response.status === 401 && refresh_token){
            fetch("/api/token", {
                method: "POST",
                headers : {
                    Authorization : "Bearer " + localStorage.getItem("access_token"),
                    "Content-Type" : "application/json",
                },
                body : JSON.stringify({
                    refresh_token: getCookie("refresh_token"),
                }),
            }).then((res) => {
                if(res.ok){
                    return res.json();
                }
            }).then((result) => {
                // 재발급 성공 시 로컬 스토리지값을 새로운 액세스 토큰으로 교체한다.
                localStorage.setItem("access_token", result.accessToken);
                httpRequest(method, url, body, success, fail);
            }).catch((error) => fail());
        }else{
            return fail();
        }
    });
}