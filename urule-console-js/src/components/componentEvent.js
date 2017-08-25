/**
 * Created by jacky on 2016/6/18.
 */
import events from 'events';
export const SHOW_LOADING='show_loading';
export const HIDE_LOADING='hide_loading';
export const TREE_NODE_CLICK='tree_node_click';
export const TREE_DIR_NODE_CLICK='tree_dir_node_click';
export const OPEN_KNOWLEDGE_TREE_DIALOG='open_knowledge_tree_dialog';
export const HIDE_KNOWLEDGE_TREE_DIALOG='hide_knowledge_tree_dialog';
export const OPEN_VERSION_SELECT_DIALOG='open_version_select_dialog';
export const HIDE_VERSION_SELECT_DIALOG='hide_version_select_dialog';

export const eventEmitter=new events.EventEmitter();

