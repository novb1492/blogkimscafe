window.onload=function(){
    var insertArticleButton=document.getElementById("insertArticleButton");
    insertArticleButton.addEventListener('click',function(){
        var xhr,url='/insertarticle',data='title='+document.getElementById('title').value+'&content='+document.getElementById('content').value;
        xhr=doajax(url,data);
        xhr.onload = function() { 
            if(xhr.status==200){ // success:function(data)부분 통신 성공시 200반환
                if(xhr.response=='true'){
                    alert('등록성공')
                    location.href="/auth/boardlist";
                }else{
                    alert("등록에 실패하였습니다");
                }
            }
        }
    })                              
}
