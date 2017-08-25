/**
 * Created by Jacky.gao on 2016/5/26.
 */
const Styles={};
Styles.frameStyle={
    getRootIcon:function () {
        return _getStyle("rootIcon","rf rf-root");
    },
    getRootIconStyle:function () {
        return _getStyle("rootIconStyle",{color:'rgb(102, 102, 102)'});
    },
    getProjectIcon:function () {
        return _getStyle("projectIcon","rf rf-project");
    },
    getProjectIconStyle:function () {
        return _getStyle("projectIconStyle",{color:'rgb(188, 15, 105)'});
    },
    getResourceIcon:function () {
        return _getStyle("resourceIcon","rf rf-library");
    },
    getResourceIconStyle:function () {
        return _getStyle("resourceIconStyle",{color:'rgb(88, 45, 170)'});
    },
    getResourcePackageIcon:function () {
        return _getStyle("resourcePackageIcon","rf rf-package");
    },
    getResourcePackageIconStyle:function () {
        return _getStyle("resourcePackageIconStyle",{color:'rgb(180, 133, 19)'});
    },
    getLibIcon:function () {
        return _getStyle("libIcon","rf rf-database");
    },
    getLibIconStyle:function () {
        return _getStyle("libIconStyle",{color:'rgb(24, 121, 27)'});
    },
    getActionIcon:function () {
        return _getStyle("actionIcon","rf rf-action");
    },
    getActionIconStyle:function () {
        return _getStyle("actionIconStyle",{color:'rgb(24, 121, 27)'});
    },
    getVariableIcon:function () {
        return _getStyle("variableIcon","rf rf-variable");
    },
    getVariableIconStyle:function () {
        return _getStyle("variableIconStyle",{color:'rgb(24, 121, 27)'});
    },
    getConstantIcon:function () {
        return _getStyle("constantIcon","rf rf-constant");
    },
    getConstantIconStyle:function () {
        return _getStyle("constantIconStyle",{color:'rgb(24, 121, 27)'});
    },
    getParameterIcon:function () {
        return _getStyle("parameterIcon","rf rf-parameter");
    },
    getParameterIconStyle:function () {
        return _getStyle("parameterIconStyle",{color:'rgb(24, 121, 27)'});
    },
    getRuleIcon:function () {
        return _getStyle("ruleIcon","rf rf-rule");
    },
    getRuleIconStyle:function () {
        return _getStyle("ruleIconStyle",{color:'rgb(31, 90, 163)'});
    },
    getUlIcon:function () {
        return _getStyle("ulIcon","rf rf-script");
    },
    getUlIconStyle:function () {
        return _getStyle("ulIconStyle",{color:'rgb(31, 90, 163)'});
    },
    getRuleLibIcon:function () {
        return _getStyle("ruleLibIcon","rf rf-rule");
    },
    getRuleLibIconStyle:function () {
        return _getStyle("ruleLibIconStyle",{color:'rgb(31, 90, 163)'});
    },
    getDecisionTableIcon:function () {
        return _getStyle("decisionTableIcon","rf rf-table");
    },
    getDecisionTableIconStyle:function () {
        return _getStyle("decisionTableIconStyle",{color:'rgb(31, 90, 163)'});
    },
    getScriptDecisionTableIcon:function () {
        return _getStyle("scriptDecisionTableIcon","rf rf-scripttable");
    },
    getScriptDecisionTableIconStyle:function () {
        return _getStyle("scriptDecisionTableIconStyle",{color:'rgb(31, 90, 163)'});
    },
    getDecisionTableLibIcon:function () {
        return _getStyle("decisionTableLibIcon","rf rf-table");
    },
    getDecisionTableLibIconStyle:function () {
        return _getStyle("decisionTableLibIconStyle",{color:'rgb(31, 90, 163)'});
    },
    getDecisionTreeIcon:function () {
        return _getStyle("decisionTreeIcon","rf rf-tree");
    },
    getDecisionTreeIconStyle:function () {
        return _getStyle("decisionTreeIconStyle",{color:'rgb(31, 90, 163)'});
    },
    getDecisionTreeLibIcon:function () {
        return _getStyle("decisionTreeLibIcon","rf rf-tree");
    },
    getDecisionTreeLibIconStyle:function () {
        return _getStyle("decisionTreeLibIconStyle",{color:'rgb(31, 90, 163)'});
    },

    getScorecardIcon:function () {
        return _getStyle("scorecardIcon","rf rf-scorecard");
    },
    getScorecardIconStyle:function () {
        return _getStyle("scorecardIconStyle",{color:'rgb(31, 90, 163)'});
    },
    getScorecardLibIcon:function () {
        return _getStyle("scorecardLibIcon","rf rf-scorecard");
    },
    getScorecardLibIconStyle:function () {
        return _getStyle("scorecardLibIconStyle",{color:'rgb(31, 90, 163)'});
    },

    getFlowIcon:function () {
        return _getStyle("flowIcon","rf rf-flow");
    },
    getFlowIconStyle:function () {
        return _getStyle("flowIconStyle",{color:'rgb(31, 90, 163)'});
    },
    getFlowLibIcon:function () {
        return _getStyle("flowLibIcon","rf rf-flow");
    },
    getFlowLibIconStyle:function () {
        return _getStyle("flowLibIconStyle",{color:'rgb(31, 90, 163)'});
    },
    getFolderIcon:function () {
        return _getStyle("flowIcon","rf rf-folder");
    },
    getFolderIconStyle:function () {
        return _getStyle("flowIconStyle",{color:'rgb(224, 126, 1)'});
    },
};
function _getStyle(styleName,defaultValue) {
    if(window._frameStyles){
        return window._frameStyles[styleName];
    }else{
        return defaultValue;
    }
}
export default Styles;