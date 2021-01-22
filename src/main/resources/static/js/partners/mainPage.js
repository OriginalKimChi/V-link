var imgList='';
var playTime=0;
var pr_cnt=1;

function del_video(id){
    /*비디오 삭제*/
    var video_id = {
        "id": id
    };
    $.ajax({
        type: "delete",
        url: "/video/delete",
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(video_id),
        success: function(res) {
            if (res.code == 200) {
                alert('삭제되었습니다');
                location.reload();
            } else {
                //alert('실패');
            }
        },
        error: function() {
            //alert('검색 결과가 없습니다.');
        }
    });
}

function modify_video(id){
    //비디오 수정
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

function show_Chart(){

    var data = {
        labels: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"],
        datasets: [
            {
                data: [10,20,30 ],
                backgroundColor: chartColors,
                borderColor: chartColors,
                borderWidth: chartColors,
                label: "Dataset 1"
            }
        ], labels: [
            'Data 1',
            'Data 2',
            'Data 3',
        ]
    };

    var ctx1 = $('#myChart1').get(0).getContext("2d");
    window.theChart8 = new Chart(ctx1, {
        type: 'pie',
        data: data,
        options: {
            responsive: false,
            legend:{
                position: 'bottom',
                labels:{
                    pointStyle:'circle',
                    usePointStyle:true,
                    fontColor: "black"
                }
            }
        }
    });

    var data1 = {
        labels: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"],
        datasets: [
            {
                data: [10,20,30 ],
                backgroundColor: chartColors,
                borderColor: chartColors,
                borderWidth: chartColors,
                label: "Dataset 1"
            }
        ], labels: [
            'Data 1',
            'Data 2',
            'Data 3',
        ]
    };

    var ctx2 = $('#myChart2').get(0).getContext("2d");
    window.theChart8 = new Chart(ctx2, {
        type: 'pie',
        data: data1,
        options: {
            responsive: false,
            legend:{
                position: 'bottom',
                labels:{
                    pointStyle:'circle',
                    usePointStyle:true,
                    fontColor: "black"
                }
            }
        }
    });

    var data2 = {
        labels: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"],
        datasets: [
            {
                data: [10,20,30 ],
                backgroundColor: chartColors,
                borderColor: chartColors,
                borderWidth: chartColors,
                label: "Dataset 1"
            }
        ], labels: [
            'Data 1',
            'Data 2',
            'Data 3',
        ]
    };

    var ctx3 = $('#myChart3').get(0).getContext("2d");
    window.theChart8 = new Chart(ctx3, {
        type: 'pie',
        data: data2,
        options: {
            responsive: false,
            legend:{
                position: 'bottom',
                labels:{
                    pointStyle:'circle',
                    usePointStyle:true,
                    fontColor: "black"
                }
            }
        }
    });

    var data3 = {
        labels: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"],
        datasets: [
            {
                data: [10,20,30 ],
                backgroundColor: chartColors,
                borderColor: chartColors,
                borderWidth: chartColors,
                label: "Dataset 1"
            }
        ], labels: [
            'Data 1',
            'Data 2',
            'Data 3',
        ]
    };

    var ctx4 = $('#myChart4').get(0).getContext("2d");
    window.theChart8 = new Chart(ctx4, {
        type: 'pie',
        data: data3,
        options: {
            responsive: false,
            legend:{
                position: 'bottom',
                labels:{
                    pointStyle:'circle',
                    usePointStyle:true,
                    fontColor: "black"
                }
            }
        }
    });
}

function myVideo_Ajax(){
    var page = 1;
    /*내 비디오 ajax*/
    var data2 = {
        "page": page
    };


    $.ajax({
        type: "get",
        url: "/partners/top-video",
        success: function (res) {
            if (res.code == 200) {
                for (var i = 0; i < Object.keys(res.data).length; i++) {
                   /* for (var j = 0; j < res.data.length; j++) {
                        if(j<4)
                            imgList += "<img class='img-sm' src='"+res.data[i].productList.product.productImageURL+"' alt='Image' data-toggle='tooltip' title='Image'>";
                        pr_cnt++;
                    }*/
                   /* if(res.data.playTime=='null'){
                        playTime = "0:00";
                    }else{
                        playTime = res.data.playTime[i];
                    }*/
                    $('#top_box').append(" <div id='top_video'"+i+" class='vertical-timeline-wrapper'>"+
                        "<div class='col-lg-12 col-md-12 col-sm-12 equel-grid'>"+
                        "<div class='grid'>"+
                        "<div class='grid-body'>"+
                        "<div class='split-header'>"+
                        "<div class='img' style='position:relative;'>"+
                        "<img src='"+res.data[i].thumbnailImageURL+"' style='width:100%;'/>"+
                        "</div>"+
                        "</div>"+
                        "<div class='d-flex align-items-end mt-2'>"+
                        "<h6>"+res.data[i].videoName+"</h6>"+
                        "</div>"+
                        "<div class='d-flex mt-2'>"+
                        "<div class='wrapper d-flex pr-4'>"+
                        "<div class='text-success font-weight-medium mr-2'> <i class='far fa-file-video'></i></div>"+
                        "<div class='text-gray'>"+res.data[i].viewCount+"</div>"+
                        "</div>"+
                        "<div class='wrapper d-flex pr-4'>"+
                        "<div class='text-primary font-weight-medium mr-2'><i class='far fa-heart'></i></div>"+
                        "<div class='text-gray'>"+res.data[i].likeCount+"</div>"+
                        "</div>"+
                        "<div class='wrapper d-flex'>"+
                        "<div class='text-primary font-weight-medium mr-2'><i class='fas fa-eye'></i></div>"+
                        "<div class='text-gray'>"+res.data[i].replyCount+"</div>"+
                        "</div>"+
                        "</div>"+
                        "<div class='d-flex flex-row mt-4 mb-4'>"+
                        "<button class='btn btn-outline-light text-gray component-flat w-50 mr-2' onclick=modify_video('"+res.data[i].id+"') type='button'>수정</button>"+
                        "<button class='btn btn-primary w-50 ml-2' onclick=del_video('"+res.data[i].id+"') type='button'>삭제</button>"+
                        "</div>"+
                        "</div>"+
                        "</div>"+
                        "</div>"+
                        "</div>");
                }


            } else {
                //alert('실패');
            }
        },
        error: function () {
            //alert('검색 결과가 없습니다.');
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
}}

function topVideo_ajax(){
    $.ajax({
        type: "get",
        url: "/partners/top-video",
        success: function(res) {
            if (res.code == 200) {
                for (var i = 0; i < Object.keys(res.data).length; i++) {
                    console.log(res.data);
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
                        "<span>"+changeClock(res.data[i].playTime)+"</span>" +
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
                //alert('실패');
            }
        },
        error: function() {
            //alert('검색 결과가 없습니다.');
        }
    });

}

function show_product(url){
    $("a[href^='"+url+"']").attr('target','_blank');
}

function topProduct_Ajax(){
    /*상위 상품 ajax*/
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
                    $('#product_list').append(" <tr>" +
                        "<td class='pr-0 pl-4'>" +
                        "<img class='profile-img img-sm' src='"+res.data[keys].productImageURL+"' alt='product image'>" +
                        "</td>" +
                        "<td class='pl-md-0'>" +
                        "<small class='product_name text-black font-weight-medium d-block'>"+res.data[keys].productName+"</small>" +
                        "<span class='text-gray'>" +
                        "<span class='status-indicator rounded-indicator small bg-primary'></span>"+res.data[keys].price+"</span>" +
                        "</td>" +
                        "<td style='color:white;'>" +
                        "<a href='"+p_link+"' target='_blank'>"+
                        "<div class='btn btn-primary'>보기</div></a>" +
                        +"</td>" +
                        "</tr>");

                }


            } else {
                //alert('실패');
            }
        }, error: function () {
            //alert('검색 결과가 없습니다.');
        }
    });
}
$(document).ready(function() {
    //메뉴 하이라이트
    $('.sidebar .navigation-menu .home').addClass('active');
    $('.sidebar .navigation-menu .home #home_icon').css({'color':'#4CCEAC'});
    $('.dz-button').html("<i class='fas fa-camera'></i>");
    $('#partners_name').text($.cookie('partners_name'));

    show_Chart();

   /* myVideo_Ajax();*/
    topVideo_ajax();
    topProduct_Ajax();

});









