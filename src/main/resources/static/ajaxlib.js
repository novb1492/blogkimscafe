
    function doajax(url,data){
        var xhr = new XMLHttpRequest(); //new로 생성
        xhr.open('POST', url, true); //j쿼리 $ajax.({type,url},true가 비동기)
        xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");////이게없으면 psot전송불가 조금 찾았네
        xhr.send(data); /// ajax data부분
        return xhr;
    }
