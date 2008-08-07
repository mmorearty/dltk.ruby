/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.ruby.internal.parser;

import java.io.CharArrayReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.Platform;
import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.FakeModuleDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.parser.AbstractSourceParser;
import org.eclipse.dltk.ast.references.ConstantReference;
import org.eclipse.dltk.ast.statements.Block;
import org.eclipse.dltk.compiler.problem.AbstractProblemReporter;
import org.eclipse.dltk.compiler.problem.IProblem;
import org.eclipse.dltk.compiler.problem.IProblemReporter;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.ruby.ast.RubyClassDeclaration;
import org.eclipse.dltk.ruby.ast.RubyModuleDeclaration;
import org.eclipse.dltk.ruby.core.utils.RubySyntaxUtils;
import org.eclipse.dltk.ruby.internal.parsers.jruby.DLTKRubyParser;
import org.eclipse.dltk.ruby.internal.parsers.jruby.RubyASTBuildVisitor;
import org.jruby.ast.Node;
import org.jruby.ast.visitor.NodeVisitor;
import org.jruby.parser.RubyParserResult;

public class JRubySourceParser extends AbstractSourceParser {

	private static boolean silentState = true;

	public static boolean isSilentState() {
		return silentState;
	}

	/**
	 * This option allows parser to suppress errors and exceptions and in result
	 * generate possibly partially non-correct AST instead of failing with
	 * exception. For running parser tests this option are being set to
	 * <code>false</code>.
	 */
	public static void setSilentState(boolean s) {
		silentState = s;
	}

	private static final boolean TRACE_AST_JRUBY = Boolean.valueOf(
			Platform.getDebugOption("org.eclipse.dltk.core/traceAST/jruby")) //$NON-NLS-1$
			.booleanValue();

	private static final boolean TRACE_AST_DLTK = Boolean.valueOf(
			Platform.getDebugOption("org.eclipse.dltk.core/traceAST/dltk")) //$NON-NLS-1$
			.booleanValue();

	private final boolean[] errorState = new boolean[1];

	private RubyParserResult parserResult;

	public RubyParserResult getParserResult() {
		return parserResult;
	}

	private class ProxyProblemReporter extends AbstractProblemReporter {

		private final IProblemReporter original;

		public ProxyProblemReporter(IProblemReporter original) {
			super();
			this.original = original;
		}

		public void reportProblem(IProblem problem) {
			if (original != null)
				original.reportProblem(problem);
			if (problem.isError()) {
				errorState[0] = true;
			}
		}

	}

	public JRubySourceParser() {

	}

	/**
	 * Should return visitor for creating ModuleDeclaration from JRuby's AST
	 * 
	 * @param module
	 * @param content
	 * @return
	 */
	protected NodeVisitor getASTBuilderVisitor(ModuleDeclaration module,
			char[] content) {
		return new RubyASTBuildVisitor(module, content);
	}

	private boolean isMethodNameChar(char inputChar, char prevChar) {
		return (((inputChar >= 'a') && (inputChar <= 'z'))
				|| ((inputChar >= '0') && (inputChar <= '9'))
				|| ((inputChar >= 'A') && (inputChar <= 'Z'))
				|| (inputChar == '_')
				|| ((inputChar == '?') && isMethodNameChar(prevChar, '@')) || ((inputChar == '!') && isMethodNameChar(
				prevChar, '@')));
	}

	private boolean isPrefixKeyword(char[] content, int endOffset) {
		boolean isPrefixKeyword = false;
		int startOffset = -1;

		if (endOffset >= 5) {
			startOffset = (endOffset - 5);
			isPrefixKeyword = "return".equals(String.valueOf(content, startOffset, 6)); //$NON-NLS-1$

			if (!isPrefixKeyword) {
				isPrefixKeyword = "unless".equals(String.valueOf(content, startOffset, 6)); //$NON-NLS-1$
			}
		}

		if (!isPrefixKeyword && endOffset >= 4) {
			startOffset = (endOffset - 4);
			isPrefixKeyword = "while".equals(String.valueOf(content, startOffset, 5)); //$NON-NLS-1$

			if (!isPrefixKeyword) {
				isPrefixKeyword = "elsif".equals(String.valueOf(content, startOffset, 5)); //$NON-NLS-1$
			}

			if (!isPrefixKeyword) {
				isPrefixKeyword = "until".equals(String.valueOf(content, startOffset, 5)); //$NON-NLS-1$
			}
		}

		if (!isPrefixKeyword && endOffset >= 3) {
			startOffset = (endOffset - 3);
			isPrefixKeyword = "then".equals(String.valueOf(content, startOffset, 4)); //$NON-NLS-1$

			if (!isPrefixKeyword) {
				isPrefixKeyword = "case".equals(String.valueOf(content, startOffset, 4)); //$NON-NLS-1$
			}
		}

		if (!isPrefixKeyword && endOffset >= 2) {
			startOffset = (endOffset - 2);
			isPrefixKeyword = "and".equals(String.valueOf(content, startOffset, 3)); //$NON-NLS-1$
		}

		if (!isPrefixKeyword && endOffset >= 1) {
			startOffset = (endOffset - 1);
			isPrefixKeyword = "if".equals(String.valueOf(content, startOffset, 2)); //$NON-NLS-1$

			if (!isPrefixKeyword) {
				isPrefixKeyword = "or".equals(String.valueOf(content, startOffset, 2)); //$NON-NLS-1$
			}
		}

		if (isPrefixKeyword) {
			isPrefixKeyword = ((startOffset == 0) || !isMethodNameChar(
					content[startOffset - 1],
					(startOffset > 1) ? content[startOffset - 2] : '@'));
		}

		return isPrefixKeyword;
	}

