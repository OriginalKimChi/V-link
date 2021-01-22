var getParameters = function (paramName) {
    // 리턴값을 위한 변수 선언
    var returnValue;

    // 현재 URL 가져오기
    var url = location.href;

    // get 파라미터 값을 가져올 수 있는 ? 를 기점으로 slice 한 후 split 으로 나눔
    var parameters = (url.slice(url.indexOf('?') + 1, url.length)).split('&');

    // 나누어진 값의 비교를 통해 paramName 으로 요청된 데이터의 값만 return
    for (var i = 0; i < parameters.length; i++) {
        var varName = parameters[i].split('=')[0];
        if (varName.toUpperCase() == paramName.toUpperCase()) {
            returnValue = parameters[i].split('=')[1];
            return decodeURIComponent(returnValue);
        }
    }
};
function pwdChageAjax(current_pwd,new_pwd){
    var data_pwd ={
        "password":current_pwd,
        "newPassword":new_pwd
    };
    $.ajax({
        type: "post",
        url: "/partners/password-change",
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(data_pwd),
        success: function(res) {
            if (res.code == 200) {
                alert('비밀번호가 변경되었습니다');
                location.replace('/');
            } else {
                alert('비밀번호가 올바르지 않습니다.');
            }
        },error: function() {
            alert('비밀번호 변경 오류');
        }
    });
}
/*function mailAuth_ajax(email,authCode){
    //login api
    var data_auth = {
        "email": email,
        "authCode":  authCode
    };

    $.ajax({
        type: "post",
        url: "/user/auth",
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(data_auth),
        success: function(res) {
            if (res.code == 200) {
                alert('인증번호가 전송되었습니다.');
                location.href='./rePassword2';
            } else {
                alert('오류');
            }
        },error: function() {
            alert('비밀번호 오류');
        }
    });
}*/
function modifyPwd(){
    var current_pwd = $('#current_pwd').val();
    var new_pwd = $('#new_pwd').val();
    var new_pwd2 = $('#new_pwd2').val();

    var email = getParameters('email');

    if(current_pwd=='' || new_pwd =='' || new_pwd2==''){
        alert('모두 입력해주세요');
        return false;
    }

    if(new_pwd!=new_pwd2){
        alert('새 비밀번호와 새 비밀번호 확인을 동일하게 입력해주세요');
        return  false;
    }

    pwdChageAjax(current_pwd,new_pwd);

}
