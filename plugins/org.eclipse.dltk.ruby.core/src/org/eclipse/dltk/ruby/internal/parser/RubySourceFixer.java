package org.eclipse.dltk.ruby.internal.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.declarations.Declaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ruby.core.RubyPlugin;

public class RubySourceFixer {

	private static final Pattern DOT_FIXER = Pattern
			.compile("\\.(?=[\\s,\\)\\]\\}]|$)"); //$NON-NLS-1$
	private static final Pattern DOLLAR_FIXER = Pattern
			.compile("\\$(?=[\\s,\\)\\]\\}]|$)"); //$NON-NLS-1$
	private static final Pattern AT_FIXER = Pattern
			.compile("@(?=[\\s,\\)\\]\\}]|$)"); //$NON-NLS-1$
	private static final Pattern COLON_FIXER1 = Pattern
			.compile("::(?=[\\s,\\)\\]\\}]|$)"); //$NON-NLS-1$
	private static final Pattern COLON_FIXER2 = Pattern.compile(
			"(?:=>.*,[\\s]*)(:)(?=[\\s]*(?=[,}\\)]))", Pattern.MULTILINE); //$NON-NLS-1$
	private static final Pattern COLON_FIXER3 = Pattern.compile(
			":(?=[\\s]*(?=[,}\\)]))", Pattern.MULTILINE); //$NON-NLS-1$
	private static final Pattern COLON_FIXER_UNSAFE1 = Pattern.compile(
			"(?:=>.*,[\\s\\\\]*)(:)(?=[\\s]*$)", Pattern.MULTILINE); //$NON-NLS-1$
	private static final Pattern COLON_FIXER_UNSAFE2 = Pattern.compile(
			":(?=[\\s]*$)", Pattern.MULTILINE); //$NON-NLS-1$
	private static final Pattern INST_BRACK_FIXER = Pattern.compile("@(])"); //$NON-NLS-1$
	private static final Pattern GLOB_BRACK_FIXER = Pattern.compile("\\$(])"); //$NON-NLS-1$
	private static final Pattern COMMA_FIXER1 = Pattern.compile(
			"(?:=>.*)(,)(?=[\\s)]*do)", Pattern.MULTILINE); //$NON-NLS-1$
	private static final Pattern COMMA_FIXER2 = Pattern.compile(
			",(?=[\\s)]*do)", Pattern.MULTILINE); //$NON-NLS-1$
	private static final Pattern COMMA_FIXER3A = Pattern.compile(
			"(?:=>.*,[^=>\r\n]*'[^']*)(')(?=[\\s)]*do)", Pattern.MULTILINE); //$NON-NLS-1$
	private static final Pattern COMMA_FIXER3B = Pattern.compile(
			"(?:=>.*,[^=>\r\n]*\"[^\"]*)(\")(?=[\\s)]*do)", Pattern.MULTILINE); //$NON-NLS-1$
	private static final Pattern COMMA_FIXER3C = Pattern
			.compile(
					"(?:=>.*,[^=>\r\n]*)([\\:a-zA-Z0-9_!?])(?=[\\s)]*do)", Pattern.MULTILINE); //$NON-NLS-1$
	private static final Pattern COMMA_FIXER4 = Pattern.compile(
			"(?:=>.*)(,)(?=[\\s]*[)][\\s]*$)", Pattern.MULTILINE); //$NON-NLS-1$
	private static final Pattern COMMA_FIXER5 = Pattern.compile(
			",(?=[\\s]*[)][\\s]*$)", Pattern.MULTILINE); //$NON-NLS-1$
	private static final Pattern COMMA_FIXER6A = Pattern
			.compile(
					"(?:=>.*,[^=>\r\n]*'[^']*)(')(?=[\\s]*[)][\\s]*$)", Pattern.MULTILINE); //$NON-NLS-1$
	private static final Pattern COMMA_FIXER6B = Pattern
			.compile(
					"(?:=>.*,[^=>\r\n]*\"[^\"]*)(\")(?=[\\s]*[)][\\s]*$)", Pattern.MULTILINE); //$NON-NLS-1$
	private static final Pattern COMMA_FIXER6C = Pattern
			.compile(
					"(?:=>.*,[^=>\r\n]*)([\\:a-zA-Z0-9_!?])(?=[\\s]*[)][\\s]*$)", Pattern.MULTILINE); //$NON-NLS-1$
	private static final Pattern COMMA_FIXER_UNSAFE1A = Pattern
			.compile(
					"(?:^[\\s]*[^\\s(\\:]+[\\s]*'[^']*'[\\s]*=>.*)(,)(?=[\\s)]*$)", Pattern.MULTILINE); //$NON-NLS-1$
	private static final Pattern COMMA_FIXER_UNSAFE1B = Pattern
			.compile(
					"(?:^[\\s]*[^\\s(\\:]+[\\s]*\"[^\"]*\"[\\s]*=>.*)(,)(?=[\\s)]*$)", Pattern.MULTILINE); //$NON-NLS-1$
	private static final Pattern COMMA_FIXER_UNSAFE1C = Pattern
			.compile(
					"(?:^[\\s]*[^\\s(\\:]+[\\s]*[\\:a-zA-Z0-9_!?]+[\\s]*=>.*)(,)(?=[\\s)]*$)", Pattern.MULTILINE); //$NON-NLS-1$
	private static final Pattern COMMA_FIXER_UNSAFE2A = Pattern
			.compile(
					"(?:^[\\s]*[^\\s(\\:]+[\\s]*'[^']*'[\\s]*)(,)(?=[\\s)]*$)", Pattern.MULTILINE); //$NON-NLS-1$
	private static final Pattern COMMA_FIXER_UNSAFE2B = Pattern
			.compile(
					"(?:^[\\s]*[^\\s(\\:]+[\\s]*\"[^\"]*\"[\\s]*)(,)(?=[\\s)]*$)", Pattern.MULTILINE); //$NON-NLS-1$
	private static final Pattern COMMA_FIXER_UNSAFE2C = Pattern
			.compile(
					"(?:^[\\s]*[^\\s(\\:]+[\\s]*[\\:a-zA-Z0-9_!?]*[\\s]*)(,)(?=[\\s)]*$)", Pattern.MULTILINE); //$NON-NLS-1$
	private static final Pattern COMMA_FIXER_UNSAFE3A = Pattern.compile(
			"(?:=>.*,[^=>\r\n]*'[^']*)(')(?=[\\s)]*$)", Pattern.MULTILINE); //$NON-NLS-1$
	private static final Pattern COMMA_FIXER_UNSAFE3B = Pattern.compile(
			"(?:=>.*,[^=>\r\n]*\"[^\"]*)(\")(?=[\\s)]*$)", Pattern.MULTILINE); //$NON-NLS-1$
	private static final Pattern COMMA_FIXER_UNSAFE3C = Pattern
			.compile(
					"(?:=>.*,[^=>\r\n]*)([\\:a-zA-Z0-9_!?])(?=[\\s)]*$)", Pattern.MULTILINE); //$NON-NLS-1$
	private static final Pattern HASH_FIXER1 = Pattern.compile(
			"=>(?=[\\s)]*do)", Pattern.MULTILINE); //$NON-NLS-1$
	private static final Pattern HASH_FIXER2 = Pattern.compile(
			"=>(?=[\\s]*[,}\\)])", Pattern.MULTILINE); //$NON-NLS-1$
	private static final Pattern HASH_FIXER_UNSAFE1 = Pattern.compile(
			"=>(?=[\\s)]*$)", Pattern.MULTILINE); //$NON-NLS-1$
	private static final Pattern HASH_FIXER_UNSAFE2A = Pattern
			.compile(
					"(?:^[\\s]*)(\\s)(?:'[^']*'[\\s]*=>.*[^\\s,)]+[\\s]*$)", Pattern.MULTILINE); //$NON-NLS-1$
	private static final Pattern HASH_FIXER_UNSAFE2B = Pattern
			.compile(
					"(?:^[\\s]*)(\\s)(?:\"[^\"]*\"[\\s]*=>.*[^\\s,)]+[\\s]*$)", Pattern.MULTILINE); //$NON-NLS-1$
	private static final Pattern HASH_FIXER_UNSAFE2C = Pattern
			.compile(
					"(?:^[\\s]*)(\\s)(?:[\\:a-zA-Z0-9_]+[\\s]*=>.*[^\\s,)]+[\\s]*$)", Pattern.MULTILINE); //$NON-NLS-1$

