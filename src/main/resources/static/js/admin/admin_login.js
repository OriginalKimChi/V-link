function admin_login() {
    var admin_id = $('#admin_id').val();
    var admin_pwd = $('#admin_pwd').val();

    if (admin_id == '' || admin_pwd == '') {
        alert('아이디와 비밀번호 모두 입력해주세요');
        return false;
    }

    const sendingData = new FormData();
    sendingData.append('id',admin_id);
    sendingData.append('password',admin_pwd);

    console.log(admin_id+" "+admin_pwd);

    $.ajax({
        url: "/admin/login",
        type: "post",
        data: sendingData,
        contentType: "application/json",
        processData: false,
        contentType: false,
        success: function(res,request,status,error) {
            console.log(res);
            if (res) {
                alert("관리자로 로그인 되었습니다");
                location.href="./admin-list"
            } else {
                alert('관리자가 아닙니다');
            }
        },error: function(request,status,error) {
            alert("code = "+ request.status + " message = " + request.responseText + " error = " + error);
            alert('관리자 로그인 오류');
        }
    });
}
