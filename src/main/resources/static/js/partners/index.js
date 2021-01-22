//비디오 소스
var v_src='';

var canvas = new fabric.Canvas('c');
var sv = document.getElementById("player");

var rect, isDown, origX, origY;
var cWidth = 0;
var rectCount = -1;

var ext = 0;

var showBtn = 0;
var element = document.getElementById('body');


var modify_btn = 1;


var rWidth = 0;
var rHeight =0;
var rTop=0;
var rLeft=0;

var chk_product=0;

var p_i_src='';
var p_id='';
var p_name='';
var p_price='';
var p_link='';

var i_src='';

var arrayProduct=new Array();

var startTime = 0;
var endTime = 0;
var totalTime=0;

var rect='';

var cnt=1;

//preview image
var imgTarget = $('.preview-image .upload-hidden');


function delete_product(id,cnt) {
    let classNum = ".product_list"+cnt;
    $("#show_product_list "+classNum).remove();
}



function final_btn(){
    //arrayProduct json화

    //태그
    var tag = $('#tags').split(' ');


    //비디오 등록 ajax
    var form = {
        "videoName": $('#video_title').val(),
        "tagList": tag,
        "videoURL": v_src,
        "thumbnailImageURL": i_src,
        "productList": arrayProduct
    };
    console.log(arrayProduct);

    $.ajax({
        type: "post",
        url: "3.35.8.74/video/save",
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(form),
        success: function(res) {
            if (res.code == 200) {

            } else {
                alert('상품정보 등록에 오류가 생겼습니다');
            }
        },error: function() {
            alert('상품정보 등록 오류');
        }
    });

    alert('등록이 완료되었습니다');
}

function save_product() {
    //startTime = 0;
    //endTime = 0;
    var s_time = $('#s_time').val();
    var e_time = $('#e_time').val();
    if ((s_time > e_time) || s_time == '' || e_time == '') {
        alert('시작 시점과 종료 시점을 다시 입력해주세요');
        return false;
    }
    //저장일경우
    $('#save_product').css({ 'display': 'none' });
    $('#modify_product').css({ 'display': 'inline-block' });
    $('#s_time').attr("readonly", true);
    $('#e_time').attr("readonly", true);
    $('#s_time').css({ 'border': "1px solid white" });
    $('#e_time').css({ 'border': "1px solid white" });

}


function showPop(){
    $('#staticBackdrop').addClass('show');
    $('#staticBackdrop').css({'display':'block'});
}

function closePop(){
    $('#staticBackdrop').removeClass('show');
    $('#staticBackdrop').css({'display':'none'});
}


