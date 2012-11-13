// created by jay 1.0.2 (c) 2002-2004 ats@cs.rit.edu
// skeleton Java 1.0 (c) 2002 ats@cs.rit.edu

					// line 2 "DefaultRubyParser.y"
/***** BEGIN LICENSE BLOCK *****
 * Version: CPL 1.0/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Common Public
 * License Version 1.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.eclipse.org/legal/cpl-v10.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * Copyright (C) 2001 Alan Moore <alan_moore@gmx.net>
 * Copyright (C) 2001-2002 Benoit Cerrina <b.cerrina@wanadoo.fr>
 * Copyright (C) 2001-2004 Stefan Matthias Aust <sma@3plus4.de>
 * Copyright (C) 2001-2004 Jan Arne Petersen <jpetersen@uni-bonn.de>
 * Copyright (C) 2002-2004 Anders Bengtsson <ndrsbngtssn@yahoo.se>
 * Copyright (C) 2004-2006 Thomas E Enebo <enebo@acm.org>
 * Copyright (C) 2004 Charles O Nutter <headius@headius.com>
 * Copyright (C) 2006 Miguel Covarrubias <mlcovarrubias@gmail.com>
 * 
 * Alternatively, the contents of this file may be used under the terms of
 * either of the GNU General Public License Version 2 or later (the "GPL"),
 * or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the CPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the CPL, the GPL or the LGPL.
 ***** END LICENSE BLOCK *****/
package org.jruby.parser;

import java.io.IOException;

import org.eclipse.dltk.ruby.formatter.lexer.HeredocToken;
import org.eclipse.dltk.ruby.formatter.lexer.StringType;
import org.jruby.ast.AliasNode;
import org.jruby.ast.ArgsNode;
import org.jruby.ast.ArgumentNode;
import org.jruby.ast.ArrayNode;
import org.jruby.ast.AssignableNode;
import org.jruby.ast.BackRefNode;
import org.jruby.ast.BeginNode;
import org.jruby.ast.BlockAcceptingNode;
import org.jruby.ast.BlockArgNode;
import org.jruby.ast.BlockNode;
import org.jruby.ast.BlockPassNode;
import org.jruby.ast.BreakNode;
import org.jruby.ast.CallNode;
import org.jruby.ast.CaseNode;
import org.jruby.ast.ClassNode;
import org.jruby.ast.ClassVarNode;
import org.jruby.ast.Colon2Node;
import org.jruby.ast.Colon3Node;
import org.jruby.ast.ConstDeclNode;
import org.jruby.ast.DRegexpNode;
import org.jruby.ast.DStrNode;
import org.jruby.ast.DSymbolNode;
import org.jruby.ast.DXStrNode;
import org.jruby.ast.DefinedNode;
import org.jruby.ast.DefnNode;
import org.jruby.ast.DefsNode;
import org.jruby.ast.DotNode;
import org.jruby.ast.EnsureNode;
import org.jruby.ast.EvStrNode;
import org.jruby.ast.FCallNode;
import org.jruby.ast.FloatNode;
import org.jruby.ast.ForNode;
import org.jruby.ast.GlobalVarNode;
import org.jruby.ast.HashNode;
import org.jruby.ast.IfNode;
import org.jruby.ast.InstVarNode;
import org.jruby.ast.IterNode;
import org.jruby.ast.ListNode;
import org.jruby.ast.ModuleNode;
import org.jruby.ast.MultipleAsgnNode;
import org.jruby.ast.NewlineNode;
import org.jruby.ast.NextNode;
import org.jruby.ast.NilNode;
import org.jruby.ast.Node;
import org.jruby.ast.NotNode;
import org.jruby.ast.OpAsgnAndNode;
import org.jruby.ast.OpAsgnNode;
import org.jruby.ast.OpAsgnOrNode;
import org.jruby.ast.OpElementAsgnNode;
import org.jruby.ast.PostExeNode;
import org.jruby.ast.RedoNode;
import org.jruby.ast.RegexpNode;
import org.jruby.ast.RescueBodyNode;
import org.jruby.ast.RescueNode;
import org.jruby.ast.RetryNode;
import org.jruby.ast.ReturnNode;
import org.jruby.ast.SClassNode;
import org.jruby.ast.SValueNode;
import org.jruby.ast.SelfNode;
import org.jruby.ast.SplatNode;
import org.jruby.ast.StarNode;
import org.jruby.ast.StrNode;
import org.jruby.ast.SymbolNode;
import org.jruby.ast.ToAryNode;
import org.jruby.ast.UndefNode;
import org.jruby.ast.UntilNode;
import org.jruby.ast.VAliasNode;
import org.jruby.ast.WhenNode;
import org.jruby.ast.WhileNode;
import org.jruby.ast.XStrNode;
import org.jruby.ast.YieldNode;
import org.jruby.ast.ZArrayNode;
import org.jruby.ast.ZSuperNode;
import org.jruby.ast.ZeroArgNode;
import org.jruby.ast.ext.ElseNode;
import org.jruby.ast.ext.HeredocNode;
import org.jruby.ast.ext.PreExeNode;
import org.jruby.ast.types.ILiteralNode;
import org.jruby.ast.types.INameNode;
import org.jruby.common.IRubyWarnings;
import org.jruby.lexer.yacc.ISourcePosition;
import org.jruby.lexer.yacc.ISourcePositionHolder;
import org.jruby.lexer.yacc.LexState;
import org.jruby.lexer.yacc.LexerSource;
import org.jruby.lexer.yacc.RubyYaccLexer;
import org.jruby.lexer.yacc.StrTerm;
import org.jruby.lexer.yacc.SyntaxException;
import org.jruby.lexer.yacc.Token;
import org.jruby.runtime.Visibility;
import org.jruby.util.ByteList;
import org.jruby.util.IdUtil;

@SuppressWarnings("nls")
public class DefaultRubyParser {
    private ParserSupport support;
    private RubyYaccLexer lexer;
    private IRubyWarnings warnings;

    public DefaultRubyParser() {
        support = new ParserSupport();
        lexer = new RubyYaccLexer();
        lexer.setParserSupport(support);
    }

    public void setWarnings(IRubyWarnings warnings) {
        this.warnings = warnings;

        support.setWarnings(warnings);
        lexer.setWarnings(warnings);
    }
					// line 155 "-"
  // %token constants
  public static final int kCLASS = 257;
  public static final int kMODULE = 258;
  public static final int kDEF = 259;
  public static final int kUNDEF = 260;
  public static final int kBEGIN = 261;
  public static final int kRESCUE = 262;
  public static final int kENSURE = 263;
  public static final int kEND = 264;
  public static final int kIF = 265;
  public static final int kUNLESS = 266;
  public static final int kTHEN = 267;
  public static final int kELSIF = 268;
  public static final int kELSE = 269;
  public static final int kCASE = 270;
  public static final int kWHEN = 271;
  public static final int kWHILE = 272;
  public static final int kUNTIL = 273;
  public static final int kFOR = 274;
  public static final int kBREAK = 275;
  public static final int kNEXT = 276;
  public static final int kREDO = 277;
  public static final int kRETRY = 278;
  public static final int kIN = 279;
  public static final int kDO = 280;
  public static final int kDO_COND = 281;
  public static final int kDO_BLOCK = 282;
  public static final int kRETURN = 283;
  public static final int kYIELD = 284;
  public static final int kSUPER = 285;
  public static final int kSELF = 286;
  public static final int kNIL = 287;
  public static final int kTRUE = 288;
  public static final int kFALSE = 289;
  public static final int kAND = 290;
  public static final int kOR = 291;
  public static final int kNOT = 292;
  public static final int kIF_MOD = 293;
  public static final int kUNLESS_MOD = 294;
  public static final int kWHILE_MOD = 295;
  public static final int kUNTIL_MOD = 296;
  public static final int kRESCUE_MOD = 297;
  public static final int kALIAS = 298;
  public static final int kDEFINED = 299;
  public static final int klBEGIN = 300;
  public static final int klEND = 301;
  public static final int k__LINE__ = 302;
  public static final int k__FILE__ = 303;
  public static final int tIDENTIFIER = 304;
  public static final int tFID = 305;
  public static final int tGVAR = 306;
  public static final int tIVAR = 307;
  public static final int tCONSTANT = 308;
  public static final int tCVAR = 309;
  public static final int tNTH_REF = 310;
  public static final int tBACK_REF = 311;
  public static final int tSTRING_CONTENT = 312;
  public static final int tINTEGER = 313;
  public static final int tFLOAT = 314;
  public static final int tREGEXP_END = 315;
  public static final int tUPLUS = 316;
  public static final int tUMINUS = 317;
  public static final int tUMINUS_NUM = 318;
  public static final int tPOW = 319;
  public static final int tCMP = 320;
  public static final int tEQ = 321;
  public static final int tEQQ = 322;
  public static final int tNEQ = 323;
  public static final int tGEQ = 324;
  public static final int tLEQ = 325;
  public static final int tANDOP = 326;
  public static final int tOROP = 327;
  public static final int tMATCH = 328;
  public static final int tNMATCH = 329;
  public static final int tDOT = 330;
  public static final int tDOT2 = 331;
  public static final int tDOT3 = 332;
  public static final int tAREF = 333;
  public static final int tASET = 334;
  public static final int tLSHFT = 335;
  public static final int tRSHFT = 336;
  public static final int tCOLON2 = 337;
  public static final int tCOLON3 = 338;
  public static final int tOP_ASGN = 339;
  public static final int tASSOC = 340;
  public static final int tLPAREN = 341;
  public static final int tLPAREN2 = 342;
  public static final int tRPAREN = 343;
  public static final int tLPAREN_ARG = 344;
  public static final int tLBRACK = 345;
  public static final int tRBRACK = 346;
  public static final int tLBRACE = 347;
  public static final int tLBRACE_ARG = 348;
  public static final int tSTAR = 349;
  public static final int tSTAR2 = 350;
  public static final int tAMPER = 351;
  public static final int tAMPER2 = 352;
  public static final int tTILDE = 353;
  public static final int tPERCENT = 354;
  public static final int tDIVIDE = 355;
  public static final int tPLUS = 356;
  public static final int tMINUS = 357;
  public static final int tLT = 358;
  public static final int tGT = 359;
  public static final int tPIPE = 360;
  public static final int tBANG = 361;
  public static final int tCARET = 362;
  public static final int tLCURLY = 363;
  public static final int tRCURLY = 364;
  public static final int tBACK_REF2 = 365;
  public static final int tSYMBEG = 366;
  public static final int tSTRING_BEG = 367;
  public static final int tXSTRING_BEG = 368;
  public static final int tREGEXP_BEG = 369;
  public static final int tWORDS_BEG = 370;
  public static final int tQWORDS_BEG = 371;
  public static final int tSTRING_DBEG = 372;
  public static final int tSTRING_DVAR = 373;
  public static final int tSTRING_END = 374;
  public static final int tRDOC = 375;
  public static final int tLOWEST = 376;
  public static final int tLAST_TOKEN = 377;
  public static final int yyErrorCode = 256;

  /** number of final state.
    */
  protected static final int yyFinal = 1;

