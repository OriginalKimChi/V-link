var ext;
var i_src='';
function logout_ajax(){
    //쿠키 삭제
    $.removeCookie('qmdlfldzm');
    $.removeCookie('partners_name');
    $.removeCookie('partners_email');
    $.removeCookie('partners_followerCount');

    $.ajax({

        url: "/partners/logout",
        type: "get",
        contentType: "application/json",
        processData: false,
        contentType: false,
        success: function (data) {
            location.href="./";
        }

    });
}
function profileImgAjax(file){


   /* var formData = new FormData();
    formData.append("file", file);

    //this.removeFile(file);
    $.ajax({
        type:"get",
        url: "/partners/update-profileImg",
        data: formData,
        processData: false,
        contentType: false,
        success: function(data) {
            if (data.code == 200) {

            } else {d
                alert('프로필 변경 실패!', '다시 시도해 주세요.');
                console.log(data.message);
            }
        },
        err: function(request,status,error) {
            alert("code = "+ request.status + " message = " + request.responseText + " error = " + error);
            alert('업로드 실패');
        }
    });*/
}
<!-- 프로필 변경 -->
$("#partners_profile_img").dropzone({
    sending: function(file) {
        //확장자 판별
        ext = file.name.split('.').pop().toLowerCase();
        if ($.inArray(ext, ['jpeg', 'jpg', 'png', 'bmp', 'tiff']) == -1) {
            alert(file.name + ' 파일은 이미지가 아니므로 업로드 하실 수 없습니다.');

            //이미지가 아닐경우 기존에 업로드 했던 파일 날리기
            this.on("complete", function (file) {
                this.removeAllFiles(true);
            });
            return false;
        } else {
            $('#partners_profile_img').css({'display': 'none'});
            var formData = new FormData();
            formData.append("file", file);
            console.log("image thumbnail upload start");

            $.ajax({
                url: "/video/upload",
                type: "post",
                data: formData,
                processData: false,
                contentType: false,
                success: function (data) {
                    if (data.code == 200) {
                        var form = new FormData();
                        form.append("image", data.data);
                        console.log(data.data);
                        console.log("변경 이미지 적용..");
                        $.ajax({
                            url: "/partners/update-profileImg",
                            type: "get",
                            data: encodeURI(form),
                            processData: false,
                            contentType: false,
                            success: function (data) {
                                if (data.code == 200) {
                                    alert('프로필 변경 완료!', '정상적으로 업로드 되었습니다.');
                                    $('#profile_img').attr('src', data.data.profileImageURL);
                                    location.reload();
                                } else {
                                    alert('프로필 변경 실패!', '다시 시도해 주세요.');
                                    console.log(data.message);
                                }
                            },
                            err: function (request, status, error) {
                                alert("code = " + request.status + " message = " + request.responseText + " error = " + error);
                                alert('업로드 실패');
                            }
                        });
                    } else {
                        console.log(data.message);
                    }
                },
                err: function () {
                    //alert('업로드 실패');
                }
            });


        }
    }
});

$(document).ready(function() {
    if(!$.cookie('qmdlfldzm')){
        alert('로그인 후 이용 가능합니다.');
        location.href="./";
    }
    $('.profile-img .dz-button').html("<i class='fas fa-camera'></i>");

    //헤더에 파트너스 정보 넣기
    $('#header_partners_name').text($.cookie('partners_name'));
    $('#header_partners_email').text($.cookie('partners_email'));

    //---------파트너스 로그인 조회수 어딨지?
    $('#header_total1').text($.cookie('partners_followerCount'));
    $('#header_total2').text($.cookie('name'));
    $('#header_total3').text($.cookie('name'));


});
