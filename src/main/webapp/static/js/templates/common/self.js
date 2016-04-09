$(function(){
    $(".main_nav .nav-pills li").mouseover(function(){
        var id=$(this).attr("data-target");
        if(id!=undefined){
            $(id).fadeIn('fast').siblings(".sec_nav").css('display','none');    //如果获取的id不为空，则让这个id对应的二级菜单显示
        }else{
            $(".sec_nav").each(function(){
                $(this).css('display','none');     //若对应的id为空，则不显示
            });
        }
    });
    var delay;
    $(".main_nav").mouseover(function(){
        clearTimeout(delay);
    });
    $(".main_nav").mouseleave(function(){
        delay=setTimeout(function(){
            delay=$(".main_nav .sec_nav").fadeOut();    //当鼠标移出导航区域，导航隐藏
        },1000);
    });
});

// ajax返回失败后统一处理,暂时phone端
function ajaxErrorCallBack(data, url) {
    if(data.data) {
        data = data.data;

        if(data.code == 'login_time_out') {
            location.href = data.url;
        }
    } else {
        alert(data.msg);
    }
}