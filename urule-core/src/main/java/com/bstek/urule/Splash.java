package com.bstek.urule;

/**
 * @author Jacky.gao
 * @since 2017年8月28日
 */
public class Splash {
	public void print(){
		StringBuilder sb=new StringBuilder();
		sb.append("\n");
		sb.append("`7MMF'   `7MF'`7MM\"\"\"Mq.  `7MMF'   `7MF'`7MMF'      `7MM\"\"\"YMM           ");
		sb.append("\n");
		sb.append("  MM       M    MM   `MM.   MM       M    MM          MM    `7           ");
		sb.append("\n");
		sb.append("  MM       M    MM   ,M9    MM       M    MM          MM   d     pd*\"*b. ");
		sb.append("\n");
		sb.append("  MM       M    MMmmdM9     MM       M    MM          MMmmMM    (O)   j8 ");
		sb.append("\n");
		sb.append("  MM       M    MM  YM.     MM       M    MM      ,   MM   Y  ,     ,;j9 ");
		sb.append("\n");
		sb.append("  YM.     ,M    MM   `Mb.   YM.     ,M    MM     ,M   MM     ,M  ,-='    ");
		sb.append("\n");
		sb.append("   `bmmmmd\"'  .JMML. .JMM.   `bmmmmd\"'  .JMMmmmmMMM .JMMmmmmMMM Ammmmmmm ");
		sb.append("\n");
		sb.append(".....................................................................................................");
		sb.append("\n");
		sb.append(".  uRule, is a Chinese style rule engine");
		sb.append(" licensed under the Apache License 2.0,                     .");
		sb.append("\n");
		sb.append(".  which is opensource, free of charge, easy to use,");
		sb.append("high-performance, with browser-based-designer.  .");
		sb.append("\n");
		sb.append(".....................................................................................................");
		sb.append("\n");
		System.out.println(sb.toString());
	}
	public static void main(String[] args) {
		Splash s=new Splash();
		s.print();
	}
}
