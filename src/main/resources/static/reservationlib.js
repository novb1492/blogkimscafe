function doajax(url,data,contentType){
    var xhr = new XMLHttpRequest(); //new로 생성
    xhr.open('POST', url, true); //j쿼리 $ajax.({type,url},true가 비동기)
    xhr.setRequestHeader("Content-Type",contentType);////이게없으면 psot전송불가 조금 찾았네
    xhr.send(data); /// ajax data부분
    return xhr;
}
function choiceseat(value){
    seat=value;
    var xhr; 
    var url='/getreservation';
    var data=JSON.stringify({"seat":""+seat+""});
    var contentType="application/json";
    xhr=doajax(url,data,contentType);
    xhr.onload = function() { 
    var color,text;
        if(xhr.status==200){ // success:function(data)부분 통신 성공시 200반환
            if(xhr.response=='false'){
                color='blue',text='';
            }else{
                color='red',text='중복된 아이디 입니다';
            }
            document.getElementById("emailcheck").innerHTML=text;
            emailconfrim.style.backgroundColor=color; 
        }
    }  
}