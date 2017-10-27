/*******************************************************************************
 * Copyright (C) 2017 Bstek.com
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.bstek.urule.dsl;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

/**
 * @author Jacky.gao
 * @since 2015年2月27日
 */
public class SyntaxErrorListener extends BaseErrorListener {
	private SyntaxErrorReportor reportor;
	public SyntaxErrorListener(SyntaxErrorReportor reportor) {
		this.reportor=reportor;
	}
	@Override
	public void syntaxError(Recognizer<?, ?> recognizer,
			Object offendingSymbol, int line, int charPositionInLine,
			String msg, RecognitionException e) {
		this.reportor.addError(line, charPositionInLine, offendingSymbol, msg);
		//super.syntaxError(recognizer, offendingSymbol, line, charPositionInLine, msg, e);
	}
}
