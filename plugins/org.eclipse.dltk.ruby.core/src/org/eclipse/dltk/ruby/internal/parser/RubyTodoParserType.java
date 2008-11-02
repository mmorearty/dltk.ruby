/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.ruby.internal.parser;

import org.eclipse.dltk.compiler.task.ITodoTaskPreferences;
import org.eclipse.dltk.compiler.task.TodoTaskPreferencesOnPreferenceLookupDelegate;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.builder.AbstractTodoTaskBuildParticipantType;
import org.eclipse.dltk.core.builder.IBuildParticipant;
import org.eclipse.dltk.ruby.core.RubyPlugin;

public class RubyTodoParserType extends AbstractTodoTaskBuildParticipantType {

	protected ITodoTaskPreferences getPreferences(IScriptProject project) {
		return new TodoTaskPreferencesOnPreferenceLookupDelegate(
				RubyPlugin.PLUGIN_ID, project);
	}

	private static class RubyTodoTaskParser extends TodoTaskBuildParticipant {

		public RubyTodoTaskParser(ITodoTaskPreferences preferences) {
			super(preferences);
		}

		protected void reset() {
			super.reset();
			blockMode = false;
		}

		private boolean blockMode;

		protected int findCommentStart(char[] content, int begin, int end) {
			if (blockMode) {
				if (checkChars(content, begin, end, "=end")) { //$NON-NLS-1$
					blockMode = false;
					return -1;
				} else {
					for (int i = begin; i < end; ++i) {
						if (Character.isLetterOrDigit(content[i])
								&& (!isCheckRanges() || isValid(i))) {
							return i;
						}
					}
					return -1;
				}
			} else {
				if (checkChars(content, begin, end, "=begin")) { //$NON-NLS-1$
					blockMode = true;
					return -1;
				} else {
					return super.findCommentStart(content, begin, end);
				}
			}
		}

		/**
		 * @param content
		 * @param begin
		 * @param end
		 * @param string
		 * @return
		 */
		private boolean checkChars(char[] content, int begin, int end,
				String substring) {
			if (begin + substring.length() <= end) {
				for (int i = 0; i < substring.length(); ++i) {
					if (content[begin + i] != substring.charAt(i)) {
						return false;
					}
				}
			}
			return true;
		}

	}

	protected IBuildParticipant getBuildParticipant(
			ITodoTaskPreferences preferences) {
		return new RubyTodoTaskParser(preferences);
	}
}
