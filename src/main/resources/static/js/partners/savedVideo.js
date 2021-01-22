var medal;

function videoDel(id) {
    alert("삭제할 동영상 id " + id);
    /*비디오 삭제*/
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
            } else {
                alert('실패');
            }
        },
        error: function() {
            //alert('검색 결과가 없습니다.');
        }
    });
}

function videoModify(id) {
    alert("수정할 동영상 id " + id);
}

/*function topVideo_ajax(){
    $.ajax({
        type: "get",
        url: "/video/top-view",
        success: function(res) {
            if (res.code == 200) {
                for (var i = 0; i < Object.keys(res.data).length; i++) {
                    if (i == 0) medal = '#FFDC3C';
                    else if (i == 1) medal = '#CFCACA';
                    else medal = '#EA7D3E';

                    $('#top_box_list').append(
                        "<div id='top_box"+(i+1)+"' class='top_box col-lg-4 col-md-6 equel-grid'>" +
                        "<div class='grid'>" +
                        "<div class='grid-body'>" +
                        "<p class='card-title'> <i class='fas fa-medal' style='color:"+medal+";'></i>Top "+(i+1)+"</p>" +
                        "<div class='img_cover' style='position:relative;'>" +
                        "<div class='time'>" +
                        "<span>"+res.data[i].playTime+"</span>" +
                        "</div>" +
                        "<img src='"+res.data[i].thumbnailImageURL+"'/>" +
                        "</div>" +
                        "<h5 class='text-center'>"+ res.data[i].videoName+"</h5>" +
                        "<p class='text-center text-muted'></p>" +
                        "<div class='count'>" +
                        "<div class='list'>" +
                        "<div class='count' id='video_count'>" +
                        "<div class='icon'>" +
                        "<i class='far fa-file-video'></i>" +
                        "</div>" +
                        res.data[i].replyCount+
                        "</div>" +
                        "</div>"+
                        "<div class='list'>" +
                        "<div class='count'>" +
                        "<div class='icon'>" +
                        "<i class='far fa-heart'></i>" +
                        "</div>" +
                        res.data[i].likeCount +
                        "</div>" +
                        "</div>" +
                        "<div class='list'>" +
                        "<div class='count' id='video_count'>" +
                        "<div class='icon'>" +
                        "<i class='fas fa-eye'></i>" +
                        "</div>" +
                        res.data[i].viewCount +
                        "</div>" +
                        "</div>" +
                        "</div>" +
                        "</div>" +
                        "</div>" +
                        "</div>");

                }
            } else {
                alert('실패');
            }
        },
        error: function() {
            //alert('검색 결과가 없습니다.');
          /!*  var res={
                "code": 200,
                "message": "success",
                "data": [
                    {
                        "id": "5fdc0b0fd959953db35d578d",
                        "videoName": "[펜트하우스]비둘기의 등장에 뒤집어진 현장",
                        "videoURL": "https://vlink-data.s3.ap-northeast-2.amazonaws.com/test.mp4.mp4",
                        "thumbnailImageURL": "https://vlink-data.s3.ap-northeast-2.amazonaws.com/%EB%8B%A4%EC%9A%B4%EB%A1%9C%EB%93%9C+(1).jfif",
                        "playTime": 235,
                        "partners": {
                            "id": "5fdc025ecf4c647a6af71559",
                            "name": "tester",
                            "profileImageURL": "https://vlink-data.s3.ap-northeast-2.amazonaws.com/partners/Fri%20Dec%2018%2011%3A14%3A47%20KST%202020st6uq3b429d21824b3z3%20%281%29.jpg"
                        },
                        "viewCount": 3,
                        "likeCount": 0,
                        "replyCount": 0,
                        "likedVideo": false
                    },
                    {
                        "id": "5fdab2863ded3b1b504269dc",
                        "videoName": "test videoName2",
                        "videoURL": "https://vlink-data.s3.ap-northeast-2.amazonaws.com/test.mp4.mp4",
                        "thumbnailImageURL": "https://vlink-data.s3.ap-northeast-2.amazonaws.com/%EB%8B%A4%EC%9A%B4%EB%A1%9C%EB%93%9C+(1).jfif",
                        "playTime": null,
                        "partners": {
                            "id": "5fd86f1bc9e9bb1d36d6b3bd",
                            "name": "jjeun",
                            "profileImageURL": "https://vlink-data.s3.ap-northeast-2.amazonaws.com/%EB%8B%A4%EC%9A%B4%EB%A1%9C%EB%93%9C.jfif"
                        },
                        "viewCount": 2,
                        "likeCount": 0,
                        "replyCount": 1,
                        "likedVideo": false
                    },
                    {
                        "id": "5fdc0a63a6af16063d57bf3d",
                        "videoName": "test videoName2",
                        "videoURL": "https://vlink-data.s3.ap-northeast-2.amazonaws.com/test.mp4.mp4",
                        "thumbnailImageURL": "https://vlink-data.s3.ap-northeast-2.amazonaws.com/%EB%8B%A4%EC%9A%B4%EB%A1%9C%EB%93%9C+(1).jfif",
                        "playTime": null,
                        "partners": {
                            "id": "5fdc025ecf4c647a6af71559",
                            "name": "tester",
                            "profileImageURL": "https://vlink-data.s3.ap-northeast-2.amazonaws.com/partners/Fri%20Dec%2018%2011%3A14%3A47%20KST%202020st6uq3b429d21824b3z3%20%281%29.jpg"
                        },
                        "viewCount": 0,
                        "likeCount": 0,
                        "replyCount": 0,
                        "likedVideo": false
                    }
                ]
            }

            if (res.code == 200) {
                for (var i = 0; i < Object.keys(res.data).length; i++) {
                    if (i == 0) medal = '#FFDC3C';
                    else if (i == 1) medal = '#CFCACA';
                    else medal = '#EA7D3E';

                    $('#top_box_list').append(
                        "<div id='top_box'"+(i+1)+" class='top_box col-lg-4 col-md-6 equel-grid'>" +
                        "<div class='grid'>" +
                        "<div class='grid-body'>" +
                        "<p class='card-title'> <i class='fas fa-medal' style='color:"+medal+";'></i>Top "+i+"</p>" +
                        "<div class='img_cover' style='position:relative;'>" +
                        "<div class='time'>" +
                        "<span>"+res.data[i].playerTime+"</span>" +
                        "</div>" +
                        "<img src='"+res.data[i].thumbnailImageURL+"'/>" +
                        "</div>" +
                        "<h5 class='text-center'>"+ res.data[i].videoName+"</h5>" +
                        "<p class='text-center text-muted'></p>" +
                        "<div class='count'>" +
                        "<div class='list'>" +
                        "<div class='count' id='video_count'>" +
                        "<div class='icon'>" +
                        "<i class='far fa-file-video'></i>" +
                        "</div>" +
                        res.data[i].replyCount+
                        "</div>" +
                        "</div>"+
                        "<div class='list'>" +
                        "<div class='count'>" +
                        "<div class='icon'>" +
                        "<i class='far fa-heart'></i>" +
                        "</div>" +
                        res.data[i].likeCount +
                        "</div>" +
                        "</div>" +
                        "<div class='list'>" +
                        "<div class='count' id='video_count'>" +
                        "<div class='icon'>" +
                        "<i class='fas fa-eye'></i>" +
                        "</div>" +
                        res.data[i].viewCount +
                        "</div>" +
                        "</div>" +
                        "</div>" +
                        "</div>" +
                        "</div>" +
                        "</div>");

                }
            }*!/
        }
    });

}*/

