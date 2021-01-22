function partnersLogin_Ajax(email,pwd){
    let data = {
        "email" : email,
        "password" : pwd
    }

    $.ajax({
        url: "/partners/sign-in",
        type: "post",
        data: JSON.stringify(data),
        contentType: "application/json",
        success: function (data) {
            if(data.code == 200) {
                console.log(data);
                window.location = "/";
            } else {
                toastr.warning('아이디 비밀번호를 확인해주세요.', '로그인 실패');
                return false;
            }
        }
    });
}
$('#sign_btn').click(function () {
    let email = $('#email_val').val();
    let pwd = $('#pwd_val').val();
    console.log(email);
    console.log(pwd);

    var exptext = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+/;

    if(exptext.test(email) == false) {
        toastr.warning('유효한 이메일 형식이 아닙니다!', '로그인 실패');
        return false;
    }
    partnersLogin_Ajax(email,pwd);

});
$(document).ready(function () {

});
