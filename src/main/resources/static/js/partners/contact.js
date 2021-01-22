function contact_ajax(email,name,title,content){
    let data_email = {
        "title":title,
        "content":content
    };
    $.ajax({
        type: "post",
        url: "/partners/contact",
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(data_email),
        success: function(res) {
            if (res.code == 200) {
                alert('메일이 성공적으로 발송되었습니다. 답변은 빠른 시일 안에 메일로 보내드리겠습니다.');
                location.reload();
            } else {
                console.log(data_email);
                alert('문의하기 이메일 전송 오류입니다');
                alert("code = "+ request.status + " message = " + request.responseText + " error = " + error);
            }
        },error: function(request,status,error) {
            alert("code = "+ request.status + " message = " + request.responseText + " error = " + error);
        }
    });
}

function sendMail(){
    var title = $('#ct_title').val();
    var content = $('#ct_content').val();

    if(title == '' || content == ''){
        alert('모든 정보를 입력해주세요');
        return false;
    }

    contact_ajax(email,name,title,content);
}

$(document).ready(function () {
    //메뉴 하이라이트
    $('.sidebar .navigation-menu .contact').addClass('active');
    $('.sidebar .navigation-menu .contact #contact_icon').css({'color':'#4CCEAC'});
    $('#ct_phone_number').text($.cookie('email'));

});
