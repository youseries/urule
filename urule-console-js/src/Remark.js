/**
 * Created by Jacky.gao on 2016/10/25.
 */
window.Remark=function (container) {
    this.remark="";
    this.defaultRemark="请输入备注内容";
    this.init(container);
};
Remark.prototype.init=function (container) {
    var toolbar=$("<div style='cursor:pointer;color:#777;font-size:12px'>备注</div>");
    container.append(toolbar);
    var icon=$("<i class='glyphicon glyphicon-circle-arrow-down'></i>");
    toolbar.append(icon);
    var contentContainer=$("<div class='collapse in'></div>");
    container.append(contentContainer);
    this.remarkLabel=$("<div style='color:#999;background: #fdfdfd;padding:5px;border:solid 1px #ddd;border-radius: 5px;font-size: 12px'>"+this.defaultRemark+"</div>");
    contentContainer.append(this.remarkLabel);
    this.remarkEditor=$("<textarea class='form-control' rows='4'>"+this.defaultRemark+"</textarea>");
    contentContainer.append(this.remarkEditor);
    this.remarkEditor.hide();
    var _this=this;
    this.remarkLabel.click(function () {
        _this.remarkEditor.show();
        _this.remarkEditor.focus();
        _this.remarkLabel.hide();
    });

    this.remarkEditor.change(function () {
        _this.remark=$(this).val();
        if(_this.remark===""){
            _this.remarkLabel.text(_this.defaultRemark);
        }else{
            _this.remarkLabel.html(_this.parseBreak(_this.remark));
        }
        if(window.setDirty){
            window.setDirty();
        }
        if(window._setDirty){
            window._setDirty();
        }
    });
    this.remarkEditor.blur(function () {
        _this.remarkEditor.hide();
        _this.remarkLabel.show();
    });

    toolbar.click(function () {
        contentContainer.collapse("toggle");
    });

    contentContainer.on('show.bs.collapse', function () {
        icon.removeClass("glyphicon-circle-arrow-right");
        icon.addClass("glyphicon-circle-arrow-down");
    });
    contentContainer.on('hide.bs.collapse', function () {
        icon.removeClass("glyphicon-circle-arrow-down");
        icon.addClass("glyphicon-circle-arrow-right");
    });
    contentContainer.collapse('hide');
};

Remark.prototype.setData=function (data) {
    if(!data || data===""){
        return;
    }
    this.remark=data;
    this.remarkEditor.val(data);
    this.remarkLabel.html(this.parseBreak(data));
};

Remark.prototype.toXml=function () {
    return "<remark><![CDATA["+this.remark+"]]></remark>";
};

Remark.prototype.parseBreak=function (data) {
    data=data.replace(new RegExp("<",'gm'),'&lt;');
    data=data.replace(new RegExp(">",'gm'),'&gt;');
    data=data.replace(new RegExp("\n",'gm'),'</br>');
    return data;
};
