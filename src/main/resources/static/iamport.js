function iamport(productName,price,buyerEmail){
    //가맹점 식별코드
    IMP.init('imp59938102');
    IMP.request_pay({
        pg : 'kakaopay',
        pay_method : 'kakaopay',
        merchant_uid : 'merchant_' + new Date().getTime(),
        name : productName, //결제창에서 보여질 이름
        amount : price, //실제 결제되는 가격
        buyer_email : buyerEmail,
        buyer_name : '구매자이름',
        buyer_tel : '010-1234-5678',
        buyer_addr : '서울',
        buyer_postcode : '123-456'
    },  function(rsp) {
        if (rsp.success) {
            console.log(rsp.imp_uid);
            $.ajax({
                type:"post",
                url:"/confrimPay",
            data:{
                'imp_uid' : rsp.imp_uid
            },
            success:function(data){
                if(data.result){
                    $('input[name="requesthour"]:checkbox:checked').each(function(){///check박스 값 가져오려면 name값 지정해 넣어주면돤다20210526
                        requesthour.push($(this).val());
                    });
                     insertReservation(seat,requesthour);
                }else{
                    alert(data.messege);
                }  
                out();
            }
            })
            
        }else{
             var msg = '결제에 실패하였습니다.';
             msg += '에러내용 : ' + rsp.error_msg;
             alert(msg);
             out();
        }
    });
}
function insertReservation(seat,requesthour){
    $.ajax({
        type:"post",
        url:"/insertreservation",
    data:{
       "seat":seat,
       "requesthour":requesthour,
    },
    success:function(data){
        if(data.result){
            alert(data.messege); 
        }else{
            alert(data.messege);
        }  
        out();
    }
    })

}
function canclePay(){
    $.ajax({
        url: "http://www.myservice.com/payments/cancel",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
          "merchant_uid": "mid_" + new Date().getTime(), // 주문번호
          "cancel_request_amount": 2000, // 환불금액
          "reason": "테스트 결제 환불" // 환불사유
        }),
        dataType: "json"
      });
}