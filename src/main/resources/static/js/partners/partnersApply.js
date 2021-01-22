function signUp_Ajax(pa_email,pa_name,pa_facebook,pa_instagram,pa_naver){
    let data = {
        "email" : pa_email,
        "name" : pa_name,
        "snsAccount" : [
            {
                "domain" : "facebook",
                "account" :pa_facebook
            },{
                "domain" : "instagram",
                "account" : pa_instagram
            }
        ],
        "blogURL" :
            {
                "domain" : "naver",
                "account" : pa_naver
            },
        "interestField" : ["love", "peace"]
    }

    $.ajax({
        url: "/partners/sign-up",
        type: "post",
        data: JSON.stringify(data),
        contentType: "application/json",
        success: function(res) {
            if (res.code == 200) {
                alert('파트너스 신청이 완료되었습니다.');
                location.href="./";

            } else {
                alert('실패');
            }
        },
        error: function(request,status,error) {
            console.log(data);
            alert("code = "+ request.status + " message = " + request.responseText + " error = " + error);
            alert('신청 오류입니다');
        }
    });
}


/*파트너스 신청*/
$('#apply_btn').click(function(){

    let pa_email = $('#pa_email').val();
    let pa_name = $('#pa_name').val();
    let pa_facebook = $('#pa_facebook').val();
    let pa_instagram = $('#pa_instagram').val();

    let pa_naver = $('#pa_naver').val();

    if(pa_email==''||pa_name==''||pa_facebook==''||pa_instagram==''||pa_naver==''){
        alert('모든 정보를 입력해주세요');
    }

    signUp_Ajax(pa_email,pa_name,pa_facebook,pa_instagram,pa_naver);
});