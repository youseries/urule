/**
 * Created by Jacky.gao on 2016/7/27.
 */

window.iframe_id_=1;

export function nextIFrameId(){
    window.iframe_id_++;
    return '_iframe'+window.iframe_id_;
};

export function getParameter(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null)return r[2];
    return null;
};

export function ajaxSave(url,parameters,callback) {
    $.ajax({
        type:'POST',
        url,
        data:parameters,
        success:function (result) {
            callback(result);
        },
        error:function (response) {
            if(response && response.status===401){
                bootbox.alert("权限不足，不能进行此操作.");
            }else{
                if(response && response.responseText){
                    bootbox.alert("<span style='color: red'>服务端错误："+response.responseText+"</span>");
                }else{
                    bootbox.alert("<span style='color: red'>服务端出错</span>");
                }
            }
        }
    });
}

export function formatDate(date,format){
    if(typeof date === 'number'){
        date=new Date(date);
    }
    if(typeof date==='string'){
        return date;
    }
    var o = {
        "M+" : date.getMonth()+1,
        "d+" : date.getDate(),
        "H+" : date.getHours(),
        "m+" : date.getMinutes(),
        "s+" : date.getSeconds()
    };
    if(/(y+)/.test(format))
        format=format.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(format))
            format = format.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return format;
};