  /** parser tables.
      Order is mandated by <i>jay</i>.
    */
  protected static final short[] yyLhs = {
//yyLhs 499
    -1,   101,     0,    20,    19,    21,    21,    21,    21,   104,
    22,    22,    22,    22,    22,    22,    22,    22,    22,    22,
    22,   105,    22,    22,    22,    22,    22,    22,    22,    22,
    22,    22,    22,    22,    22,    22,    23,    23,    23,    23,
    23,    23,    27,    18,    18,    18,    18,    18,    43,    43,
    43,   106,    78,    26,    26,    26,    26,    26,    26,    26,
    26,    79,    79,    81,    81,    80,    80,    80,    80,    80,
    80,    51,    51,    67,    67,    52,    52,    52,    52,    52,
    52,    52,    52,    59,    59,    59,    59,    59,    59,    59,
    59,    91,    91,    17,    17,    17,    92,    92,    92,    92,
    92,    85,    85,    47,   108,    47,    93,    93,    93,    93,
    93,    93,    93,    93,    93,    93,    93,    93,    93,    93,
    93,    93,    93,    93,    93,    93,    93,    93,    93,    93,
    93,    93,   107,   107,   107,   107,   107,   107,   107,   107,
   107,   107,   107,   107,   107,   107,   107,   107,   107,   107,
   107,   107,   107,   107,   107,   107,   107,   107,   107,   107,
   107,   107,   107,   107,   107,   107,   107,   107,   107,   107,
   107,   107,   107,    24,    24,    24,    24,    24,    24,    24,
    24,    24,    24,    24,    24,    24,    24,    24,    24,    24,
    24,    24,    24,    24,    24,    24,    24,    24,    24,    24,
    24,    24,    24,    24,    24,    24,    24,    24,    24,    24,
    24,    24,    24,    24,    24,    24,    24,    53,    56,    56,
    56,    56,    56,    56,    36,    36,    36,    36,    41,    41,
    33,    33,    33,    33,    33,    33,    33,    33,    33,    34,
    34,    34,    34,    34,    34,    34,    34,    34,    34,    34,
    34,   111,    39,    35,   112,    35,   113,    35,    72,    71,
    71,    65,    65,    50,    50,    50,    25,    25,    25,    25,
    25,    25,    25,    25,    25,    25,   114,    25,    25,    25,
    25,    25,    25,    25,    25,    25,    25,    25,    25,    25,
    25,    25,    25,    25,   115,   117,    25,   118,   119,    25,
    25,    25,    25,   120,   121,    25,   122,    25,   123,   124,
    25,   125,    25,   126,    25,   127,   128,    25,    25,    25,
    25,    25,    28,    99,    99,    99,    99,   116,   116,   116,
    31,    31,    29,    29,    57,    57,    58,    58,    58,    58,
   129,    77,    42,    42,    42,    10,    10,    10,    10,    10,
    10,   130,    76,   131,    76,    54,    66,    66,    66,    30,
    30,    82,    82,    55,    55,    55,    32,    32,    38,    38,
    14,    14,    14,     2,     3,     3,   132,     4,   133,     5,
   134,     6,    11,   135,    11,    62,    62,    13,    13,    12,
    12,    61,    61,     7,     7,     8,     8,     9,   136,     9,
   137,     9,    48,    48,    48,    48,    87,    86,    86,    86,
    86,    16,    15,    15,    15,    15,    84,    84,    84,    84,
    84,    84,    84,    84,    84,    84,    84,    40,    83,    49,
    49,    37,   138,    37,    37,    44,    44,    45,    45,    45,
    45,    45,    45,    45,    45,    45,    94,    94,    94,    94,
    63,    63,    46,    64,    64,    97,    97,    95,    95,    98,
    98,    75,    74,    74,     1,   139,     1,    70,    70,    70,
    68,    68,    69,    69,    88,    88,    88,    89,    89,    89,
    89,    90,    90,    90,    96,    96,   102,   102,   109,   109,
   110,   110,   110,   100,   100,   103,   103,    60,    73,
    }, yyLen = {
//yyLen 499
     2,     0,     2,     4,     2,     1,     1,     3,     2,     0,
     4,     3,     3,     3,     2,     1,     3,     3,     3,     3,
     3,     0,     5,     4,     3,     3,     3,     6,     5,     5,
     5,     3,     3,     3,     3,     1,     1,     3,     3,     2,
     2,     1,     1,     1,     1,     2,     2,     2,     1,     4,
     4,     0,     5,     2,     3,     4,     5,     4,     5,     2,
     2,     1,     3,     1,     3,     1,     2,     3,     2,     2,
     1,     1,     3,     2,     3,     1,     4,     3,     3,     3,
     3,     2,     1,     1,     4,     3,     3,     3,     3,     2,
     1,     1,     1,     2,     1,     3,     1,     1,     1,     1,
     1,     1,     1,     1,     0,     4,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     3,     5,     3,     6,     5,     5,     5,
     5,     4,     3,     3,     3,     3,     3,     3,     3,     3,
     3,     4,     4,     2,     2,     3,     3,     3,     3,     3,
     3,     3,     3,     3,     3,     3,     3,     3,     2,     2,
     3,     3,     3,     3,     3,     5,     1,     1,     1,     2,
     2,     5,     2,     3,     3,     4,     4,     6,     1,     1,
     1,     2,     5,     2,     5,     4,     7,     3,     1,     4,
     3,     5,     7,     2,     5,     4,     6,     7,     9,     3,
     1,     0,     2,     1,     0,     3,     0,     4,     2,     2,
     1,     1,     3,     3,     4,     2,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     3,     0,     5,     3,     3,
     2,     4,     3,     3,     1,     4,     3,     1,     5,     2,
     1,     2,     6,     6,     0,     0,     7,     0,     0,     7,
     5,     4,     5,     0,     0,     9,     0,     6,     0,     0,
     8,     0,     5,     0,     6,     0,     0,     9,     1,     1,
     1,     1,     1,     1,     1,     1,     2,     1,     1,     1,
     1,     5,     1,     2,     1,     1,     1,     2,     1,     3,
     0,     5,     2,     4,     4,     2,     4,     4,     3,     2,
     1,     0,     5,     0,     5,     5,     1,     4,     2,     1,
     1,     6,     0,     1,     1,     1,     2,     1,     2,     1,
     1,     1,     1,     1,     1,     2,     0,     4,     0,     4,
     0,     4,     3,     0,     4,     0,     3,     1,     2,     3,
     3,     0,     3,     0,     2,     0,     2,     1,     0,     3,
     0,     4,     1,     1,     1,     1,     2,     1,     1,     1,
     1,     3,     1,     1,     2,     2,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     0,     4,     2,     4,     2,     6,     4,     4,
     2,     4,     2,     2,     1,     0,     1,     1,     1,     1,
     1,     3,     3,     1,     3,     1,     1,     2,     1,     1,
     1,     2,     2,     0,     1,     0,     5,     1,     2,     2,
     1,     3,     3,     3,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     0,     1,     0,     1,
     0,     1,     1,     1,     1,     1,     2,     0,     0,
    }, yyDefRed = {
//yyDefRed 892
     1,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,   294,   297,     0,     0,     0,   320,   321,     0,
     0,     0,   422,   421,   423,   424,     0,     0,     0,    21,
     0,   426,   425,     0,     0,   418,   417,     0,   420,   429,
   430,   412,   413,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,   376,   378,   380,     0,     0,
    15,   267,     0,   374,   268,   269,     0,   270,   271,   266,
   370,   372,    36,     2,     0,     0,     0,     0,     0,     0,
     0,   272,     0,    44,     0,     0,    71,     0,     5,     0,
     0,    61,     0,     0,   371,     0,     0,   318,   319,   284,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
   322,     0,   273,   427,     0,    94,   311,   141,   152,   142,
   165,   138,   158,   148,   147,   163,   146,   145,   140,   166,
   150,   139,   153,   157,   159,   151,   144,   160,   167,   162,
     0,     0,     0,     0,   137,   156,   155,   168,   169,   170,
   171,   172,   136,   143,   134,   135,     0,     0,     0,    98,
     0,   127,   128,   125,   109,   110,   111,   114,   116,   112,
   129,   130,   117,   118,   465,   122,   121,   108,   126,   124,
   123,   119,   120,   115,   113,   106,   107,   131,     0,   464,
   313,    99,   100,   161,   154,   164,   149,   132,   133,    96,
    97,     0,     0,   103,   102,   101,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,   494,   493,     0,
   495,     0,     0,     0,     0,     0,     0,     0,     0,   334,
   335,     0,     0,     0,     0,     0,   230,    46,     0,     0,
     0,   470,   238,     0,    47,    45,     0,    60,     0,     0,
   349,    59,    39,     0,     9,   489,     0,     0,     0,   193,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,   218,     0,     0,   467,     0,     0,     0,
     0,     0,     0,    69,     0,   209,    40,   208,   409,   408,
   410,     0,   406,   407,   393,   395,   395,     0,   385,     0,
     0,   375,   353,   351,   291,     4,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
   340,   342,     0,     0,     0,     0,     0,     0,    73,     0,
     0,     0,     0,     0,     0,   345,     0,   289,     0,   414,
   415,     0,    91,     0,    93,     0,   432,   306,   431,     0,
     0,     0,     0,     0,   484,   485,   315,     0,   104,     0,
     0,   275,     0,   325,   324,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,   496,     0,
     0,     0,     0,     0,     0,   303,     0,   258,     0,     0,
   231,   260,     0,   233,     0,   286,     0,     0,   253,   252,
     0,     0,     0,     0,     0,    11,    13,    12,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,   278,
     0,     0,     0,   219,   282,     0,   491,   220,     0,   222,
     0,   469,   468,   283,     0,     0,     0,     0,   397,   400,
   398,   411,   396,     0,     0,     0,   382,     0,   389,     0,
   390,     0,     0,     0,    16,    17,    18,    19,    20,    37,
    38,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,   479,
     0,     0,   480,     0,     0,     0,     0,   348,     0,     0,
   477,   478,     0,     0,    31,     0,     0,    24,     0,    32,
   261,     0,     0,    67,    74,    25,    34,     0,    26,     0,
    51,    54,     0,   434,     0,     0,     0,     0,     0,     0,
    95,     0,     0,     0,     0,     0,   447,   446,   448,     0,
   456,   455,   460,   459,     0,     0,   453,     0,     0,   444,
   450,     0,     0,     0,     0,   364,     0,     0,   365,     0,
     0,   332,     0,   326,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,   301,   329,   328,   327,
   295,   298,     0,     0,     0,     0,     0,     0,     0,   237,
   472,     0,     0,     0,   259,     0,     0,   471,   473,   285,
     0,     0,   256,     0,     0,   250,     0,     0,     0,     0,
     0,   224,     0,    10,     0,     0,    23,     0,     0,     0,
     0,     0,   223,     0,   262,     0,     0,     0,     0,     0,
     0,     0,   377,   394,   379,   381,   384,   387,     0,   392,
   338,     0,     0,   336,     0,     0,     0,     0,     0,   229,
     0,   346,   228,     0,     0,   347,     0,     0,    49,   343,
    50,   344,   265,     0,     0,    72,     0,   309,     0,     0,
   281,   312,     0,   316,     0,     0,     0,   436,     0,   440,
     0,   442,     0,   443,   457,   461,   105,     0,     0,   367,
   333,     0,     3,   369,     0,   330,     0,     0,     0,     0,
     0,     0,   300,   302,   358,     0,     0,     0,     0,     0,
     0,     0,     0,   235,     0,     0,     0,     0,     0,   243,
   255,   225,     0,     0,   226,     0,     0,   288,    22,   277,
     0,     0,     0,   402,   403,   404,   399,   405,   386,   388,
   337,     0,     0,     0,     0,     0,    28,     0,    29,     0,
    56,    30,     0,     0,    58,     0,     0,     0,     0,     0,
     0,   433,   307,   466,     0,   452,     0,   314,     0,   462,
   451,     0,     0,   454,     0,     0,     0,     0,   366,     0,
     0,   368,     0,   292,     0,   293,     0,     0,     0,     0,
   304,   232,     0,   234,   249,   257,     0,     0,     0,   240,
     0,     0,   221,   401,   339,   354,   352,   341,    27,     0,
   264,     0,     0,     0,   435,     0,   438,   439,   441,     0,
     0,     0,     0,     0,     0,   357,   359,   355,   360,   296,
   299,     0,     0,     0,     0,   239,     0,   245,     0,   227,
    52,   310,     0,     0,     0,     0,     0,     0,     0,   361,
     0,     0,   236,   241,     0,     0,     0,   244,   317,   437,
     0,   331,   305,     0,     0,   246,     0,   242,     0,   247,
     0,   248,
    }, yyDgoto = {
//yyDgoto 140
     1,   188,    61,    62,    63,    64,    65,   463,   291,   462,
    66,    67,    68,   658,    69,    70,    71,   109,    72,   206,
   207,    74,    75,    76,    77,    78,    79,   210,   260,   715,
   847,   716,   708,   237,   622,   419,   669,   367,   712,   247,
    81,   671,    82,    83,   564,   565,   566,   202,   756,   212,
   529,    85,    86,   238,   397,   577,   272,   228,   662,   213,
    88,   300,   467,   567,   568,   274,   595,    89,   275,   241,
   279,   410,   614,   411,   699,   789,   357,   341,   541,    90,
    91,   268,   380,   214,   215,   203,   292,    94,   114,   546,
   517,   115,   205,   512,   570,   571,   376,   572,   573,   385,
   386,     2,   221,   222,   428,   257,   686,   192,   574,   256,
   447,   248,   626,   736,   441,   223,   600,   727,   224,   728,
   607,   851,   545,   542,   780,   372,   377,   554,   784,   507,
   472,   471,   294,   295,   296,   298,   651,   650,   544,   373,
    }, yySindex = {
//yySindex 892
     0,     0,  3838,  5276, 16399, 16744, 17309, 17199,  3838,  4832,
  4832,  4715,     0,     0, 16514, 13639, 13639,     0,     0, 13639,
  -154,  -125,     0,     0,     0,     0,  4832, 17089,   221,     0,
  -129,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0, 16054, 16054,  -162,   -71,  4318,  4832, 15019,
 16054, 16859, 16054, 16169, 17418,     0,     0,     0,   210,   216,
     0,     0,   -83,     0,     0,     0,  -188,     0,     0,     0,
     0,     0,     0,     0,    99,   375,   -96,  3378,     0,    -6,
    17,     0,  -182,     0,   -53,   246,     0,   237,     0, 16629,
   240,     0,   -34,     0,     0,  -137,   375,     0,     0,     0,
  -154,  -125,   221,     0,     0,   -54,  4832,  -143,  3838,    41,
     0,   113,     0,     0,  -137,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,  -148,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0, 17418,   278,     0,     0,     0,    67,   102,    38,   -96,
    88,   149,    -7,   312,    53,     0,    88,     0,     0,    99,
     0,    37,   280,  4832,  4832,    70,   166,     0,   175,     0,
     0,     0, 16054, 16054, 16054,  3378,     0,     0,   129,   434,
   446,     0,     0,   425,     0,     0,  5794,     0, 13754, 13639,
     0,     0,     0,   -56,     0,     0, 15134,   132,  3838,     0,
   205,   187,   212,   220,   182,  4318,   203,     0,   207,   -96,
 16054,   221,   191,     0,   105,   112,     0,   126,   112,   189,
   247,   386,     0,     0,     0,     0,     0,     0,     0,     0,
     0,  -205,     0,     0,     0,     0,     0,   184,     0,   186,
  -195,     0,     0,     0,     0,     0, 13409,  4832,  4832,  4832,
  4832,  5276,  4832,  4832, 16054, 16054, 16054, 16054, 16054, 16054,
 16054, 16054, 16054, 16054, 16054, 16054, 16054, 16054, 16054, 16054,
 16054, 16054, 16054, 16054, 16054, 16054, 16054, 16054, 16054, 16054,
     0,     0, 17586, 17645, 15019, 17704, 17704, 16169,     0, 15249,
  4318, 16859,   523, 15249, 16169,     0,   222,     0,   232,     0,
     0,   -96,     0,     0,     0,    99,     0,     0,     0, 17704,
 17763, 15019,  3838,  4832,     0,     0,     0,  1071,     0, 15364,
   303,     0,   182,     0,     0,  3838,   321, 17822, 17881, 15019,
 16054, 16054, 16054,  3838,   320,  3838, 15479,   368,     0,    72,
    72,     0, 17940, 17999, 15019,     0,   605,     0, 16054, 13869,
     0,     0, 13984,     0, 16054,     0,   308, 13524,     0,     0,
    -6,   221,   103,   311,   617,     0,     0,     0, 17199,  4832,
  3378,  3838,   315, 17822, 17881, 16054, 16054, 16054,   341,     0,
     0,   221,  2426,     0,     0, 15594,     0,     0, 16054,     0,
 16054,     0,     0,     0,     0, 18058, 18117, 15019,     0,     0,
     0,     0,     0,  -196,  -159,   -91,     0,   371,     0,   654,
     0,  -177,  -177,   375,     0,     0,     0,     0,     0,     0,
     0,   187,  2264,  2264,  2264,  2264,  2909,  2909,  2842,  2362,
  2264,  2264,  2308,  2308,   609,   609,   187,  1312,   187,   187,
   -61,   -61,  2909,  2909,   974,   974,  5125,  -177,   350,     0,
   352,  -125,     0,   356,     0,   361,  -125,     0,     0,   344,
     0,     0,  -125,  -125,     0,  3378, 16054,     0,  2898,     0,
     0,   658,   372,     0,     0,     0,     0,     0,     0,  3378,
     0,     0,    99,     0,  4832,  3838,  -125,     0,     0,  -125,
     0,   381,   450,    40, 17527,   667,     0,     0,     0,   896,
     0,     0,     0,     0,  3838,    99,     0,   687,   689,     0,
     0,   693,   438,   442, 17199,     0,     0,   408,     0,  3838,
   487,     0,   295,     0,   415,   417,   428,   361,   418,  2898,
   303,   508,   515, 16054,   742,    88,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,   441,  4832,   437,     0,
     0, 16054,   129,   750,     0, 16054,   129,     0,     0,     0,
 16054,  3378,     0,    35,   751,     0,   454,   456, 17704, 17704,
   460,     0, 14099,     0,   -63,   444,     0,   187,   187,  3378,
     0,   468,     0, 16054,     0,     0,     0,     0,     0,   474,
  3838,   275,     0,     0,     0,     0,     0,     0,    12,     0,
     0, 16284,  3838,     0,  3838, 16054,  3838, 16169, 16169,     0,
   222,     0,     0, 16169, 16054,     0,   222,   482,     0,     0,
     0,     0,     0, 16054, 15709,     0,  -177,     0,    99,   571,
     0,     0,   498,     0, 16054,   221,   586,     0,   896,     0,
   646,     0,   314,     0,     0,     0,     0, 16974,    88,     0,
     0,  3838,     0,     0,  4832,     0,   595, 16054, 16054, 16054,
   524,   600,     0,     0,     0, 15824,  3838,  3838,  3838,     0,
    72,   605, 14214,     0,   605,   605,   522, 14329, 14444,     0,
     0,     0,  -125,  -125,     0,    -6,   103,     0,     0,     0,
  2426,     0,   504,     0,     0,     0,     0,     0,     0,     0,
     0,   509,   608,   514,  3378,   615,     0,  3378,     0,  3378,
     0,     0,  3378,  3378,     0, 16169,  3378, 16054,     0,  3838,
  3838,     0,     0,     0,  1071,     0,   541,     0,   843,     0,
     0,   693,   667,     0,   693,   580,   489,     0,     0,     0,
  3838,     0,    88,     0, 16054,     0, 16054,    46,   625,   630,
     0,     0, 16054,     0,     0,     0, 16054,   852,   859,     0,
 16054,   563,     0,     0,     0,     0,     0,     0,     0,  3378,
     0,   543,   647,  3838,     0,   646,     0,     0,     0,     0,
 18176, 18235, 15019,    67,  3838,     0,     0,     0,     0,     0,
     0,  3838,  4167,   605, 14559,     0, 14674,     0,   605,     0,
     0,     0,   648,   693,     0,     0,     0,     0,   564,     0,
   295,   650,     0,     0, 16054,   871, 16054,     0,     0,     0,
     0,     0,     0,   605, 14789,     0,   605,     0, 16054,     0,
   605,     0,
    }, yyRindex = {
//yyRindex 892
     0,     0,   134,     0,     0,     0,     0,     0,   584,     0,
     0,   241,     0,     0,     0,  8550,  8669,     0,     0,  8792,
  4589,  3994,     0,     0,     0,     0,     0,     0, 15939,     0,
     0,     0,     0,  2071,  3149,     0,     0,  2189,     0,     0,
     0,     0,     0,     0,     0,     0,     0,    22,     0,   577,
   560,   110,     0,     0,   410,     0,     0,     0,   451,  -183,
     0,     0,  7824,     0,     0,     0,  7943,     0,     0,     0,
     0,     0,     0,     0,   538,   565,  2787, 13147,  8066, 13185,
     0,     0, 13229,     0,  9756,     0,     0,     0,     0,   208,
     0,     0,     0,  1946,     0, 14904,  1294,     0,     0,     0,
  8185,  6969,   575,  5915,  6036,     0,     0,     0,    22,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
   771,  1571,  1643,  1835,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,  2796,  3276,  3698,     0,
  3709,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,  3267,     0,     0,     0,    49,     0,     0, 13314,
     0,     0,  7340,     0,     0,  7098,     0,     0,     0,   663,
     0,     0,   258,     0,     0,     0,     0,   431,     0,     0,
     0,   553,     0,     0,     0, 12062,     0,     0, 12871,  2313,
  2313,     0,     0,  7459,     0,     0,     0,     0,     0,   592,
     0,     0,     0,     0,     0,     0,     0,     0,    32,     0,
     0,  8911,  8308,  8427,  9875,    22,     0,   -33,     0,    57,
     0,   594,     0,     0,   602,   602,     0,   582,   582,     0,
     0,     0,   705,     0,   865,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,   629,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,   577,     0,     0,     0,     0,     0,
    22,   218,   230,     0,     0,     0, 13001,     0,     0,     0,
     0,   150,     0,  6437,     0,     0,     0,     0,     0,     0,
     0,   577,   584,     0,     0,     0,     0,   164,     0,   180,
   363,     0,  7582,     0,     0,   413,  6558,     0,     0,   577,
     0,     0,     0,   439,     0,   163,     0,     0,     0,     0,
     0,   713,     0,     0,   577,     0,  2313,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,   598,     0,     0,
    60,   610,   610,     0,    73,     0,     0,     0,     0,     0,
 12166,    32,     0,     0,     0,     0,     0,     0,     0,     0,
    28,   610,   594,     0,     0,   614,     0,     0,  -223,     0,
   591,     0,     0,     0,  1290,     0,     0,   577,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,  6698,  6827,  1450,     0,     0,     0,     0,     0,     0,
     0,  9032,  1636, 11375, 11483, 11570, 10933, 11041, 11673, 11938,
 11761, 11848,  1805, 12026, 10359, 10478,  9153, 10599,  9272,  9393,
 10117, 10238, 11149, 11267, 10704, 10815,     0,  6698,  4952,     0,
  5067,  4109,     0,  5432,  3514,  5547, 14904,     0,  3629,     0,
     0,     0,  5674,  5674,     0, 12203,     0,     0,  1207,     0,
     0,     0,     0,     0,     0,     0,     0, 11391,     0, 12291,
     0,     0,     0,     0,     0,   584,  7211,  6176,  6297,     0,
     0,     0,     0,   610,     0,    31,     0,     0,     0,    75,
     0,     0,     0,     0,   584,     0,     0,    55,    55,     0,
     0,    55,    66,     0,     0,     0,    68,   282,     0,   261,
   707,     0,   707,     0,  2554,  2669,  3034,  4474,     0,  1495,
   707,     0,     0,     0,   285,     0,     0,     0,     0,     0,
     0,     0,  1692,  1798,  1837,   357,     0,     0,     0,     0,
     0,     0, 12907,  2313,     0,     0,     0,     0,     0,     0,
     0,    91,     0,     0,   639,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,  9514,  9633, 12327,
    19,     0,     0,     0,     0,  1169,  1470,  1524,   432,     0,
    32,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,   163,     0,    32,     0,   163,     0,     0,     0,
 13044,     0,     0,     0,     0,     0, 13087,  9998,     0,     0,
     0,     0,     0,     0,     0,     0,  6827,     0,     0,     0,
     0,     0,     0,     0,     0,   610,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,   163,     0,     0,     0,     0,     0,     0,     0,     0,
  7701,     0,     0,     0,     0,     0,   202,   163,   163,   727,
     0,  2313,     0,     0,  2313,   639,     0,     0,     0,     0,
     0,     0,    74,    74,     0,     0,   610,     0,     0,     0,
   594,  1875,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0, 12431,     0,     0, 12469,     0, 12556,
     0,     0, 12592, 12686,     0,     0, 12746,     0, 13274,    32,
   584,     0,     0,     0,   164,     0,     0,     0,    55,     0,
     0,    55,     0,     0,    55,     0,     0,   174,     0,   694,
   584,     0,     0,     0,     0,     0,     0,   707,     0,     0,
     0,     0,     0,     0,     0,     0,     0,   639,   639,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0, 12784,
     0,     0,     0,   584,     0,     0,     0,     0,     0,   780,
     0,     0,   577,    49,   413,     0,     0,     0,     0,     0,
     0,   163,  2313,   639,     0,     0,     0,     0,   639,     0,
     0,     0,     0,    55,   152,   171,   827,   177,     0,     0,
   707,     0,     0,     0,     0,   639,     0,     0,     0,     0,
   960,     0,     0,   639,     0,     0,   639,     0,     0,     0,
   639,     0,
    }, yyGindex = {
//yyGindex 140
     0,     0,     0,     0,   921,     0,     0,     0,   351,  -335,
     0,     0,     0,     0,     0,     0,     0,   982,   -28,   420,
  -345,     0,   115,    51,   284,     6,    24,   153,   928,  -365,
     0,   118,     0,  1010,     0,     0,    10,     0,     0,    25,
   983,   119,  -226,     0,   206,   445,  -629,     0,     0,   194,
  -161,   902,     1,  1443,  -368,     0,  -285,   339,  -370,   704,
   936,     0,     0,     0,   307,    18,     0,     0,     3,  -382,
     0,   519,    39,     0,    82,  -339,   941,     0,  -490,    -9,
   -12,  -191,   165,   872,    -2,    -3,     0,     9,  1380,  -249,
     0,   -19,    15,     2,   313,  -558,     0,     0,     0,  -209,
    92,     0,    59,   938,     0,     0,     0,     0,     0,    43,
   539,     0,     0,     0,     0,     0,  -336,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
    };
    protected static final short[] yyTable = YyTables.yyTable();
    protected static final short[] yyCheck = YyTables.yyCheck();

