function doajax(url,data,contentType){
    var xhr = new XMLHttpRequest(); //new로 생성
    xhr.open('POST', url, true); //j쿼리 $ajax.({type,url},true가 비동기)
    xhr.setRequestHeader("Content-Type",contentType);////이게없으면 psot전송불가 조금 찾았네
    xhr.send(data); /// ajax data부분
    return xhr;
}
function doInsertUser(){
    var form= document.getElementById('form');
    var formData = new FormData(form);
    var xhr = new XMLHttpRequest();
    xhr.open("POST" , "/auth/insertuser" , true);
    xhr.send(formData);
    xhr.onload = function() { 
        if(xhr.status==200){ // success:function(data)부분 통신 성공시 200반환
            var result=JSON.parse(xhr.response);
            if(result.result){
                alert(result.messege);
                location.href='/auth/loginpage';
            }else{
                alert(result.messege);
            }
        }else{
            alert("통신에 실패 하였습니다");
        }
    }  
}
function doConfrimEmail(){
    var emailconfrim=document.getElementById("useremail");
    var xhr; 
    var url='/auth/emailconfirm';
    var data=JSON.stringify({"email":""+emailconfrim.value+""});
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
function doUpdateArticle(){
        var  bid=document.getElementById('bid').value;
        var xhr;
        var url='/updatearticle';
        var data="bid="+bid+"&title="+document.getElementById('title').value+"&content="+document.getElementById('content').value;
        var contentType="application/x-www-form-urlencoded";
        xhr=doajax(url,data,contentType);
        xhr.onload = function() { 
            if(xhr.status==200){ // success:function(data)부분 통신 성공시 200반환
                if(xhr.response=='true'){
                    alert('수정성공')
                    location.href="/auth/content?bid="+bid;
                }else{
                    alert("수정에 실패하였습니다");
                }
            }
        }                            
}   
function doCheckEmailauthentication(){
        var xhr,url='/confrimemailcheck',data=null;
        var contentType="application/json";
        xhr=doajax(url,data,contentType);
        xhr.onload = function() { 
            if(xhr.status==200){ // success:function(data)부분 통신 성공시 200반환
                var result=JSON.parse(xhr.response);
                if(result.result){
                    location.href='/writearticle';
                }else{
                    alert(result.messege);
                }   
            }
        }                        
}  
function doSendEamil(){
        var xhr,url='/sendemail',data=null;
        var contentType="application/json";
        xhr=doajax(url,data,contentType);
        xhr.onload = function() { 
            if(xhr.status==200){ // success:function(data)부분 통신 성공시 200반환
            var result=JSON.parse(xhr.response);
                if(result.result){
                    document.getElementById('sendTempNum').disabled=false;
                    alert(result.messege);
                }else{
                    alert(result.messege);
                }  
            }else{
                alert('통신에 실패했습니다');
            }
        }  
}
function doConfrimTempnum(){
    var sendTempNum= document.getElementById('sendTempNum');
    sendTempNum.disabled=true;
    var xhr,url='/confrimrandnum',data=JSON.stringify({"randnum":""+document.getElementById('randnum').value+""});//'randnum='+document.getElementById('randnum').value;
    var contentType="application/json";
    xhr=doajax(url,data,contentType);
    xhr.onload = function() { 
        if(xhr.status==200){ // success:function(data)부분 통신 성공시 200반환
                var result=JSON.parse(xhr.response);
                if(result.result){
                    alert(result.messege);
                    location.href='/mypage';
                }else{
                    sendTempNum.disabled=false;
                    alert(result.messege);
                }  
        }else{
            alert('통신에 실패했습니다');
            sendTempNum.disabled=false;
        }
    }    
}
function doSendEmailNoLoing(){
    
    var xhr,url='/sendemailnologin',data=JSON.stringify({"email":""+document.getElementById('email').value+""});
    var contentType="application/json";
    var sendTempNum= document.getElementById('sendTempPwd');
    xhr=doajax(url,data,contentType);
        xhr.onload = function() { 
            if(xhr.status==200){ // success:function(data)부분 통신 성공시 200반환
            var result=JSON.parse(xhr.response);
                if(result.result){
                    sendTempNum.disabled=false;
                    alert(result.messege);
                }else{
                    alert(result.messege);
                }  
        }else{
            alert('통신에 실패했습니다');
        }
        }
}  
function doSendTempPwdEmail(){
    var sendTempNum=document.getElementById('sendTempPwd');
    sendTempNum.disabled=true;
    var xhr,url='/sendtemppwd',data='email='+document.getElementById('email').value+'&randnum='+document.getElementById('randnum').value;
    var contentType="application/x-www-form-urlencoded";
    xhr=doajax(url,data,contentType);
    xhr.onload = function() { 
        if(xhr.status==200){ // success:function(data)부분 통신 성공시 200반환
            var result=JSON.parse(xhr.response);
                if(result.result){
                    alert(result.messege);
                    location.href='/auth/loginpage';
                }else{
                    sendTempNum.disabled=false;
                    alert(result.messege);
                }  
        }else{
            sendTempNum.disabled=false;
            alert('통신에 실패했습니다');
        }
    }
}     
function doUpdatePwd(){
    var xhr,url='/updatepwd';
    var data=JSON.stringify({"pwd":""+document.getElementById('pwd').value+"","npwd":""+document.getElementById('npwd').value+"","npwd2":""+document.getElementById('npwd2').value+""});
    var contentType="application/json";
    xhr=doajax(url,data,contentType);
    xhr.onload = function() { 
        if(xhr.status==200){ // success:function(data)부분 통신 성공시 200반환
            var result=JSON.parse(xhr.response);
            if(result.result){
                alert(result.messege);
                location.href='/mypage';
            }else{
                alert(result.messege);
            }
            
        }else{
            alert('통신에 실패했습니다');
        }
    }   
}
function doInsertComment(){
    var bid=document.getElementById('bid').value;
    var xhr;
    var url='/insertcomment'; 
    var data=JSON.stringify({"comment":""+document.getElementById('comment').value+"","bid":""+bid+""});
    var contentType="application/json";
    xhr=doajax(url,data,contentType);
    xhr.onload = function() { 
        if(xhr.status==200){ // success:function(data)부분 통신 성공시 200반환
            if(xhr.response=='true'){
                alert("댓글을 등록 했습니다");
                location.href="/auth/content?bid="+bid;
            }else{
                alert("댓글 등록에 실패했습니다");
            }
        }
    }   
}
function doDeleteComment(cid){
    var xhr;
    var bid=document.getElementById('bid').value;
    var url='/deletecomment'; 
    var data='cid='+cid;
    var contentType="application/x-www-form-urlencoded";
    xhr=doajax(url,data,contentType);
    xhr.onload = function() { 
        if(xhr.status==200){ // success:function(data)부분 통신 성공시 200반환
            if(xhr.response=='true'){
                alert("댓글을 삭제 했습니다");
                location.href="/auth/content?bid="+bid;
            }else{
                alert("삭제에 실패했습니다");
            }
        }
    }   
}
function doUpdateComment(cid){
    var xhr;
    var bid=document.getElementById('bid').value;
    var url='/updatecomment'; 
    var data=JSON.stringify({"cid":""+cid+"","comment":""+document.getElementById(cid+'comment').value+""});
    var contentType="application/json";
    xhr=doajax(url,data,contentType);
    xhr.onload = function() { 
        if(xhr.status==200){ // success:function(data)부분 통신 성공시 200반환
            if(xhr.response=='true'){
                alert("댓글을 수정 했습니다");
                location.href="/auth/content?bid="+bid;
            }else{
                alert("수정에 실패했습니다");
            }
        }
    }   
}
var click=true;
var beforeClickUpdateID;
function clickUpdateButton(cid){
   
    if(click){
        disabedTrue(beforeClickUpdateID,'update');
        disabedTrue(beforeClickUpdateID,'comment');
        disabedFalse(cid,'comment');
        disabedFalse(cid,'update');
        beforeClickUpdateID=cid;
        click=false;
    }else{
        disabedTrue(beforeClickUpdateID,'update');
        disabedTrue(beforeClickUpdateID,'comment');
        disabedFalse(cid,'comment');
        disabedFalse(cid,'update')
        beforeClickUpdateID=cid;
        click=true;
    }
}
function disabedFalse(cid,name){
    document.getElementById(cid+name).disabled=false;
}
function disabedTrue(beforeClickUpdateID,name){
    if(beforeClickUpdateID!=null){
        document.getElementById(beforeClickUpdateID+name).disabled=true;  
    }
}


    
