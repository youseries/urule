/**
 * Created by Jacky.gao on 2016/9/21.
 */
export default class Col{
    constructor(table){
        this.scoreCardTable=table;
    }
    buildColResizeTrigger(){
        this.resizeTrigger=$(`<span class="col-resize-trigger">&nbsp;</span>`);
        return this.resizeTrigger;
    }
    getColNumber(){
        switch (this.type){
            case "attribute":
                return 1;
            case "condition":
                return 2;
            case "score":
                return 3;
        }
        const pos=this.scoreCardTable.customCols.indexOf(this);
        return pos+4;
    }

    bindColResize(){
        let resizeStart=false,resizeTargetCol,resizeStartX,resizeStartWidth;
        const _this=this;
        this.resizeTrigger.mousedown(function (e) {
            resizeTargetCol=$(this).parent();
            resizeStart=true;
            resizeStartX=e.pageX;
            resizeStartWidth=resizeTargetCol.width();
            e.preventDefault();
        });
        $(document).mousemove(function (e) {
            if(resizeStart){
                const newWidth=resizeStartWidth+(e.pageX-resizeStartX);
                _this.width=newWidth;
                resizeTargetCol.width(newWidth);
                e.preventDefault();
            }
        });
        $(document).mouseup(function (e) {
            resizeStart=false;
            window._setDirty();
        });
    }
}