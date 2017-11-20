// Generated from RuleParser.g4 by ANTLR 4.5.3
package com.bstek.urule.dsl;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link RuleParserParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface RuleParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#ruleSet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleSet(RuleParserParser.RuleSetContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#ruleSetHeader}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleSetHeader(RuleParserParser.RuleSetHeaderContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#ruleSetBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleSetBody(RuleParserParser.RuleSetBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#rules}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRules(RuleParserParser.RulesContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#functionImport}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionImport(RuleParserParser.FunctionImportContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#packageDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackageDef(RuleParserParser.PackageDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#resource}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResource(RuleParserParser.ResourceContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#importParameterLibrary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportParameterLibrary(RuleParserParser.ImportParameterLibraryContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#importVariableLibrary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportVariableLibrary(RuleParserParser.ImportVariableLibraryContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#importConstantLibrary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportConstantLibrary(RuleParserParser.ImportConstantLibraryContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#importActionLibrary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportActionLibrary(RuleParserParser.ImportActionLibraryContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#functionDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDef(RuleParserParser.FunctionDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#functionParameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionParameters(RuleParserParser.FunctionParametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#functionParameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionParameter(RuleParserParser.FunctionParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#ruleDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleDef(RuleParserParser.RuleDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#loopRuleDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoopRuleDef(RuleParserParser.LoopRuleDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#loopTarget}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoopTarget(RuleParserParser.LoopTargetContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#loopStart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoopStart(RuleParserParser.LoopStartContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#loopEnd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoopEnd(RuleParserParser.LoopEndContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#attribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribute(RuleParserParser.AttributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#loopAttribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoopAttribute(RuleParserParser.LoopAttributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#salienceAttribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSalienceAttribute(RuleParserParser.SalienceAttributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#effectiveDateAttribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEffectiveDateAttribute(RuleParserParser.EffectiveDateAttributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#expiresDateAttribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpiresDateAttribute(RuleParserParser.ExpiresDateAttributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#enabledAttribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnabledAttribute(RuleParserParser.EnabledAttributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#debugAttribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDebugAttribute(RuleParserParser.DebugAttributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#activationGroupAttribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActivationGroupAttribute(RuleParserParser.ActivationGroupAttributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#agendaGroupAttribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAgendaGroupAttribute(RuleParserParser.AgendaGroupAttributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#autoFocusAttribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAutoFocusAttribute(RuleParserParser.AutoFocusAttributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#ruleflowGroupAttribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleflowGroupAttribute(RuleParserParser.RuleflowGroupAttributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#left}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLeft(RuleParserParser.LeftContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parenConditions}
	 * labeled alternative in {@link RuleParserParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenConditions(RuleParserParser.ParenConditionsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code multiConditions}
	 * labeled alternative in {@link RuleParserParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiConditions(RuleParserParser.MultiConditionsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code singleCondition}
	 * labeled alternative in {@link RuleParserParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleCondition(RuleParserParser.SingleConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code singleNamedConditionSet}
	 * labeled alternative in {@link RuleParserParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleNamedConditionSet(RuleParserParser.SingleNamedConditionSetContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#namedConditionSet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamedConditionSet(RuleParserParser.NamedConditionSetContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parenNamedConditions}
	 * labeled alternative in {@link RuleParserParser#namedCondition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenNamedConditions(RuleParserParser.ParenNamedConditionsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code multiNamedConditions}
	 * labeled alternative in {@link RuleParserParser#namedCondition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiNamedConditions(RuleParserParser.MultiNamedConditionsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code singleNamedConditions}
	 * labeled alternative in {@link RuleParserParser#namedCondition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleNamedConditions(RuleParserParser.SingleNamedConditionsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code singleCellCondition}
	 * labeled alternative in {@link RuleParserParser#decisionTableCellCondition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleCellCondition(RuleParserParser.SingleCellConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code multiCellConditions}
	 * labeled alternative in {@link RuleParserParser#decisionTableCellCondition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiCellConditions(RuleParserParser.MultiCellConditionsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parenCellConditions}
	 * labeled alternative in {@link RuleParserParser#decisionTableCellCondition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenCellConditions(RuleParserParser.ParenCellConditionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#refName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefName(RuleParserParser.RefNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#refObject}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefObject(RuleParserParser.RefObjectContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#nullValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullValue(RuleParserParser.NullValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#conditionLeft}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionLeft(RuleParserParser.ConditionLeftContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#expEval}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpEval(RuleParserParser.ExpEvalContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#expAll}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpAll(RuleParserParser.ExpAllContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#expExists}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpExists(RuleParserParser.ExpExistsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#expCollect}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpCollect(RuleParserParser.ExpCollectContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#commonFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommonFunction(RuleParserParser.CommonFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#exprCondition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprCondition(RuleParserParser.ExprConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#expressionBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionBody(RuleParserParser.ExpressionBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#percent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPercent(RuleParserParser.PercentContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#leftParen}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLeftParen(RuleParserParser.LeftParenContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#rightParen}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRightParen(RuleParserParser.RightParenContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#colon}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColon(RuleParserParser.ColonContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#join}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoin(RuleParserParser.JoinContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#right}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRight(RuleParserParser.RightContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#other}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOther(RuleParserParser.OtherContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#actions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActions(RuleParserParser.ActionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#action}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAction(RuleParserParser.ActionContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#assignAction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignAction(RuleParserParser.AssignActionContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#outAction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOutAction(RuleParserParser.OutActionContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#methodInvoke}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodInvoke(RuleParserParser.MethodInvokeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#functionInvoke}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionInvoke(RuleParserParser.FunctionInvokeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#actionParameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionParameters(RuleParserParser.ActionParametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#beanMethod}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBeanMethod(RuleParserParser.BeanMethodContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#complexValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComplexValue(RuleParserParser.ComplexValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter(RuleParserParser.ParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#parameterName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterName(RuleParserParser.ParameterNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstant(RuleParserParser.ConstantContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(RuleParserParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#namedVariable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamedVariable(RuleParserParser.NamedVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#property}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProperty(RuleParserParser.PropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#variableCategory}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableCategory(RuleParserParser.VariableCategoryContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#namedVariableCategory}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamedVariableCategory(RuleParserParser.NamedVariableCategoryContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#constantCategory}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantCategory(RuleParserParser.ConstantCategoryContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(RuleParserParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleParserParser#op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOp(RuleParserParser.OpContext ctx);
}