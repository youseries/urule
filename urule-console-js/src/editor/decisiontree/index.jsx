/**
 * Created by Jacky.gao on 2016/8/4.
 */
import '../../../node_modules/bootstrap/dist/css/bootstrap.css';
import '../context.standalone.css';
import '../../css/iconfont.css';
import './decision-tree.css';
import '../urule/ruleset.css';
import '../Math.uuid.js';
import '../../Remark.js';
import '../common/contextMenu.js';
import '../common/URule.js';
import '../common/ComparisonOperator.js';
import '../common/ComplexArithmetic.js';
import '../common/VariableValue.js';
import '../common/ResourceListDialog.js';
import '../common/ResourceVersionDialog.js';
import '../common/ConstantValue.js';
import '../urule/ConfigActionDialog.js';
import '../urule/ConfigConstantDialog.js';
import '../urule/ConfigParameterDialog.js';
import '../urule/ConfigVariableDialog.js';
import '../urule/ActionType.js';
import '../urule/PrintAction.js';
import '../urule/AssignmentAction.js';
import '../urule/ActionType.js';
import '../urule/SimpleArithmetic.js';
import '../common/InputType.js';
import '../common/NextType.js';
import '../common/Paren.js';
import '../common/MethodParameter.js';
import '../common/MethodAction.js';
import '../common/ParameterValue.js';
import '../common/MethodValue.js';
import '../common/FunctionProperty.js';
import '../common/FunctionParameter.js';
import '../common/FunctionValue.js';
import '../common/SimpleValue.js';
import '../urule/NamedReferenceValue.js';
import './ConditionLeft.js';
import '../urule/RuleProperty.js';


import '../common/jquery.utils.js';
import DecisionTree from './new/DecisionTree.js';
import KnowledgeTreeDialog from '../../components/dialog/component/KnowledgeTreeDialog.jsx';
import React from 'react';
import ReactDOM from 'react-dom';

$(document).ready(function () {
    ReactDOM.render(
        <KnowledgeTreeDialog/>,
        document.getElementById('dialogContainer')
    );
    new DecisionTree($('#container'));
});