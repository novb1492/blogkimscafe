window.onload=function(){
    var confrimemailcheck=document.getElementById("confrimemailcheck");
    confrimemailcheck.addEventListener('click',function(){
        var xhr,url='/confrimemailcheck',data=null;
        xhr=doajax(url,data);
        xhr.onload = function() { 
            if(xhr.status==200){ // success:function(data)부분 통신 성공시 200반환
                if(xhr.response=='true'){
                    location.href="/wirtearticlepage";
                }else{
                    alert("이메일인증 부탁드립니다");
                }
            }
        }
    })                              
}