	private static final String missingKey = "_m_key_"; //$NON-NLS-1$
	private static final String missingValue = "_m_value__"; //$NON-NLS-1$
	private static final String missingName = "_missing_method_name_"; //$NON-NLS-1$
	private static final String missingName2 = "NoConstant___________"; //$NON-NLS-1$
	private static final String missingName3 = "_missing_param_name__"; //$NON-NLS-1$
	private static final String missingName4 = missingKey
			+ " => " + missingValue; //$NON-NLS-1$
	private static final String missingName4a = " => " + missingValue; //$NON-NLS-1$
	private static final int magicLength = missingName.length();

	// missingName.len should == missingName2.len, etc

	private final class ASTPositionsCorrector extends ASTVisitor {
		public boolean visitGeneral(ASTNode node) throws Exception {
			if (node.sourceStart() < 0 || node.sourceEnd() < 0)
				return true;
			int st = 0;
			int en = 0;
			int n_st = 0;
			int n_en = 0;
			for (Iterator iterator = fixPositions.iterator(); iterator
					.hasNext();) {
				Integer pos = (Integer) iterator.next();
				int fixPos = pos.intValue();
				// starts
				if (node.sourceStart() > fixPos) {
					st++;
				}
				if (node.sourceEnd() > fixPos) {
					en++;
				}
				if (node instanceof Declaration) {
					Declaration declaration = (Declaration) node;
					if (declaration.getNameStart() > fixPos) {
						n_st++;
					}
					if (declaration.getNameEnd() > fixPos) {
						n_en++;
					}
				}
			}

			node.setStart(node.sourceStart() - st * magicLength);
			node.setEnd(node.sourceEnd() - en * magicLength);
			if (node instanceof Declaration) {
				Declaration declaration = (Declaration) node;
				declaration.setNameStart(declaration.getNameStart() - n_st
						* magicLength);
				declaration.setNameEnd(declaration.getNameEnd() - n_en
						* magicLength);
			}
			// if (st == 0 && en == 0 && n_st == 0 && n_en == 0)
			// return false;

			return true;
		}
	}