$(document).ready(function () {
    $('.sidebar .navigation-menu .uploadvideo').addClass('active');
    $('.sidebar .navigation-menu .uploadvideo #uploadvideo_icon').css({'color':'#4CCEAC'});

    $("#cloud-tags").prettyTag({
        randomColor:true
    });


    $("#demo-upload").dropzone({
        sending: function (file) {
            //영상 제목, 관련태그, 이미지 입력 판별
            var video_title = $('#video_title').val();
            var video_tag = $('#video_tag').val();

            //확장자 판별
            ext = file.name.split('.').pop().toLowerCase();
            if ($.inArray(ext, ['mp4', 'ogg', 'avi', 'm4v', 'WebM']) == -1) {
                alert(file.name + ' 파일은 영상이 아니므로 업로드 하실 수 없습니다.');

                //영상이 아닐경우 기존에 업로드 했던 파일 날리기
                this.on("complete", function (file) {
                    this.removeAllFiles(true);
                });

                return false;
            } else {
                //드롭존 박스 없애고 비디오 박스 보여주기(+로딩)
                var video = document.getElementById('player');
                var source = document.createElement('source');
                var formData = new FormData();
                formData.append("file", file);
                console.log("upload start");
                $(".dropzone_box2").remove();
                $(".blackScreen").css({'display': 'block'});

                $.ajax({
                    url: "/video/upload",
                    type: "post",
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (data) {
                        if (data.code == 200) {
                            $(".blackScreen").css({'display': 'none'});
                            v_src = data.data;
                            source.setAttribute('src', v_src);
                            video.appendChild(source);
                            video.play();
                            $("#drop_after").css({ 'display': 'block' });
                        } else {
                            console.log(data.message);
                        }
                    },
                    err: function () {
                        $(".blackScreen").css({'display': 'none'});
                        v_src = "http://jjeun.com/v-link/partner/lotte_test2.mp4";
                        source.setAttribute('src', v_src);
                        video.appendChild(source);
                        video.play();
                        $("#drop_after").css({ 'display': 'block' });
                        alert('업로드 실패');
                    }
                });
            }
        },
        success: function (file, response) {
        }
    });

    $("#demo-upload2").dropzone({
        sending: function (file) {

            //확장자 판별
            ext = file.name.split('.').pop().toLowerCase();
            if ($.inArray(ext, ['jpg','jpeg','png','tiff']) == -1) {
                alert(file.name + ' 파일은 이미지가 아니므로 업로드 하실 수 없습니다.');

                //영상이 아닐경우 기존에 업로드 했던 파일 날리기
                this.on("complete", function (file) {
                    this.removeAllFiles(true);
                });

                return false;
            } else {
                i_src=file.name;
                this.removeFile(file);

            }
        },
        success: function (file, response) {
           console.log("i_src 업로드 완료");
        }
    });

    canvas.setWidth(sv.clientWidth);
    canvas.setHeight(sv.clientHeight);

    $('.canvas-container').css({ 'position': 'absolute' });

    $('.actions .js-play').css({ 'display': 'block' });
    $('.actions .js-complete').css({ 'display': 'none' });

    /*이미지 업로드*/
    var fileTarget = $('.file-upload .upload-hidden');

    fileTarget.on('change', function () {  // 값이 변경되면
        if (window.FileReader) {  // modern browser
            var filename = $(this)[0].files[0].name;
        }
        else {  // old IE
            var filename = $(this).val().split('/').pop().split('\\').pop();  // 파일명만 추출
        }

        // 추출한 파일명 삽입
        $(this).siblings('.upload-name').val(filename);
    });
});


