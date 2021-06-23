window.onload=function(){
    document.querySelectorAll('.image').forEach(function(item) {
        item.addEventListener('c', function() {
            var id = item.id;
            document.getElementsByClassName(id+'comment')[0].disabled=false;
            document.getElementById(id+'update').disabled=false;
        });
    });
}
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
 function doUpdateArticle() {
    var alreadyimagesarray=[]; 
    var alreayimages= document.getElementsByTagName('img');
    for(var i=0;i<alreayimages.length;i++){
        alreadyimagesarray.push(alreayimages[i].id);
    }
    var form= document.getElementById('form');
    var formData = new FormData(form);
    formData.append('content',document.getElementById('contentEditable').textContent);
    if(alreayimages!=undefined){
        formData.append('alreadyimages',alreadyimagesarray);
    }
    var xhr = new XMLHttpRequest();
    xhr.open("POST" , "/updatearticle" , true);
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