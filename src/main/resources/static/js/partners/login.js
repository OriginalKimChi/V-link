$('#sign_btn').click(function(){
    var email = $('#email_val').val();
    var pwd = $('#pwd_val').val();

    if(email=='' || pwd ==''){
        alert('이메일과 비밀번호 모두 입력해주세요');
        return false;
    }

    var data={
        "email":email,
        "password":pwd,
    }
    $.ajax({

        url: "/partners/sign-in",

        type: "post",

        data: JSON.stringify(data),

        contentType: "application/json",

        success: function (data) {

            if(data.code == 200) {
                $.cookie('qmdlfldzm',data.data.id);
                $.cookie('partners_name',data.data.name);
                $.cookie('partners_email',data.data.email);

                location.href='./dashboard';

            } else {
                console.log(data);
                toastr.warning('아이디 비밀번호를 확인해주세요.', '로그인 실패');

                return false;

            }

        }

    });


});