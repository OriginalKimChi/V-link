

function findPwd_ajax(email,name){
    //login api
    var data_email = {
        "email": email,
        "name":  name
    };
    $.ajax({
        type: "post",
        url: "/user/mail-check",
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(data_email),
        success: function(res) {
            if (res.code == 200) {
                location.replace('./re-Password2?email='+res.data.id);
            } else {
                alert('해당하는 계정이 존재하지 않습니다.');
            }
        },error: function() {
            alert('로그인 오류');
        }
    });
}
function auth_email(){
    var name = $('#name_val').val();
    var email = $('#email_val').val();

    if(email=='' || name==''){
        alert('이름과 이메일 모두 입력해주세요');
        return false;
    }

  findPwd_ajax(email,name);
}