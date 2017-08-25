/**
 * Created by jacky on 2016/7/18.
 */
import BaseTool from './BaseTool.js';
import PackageNode from './PackageNode.js';

export default class PackageTool extends BaseTool{
    getType(){
        return '知识包';
    }
    getIcon(){
        return `<i class="rf rf-package" style="color:#737383"></i>`
    }
    newNode(){
        return new PackageNode();
    }
    getConfigs(){
        return {
            out:1
        };
    }
    getPropertiesProducer(){
        const _this=this;
        return function (){
            const g=$(`<div></div>`);
            const packageIdGroup=$(`<div class="form-group"><label>知识包ID</label></div>`);
            const packageIdSelect=$(`<select class="form-control"><option></option></select>`);
            packageIdGroup.append(packageIdSelect);
            const self=this;
            packageIdSelect.change(function(){
                self.packageId=$(this).val();
            });
            if(!_this.packages){
                $.ajax({
                    url:window._server+'/ruleflowdesigner/loadPackages?project='+window._project,
                    success:function(packages){
                        _this.packages=packages;
                        for(let p of packages){
                            packageIdSelect.append(`<option>${p.id}</option>`);
                        }
                        packageIdSelect.val(self.packageId);
                    },
                    error:function(){
                        alert('加载知识包出错！');
                    }
                });
            }else{
                for(let p of _this.packages){
                    packageIdSelect.append(`<option>${p.id}</option>`);
                }
                packageIdSelect.val(this.packageId);
            }
            g.append(packageIdGroup);
            g.append(_this.getCommonProperties(this));
            return g;
        }
    }
}