imgTarget.on('change', function () {
    var parent = $(this).parent();
    parent.children('.upload-display').remove();

    if (window.FileReader) {
        //image 파일만
        if (!$(this)[0].files[0].type.match(/image\//)) return;

        var reader = new FileReader();
        reader.onload = function (e) {
            var src = e.target.result;
            parent.append('<div class="upload-display"><div class="upload-thumb-wrap"><img src="' + src + '" class="upload-thumb"></div></div>');
        }
        reader.readAsDataURL($(this)[0].files[0]);
    }

    else {
        $(this)[0].select();
        $(this)[0].blur();
        var imgSrc = document.selection.createRange().text;
        parent.prepend('<div class="upload-display"><div class="upload-thumb-wrap"><img class="upload-thumb" alt="이미지 썸네일"></div></div>');

        var img = $(this).siblings('.upload-display').find('img');
        img[0].style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(enable='true',sizingMethod='scale',src=\"" + imgSrc + "\")";
    }
});






canvas.on('mouse:down', function (o) {
    isDown = true;
    var pointer = canvas.getPointer(o.e);
    origX = pointer.x;
    origY = pointer.y;
    var pointer = canvas.getPointer(o.e);
    rect = new fabric.Rect({
        left: origX,
        top: origY,
        originX: 'left',
        originY: 'top',
        width: pointer.x - origX,
        height: pointer.y - origY,
        angle: 0,
        selectable: false,
        fill: 'rgba(255,0,0,0.5)',
        transparentCorners: false
    });

    var pointer = canvas.getPointer(o.e);

    if (rectCount == 0) {
        canvas.add(rect);
        rectCount++;
    }
});

canvas.on('mouse:move', function (o) {

    if (!isDown) return;
    var pointer = canvas.getPointer(o.e);

    if (origX > pointer.x) {
        rect.set({ left: Math.abs(pointer.x) });
    }
    if (origY > pointer.y) {
        rect.set({ top: Math.abs(pointer.y) });
    }

    rect.set({ width: Math.abs(origX - pointer.x) });
    rect.set({ height: Math.abs(origY - pointer.y) });

    canvas.renderAll();
});

canvas.on('mouse:up', function (o) {
    isDown = false;
    //px 기준
    /*
    console.log("left : "+Math.ceil(origX));
    console.log("top : "+Math.ceil(origY));
    console.log("width : "+Math.ceil(rect.width));
    console.log("height : "+Math.ceil(rect.height));
    */
    //px을 백분율로 환산 : (반응형 너비/전체 너비) * 100

    /*
    console.log("----------------------------------------------------");
    console.log("width : "+Math.ceil(rect.width/sv.clientWidth*100)+"%");
    console.log("height : "+Math.ceil(rect.height/sv.clientHeight*100)+"%");
    console.log("top : "+ Math.ceil(origY/sv.clientHeight*100)+"%");
    console.log("left : "+Math.ceil(origX/sv.clientWidth*100)+"%");
    */
});

function requestProductDetect(data) {
    var formData = new FormData();
    formData.append("base64", data)

    var result;

    //비디오 업로드 ajax
    $.ajax({
        url: "/product/detect",
        type: "post",
        data: formData,
        processData: false,
        contentType: false,
        success: function (data) {
            result = data;
        }
    })
    return result;
}

function printTime(player) {
    console.log(player.currentTime());
}

document.addEventListener('DOMContentLoaded', () => {
    const player = new Plyr('#player', {
        controls: ['play-large', 'play', 'progress', 'current-time', 'mute', 'volume', 'captions', 'settings', 'pip', 'airplay', 'fullscreen', 'capture'],
        settings: ['captions', 'quality', 'loop'],
        loadSprite: true,
        autoplay: true,
        muted: true,
    });

    window.player = player;

    function on(selector, type, callback) {
        document.querySelector(selector).addEventListener(type, callback, false);
    }

    // 1. 시작 - 시간 설정
    on('.js-play', 'click', () => {
        totalTime = $('#player').duration;
        canvas.setWidth(sv.clientWidth);
        canvas.setHeight(sv.clientHeight);
        sv.pause();
        startTime = 0;
        endTime = 0;
        rectCount = 0;
        var activeObject = canvas.getActiveObject();
        //canvas.remove(rect);
        $('#getTime').css({ 'display': 'block' });
        $('.js-play').css({ 'display': 'none' });
    });


    on('.js-start', 'click', () => {
        startTime = Math.ceil(player.currentTime);
        $('div#startTime').html("<span style='line-height:43px;'>" + startTime + "초</span>");
        $('#product_name').val('');
        $('#p_info_list').empty();
    });

    on('.js-end', 'click', () => {
        endTime = Math.ceil(player.currentTime);
        //alert('종료 : '+endTime);
        $('div#endTime').html("<span style='line-height:43px;'>" + endTime + "초</span>");

    });

    // 2. 선택 - 객체 인식할건지 직접선택할건지 선택
    on('.js-timeBtn', 'click', () => {
        sv.pause();
        if (!startTime || !endTime) {
            alert('시간을 선택해주세요');
            return false;
        }

        if (startTime > endTime) {
            startTime = 0;
            endTime = 0;
            alert('시작 시점과 종료 시점을 다시 선택해주세요');
            $('div#startTime').html("<span style='line-height:43px;'></span>");
            $('div#endTime').html("<span style='line-height:43px;'></span>");
            return false;
        }
        var chk = confirm('상품 구간을 확정하시겠습니까?');
        if (chk && startTime && endTime) {
            $('#getTime').css({ 'display': 'none' });
            $('#getSelect').css({ 'display': 'block' });
        }
        rLeft=0;
        rHeight = 0;
        rTop = 0;
        rWidth = 0;
        //$('#c').attr("width", "0");
        //$('#c').attr("height", "0");
    });



    //3. 자동인식
    on('.js-auto', 'click', () => {
        //시작점에서 멈추기
        sv.currentTime = startTime;
        sv.pause();

        const width = player.media.videoWidth;
        const height = player.media.videoHeight;
        const canvas = Object.assign(document.createElement('canvas'), { width, height });
        const canvasCtx = canvas.getContext('2d');

        const img = new Image();
        img.setAttribute('crossOrigin', 'anonymous');

        img.onload = (function onload() {
            canvasCtx.drawImage(player.media, 0, 0, width, height);
            img.src = canvas.toDataURL('image/png').replace('image/png', 'image/octet-stream');
            const screenShotImg = img.src.replace(/^data:image\/[^;]+/, 'data:application/octet-stream');
            var detect_data = requestProductDetect(screenShotImg);
            console.log(detect_data)
        }());
    });

    $(window).resize(function () {
        canvas.setWidth(sv.clientWidth);
        canvas.setHeight(sv.clientHeight);
    });
    // 직접선택
    on('.js-hand', 'click', () => {
        sv.pause();
        sv.currentTime = startTime;

        $('.upper-canvas').css({ 'display': 'block' });
        $('#c').css({ 'display': 'block' });
        $('.canvas-container').css({ 'display': 'block' });


        $('#getSelect').css({ 'display': 'none' });
        $('#getSelect2').css({ 'display': 'block' });
        //캔버스 보이기
        rectCount = 0;
        $('.upper-canvas').css({ 'z-index': '9999' });
        $('#c').css({ 'z-index': '9999' });
        $('.canvas-container').css({ 'z-index': '9999' });
    });

    // 직접선택 - 위치 취소
    on('.js-cancel', 'click', () => {
        rectCount = 0;
        var activeObject = canvas.getActiveObject();
        canvas.remove(rect);
    });

    //위치등록 - 서버로 객체 위치 보내기
    on('.js-position', 'click', () => {
        if (rectCount == 0) {
            alert('위치를 지정하세요');
            return false;
        }
        $('.upper-canvas').css({ 'display': 'none' });
        $('#c').css({ 'display': 'none' });
        $('.canvas-container').css({ 'display': 'none' });

        alert('위치가 등록되었습니다.');
        //console.log("width : "+Math.ceil(rect.width/sv.clientWidth*100)+"%");
        //console.log("height : "+Math.ceil(rect.height/sv.clientHeight*100)+"%");
        //console.log("top : "+ Math.ceil(origY/sv.clientHeight*100)+"%");
        //console.log("left : "+Math.ceil(origX/sv.clientWidth*100)+"%");

        rWidth = Math.ceil(rect.width / sv.clientWidth * 100) + "%";
        rHeight = Math.ceil(rect.height / sv.clientHeight * 100) + "%";
        rTop = Math.ceil(origY / sv.clientHeight * 100) + "%";
        rLeft = Math.ceil(origX / sv.clientWidth * 100) + "%";

        $('#getSelect2').css({ 'display': 'none' });
        //제품 검색
        $(".searchPop").css({ 'display': 'block' });

        $('.js-complete').css({ 'display': 'block' });

        showBtn = 1;
    });


    //완료 - 상품 지정
    on('.js-complete', 'click', () => {
       //등록 상품 js 추가
        //등록 상품 append (index 붙여서)
        $('.js-complete').css({ 'display': 'none' });
        $('.js-play').css({ 'display': 'block' });
        $('#product_main').css({ 'display': 'block' });
    });
});


function selectProduct(){
    var chk = $("input[name='product']:checked").val();
    var option = "#product_id" + chk;
    p_i_src = $(option + " .image_box img").attr('src');
    p_id = $(option + " .p_id b b").text();
    p_name = $(option + " .product .title b").text();
    p_price = $(option + " .product .price b b").text();
    p_link = $(option + " a").attr('href');


    console.log(p_i_src);
    $('#none_product').css({'display':'none'});
    $('#final_btn').css({'display':'block'});
        $('#show_product_list').append("<tr class='product_list"+cnt+"'>"+
            "<td>"+
                "<div class='display-avatar animated-avatar'>"+
                    "<a class='product_info_link' id='product_info_link"+cnt+"' href='"+p_link+"' target='_blank'>"+
                        "<img class='profile-img img-lg rounded-circle' id='product_info_img"+cnt+"' class='profile-img img-lg rounded-circle' src='"+p_i_src+"' alt='profile image'></a>"+
                "</div>"+
            "</td>"+
            "<td class='font-weight-medium'>"+
                "<div><span class='product_info_id' id='product_info_id"+cnt+"' style='display:none;'>"+p_id+"</span></div>"+
                "<div class='product_info_title' id='product_info_title"+cnt+"'>"+p_name+"</div>"+
                "<div class='product_info_price' id='product_info_price"+cnt+"'>"+p_price+"<b>원</b></div>"+
            "</td>"+
        "</tr>"+
    "<tr class='product_list"+cnt+"'>"+
        "<td colSpan='2'>"+
            "<div class='show_time'>"+
                "<div> 시간 <b id='s_time"+cnt+"'>"+startTime+"</b> ~ <b id='e_time"+cnt+"'>"+endTime+"</b>/"+parseInt(sv.duration)+"초</div>"+
                "<div class='form-group mb-0'>"+
                    "<p>"+
                        "<input type='text' readOnly style='border:0; color:#f6931f; font-weight:bold;'>"+
                    "</p>"+
                    "<div class='slider-range' id='slider-range"+cnt+"'>"+
                        "<script>"+
                            "$(function() {"+
                            "$('#slider-range"+cnt+"').slider({"+
                                "range: true,"+
                                "min: 0,"+
                                "max:"+ parseInt(sv.duration)+","+
                                "values: ["+startTime+","+ endTime+"],"+
                                "slide: function (event, ui) {"+
                                    "$('#s_time"+cnt+"').text(ui.values[0]);"+
                                    "$('#e_time"+cnt+"').text(ui.values[1]);"+
                                "}"+
                            "});"+
                        "});"+
                        "</script>"+
                    "</div>"+
                "</div>"+
            "</div>"+
            "<div class='timeline_box'>"+
                "<button class='btn btn-outline-light text-gray component-flat w-33 ml-2' onClick=delete_product('"+p_id+"','"+cnt+"')>삭제</button>"+
            "</div>"+
        "</td>"+
    "</tr>");

    cnt++;
    $('#staticBackdrop').removeClass('show');
    $('#staticBackdrop').css({'display':'none'});
    alert('상품이 등록되었습니다');

    arrayProduct = {
        "startTime": startTime,
        "endTime": endTime,
        "left": rLeft,
        "top": rTop,
        "width": rWidth,
        "height": rHeight,
        "product":
            {
            "productName": p_name,
            "productId": p_id,
            "productImageURL": p_i_src,
            "price": p_price,
            "productURL": p_link
        }
    }
    console.log("array : "+arrayProduct[0]);
    var obj_s = JSON.stringify(arrayProduct);
    console.log(obj_s.product);





    //모든 상품 관련 변수 초기화 + 시작지점 , 종료지점 html 없애기
    $('#startTime').html("");
    $('#endTime').html("");
}

function searchBtn() {
    var productName = $("#product_name").val();
    $('#s_keyword').text(productName);
    var data = { "productName": productName }
    $('#keyword_box').css({'display':'block'});
    $.ajax({
        type: "post",
        url: "/product/search-himart",
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(data),
        success: function (res) {
           if (res.code == 200) {
                for (var i = 0; i < Object.keys(res.data).length; i++) {
                    $('#p_info_list').append("<div id='product_id" + (i + 1) + "'><a href='" + res.data[i].productURL + "' target='_blank'><div class='p_info'><div class='image_box'><img src='" + res.data[i].productImageURL + "' style='width:100%;'/></div><div class='product'><div class='title'><b>" + res.data[i].productName + "<b></div><div class='brand'></div><div class='price'>" + res.data[i].price + "</div></div><div class='chk_box'><input type='radio' id='option" + (i + 1) + "' name='product' value='" + (i + 1) + "'><label for='option" + (i + 1) + "'>상품" + (i + 1) + "</label></div></div><div class='p_id' style='clear:both;opacity:0;'>" + res.data[i].productId + "</div></a></div>");
                }
            } else {
                alert('실패');
            }
        },
        error: function () {
            alert('검색 결과가 없습니다.');
        }
    });
}

$('#player').bind('timeupdate', function(){
    if(this.currentTime > 1 && this.currentTime < 3) {
        console.log("it works!");
    }
});
