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
                    location.reload();
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


$(document).ready(function() {
    $.ajax({
        type: "get",
        url: "/partners/videos-info",
        contentType: "application/json",
        processData: false,
        contentType: false,
        success: function(res) {
            if (res.code == 200) {
                let videoCount = res.data.videoCount;
                let likeCount = res.data.likeCount;
                let viewCount = res.data.viewCount;
                let replyCount = res.data.replyCount;

               $("#video_count").text(videoCount);
                $("#video_like").text(likeCount);
                $("#view_count").text(viewCount);
                $("#reply_cnt").text(replyCount);

            } else {
                //alert('실패');
            }
        },
        error: function(request,status,error) {
            //alert('검색 결과가 없습니다.');
            alert("code = "+ request.status + " message = " + request.responseText + " error = " + error);
        }
    });

    $.ajax({
        type: "get",
        url: "/video/top-view",
        success: function(res) {
            if (res.code == 200) {
                console.log(res.data);
                for (var i = 0; i < Object.keys(res.data).length; i++) {
                    $('#top-video-list').append(
                        "<div class='col-12 col-xl-4'>"
                        +"<div class='row'>"
                        +"<div class='col-12 mb-4'>"
                        +"<div class='card border-light text-center p-0'>"
                        +"<div class='profile-cover rounded-top'' style='background: url(&quot;"+res.data[i].thumbnailImageURL+"&quot;) no-repeat center;background-size:auto 100%;height:280px;'></div>"
                        +"<div class='card-body pb-5'>"
                        +"<h4 class='h4'>"+ res.data[i].videoName +"</h4>"
                        +"<p class='text-gray mb-4'>조회수 : "+res.data[i].viewCount+" / 좋아요 : "+res.data[i].likeCount+"</p>"
                        +"<a class='btn btn-sm btn-primary mr-2' href='#'>수정</a>"
                        +"<a class='btn btn-sm btn-secondary' onClick=videoDel(\""+res.data[i].id+"\")>삭제</a>"
                        +"</div>"
                        +"</div>"
                        +"</div>"
                        +"</div>"
                        +"</div>"
                    );
                }
            } else {
                //alert('실패');
            }
        },
    });

    $.ajax({
        type: "get",
        url: "/partners/videos-info",
        contentType: "application/json",
        processData: false,
        contentType: false,
        success: function(res) {
            if (res.code == 200) {
                let videoCount = res.data.videoCount;
                let likeCount = res.data.likeCount;
                let viewCount = res.data.viewCount;
                let replyCount = res.data.replyCount;
                let addressStatics = res.data.addressStatics;
                let genderStatics = res.data.genderStatics;
                let birthStatics = res.data.birthStatics;

                var addressCnt = 0;
                if(res.data.addressStatics['서울특별시'] != null) {
                    var s_cnt = res.data.addressStatics['서울특별시'];
                    addressCnt += s_cnt;
                }
                if(res.data.addressStatics['부산광역시'] != null) {
                    var b_cnt = res.data.addressStatics['부산광역시'];
                    addressCnt += b_cnt;
                }
                if(res.data.addressStatics['대구광역시'] != null) {
                    var dg_cnt = res.data.addressStatics['대구광역시']
                    addressCnt += dg_cnt;
                }
                if(res.data.addressStatics['인천광역시'] != null) {
                    var i_cnt = res.data.addressStatics['인천광역시'];
                    addressCnt += i_cnt;
                }
                if(res.data.addressStatics['대전광역시'] != null) {
                    var d_cnt = res.data.addressStatics['대전광역시'];
                    addressCnt += d_cnt;
                }

                if(res.data.addressStatics['서울특별시'] != null) {
                    $('#s_count').text(s_cnt/addressCnt*100+"%");
                    $('#s_per').css("width", s_cnt/addressCnt*100+"%")
                }
                if(res.data.addressStatics['부산광역시'] != null) {
                    $('#b_count').text(b_cnt/addressCnt*100+"%");
                    $('#b_per').css("width", b_cnt/addressCnt*100+"%")
                }
                if(res.data.addressStatics['대구광역시'] != null) {
                    $('#dg_count').text(dg_cnt/addressCnt*100+"%");
                    $('#dg_per').css("width", dg_cnt/addressCnt*100+"%")
                }
                if(res.data.addressStatics['인천광역시'] != null) {
                    $('#i_count').text(i_cnt/addressCnt*100+"%");
                    $('#i_per').css("width", i_cnt/addressCnt*100+"%")
                }
                if(res.data.addressStatics['대전광역시'] != null) {
                    $('#d_count').text(d_cnt/addressCnt*100+"%");
                    $('#d_per').css("width", d_cnt/addressCnt*100+"%")
                }

                var birthCnt = 0;
                if(res.data.birthStatics['1990'] != null) {
                    var s_cnt = res.data.birthStatics['1990'];
                    birthCnt += s_cnt;
                }
                if(res.data.birthStatics['1980'] != null) {
                    var b_cnt = res.data.birthStatics['1980'];
                    birthCnt += b_cnt;
                }
                if(res.data.birthStatics['2000'] != null) {
                    var dg_cnt = res.data.birthStatics['2000']
                    birthCnt += dg_cnt;
                }
                if(res.data.birthStatics['2010'] != null) {
                    var i_cnt = res.data.birthStatics['2010'];
                    birthCnt += i_cnt;
                }
                if(res.data.birthStatics['1970'] != null) {
                    var d_cnt = res.data.birthStatics['1970'];
                    birthCnt += d_cnt;
                }

                if(res.data.birthStatics['1990'] != null) {
                    $('#1990_count').text(s_cnt/birthCnt*100+"%");
                    $('#1990_per').css("width", s_cnt/birthCnt*100+"%")
                }
                if(res.data.birthStatics['1980'] != null) {
                    $('#1980_count').text(b_cnt/birthCnt*100+"%");
                    $('#1980_per').css("width", b_cnt/birthCnt*100+"%")
                }
                if(res.data.birthStatics['2000'] != null) {
                    $('#2000_count').text(dg_cnt/birthCnt*100+"%");
                    $('#2000_per').css("width", dg_cnt/birthCnt*100+"%")
                }
                if(res.data.birthStatics['2010'] != null) {
                    $('#2010_count').text(i_cnt/birthCnt*100+"%");
                    $('#2010_per').css("width", i_cnt/birthCnt*100+"%")
                }
                if(res.data.birthStatics['1970'] != null) {
                    $('#1970_count').text(d_cnt/birthCnt*100+"%");
                    $('#1970_per').css("width", d_cnt/birthCnt*100+"%")
                }

                var genderCnt = 0;
                if(res.data.genderStatics['남'] != null) {
                    var s_cnt = res.data.genderStatics['남'];
                    genderCnt += s_cnt;
                }
                if(res.data.genderStatics['여'] != null) {
                    var b_cnt = res.data.genderStatics['여'];
                    genderCnt += b_cnt;
                }

                if(res.data.genderStatics['남'] != null) {
                    $('#male_count').text(s_cnt/genderCnt*100+"%");
                    $('#male_per').css("width", s_cnt/genderCnt*100+"%")
                }
                if(res.data.genderStatics['여'] != null) {
                    $('#female_count').text(b_cnt/genderCnt*100+"%");
                    $('#female_per').css("width", b_cnt/genderCnt*100+"%")
                }

                $("#video_count").text(videoCount);
                $("#video_like").text(likeCount);
                $("#view_count").text(viewCount);
                $("#reply_cnt").text(replyCount);

            } else {
                //alert('실패');
            }
        },
        error: function(request,status,error) {
            //alert('검색 결과가 없습니다.');
            alert("code = "+ request.status + " message = " + request.responseText + " error = " + error);
        }
    });
});