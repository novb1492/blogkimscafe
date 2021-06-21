function doInsertArticle(){
    var form= document.getElementById('form');
    var formData = new FormData(form);
    var xhr = new XMLHttpRequest();
    xhr.open("POST" , "/insertarticle" , true);
    xhr.send(formData);
 }