  /** maps symbol value to printable name.
      @see #yyExpecting
    */
  protected static final String[] yyNames = {
    "end-of-file",null,null,null,null,null,null,null,null,null,"'\\n'",
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,"' '",null,null,null,null,null,
    null,null,null,null,null,null,"','",null,null,null,null,null,null,
    null,null,null,null,null,null,null,"':'","';'",null,"'='",null,"'?'",
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,
    "'['",null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,
    "kCLASS","kMODULE","kDEF","kUNDEF","kBEGIN","kRESCUE","kENSURE",
    "kEND","kIF","kUNLESS","kTHEN","kELSIF","kELSE","kCASE","kWHEN",
    "kWHILE","kUNTIL","kFOR","kBREAK","kNEXT","kREDO","kRETRY","kIN",
    "kDO","kDO_COND","kDO_BLOCK","kRETURN","kYIELD","kSUPER","kSELF",
    "kNIL","kTRUE","kFALSE","kAND","kOR","kNOT","kIF_MOD","kUNLESS_MOD",
    "kWHILE_MOD","kUNTIL_MOD","kRESCUE_MOD","kALIAS","kDEFINED","klBEGIN",
    "klEND","k__LINE__","k__FILE__","tIDENTIFIER","tFID","tGVAR","tIVAR",
    "tCONSTANT","tCVAR","tNTH_REF","tBACK_REF","tSTRING_CONTENT",
    "tINTEGER","tFLOAT","tREGEXP_END","tUPLUS","tUMINUS","tUMINUS_NUM",
    "tPOW","tCMP","tEQ","tEQQ","tNEQ","tGEQ","tLEQ","tANDOP","tOROP",
    "tMATCH","tNMATCH","tDOT","tDOT2","tDOT3","tAREF","tASET","tLSHFT",
    "tRSHFT","tCOLON2","tCOLON3","tOP_ASGN","tASSOC","tLPAREN","tLPAREN2",
    "tRPAREN","tLPAREN_ARG","tLBRACK","tRBRACK","tLBRACE","tLBRACE_ARG",
    "tSTAR","tSTAR2","tAMPER","tAMPER2","tTILDE","tPERCENT","tDIVIDE",
    "tPLUS","tMINUS","tLT","tGT","tPIPE","tBANG","tCARET","tLCURLY",
    "tRCURLY","tBACK_REF2","tSYMBEG","tSTRING_BEG","tXSTRING_BEG",
    "tREGEXP_BEG","tWORDS_BEG","tQWORDS_BEG","tSTRING_DBEG",
    "tSTRING_DVAR","tSTRING_END","tRDOC","tLOWEST","tLAST_TOKEN",
    };

  /** thrown for irrecoverable syntax errors and stack overflow.
      Nested for convenience, does not depend on parser class.
    */
  public static class yyException extends java.lang.Exception {
    private static final long serialVersionUID = 1L;
    public yyException (String message) {
      super(message);
    }
  }

  /** must be implemented by a scanner object to supply input to the parser.
      Nested for convenience, does not depend on parser class.
    */
  public interface yyInput {

    /** move on to next token.
        @return <tt>false</tt> if positioned beyond tokens.
        @throws IOException on input error.
      */
    boolean advance () throws java.io.IOException;

    /** classifies current token.
        Should not be called if {@link #advance()} returned <tt>false</tt>.
        @return current <tt>%token</tt> or single character.
      */
    int token ();

    /** associated with current token.
        Should not be called if {@link #advance()} returned <tt>false</tt>.
        @return value for {@link #token()}.
      */
    Object value ();
  }

  /** simplified error message.
      @see #yyerror(java.lang.String, java.lang.String[])
    */
  public void yyerror (String message) {
    yyerror(message, null, null);
  }

  /** (syntax) error message.
      Can be overwritten to control message format.
      @param message text to be displayed.
      @param expected list of acceptable tokens, if available.
    */
  public void yyerror(String message, String[] expected, String found) {
		if (found != null) {
			throw new SyntaxException(getPosition(null), message
					+ ", unexpected " + found);
		} else {
			throw new SyntaxException(getPosition(null), message);
		}
	}

  /** computes list of expected tokens on error by tracing the tables.
      @param state for which to compute the list.
      @return list of token names.
    */
  protected String[] yyExpecting (int state) {
    int token, n, len = 0;
    boolean[] ok = new boolean[yyNames.length];

    if ((n = yySindex[state]) != 0)
      for (token = n < 0 ? -n : 0;
           token < yyNames.length && n+token < yyTable.length; ++ token)
        if (yyCheck[n+token] == token && !ok[token] && yyNames[token] != null) {
          ++ len;
          ok[token] = true;
        }
    if ((n = yyRindex[state]) != 0)
      for (token = n < 0 ? -n : 0;
           token < yyNames.length && n+token < yyTable.length; ++ token)
        if (yyCheck[n+token] == token && !ok[token] && yyNames[token] != null) {
          ++ len;
          ok[token] = true;
        }

    String result[] = new String[len];
    for (n = token = 0; n < len;  ++ token)
      if (ok[token]) result[n++] = yyNames[token];
    return result;
  }

  /** the generated parser, with debugging messages.
      Maintains a dynamic state and value stack.
      @param yyLex scanner.
      @param yydebug debug message writer implementing <tt>yyDebug</tt>, or <tt>null</tt>.
      @return result of the last reduction, if any.
      @throws yyException on irrecoverable parse error.
    */
  public Object yyparse (RubyYaccLexer yyLex, Object ayydebug)
				throws java.io.IOException, yyException {
    return yyparse(yyLex);
  }

  /** initial size and increment of the state/value stack [default 256].
      This is not final so that it can be overwritten outside of invocations
      of {@link #yyparse}.
    */
  protected int yyMax;

  /** executed at the beginning of a reduce action.
      Used as <tt>$$ = yyDefault($1)</tt>, prior to the user-specified action, if any.
      Can be overwritten to provide deep copy, etc.
      @param first value for <tt>$1</tt>, or <tt>null</tt>.
      @return first.
    */
  protected Object yyDefault (Object first) {
    return first;
  }

  /** the generated parser.
      Maintains a dynamic state and value stack.
      @param yyLex scanner.
      @return result of the last reduction, if any.
      @throws yyException on irrecoverable parse error.
    */
  public Object yyparse (RubyYaccLexer yyLex) throws java.io.IOException, yyException {
    if (yyMax <= 0) yyMax = 256;			// initial size
    int yyState = 0, yyStates[] = new int[yyMax];	// state stack
    Object yyVal = null, yyVals[] = new Object[yyMax];	// value stack
    int yyToken = -1;					// current input
    int yyErrorFlag = 0;				// #tokens to shift

    yyLoop: for (int yyTop = 0;; ++ yyTop) {
      if (yyTop >= yyStates.length) {			// dynamically increase
        int[] i = new int[yyStates.length+yyMax];
        System.arraycopy(yyStates, 0, i, 0, yyStates.length);
        yyStates = i;
        Object[] o = new Object[yyVals.length+yyMax];
        System.arraycopy(yyVals, 0, o, 0, yyVals.length);
        yyVals = o;
      }
      yyStates[yyTop] = yyState;
      yyVals[yyTop] = yyVal;

      yyDiscarded: for (;;) {	// discarding a token does not change stack
        int yyN;
        if ((yyN = yyDefRed[yyState]) == 0) {	// else [default] reduce (yyN)
          if (yyToken < 0) {
            yyToken = yyLex.advance() ? yyLex.token() : 0;
          }
          if ((yyN = yySindex[yyState]) != 0 && (yyN += yyToken) >= 0
              && yyN < yyTable.length && yyCheck[yyN] == yyToken) {
            yyState = yyTable[yyN];		// shift to yyN
            yyVal = yyLex.value();
            yyToken = -1;
            if (yyErrorFlag > 0) -- yyErrorFlag;
            continue yyLoop;
          }
          if ((yyN = yyRindex[yyState]) != 0 && (yyN += yyToken) >= 0
              && yyN < yyTable.length && yyCheck[yyN] == yyToken)
            yyN = yyTable[yyN];			// reduce (yyN)
          else
            switch (yyErrorFlag) {
  
            case 0:
              yyerror("syntax error", yyExpecting(yyState), yyNames[yyToken]);
  
            case 1: case 2:
              yyErrorFlag = 3;
              do {
                if ((yyN = yySindex[yyStates[yyTop]]) != 0
                    && (yyN += yyErrorCode) >= 0 && yyN < yyTable.length
                    && yyCheck[yyN] == yyErrorCode) {
                  yyState = yyTable[yyN];
                  yyVal = yyLex.value();
                  continue yyLoop;
                }
              } while (-- yyTop >= 0);
              throw new yyException("irrecoverable syntax error");
  
            case 3:
              if (yyToken == 0) {
                throw new yyException("irrecoverable syntax error at end-of-file");
              }
              yyToken = -1;
              continue yyDiscarded;		// leave stack alone
            }
        }
        int yyV = yyTop + 1-yyLen[yyN];
        yyVal = yyDefault(yyV > yyTop ? null : yyVals[yyV]);
        switch (yyN) {
case 1:
					// line 268 "DefaultRubyParser.y"
  {
                  lexer.setState(LexState.EXPR_BEG);
                  support.initTopLocalVariables();
              }
  break;
case 2:
					// line 271 "DefaultRubyParser.y"
  {
                  if (((Node)yyVals[0+yyTop]) != null) {
                      /* last expression should not be void */
                      if (((Node)yyVals[0+yyTop]) instanceof BlockNode) {
                          support.checkUselessStatement(((BlockNode)yyVals[0+yyTop]).getLast());
                      } else {
                          support.checkUselessStatement(((Node)yyVals[0+yyTop]));
                      }
                  }
                  support.getResult().setAST(support.addRootNode(((Node)yyVals[0+yyTop]), getPosition(((Node)yyVals[0+yyTop]))));
              }
  break;
case 3:
					// line 283 "DefaultRubyParser.y"
  {
                  Node node = ((Node)yyVals[-3+yyTop]);

		  if (((RescueBodyNode)yyVals[-2+yyTop]) != null) {
		      node = new RescueNode(getPosition(((Node)yyVals[-3+yyTop]), true), ((Node)yyVals[-3+yyTop]), ((RescueBodyNode)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]));
		  } else if (((Node)yyVals[-1+yyTop]) != null) {
		      warnings.warn(getPosition(((Node)yyVals[-3+yyTop])), "else without rescue is useless");
		      node = new RescueNode(getPosition(((Node)yyVals[-3+yyTop]), true), ((Node)yyVals[-3+yyTop]), null, ((Node)yyVals[-1+yyTop]));
                      /* node = support.appendToBlock($1, $3);*/
		  }
		  if (((EnsureNode.Keyword)yyVals[0+yyTop]) != null) {
		      node = new EnsureNode(getPosition(((Node)yyVals[-3+yyTop])), node, ((EnsureNode.Keyword)yyVals[0+yyTop]));
		  }

	          yyVal = node;
              }
  break;
case 4:
					// line 300 "DefaultRubyParser.y"
  {
                  if (((Node)yyVals[-1+yyTop]) instanceof BlockNode) {
                      support.checkUselessStatements(((BlockNode)yyVals[-1+yyTop]));
		  }
                  yyVal = ((Node)yyVals[-1+yyTop]);
              }
  break;
case 6:
					// line 308 "DefaultRubyParser.y"
  {
                  yyVal = support.newline_node(((Node)yyVals[0+yyTop]), getPosition(((Node)yyVals[0+yyTop]), true));
              }
  break;
case 7:
					// line 311 "DefaultRubyParser.y"
  {
	          yyVal = support.appendToBlock(((Node)yyVals[-2+yyTop]), support.newline_node(((Node)yyVals[0+yyTop]), getPosition(((Node)yyVals[0+yyTop]), true)));
              }
  break;
case 8:
					// line 314 "DefaultRubyParser.y"
  {
                  yyVal = ((Node)yyVals[0+yyTop]);
              }
  break;
case 9:
					// line 318 "DefaultRubyParser.y"
  {
                  lexer.setState(LexState.EXPR_FNAME);
              }
  break;
case 10:
					// line 320 "DefaultRubyParser.y"
  {
                  yyVal = new AliasNode(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])), (String) ((Token)yyVals[-2+yyTop]).getValue(), (String) ((Token)yyVals[0+yyTop]).getValue());
              }
  break;
case 11:
					// line 323 "DefaultRubyParser.y"
  {
                  yyVal = new VAliasNode(getPosition(((Token)yyVals[-2+yyTop])), (String) ((Token)yyVals[-1+yyTop]).getValue(), (String) ((Token)yyVals[0+yyTop]).getValue());
              }
  break;
case 12:
					// line 326 "DefaultRubyParser.y"
  {
                  yyVal = new VAliasNode(getPosition(((Token)yyVals[-2+yyTop])), (String) ((Token)yyVals[-1+yyTop]).getValue(), "$" + ((BackRefNode)yyVals[0+yyTop]).getType()); /* XXX*/
              }
  break;
case 13:
					// line 329 "DefaultRubyParser.y"
  {
                  yyerror("can't make alias for the number variables");
              }
  break;
case 14:
					// line 332 "DefaultRubyParser.y"
  {
                  yyVal = ((Node)yyVals[0+yyTop]);
              }
  break;
case 15:
					// line 335 "DefaultRubyParser.y"
  {
                  yyVal = lexer.comment();
              }
  break;
case 16:
					// line 338 "DefaultRubyParser.y"
  {
                  yyVal = new IfNode.Inline(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), null);
              }
  break;
