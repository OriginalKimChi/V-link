//파트너스 정보 보기
function show_partners(id,profile,email,name,facebook,instagram,naver){
    console.log(id);
    $("#pa_info_img").attr("src", profile);
    $('#pa_id').val(id);
    $('#pa_email').val(email);
    $('#pa_name').val(name);
    $('#sns1').val(facebook);
    $('#sns2').val(instagram);
    $('#blog1').val(naver);

}

//파트너스 승인
function ap_partners(id){
   /* let data = {
        "id": encodeURI(id)
    }
    $.ajax({
        type: "get",
        url: "/admin/allow-partners",
        data: JSON.stringify(data),
        contentType: "application/json",
        processData: false,
        success: function(res) {
            if (res) {
                alert("파트너스 승인이 되었습니다");
                location.reload();
            } else {
                alert('파트너스 승인에 오류가 발생했습니다');
            }
        },error: function (request,status,error) {
            alert("code = "+ request.status + " message = " + request.responseText + " error = " + error);
        }
    });*/
    let settings = {
        "url": "/admin/allow-partners",
        "method": "POST",
        "timeout": 0,
        "headers": {
            "Content-Type": "application/json"
        },
        "data": JSON.stringify({"id":id}),
    };

    $.ajax(settings).done(function (response) {
        location.href='./admin-list';
    });
}
$(document).ready(function() {
    $.ajax({
        type: "get",
        url: "/admin/waiting-partners",
        success: function (res) {
            if (res.code == 200) {
                for (var i = 0; i < Object.keys(res.data).length; i++) {
                    var id = res.data[i].id;
                    var email = res.data[i].email;
                    var profile = res.data[i].profileImageURL;
                    var name = res.data[i].name;
                    var facebook = $.trim(res.data[i].snsAccount[0].account);
                    var naver = (res.data[i].blogURL.account).replace(/ /g, '');

                    var instagram="";
                    if(!facebook) facebook='';
                    if(!naver)  naver='';


                    $('#show_admin_list').append("<tr>" +
                        "<td>" +
                        "<p class='mb-n1 font-weight-medium'>" + (i + 1) + "</p>" +
                        "</td>" +
                        "<td class='font-weight-medium'>" + email + "</td>" +
                        "<td class='font-weight-medium'>" +
                        "<div>" +
                        "<button class='btn btn-outline-light text-gray component-flat w-40 mr-2' data-toggle='modal' data-target='#myModal' onClick=show_partners('"+id+"','"+profile+"','"+email+"','"+name+"','"+facebook+"','"+instagram+"','"+naver+"')>보기</button>"+
                        "<button class='btn btn-primary w-40 ml-2' onClick=ap_partners('"+id+"')>승인</button>"+
                        "</div>" +
                        "</td>" +
                        "</tr>");
                    //id,profile,name,facebook,instagram,naver,daum
                }


            } else {
                alert('파트너스 리스트 조회 실패');
            }
        },
        error: function () {
            //alert('오류입니다');
            /*var res ={
                "code": 200,
                "message": "success",
                "data": [
                    {
                        "id": "5fd71a2bb31a985da10a220a",
                        "email": "pjhyl1127@gmail.com",
                        "name": "tester",
                        "profileImageURL": "https://vlink-data.s3.ap-northeast-2.amazonaws.com/user_image.png",
                        "snsAccount": [
                            {
                                "domain": "facebook",
                                "account": "facebook1"
                            },{
                                "domain": "instagram",
                                "account": "@junghwnaTa"
                            }
                        ],
                        "blogURL": [{
                            "domain": "naver",
                            "account": "naver url"
                        },{
                            "domain": "daum",
                            "account": "daum url"
                        }],
                        "interestField": [
                            "love",
                            "peace"
                        ],
                        "password": null,
                        "authState": false,
                        "videoList": null,
                        "followeeList": null,
                        "createAt": "2020-12-14 16:54:19"
                    }
                ]
            };
            if (res.code == 200) {
                for (var i = 0; i < Object.keys(res.data).length; i++) {
                    var id = res.data[i].id;
                    var email = res.data[i].email;
                    var profile = res.data[i].profileImageURL;
                    var name = res.data[i].name;
                    var facebook = res.data[i].snsAccount[0].account;
                    var instagram = res.data[i].snsAccount[1].account;
                    var naver = res.data[i].blogURL[0].account;
                    var daum = res.data[i].blogURL[1].account;

                    $('#show_admin_list').append("<tr>" +
                        "<td>" +
                        "<p class='mb-n1 font-weight-medium'>" + (i + 1) + "</p>" +
                        "</td>" +
                        "<td class='font-weight-medium'>" + res.data[i].id + "</td>" +
                        "<td class='font-weight-medium'>" +
                        "<div style='margin-top:10px;'>" +
                        "<button class='btn btn-outline-light text-gray component-flat w-40 mr-2' onClick=show_partners('"+id+"','"+profile+"','"+email+"','"+name+"','"+facebook+"','"+instagram+"','"+naver+"','"+daum+"')>보기</button>"+
                        "<button class='btn btn-primary w-40 ml-2' onClick='ap_partners('"+res.data[i].id+"')'>승인</button>"+
                        "</div>" +
                        "</td>" +
                        "</tr>");
                    //id,profile,name,facebook,instagram,naver,daum
                }


            }*/
        }
    });
});
