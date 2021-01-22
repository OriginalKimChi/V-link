function logout(){
    $.ajax({
        type: "get",
        url: "/partners/logout",
        success: function(res) {
            if (res.code == 200) {
                location.replace('./');

            } else {
                //('로그아웃이 되지 않았습니다');
            }
        },error: function() {
            //alert('로그아웃 오류');
        }
    });
}

function auth_ajax(current_pwd,new_pwd){
    var data = {
        "password": current_pwd,
        "newPassword":  new_pwd
    };


    $.ajax({
                    type : "post",
                    url: "/partners/password-change",
                    dataType: "json",
                    contentType: "application/json",
                    processData: false,
                    data: JSON.stringify(data),
                    success: function(res) {
                        if (res.code == 200) {
                            alert('비밀번호가 변경되었습니다');
                        } else {
                            alert('비밀번호가 올바르지 않습니다.');
                        }
                    },error: function() {
                        //alert('비밀번호 변경 오류');
                    }
                });
}

function modifyPwd(){
    var current_pwd = $('#current_pwd').val();
    var new_pwd = $('#new_pwd').val();
    var new_pwd2 =  $('#new_pwd2').val();


    if(current_pwd =='' || new_pwd =='' || new_pwd2==''){
        alert('모든 정보를 입력해주세요');
        return false;
    }

    if(new_pwd!=new_pwd2){
        alert('새 비밀번호와 새 비밀번호 확인 입력 정보가 다릅니다.');
        return false;
    }

    auth_ajax(current_pwd,new_pwd);
}

$(document).ready(function () {
    //메뉴 하이라이트
    $('.sidebar .navigation-menu .pwd').addClass('active');
    $('.sidebar .navigation-menu .pwd #repassword_icon').css({'color':'#4CCEAC'});
});