case 17:
					// line 341 "DefaultRubyParser.y"
  {
                  yyVal = new IfNode.InlineUnless(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), null, ((Node)yyVals[-2+yyTop]));
              }
  break;
case 18:
					// line 344 "DefaultRubyParser.y"
  {
                  if (((Node)yyVals[-2+yyTop]) != null && ((Node)yyVals[-2+yyTop]) instanceof BeginNode) {
                      yyVal = new WhileNode.Inline(getPosition(((Node)yyVals[-2+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), false, ((Token)yyVals[-1+yyTop]));
                  } else {
                      yyVal = new WhileNode.Inline(getPosition(((Node)yyVals[-2+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), true, ((Token)yyVals[-1+yyTop]));
                  }
              }
  break;
case 19:
					// line 351 "DefaultRubyParser.y"
  {
                  if (((Node)yyVals[-2+yyTop]) != null && ((Node)yyVals[-2+yyTop]) instanceof BeginNode) {
                      yyVal = new UntilNode.Inline(getPosition(((Node)yyVals[-2+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), ((Token)yyVals[-1+yyTop]));
                  } else {
                      yyVal = new UntilNode.Inline(getPosition(((Node)yyVals[-2+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), ((Token)yyVals[-1+yyTop]));
                  }
              }
  break;
case 20:
					// line 358 "DefaultRubyParser.y"
  {
	          yyVal = new RescueNode.Inline(getPosition(((Node)yyVals[-2+yyTop])), ((Node)yyVals[-2+yyTop]), new RescueBodyNode(getPosition(((Node)yyVals[-2+yyTop])), null,((Node)yyVals[0+yyTop]), null, null), null);
              }
  break;
case 21:
					// line 361 "DefaultRubyParser.y"
  {
                  if (support.isInDef() || support.isInSingle()) {
                      yyerror("BEGIN in method");
                  }
		  support.pushLocalScope();
              }
  break;
case 22:
					// line 366 "DefaultRubyParser.y"
  {
                  support.popCurrentScope();
                  yyVal = new PreExeNode(((Token)yyVals[-4+yyTop]), ((Token)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop]));
              }
  break;
case 23:
					// line 370 "DefaultRubyParser.y"
  {
                  if (support.isInDef() || support.isInSingle()) {
                      yyerror("END in method; use at_exit");
                  }

                  yyVal = new PostExeNode(getPosition(((Token)yyVals[-3+yyTop])), ((Node)yyVals[-1+yyTop]), ((Token)yyVals[-3+yyTop]), ((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]));
              }
  break;
case 24:
					// line 377 "DefaultRubyParser.y"
  {
                  support.checkExpression(((Node)yyVals[0+yyTop]));
                  yyVal = support.node_assign(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
              }
  break;
case 25:
					// line 381 "DefaultRubyParser.y"
  {
                  support.checkExpression(((Node)yyVals[0+yyTop]));
		  if (((MultipleAsgnNode)yyVals[-2+yyTop]).getHeadNode() != null) {
		      ((MultipleAsgnNode)yyVals[-2+yyTop]).setValueNode(new ToAryNode(getPosition(((MultipleAsgnNode)yyVals[-2+yyTop])), ((Node)yyVals[0+yyTop])));
		  } else {
		      ((MultipleAsgnNode)yyVals[-2+yyTop]).setValueNode(new ArrayNode(getPosition(((MultipleAsgnNode)yyVals[-2+yyTop])), ((Node)yyVals[0+yyTop])));
		  }
		  yyVal = ((MultipleAsgnNode)yyVals[-2+yyTop]);
              }
  break;
case 26:
					// line 390 "DefaultRubyParser.y"
  {
 	          support.checkExpression(((Node)yyVals[0+yyTop]));

	          String name = ((INameNode)yyVals[-2+yyTop]).getName();
		  String asgnOp = (String) ((Token)yyVals[-1+yyTop]).getValue();
		  if (asgnOp.equals("||")) {
	              ((AssignableNode)yyVals[-2+yyTop]).setValueNode(((Node)yyVals[0+yyTop]));
	              yyVal = new OpAsgnOrNode(support.union(((AssignableNode)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), support.gettable2(name, ((AssignableNode)yyVals[-2+yyTop]).getPosition()), ((AssignableNode)yyVals[-2+yyTop]));
		  } else if (asgnOp.equals("&&")) {
	              ((AssignableNode)yyVals[-2+yyTop]).setValueNode(((Node)yyVals[0+yyTop]));
                      yyVal = new OpAsgnAndNode(support.union(((AssignableNode)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), support.gettable2(name, ((AssignableNode)yyVals[-2+yyTop]).getPosition()), ((AssignableNode)yyVals[-2+yyTop]));
		  } else {
                      ((AssignableNode)yyVals[-2+yyTop]).setValueNode(support.getOperatorCallNode(support.gettable2(name, ((AssignableNode)yyVals[-2+yyTop]).getPosition()), asgnOp, ((Node)yyVals[0+yyTop])));
                      ((AssignableNode)yyVals[-2+yyTop]).setPosition(support.union(((AssignableNode)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])));
		      yyVal = ((AssignableNode)yyVals[-2+yyTop]);
		  }
	      }
  break;
case 27:
					// line 407 "DefaultRubyParser.y"
  {
                  yyVal = new OpElementAsgnNode(getPosition(((Node)yyVals[-5+yyTop])), ((Node)yyVals[-5+yyTop]), (String) ((Token)yyVals[-1+yyTop]).getValue(), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop]));

              }
  break;
case 28:
					// line 411 "DefaultRubyParser.y"
  {
                  yyVal = new OpAsgnNode(getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), ((Node)yyVals[0+yyTop]), (String) ((Token)yyVals[-2+yyTop]).getValue(), (String) ((Token)yyVals[-1+yyTop]).getValue());
              }
  break;
case 29:
					// line 414 "DefaultRubyParser.y"
  {
                  yyVal = new OpAsgnNode(getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), ((Node)yyVals[0+yyTop]), (String) ((Token)yyVals[-2+yyTop]).getValue(), (String) ((Token)yyVals[-1+yyTop]).getValue());
              }
  break;
case 30:
					// line 417 "DefaultRubyParser.y"
  {
                  yyVal = new OpAsgnNode(getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), ((Node)yyVals[0+yyTop]), (String) ((Token)yyVals[-2+yyTop]).getValue(), (String) ((Token)yyVals[-1+yyTop]).getValue());
              }
  break;
case 31:
					// line 420 "DefaultRubyParser.y"
  {
                  support.backrefAssignError(((Node)yyVals[-2+yyTop]));
              }
  break;
case 32:
					// line 423 "DefaultRubyParser.y"
  {
                  yyVal = support.node_assign(((Node)yyVals[-2+yyTop]), new SValueNode(getPosition(((Node)yyVals[-2+yyTop])), ((Node)yyVals[0+yyTop])));
              }
  break;
case 33:
					// line 426 "DefaultRubyParser.y"
  {
                  if (((MultipleAsgnNode)yyVals[-2+yyTop]).getHeadNode() != null) {
		      ((MultipleAsgnNode)yyVals[-2+yyTop]).setValueNode(new ToAryNode(getPosition(((MultipleAsgnNode)yyVals[-2+yyTop])), ((Node)yyVals[0+yyTop])));
		  } else {
		      ((MultipleAsgnNode)yyVals[-2+yyTop]).setValueNode(new ArrayNode(getPosition(((MultipleAsgnNode)yyVals[-2+yyTop])), ((Node)yyVals[0+yyTop])));
		  }
		  yyVal = ((MultipleAsgnNode)yyVals[-2+yyTop]);
	      }
  break;
case 34:
					// line 434 "DefaultRubyParser.y"
  {
                  ((AssignableNode)yyVals[-2+yyTop]).setValueNode(((Node)yyVals[0+yyTop]));
		  yyVal = ((MultipleAsgnNode)yyVals[-2+yyTop]);
                  ((MultipleAsgnNode)yyVals[-2+yyTop]).setPosition(support.union(((MultipleAsgnNode)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])));
	      }
  break;
case 37:
					// line 442 "DefaultRubyParser.y"
  {
                  yyVal = support.newAndNode(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
              }
  break;
case 38:
					// line 445 "DefaultRubyParser.y"
  {
                  yyVal = support.newOrNode(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
              }
  break;
case 39:
					// line 448 "DefaultRubyParser.y"
  {
                  yyVal = new NotNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])));
              }
  break;
case 40:
					// line 451 "DefaultRubyParser.y"
  {
                  yyVal = new NotNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])));
              }
  break;
case 42:
					// line 456 "DefaultRubyParser.y"
  {
                  support.checkExpression(((Node)yyVals[0+yyTop]));
	      }
  break;
case 45:
					// line 462 "DefaultRubyParser.y"
  {
                  yyVal = new ReturnNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), support.ret_args(((Node)yyVals[0+yyTop]), getPosition(((Token)yyVals[-1+yyTop]))));
              }
  break;
case 46:
					// line 465 "DefaultRubyParser.y"
  {
                  yyVal = new BreakNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), support.ret_args(((Node)yyVals[0+yyTop]), getPosition(((Token)yyVals[-1+yyTop]))));
              }
  break;
case 47:
					// line 468 "DefaultRubyParser.y"
  {
                  yyVal = new NextNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), support.ret_args(((Node)yyVals[0+yyTop]), getPosition(((Token)yyVals[-1+yyTop]))));
              }
  break;
case 49:
					// line 473 "DefaultRubyParser.y"
  {
                  yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
              }
  break;
case 50:
					// line 476 "DefaultRubyParser.y"
  {
                  yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
              }
  break;
case 51:
					// line 480 "DefaultRubyParser.y"
  {
                    support.pushBlockScope();
		}
  break;
case 52:
					// line 482 "DefaultRubyParser.y"
  {
                    yyVal = new IterNode(getPosition(((Token)yyVals[-4+yyTop])), ((Node)yyVals[-2+yyTop]), support.getCurrentScope(), ((Node)yyVals[-1+yyTop]), ((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop]));
                    support.popCurrentScope();
		}
  break;
case 53:
					// line 487 "DefaultRubyParser.y"
  {
                  yyVal = support.new_fcall(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
              }
  break;
case 54:
					// line 490 "DefaultRubyParser.y"
  {
                  yyVal = support.new_fcall(((Token)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop])); 
              }
  break;
case 55:
					// line 493 "DefaultRubyParser.y"
  {
                  yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
              }
  break;
case 56:
					// line 496 "DefaultRubyParser.y"
  {
                  yyVal = support.new_call(((Node)yyVals[-4+yyTop]), ((Token)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop])); 
	      }
  break;
case 57:
					// line 499 "DefaultRubyParser.y"
  {
                  yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
              }
  break;
case 58:
					// line 502 "DefaultRubyParser.y"
  {
                  yyVal = support.new_call(((Node)yyVals[-4+yyTop]), ((Token)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop])); 
	      }
  break;
case 59:
					// line 505 "DefaultRubyParser.y"
  {
		  yyVal = support.new_super(((Node)yyVals[0+yyTop]), ((Token)yyVals[-1+yyTop])); /* .setPosFrom($2);*/
	      }
  break;
case 60:
					// line 508 "DefaultRubyParser.y"
  {
                  yyVal = support.new_yield(getPosition(((Token)yyVals[-1+yyTop])), ((Node)yyVals[0+yyTop]));
	      }
  break;
case 62:
					// line 513 "DefaultRubyParser.y"
  {
                  yyVal = ((MultipleAsgnNode)yyVals[-1+yyTop]);
	      }
  break;
case 64:
					// line 518 "DefaultRubyParser.y"
  {
                  yyVal = new MultipleAsgnNode(getPosition(((Token)yyVals[-2+yyTop])), new ArrayNode(getPosition(((Token)yyVals[-2+yyTop])), ((MultipleAsgnNode)yyVals[-1+yyTop])), null);
              }
  break;
case 65:
					// line 522 "DefaultRubyParser.y"
  {
                  yyVal = new MultipleAsgnNode(getPosition(((ListNode)yyVals[0+yyTop])), ((ListNode)yyVals[0+yyTop]), null);
              }
  break;
