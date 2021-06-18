function doInsertArticle(){
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
}
  
function doUpdateArticle(){
        var  bid=document.getElementById('bid').value;
        var xhr,url='/updatearticle',data='title='+document.getElementById('title').value+'&content='+document.getElementById('content').value+'&bid='+bid;
        xhr=doajax(url,data);
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
    