	private char[] fixSpacedParens(char[] content) {
		char[] fixedContent = new char[content.length];
		System.arraycopy(content, 0, fixedContent, 0, content.length);

		boolean inComment = false;
		boolean inSingleString = false;
		boolean inDoubleString = false;
		boolean inBackQuoteString = false;
		boolean inBraceString = false;
		boolean inInterpString = false;
		boolean inRegexp = false;
		for (int cnt = 0, max = fixedContent.length; cnt < max; cnt++) {
			// ssanders - If there is a space between a method name and its
			// opening parenthesis
			if ((cnt > 1) && !inComment && !inSingleString && !inDoubleString
					&& !inBackQuoteString && !inBraceString && !inRegexp
					&& (fixedContent[cnt] == '(')
					&& (fixedContent[cnt - 1] == ' ')) {
				if (isMethodNameChar(fixedContent[cnt - 2],
						(cnt > 2) ? fixedContent[cnt - 3] : '@')) {
					if (!isPrefixKeyword(fixedContent, (cnt - 2))) {
						// ssanders - Invert the space and parenthesis to
						// correct warning and position info
						fixedContent[cnt - 1] = '(';
						fixedContent[cnt] = ' ';
					}
				}
			} else if (fixedContent[cnt] == '#') {
				if ((inSingleString || inDoubleString || inBackQuoteString
						|| inBraceString || inRegexp)
						&& (fixedContent[cnt + 1] == '{')) {
					inInterpString = true;
				} else if (!inSingleString && !inDoubleString
						&& !inBackQuoteString && !inBraceString) {
					inComment = true;
				}
			} else if ((fixedContent[cnt] == '\r')
					|| (fixedContent[cnt] == '\n')) {
				inComment = false;
			} else if (fixedContent[cnt] == '"') {
				if ((cnt < 1) || (fixedContent[cnt - 1] != '\\')) {
					if (!inComment & !inInterpString) {
						inDoubleString = !inDoubleString;
					}
				}
			} else if (fixedContent[cnt] == '\'') {
				if ((cnt < 1) || (fixedContent[cnt - 1] != '\\')) {
					if (!inComment) {
						inSingleString = !inSingleString;
					}
				}
			} else if (fixedContent[cnt] == '`') {
				if ((cnt < 1) || (fixedContent[cnt - 1] != '\\')) {
					if (!inComment) {
						inBackQuoteString = !inBackQuoteString;
					}
				}
			} else if (fixedContent[cnt] == '{') {
				if ((cnt < 1) || (fixedContent[cnt - 1] == '%')) {
					if (!inComment) {
						inBraceString = true;
					}
				}
			} else if (fixedContent[cnt] == '}') {
				if ((cnt < 1) || (fixedContent[cnt - 1] != '\\')) {
					if (!inComment) {
						if (inInterpString) {
							inInterpString = false;
						} else {
							inBraceString = false;
						}
					}
				}
			} else if (fixedContent[cnt] == '/') {
				if ((cnt < 1) || (fixedContent[cnt - 1] != '\\')) {
					if (!inComment && !inSingleString && !inDoubleString
							&& !inBackQuoteString && !inBraceString) {
						inRegexp = !inRegexp;
					}
				}
			}
		}

		return fixedContent;
	}

