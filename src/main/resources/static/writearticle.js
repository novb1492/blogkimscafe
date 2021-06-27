function doInsertArticle(){
    var form= document.getElementById('form');
    var formData = new FormData(form);
    var xhr = new XMLHttpRequest();
    xhr.open("POST" , "/insertarticle" , true);
    xhr.send(formData);
    xhr.onload = function() { 
        if(xhr.status==200){ // success:function(data)부분 통신 성공시 200반환
            var result=JSON.parse(xhr.response);
            if(result.result){
                alert(result.messege);
                location.href='/auth/boardlist';
            }else{
                alert(result.messege);
            }
            
        }else{
            alert('통신에 실패했습니다');
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
            var result=JSON.parse(xhr.response);
            if(result.result){
                alert(result.messege);
                location.href='/auth/content?bid='+document.getElementById("bid").value;
            }else{
                alert(result.messege);
            }
            
        }else{
            alert('통신에 실패했습니다');
        }
    }  
 }