	public static Set FIXUP_NAMES = new HashSet();
	{
		FIXUP_NAMES.add(missingKey);
		FIXUP_NAMES.add(missingValue);
		FIXUP_NAMES.add(missingName);
		FIXUP_NAMES.add(missingName2);
		FIXUP_NAMES.add(missingName3);
		FIXUP_NAMES.add(missingName4);
		FIXUP_NAMES.add(missingName4a);
	}

	private final List fixPositions = new ArrayList();

	private String fixBrokenThings(Pattern pattern, String content,
			String replacement, int delta) {
		Matcher matcher = pattern.matcher(content);
		StringBuffer result = new StringBuffer();
		int regionStart = 0;
		while (matcher.find(regionStart)) {
			int offset = matcher.start(matcher.groupCount());
			if (offset > regionStart)
				result.append(content.subSequence(regionStart, offset));
			fixPositions.add(new Integer(result.length()));
			result.append(replacement);
			// fixPositions.add(new Integer(offset + fixPositions.size() *
			// magicLength));
			regionStart = offset + delta; // 2
		}
		if (regionStart < content.length() - 1)
			result.append(content.subSequence(regionStart, content.length()));
		if (regionStart == 0)
			return content; // nothing fixed
		else
			return result.toString();
	}

	private String fixBrokenDots(String content) {
		return fixBrokenThings(DOT_FIXER, content, "." + missingName, 1); //$NON-NLS-1$
	}

	private String fixBrokenColons(String content) {
		String content2 = fixBrokenThings(COLON_FIXER1, content,
				"::" + missingName2, 2); //$NON-NLS-1$
		content2 = fixBrokenThings(COLON_FIXER2, content2,
				":" + missingName4, 1); //$NON-NLS-1$
		return fixBrokenThings(COLON_FIXER3, content2, ":" + missingName2, 1); //$NON-NLS-1$
	}