	public ModuleDeclaration parse(final char[] fileName, char[] content,
			IProblemReporter reporter) {
		try {
			DLTKRubyParser parser = new DLTKRubyParser();
			ProxyProblemReporter proxyProblemReporter = new ProxyProblemReporter(
					reporter);
			errorState[0] = false;

			final long sTime = TRACE_AST_DLTK ? System.currentTimeMillis() : 0;
			final String strFileName = fileName != null ? String
					.valueOf(fileName) : ""; //$NON-NLS-1$

			char[] fixedContent = fixSpacedParens(content);
			Node node;
			if (Arrays.equals(fixedContent, content) != true) {
				// ssanders - Parse with reporter to collect parenthesis
				// warnings
				parser.parse(strFileName, new CharArrayReader(content),
						proxyProblemReporter);
				// ssanders - However, use modified content to have corrected
				// position info
				node = parser.parse(strFileName, new CharArrayReader(
						fixedContent), null);
			} else {
				node = parser.parse(strFileName, new CharArrayReader(content),
						proxyProblemReporter);
			}
			final RubySourceFixer fixer = new RubySourceFixer();
			if (!parser.isSuccess() || errorState[0]) {
				String content2 = fixer.fix1(String.valueOf(fixedContent));

				Node node2 = parser.parse(strFileName, new StringReader(
						content2), null);
				if (node2 != null)
					node = node2;
				else {
					fixer.clearPositions();

					content2 = fixer.fixUnsafe1(content2);

					node2 = parser.parse(strFileName,
							new StringReader(content2), null);
					if (node2 != null)
						node = node2;
					else {
						fixer.clearPositions();

						content2 = fixer.fixUnsafe2(content2);

						node2 = parser.parse(strFileName, new StringReader(
								content2), new AbstractProblemReporter() {

							public void reportProblem(IProblem problem) {
								if (DLTKCore.DEBUG) {
									System.out
											.println("JRubySourceParser.parse(): Fallback Parse Problem - fileName=" + strFileName + //$NON-NLS-1$
													", message=" //$NON-NLS-1$
													+ problem.getMessage()
													+ ", line=" + problem.getSourceLineNumber()); //$NON-NLS-1$
								}
							}

						});
					}
					if (node2 != null)
						node = node2;
					else
						fixer.clearPositions();
				}
				content = content2.toCharArray();
			}

			ModuleDeclaration module = new ModuleDeclaration(content.length);
			NodeVisitor visitor = getASTBuilderVisitor(module, content);
			if (node != null)
				node.accept(visitor);

			if (node != null) {
				if (TRACE_AST_JRUBY || TRACE_AST_DLTK)
					System.out.println("\n\nAST rebuilt\n"); //$NON-NLS-1$
				if (TRACE_AST_JRUBY)
					System.out.println("JRuby AST:\n" + node.toString()); //$NON-NLS-1$
				if (TRACE_AST_DLTK)
					System.out.println("DLTK AST:\n" + module.toString()); //$NON-NLS-1$
			}

			fixer.correctPositionsIfNeeded(module);

			if (TRACE_AST_DLTK) {
				long eTime = System.currentTimeMillis();
				System.out.println("Parsing took " + (eTime - sTime) //$NON-NLS-1$
						+ " ms"); //$NON-NLS-1$
			}
			this.parserResult = parser.getParserResult();

			if (!parser.isSuccess() && module.isEmpty()) {
				module = new FakeModuleDeclaration(content.length);
				minimumParse(content, module);
			}

			return module;
		} catch (Throwable t) {
			if (DLTKCore.DEBUG) {
				t.printStackTrace();
			}
			if (isSilentState()) {
				ModuleDeclaration mdl = new ModuleDeclaration(1);
				return mdl;
			}
			throw new RuntimeException(t);
		}
	}

	public ModuleDeclaration parse(String source) {
		return this.parse(null, source.toCharArray(), null);
	}

	/**
	 * Really basic parse to find the first class or module definition, the
	 * intent is that a module declaration has at least a type in it (if one
	 * exists or can be parsed).
	 * 
	 * @param content
	 * @param md
	 */
	private static void minimumParse(char[] content, ModuleDeclaration md) {
		StringTokenizer toker = new StringTokenizer(new String(content));
		while (toker.hasMoreTokens()) {
			String token = toker.nextToken();
			if (token.equals("class") || token.equals("module")) { //$NON-NLS-1$ //$NON-NLS-2$
				String className = toker.nextToken();

				if (RubySyntaxUtils.isValidClass(className)) {
					String source = new String(content);
					// TODO(mhowe): Make position calculation more robust
					int indexOf = source.indexOf(className);
					int nameEnd = indexOf + className.length();
					RubyModuleDeclaration type;
					ASTNode nameNode = new ConstantReference(indexOf, nameEnd,
							className);
					Block bodyBlock = new Block(indexOf + nameEnd, source
							.length() - 1);
					if (token.equals("class")) { //$NON-NLS-1$
						type = new RubyClassDeclaration(null, nameNode,
								bodyBlock, indexOf, source.length() - 1);
					} else
						type = new RubyModuleDeclaration(nameNode, bodyBlock,
								indexOf, source.length() - 1);
					md.addStatement(type);
					if (toker.nextToken().equals("<")) { //$NON-NLS-1$
						String superClass = toker.nextToken();
						if (RubySyntaxUtils.isValidClass(superClass)) {
							indexOf = source.indexOf(className);
							type.addSuperClass(new ConstantReference(indexOf,
									indexOf + superClass.length(), superClass));
						}
					}
					return;
				}
			}
		}
	}

}