case 66:
					// line 525 "DefaultRubyParser.y"
  {
/*mirko: check*/
                  yyVal = new MultipleAsgnNode(support.union(((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), ((ListNode)yyVals[-1+yyTop]).add(((Node)yyVals[0+yyTop])), null);
                  ((Node)yyVals[-1+yyTop]).setPosition(support.union(((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])));
              }
  break;
case 67:
					// line 530 "DefaultRubyParser.y"
  {
                  yyVal = new MultipleAsgnNode(getPosition(((ListNode)yyVals[-2+yyTop])), ((ListNode)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
              }
  break;
case 68:
					// line 533 "DefaultRubyParser.y"
  {
                  yyVal = new MultipleAsgnNode(getPosition(((ListNode)yyVals[-1+yyTop])), ((ListNode)yyVals[-1+yyTop]), new StarNode(getPosition(null)));
              }
  break;
case 69:
					// line 536 "DefaultRubyParser.y"
  {
                  yyVal = new MultipleAsgnNode(getPosition(((Token)yyVals[-1+yyTop])), null, ((Node)yyVals[0+yyTop]));
              }
  break;
case 70:
					// line 539 "DefaultRubyParser.y"
  {
                  yyVal = new MultipleAsgnNode(getPosition(((Token)yyVals[0+yyTop])), null, new StarNode(getPosition(null)));
              }
  break;
case 72:
					// line 544 "DefaultRubyParser.y"
  {
                  yyVal = ((MultipleAsgnNode)yyVals[-1+yyTop]);
              }
  break;
case 73:
					// line 548 "DefaultRubyParser.y"
  {
                  yyVal = new ArrayNode(((Node)yyVals[-1+yyTop]).getPosition(), ((Node)yyVals[-1+yyTop]));
              }
  break;
case 74:
					// line 551 "DefaultRubyParser.y"
  {
                  yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[-1+yyTop]));
              }
  break;
case 75:
					// line 555 "DefaultRubyParser.y"
  {
                  yyVal = support.assignable(((Token)yyVals[0+yyTop]), null);
              }
  break;
case 76:
					// line 558 "DefaultRubyParser.y"
  {
                  yyVal = support.aryset(((Node)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]));
              }
  break;
case 77:
					// line 561 "DefaultRubyParser.y"
  {
                  yyVal = support.attrset(((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
              }
  break;
case 78:
					// line 564 "DefaultRubyParser.y"
  {
                  yyVal = support.attrset(((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
              }
  break;
case 79:
					// line 567 "DefaultRubyParser.y"
  {
                  yyVal = support.attrset(((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
              }
  break;
case 80:
					// line 570 "DefaultRubyParser.y"
  {
                  if (support.isInDef() || support.isInSingle()) {
		      yyerror("dynamic constant assignment");
		  }

		  ISourcePosition position = support.union(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]));

                  yyVal = new ConstDeclNode(position, null, new Colon2Node(position, ((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue()), null);
	      }
  break;
case 81:
					// line 579 "DefaultRubyParser.y"
  {
                  if (support.isInDef() || support.isInSingle()) {
		      yyerror("dynamic constant assignment");
		  }

                  ISourcePosition position = support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop]));

                  yyVal = new ConstDeclNode(position, null, new Colon3Node(position, (String) ((Token)yyVals[0+yyTop]).getValue()), null);
	      }
  break;
case 82:
					// line 588 "DefaultRubyParser.y"
  {
	          support.backrefAssignError(((Node)yyVals[0+yyTop]));
              }
  break;
case 83:
					// line 592 "DefaultRubyParser.y"
  {
                  yyVal = support.assignable(((Token)yyVals[0+yyTop]), null);
              }
  break;
case 84:
					// line 595 "DefaultRubyParser.y"
  {
                  yyVal = support.aryset(((Node)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]));
              }
  break;
case 85:
					// line 598 "DefaultRubyParser.y"
  {
                  yyVal = support.attrset(((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
              }
  break;
case 86:
					// line 601 "DefaultRubyParser.y"
  {
                  yyVal = support.attrset(((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
 	      }
  break;
case 87:
					// line 604 "DefaultRubyParser.y"
  {
                  yyVal = support.attrset(((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
              }
  break;
case 88:
					// line 607 "DefaultRubyParser.y"
  {
                  if (support.isInDef() || support.isInSingle()) {
		      yyerror("dynamic constant assignment");
		  }
			
		  ISourcePosition position = support.union(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]));

                  yyVal = new ConstDeclNode(position, null, new Colon2Node(position, ((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue()), null);
              }
  break;
case 89:
					// line 616 "DefaultRubyParser.y"
  {
                  if (support.isInDef() || support.isInSingle()) {
		      yyerror("dynamic constant assignment");
		  }

                  ISourcePosition position = support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop]));

                  yyVal = new ConstDeclNode(position, null, new Colon3Node(position, (String) ((Token)yyVals[0+yyTop]).getValue()), null);
	      }
  break;
case 90:
					// line 625 "DefaultRubyParser.y"
  {
                   support.backrefAssignError(((Node)yyVals[0+yyTop]));
	      }
  break;
case 91:
					// line 629 "DefaultRubyParser.y"
  {
                  yyerror("class/module name must be CONSTANT");
              }
  break;
case 93:
					// line 634 "DefaultRubyParser.y"
  {
                  yyVal = new Colon3Node(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), (String) ((Token)yyVals[0+yyTop]).getValue());
	      }
  break;
case 94:
					// line 637 "DefaultRubyParser.y"
  {
                  yyVal = new Colon2Node(((Token)yyVals[0+yyTop]).getPosition(), null, (String) ((Token)yyVals[0+yyTop]).getValue());
 	      }
  break;
case 95:
					// line 640 "DefaultRubyParser.y"
  {
                  yyVal = new Colon2Node(support.union(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
	      }
  break;
case 99:
					// line 646 "DefaultRubyParser.y"
  {
                  lexer.setState(LexState.EXPR_END);
                  yyVal = ((Token)yyVals[0+yyTop]);
              }
  break;
case 100:
					// line 651 "DefaultRubyParser.y"
  {
                  lexer.setState(LexState.EXPR_END);
                  yyVal = yyVals[0+yyTop];
              }
  break;
case 103:
					// line 658 "DefaultRubyParser.y"
  {
                  yyVal = new UndefNode(getPosition(((Token)yyVals[0+yyTop])), (String) ((Token)yyVals[0+yyTop]).getValue());
              }
  break;
case 104:
					// line 661 "DefaultRubyParser.y"
  {
                  lexer.setState(LexState.EXPR_FNAME);
	      }
  break;
case 105:
					// line 663 "DefaultRubyParser.y"
  {
                  yyVal = support.appendToBlock(((Node)yyVals[-3+yyTop]), new UndefNode(getPosition(((Node)yyVals[-3+yyTop])), (String) ((Token)yyVals[0+yyTop]).getValue()));
              }
  break;
case 173:
					// line 682 "DefaultRubyParser.y"
  {
                  yyVal = support.node_assign(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
		  /* FIXME: Consider fixing node_assign itself rather than single case*/
		  ((Node)yyVal).setPosition(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])));
              }
  break;
case 174:
					// line 687 "DefaultRubyParser.y"
  {
                  ISourcePosition position = support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
                  yyVal = support.node_assign(((Node)yyVals[-4+yyTop]), new RescueNode.Inline(position, ((Node)yyVals[-2+yyTop]), new RescueBodyNode(position, null, ((Node)yyVals[0+yyTop]), null, null), null));
	      }
  break;
case 175:
					// line 691 "DefaultRubyParser.y"
  {
		  support.checkExpression(((Node)yyVals[0+yyTop]));
	          String name = ((INameNode)yyVals[-2+yyTop]).getName();
		  String asgnOp = (String) ((Token)yyVals[-1+yyTop]).getValue();

		  if (asgnOp.equals("||")) {
	              ((AssignableNode)yyVals[-2+yyTop]).setValueNode(((Node)yyVals[0+yyTop]));
	              yyVal = new OpAsgnOrNode(support.union(((AssignableNode)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), support.gettable2(name, ((AssignableNode)yyVals[-2+yyTop]).getPosition()), ((AssignableNode)yyVals[-2+yyTop]));
		  } else if (asgnOp.equals("&&")) {
	              ((AssignableNode)yyVals[-2+yyTop]).setValueNode(((Node)yyVals[0+yyTop]));
                      yyVal = new OpAsgnAndNode(support.union(((AssignableNode)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), support.gettable2(name, ((AssignableNode)yyVals[-2+yyTop]).getPosition()), ((AssignableNode)yyVals[-2+yyTop]));
		  } else {
		      ((AssignableNode)yyVals[-2+yyTop]).setValueNode(support.getOperatorCallNode(support.gettable2(name, ((AssignableNode)yyVals[-2+yyTop]).getPosition()), asgnOp, ((Node)yyVals[0+yyTop])));
                      ((AssignableNode)yyVals[-2+yyTop]).setPosition(support.union(((AssignableNode)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])));
		      yyVal = ((AssignableNode)yyVals[-2+yyTop]);
		  }
              }
  break;
case 176:
					// line 708 "DefaultRubyParser.y"
  {
                  yyVal = new OpElementAsgnNode(getPosition(((Node)yyVals[-5+yyTop])), ((Node)yyVals[-5+yyTop]), (String) ((Token)yyVals[-1+yyTop]).getValue(), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop]));
              }
  break;
case 177:
					// line 711 "DefaultRubyParser.y"
  {
                  yyVal = new OpAsgnNode(getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), ((Node)yyVals[0+yyTop]), (String) ((Token)yyVals[-2+yyTop]).getValue(), (String) ((Token)yyVals[-1+yyTop]).getValue());
              }
  break;
case 178:
					// line 714 "DefaultRubyParser.y"
  {
                  yyVal = new OpAsgnNode(getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), ((Node)yyVals[0+yyTop]), (String) ((Token)yyVals[-2+yyTop]).getValue(), (String) ((Token)yyVals[-1+yyTop]).getValue());
              }
  break;
case 179:
					// line 717 "DefaultRubyParser.y"
  {
                  yyVal = new OpAsgnNode(getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), ((Node)yyVals[0+yyTop]), (String) ((Token)yyVals[-2+yyTop]).getValue(), (String) ((Token)yyVals[-1+yyTop]).getValue());
              }
  break;
case 180:
					// line 720 "DefaultRubyParser.y"
  {
	          yyerror("constant re-assignment");
	      }
  break;
case 181:
					// line 723 "DefaultRubyParser.y"
  {
		  yyerror("constant re-assignment");
	      }
  break;
case 182:
					// line 726 "DefaultRubyParser.y"
  {
                  support.backrefAssignError(((Node)yyVals[-2+yyTop]));
              }
  break;
case 183:
					// line 729 "DefaultRubyParser.y"
  {
		  support.checkExpression(((Node)yyVals[-2+yyTop]));
		  support.checkExpression(((Node)yyVals[0+yyTop]));
                  yyVal = new DotNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]), false);
              }
  break;
case 184:
					// line 734 "DefaultRubyParser.y"
  {
		  support.checkExpression(((Node)yyVals[-2+yyTop]));
		  support.checkExpression(((Node)yyVals[0+yyTop]));
                  yyVal = new DotNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]), true);
              }
  break;
case 185:
					// line 739 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "+", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 186:
					// line 742 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "-", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 187:
					// line 745 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "*", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 188:
					// line 748 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "/", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 189:
					// line 751 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "%", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 190:
					// line 754 "DefaultRubyParser.y"
  {
		  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "**", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 191:
					// line 757 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "**", ((Node)yyVals[0+yyTop]), getPosition(null)), "-@");
              }
  break;
case 192:
					// line 760 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(support.getOperatorCallNode(((FloatNode)yyVals[-2+yyTop]), "**", ((Node)yyVals[0+yyTop]), getPosition(null)), "-@");
              }
  break;
case 193:
					// line 763 "DefaultRubyParser.y"
  {
 	          if (((Node)yyVals[0+yyTop]) != null && ((Node)yyVals[0+yyTop]) instanceof ILiteralNode) {
		      yyVal = ((Node)yyVals[0+yyTop]);
		  } else {
                      yyVal = support.getOperatorCallNode(((Node)yyVals[0+yyTop]), "+@");
		  }
              }
  break;
case 194:
					// line 770 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[0+yyTop]), "-@");
	      }
  break;
case 195:
					// line 773 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "|", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 196:
					// line 776 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "^", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 197:
					// line 779 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "&", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 198:
					// line 782 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "<=>", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 199:
					// line 785 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), ">", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 200:
					// line 788 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), ">=", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 201:
					// line 791 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "<", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 202:
					// line 794 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "<=", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 203:
					// line 797 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "==", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 204:
					// line 800 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "===", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 205:
					// line 803 "DefaultRubyParser.y"
  {
                  yyVal = new NotNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "==", ((Node)yyVals[0+yyTop]), getPosition(null)));
              }
  break;
case 206:
					// line 806 "DefaultRubyParser.y"
  {
                  yyVal = support.getMatchNode(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
              }
  break;
case 207:
					// line 809 "DefaultRubyParser.y"
  {
                  yyVal = new NotNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), support.getMatchNode(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])));
              }
  break;
case 208:
					// line 812 "DefaultRubyParser.y"
  {
                  yyVal = new NotNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])));
              }
  break;
case 209:
					// line 815 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[0+yyTop]), "~");
              }
  break;
case 210:
					// line 818 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "<<", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 211:
					// line 821 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), ">>", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 212:
					// line 824 "DefaultRubyParser.y"
  {
                  yyVal = support.newAndNode(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
              }
  break;
case 213:
					// line 827 "DefaultRubyParser.y"
  {
                  yyVal = support.newOrNode(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
              }
  break;
case 214:
					// line 830 "DefaultRubyParser.y"
  {
                  yyVal = new DefinedNode(getPosition(((Token)yyVals[-2+yyTop])), ((Node)yyVals[0+yyTop]));
              }
  break;
case 215:
					// line 833 "DefaultRubyParser.y"
  {
                  yyVal = new IfNode.Inline(getPosition(((Node)yyVals[-4+yyTop])), support.getConditionNode(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
              }
  break;
case 216:
					// line 836 "DefaultRubyParser.y"
  {
                  yyVal = ((Node)yyVals[0+yyTop]);
              }
  break;
case 217:
					// line 840 "DefaultRubyParser.y"
  {
	          support.checkExpression(((Node)yyVals[0+yyTop]));
	          yyVal = ((Node)yyVals[0+yyTop]);   
	      }
  break;
case 219:
					// line 846 "DefaultRubyParser.y"
  {
                  warnings.warn(getPosition(((Node)yyVals[-1+yyTop])), "parenthesize argument(s) for future version");
                  yyVal = new ArrayNode(getPosition(((Node)yyVals[-1+yyTop])), ((Node)yyVals[-1+yyTop]));
              }
  break;
case 220:
					// line 850 "DefaultRubyParser.y"
  {
                  yyVal = ((ListNode)yyVals[-1+yyTop]);
              }
  break;
case 221:
					// line 853 "DefaultRubyParser.y"
  {
                  support.checkExpression(((Node)yyVals[-1+yyTop]));
                  yyVal = support.arg_concat(getPosition(((ListNode)yyVals[-4+yyTop])), ((ListNode)yyVals[-4+yyTop]), ((Node)yyVals[-1+yyTop]));
              }
  break;
case 222:
					// line 857 "DefaultRubyParser.y"
  {
                  yyVal = new ArrayNode(getPosition(((ListNode)yyVals[-1+yyTop])), new HashNode(getPosition(null), ((ListNode)yyVals[-1+yyTop])));
              }
  break;
case 223:
					// line 860 "DefaultRubyParser.y"
  {
                  support.checkExpression(((Node)yyVals[-1+yyTop]));
		  yyVal = new NewlineNode(getPosition(((Token)yyVals[-2+yyTop])), new SplatNode(getPosition(((Token)yyVals[-2+yyTop])), ((Node)yyVals[-1+yyTop])));
              }
  break;
case 224:
					// line 865 "DefaultRubyParser.y"
  {
                  yyVal = new ArrayNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
              }
  break;
case 225:
					// line 868 "DefaultRubyParser.y"
  {
                  yyVal = ((Node)yyVals[-2+yyTop]);
                  if (!(yyVal instanceof BlockPassNode)) {
                      ((Node)yyVal).setPosition(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])));
                  }
              }
  break;
case 226:
					// line 874 "DefaultRubyParser.y"
  {
                  warnings.warn(getPosition(((Token)yyVals[-3+yyTop])), "parenthesize argument(s) for future version");
                  yyVal = new ArrayNode(getPosition(((Token)yyVals[-3+yyTop])), ((Node)yyVals[-2+yyTop]));
              }
  break;
case 227:
					// line 878 "DefaultRubyParser.y"
  {
                  warnings.warn(getPosition(((Token)yyVals[-5+yyTop])), "parenthesize argument(s) for future version");
                  yyVal = ((ListNode)yyVals[-4+yyTop]).add(((Node)yyVals[-2+yyTop]));
              }
  break;
case 230:
					// line 886 "DefaultRubyParser.y"
  {
                  warnings.warn(((Node)yyVals[0+yyTop]).getPosition(), "parenthesize argument(s) for future version");
                  yyVal = new ArrayNode(getPosition(((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));
              }
  break;
case 231:
					// line 890 "DefaultRubyParser.y"
  {
                  yyVal = support.arg_blk_pass(((ListNode)yyVals[-1+yyTop]), ((BlockPassNode)yyVals[0+yyTop]));
              }
  break;
case 232:
					// line 893 "DefaultRubyParser.y"
  {
                  yyVal = support.arg_concat(getPosition(((ListNode)yyVals[-4+yyTop])), ((ListNode)yyVals[-4+yyTop]), ((Node)yyVals[-1+yyTop]));
                  yyVal = support.arg_blk_pass(((Node)yyVal), ((BlockPassNode)yyVals[0+yyTop]));
              }
  break;
case 233:
					// line 897 "DefaultRubyParser.y"
  {
                  yyVal = new ArrayNode(getPosition(((ListNode)yyVals[-1+yyTop])), new HashNode(getPosition(null), ((ListNode)yyVals[-1+yyTop])));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
              }
  break;
case 234:
					// line 901 "DefaultRubyParser.y"
  {
                  yyVal = support.arg_concat(getPosition(((ListNode)yyVals[-4+yyTop])), new ArrayNode(getPosition(((ListNode)yyVals[-4+yyTop])), new HashNode(getPosition(null), ((ListNode)yyVals[-4+yyTop]))), ((Node)yyVals[-1+yyTop]));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
              }
  break;
case 235:
					// line 905 "DefaultRubyParser.y"
  {
                  yyVal = ((ListNode)yyVals[-3+yyTop]).add(new HashNode(getPosition(null), ((ListNode)yyVals[-1+yyTop])));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
              }
  break;
case 236:
					// line 909 "DefaultRubyParser.y"
  {
                  support.checkExpression(((Node)yyVals[-1+yyTop]));
		  yyVal = support.arg_concat(getPosition(((ListNode)yyVals[-6+yyTop])), ((ListNode)yyVals[-6+yyTop]).add(new HashNode(getPosition(null), ((ListNode)yyVals[-4+yyTop]))), ((Node)yyVals[-1+yyTop]));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
              }
  break;
case 237:
					// line 914 "DefaultRubyParser.y"
  {
                  yyVal = support.arg_blk_pass(new SplatNode(getPosition(((Token)yyVals[-2+yyTop])), ((Node)yyVals[-1+yyTop])), ((BlockPassNode)yyVals[0+yyTop]));
              }
  break;
case 238:
					// line 917 "DefaultRubyParser.y"
  {}
  break;
case 239:
					// line 919 "DefaultRubyParser.y"
  {
                  yyVal = support.arg_blk_pass(new ArrayNode(getPosition(((Node)yyVals[-3+yyTop])), ((Node)yyVals[-3+yyTop])).addAll(((ListNode)yyVals[-1+yyTop])), ((BlockPassNode)yyVals[0+yyTop]));
	      }
  break;
case 240:
					// line 922 "DefaultRubyParser.y"
  {
                  yyVal = support.arg_blk_pass(new ArrayNode(getPosition(((Node)yyVals[-2+yyTop])), ((Node)yyVals[-2+yyTop])), ((BlockPassNode)yyVals[0+yyTop]));
              }
  break;
case 241:
					// line 925 "DefaultRubyParser.y"
  {
                  yyVal = support.arg_concat(getPosition(((Node)yyVals[-4+yyTop])), new ArrayNode(getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop])), ((Node)yyVals[-1+yyTop]));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
	      }
  break;
case 242:
					// line 929 "DefaultRubyParser.y"
  {
                  yyVal = support.arg_concat(getPosition(((Node)yyVals[-6+yyTop])), new ArrayNode(getPosition(((Node)yyVals[-6+yyTop])), ((Node)yyVals[-6+yyTop])).addAll(new HashNode(getPosition(null), ((ListNode)yyVals[-4+yyTop]))), ((Node)yyVals[-1+yyTop]));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
	      }
  break;
case 243:
					// line 933 "DefaultRubyParser.y"
  {
                  yyVal = new ArrayNode(getPosition(((ListNode)yyVals[-1+yyTop])), new HashNode(getPosition(null), ((ListNode)yyVals[-1+yyTop])));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
	      }
  break;
case 244:
					// line 937 "DefaultRubyParser.y"
  {
                  yyVal = support.arg_concat(getPosition(((ListNode)yyVals[-4+yyTop])), new ArrayNode(getPosition(((ListNode)yyVals[-4+yyTop])), new HashNode(getPosition(null), ((ListNode)yyVals[-4+yyTop]))), ((Node)yyVals[-1+yyTop]));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
	      }
  break;
case 245:
					// line 941 "DefaultRubyParser.y"
  {
                  yyVal = new ArrayNode(getPosition(((Node)yyVals[-3+yyTop])), ((Node)yyVals[-3+yyTop])).add(new HashNode(getPosition(null), ((ListNode)yyVals[-1+yyTop])));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
	      }
  break;
case 246:
					// line 945 "DefaultRubyParser.y"
  {
                  yyVal = new ArrayNode(getPosition(((Node)yyVals[-5+yyTop])), ((Node)yyVals[-5+yyTop])).addAll(((ListNode)yyVals[-3+yyTop])).add(new HashNode(getPosition(null), ((ListNode)yyVals[-1+yyTop])));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
	      }
  break;
case 247:
					// line 949 "DefaultRubyParser.y"
  {
                  yyVal = support.arg_concat(getPosition(((Node)yyVals[-6+yyTop])), new ArrayNode(getPosition(((Node)yyVals[-6+yyTop])), ((Node)yyVals[-6+yyTop])).add(new HashNode(getPosition(null), ((ListNode)yyVals[-4+yyTop]))), ((Node)yyVals[-1+yyTop]));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
	      }
  break;
case 248:
					// line 953 "DefaultRubyParser.y"
  {
                  yyVal = support.arg_concat(getPosition(((Node)yyVals[-8+yyTop])), new ArrayNode(getPosition(((Node)yyVals[-8+yyTop])), ((Node)yyVals[-8+yyTop])).addAll(((ListNode)yyVals[-6+yyTop])).add(new HashNode(getPosition(null), ((ListNode)yyVals[-4+yyTop]))), ((Node)yyVals[-1+yyTop]));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
	      }
  break;
case 249:
					// line 957 "DefaultRubyParser.y"
  {
                  yyVal = support.arg_blk_pass(new SplatNode(getPosition(((Token)yyVals[-2+yyTop])), ((Node)yyVals[-1+yyTop])), ((BlockPassNode)yyVals[0+yyTop]));
	      }
  break;
case 250:
					// line 960 "DefaultRubyParser.y"
  {}
  break;
case 251:
					// line 962 "DefaultRubyParser.y"
  { 
	          yyVal = new Long(lexer.getCmdArgumentState().begin());
	      }
  break;
case 252:
					// line 964 "DefaultRubyParser.y"
  {
                  lexer.getCmdArgumentState().reset(((Long)yyVals[-1+yyTop]).longValue());
                  yyVal = ((Node)yyVals[0+yyTop]);
              }
  break;
case 254:
					// line 970 "DefaultRubyParser.y"
  {                    
		  lexer.setState(LexState.EXPR_ENDARG);
	      }
  break;
case 255:
					// line 972 "DefaultRubyParser.y"
  {
                  warnings.warn(getPosition(((Token)yyVals[-2+yyTop])), "don't put space before argument parentheses");
	          yyVal = null;
	      }
  break;
case 256:
					// line 976 "DefaultRubyParser.y"
  {
		  lexer.setState(LexState.EXPR_ENDARG);
	      }
  break;
case 257:
					// line 978 "DefaultRubyParser.y"
  {
                  warnings.warn(getPosition(((Token)yyVals[-3+yyTop])), "don't put space before argument parentheses");
		  yyVal = ((Node)yyVals[-2+yyTop]);
	      }
  break;
case 258:
					// line 983 "DefaultRubyParser.y"
  {
                  support.checkExpression(((Node)yyVals[0+yyTop]));
                  yyVal = new BlockPassNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));
              }
  break;