function viewCount_ajax(){
    $.ajax({
        type: "get",
        url: "/partners/videos-info",
        contentType: "application/json",
        processData: false,
        contentType: false,
        success: function(res) {
            if (res.code == 200) {
            console.log(res);
                let videoCount = res.data.videoCount;
                let likeCount = res.data.likeCount;
                let viewCount = res.data.viewCount;
                let replyCount = res.data.replyCount;

               $("#video_count").text(videoCount);
                $("#video_like").text(likeCount);
                $("#video_cnt").text(viewCount);
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
}

function changeClock(seconds) {
    const secondsNumber = parseInt(seconds, 10)
    let minutes = Math.floor(secondsNumber / 60)
    let totalSeconds = secondsNumber % 60

    if (minutes < 10) {
        minutes = `0${minutes}`
    }
    if (totalSeconds < 10) {
        totalSeconds = `0${totalSeconds}`
    }
    return `${minutes}:${totalSeconds}`
}

function videoList_ajax(){

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
                            imgList += "<img src='" + res.data.content[i].productList[j].product.productImageURL + "' class='img-sm'/>";

                        }
                        var tag = res.data.content[i].tagList;
                        if (t in tag) {
                            tagBox +="<p class='text-gray tag' style='margin-right:3px;'>"+res.data.content[i].tagList[t]+"</p>";
                            t++;
                        }
                        var id = res.data.content[i].id;
                        var del_btn = "onClick=videoDel('" + id + "')";
                        var modify_btn = "onClick=videoModify('" + id + "')";
                        $('#my_video').append(" <div id='my_video"+(i+1)+"' class='my_video_box col-lg-3 col-md-6 col-sm-12 equel-grid'>" +
                            "<div class='grid'>" +
                            "<div class='grid-body'>" +
                            "<div class='split-header'>" +
                            "<div class='img' style='position:relative;'>" +
                            "<img src='"+res.data.content[i].thumbnailImageURL+"' style='width:100%;'/>" +
                            "<div class='time'><span>"+changeClock(res.data.content[i].playTime)+"</span></div>" +
                            "</div>" +
                            "</div>" +
                            "<div class='d-flex align-items-end mt-2'>" +
                            "<h6 id='v_t'>"+  res.data.content[i].videoName+"</h6>" +
                            "</div>" +
                            "<div class='count d-flex mt-2'>" +
                            "<div class='wrapper d-flex pr-4'>" +
                            "<div class='text-success font-weight-medium mr-2'> <i class='far fa-file-video'></i></div>"+
                            "<div class='text-gray'>"+res.data.content[i].replyCount+"</div>"+
                            "</div>"+
                            "<div class='wrapper d-flex pr-4'>" +
                            "<div class='text-primary font-weight-medium mr-2'><i class='far fa-heart'></i></div>"+
                            "<div class='text-gray'>"+res.data.content[i].likeCount+"</div>"+
                            "</div>" +
                            "<div class='wrapper d-flex'>" +
                            "<div class='text-primary font-weight-medium mr-2'><i class='fas fa-eye'></i></div>" +
                            "<div class='text-gray'>"+ res.data.content[i].viewCount+"</div>"+
                            "</div>" +
                            "</div>" +
                            "</div>" +
                            "<div class='d-flex flex-row mt-4 mb-4'>" +
                            "<button class='btn btn-outline-light text-gray component-flat w-50 mr-2' type='button' "+modify_btn+">수정</button>" +
                            "<button class='btn btn-primary w-50 ml-2' type='button' "+del_btn+">삭제</button>" +
                            "</div>" +
                            "<div class='d-flex mt-5 mb-3'>" +
                            "<small class='mb-0 text-primary'>상품 리스트</small>" +
                            "</div>" +
                            "<div class='grouped-images'>" +
                            imgList+
                            "</div>" +
                            "<div class='d-flex mt-5 mb-3'>" +
                            "<small class='mb-0 text-primary'>태그</small>" +
                            "</div>" +
                            "<div class='d-flex'>" +
                            tagBox+

                            "</div>" +
                            "</div>" +
                            "</div>" +
                            "</div>");

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
}
$(document).ready(function() {
    //메뉴 하이라이트
    $('.sidebar .navigation-menu .savedvideo').addClass('active');
    $('.sidebar .navigation-menu .savedvideo #savedvideo_icon').css({'color':'#4CCEAC'});
    $('#partners_name').text($.cookie('partners_name'));


                /* 비디오 리스트 */
                videoList_ajax();

                /*통계*/
                viewCount_ajax();


});