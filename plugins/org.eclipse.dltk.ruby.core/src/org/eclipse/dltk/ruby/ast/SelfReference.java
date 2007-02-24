/*
 * (c) 2002, 2005 xored software and others all rights reserved. http://www.xored.com
 */

package org.eclipse.dltk.ruby.ast;

import org.eclipse.dltk.ast.DLTKToken;
import org.eclipse.dltk.ast.references.Reference;
import org.eclipse.dltk.utils.CorePrinter;

public class SelfReference extends Reference {

	public SelfReference(int start, int end) {
		super(start, end);
	}

	public SelfReference(DLTKToken token) {
		this.setStart(token.getColumn());
		this.setEnd(this.sourceStart() + 4);
	}

	public String getStringRepresentation() {
		return "self";
	}

	public void printNode(CorePrinter output) {
		output.formatPrintLn("SelfReference" + this.getSourceRange().toString());
	}

	public String toString() {
		return "self";
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof SelfReference)) {
			return false;
		}
		SelfReference d = (SelfReference) obj;
		return sourceStart() == d.sourceStart() && sourceEnd() == d.sourceEnd();
	}

	public int hashCode() {
		return sourceStart() ^ sourceEnd();
	}
}