	private String fixBrokenColonsUnsafe(String content) {
		String content2 = fixBrokenThings(COLON_FIXER_UNSAFE1, content,
				":" + missingName4, 1); //$NON-NLS-1$
		return fixBrokenThings(COLON_FIXER_UNSAFE2, content2,
				":" + missingName2, 1); //$NON-NLS-1$
	}

	private String fixBrokenDollars(String content) {
		return fixBrokenThings(DOLLAR_FIXER, content, "$" + missingName, 1); //$NON-NLS-1$
	}

	private String fixBrokenAts(String content) {
		return fixBrokenThings(AT_FIXER, content, "@" + missingName, 1); //$NON-NLS-1$
	}

	private String fixBrokenInstbracks(String content) {
		return fixBrokenThings(INST_BRACK_FIXER, content, "@" + missingName, 1); //$NON-NLS-1$
	}

	private String fixBrokenGlobbracks(String content) {
		return fixBrokenThings(GLOB_BRACK_FIXER, content, "$" + missingName, 1); //$NON-NLS-1$
	}

	private String fixBrokenUnmatched(String content) {
		char[] contents = content.toCharArray();
		StringBuffer buffer = new StringBuffer(contents.length);
		int parenDepth = 0;
		int bracketDepth = 0;
		int braceDepth = 0;
		int start = contents.length;

		for (int cnt = (start - 1); cnt >= 0; cnt--) {
			if (contents[cnt] == '(') {
				parenDepth--;

				if (parenDepth < 0) {
					int eol = cnt;
					for (int cnt2 = cnt; cnt2 < start; cnt2++) {
						if ((contents[cnt2] == '\r')
								|| (contents[cnt2] == '\n')) {
							eol = cnt2;

							break;
						}
					}

					buffer.insert(0, contents, eol, (start - eol));
					buffer.append(')');
					buffer.insert(0, contents, cnt, (eol - cnt));
					start = cnt;
					parenDepth = 0;
				}
			} else if (contents[cnt] == '[') {
				bracketDepth--;

				if (bracketDepth < 0) {
					int eol = cnt;
					for (int cnt2 = cnt; cnt2 < start; cnt2++) {
						if ((contents[cnt2] == '\r')
								|| (contents[cnt2] == '\n')) {
							eol = cnt2;

							break;
						}
					}

					buffer.insert(0, contents, eol, (start - eol));
					buffer.append(']');
					buffer.insert(0, contents, cnt, (eol - cnt));
					start = cnt;
					bracketDepth = 0;
				}
			} else if (contents[cnt] == '{') {
				braceDepth--;

				if (braceDepth < 0) {
					int eol = cnt;
					for (int cnt2 = cnt; cnt2 < start; cnt2++) {
						if ((contents[cnt2] == '\r')
								|| (contents[cnt2] == '\n')) {
							eol = cnt2;

							break;
						}
					}

					buffer.insert(0, contents, eol, (start - eol));
					buffer.append('}');
					buffer.insert(0, contents, cnt, (eol - cnt));
					start = cnt;
					braceDepth = 0;
				}
			} else if (contents[cnt] == ')') {
				parenDepth++;
			} else if (contents[cnt] == ']') {
				bracketDepth++;
			} else if (contents[cnt] == '}') {
				braceDepth++;
			}
		}

		if (start > 0) {
			buffer.insert(0, contents, 0, start);
		}

		return buffer.toString();
	}

	private String fixBrokenCommas(String content) {
		String content2 = content;
		content2 = fixBrokenThings(COMMA_FIXER1, content2,
				"," + missingName4 + " ", 1); //$NON-NLS-1$ //$NON-NLS-2$
		content2 = fixBrokenThings(COMMA_FIXER2, content2,
				"," + missingName3 + " ", 1); //$NON-NLS-1$ //$NON-NLS-2$
		content2 = fixBrokenThings(COMMA_FIXER3A, content2,
				"'" + missingName4a, 1); //$NON-NLS-1$
		content2 = fixBrokenThings(COMMA_FIXER3B, content2,
				"\"" + missingName4a, 1); //$NON-NLS-1$
		content2 = fixBrokenThings(COMMA_FIXER3C, content2,
				missingName4 + " ", 1); //$NON-NLS-1$
		content2 = fixBrokenThings(COMMA_FIXER4, content2,
				"," + missingName4, 1); //$NON-NLS-1$
		content2 = fixBrokenThings(COMMA_FIXER5, content2,
				"," + missingName3, 1); //$NON-NLS-1$
		content2 = fixBrokenThings(COMMA_FIXER6A, content2,
				"'" + missingName4a, 1); //$NON-NLS-1$
		content2 = fixBrokenThings(COMMA_FIXER6B, content2,
				"\"" + missingName4a, 1); //$NON-NLS-1$
		return fixBrokenThings(COMMA_FIXER6C, content2, missingName4 + " ", 1); //$NON-NLS-1$
	}