case 259:
					// line 988 "DefaultRubyParser.y"
  {
                  yyVal = ((BlockPassNode)yyVals[0+yyTop]);
              }
  break;
case 261:
					// line 993 "DefaultRubyParser.y"
  {
                  yyVal = new ArrayNode(getPosition2(((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));
              }
  break;
case 262:
					// line 996 "DefaultRubyParser.y"
  {
                  yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[0+yyTop]));
              }
  break;
case 263:
					// line 1000 "DefaultRubyParser.y"
  {
		  yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[0+yyTop]));
              }
  break;
case 264:
					// line 1003 "DefaultRubyParser.y"
  {
                  yyVal = support.arg_concat(getPosition(((ListNode)yyVals[-3+yyTop])), ((ListNode)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop]));
	      }
  break;
case 265:
					// line 1006 "DefaultRubyParser.y"
  {  
                  yyVal = new SplatNode(getPosition(((Token)yyVals[-1+yyTop])), ((Node)yyVals[0+yyTop]));
	      }
  break;
case 274:
					// line 1018 "DefaultRubyParser.y"
  {
                  yyVal = new FCallNode(((Token)yyVals[0+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue(), null);
	      }
  break;
case 275:
					// line 1021 "DefaultRubyParser.y"
  {
                  yyVal = new BeginNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-1+yyTop]), ((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]));
	      }
  break;
case 276:
					// line 1024 "DefaultRubyParser.y"
  { 
                  lexer.setState(LexState.EXPR_ENDARG); 
              }
  break;
case 277:
					// line 1026 "DefaultRubyParser.y"
  {
		  warnings.warning(getPosition(((Token)yyVals[-4+yyTop])), "(...) interpreted as grouped expression");
                  yyVal = ((Node)yyVals[-3+yyTop]);
	      }
  break;
case 278:
					// line 1030 "DefaultRubyParser.y"
  {
		  yyVal = ((Node)yyVals[-1+yyTop]);
              }
  break;
case 279:
					// line 1033 "DefaultRubyParser.y"
  {
                  yyVal = new Colon2Node(support.union(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
              }
  break;
case 280:
					// line 1036 "DefaultRubyParser.y"
  {
                  yyVal = new Colon3Node(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), (String) ((Token)yyVals[0+yyTop]).getValue());
              }
  break;
case 281:
					// line 1039 "DefaultRubyParser.y"
  {
                  if (((Node)yyVals[-3+yyTop]) instanceof SelfNode) {
                      yyVal = new FCallNode(getPosition(((Node)yyVals[-3+yyTop])), "[]", ((Node)yyVals[-1+yyTop]));
                  } else {
                      yyVal = new CallNode(getPosition(((Node)yyVals[-3+yyTop])), ((Node)yyVals[-3+yyTop]), "[]", ((Node)yyVals[-1+yyTop]));
                  }
              }
  break;
case 282:
					// line 1046 "DefaultRubyParser.y"
  {
                  ISourcePosition position = support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]));
                  if (((Node)yyVals[-1+yyTop]) == null) {
                      yyVal = new ZArrayNode(position); /* zero length array */
                  } else {
                      yyVal = ((Node)yyVals[-1+yyTop]);
                      if (yyVal instanceof ArrayNode) {
                          ((ArrayNode)yyVal).setLeftBracketPosition(((Token)yyVals[-2+yyTop]));
                          ((ArrayNode)yyVal).setRightBracketPosition(((Token)yyVals[0+yyTop]));
                      }
                      ((ISourcePositionHolder)yyVal).setPosition(position);
                  }
              }
  break;
case 283:
					// line 1059 "DefaultRubyParser.y"
  {
                  yyVal = new HashNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), ((ListNode)yyVals[-1+yyTop]), ((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]));
              }
  break;
case 284:
					// line 1062 "DefaultRubyParser.y"
  {
		  yyVal = new ReturnNode(((Token)yyVals[0+yyTop]).getPosition(), null);
              }
  break;
case 285:
					// line 1065 "DefaultRubyParser.y"
  {
                  yyVal = support.new_yield(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-1+yyTop]));
              }
  break;
case 286:
					// line 1068 "DefaultRubyParser.y"
  {
                  yyVal = new YieldNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), null, false);
              }
  break;
case 287:
					// line 1071 "DefaultRubyParser.y"
  {
                  yyVal = new YieldNode(((Token)yyVals[0+yyTop]).getPosition(), null, false);
              }
  break;
case 288:
					// line 1074 "DefaultRubyParser.y"
  {
                  yyVal = new DefinedNode(getPosition(((Token)yyVals[-4+yyTop])), ((Node)yyVals[-1+yyTop]));
              }
  break;
case 289:
					// line 1077 "DefaultRubyParser.y"
  {
                  yyVal = new FCallNode(support.union(((Token)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop])), (String) ((Token)yyVals[-1+yyTop]).getValue(), null, ((IterNode)yyVals[0+yyTop]));
              }
  break;
case 291:
					// line 1081 "DefaultRubyParser.y"
  {
	          if (((Node)yyVals[-1+yyTop]) != null && 
                      ((BlockAcceptingNode)yyVals[-1+yyTop]).getIterNode() instanceof BlockPassNode) {
                      throw new SyntaxException(getPosition(((Node)yyVals[-1+yyTop])), "Both block arg and actual block given.");
		  }
		  ((BlockAcceptingNode)yyVals[-1+yyTop]).setIterNode(((IterNode)yyVals[0+yyTop]));
		  ((Node)yyVals[-1+yyTop]).setPosition(support.union(((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop])));
              }
  break;
case 292:
					// line 1089 "DefaultRubyParser.y"
  {
                  yyVal = new IfNode(support.union(((Token)yyVals[-5+yyTop]), ((Token)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), ((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop]));
              }
  break;
case 293:
					// line 1092 "DefaultRubyParser.y"
  {
                  yyVal = new IfNode.Unless(support.union(((Token)yyVals[-5+yyTop]), ((Token)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[-2+yyTop]), ((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop]));
              }
  break;
case 294:
					// line 1095 "DefaultRubyParser.y"
  { 
                  lexer.getConditionState().begin();
	      }
  break;
case 295:
					// line 1097 "DefaultRubyParser.y"
  {
		  lexer.getConditionState().end();
	      }
  break;
case 296:
					// line 1099 "DefaultRubyParser.y"
  {
                  yyVal = new WhileNode(support.union(((Token)yyVals[-6+yyTop]), ((Token)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-1+yyTop]), ((Token)yyVals[-6+yyTop]), ((Token)yyVals[0+yyTop]));
              }
  break;
case 297:
					// line 1102 "DefaultRubyParser.y"
  {
                  lexer.getConditionState().begin();
              }
  break;
case 298:
					// line 1104 "DefaultRubyParser.y"
  {
                  lexer.getConditionState().end();
              }
  break;
case 299:
					// line 1106 "DefaultRubyParser.y"
  {
                  yyVal = new UntilNode(getPosition(((Token)yyVals[-6+yyTop])), support.getConditionNode(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-1+yyTop]), ((Token)yyVals[-6+yyTop]), ((Token)yyVals[0+yyTop]));
              }
  break;
case 300:
					// line 1109 "DefaultRubyParser.y"
  {
                  yyVal = new CaseNode(support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]), ((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop]));
              }
  break;
case 301:
					// line 1112 "DefaultRubyParser.y"
  {
                  yyVal = new CaseNode(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])), null, ((Node)yyVals[-1+yyTop]), ((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop]));
              }
  break;
case 302:
					// line 1115 "DefaultRubyParser.y"
  {
                  yyVal = new CaseNode(support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop])), null, new ElseNode(support.union(((Token)yyVals[-2+yyTop]),((Node)yyVals[-1+yyTop])), ((Token)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop])), ((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop]));
              }
  break;
case 303:
					// line 1118 "DefaultRubyParser.y"
  {
                  lexer.getConditionState().begin();
              }
  break;
case 304:
					// line 1120 "DefaultRubyParser.y"
  {
                  lexer.getConditionState().end();
              }
  break;
case 305:
					// line 1122 "DefaultRubyParser.y"
  {
                  yyVal = new ForNode(support.union(((Token)yyVals[-8+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-7+yyTop]), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[-4+yyTop]), ((Token)yyVals[-8+yyTop]), ((Token)yyVals[0+yyTop]));
              }
  break;
case 306:
					// line 1125 "DefaultRubyParser.y"
  {
                  if (support.isInDef() || support.isInSingle()) {
                      yyerror("class definition in method body");
                  }
		  support.pushLocalScope();
              }
  break;
case 307:
					// line 1130 "DefaultRubyParser.y"
  {
                  yyVal = new ClassNode(support.union(((Token)yyVals[-5+yyTop]), ((Token)yyVals[0+yyTop])), ((Colon3Node)yyVals[-4+yyTop]), support.getCurrentScope(), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[-3+yyTop]), ((Token)yyVals[-5+yyTop]), ((Token)yyVals[0+yyTop]));
                  support.popCurrentScope();
              }
  break;
case 308:
					// line 1134 "DefaultRubyParser.y"
  {
                  yyVal = new Boolean(support.isInDef());
                  support.setInDef(false);
              }
  break;
case 309:
					// line 1137 "DefaultRubyParser.y"
  {
                  yyVal = new Integer(support.getInSingle());
                  support.setInSingle(0);
		  support.pushLocalScope();
              }
  break;
case 310:
					// line 1141 "DefaultRubyParser.y"
  {
                  yyVal = new SClassNode(support.union(((Token)yyVals[-7+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-5+yyTop]), support.getCurrentScope(), ((Node)yyVals[-1+yyTop]), ((Token)yyVals[-7+yyTop]), ((Token)yyVals[0+yyTop]));
                  support.popCurrentScope();
                  support.setInDef(((Boolean)yyVals[-4+yyTop]).booleanValue());
                  support.setInSingle(((Integer)yyVals[-2+yyTop]).intValue());
              }
  break;
case 311:
					// line 1147 "DefaultRubyParser.y"
  {
                  if (support.isInDef() || support.isInSingle()) { 
                      yyerror("module definition in method body");
                  }
		  support.pushLocalScope();
              }
  break;
case 312:
					// line 1152 "DefaultRubyParser.y"
  {
                  yyVal = new ModuleNode(support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop])), ((Colon3Node)yyVals[-3+yyTop]), support.getCurrentScope(), ((Node)yyVals[-1+yyTop]), ((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop]));
                  support.popCurrentScope();
              }
  break;
case 313:
					// line 1156 "DefaultRubyParser.y"
  {
                  support.setInDef(true);
		  support.pushLocalScope();
              }
  break;
case 314:
					// line 1159 "DefaultRubyParser.y"
  {
                    /* NOEX_PRIVATE for toplevel */
                  yyVal = new DefnNode(support.union(((Token)yyVals[-5+yyTop]), ((Token)yyVals[0+yyTop])), new ArgumentNode(((Token)yyVals[-4+yyTop]).getPosition(), (String) ((Token)yyVals[-4+yyTop]).getValue()), ((ArgsNode)yyVals[-2+yyTop]), support.getCurrentScope(), ((Node)yyVals[-1+yyTop]), Visibility.PRIVATE, ((Token)yyVals[0+yyTop]));
                  support.popCurrentScope();
                  support.setInDef(false);
              }
  break;
case 315:
					// line 1165 "DefaultRubyParser.y"
  {
                  lexer.setState(LexState.EXPR_FNAME);
              }
  break;
case 316:
					// line 1167 "DefaultRubyParser.y"
  {
                  support.setInSingle(support.getInSingle() + 1);
		  support.pushLocalScope();
                  lexer.setState(LexState.EXPR_END); /* force for args */
              }
  break;
case 317:
					// line 1171 "DefaultRubyParser.y"
  {
                  yyVal = new DefsNode(support.union(((Token)yyVals[-8+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-7+yyTop]), new ArgumentNode(((Token)yyVals[-4+yyTop]).getPosition(), (String) ((Token)yyVals[-4+yyTop]).getValue()), ((ArgsNode)yyVals[-2+yyTop]), support.getCurrentScope(), ((Node)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop]));
                  support.popCurrentScope();
                  support.setInSingle(support.getInSingle() - 1);
              }
  break;
case 318:
					// line 1176 "DefaultRubyParser.y"
  {
                  yyVal = new BreakNode(((Token)yyVals[0+yyTop]).getPosition());
              }
  break;
