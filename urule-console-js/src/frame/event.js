/**
 * Created by Jacky.gao on 2016/5/24.
 */
import events from 'events';
export const OPEN_DIALOG='open_dialog';
export const CLOSE_DIALOG='close_dialog';
export const DIALOG_CONTNET_CHANGE='dialog_content_change';
export const OPEN_NEW_PROJECT_DIALOG='open_new_project_dialog';
export const CLOSE_NEW_PROJECT_DIALOG='close_new_project_dialog';
export const OPEN_UPDATE_PROJECT_DIALOG='open_update_project_dialog';
export const CLOSE_UPDATE_PROJECT_DIALOG='close_update_project_dialog';
export const OPEN_CREATE_FILE_DIALOG='create_file_dialog';
export const CLOSE_CREATE_FILE_DIALOG='close_file_dialog';
export const OPEN_CREATE_FOLDER_DIALOG='create_folder_dialog';
export const CLOSE_CREATE_FOLDER_DIALOG='close_folder_dialog';
export const EXPAND_TREE_NODE='expand_tree_node';
export const SHOW_RENAME_DIALOG='SHOW_RENAME_DIALOG';
export const HIDE_RENAME_DIALOG='HIDE_RENAME_DIALOG';

export const PROJECT_LIST_CHANGE='project_list_change';
export const PROJECT_FILTER_CHANGE='project_filter_change';

export const OPEN_IMPORT_PROJECT_DIALOG='open_import_project_dialog';
export const CLOSE_IMPORT_PROJECT_DIALOG='close_import_project_dialog';

export const OPEN_SOURCE_DIALOG='open_source_dialog';
export const CLOSE_SOURCE_DIALOG='close_source_dialog';

export const OPEN_FILE_VERSION_DIALOG='open_file_version_dialog';
export const CLOSE_FILE_VERSION_DIALOG='close_file_version_dialog';

export const CHANGE_CLASSIFY='change_classify';

export const eventEmitter=new events.EventEmitter();