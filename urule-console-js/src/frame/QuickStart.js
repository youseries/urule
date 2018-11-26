/**
 * Created by Jacky.gao on 2016/8/10.
 */
import React,{Component,PropTypes} from 'react';
export default class QuickStart extends Component{
    render(){
        return (
            <div style={{fontSize: '14px',fontFamily: 'Microsoft YaHei UI, Microsoft YaHei',margin:'10px',textAlign:"center"}}><h1>欢迎您使用URULE开源免费版本</h1>
                <div style={{marginTop:"20px"}}>
                    您当前正在使用的是URULE开源免费版本，Github地址为：<a href="https://github.com/youseries/urule" target="_blank">https://github.com/youseries/urule</a>。
                    <div style={{marginTop:"10px"}}>如果您需要更多功能，更完善的技术支持，可以选择<a href="http://www.bstek.com/products/urule" target="_blank"><strong>URULE PRO</strong></a>版，点击<a href="http://www.bstek.com/products/urule" target="_blank">此处</a>了解更多URULE PRO版信息</div>
                </div>
                <table className="table table-bordered" align="center" style={{marginTop:'20px',width:'750px'}}>
                    <thead>
                    <tr style={{background:'#fdfdfd',textAlign:"left",verticalAlign:'middle',fontSize:'18px',color:'#7b7a7a'}}><td colSpan="3">URULE PRO版与开源版主要功能比较</td></tr>
                    </thead>
                    <tbody>
                    <tr style={{fontSize:'14pt',background:'#98908d',color:'#fff'}}>
                        <td style={{width:'200px'}}>特性</td>
                        <td style={{width:'60px'}}>URULE PRO版</td>
                        <td style={{width:'60px'}}>URULE开源版</td>
                    </tr>
                    <tr>
                        <td>向导式决策集</td>
                        <td><i className="glyphicon glyphicon-ok" style={{fontSize:'20px',color:'green'}}></i></td>
                        <td><i className="glyphicon glyphicon-ok" style={{fontSize:'20px',color:'green'}}></i></td>
                    </tr>
                    <tr>
                        <td>脚本式决策集</td>
                        <td><i className="glyphicon glyphicon-ok" style={{fontSize:'20px',color:'green'}}></i></td>
                        <td><i className="glyphicon glyphicon-ok" style={{fontSize:'20px',color:'green'}}></i></td>
                    </tr>
                    <tr>
                        <td>决策树</td>
                        <td><i className="glyphicon glyphicon-ok" style={{fontSize:'20px',color:'green'}}></i></td>
                        <td><i className="glyphicon glyphicon-ok" style={{fontSize:'20px',color:'green'}}></i></td>
                    </tr>
                    <tr>
                        <td>决策流</td>
                        <td><i className="glyphicon glyphicon-ok" style={{fontSize:'20px',color:'green'}}></i></td>
                        <td><i className="glyphicon glyphicon-ok" style={{fontSize:'20px',color:'green'}}></i></td>
                    </tr>
                    <tr>
                        <td>决策表</td>
                        <td><i className="glyphicon glyphicon-ok" style={{fontSize:'20px',color:'green'}}></i></td>
                        <td><i className="glyphicon glyphicon-ok" style={{fontSize:'20px',color:'green'}}></i></td>
                    </tr>
                    <tr>
                        <td>交叉决策表</td>
                        <td><i className="glyphicon glyphicon-ok" style={{fontSize:'20px',color:'green'}}></i></td>
                        <td><i className="glyphicon glyphicon-remove" style={{fontSize:'20px',color:'red'}}></i></td>
                    </tr>
                    <tr>
                        <td>复杂评分卡</td>
                        <td><i className="glyphicon glyphicon-ok" style={{fontSize:'20px',color:'green'}}></i></td>
                        <td><i className="glyphicon glyphicon-remove" style={{fontSize:'20px',color:'red'}}></i></td>
                    </tr>
                    <tr>
                        <td>文件名、项目名重构</td>
                        <td><i className="glyphicon glyphicon-ok" style={{fontSize:'20px',color:'green'}}></i></td>
                        <td><i className="glyphicon glyphicon-remove" style={{fontSize:'20px',color:'red'}}></i></td>
                    </tr>
                    <tr>
                        <td>参数名、变量常量名重构</td>
                        <td><i className="glyphicon glyphicon-ok" style={{fontSize:'20px',color:'green'}}></i></td>
                        <td><i className="glyphicon glyphicon-remove" style={{fontSize:'20px',color:'red'}}></i></td>
                    </tr>
                    <tr>
                        <td>Excel决策表导入</td>
                        <td><i className="glyphicon glyphicon-ok" style={{fontSize:'20px',color:'green'}}></i></td>
                        <td><i className="glyphicon glyphicon-remove" style={{fontSize:'20px',color:'red'}}></i></td>
                    </tr>
                    <tr>
                        <td>规则集模版保存与加载</td>
                        <td><i className="glyphicon glyphicon-ok" style={{fontSize:'20px',color:'green'}}></i></td>
                        <td><i className="glyphicon glyphicon-remove" style={{fontSize:'20px',color:'red'}}></i></td>
                    </tr>
                    <tr>
                        <td>中文项目名和文件名支持</td>
                        <td><i className="glyphicon glyphicon-ok" style={{fontSize:'20px',color:'green'}}></i></td>
                        <td><i className="glyphicon glyphicon-remove" style={{fontSize:'20px',color:'red'}}></i></td>
                    </tr>
                    <tr>
                        <td>服务器推送知识包到客户端功能的支持</td>
                        <td><i className="glyphicon glyphicon-ok" style={{fontSize:'20px',color:'green'}}></i></td>
                        <td><i className="glyphicon glyphicon-remove" style={{fontSize:'20px',color:'red'}}></i></td>
                    </tr>
                    <tr>
                        <td>知识包优化与压缩的支持</td>
                        <td><i className="glyphicon glyphicon-ok" style={{fontSize:'20px',color:'green'}}></i></td>
                        <td><i className="glyphicon glyphicon-remove" style={{fontSize:'20px',color:'red'}}></i></td>
                    </tr>
                    <tr>
                        <td>客户端服务器模式下大知识包的推拉支持</td>
                        <td><i className="glyphicon glyphicon-ok" style={{fontSize:'20px',color:'green'}}></i></td>
                        <td><i className="glyphicon glyphicon-remove" style={{fontSize:'20px',color:'red'}}></i></td>
                    </tr>
                    <tr>
                        <td>规则集中执行组的支持</td>
                        <td><i className="glyphicon glyphicon-ok" style={{fontSize:'20px',color:'green'}}></i></td>
                        <td><i className="glyphicon glyphicon-remove" style={{fontSize:'20px',color:'red'}}></i></td>
                    </tr>
                    <tr>
                        <td>规则流中所有节点向导式条件与动作配置的支持</td>
                        <td><i className="glyphicon glyphicon-ok" style={{fontSize:'20px',color:'green'}}></i></td>
                        <td><i className="glyphicon glyphicon-remove" style={{fontSize:'20px',color:'red'}}></i></td>
                    </tr>
                    <tr>
                        <td>循环规则多循环单元支持</td>
                        <td><i className="glyphicon glyphicon-ok" style={{fontSize:'20px',color:'green'}}></i></td>
                        <td><i className="glyphicon glyphicon-remove" style={{fontSize:'20px',color:'red'}}></i></td>
                    </tr>
                    <tr>
                        <td>循环规则中无条件执行的支持</td>
                        <td><i className="glyphicon glyphicon-ok" style={{fontSize:'20px',color:'green'}}></i></td>
                        <td><i className="glyphicon glyphicon-remove" style={{fontSize:'20px',color:'red'}}></i></td>
                    </tr>
                    <tr>
                        <td>导入项目自动重命名功能</td>
                        <td><i className="glyphicon glyphicon-ok" style={{fontSize:'20px',color:'green'}}></i></td>
                        <td><i className="glyphicon glyphicon-remove" style={{fontSize:'20px',color:'red'}}></i></td>
                    </tr>
                    <tr>
                        <td>规则树构建优化</td>
                        <td><i className="glyphicon glyphicon-ok" style={{fontSize:'20px',color:'green'}}></i></td>
                        <td><i className="glyphicon glyphicon-remove" style={{fontSize:'20px',color:'red'}}></i></td>
                    </tr>
                    <tr>
                        <td>对象查找索引支持</td>
                        <td><i className="glyphicon glyphicon-ok" style={{fontSize:'20px',color:'green'}}></i></td>
                        <td><i className="glyphicon glyphicon-remove" style={{fontSize:'20px',color:'red'}}></i></td>
                    </tr>
                    <tr>
                        <td>规则树中短路计算的支持</td>
                        <td><i className="glyphicon glyphicon-ok" style={{fontSize:'20px',color:'green'}}></i></td>
                        <td><i className="glyphicon glyphicon-remove" style={{fontSize:'20px',color:'red'}}></i></td>
                    </tr>
                    <tr>
                        <td>规则条件冗余计算缓存支持</td>
                        <td><i className="glyphicon glyphicon-ok" style={{fontSize:'20px',color:'green'}}></i></td>
                        <td><i className="glyphicon glyphicon-remove" style={{fontSize:'20px',color:'red'}}></i></td>
                    </tr>
                    <tr>
                        <td>更为完善的文件读写权限控制</td>
                        <td><i className="glyphicon glyphicon-ok" style={{fontSize:'20px',color:'green'}}></i></td>
                        <td><i className="glyphicon glyphicon-remove" style={{fontSize:'20px',color:'red'}}></i></td>
                    </tr>
                    <tr>
                        <td>技术支持</td>
                        <td><i className="glyphicon glyphicon-ok" style={{fontSize:'20px',color:'green'}}></i></td>
                        <td><i className="glyphicon glyphicon-remove" style={{fontSize:'20px',color:'red'}}></i></td>
                    </tr>
                    </tbody>
                </table>
        </div>
        );
    }
};