case 319:
					// line 1179 "DefaultRubyParser.y"
  {
                  yyVal = new NextNode(((Token)yyVals[0+yyTop]).getPosition());
              }
  break;
case 320:
					// line 1182 "DefaultRubyParser.y"
  {
                  yyVal = new RedoNode(((Token)yyVals[0+yyTop]).getPosition());
              }
  break;
case 321:
					// line 1185 "DefaultRubyParser.y"
  {
                  yyVal = new RetryNode(((Token)yyVals[0+yyTop]).getPosition());
              }
  break;
case 322:
					// line 1189 "DefaultRubyParser.y"
  {
                  support.checkExpression(((Node)yyVals[0+yyTop]));
		  yyVal = ((Node)yyVals[0+yyTop]);
	      }
  break;
case 324:
					// line 1195 "DefaultRubyParser.y"
  {
                yyVal = new Token(":", lexer.getPosition());
              }
  break;
case 325:
					// line 1198 "DefaultRubyParser.y"
  {
                yyVal = ((Token)yyVals[0+yyTop]);
              }
  break;
case 326:
					// line 1201 "DefaultRubyParser.y"
  {
                yyVal = ((Token)yyVals[-1+yyTop]);
              }
  break;
case 331:
					// line 1210 "DefaultRubyParser.y"
  {
/*mirko: support.union($<ISourcePositionHolder>1.getPosition(), getPosition($<ISourcePositionHolder>1)) ?*/
                  yyVal = new IfNode.ElseIf(((Token)yyVals[-4+yyTop]).getPosition(), support.getConditionNode(((Node)yyVals[-3+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), ((Token)yyVals[-2+yyTop]));
              }
  break;
case 333:
					// line 1216 "DefaultRubyParser.y"
  {
                  yyVal = new ElseNode(support.union(((Token)yyVals[-1+yyTop]),((Node)yyVals[0+yyTop])), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
              }
  break;
case 335:
					// line 1221 "DefaultRubyParser.y"
  {}
  break;
case 337:
					// line 1224 "DefaultRubyParser.y"
  {
                  yyVal = new ZeroArgNode(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])));
              }
  break;
case 338:
					// line 1227 "DefaultRubyParser.y"
  {
                  yyVal = new ZeroArgNode(((Token)yyVals[0+yyTop]).getPosition());
	      }
  break;
case 339:
					// line 1230 "DefaultRubyParser.y"
  {
                  yyVal = ((Node)yyVals[-1+yyTop]);

		  /* Include pipes on multiple arg type*/
                  if (((Node)yyVals[-1+yyTop]) instanceof MultipleAsgnNode) {
		      ((Node)yyVals[-1+yyTop]).setPosition(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
		  } 
              }
  break;
case 340:
					// line 1239 "DefaultRubyParser.y"
  {
                  support.pushBlockScope();
	      }
  break;
case 341:
					// line 1241 "DefaultRubyParser.y"
  {
                  yyVal = new IterNode(support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), support.getCurrentScope(), ((Node)yyVals[-1+yyTop]), ((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop]));
                  support.popCurrentScope();
              }
  break;
case 342:
					// line 1246 "DefaultRubyParser.y"
  {
	          if (((Node)yyVals[-1+yyTop]) != null && 
                      ((BlockAcceptingNode)yyVals[-1+yyTop]).getIterNode() instanceof BlockPassNode) {
                      throw new SyntaxException(getPosition(((Node)yyVals[-1+yyTop])), "Both block arg and actual block given.");
                  }
		  ((BlockAcceptingNode)yyVals[-1+yyTop]).setIterNode(((IterNode)yyVals[0+yyTop]));
		  ((Node)yyVals[-1+yyTop]).setPosition(support.union(((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop])));
              }
  break;
case 343:
					// line 1254 "DefaultRubyParser.y"
  {
                  yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
              }
  break;
case 344:
					// line 1257 "DefaultRubyParser.y"
  {
                  yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
              }
  break;
case 345:
					// line 1261 "DefaultRubyParser.y"
  {
                  yyVal = support.new_fcall(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
              }
  break;
case 346:
					// line 1264 "DefaultRubyParser.y"
  {
                  yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
              }
  break;
case 347:
					// line 1267 "DefaultRubyParser.y"
  {
                  yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
              }
  break;
case 348:
					// line 1270 "DefaultRubyParser.y"
  {
                  yyVal = support.new_call(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]), null, null);
              }
  break;
case 349:
					// line 1273 "DefaultRubyParser.y"
  {
                  yyVal = support.new_super(((Node)yyVals[0+yyTop]), ((Token)yyVals[-1+yyTop]));
              }
  break;
case 350:
					// line 1276 "DefaultRubyParser.y"
  {
                  yyVal = new ZSuperNode(((Token)yyVals[0+yyTop]).getPosition());
              }
  break;
case 351:
					// line 1281 "DefaultRubyParser.y"
  {
                  support.pushBlockScope();
	      }
  break;
case 352:
					// line 1283 "DefaultRubyParser.y"
  {
                  yyVal = new IterNode(support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), support.getCurrentScope(), ((Node)yyVals[-1+yyTop]), ((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop]));
                  support.popCurrentScope();
              }
  break;
case 353:
					// line 1287 "DefaultRubyParser.y"
  {
                  support.pushBlockScope();
	      }
  break;
case 354:
					// line 1289 "DefaultRubyParser.y"
  {
                  yyVal = new IterNode(support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), support.getCurrentScope(), ((Node)yyVals[-1+yyTop]), ((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop]));
                  ((ISourcePositionHolder)yyVals[-5+yyTop]).setPosition(support.union(((ISourcePositionHolder)yyVals[-5+yyTop]), ((ISourcePositionHolder)yyVal)));
                  support.popCurrentScope();
              }
  break;
case 355:
					// line 1295 "DefaultRubyParser.y"
  {
                  yyVal = new WhenNode(support.union(((Token)yyVals[-4+yyTop]), support.unwrapNewlineNode(((Node)yyVals[-1+yyTop]))), ((ListNode)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
              }
  break;
case 357:
					// line 1300 "DefaultRubyParser.y"
  {
                  yyVal = ((ListNode)yyVals[-3+yyTop]).add(new WhenNode(getPosition(((ListNode)yyVals[-3+yyTop])), ((Node)yyVals[0+yyTop]), null, null));
              }
  break;
case 358:
					// line 1303 "DefaultRubyParser.y"
  {
                  yyVal = new ArrayNode(getPosition(((Token)yyVals[-1+yyTop])), new WhenNode(getPosition(((Token)yyVals[-1+yyTop])), ((Node)yyVals[0+yyTop]), null, null));
              }
  break;
case 361:
					// line 1310 "DefaultRubyParser.y"
  {
                  Node node;
                  if (((Node)yyVals[-3+yyTop]) != null) {
                     node = support.appendToBlock(support.node_assign(((Node)yyVals[-3+yyTop]), new GlobalVarNode(getPosition(((Token)yyVals[-5+yyTop])), "$!")), ((Node)yyVals[-1+yyTop]));
                     if(((Node)yyVals[-1+yyTop]) != null) {
                        node.setPosition(support.unwrapNewlineNode(((Node)yyVals[-1+yyTop])).getPosition());
                     }
		  } else {
		     node = ((Node)yyVals[-1+yyTop]);
                  }
                  yyVal = new RescueBodyNode(getPosition(((Token)yyVals[-5+yyTop]), true), ((Node)yyVals[-4+yyTop]), node, ((RescueBodyNode)yyVals[0+yyTop]), ((Token)yyVals[-5+yyTop]));
	      }
  break;
case 362:
					// line 1322 "DefaultRubyParser.y"
  {yyVal = null;}
  break;
case 363:
					// line 1324 "DefaultRubyParser.y"
  {
                  yyVal = new ArrayNode(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]));
	      }
  break;
case 366:
					// line 1330 "DefaultRubyParser.y"
  {
                  yyVal = ((Node)yyVals[0+yyTop]);
              }
  break;
case 368:
					// line 1335 "DefaultRubyParser.y"
  {
                  if (((Node)yyVals[0+yyTop]) != null) {
                      yyVal = new EnsureNode.Keyword(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
                  } else {
                      yyVal = new EnsureNode.Keyword(((Token)yyVals[-1+yyTop]), new NilNode(getPosition(null)));
                  }
              }
  break;
case 369:
					// line 1342 "DefaultRubyParser.y"
  {
                  yyVal = null;
              }
  break;
case 371:
					// line 1347 "DefaultRubyParser.y"
  {
                  yyVal = new SymbolNode(((Token)yyVals[0+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue());
              }
  break;
case 373:
					// line 1352 "DefaultRubyParser.y"
  {
                  if (((Node)yyVals[0+yyTop]) instanceof EvStrNode) {
                      yyVal = new DStrNode(getPosition(((Node)yyVals[0+yyTop]))).add(((Node)yyVals[0+yyTop]));
                  } else {
                      yyVal = ((Node)yyVals[0+yyTop]);
                  }
	      }
  break;
case 375:
					// line 1361 "DefaultRubyParser.y"
  {
                  yyVal = support.literal_concat(getPosition(((Node)yyVals[-1+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
              }
  break;
case 376:
					// line 1365 "DefaultRubyParser.y"
  {
                support.pushStringType(((Token)yyVals[0+yyTop]) instanceof HeredocToken ? StringType.HEREDOC_STRING : StringType.STRING);
              }
  break;
case 377:
					// line 1367 "DefaultRubyParser.y"
  {
                  if (support.popStringType().isHeredoc()) {
                    yyVal = new HeredocNode(((Token)yyVals[-3+yyTop]).getPosition(), ((Node)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop]), ((HeredocToken)yyVals[-3+yyTop]).isIndent());
                  }
                  else {
                    yyVal = ((Node)yyVals[-1+yyTop]);
                    ((ISourcePositionHolder)yyVal).setPosition(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])));
                    int extraLength = ((String) ((Token)yyVals[-3+yyTop]).getValue()).length() - 1;

                    /* We may need to subtract addition offset off of first */
                    /* string fragment (we optimistically take one off in*/
                    /* ParserSupport.literal_concat).  Check token length*/
                    /* and subtract as neeeded.*/
                    if ((((Node)yyVals[-1+yyTop]) instanceof DStrNode) && extraLength > 0) {
                       Node strNode = ((DStrNode)((Node)yyVals[-1+yyTop])).get(0);
                       assert strNode != null;
                       strNode.getPosition().adjustStartOffset(-extraLength);
                    }
                  }
              }
  break;
case 378:
					// line 1388 "DefaultRubyParser.y"
  {
                support.pushStringType(((Token)yyVals[0+yyTop]) instanceof HeredocToken ? StringType.HEREDOC_XSTRING : StringType.XSTRING);
              }
  break;
case 379:
					// line 1390 "DefaultRubyParser.y"
  {
                  if (support.popStringType().isHeredoc()) {
                    yyVal = new HeredocNode(((Token)yyVals[-3+yyTop]).getPosition(), ((Node)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop]), ((HeredocToken)yyVals[-3+yyTop]).isIndent());
                  }
                  else {
                    ISourcePosition position = support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop]));

                    if (((Node)yyVals[-1+yyTop]) == null) {
                        yyVal = new XStrNode(position, null);
                    } else if (((Node)yyVals[-1+yyTop]) instanceof StrNode) {
                        yyVal = new XStrNode(position, (ByteList) ((StrNode)yyVals[-1+yyTop]).getValue().clone());
                    } else if (((Node)yyVals[-1+yyTop]) instanceof DStrNode) {
                        yyVal = new DXStrNode(position, ((DStrNode)yyVals[-1+yyTop]));

                        ((Node)yyVal).setPosition(position);
                    } else {
                        yyVal = new DXStrNode(position).add(((Node)yyVals[-1+yyTop]));
                    }
                  }
              }
  break;
case 380:
					// line 1411 "DefaultRubyParser.y"
  {
                 support.pushStringType(StringType.REGEXP);
              }
  break;
case 381:
					// line 1413 "DefaultRubyParser.y"
  {
		  int options = ((RegexpNode)yyVals[0+yyTop]).getOptions();
		  Node node = ((Node)yyVals[-1+yyTop]);

		  if (node == null) {
                      yyVal = new RegexpNode(getPosition(((Token)yyVals[-3+yyTop])), ByteList.createEmpty(), options & ~ReOptions.RE_OPTION_ONCE);
		  } else if (node instanceof StrNode) {
                      yyVal = new RegexpNode(((Node)yyVals[-1+yyTop]).getPosition(), (ByteList) ((StrNode) node).getValue().clone(), options & ~ReOptions.RE_OPTION_ONCE);
		  } else if (node instanceof DStrNode) {
                      yyVal = new DRegexpNode(getPosition(((Token)yyVals[-3+yyTop])), (DStrNode) node, options, (options & ReOptions.RE_OPTION_ONCE) != 0);
		  } else {
		      yyVal = new DRegexpNode(getPosition(((Token)yyVals[-3+yyTop])), options, (options & ReOptions.RE_OPTION_ONCE) != 0).add(node);
                  }
                  support.popStringType();
	       }
  break;
case 382:
					// line 1429 "DefaultRubyParser.y"
  {
                   yyVal = new ZArrayNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
	       }
  break;
case 383:
					// line 1432 "DefaultRubyParser.y"
  {
	         support.pushStringType(StringType.STRING);
	       }
  break;
case 384:
					// line 1434 "DefaultRubyParser.y"
  {
		   yyVal = ((ListNode)yyVals[-1+yyTop]);
                   ((ISourcePositionHolder)yyVal).setPosition(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])));
                   support.popStringType();
	       }
  break;
case 385:
					// line 1440 "DefaultRubyParser.y"
  {
                   yyVal = new ArrayNode(getPosition(null));
	       }
  break;
case 386:
					// line 1443 "DefaultRubyParser.y"
  {
                   yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[-1+yyTop]) instanceof EvStrNode ? new DStrNode(getPosition(((ListNode)yyVals[-2+yyTop]))).add(((Node)yyVals[-1+yyTop])) : ((Node)yyVals[-1+yyTop]));
	       }
  break;
