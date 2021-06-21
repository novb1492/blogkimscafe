function doInsertArticle(){
    var form= document.getElementById('form');
    var formData = new FormData(form);
    var xhr = new XMLHttpRequest();
    xhr.open("POST" , "/insertarticle" , true);
    xhr.send(formData);
    xhr.onload = function() { 
        if(xhr.status==200){ // success:function(data)부분 통신 성공시 200반환
            if(xhr.response=='true'){
                alert("작성완료");
                location.href="/auth/boardlist";
            }else{
                alert("등록에 실패했습니다");
            }
        }
    }  
 }