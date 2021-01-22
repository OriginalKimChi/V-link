function videoDel(id) {
        var video_id = {
            "id": id
        };
        $(".blackScreen").css({'display': 'block'});
        $.ajax({
            type: "delete",
            url: "/video/delete",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(video_id),
            success: function(res) {
                if (res.code == 200) {
                    $(".blackScreen").css({'display': 'none'});
                    const notyf = new Notyf({
                            position: {
                                x: 'left',
                                y: 'top',
                            },
                            types: [
                                {
                                    type: 'info',
                                    background: 'green',
                                    icon: {
                                        className: 'fas fa-info-circle',
                                        tagName: 'span',
                                        color: '#fff'
                                    },
                                    dismissible: false
                                }
                            ]
                        });
                        notyf.open({
                            type: 'info',
                            message: '정상적으로 삭제 되었습니다.'
                        });
                    location.reload();
                } else {
                    alert('실패');
                }
            },
            error: function() {
                //alert('검색 결과가 없습니다.');
            }
        });
    }

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

    $("#partners_profile_img").dropzone({
        sending: function(file) {
            $(".blackScreen").css({'display': 'block'});
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
                formData.append("image", file);
                console.log("image thumbnail upload start");

                $.ajax({
                    url: "/partners/update-profileImg",
                    type: "post",
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (data) {
                        if (data.code == 200) {
                            $(".blackScreen").css({'display': 'none'});
                            const notyf = new Notyf({
                                                        position: {
                                                            x: 'left',
                                                            y: 'top',
                                                        },
                                                        types: [
                                                            {
                                                                type: 'info',
                                                                background: 'green',
                                                                icon: {
                                                                    className: 'fas fa-info-circle',
                                                                    tagName: 'span',
                                                                    color: '#fff'
                                                                },
                                                                dismissible: false
                                                            }
                                                        ]
                                                    });
                                                    notyf.open({
                                                        type: 'info',
                                                        message: '정상적으로 변경 되었습니다.'
                                                    });
                            location.reload();
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
    $.ajax({
        type: "get",
        url: "/partners/product-list",
        contentType: "application/json",
        processData: false,
        contentType: false,
        success: function (res) {
            if (res.code == 200) {
                console.log(res.data);
                //for (let i = 0; i < Object.keys(res.data).length; i++) {
                 for(let keys of Object.keys(res.data)){
                     var p_link =res.data[keys].productURL;
                    $('#product-list').append(
                        "<li class=\"list-group-item px-0\">"
                        +"<div class=\"row align-items-center\">"
                        +"<div class=\"col-2 align-items-center\">"
                        +"<a href=\"#\" class=\"user-avatar\" style=\"width: 4rem; height: 4rem;\">"
                        +"<img class=\"rounded-circle\" alt=\"Image placeholder\" src=\""+res.data[keys].productImageURL+"\"></a></div>"
                        +"<div class=\"col-7 ml--2\">"
                        +"<h4 class=\"h6 mb-0\"><a href=\"#\">"+res.data[keys].productName+"</a></h4>"
                        +"<span class=\"text-success\">●</span>"
                        +"<small>"+res.data[keys].price+"</small></div>"
                        +"<div class=\"col-3\">"
                        +"<div class=\"col text-right\"><a href=\""+res.data[keys].productURL+"\" class=\"btn btn-sm btn-tertiary\"><i class=\"fas fa-calendar-check mr-1\"></i>이동</a></div>"
                        +"</div></div></li>"
                    );

                }


            } else {
                //alert('실패');
            }
        }, error: function () {
            //alert('검색 결과가 없습니다.');
        }
    });

        var page = 1;
        /*내 비디오 ajax*/
        var data2 = {
            "page": page
        };

        $.ajax({
            type: "post",
            url: "/partners/video-list",
            dataType: "json",
            contentType: "application/json",
            processData: false,
            data: JSON.stringify(data2),
            success: function(res) {
                if (res.code == 200) {
                    var imgList = [];
                    var tagBox = [];
                    var t = 0;
                    page =1;
                    console.log(res);

                    //상품 리스트
                    for (var i = 0; i < res.data.content.length; i++) {
                        imgList ="";
                        tagBox="";
                        //페이징 처리
                        var total_page = res.data.totalPages;
                        if (total_page < page) {
                            console.log('마지막 페이지');

                            //------------스크롤링 안되게 하는 스크립트 추가 예정--------
                        }
                        for (var j = 0; j < res.data.content[i].productList.length; j++) {
                            imgList += "<div class=\"col-2\"><a href=\"#\" class=\"user-avatar\"><img class=\"rounded-circle\" alt=\"Image placeholder\" src=\""+res.data.content[i].productList[j].product.productImageURL+"\"></a></div>"
                        }
//                        var tag = res.data.content[i].tagList;
//                        if (t in tag) {
//                            tagBox +="<p class='text-gray tag' style='margin-right:3px;'>"+res.data.content[i].tagList[t]+"</p>";
//                            t++;
//                        }

                        $('#my_video').append(
                            "<div class=\"col-12 col-xl-3\">"
                            +"<div class=\"row\">"
                            +"<div class=\"col-12 mb-4\">"
                            +"<div class=\"card border-light text-center p-0\">"
                            +"<div class=\"profile-cover rounded-top\" style=\"background: url(&quot;"+res.data.content[i].thumbnailImageURL+"&quot;) no-repeat center;background-size:auto 100%;height:220px;\"></div>"
                            +"<div class=\"card-body pb-5\">"
                            +"<h4 class=\"h5\">"+  res.data.content[i].videoName+"</h4>"
                            +"<p class=\"text-gray mb-4\">조회수 : "+ res.data.content[i].viewCount+" / 좋아요 : "+res.data.content[i].likeCount+"</p>"
                            +"<div class=\"row text-left\">"
                            +"<h5 class=\"h6\">상품 리스트</h5></div>"
                            +"<div class=\"row\" style=\"margin-bottom:30px\">"
                            +imgList
                            +"</div>"
                            +"<a class=\"btn btn-sm btn-primary col-5 mr-2\" onClick=videoModify("+res.data.content[i].id+")>수정</a>"
                            +"<a class=\"btn btn-sm col-5 btn-secondary\" onClick=videoDel(\""+res.data.content[i].id+"\")>삭제</a></div></div></div></div></div>"
                        );

                        page++;
                    }

                } else {
                    //alert('실패');
                }
            },
            error: function() {
                //alert('검색 결과가 없습니다.');
            }

        });
});