	private String fixBrokenCommasUnsafe(String content) {
		String content2 = content;
		content2 = fixBrokenThings(COMMA_FIXER_UNSAFE1A, content2,
				"," + missingName4, 1); //$NON-NLS-1$
		content2 = fixBrokenThings(COMMA_FIXER_UNSAFE1B, content2,
				"," + missingName4, 1); //$NON-NLS-1$
		content2 = fixBrokenThings(COMMA_FIXER_UNSAFE1C, content2,
				"," + missingName4, 1); //$NON-NLS-1$
		content2 = fixBrokenThings(COMMA_FIXER_UNSAFE2A, content2,
				"," + missingName3, 1); //$NON-NLS-1$
		content2 = fixBrokenThings(COMMA_FIXER_UNSAFE2B, content2,
				"," + missingName3, 1); //$NON-NLS-1$
		content2 = fixBrokenThings(COMMA_FIXER_UNSAFE2C, content2,
				"," + missingName3, 1); //$NON-NLS-1$
		content2 = fixBrokenThings(COMMA_FIXER_UNSAFE3A, content2,
				"'" + missingName4a, 1); //$NON-NLS-1$
		content2 = fixBrokenThings(COMMA_FIXER_UNSAFE3B, content2,
				"\"" + missingName4a, 1); //$NON-NLS-1$
		return fixBrokenThings(COMMA_FIXER_UNSAFE3C, content2, missingName4
				+ " ", 1); //$NON-NLS-1$
	}

	private String fixBrokenHashes(String content) {
		String content2 = content;
		content2 = fixBrokenThings(HASH_FIXER1, content2,
				"=>" + missingName3 + " ", 2); //$NON-NLS-1$ //$NON-NLS-2$
		return fixBrokenThings(HASH_FIXER2, content2, "=>" + missingName3, 2); //$NON-NLS-1$
	}

	private String fixBrokenHashesUnsafe(String content) {
		String content2 = content;
		content2 = fixBrokenThings(HASH_FIXER_UNSAFE1, content2,
				"=>" + missingName3, 2); //$NON-NLS-1$
		content2 = fixBrokenThings(HASH_FIXER_UNSAFE2A, content2,
				" " + missingName + " ", 1); //$NON-NLS-1$ //$NON-NLS-2$
		content2 = fixBrokenThings(HASH_FIXER_UNSAFE2B, content2,
				" " + missingName + " ", 1); //$NON-NLS-1$ //$NON-NLS-2$
		return fixBrokenThings(HASH_FIXER_UNSAFE2C, content2,
				" " + missingName + " ", 1); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public String fix1(String content) {
		String content2 = fixBrokenDots(content);
		content2 = fixBrokenColons(content2);
		content2 = fixBrokenDollars(content2);
		content2 = fixBrokenAts(content2);
		content2 = fixBrokenInstbracks(content2);
		content2 = fixBrokenGlobbracks(content2);

		content2 = fixBrokenUnmatched(content2);
		content2 = fixBrokenCommas(content2);
		content2 = fixBrokenHashes(content2);
		return content2;
	}

	public String fixUnsafe1(String content) {
		return fixBrokenColonsUnsafe(content);
	}

	public String fixUnsafe2(String content) {
		content = fixBrokenCommasUnsafe(content);
		content = fixBrokenHashesUnsafe(content);
		return content;
	}

	public void correctPositionsIfNeeded(ModuleDeclaration module) {
		if (!fixPositions.isEmpty()) {
			try {
				module.traverse(new ASTPositionsCorrector());
			} catch (Exception e) {
				RubyPlugin.log(e);
			}
		}
	}

	public void clearPositions() {
		fixPositions.clear();
	}

}
