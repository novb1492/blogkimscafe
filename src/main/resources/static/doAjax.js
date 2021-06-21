function doajax(url,data,contentType){
    var xhr = new XMLHttpRequest(); //new로 생성
    xhr.open('POST', url, true); //j쿼리 $ajax.({type,url},true가 비동기)
    xhr.setRequestHeader("Content-Type",contentType);////이게없으면 psot전송불가 조금 찾았네
    xhr.send(data); /// ajax data부분
    return xhr;
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
            if(xhr.response=='true'){
                color='blue',text="";
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
                if(xhr.response=='true'){
                    location.href="/writearticle";
                }else{
                    alert("이메일인증 부탁드립니다");
                }
            }
        }                        
}  
function doSendEamil(){
        var xhr,url='/sendemail',data=null;
        var contentType="application/json";
        xhr=doajax(url,data,contentType);
        xhr.onload = function() { 
            var text;
            if(xhr.status==200){ // success:function(data)부분 통신 성공시 200반환
                if(xhr.response=='true'){
                    text="인증번호를 전송했습니다";
                }else{
                    text="인증번호 전송에 실패했습니다";
                }
                alert(text);
            }
        }  
}
function doConfrimTempnum(){
    var xhr,url='/confrimrandnum',data=JSON.stringify({"randnum":""+document.getElementById('randnum').value+""});//'randnum='+document.getElementById('randnum').value;
    var contentType="application/json";
    xhr=doajax(url,data,contentType);
    xhr.onload = function() { 
        var text;
        if(xhr.status==200){ // success:function(data)부분 통신 성공시 200반환
            if(xhr.response=='true'){
                text="이메일인증완료";
                location.href="/mypage";
            }else{
                text="번호를 다시 확인해주세요 ";
            }
            alert(text);
        }
    }    
}
function doSendEmailNoLoing(){
    var xhr,url='/sendemailnologin',data=JSON.stringify({"email":""+document.getElementById('email').value+""});
    var contentType="application/json";
    xhr=doajax(url,data,contentType);
        xhr.onload = function() { 
            var text;
            if(xhr.status==200){ // success:function(data)부분 통신 성공시 200반환
                if(xhr.response=='true'){
                    text="인증번호를 전송했습니다";
                    document.getElementById('confrimrandnum').disabled=false;
                }else{
                    text="인증번호 전송에 실패했습니다";
                }
                alert(text);
            }
        }
}  
function doSendTempPwdEmail(){
    var confrimrandnum=document.getElementById('confrimrandnum');
    confrimrandnum.disabled=true;
    var xhr,url='/sendtemppwd',data='email='+document.getElementById('email').value+'&randnum='+document.getElementById('randnum').value;
    var contentType="application/x-www-form-urlencoded";
    xhr=doajax(url,data,contentType);
    xhr.onload = function() { 
        if(xhr.status==200){ // success:function(data)부분 통신 성공시 200반환
            if(xhr.response=='true'){
                alert("임시 비밀번호 전송");
                location.href="/auth/loginpage";
            }else{
                confrimrandnum.disabled=false;
                alert("번호를 다시 확인해주세요");
            }
        }
    }
}     
function doUpdatePwd(){
    var xhr,url='/updatepwd';
    var data=JSON.stringify({"pwd":""+document.getElementById('pwd').value+"","npwd":""+document.getElementById('npwd').value+"","npwd2":""+document.getElementById('npwd2').value+""});
    var contentType="application/json";
    xhr=doajax(url,data,contentType);
    xhr.onload = function() { 
        var text;
        if(xhr.status==200){ // success:function(data)부분 통신 성공시 200반환
            if(xhr.response=='true'){
                text="비밀번호변경완료";
                location.href="/mypage";
            }else{
                text="뭔가틀립니다";
            }
            alert(text);
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


    