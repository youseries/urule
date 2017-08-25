/**
 * Created by Jacky.gao on 2016/5/27.
 */
import React,{Component,PropTypes} from 'react';
import {connect} from 'react-redux';
import CreateProjectDialog from './CreateProjectDialog.jsx';
import UpdateProjectDialog from './UpdateProjectDialog.jsx';
import CreateFileDialog from './CreateFileDialog.jsx';
import ImportProjectDialog from './ImportProjectDialog.jsx';
import CreateFolderDialog from './CreateFolderDialog.jsx';
import RenameDialog from './RenameDialog.jsx';
import SourceDialog from './SourceDialog.jsx';
import VersionListDialog from './VersionListDialog.jsx';

class ComponentContainer extends Component{
    render(){
        const dispatch=this.props.dispatch;
        return (
            <div>
                <CreateProjectDialog dispatch={dispatch}/>
                <UpdateProjectDialog dispatch={dispatch}/>
                <CreateFileDialog dispatch={dispatch}/>
                <ImportProjectDialog dispatch={dispatch}/>
                <CreateFolderDialog dispatch={dispatch}/>
                <RenameDialog dispatch={dispatch}/>
                <SourceDialog/>
                <VersionListDialog/>
            </div>
        );
    }
};
export default connect()(ComponentContainer);