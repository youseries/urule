/**
 * @author GJ
 */
import Raphael from 'raphael';

urule.Context=function(container,rule){
	this.container=container;
	this.paper = new Raphael(this.container.get(0), "100%", "100%");
	this.rule=rule;
	this.rootJoin=null;
};
urule.Context.prototype.putToNamedMap=function(referenceName,variableCategory){
	this.rule.namedMap.set(referenceName,variableCategory);
};
urule.Context.prototype.deleteFromNamedMap=function(referenceName){
	this.rule.namedMap.delete(referenceName);
};
urule.Context.prototype.getVariableCategoryFromNamedMap=function(referenceName){
	return this.rule.namedMap.get(referenceName);
};
urule.Context.prototype.getCanvas=function(){
	return this.container;
};
urule.Context.prototype.getPaper=function(){
	return this.paper;
};
urule.Context.prototype.setRootJoin=function(join){
	this.rootJoin=join;
};
urule.Context.prototype.getRootJoin=function(){
	return this.rootJoin;
};
urule.Context.prototype.getTotalChildrenCount=function(){
	return this.rootJoin.getChildrenCount();
};