case 388:
					// line 1448 "DefaultRubyParser.y"
  {
                   yyVal = support.literal_concat(getPosition(((Node)yyVals[-1+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
	       }
  break;
case 389:
					// line 1452 "DefaultRubyParser.y"
  {
                   yyVal = new ZArrayNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
	       }
  break;
case 390:
					// line 1455 "DefaultRubyParser.y"
  {
		   yyVal = ((ListNode)yyVals[-1+yyTop]);
                   ((ISourcePositionHolder)yyVal).setPosition(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
	       }
  break;
case 391:
					// line 1460 "DefaultRubyParser.y"
  {
                   yyVal = new ArrayNode(getPosition(null));
	       }
  break;
case 392:
					// line 1463 "DefaultRubyParser.y"
  {
                   yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[-1+yyTop]));
	       }
  break;
case 393:
					// line 1467 "DefaultRubyParser.y"
  {
                   yyVal = new StrNode(lexer.getPositionFactory().getDummyPosition(), ByteList.createEmpty());
	       }
  break;
case 394:
					// line 1470 "DefaultRubyParser.y"
  {
                   yyVal = support.literal_concat(getPosition(((Node)yyVals[-1+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
	       }
  break;
case 395:
					// line 1474 "DefaultRubyParser.y"
  {
		   yyVal = null;
	       }
  break;
case 396:
					// line 1477 "DefaultRubyParser.y"
  {
                   yyVal = support.literal_concat(getPosition(((Node)yyVals[-1+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
	       }
  break;
case 397:
					// line 1481 "DefaultRubyParser.y"
  {
                   yyVal = ((Node)yyVals[0+yyTop]);
               }
  break;
case 398:
					// line 1484 "DefaultRubyParser.y"
  {
                   yyVal = lexer.getStrTerm();
		   lexer.setStrTerm(null);
		   lexer.setState(LexState.EXPR_BEG);
	       }
  break;
case 399:
					// line 1488 "DefaultRubyParser.y"
  {
		   lexer.setStrTerm(((StrTerm)yyVals[-1+yyTop]));
	           yyVal = new EvStrNode(support.union(((Token)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));
	       }
  break;
case 400:
					// line 1492 "DefaultRubyParser.y"
  {
		   yyVal = lexer.getStrTerm();
		   lexer.setStrTerm(null);
		   lexer.setState(LexState.EXPR_BEG);
	       }
  break;
case 401:
					// line 1496 "DefaultRubyParser.y"
  {
		   lexer.setStrTerm(((StrTerm)yyVals[-2+yyTop]));

		   yyVal = support.newEvStrNode(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-1+yyTop]));
	       }
  break;
case 402:
					// line 1502 "DefaultRubyParser.y"
  {
                   yyVal = new GlobalVarNode(((Token)yyVals[0+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue());
               }
  break;
case 403:
					// line 1505 "DefaultRubyParser.y"
  {
                   yyVal = new InstVarNode(((Token)yyVals[0+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue());
               }
  break;
case 404:
					// line 1508 "DefaultRubyParser.y"
  {
                   yyVal = new ClassVarNode(((Token)yyVals[0+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue());
               }
  break;
case 406:
					// line 1514 "DefaultRubyParser.y"
  {
                   lexer.setState(LexState.EXPR_END);
                   yyVal = ((Token)yyVals[0+yyTop]);
		   ((ISourcePositionHolder)yyVal).setPosition(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])));
               }
  break;
case 411:
					// line 1522 "DefaultRubyParser.y"
  {
                   lexer.setState(LexState.EXPR_END);

		   /* DStrNode: :"some text #{some expression}"*/
                   /* StrNode: :"some text"*/
		   /* EvStrNode :"#{some expression}"*/
                   if (((Node)yyVals[-1+yyTop]) == null) {
                       yyerror("empty symbol literal");
                   }

		   if (((Node)yyVals[-1+yyTop]) instanceof DStrNode) {
		       yyVal = new DSymbolNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), ((DStrNode)yyVals[-1+yyTop]));
		   } else {
                       ISourcePosition position = support.union(((Node)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop]));

                       /* We substract one since tsymbeg is longer than one*/
		       /* and we cannot union it directly so we assume quote*/
                       /* is one character long and subtract for it.*/
		       position.adjustStartOffset(-1);
                       ((Node)yyVals[-1+yyTop]).setPosition(position);
		       
		       yyVal = new DSymbolNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
                       ((DSymbolNode)yyVal).add(((Node)yyVals[-1+yyTop]));
                   }
	       }
  break;
case 413:
					// line 1549 "DefaultRubyParser.y"
  {
                   yyVal = ((FloatNode)yyVals[0+yyTop]);
               }
  break;
case 414:
					// line 1552 "DefaultRubyParser.y"
  {
                   yyVal = support.negateInteger(((Node)yyVals[0+yyTop]));
	       }
  break;
case 415:
					// line 1555 "DefaultRubyParser.y"
  {
                   yyVal = support.negateFloat(((FloatNode)yyVals[0+yyTop]));
	       }
  break;
case 421:
					// line 1561 "DefaultRubyParser.y"
  { 
		   yyVal = new Token("nil", ((Token)yyVals[0+yyTop]).getPosition());
               }
  break;
case 422:
					// line 1564 "DefaultRubyParser.y"
  {
		   yyVal = new Token("self", ((Token)yyVals[0+yyTop]).getPosition());
               }
  break;
case 423:
					// line 1567 "DefaultRubyParser.y"
  { 
		   yyVal = new Token("true", ((Token)yyVals[0+yyTop]).getPosition());
               }
  break;
case 424:
					// line 1570 "DefaultRubyParser.y"
  {
		   yyVal = new Token("false", ((Token)yyVals[0+yyTop]).getPosition());
               }
  break;
case 425:
					// line 1573 "DefaultRubyParser.y"
  {
		   yyVal = new Token("__FILE__", ((Token)yyVals[0+yyTop]).getPosition());
               }
  break;
case 426:
					// line 1576 "DefaultRubyParser.y"
  {
		   yyVal = new Token("__LINE__", ((Token)yyVals[0+yyTop]).getPosition());
               }
  break;
case 427:
					// line 1580 "DefaultRubyParser.y"
  {
		   yyVal = support.gettable((String) ((Token)yyVals[0+yyTop]).getValue(), ((Token)yyVals[0+yyTop]).getPosition());
               }
  break;
case 428:
					// line 1584 "DefaultRubyParser.y"
  {
                   yyVal = support.assignable(((Token)yyVals[0+yyTop]), null);
               }
  break;
case 431:
					// line 1590 "DefaultRubyParser.y"
  {
                   yyVal = null;
               }
  break;
case 432:
					// line 1593 "DefaultRubyParser.y"
  {
                   lexer.setState(LexState.EXPR_BEG);
               }
  break;
case 433:
					// line 1595 "DefaultRubyParser.y"
  {
                   yyVal = ((Node)yyVals[-1+yyTop]);
               }
  break;
case 434:
					// line 1598 "DefaultRubyParser.y"
  {
                   yyerrok();
                   yyVal = null;
               }
  break;
case 435:
					// line 1604 "DefaultRubyParser.y"
  {
                   yyVal = ((Node)yyVals[-2+yyTop]);
                   ((ISourcePositionHolder)yyVal).setPosition(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])));
                   lexer.setState(LexState.EXPR_BEG);
               }
  break;
case 436:
					// line 1609 "DefaultRubyParser.y"
  {
                   yyVal = ((Node)yyVals[-1+yyTop]);
               }
  break;
case 437:
					// line 1613 "DefaultRubyParser.y"
  {
                   yyVal = new ArgsNode(support.union(((ListNode)yyVals[-5+yyTop]), ((BlockArgNode)yyVals[0+yyTop])), ((ListNode)yyVals[-5+yyTop]), ((ListNode)yyVals[-3+yyTop]), ((Integer) ((Token)yyVals[-1+yyTop]).getValue()).intValue(), ((BlockArgNode)yyVals[0+yyTop]));
               }
  break;
case 438:
					// line 1616 "DefaultRubyParser.y"
  {
                   yyVal = new ArgsNode(getPosition(((ListNode)yyVals[-3+yyTop])), ((ListNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), -1, ((BlockArgNode)yyVals[0+yyTop]));
               }
  break;
case 439:
					// line 1619 "DefaultRubyParser.y"
  {
                   yyVal = new ArgsNode(support.union(((ListNode)yyVals[-3+yyTop]), ((BlockArgNode)yyVals[0+yyTop])), ((ListNode)yyVals[-3+yyTop]), null, ((Integer) ((Token)yyVals[-1+yyTop]).getValue()).intValue(), ((BlockArgNode)yyVals[0+yyTop]));
               }
  break;
case 440:
					// line 1622 "DefaultRubyParser.y"
  {
                   yyVal = new ArgsNode(((ISourcePositionHolder)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop]), null, -1, ((BlockArgNode)yyVals[0+yyTop]));
               }
  break;
case 441:
					// line 1625 "DefaultRubyParser.y"
  {
                   yyVal = new ArgsNode(getPosition(((ListNode)yyVals[-3+yyTop])), null, ((ListNode)yyVals[-3+yyTop]), ((Integer) ((Token)yyVals[-1+yyTop]).getValue()).intValue(), ((BlockArgNode)yyVals[0+yyTop]));
               }
  break;
case 442:
					// line 1628 "DefaultRubyParser.y"
  {
                   yyVal = new ArgsNode(getPosition(((ListNode)yyVals[-1+yyTop])), null, ((ListNode)yyVals[-1+yyTop]), -1, ((BlockArgNode)yyVals[0+yyTop]));
               }
  break;
case 443:
					// line 1631 "DefaultRubyParser.y"
  {
                   yyVal = new ArgsNode(getPosition(((Token)yyVals[-1+yyTop])), null, null, ((Integer) ((Token)yyVals[-1+yyTop]).getValue()).intValue(), ((BlockArgNode)yyVals[0+yyTop]));
               }
  break;
case 444:
					// line 1634 "DefaultRubyParser.y"
  {
                   yyVal = new ArgsNode(getPosition(((BlockArgNode)yyVals[0+yyTop])), null, null, -1, ((BlockArgNode)yyVals[0+yyTop]));
               }
  break;
case 445:
					// line 1637 "DefaultRubyParser.y"
  {
                   yyVal = new ArgsNode(support.createEmptyArgsNodePosition(getPosition(null)), null, null, -1, null);
               }
  break;
case 446:
					// line 1641 "DefaultRubyParser.y"
  {
                   yyerror("formal argument cannot be a constant");
               }
  break;
case 447:
					// line 1644 "DefaultRubyParser.y"
  {
                   yyerror("formal argument cannot be an instance variable");
               }
  break;
case 448:
					// line 1647 "DefaultRubyParser.y"
  {
                   yyerror("formal argument cannot be a class variable");
               }
  break;
case 449:
					// line 1650 "DefaultRubyParser.y"
  {
                   String identifier = (String) ((Token)yyVals[0+yyTop]).getValue();
                   if (IdUtil.getVarType(identifier) != IdUtil.LOCAL_VAR) {
                       yyerror("formal argument must be local variable");
                   } else if (support.getCurrentScope().getLocalScope().isDefined(identifier) >= 0) {
                       yyerror("duplicate argument name");
                   }

		   support.getCurrentScope().getLocalScope().addVariable(identifier);
                   yyVal = ((Token)yyVals[0+yyTop]);
               }
  break;
case 450:
					// line 1662 "DefaultRubyParser.y"
  {
                    yyVal = new ListNode(((ISourcePositionHolder)yyVals[0+yyTop]).getPosition());
                    ((ListNode) yyVal).add(new ArgumentNode(((ISourcePositionHolder)yyVals[0+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue()));
               }
  break;
case 451:
					// line 1666 "DefaultRubyParser.y"
  {
                   ((ListNode)yyVals[-2+yyTop]).add(new ArgumentNode(((ISourcePositionHolder)yyVals[0+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue()));
                   ((ListNode)yyVals[-2+yyTop]).setPosition(support.union(((ListNode)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
		   yyVal = ((ListNode)yyVals[-2+yyTop]);
               }
  break;
case 452:
					// line 1672 "DefaultRubyParser.y"
  {
                   String identifier = (String) ((Token)yyVals[-2+yyTop]).getValue();

                   if (IdUtil.getVarType(identifier) != IdUtil.LOCAL_VAR) {
                       yyerror("formal argument must be local variable");
                   } else if (support.getCurrentScope().getLocalScope().isDefined(identifier) >= 0) {
                       yyerror("duplicate optional argument name");
                   }
		   support.getCurrentScope().getLocalScope().addVariable(identifier);
                   yyVal = support.assignable(((Token)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
              }
  break;
case 453:
					// line 1684 "DefaultRubyParser.y"
  {
                  yyVal = new BlockNode(getPosition(((Node)yyVals[0+yyTop]))).add(((Node)yyVals[0+yyTop]));
              }
  break;
case 454:
					// line 1687 "DefaultRubyParser.y"
  {
                  yyVal = support.appendToBlock(((ListNode)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
              }
  break;
case 457:
					// line 1693 "DefaultRubyParser.y"
  {
                  String identifier = (String) ((Token)yyVals[0+yyTop]).getValue();

                  if (IdUtil.getVarType(identifier) != IdUtil.LOCAL_VAR) {
                      yyerror("rest argument must be local variable");
                   } else if (support.getCurrentScope().getLocalScope().isDefined(identifier) >= 0) {
                      yyerror("duplicate rest argument name");
                  }
		  ((Token)yyVals[-1+yyTop]).setValue(new Integer(support.getCurrentScope().getLocalScope().addVariable(identifier)));
                  yyVal = ((Token)yyVals[-1+yyTop]);
              }
  break;
case 458:
					// line 1704 "DefaultRubyParser.y"
  {
                  ((Token)yyVals[0+yyTop]).setValue(new Integer(-2));
                  yyVal = ((Token)yyVals[0+yyTop]);
              }
  break;
case 461:
					// line 1711 "DefaultRubyParser.y"
  {
                  String identifier = (String) ((Token)yyVals[0+yyTop]).getValue();

                  if (IdUtil.getVarType(identifier) != IdUtil.LOCAL_VAR) {
                      yyerror("block argument must be local variable");
		  } else if (support.getCurrentScope().getLocalScope().isDefined(identifier) >= 0) {
                      yyerror("duplicate block argument name");
                  }
                  yyVal = new BlockArgNode(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), support.getCurrentScope().getLocalScope().addVariable(identifier), identifier);
              }
  break;
case 462:
					// line 1722 "DefaultRubyParser.y"
  {
                  yyVal = ((BlockArgNode)yyVals[0+yyTop]);
              }
  break;
case 463:
					// line 1725 "DefaultRubyParser.y"
  {
	          yyVal = null;
	      }
  break;
case 464:
					// line 1729 "DefaultRubyParser.y"
  {
                  if (!(((Node)yyVals[0+yyTop]) instanceof SelfNode)) {
		      support.checkExpression(((Node)yyVals[0+yyTop]));
		  }
		  yyVal = ((Node)yyVals[0+yyTop]);
              }
  break;
case 465:
					// line 1735 "DefaultRubyParser.y"
  {
                  lexer.setState(LexState.EXPR_BEG);
              }
  break;
case 466:
					// line 1737 "DefaultRubyParser.y"
  {
                  if (((Node)yyVals[-2+yyTop]) instanceof ILiteralNode) {
                      yyerror("Can't define single method for literals.");
                  }
		  support.checkExpression(((Node)yyVals[-2+yyTop]));
                  yyVal = ((Node)yyVals[-2+yyTop]);
              }
  break;
case 467:
					// line 1747 "DefaultRubyParser.y"
  { /* [!null]*/
                  yyVal = new ArrayNode(getPosition(null));
              }
  break;
case 468:
					// line 1750 "DefaultRubyParser.y"
  { /* [!null]*/
                  yyVal = ((ListNode)yyVals[-1+yyTop]);
              }
  break;
case 469:
					// line 1753 "DefaultRubyParser.y"
  {
                  if (((ListNode)yyVals[-1+yyTop]).size() % 2 != 0) {
                      yyerror("Odd number list for Hash.");
                  }
                  yyVal = ((ListNode)yyVals[-1+yyTop]);
              }
  break;
case 471:
					// line 1762 "DefaultRubyParser.y"
  { /* [!null]*/
                  yyVal = ((ListNode)yyVals[-2+yyTop]).addAll(((ListNode)yyVals[0+yyTop]));
              }
  break;
case 472:
					// line 1767 "DefaultRubyParser.y"
  { /* [!null]*/
                  yyVal = new ArrayNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop])).add(((Node)yyVals[0+yyTop]));
              }
  break;
case 473:
					// line 1771 "DefaultRubyParser.y"
  {
                  yyVal = new ArrayNode(support.union(((Token)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), new SymbolNode(((Token)yyVals[-2+yyTop]).getPosition(), (String) ((Token)yyVals[-2+yyTop]).getValue())).add(((Node)yyVals[0+yyTop]));
              }
  break;
case 493:
					// line 1783 "DefaultRubyParser.y"
  {
                  yyerrok();
                  yyVal = new Token(";", lexer.getPosition());
              }
  break;
case 494:
					// line 1787 "DefaultRubyParser.y"
  {
                  yyVal = new Token(";", lexer.getPosition());
              }
  break;
case 496:
					// line 1792 "DefaultRubyParser.y"
  {
                  yyerrok();
              }
  break;
case 497:
					// line 1796 "DefaultRubyParser.y"
  {
                  yyVal = null;
              }
  break;
case 498:
					// line 1800 "DefaultRubyParser.y"
  {  
                  yyVal = null;
	      }
  break;
					// line 7612 "-"
        }
        yyTop -= yyLen[yyN];
        yyState = yyStates[yyTop];
        int yyM = yyLhs[yyN];
        if (yyState == 0 && yyM == 0) {
          yyState = yyFinal;
          if (yyToken < 0) {
            yyToken = yyLex.advance() ? yyLex.token() : 0;
          }
          if (yyToken == 0) {
            return yyVal;
          }
          continue yyLoop;
        }
        if ((yyN = yyGindex[yyM]) != 0 && (yyN += yyState) >= 0
            && yyN < yyTable.length && yyCheck[yyN] == yyState)
          yyState = yyTable[yyN];
        else
          yyState = yyDgoto[yyM];
        continue yyLoop;
      }
    }
  }

					// line 1805 "DefaultRubyParser.y"

    /** The parse method use an lexer stream and parse it to an AST node 
     * structure
     */
    public RubyParserResult parse(RubyParserConfiguration configuration, LexerSource source) {
        support.reset();
        support.setConfiguration(configuration);
        support.setResult(new RubyParserResult());
        
        lexer.reset();
        lexer.setSource(source);
        try {
	    //yyparse(lexer, new jay.yydebug.yyAnim("JRuby", 9));
	    //yyparse(lexer, new jay.yydebug.yyDebugAdapter());
	    yyparse(lexer, null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (yyException e) {
            e.printStackTrace();
        }
        
        return support.getResult();
    }

    // +++
    // Helper Methods
    
    void yyerrok() {}

    /**
     * Since we can recieve positions at times we know can be null we
     * need an extra safety net here.
     */
    private ISourcePosition getPosition2(ISourcePositionHolder pos) {
        return pos == null ? lexer.getPosition(null, false) : pos.getPosition();
    }

    private ISourcePosition getPosition(ISourcePositionHolder start) {
        return getPosition(start, false);
    }

    private ISourcePosition getPosition(ISourcePositionHolder start, boolean inclusive) {
        if (start != null) {
	    return lexer.getPosition(start.getPosition(), inclusive);
	} 
	
	return lexer.getPosition(null, inclusive);
    }
}
					// line 7692 "-"
