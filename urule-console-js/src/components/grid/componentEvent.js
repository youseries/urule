/**
 * Created by jacky on 2016/6/8.
 */
import events from 'events';

export const SHOW_CELL_EDITOR='show_cell_editor';
export const HIDE_CELL_EDITOR='hide_cell_editor';

export const SHOW_CHILD_LIST_DIALOG='show_child_list_dialog';
export const HIDE_CHILD_LIST_DIALOG='hide_child_list_dialog';

export const eventEmitter=new events.EventEmitter();