/**
 * Created by Jacky.gao on 2016/6/2.
 */
import events from 'events';

export const OPEN_CREATE_PACKAGE_DIALOG='open_create_package_dialog';
export const HIDE_CREATE_PACKAGE_DIALOG='hide_create_package_dialog';
export const OPEN_CREATE_PACKAGE_ITEM_DIALOG='open_create_package_item_dialog';
export const HIDE_CREATE_PACKAGE_ITEM_DIALOG='hide_create_package_item_dialog';

export const OPEN_SIMULATOR_DIALOG='open_simulator_dialog';
export const HIDE_SIMULATOR_DIALOG='hide_simulator_dialog';

export const OPEN_RETE_DIAGRAM_DIALOG='open_rete_diagram_dialog';
export const HIDE_RETE_DIAGRAM_DIALOG='hide_rete_diagram_dialog';

export const OPEN_FLOW_DIALOG='open_flow_dialog';
export const HIDE_FLOW_DIALOG='hide_flow_dialog';

export const OPEN_IMPORT_EXCEL_DIALOG='open_import_excel_dialog';
export const HIDE_IMPORT_EXCEL_DIALOG='hide_import_excel_dialog';

export const OPEN_BATCH_TEST_DIALOG='open_batch_test_dialog';
export const HIDE_BATCH_TEST_DIALOG='hide_batch_test_dialog';

export const REFRESH_SIMULATOR_DATA='refresh_simulator_data';

export const eventEmitter = new events.EventEmitter();
