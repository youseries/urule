/**
 * Created by Jacky.gao on 2016/5/27.
 */
import React,{Component,PropTypes} from 'react';
import ReactDOM from 'react-dom';

export default class CommonDialog extends Component{
    componentDidMount(){
        const $dom=$(ReactDOM.findDOMNode(this));
        $dom.on('show.bs.modal',function () {
            let zIndex=1050;
            $(document).find('.modal').each(function (index,modal) {
                const zindex=$(modal).css('z-index');
                if(zindex && zindex!=='' && !isNaN(zindex)){
                    const z=parseInt(zindex);
                    if(z>zIndex){
                        zIndex=z;
                    }
                }
            });
            $dom.css('z-index',zIndex+1);
        });
    }
    render(){
        const buttons=[];
        this.props.buttons.forEach((btn,index)=>{
            buttons.push(<button type="button" key={index} className={btn.className} onClick={(e)=>{
                btn.click(this.props.dispatch);
            }}><i className={btn.icon}></i> {btn.name}</button>)
        });
        const large=this.props.large;
        const className='modal-dialog'+ (large ? ' modal-lg' : '');
        return (
            <div className='modal fade' tabIndex="-1" role="dialog" aria-hidden="true" style={{'overflow':'auto'}}>
                <div className={className}>
                    <div className="modal-content">
                        <div className="modal-header">
                            <button type="button" className="close" data-dismiss="modal" aria-hidden="true">
                                &times;
                            </button>
                            <h3 className="modal-title" id="myModalLabel" style={{wordWrap:'break-word'}}>
                                {this.props.title}
                                <div className="text-danger" style={{fontSize:'12pt'}}>{this.props.info ? this.props.info : null}</div>
                            </h3>
                        </div>
                        <div className="modal-body" style={{padding:'10px'}}>
                            {this.props.body}
                        </div>
                        <div className="modal-footer">
                            {buttons}
                        </div>
                    </div>
                </div>
            </div>
        );
    }
};
