window.onload=function(){
    var button=document.getElementById("button")

    button.addEventListener('click',function(){
        document.getElementById('target').onsubmit();
        var xhr = new XMLHttpRequest(); //new로 생성
        xhr.open('POST', url, true); //j쿼리 $ajax.({type,url},true가 비동기)
        xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");////이게없으면 psot전송불가 조금 찾았네
        xhr.send(document.getElementById("text").value); /// ajax data부분
    })
}