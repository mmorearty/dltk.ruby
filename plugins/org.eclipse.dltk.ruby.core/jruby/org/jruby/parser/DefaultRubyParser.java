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
import org.jruby.util.IdUtil;
import org.jruby.util.ByteList;

@SuppressWarnings({"nls","cast"})
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
					// line 150 "-"
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
  public static final int tLOWEST = 375;
  public static final int tLAST_TOKEN = 376;
  public static final int yyErrorCode = 256;

  /** number of final state.
    */
  protected static final int yyFinal = 1;

  /** parser tables.
      Order is mandated by <i>jay</i>.
    */
  protected static final short[] yyLhs = {
//yyLhs 494
    -1,    99,     0,    20,    19,    21,    21,    21,    21,   102,
    22,    22,    22,    22,    22,    22,    22,    22,    22,    22,
   103,    22,    22,    22,    22,    22,    22,    22,    22,    22,
    22,    22,    22,    22,    22,    23,    23,    23,    23,    23,
    23,    27,    18,    18,    18,    18,    18,    43,    43,    43,
   104,    78,    26,    26,    26,    26,    26,    26,    26,    26,
    79,    79,    81,    81,    80,    80,    80,    80,    80,    80,
    51,    51,    67,    67,    52,    52,    52,    52,    52,    52,
    52,    52,    59,    59,    59,    59,    59,    59,    59,    59,
    91,    91,    17,    17,    17,    92,    92,    92,    92,    92,
    85,    85,    47,   106,    47,    93,    93,    93,    93,    93,
    93,    93,    93,    93,    93,    93,    93,    93,    93,    93,
    93,    93,    93,    93,    93,    93,    93,    93,    93,    93,
    93,   105,   105,   105,   105,   105,   105,   105,   105,   105,
   105,   105,   105,   105,   105,   105,   105,   105,   105,   105,
   105,   105,   105,   105,   105,   105,   105,   105,   105,   105,
   105,   105,   105,   105,   105,   105,   105,   105,   105,   105,
   105,   105,    24,    24,    24,    24,    24,    24,    24,    24,
    24,    24,    24,    24,    24,    24,    24,    24,    24,    24,
    24,    24,    24,    24,    24,    24,    24,    24,    24,    24,
    24,    24,    24,    24,    24,    24,    24,    24,    24,    24,
    24,    24,    24,    24,    24,    24,    53,    56,    56,    56,
    56,    56,    56,    37,    37,    37,    37,    41,    41,    33,
    33,    33,    33,    33,    33,    33,    33,    33,    34,    34,
    34,    34,    34,    34,    34,    34,    34,    34,    34,    34,
   109,    39,    35,   110,    35,   111,    35,    72,    71,    71,
    65,    65,    50,    50,    50,    25,    25,    25,    25,    25,
    25,    25,    25,    25,    25,   112,    25,    25,    25,    25,
    25,    25,    25,    25,    25,    25,    25,    25,    25,    25,
    25,    25,    25,   114,   116,    25,   117,   118,    25,    25,
    25,    25,   119,   120,    25,   121,    25,   123,   124,    25,
   125,    25,   126,    25,   127,   128,    25,    25,    25,    25,
    25,    28,   113,   113,   113,   113,   115,   115,   115,    31,
    31,    29,    29,    57,    57,    58,    58,    58,    58,   129,
    77,    42,    42,    42,    10,    10,    10,    10,    10,    10,
   130,    76,   131,    76,    54,    66,    66,    66,    30,    30,
    82,    82,    55,    55,    55,    32,    32,    36,    36,    14,
    14,    14,     2,     3,     3,     4,     5,     6,    11,    11,
    62,    62,    13,    13,    12,    12,    61,    61,     7,     7,
     8,     8,     9,   132,     9,   133,     9,    48,    48,    48,
    48,    87,    86,    86,    86,    86,    16,    15,    15,    15,
    15,    84,    84,    84,    84,    84,    84,    84,    84,    84,
    84,    84,    40,    83,    49,    49,    38,   134,    38,    38,
    44,    44,    45,    45,    45,    45,    45,    45,    45,    45,
    45,    94,    94,    94,    94,    63,    63,    46,    64,    64,
    97,    97,    95,    95,    98,    98,    75,    74,    74,     1,
   135,     1,    70,    70,    70,    68,    68,    69,    69,    88,
    88,    88,    89,    89,    89,    89,    90,    90,    90,    96,
    96,   100,   100,   107,   107,   108,   108,   108,   122,   122,
   101,   101,    60,    73,
    }, yyLen = {
//yyLen 494
     2,     0,     2,     4,     2,     1,     1,     3,     2,     0,
     4,     3,     3,     3,     2,     3,     3,     3,     3,     3,
     0,     5,     4,     3,     3,     3,     6,     5,     5,     5,
     3,     3,     3,     3,     1,     1,     3,     3,     2,     2,
     1,     1,     1,     1,     2,     2,     2,     1,     4,     4,
     0,     5,     2,     3,     4,     5,     4,     5,     2,     2,
     1,     3,     1,     3,     1,     2,     3,     2,     2,     1,
     1,     3,     2,     3,     1,     4,     3,     3,     3,     3,
     2,     1,     1,     4,     3,     3,     3,     3,     2,     1,
     1,     1,     2,     1,     3,     1,     1,     1,     1,     1,
     1,     1,     1,     0,     4,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     3,     5,     3,     6,     5,     5,     5,     5,
     4,     3,     3,     3,     3,     3,     3,     3,     3,     3,
     4,     4,     2,     2,     3,     3,     3,     3,     3,     3,
     3,     3,     3,     3,     3,     3,     3,     2,     2,     3,
     3,     3,     3,     3,     5,     1,     1,     1,     2,     2,
     5,     2,     3,     3,     4,     4,     6,     1,     1,     1,
     2,     5,     2,     5,     4,     7,     3,     1,     4,     3,
     5,     7,     2,     5,     4,     6,     7,     9,     3,     1,
     0,     2,     1,     0,     3,     0,     4,     2,     2,     1,
     1,     3,     3,     4,     2,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     3,     0,     5,     3,     3,     2,
     4,     3,     3,     1,     4,     3,     1,     5,     2,     1,
     2,     6,     6,     0,     0,     7,     0,     0,     7,     5,
     4,     5,     0,     0,     9,     0,     6,     0,     0,     8,
     0,     5,     0,     6,     0,     0,     9,     1,     1,     1,
     1,     1,     1,     1,     1,     2,     1,     1,     1,     1,
     5,     1,     2,     1,     1,     1,     2,     1,     3,     0,
     5,     2,     4,     4,     2,     4,     4,     3,     2,     1,
     0,     5,     0,     5,     5,     1,     4,     2,     1,     1,
     6,     0,     1,     1,     1,     2,     1,     2,     1,     1,
     1,     1,     1,     1,     2,     3,     3,     3,     3,     3,
     0,     3,     1,     2,     3,     3,     0,     3,     0,     2,
     0,     2,     1,     0,     3,     0,     4,     1,     1,     1,
     1,     2,     1,     1,     1,     1,     3,     1,     1,     2,
     2,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     0,     4,     2,
     4,     2,     6,     4,     4,     2,     4,     2,     2,     1,
     0,     1,     1,     1,     1,     1,     3,     3,     1,     3,
     1,     1,     2,     1,     1,     1,     2,     2,     0,     1,
     0,     5,     1,     2,     2,     1,     3,     3,     3,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     0,     1,     0,     1,     0,     1,     1,     1,     1,
     1,     2,     0,     0,
    }, yyDefRed = {
//yyDefRed 887
     1,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,   293,   296,     0,     0,     0,   319,   320,     0,
     0,     0,   417,   416,   418,   419,     0,     0,     0,    20,
     0,   421,   420,     0,     0,   413,   412,     0,   415,   424,
   425,   407,   408,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,   388,   390,   390,     0,     0,
   266,     0,   373,   267,   268,     0,   269,   270,   265,   369,
   371,    35,     2,     0,     0,     0,     0,     0,     0,     0,
   271,     0,    43,     0,     0,    70,     0,     5,     0,     0,
    60,     0,     0,   370,     0,     0,   317,   318,   283,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,   321,
     0,   272,   422,     0,    93,   310,   140,   151,   141,   164,
   137,   157,   147,   146,   162,   145,   144,   139,   165,   149,
   138,   152,   156,   158,   150,   143,   159,   166,   161,     0,
     0,     0,     0,   136,   155,   154,   167,   168,   169,   170,
   171,   135,   142,   133,   134,     0,     0,     0,    97,     0,
   126,   127,   124,   108,   109,   110,   113,   115,   111,   128,
   129,   116,   117,   460,   121,   120,   107,   125,   123,   122,
   118,   119,   114,   112,   105,   106,   130,     0,   459,   312,
    98,    99,   160,   153,   163,   148,   131,   132,    95,    96,
     0,     0,   102,   101,   100,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,   489,   488,     0,     0,
     0,   490,     0,     0,     0,     0,     0,     0,   333,   334,
     0,     0,     0,     0,     0,   229,    45,     0,     0,     0,
   465,   237,     0,    46,    44,     0,    59,     0,     0,   348,
    58,    38,     0,     9,   484,     0,     0,     0,   192,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,   217,     0,     0,   462,     0,     0,     0,     0,
     0,     0,    68,     0,   208,    39,   207,   404,   403,   405,
     0,   401,   402,     0,     0,     0,     0,     0,     0,     0,
   374,   352,   350,   290,     4,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,   339,
   341,     0,     0,     0,     0,     0,     0,    72,     0,     0,
     0,     0,     0,     0,   344,     0,   288,     0,   409,   410,
     0,    90,     0,    92,     0,   427,   305,   426,     0,     0,
     0,     0,     0,   479,   480,   314,     0,   103,     0,     0,
   274,     0,   324,   323,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,   491,     0,     0,
     0,     0,     0,     0,   302,     0,   257,     0,     0,   230,
   259,     0,   232,     0,   285,     0,     0,   252,   251,     0,
     0,     0,     0,     0,    11,    13,    12,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,   277,     0,
     0,     0,   218,   281,     0,   486,   219,     0,   221,     0,
   464,   463,   282,     0,     0,     0,     0,   392,   395,   393,
   406,   391,   375,   389,   376,   377,   378,   379,   382,     0,
   384,     0,   385,     0,     0,     0,    15,    16,    17,    18,
    19,    36,    37,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,   474,     0,     0,   475,     0,     0,     0,     0,   347,
     0,     0,   472,   473,     0,     0,    30,     0,     0,    23,
     0,    31,   260,     0,     0,    66,    73,    24,    33,     0,
    25,     0,    50,    53,     0,   429,     0,     0,     0,     0,
     0,     0,    94,     0,     0,     0,     0,     0,   442,   441,
   443,     0,   451,   450,   455,   454,     0,     0,   448,     0,
     0,   439,   445,     0,     0,     0,     0,   363,     0,     0,
   364,     0,     0,   331,     0,   325,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,   300,   328,
   327,   294,   326,   297,     0,     0,     0,     0,     0,     0,
     0,   236,   467,     0,     0,     0,   258,     0,     0,   466,
   468,   284,     0,     0,   255,     0,     0,   249,     0,     0,
     0,     0,     0,   223,     0,    10,     0,     0,    22,     0,
     0,     0,     0,     0,   222,     0,   261,     0,     0,     0,
     0,     0,     0,     0,   381,   383,   387,   337,     0,     0,
   335,     0,     0,     0,     0,     0,   228,     0,   345,   227,
     0,     0,   346,     0,     0,    48,   342,    49,   343,   264,
     0,     0,    71,     0,   308,     0,     0,   280,   311,     0,
   315,     0,     0,     0,   431,     0,   435,     0,   437,     0,
   438,   452,   456,   104,     0,     0,   366,   332,     0,     3,
   368,     0,   329,     0,     0,     0,     0,     0,     0,   299,
   301,   357,     0,     0,     0,     0,     0,     0,     0,     0,
   234,     0,     0,     0,     0,     0,   242,   254,   224,     0,
     0,   225,     0,     0,   287,    21,   276,     0,     0,     0,
   397,   398,   399,   394,   400,   336,     0,     0,     0,     0,
     0,    27,     0,    28,     0,    55,    29,     0,     0,    57,
     0,     0,     0,     0,     0,     0,   428,   306,   461,     0,
   447,     0,   313,     0,   457,   446,     0,     0,   449,     0,
     0,     0,     0,   365,     0,     0,   367,     0,   291,     0,
   292,     0,     0,     0,     0,   303,   231,     0,   233,   248,
   256,     0,     0,     0,   239,     0,     0,   220,   396,   338,
   353,   351,   340,    26,     0,   263,     0,     0,     0,   430,
     0,   433,   434,   436,     0,     0,     0,     0,     0,     0,
   356,   358,   354,   359,   295,   298,     0,     0,     0,     0,
   238,     0,   244,     0,   226,    51,   309,     0,     0,     0,
     0,     0,     0,     0,   360,     0,     0,   235,   240,     0,
     0,     0,   243,   316,   432,     0,   330,   304,     0,     0,
   245,     0,   241,     0,   246,     0,   247,
    }, yyDgoto = {
//yyDgoto 136
     1,   187,    60,    61,    62,    63,    64,   293,   290,   461,
    65,    66,    67,   469,    68,    69,    70,   108,    71,   205,
   206,    73,    74,    75,    76,    77,    78,   209,   259,   712,
   842,   713,   705,   236,   624,   418,   709,   666,   366,   246,
    80,   668,    81,    82,   566,   567,   568,   201,   753,   211,
   531,    84,    85,   237,   396,   579,   271,   227,   659,   212,
    87,   299,   297,   569,   570,   273,   597,    88,   274,   240,
   278,   409,   616,   410,   696,   784,   356,   340,   543,    89,
    90,   267,   379,   213,   214,   202,   291,    93,   113,   548,
   519,   114,   204,   514,   572,   573,   375,   574,   575,     2,
   219,   220,   427,   256,   683,   191,   576,   255,   446,   247,
   628,   733,   440,   384,   222,   601,   724,   223,   725,   609,
   846,   547,   385,   544,   775,   371,   376,   556,   779,   509,
   474,   473,   653,   652,   546,   372,
    }, yySindex = {
//yySindex 887
     0,     0,  4220, 13541,  3260, 16646, 17211, 17101,  4220,  5703,
  5703,  4617,     0,     0, 16416, 13771, 13771,     0,     0, 13771,
  -172,  -139,     0,     0,     0,     0,  5703, 16991,   143,     0,
  -156,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0, 16186, 16186,  -189,   -90,  5178,  5703, 15151,
 16186, 16761, 16186, 16301, 17320,     0,     0,     0,   195,   236,
     0,  -123,     0,     0,     0,  -177,     0,     0,     0,     0,
     0,     0,     0,    96,   643,   212,  2685,     0,    -3,   126,
     0,  -176,     0,   -58,   244,     0,   251,     0, 16531,   256,
     0,   -55,     0,     0,  -190,   643,     0,     0,     0,  -172,
  -139,   143,     0,     0,   -54,  5703,  -120,  4220,    36,     0,
   198,     0,     0,  -190,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,  -121,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
 17320,   261,     0,     0,     0,    58,    76,    50,   212,    92,
   207,   -17,   318,   104,     0,    92,     0,     0,    96,   -86,
   405,     0,  5703,  5703,   211,   240,     0,   250,     0,     0,
     0, 16186, 16186, 16186,  2685,     0,     0,   206,   505,   507,
     0,     0,   498,     0,     0,  3774,     0, 13886, 13771,     0,
     0,     0,   -40,     0,     0, 15266,   209,  4220,     0,   368,
   263,   272,   278,   270,  5178,   268,     0,   274,   212, 16186,
   143,   258,     0,   131,   146,     0,   171,   146,   249,   313,
   370,     0,     0,     0,     0,     0,     0,     0,     0,     0,
  -180,     0,     0,  -174,  -152,  -143,   260,   -45,   273,  -196,
     0,     0,     0,     0,     0, 13426,  5703,  5703,  5703,  5703,
 13541,  5703,  5703, 16186, 16186, 16186, 16186, 16186, 16186, 16186,
 16186, 16186, 16186, 16186, 16186, 16186, 16186, 16186, 16186, 16186,
 16186, 16186, 16186, 16186, 16186, 16186, 16186, 16186, 16186,     0,
     0,  2233,  3660, 15151,  5771,  5771, 16301,     0, 15381,  5178,
 16761,   581, 15381, 16301,     0,   296,     0,   299,     0,     0,
   212,     0,     0,     0,    96,     0,     0,     0,  5771,  6688,
 15151,  4220,  5703,     0,     0,     0,   767,     0, 15496,   377,
     0,   270,     0,     0,  4220,   393, 17488, 17547, 15151, 16186,
 16186, 16186,  4220,   399,  4220, 15611,   410,     0,    71,    71,
     0, 17606, 17665, 15151,     0,   633,     0, 16186, 14001,     0,
     0, 14116,     0, 16186,     0,   339, 13656,     0,     0,    -3,
   143,   109,   341,   644,     0,     0,     0, 17101,  5703,  2685,
  4220,   326, 17488, 17547, 16186, 16186, 16186,   349,     0,     0,
   143,  2166,     0,     0, 15726,     0,     0, 16186,     0, 16186,
     0,     0,     0,     0, 17724, 17783, 15151,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,    30,
     0,   661,     0,  -184,  -184,   643,     0,     0,     0,     0,
     0,     0,     0,   263,  2810,  2810,  2810,  2810,  3283,  3283,
  4094,  8822,  2810,  2810,  4679,  4679,   -41,   -41,   263,  1501,
   263,   263,   265,   265,  3283,  3283,  1209,  1209,  5029,  -184,
   355,     0,   357,  -139,     0,   358,     0,   360,  -139,     0,
     0,   364,     0,     0,  -139,  -139,     0,  2685, 16186,     0,
  2320,     0,     0,   660,   369,     0,     0,     0,     0,     0,
     0,  2685,     0,     0,    96,     0,  5703,  4220,  -139,     0,
     0,  -139,     0,   374,   451,    73, 17429,   663,     0,     0,
     0,  1116,     0,     0,     0,     0,  4220,    96,     0,   686,
   687,     0,     0,   690,   421,   432, 17101,     0,     0,   395,
     0,  4220,   476,     0,    27,     0,   402,   406,   407,   360,
   398,  2320,   377,   484,   487, 16186,   708,    92,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,   413,  5703,
   419,     0,     0, 16186,   206,   709,     0, 16186,   206,     0,
     0,     0, 16186,  2685,     0,    -6,   720,     0,   433,   434,
  5771,  5771,   440,     0, 14231,     0,  -146,   420,     0,   263,
   263,  2685,     0,   446,     0, 16186,     0,     0,     0,     0,
     0,   444,  4220,   823,     0,     0,     0,     0,  2814,  4220,
     0,  4220, 16186,  4220, 16301, 16301,     0,   296,     0,     0,
 16301, 16186,     0,   296,   453,     0,     0,     0,     0,     0,
 16186, 15841,     0,  -184,     0,    96,   530,     0,     0,   455,
     0, 16186,   143,   532,     0,  1116,     0,   -76,     0,   -52,
     0,     0,     0,     0, 16876,    92,     0,     0,  4220,     0,
     0,  5703,     0,   538, 16186, 16186, 16186,   465,   542,     0,
     0,     0, 15956,  4220,  4220,  4220,     0,    71,   633, 14346,
     0,   633,   633,   466, 14461, 14576,     0,     0,     0,  -139,
  -139,     0,    -3,   109,     0,     0,     0,  2166,     0,   447,
     0,     0,     0,     0,     0,     0,   450,   554,   456,  2685,
   555,     0,  2685,     0,  2685,     0,     0,  2685,  2685,     0,
 16301,  2685, 16186,     0,  4220,  4220,     0,     0,     0,   767,
     0,   478,     0,   781,     0,     0,   690,   663,     0,   690,
   519,   371,     0,     0,     0,  4220,     0,    92,     0, 16186,
     0, 16186,   -37,   565,   566,     0,     0, 16186,     0,     0,
     0, 16186,   787,   790,     0, 16186,   495,     0,     0,     0,
     0,     0,     0,     0,  2685,     0,   475,   578,  4220,     0,
   -76,     0,     0,     0,     0, 17842, 17901, 15151,    58,  4220,
     0,     0,     0,     0,     0,     0,  4220,  4726,   633, 14691,
     0, 14806,     0,   633,     0,     0,     0,   582,   690,     0,
     0,     0,     0,   499,     0,    27,   586,     0,     0, 16186,
   804, 16186,     0,     0,     0,     0,     0,     0,   633, 14921,
     0,   633,     0, 16186,     0,   633,     0,
    }, yyRindex = {
//yyRindex 887
     0,     0,   149,     0,     0,     0,     0,     0,   626,     0,
     0,    66,     0,     0,     0,  7630,  7733,     0,     0,  7872,
  4491,  3896,     0,     0,     0,     0,     0,     0, 16071,     0,
     0,     0,     0,  1974,  3051,     0,     0,  2091,     0,     0,
     0,     0,     0,     0,     0,     0,     0,    48,     0,   512,
   489,   269,     0,     0,   281,     0,     0,     0,   293,  -195,
     0,  6926,     0,     0,     0,  7029,     0,     0,     0,     0,
     0,     0,     0,   189,   592,  1007,  1026,  7168, 12309,     0,
     0, 12360,     0,  8897,     0,     0,     0,     0,   288,     0,
     0,     0,  8759,     0, 15036,   630,     0,     0,     0,  7277,
  5891,   520, 12492, 12610,     0,     0,     0,    48,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,  1476,
  1558,  2640,  2651,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,  3120,  3131,  3600,     0,  3611,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,  2213,     0,     0,     0,    69,     0,     0,  1746,     0,
     0,  6405,     0,     0,  5994,     0,     0,     0,   598,     0,
   223,     0,     0,     0,     0,     0,   627,     0,     0,     0,
  1194,     0,     0,     0, 11282,     0,     0, 12031,  6345,  6345,
     0,     0,  6508,     0,     0,     0,     0,     0,   527,     0,
     0,     0,     0,     0,     0,     0,     0,    13,     0,     0,
  7981,  7382,  7519,  9061,    48,     0,     7,     0,    17,     0,
   529,     0,     0,   533,   533,     0,   509,   509,     0,     0,
     0,  1357,     0,  1595,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,   252,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,   512,     0,     0,     0,     0,     0,    48,
   292,   366,     0,     0,     0,  9007,     0,     0,     0,     0,
   105,     0, 12960,     0,     0,     0,     0,     0,     0,     0,
   512,   626,     0,     0,     0,     0,   123,     0,   177,   262,
     0,  6619,     0,     0,   544, 13076,     0,     0,   512,     0,
     0,     0,    98,     0,    81,     0,     0,     0,     0,     0,
  1281,     0,     0,   512,     0,  6345,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,   539,     0,     0,    33,
   540,   540,     0,    70,     0,     0,     0,     0,     0, 11319,
    13,     0,     0,     0,     0,     0,     0,     0,     0,    57,
   540,   529,     0,     0,   545,     0,     0,  -198,     0,   541,
     0,     0,     0,  1652,     0,     0,   512,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0, 13192, 13310,   681,     0,     0,     0,     0,
     0,     0,     0,  8086, 10505, 10599, 10746, 10840, 10063, 10166,
 10927, 11192, 11016, 11105,  1835,  6242,  9477,  9582,  8210,  9719,
  8315,  8425,  9248,  9368, 10265, 10407,  9822,  9926,     0, 13192,
  4854,     0,  4969,  4011,     0,  5334,  3416,  5449, 15036,     0,
  3531,     0,     0,     0,  5576,  5576,     0, 11406,     0,     0,
   910,     0,     0,     0,     0,     0,     0,     0,     0, 12396,
     0, 11447,     0,     0,     0,     0,     0,   626,  6098, 12726,
 12842,     0,     0,     0,     0,   540,     0,    55,     0,     0,
     0,    75,     0,     0,     0,     0,   626,     0,     0,    67,
    67,     0,     0,    67,    77,     0,     0,     0,   167,   155,
     0,    61,   612,     0,   612,     0,  2456,  2571,  2936,  4376,
     0, 12140,   612,     0,     0,     0,   243,     0,     0,     0,
     0,     0,     0,     0,   628,  1084,  1173,  1355,     0,     0,
     0,     0,     0,     0, 12175,  6345,     0,     0,     0,     0,
     0,     0,     0,    53,     0,     0,   553,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,  8544,
  8654, 11510,    -4,     0,     0,     0,     0,  1584,  1778,  4065,
   885,     0,    13,     0,     0,     0,     0,     0,     0,    81,
     0,    13,     0,    81,     0,     0,     0, 10686,     0,     0,
     0,     0,     0, 12273,  9139,     0,     0,     0,     0,     0,
     0,     0,     0, 13310,     0,     0,     0,     0,     0,     0,
     0,     0,   540,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,    81,     0,
     0,     0,     0,     0,     0,     0,     0,  6815,     0,     0,
     0,     0,     0,   267,    81,    81,  1294,     0,  6345,     0,
     0,  6345,   553,     0,     0,     0,     0,     0,     0,   112,
   112,     0,     0,   540,     0,     0,     0,   529,  1857,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0, 11597,
     0,     0, 11634,     0, 11724,     0,     0, 11765, 11863,     0,
     0, 11900,     0,  1709,    13,   626,     0,     0,     0,   123,
     0,     0,     0,    67,     0,     0,    67,     0,     0,    67,
     0,     0,    51,     0,   181,   626,     0,     0,     0,     0,
     0,     0,   612,     0,     0,     0,     0,     0,     0,     0,
     0,     0,   553,   553,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0, 11994,     0,     0,     0,   626,     0,
     0,     0,     0,     0,   548,     0,     0,   512,    69,   544,
     0,     0,     0,     0,     0,     0,    81,  6345,   553,     0,
     0,     0,     0,   553,     0,     0,     0,     0,    67,   584,
  1174,  1321,   232,     0,     0,   612,     0,     0,     0,     0,
   553,     0,     0,     0,     0,   604,     0,     0,   553,     0,
     0,   553,     0,     0,     0,   553,     0,
    }, yyGindex = {
//yyGindex 136
     0,     0,     0,     0,   836,     0,     0,     0,   524,  -162,
     0,     0,     0,     0,     0,     0,     0,   898,   -42,   319,
  -335,     0,    41,  1186,   152,    25,    35,    64,   752,  -369,
     0,    39,     0,   431,     0,     0,     0,    34,     0,   -12,
   903,  -231,  -222,     0,   132,   351,  -624,     0,     0,   777,
  -215,   825,   -31,  1347,  -378,     0,  -310,   259,  -386,  1078,
  1197,     0,     0,     0,   221,     9,     0,     0,     3,  -355,
     0,  1034,    16,     0,   144,  -342,   853,     0,  -426,    -9,
     5,  -175,    90,   531,    -2,   -19,     0,    14,  1003,  -275,
     0,    56,    10,   231,   224,  -597,     0,     0,     0,     0,
     6,   858,     0,     0,     0,     0,     0,    -5,   350,     0,
     0,     0,     0,  -208,     0,  -362,     0,     0,     0,     0,
     0,     0,    31,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,
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
    "tSTRING_DVAR","tSTRING_END","tLOWEST","tLAST_TOKEN",
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
					// line 259 "DefaultRubyParser.y"
  {
                  lexer.setState(LexState.EXPR_BEG);
                  support.initTopLocalVariables();
              }
  break;
case 2:
					// line 262 "DefaultRubyParser.y"
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
					// line 274 "DefaultRubyParser.y"
  {
                  Node node = ((Node)yyVals[-3+yyTop]);

		  if (((RescueBodyNode)yyVals[-2+yyTop]) != null) {
		      node = new RescueNode(getPosition(((Node)yyVals[-3+yyTop]), true), ((Node)yyVals[-3+yyTop]), ((RescueBodyNode)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]));
		  } else if (((Node)yyVals[-1+yyTop]) != null) {
		      warnings.warn(getPosition(((Node)yyVals[-3+yyTop])), "else without rescue is useless");
                      node = support.appendToBlock(((Node)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]));
		  }
		  if (((Node)yyVals[0+yyTop]) != null) {
		      node = new EnsureNode(getPosition(((Node)yyVals[-3+yyTop])), node, ((Node)yyVals[0+yyTop]));
		  }

	          yyVal = node;
              }
  break;
case 4:
					// line 290 "DefaultRubyParser.y"
  {
                  if (((Node)yyVals[-1+yyTop]) instanceof BlockNode) {
                      support.checkUselessStatements(((BlockNode)yyVals[-1+yyTop]));
		  }
                  yyVal = ((Node)yyVals[-1+yyTop]);
              }
  break;
case 6:
					// line 298 "DefaultRubyParser.y"
  {
                  yyVal = support.newline_node(((Node)yyVals[0+yyTop]), getPosition(((Node)yyVals[0+yyTop]), true));
              }
  break;
case 7:
					// line 301 "DefaultRubyParser.y"
  {
	          yyVal = support.appendToBlock(((Node)yyVals[-2+yyTop]), support.newline_node(((Node)yyVals[0+yyTop]), getPosition(((Node)yyVals[0+yyTop]), true)));
              }
  break;
case 8:
					// line 304 "DefaultRubyParser.y"
  {
                  yyVal = ((Node)yyVals[0+yyTop]);
              }
  break;
case 9:
					// line 308 "DefaultRubyParser.y"
  {
                  lexer.setState(LexState.EXPR_FNAME);
              }
  break;
case 10:
					// line 310 "DefaultRubyParser.y"
  {
                  yyVal = new AliasNode(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])), (String) ((Token)yyVals[-2+yyTop]).getValue(), (String) ((Token)yyVals[0+yyTop]).getValue());
              }
  break;
case 11:
					// line 313 "DefaultRubyParser.y"
  {
                  yyVal = new VAliasNode(getPosition(((Token)yyVals[-2+yyTop])), (String) ((Token)yyVals[-1+yyTop]).getValue(), (String) ((Token)yyVals[0+yyTop]).getValue());
              }
  break;
case 12:
					// line 316 "DefaultRubyParser.y"
  {
                  yyVal = new VAliasNode(getPosition(((Token)yyVals[-2+yyTop])), (String) ((Token)yyVals[-1+yyTop]).getValue(), "$" + ((BackRefNode)yyVals[0+yyTop]).getType()); /* XXX*/
              }
  break;
case 13:
					// line 319 "DefaultRubyParser.y"
  {
                  yyerror("can't make alias for the number variables");
              }
  break;
case 14:
					// line 322 "DefaultRubyParser.y"
  {
                  yyVal = ((Node)yyVals[0+yyTop]);
              }
  break;
case 15:
					// line 325 "DefaultRubyParser.y"
  {
                  yyVal = new IfNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), null);
              }
  break;
case 16:
					// line 328 "DefaultRubyParser.y"
  {
                  yyVal = new IfNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), null, ((Node)yyVals[-2+yyTop]));
              }
  break;
case 17:
					// line 331 "DefaultRubyParser.y"
  {
                  if (((Node)yyVals[-2+yyTop]) != null && ((Node)yyVals[-2+yyTop]) instanceof BeginNode) {
                      yyVal = new WhileNode(getPosition(((Node)yyVals[-2+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), ((BeginNode)yyVals[-2+yyTop]).getBodyNode(), false);
                  } else {
                      yyVal = new WhileNode(getPosition(((Node)yyVals[-2+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), true);
                  }
              }
  break;
case 18:
					// line 338 "DefaultRubyParser.y"
  {
                  if (((Node)yyVals[-2+yyTop]) != null && ((Node)yyVals[-2+yyTop]) instanceof BeginNode) {
                      yyVal = new UntilNode(getPosition(((Node)yyVals[-2+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), ((BeginNode)yyVals[-2+yyTop]).getBodyNode());
                  } else {
                      yyVal = new UntilNode(getPosition(((Node)yyVals[-2+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]));
                  }
              }
  break;
case 19:
					// line 345 "DefaultRubyParser.y"
  {
	          yyVal = new RescueNode(getPosition(((Node)yyVals[-2+yyTop])), ((Node)yyVals[-2+yyTop]), new RescueBodyNode(getPosition(((Node)yyVals[-2+yyTop])), null,((Node)yyVals[0+yyTop]), null), null);
              }
  break;
case 20:
					// line 348 "DefaultRubyParser.y"
  {
                  if (support.isInDef() || support.isInSingle()) {
                      yyerror("BEGIN in method");
                  }
		  support.pushLocalScope();
              }
  break;
case 21:
					// line 353 "DefaultRubyParser.y"
  {
                  support.getResult().addBeginNode(support.getCurrentScope(), ((Node)yyVals[-1+yyTop]));
                  support.popCurrentScope();
                  yyVal = null; /*XXX 0;*/
              }
  break;
case 22:
					// line 358 "DefaultRubyParser.y"
  {
                  if (support.isInDef() || support.isInSingle()) {
                      yyerror("END in method; use at_exit");
                  }

                  yyVal = new PostExeNode(getPosition(((Token)yyVals[-3+yyTop])), ((Node)yyVals[-1+yyTop]));
              }
  break;
case 23:
					// line 365 "DefaultRubyParser.y"
  {
                  support.checkExpression(((Node)yyVals[0+yyTop]));
                  yyVal = support.node_assign(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
              }
  break;
case 24:
					// line 369 "DefaultRubyParser.y"
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
case 25:
					// line 378 "DefaultRubyParser.y"
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
case 26:
					// line 395 "DefaultRubyParser.y"
  {
                  yyVal = new OpElementAsgnNode(getPosition(((Node)yyVals[-5+yyTop])), ((Node)yyVals[-5+yyTop]), (String) ((Token)yyVals[-1+yyTop]).getValue(), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop]));

              }
  break;
case 27:
					// line 399 "DefaultRubyParser.y"
  {
                  yyVal = new OpAsgnNode(getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), ((Node)yyVals[0+yyTop]), (String) ((Token)yyVals[-2+yyTop]).getValue(), (String) ((Token)yyVals[-1+yyTop]).getValue());
              }
  break;
case 28:
					// line 402 "DefaultRubyParser.y"
  {
                  yyVal = new OpAsgnNode(getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), ((Node)yyVals[0+yyTop]), (String) ((Token)yyVals[-2+yyTop]).getValue(), (String) ((Token)yyVals[-1+yyTop]).getValue());
              }
  break;
case 29:
					// line 405 "DefaultRubyParser.y"
  {
                  yyVal = new OpAsgnNode(getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), ((Node)yyVals[0+yyTop]), (String) ((Token)yyVals[-2+yyTop]).getValue(), (String) ((Token)yyVals[-1+yyTop]).getValue());
              }
  break;
case 30:
					// line 408 "DefaultRubyParser.y"
  {
                  support.backrefAssignError(((Node)yyVals[-2+yyTop]));
              }
  break;
case 31:
					// line 411 "DefaultRubyParser.y"
  {
                  yyVal = support.node_assign(((Node)yyVals[-2+yyTop]), new SValueNode(getPosition(((Node)yyVals[-2+yyTop])), ((Node)yyVals[0+yyTop])));
              }
  break;
case 32:
					// line 414 "DefaultRubyParser.y"
  {
                  if (((MultipleAsgnNode)yyVals[-2+yyTop]).getHeadNode() != null) {
		      ((MultipleAsgnNode)yyVals[-2+yyTop]).setValueNode(new ToAryNode(getPosition(((MultipleAsgnNode)yyVals[-2+yyTop])), ((Node)yyVals[0+yyTop])));
		  } else {
		      ((MultipleAsgnNode)yyVals[-2+yyTop]).setValueNode(new ArrayNode(getPosition(((MultipleAsgnNode)yyVals[-2+yyTop])), ((Node)yyVals[0+yyTop])));
		  }
		  yyVal = ((MultipleAsgnNode)yyVals[-2+yyTop]);
	      }
  break;
case 33:
					// line 422 "DefaultRubyParser.y"
  {
                  ((AssignableNode)yyVals[-2+yyTop]).setValueNode(((Node)yyVals[0+yyTop]));
		  yyVal = ((MultipleAsgnNode)yyVals[-2+yyTop]);
                  ((MultipleAsgnNode)yyVals[-2+yyTop]).setPosition(support.union(((MultipleAsgnNode)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])));
	      }
  break;
case 36:
					// line 430 "DefaultRubyParser.y"
  {
                  yyVal = support.newAndNode(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
              }
  break;
case 37:
					// line 433 "DefaultRubyParser.y"
  {
                  yyVal = support.newOrNode(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
              }
  break;
case 38:
					// line 436 "DefaultRubyParser.y"
  {
                  yyVal = new NotNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])));
              }
  break;
case 39:
					// line 439 "DefaultRubyParser.y"
  {
                  yyVal = new NotNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])));
              }
  break;
case 41:
					// line 444 "DefaultRubyParser.y"
  {
                  support.checkExpression(((Node)yyVals[0+yyTop]));
	      }
  break;
case 44:
					// line 450 "DefaultRubyParser.y"
  {
                  yyVal = new ReturnNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), support.ret_args(((Node)yyVals[0+yyTop]), getPosition(((Token)yyVals[-1+yyTop]))));
              }
  break;
case 45:
					// line 453 "DefaultRubyParser.y"
  {
                  yyVal = new BreakNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), support.ret_args(((Node)yyVals[0+yyTop]), getPosition(((Token)yyVals[-1+yyTop]))));
              }
  break;
case 46:
					// line 456 "DefaultRubyParser.y"
  {
                  yyVal = new NextNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), support.ret_args(((Node)yyVals[0+yyTop]), getPosition(((Token)yyVals[-1+yyTop]))));
              }
  break;
case 48:
					// line 461 "DefaultRubyParser.y"
  {
                  yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
              }
  break;
case 49:
					// line 464 "DefaultRubyParser.y"
  {
                  yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
              }
  break;
case 50:
					// line 468 "DefaultRubyParser.y"
  {
                    support.pushBlockScope();
		}
  break;
case 51:
					// line 470 "DefaultRubyParser.y"
  {
                    yyVal = new IterNode(getPosition(((Token)yyVals[-4+yyTop])), ((Node)yyVals[-2+yyTop]), support.getCurrentScope(), ((Node)yyVals[-1+yyTop]));
                    support.popCurrentScope();
		}
  break;
case 52:
					// line 475 "DefaultRubyParser.y"
  {
                  yyVal = support.new_fcall(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
              }
  break;
case 53:
					// line 478 "DefaultRubyParser.y"
  {
                  yyVal = support.new_fcall(((Token)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop])); 
              }
  break;
case 54:
					// line 481 "DefaultRubyParser.y"
  {
                  yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
              }
  break;
case 55:
					// line 484 "DefaultRubyParser.y"
  {
                  yyVal = support.new_call(((Node)yyVals[-4+yyTop]), ((Token)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop])); 
	      }
  break;
case 56:
					// line 487 "DefaultRubyParser.y"
  {
                  yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
              }
  break;
case 57:
					// line 490 "DefaultRubyParser.y"
  {
                  yyVal = support.new_call(((Node)yyVals[-4+yyTop]), ((Token)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop])); 
	      }
  break;
case 58:
					// line 493 "DefaultRubyParser.y"
  {
		  yyVal = support.new_super(((Node)yyVals[0+yyTop]), ((Token)yyVals[-1+yyTop])); /* .setPosFrom($2);*/
	      }
  break;
case 59:
					// line 496 "DefaultRubyParser.y"
  {
                  yyVal = support.new_yield(getPosition(((Token)yyVals[-1+yyTop])), ((Node)yyVals[0+yyTop]));
	      }
  break;
case 61:
					// line 501 "DefaultRubyParser.y"
  {
                  yyVal = ((MultipleAsgnNode)yyVals[-1+yyTop]);
	      }
  break;
case 63:
					// line 506 "DefaultRubyParser.y"
  {
                  yyVal = new MultipleAsgnNode(getPosition(((Token)yyVals[-2+yyTop])), new ArrayNode(getPosition(((Token)yyVals[-2+yyTop])), ((MultipleAsgnNode)yyVals[-1+yyTop])), null);
              }
  break;
case 64:
					// line 510 "DefaultRubyParser.y"
  {
                  yyVal = new MultipleAsgnNode(getPosition(((ListNode)yyVals[0+yyTop])), ((ListNode)yyVals[0+yyTop]), null);
              }
  break;
case 65:
					// line 513 "DefaultRubyParser.y"
  {
/*mirko: check*/
                  yyVal = new MultipleAsgnNode(support.union(((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), ((ListNode)yyVals[-1+yyTop]).add(((Node)yyVals[0+yyTop])), null);
                  ((Node)yyVals[-1+yyTop]).setPosition(support.union(((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])));
              }
  break;
case 66:
					// line 518 "DefaultRubyParser.y"
  {
                  yyVal = new MultipleAsgnNode(getPosition(((ListNode)yyVals[-2+yyTop])), ((ListNode)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
              }
  break;
case 67:
					// line 521 "DefaultRubyParser.y"
  {
                  yyVal = new MultipleAsgnNode(getPosition(((ListNode)yyVals[-1+yyTop])), ((ListNode)yyVals[-1+yyTop]), new StarNode(getPosition(null)));
              }
  break;
case 68:
					// line 524 "DefaultRubyParser.y"
  {
                  yyVal = new MultipleAsgnNode(getPosition(((Token)yyVals[-1+yyTop])), null, ((Node)yyVals[0+yyTop]));
              }
  break;
case 69:
					// line 527 "DefaultRubyParser.y"
  {
                  yyVal = new MultipleAsgnNode(getPosition(((Token)yyVals[0+yyTop])), null, new StarNode(getPosition(null)));
              }
  break;
case 71:
					// line 532 "DefaultRubyParser.y"
  {
                  yyVal = ((MultipleAsgnNode)yyVals[-1+yyTop]);
              }
  break;
case 72:
					// line 536 "DefaultRubyParser.y"
  {
                  yyVal = new ArrayNode(((Node)yyVals[-1+yyTop]).getPosition(), ((Node)yyVals[-1+yyTop]));
              }
  break;
case 73:
					// line 539 "DefaultRubyParser.y"
  {
                  yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[-1+yyTop]));
              }
  break;
case 74:
					// line 543 "DefaultRubyParser.y"
  {
                  yyVal = support.assignable(((Token)yyVals[0+yyTop]), null);
              }
  break;
case 75:
					// line 546 "DefaultRubyParser.y"
  {
                  yyVal = support.aryset(((Node)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]));
              }
  break;
case 76:
					// line 549 "DefaultRubyParser.y"
  {
                  yyVal = support.attrset(((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
              }
  break;
case 77:
					// line 552 "DefaultRubyParser.y"
  {
                  yyVal = support.attrset(((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
              }
  break;
case 78:
					// line 555 "DefaultRubyParser.y"
  {
                  yyVal = support.attrset(((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
              }
  break;
case 79:
					// line 558 "DefaultRubyParser.y"
  {
                  if (support.isInDef() || support.isInSingle()) {
		      yyerror("dynamic constant assignment");
		  }

		  ISourcePosition position = support.union(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]));

                  yyVal = new ConstDeclNode(position, null, new Colon2Node(position, ((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue()), null);
	      }
  break;
case 80:
					// line 567 "DefaultRubyParser.y"
  {
                  if (support.isInDef() || support.isInSingle()) {
		      yyerror("dynamic constant assignment");
		  }

                  ISourcePosition position = support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop]));

                  yyVal = new ConstDeclNode(position, null, new Colon3Node(position, (String) ((Token)yyVals[0+yyTop]).getValue()), null);
	      }
  break;
case 81:
					// line 576 "DefaultRubyParser.y"
  {
	          support.backrefAssignError(((Node)yyVals[0+yyTop]));
              }
  break;
case 82:
					// line 580 "DefaultRubyParser.y"
  {
                  yyVal = support.assignable(((Token)yyVals[0+yyTop]), null);
              }
  break;
case 83:
					// line 583 "DefaultRubyParser.y"
  {
                  yyVal = support.aryset(((Node)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]));
              }
  break;
case 84:
					// line 586 "DefaultRubyParser.y"
  {
                  yyVal = support.attrset(((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
              }
  break;
case 85:
					// line 589 "DefaultRubyParser.y"
  {
                  yyVal = support.attrset(((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
 	      }
  break;
case 86:
					// line 592 "DefaultRubyParser.y"
  {
                  yyVal = support.attrset(((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
              }
  break;
case 87:
					// line 595 "DefaultRubyParser.y"
  {
                  if (support.isInDef() || support.isInSingle()) {
		      yyerror("dynamic constant assignment");
		  }
			
		  ISourcePosition position = support.union(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]));

                  yyVal = new ConstDeclNode(position, null, new Colon2Node(position, ((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue()), null);
              }
  break;
case 88:
					// line 604 "DefaultRubyParser.y"
  {
                  if (support.isInDef() || support.isInSingle()) {
		      yyerror("dynamic constant assignment");
		  }

                  ISourcePosition position = support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop]));

                  yyVal = new ConstDeclNode(position, null, new Colon3Node(position, (String) ((Token)yyVals[0+yyTop]).getValue()), null);
	      }
  break;
case 89:
					// line 613 "DefaultRubyParser.y"
  {
                   support.backrefAssignError(((Node)yyVals[0+yyTop]));
	      }
  break;
case 90:
					// line 617 "DefaultRubyParser.y"
  {
                  yyerror("class/module name must be CONSTANT");
              }
  break;
case 92:
					// line 622 "DefaultRubyParser.y"
  {
                  yyVal = new Colon3Node(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), (String) ((Token)yyVals[0+yyTop]).getValue());
	      }
  break;
case 93:
					// line 625 "DefaultRubyParser.y"
  {
                  yyVal = new Colon2Node(((Token)yyVals[0+yyTop]).getPosition(), null, (String) ((Token)yyVals[0+yyTop]).getValue());
 	      }
  break;
case 94:
					// line 628 "DefaultRubyParser.y"
  {
                  yyVal = new Colon2Node(support.union(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
	      }
  break;
case 98:
					// line 634 "DefaultRubyParser.y"
  {
                  lexer.setState(LexState.EXPR_END);
                  yyVal = ((Token)yyVals[0+yyTop]);
              }
  break;
case 99:
					// line 639 "DefaultRubyParser.y"
  {
                  lexer.setState(LexState.EXPR_END);
                  yyVal = yyVals[0+yyTop];
              }
  break;
case 102:
					// line 646 "DefaultRubyParser.y"
  {
                  yyVal = new UndefNode(getPosition(((Token)yyVals[0+yyTop])), (String) ((Token)yyVals[0+yyTop]).getValue());
              }
  break;
case 103:
					// line 649 "DefaultRubyParser.y"
  {
                  lexer.setState(LexState.EXPR_FNAME);
	      }
  break;
case 104:
					// line 651 "DefaultRubyParser.y"
  {
                  yyVal = support.appendToBlock(((Node)yyVals[-3+yyTop]), new UndefNode(getPosition(((Node)yyVals[-3+yyTop])), (String) ((Token)yyVals[0+yyTop]).getValue()));
              }
  break;
case 172:
					// line 670 "DefaultRubyParser.y"
  {
                  yyVal = support.node_assign(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
		  /* FIXME: Consider fixing node_assign itself rather than single case*/
		  ((Node)yyVal).setPosition(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])));
              }
  break;
case 173:
					// line 675 "DefaultRubyParser.y"
  {
                  ISourcePosition position = support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
                  yyVal = support.node_assign(((Node)yyVals[-4+yyTop]), new RescueNode(position, ((Node)yyVals[-2+yyTop]), new RescueBodyNode(position, null, ((Node)yyVals[0+yyTop]), null), null));
	      }
  break;
case 174:
					// line 679 "DefaultRubyParser.y"
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
case 175:
					// line 696 "DefaultRubyParser.y"
  {
                  yyVal = new OpElementAsgnNode(getPosition(((Node)yyVals[-5+yyTop])), ((Node)yyVals[-5+yyTop]), (String) ((Token)yyVals[-1+yyTop]).getValue(), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop]));
              }
  break;
case 176:
					// line 699 "DefaultRubyParser.y"
  {
                  yyVal = new OpAsgnNode(getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), ((Node)yyVals[0+yyTop]), (String) ((Token)yyVals[-2+yyTop]).getValue(), (String) ((Token)yyVals[-1+yyTop]).getValue());
              }
  break;
case 177:
					// line 702 "DefaultRubyParser.y"
  {
                  yyVal = new OpAsgnNode(getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), ((Node)yyVals[0+yyTop]), (String) ((Token)yyVals[-2+yyTop]).getValue(), (String) ((Token)yyVals[-1+yyTop]).getValue());
              }
  break;
case 178:
					// line 705 "DefaultRubyParser.y"
  {
                  yyVal = new OpAsgnNode(getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop]), ((Node)yyVals[0+yyTop]), (String) ((Token)yyVals[-2+yyTop]).getValue(), (String) ((Token)yyVals[-1+yyTop]).getValue());
              }
  break;
case 179:
					// line 708 "DefaultRubyParser.y"
  {
	          yyerror("constant re-assignment");
	      }
  break;
case 180:
					// line 711 "DefaultRubyParser.y"
  {
		  yyerror("constant re-assignment");
	      }
  break;
case 181:
					// line 714 "DefaultRubyParser.y"
  {
                  support.backrefAssignError(((Node)yyVals[-2+yyTop]));
              }
  break;
case 182:
					// line 717 "DefaultRubyParser.y"
  {
		  support.checkExpression(((Node)yyVals[-2+yyTop]));
		  support.checkExpression(((Node)yyVals[0+yyTop]));
                  yyVal = new DotNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]), false);
              }
  break;
case 183:
					// line 722 "DefaultRubyParser.y"
  {
		  support.checkExpression(((Node)yyVals[-2+yyTop]));
		  support.checkExpression(((Node)yyVals[0+yyTop]));
                  yyVal = new DotNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]), true);
              }
  break;
case 184:
					// line 727 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "+", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 185:
					// line 730 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "-", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 186:
					// line 733 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "*", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 187:
					// line 736 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "/", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 188:
					// line 739 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "%", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 189:
					// line 742 "DefaultRubyParser.y"
  {
		  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "**", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 190:
					// line 745 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "**", ((Node)yyVals[0+yyTop]), getPosition(null)), "-@");
              }
  break;
case 191:
					// line 748 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(support.getOperatorCallNode(((FloatNode)yyVals[-2+yyTop]), "**", ((Node)yyVals[0+yyTop]), getPosition(null)), "-@");
              }
  break;
case 192:
					// line 751 "DefaultRubyParser.y"
  {
 	          if (((Node)yyVals[0+yyTop]) != null && ((Node)yyVals[0+yyTop]) instanceof ILiteralNode) {
		      yyVal = ((Node)yyVals[0+yyTop]);
		  } else {
                      yyVal = support.getOperatorCallNode(((Node)yyVals[0+yyTop]), "+@");
		  }
              }
  break;
case 193:
					// line 758 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[0+yyTop]), "-@");
	      }
  break;
case 194:
					// line 761 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "|", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 195:
					// line 764 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "^", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 196:
					// line 767 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "&", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 197:
					// line 770 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "<=>", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 198:
					// line 773 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), ">", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 199:
					// line 776 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), ">=", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 200:
					// line 779 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "<", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 201:
					// line 782 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "<=", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 202:
					// line 785 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "==", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 203:
					// line 788 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "===", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 204:
					// line 791 "DefaultRubyParser.y"
  {
                  yyVal = new NotNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "==", ((Node)yyVals[0+yyTop]), getPosition(null)));
              }
  break;
case 205:
					// line 794 "DefaultRubyParser.y"
  {
                  yyVal = support.getMatchNode(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
              }
  break;
case 206:
					// line 797 "DefaultRubyParser.y"
  {
                  yyVal = new NotNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), support.getMatchNode(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])));
              }
  break;
case 207:
					// line 800 "DefaultRubyParser.y"
  {
                  yyVal = new NotNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[0+yyTop])));
              }
  break;
case 208:
					// line 803 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[0+yyTop]), "~");
              }
  break;
case 209:
					// line 806 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), "<<", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 210:
					// line 809 "DefaultRubyParser.y"
  {
                  yyVal = support.getOperatorCallNode(((Node)yyVals[-2+yyTop]), ">>", ((Node)yyVals[0+yyTop]), getPosition(null));
              }
  break;
case 211:
					// line 812 "DefaultRubyParser.y"
  {
                  yyVal = support.newAndNode(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
              }
  break;
case 212:
					// line 815 "DefaultRubyParser.y"
  {
                  yyVal = support.newOrNode(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
              }
  break;
case 213:
					// line 818 "DefaultRubyParser.y"
  {
                  yyVal = new DefinedNode(getPosition(((Token)yyVals[-2+yyTop])), ((Node)yyVals[0+yyTop]));
              }
  break;
case 214:
					// line 821 "DefaultRubyParser.y"
  {
                  yyVal = new IfNode(getPosition(((Node)yyVals[-4+yyTop])), support.getConditionNode(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
              }
  break;
case 215:
					// line 824 "DefaultRubyParser.y"
  {
                  yyVal = ((Node)yyVals[0+yyTop]);
              }
  break;
case 216:
					// line 828 "DefaultRubyParser.y"
  {
	          support.checkExpression(((Node)yyVals[0+yyTop]));
	          yyVal = ((Node)yyVals[0+yyTop]);   
	      }
  break;
case 218:
					// line 834 "DefaultRubyParser.y"
  {
                  warnings.warn(getPosition(((Node)yyVals[-1+yyTop])), "parenthesize argument(s) for future version");
                  yyVal = new ArrayNode(getPosition(((Node)yyVals[-1+yyTop])), ((Node)yyVals[-1+yyTop]));
              }
  break;
case 219:
					// line 838 "DefaultRubyParser.y"
  {
                  yyVal = ((ListNode)yyVals[-1+yyTop]);
              }
  break;
case 220:
					// line 841 "DefaultRubyParser.y"
  {
                  support.checkExpression(((Node)yyVals[-1+yyTop]));
                  yyVal = support.arg_concat(getPosition(((ListNode)yyVals[-4+yyTop])), ((ListNode)yyVals[-4+yyTop]), ((Node)yyVals[-1+yyTop]));
              }
  break;
case 221:
					// line 845 "DefaultRubyParser.y"
  {
                  yyVal = new ArrayNode(getPosition(((ListNode)yyVals[-1+yyTop])), new HashNode(getPosition(null), ((ListNode)yyVals[-1+yyTop])));
              }
  break;
case 222:
					// line 848 "DefaultRubyParser.y"
  {
                  support.checkExpression(((Node)yyVals[-1+yyTop]));
		  yyVal = new NewlineNode(getPosition(((Token)yyVals[-2+yyTop])), new SplatNode(getPosition(((Token)yyVals[-2+yyTop])), ((Node)yyVals[-1+yyTop])));
              }
  break;
case 223:
					// line 853 "DefaultRubyParser.y"
  {
                  yyVal = new ArrayNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
              }
  break;
case 224:
					// line 856 "DefaultRubyParser.y"
  {
                  yyVal = ((Node)yyVals[-2+yyTop]);
		  ((Node)yyVal).setPosition(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])));
              }
  break;
case 225:
					// line 860 "DefaultRubyParser.y"
  {
                  warnings.warn(getPosition(((Token)yyVals[-3+yyTop])), "parenthesize argument(s) for future version");
                  yyVal = new ArrayNode(getPosition(((Token)yyVals[-3+yyTop])), ((Node)yyVals[-2+yyTop]));
              }
  break;
case 226:
					// line 864 "DefaultRubyParser.y"
  {
                  warnings.warn(getPosition(((Token)yyVals[-5+yyTop])), "parenthesize argument(s) for future version");
                  yyVal = ((ListNode)yyVals[-4+yyTop]).add(((Node)yyVals[-2+yyTop]));
              }
  break;
case 229:
					// line 872 "DefaultRubyParser.y"
  {
                  warnings.warn(((Node)yyVals[0+yyTop]).getPosition(), "parenthesize argument(s) for future version");
                  yyVal = new ArrayNode(getPosition(((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));
              }
  break;
case 230:
					// line 876 "DefaultRubyParser.y"
  {
                  yyVal = support.arg_blk_pass(((ListNode)yyVals[-1+yyTop]), ((BlockPassNode)yyVals[0+yyTop]));
              }
  break;
case 231:
					// line 879 "DefaultRubyParser.y"
  {
                  yyVal = support.arg_concat(getPosition(((ListNode)yyVals[-4+yyTop])), ((ListNode)yyVals[-4+yyTop]), ((Node)yyVals[-1+yyTop]));
                  yyVal = support.arg_blk_pass(((Node)yyVal), ((BlockPassNode)yyVals[0+yyTop]));
              }
  break;
case 232:
					// line 883 "DefaultRubyParser.y"
  {
                  yyVal = new ArrayNode(getPosition(((ListNode)yyVals[-1+yyTop])), new HashNode(getPosition(null), ((ListNode)yyVals[-1+yyTop])));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
              }
  break;
case 233:
					// line 887 "DefaultRubyParser.y"
  {
                  yyVal = support.arg_concat(getPosition(((ListNode)yyVals[-4+yyTop])), new ArrayNode(getPosition(((ListNode)yyVals[-4+yyTop])), new HashNode(getPosition(null), ((ListNode)yyVals[-4+yyTop]))), ((Node)yyVals[-1+yyTop]));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
              }
  break;
case 234:
					// line 891 "DefaultRubyParser.y"
  {
                  yyVal = ((ListNode)yyVals[-3+yyTop]).add(new HashNode(getPosition(null), ((ListNode)yyVals[-1+yyTop])));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
              }
  break;
case 235:
					// line 895 "DefaultRubyParser.y"
  {
                  support.checkExpression(((Node)yyVals[-1+yyTop]));
		  yyVal = support.arg_concat(getPosition(((ListNode)yyVals[-6+yyTop])), ((ListNode)yyVals[-6+yyTop]).add(new HashNode(getPosition(null), ((ListNode)yyVals[-4+yyTop]))), ((Node)yyVals[-1+yyTop]));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
              }
  break;
case 236:
					// line 900 "DefaultRubyParser.y"
  {
                  yyVal = support.arg_blk_pass(new SplatNode(getPosition(((Token)yyVals[-2+yyTop])), ((Node)yyVals[-1+yyTop])), ((BlockPassNode)yyVals[0+yyTop]));
              }
  break;
case 237:
					// line 903 "DefaultRubyParser.y"
  {}
  break;
case 238:
					// line 905 "DefaultRubyParser.y"
  {
                  yyVal = support.arg_blk_pass(new ArrayNode(getPosition(((Node)yyVals[-3+yyTop])), ((Node)yyVals[-3+yyTop])).addAll(((ListNode)yyVals[-1+yyTop])), ((BlockPassNode)yyVals[0+yyTop]));
	      }
  break;
case 239:
					// line 908 "DefaultRubyParser.y"
  {
                  yyVal = support.arg_blk_pass(new ArrayNode(getPosition(((Node)yyVals[-2+yyTop])), ((Node)yyVals[-2+yyTop])), ((BlockPassNode)yyVals[0+yyTop]));
              }
  break;
case 240:
					// line 911 "DefaultRubyParser.y"
  {
                  yyVal = support.arg_concat(getPosition(((Node)yyVals[-4+yyTop])), new ArrayNode(getPosition(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-4+yyTop])), ((Node)yyVals[-1+yyTop]));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
	      }
  break;
case 241:
					// line 915 "DefaultRubyParser.y"
  {
                  yyVal = support.arg_concat(getPosition(((Node)yyVals[-6+yyTop])), new ArrayNode(getPosition(((Node)yyVals[-6+yyTop])), ((Node)yyVals[-6+yyTop])).addAll(new HashNode(getPosition(null), ((ListNode)yyVals[-4+yyTop]))), ((Node)yyVals[-1+yyTop]));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
	      }
  break;
case 242:
					// line 919 "DefaultRubyParser.y"
  {
                  yyVal = new ArrayNode(getPosition(((ListNode)yyVals[-1+yyTop])), new HashNode(getPosition(null), ((ListNode)yyVals[-1+yyTop])));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
	      }
  break;
case 243:
					// line 923 "DefaultRubyParser.y"
  {
                  yyVal = support.arg_concat(getPosition(((ListNode)yyVals[-4+yyTop])), new ArrayNode(getPosition(((ListNode)yyVals[-4+yyTop])), new HashNode(getPosition(null), ((ListNode)yyVals[-4+yyTop]))), ((Node)yyVals[-1+yyTop]));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
	      }
  break;
case 244:
					// line 927 "DefaultRubyParser.y"
  {
                  yyVal = new ArrayNode(getPosition(((Node)yyVals[-3+yyTop])), ((Node)yyVals[-3+yyTop])).add(new HashNode(getPosition(null), ((ListNode)yyVals[-1+yyTop])));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
	      }
  break;
case 245:
					// line 931 "DefaultRubyParser.y"
  {
                  yyVal = new ArrayNode(getPosition(((Node)yyVals[-5+yyTop])), ((Node)yyVals[-5+yyTop])).addAll(((ListNode)yyVals[-3+yyTop])).add(new HashNode(getPosition(null), ((ListNode)yyVals[-1+yyTop])));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
	      }
  break;
case 246:
					// line 935 "DefaultRubyParser.y"
  {
                  yyVal = support.arg_concat(getPosition(((Node)yyVals[-6+yyTop])), new ArrayNode(getPosition(((Node)yyVals[-6+yyTop])), ((Node)yyVals[-6+yyTop])).add(new HashNode(getPosition(null), ((ListNode)yyVals[-4+yyTop]))), ((Node)yyVals[-1+yyTop]));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
	      }
  break;
case 247:
					// line 939 "DefaultRubyParser.y"
  {
                  yyVal = support.arg_concat(getPosition(((Node)yyVals[-8+yyTop])), new ArrayNode(getPosition(((Node)yyVals[-8+yyTop])), ((Node)yyVals[-8+yyTop])).addAll(((ListNode)yyVals[-6+yyTop])).add(new HashNode(getPosition(null), ((ListNode)yyVals[-4+yyTop]))), ((Node)yyVals[-1+yyTop]));
                  yyVal = support.arg_blk_pass((Node)yyVal, ((BlockPassNode)yyVals[0+yyTop]));
	      }
  break;
case 248:
					// line 943 "DefaultRubyParser.y"
  {
                  yyVal = support.arg_blk_pass(new SplatNode(getPosition(((Token)yyVals[-2+yyTop])), ((Node)yyVals[-1+yyTop])), ((BlockPassNode)yyVals[0+yyTop]));
	      }
  break;
case 249:
					// line 946 "DefaultRubyParser.y"
  {}
  break;
case 250:
					// line 948 "DefaultRubyParser.y"
  { 
	          yyVal = new Long(lexer.getCmdArgumentState().begin());
	      }
  break;
case 251:
					// line 950 "DefaultRubyParser.y"
  {
                  lexer.getCmdArgumentState().reset(((Long)yyVals[-1+yyTop]).longValue());
                  yyVal = ((Node)yyVals[0+yyTop]);
              }
  break;
case 253:
					// line 956 "DefaultRubyParser.y"
  {                    
		  lexer.setState(LexState.EXPR_ENDARG);
	      }
  break;
case 254:
					// line 958 "DefaultRubyParser.y"
  {
                  warnings.warn(getPosition(((Token)yyVals[-2+yyTop])), "don't put space before argument parentheses");
	          yyVal = null;
	      }
  break;
case 255:
					// line 962 "DefaultRubyParser.y"
  {
		  lexer.setState(LexState.EXPR_ENDARG);
	      }
  break;
case 256:
					// line 964 "DefaultRubyParser.y"
  {
                  warnings.warn(getPosition(((Token)yyVals[-3+yyTop])), "don't put space before argument parentheses");
		  yyVal = ((Node)yyVals[-2+yyTop]);
	      }
  break;
case 257:
					// line 969 "DefaultRubyParser.y"
  {
                  support.checkExpression(((Node)yyVals[0+yyTop]));
                  yyVal = new BlockPassNode(support.union(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));
              }
  break;
case 258:
					// line 974 "DefaultRubyParser.y"
  {
                  yyVal = ((BlockPassNode)yyVals[0+yyTop]);
              }
  break;
case 260:
					// line 979 "DefaultRubyParser.y"
  {
                  yyVal = new ArrayNode(getPosition2(((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));
              }
  break;
case 261:
					// line 982 "DefaultRubyParser.y"
  {
                  yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[0+yyTop]));
              }
  break;
case 262:
					// line 986 "DefaultRubyParser.y"
  {
		  yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[0+yyTop]));
              }
  break;
case 263:
					// line 989 "DefaultRubyParser.y"
  {
                  yyVal = support.arg_concat(getPosition(((ListNode)yyVals[-3+yyTop])), ((ListNode)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop]));
	      }
  break;
case 264:
					// line 992 "DefaultRubyParser.y"
  {  
                  yyVal = new SplatNode(getPosition(((Token)yyVals[-1+yyTop])), ((Node)yyVals[0+yyTop]));
	      }
  break;
case 273:
					// line 1004 "DefaultRubyParser.y"
  {
                  yyVal = new FCallNode(((Token)yyVals[0+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue(), null);
	      }
  break;
case 274:
					// line 1007 "DefaultRubyParser.y"
  {
                  yyVal = new BeginNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-1+yyTop]));
	      }
  break;
case 275:
					// line 1010 "DefaultRubyParser.y"
  { 
                  lexer.setState(LexState.EXPR_ENDARG); 
              }
  break;
case 276:
					// line 1012 "DefaultRubyParser.y"
  {
		  warnings.warning(getPosition(((Token)yyVals[-4+yyTop])), "(...) interpreted as grouped expression");
                  yyVal = ((Node)yyVals[-3+yyTop]);
	      }
  break;
case 277:
					// line 1016 "DefaultRubyParser.y"
  {
		  yyVal = ((Node)yyVals[-1+yyTop]);
              }
  break;
case 278:
					// line 1019 "DefaultRubyParser.y"
  {
                  yyVal = new Colon2Node(support.union(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), (String) ((Token)yyVals[0+yyTop]).getValue());
              }
  break;
case 279:
					// line 1022 "DefaultRubyParser.y"
  {
                  yyVal = new Colon3Node(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])), (String) ((Token)yyVals[0+yyTop]).getValue());
              }
  break;
case 280:
					// line 1025 "DefaultRubyParser.y"
  {
                  if (((Node)yyVals[-3+yyTop]) instanceof SelfNode) {
                      yyVal = new FCallNode(getPosition(((Node)yyVals[-3+yyTop])), "[]", ((Node)yyVals[-1+yyTop]));
                  } else {
                      yyVal = new CallNode(getPosition(((Node)yyVals[-3+yyTop])), ((Node)yyVals[-3+yyTop]), "[]", ((Node)yyVals[-1+yyTop]));
                  }
              }
  break;
case 281:
					// line 1032 "DefaultRubyParser.y"
  {
                  ISourcePosition position = support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]));
                  if (((Node)yyVals[-1+yyTop]) == null) {
                      yyVal = new ZArrayNode(position); /* zero length array */
                  } else {
                      yyVal = ((Node)yyVals[-1+yyTop]);
                      ((ISourcePositionHolder)yyVal).setPosition(position);
                  }
              }
  break;
case 282:
					// line 1041 "DefaultRubyParser.y"
  {
                  yyVal = new HashNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), ((ListNode)yyVals[-1+yyTop]));
              }
  break;
case 283:
					// line 1044 "DefaultRubyParser.y"
  {
		  yyVal = new ReturnNode(((Token)yyVals[0+yyTop]).getPosition(), null);
              }
  break;
case 284:
					// line 1047 "DefaultRubyParser.y"
  {
                  yyVal = support.new_yield(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-1+yyTop]));
              }
  break;
case 285:
					// line 1050 "DefaultRubyParser.y"
  {
                  yyVal = new YieldNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])), null, false);
              }
  break;
case 286:
					// line 1053 "DefaultRubyParser.y"
  {
                  yyVal = new YieldNode(((Token)yyVals[0+yyTop]).getPosition(), null, false);
              }
  break;
case 287:
					// line 1056 "DefaultRubyParser.y"
  {
                  yyVal = new DefinedNode(getPosition(((Token)yyVals[-4+yyTop])), ((Node)yyVals[-1+yyTop]));
              }
  break;
case 288:
					// line 1059 "DefaultRubyParser.y"
  {
                  yyVal = new FCallNode(support.union(((Token)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop])), (String) ((Token)yyVals[-1+yyTop]).getValue(), null, ((IterNode)yyVals[0+yyTop]));
              }
  break;
case 290:
					// line 1063 "DefaultRubyParser.y"
  {
	          if (((Node)yyVals[-1+yyTop]) != null && 
                      ((BlockAcceptingNode)yyVals[-1+yyTop]).getIterNode() instanceof BlockPassNode) {
                      throw new SyntaxException(getPosition(((Node)yyVals[-1+yyTop])), "Both block arg and actual block given.");
		  }
		  ((BlockAcceptingNode)yyVals[-1+yyTop]).setIterNode(((IterNode)yyVals[0+yyTop]));
		  ((Node)yyVals[-1+yyTop]).setPosition(support.union(((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop])));
              }
  break;
case 291:
					// line 1071 "DefaultRubyParser.y"
  {
                  yyVal = new IfNode(support.union(((Token)yyVals[-5+yyTop]), ((Token)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop]));
              }
  break;
case 292:
					// line 1074 "DefaultRubyParser.y"
  {
                  yyVal = new IfNode(support.union(((Token)yyVals[-5+yyTop]), ((Token)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[-2+yyTop]));
              }
  break;
case 293:
					// line 1077 "DefaultRubyParser.y"
  { 
                  lexer.getConditionState().begin();
	      }
  break;
case 294:
					// line 1079 "DefaultRubyParser.y"
  {
		  lexer.getConditionState().end();
	      }
  break;
case 295:
					// line 1081 "DefaultRubyParser.y"
  {
                  yyVal = new WhileNode(support.union(((Token)yyVals[-6+yyTop]), ((Token)yyVals[0+yyTop])), support.getConditionNode(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-1+yyTop]));
              }
  break;
case 296:
					// line 1084 "DefaultRubyParser.y"
  {
                  lexer.getConditionState().begin();
              }
  break;
case 297:
					// line 1086 "DefaultRubyParser.y"
  {
                  lexer.getConditionState().end();
              }
  break;
case 298:
					// line 1088 "DefaultRubyParser.y"
  {
                  yyVal = new UntilNode(getPosition(((Token)yyVals[-6+yyTop])), support.getConditionNode(((Node)yyVals[-4+yyTop])), ((Node)yyVals[-1+yyTop]));
              }
  break;
case 299:
					// line 1091 "DefaultRubyParser.y"
  {
                  yyVal = new CaseNode(support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]));
              }
  break;
case 300:
					// line 1094 "DefaultRubyParser.y"
  {
                  yyVal = new CaseNode(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])), null, ((Node)yyVals[-1+yyTop]));
              }
  break;
case 301:
					// line 1097 "DefaultRubyParser.y"
  {
		  yyVal = ((Node)yyVals[-1+yyTop]);
              }
  break;
case 302:
					// line 1100 "DefaultRubyParser.y"
  {
                  lexer.getConditionState().begin();
              }
  break;
case 303:
					// line 1102 "DefaultRubyParser.y"
  {
                  lexer.getConditionState().end();
              }
  break;
case 304:
					// line 1104 "DefaultRubyParser.y"
  {
                  yyVal = new ForNode(support.union(((Token)yyVals[-8+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-7+yyTop]), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[-4+yyTop]));
              }
  break;
case 305:
					// line 1107 "DefaultRubyParser.y"
  {
                  if (support.isInDef() || support.isInSingle()) {
                      yyerror("class definition in method body");
                  }
		  support.pushLocalScope();
              }
  break;
case 306:
					// line 1112 "DefaultRubyParser.y"
  {
                  yyVal = new ClassNode(support.union(((Token)yyVals[-5+yyTop]), ((Token)yyVals[0+yyTop])), ((Colon3Node)yyVals[-4+yyTop]), support.getCurrentScope(), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[-3+yyTop]));
                  support.popCurrentScope();
              }
  break;
case 307:
					// line 1116 "DefaultRubyParser.y"
  {
                  yyVal = new Boolean(support.isInDef());
                  support.setInDef(false);
              }
  break;
case 308:
					// line 1119 "DefaultRubyParser.y"
  {
                  yyVal = new Integer(support.getInSingle());
                  support.setInSingle(0);
		  support.pushLocalScope();
              }
  break;
case 309:
					// line 1123 "DefaultRubyParser.y"
  {
                  yyVal = new SClassNode(support.union(((Token)yyVals[-7+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-5+yyTop]), support.getCurrentScope(), ((Node)yyVals[-1+yyTop]));
                  support.popCurrentScope();
                  support.setInDef(((Boolean)yyVals[-4+yyTop]).booleanValue());
                  support.setInSingle(((Integer)yyVals[-2+yyTop]).intValue());
              }
  break;
case 310:
					// line 1129 "DefaultRubyParser.y"
  {
                  if (support.isInDef() || support.isInSingle()) { 
                      yyerror("module definition in method body");
                  }
		  support.pushLocalScope();
              }
  break;
case 311:
					// line 1134 "DefaultRubyParser.y"
  {
                  yyVal = new ModuleNode(support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop])), ((Colon3Node)yyVals[-3+yyTop]), support.getCurrentScope(), ((Node)yyVals[-1+yyTop]));
                  support.popCurrentScope();
              }
  break;
case 312:
					// line 1138 "DefaultRubyParser.y"
  {
                  support.setInDef(true);
		  support.pushLocalScope();
              }
  break;
case 313:
					// line 1141 "DefaultRubyParser.y"
  {
                    /* NOEX_PRIVATE for toplevel */
                  yyVal = new DefnNode(support.union(((Token)yyVals[-5+yyTop]), ((Token)yyVals[0+yyTop])), new ArgumentNode(((Token)yyVals[-4+yyTop]).getPosition(), (String) ((Token)yyVals[-4+yyTop]).getValue()), ((ArgsNode)yyVals[-2+yyTop]), support.getCurrentScope(), ((Node)yyVals[-1+yyTop]), Visibility.PRIVATE);
                  support.popCurrentScope();
                  support.setInDef(false);
              }
  break;
case 314:
					// line 1147 "DefaultRubyParser.y"
  {
                  lexer.setState(LexState.EXPR_FNAME);
              }
  break;
case 315:
					// line 1149 "DefaultRubyParser.y"
  {
                  support.setInSingle(support.getInSingle() + 1);
		  support.pushLocalScope();
                  lexer.setState(LexState.EXPR_END); /* force for args */
              }
  break;
case 316:
					// line 1153 "DefaultRubyParser.y"
  {
                  yyVal = new DefsNode(support.union(((Token)yyVals[-8+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-7+yyTop]), new ArgumentNode(((Token)yyVals[-4+yyTop]).getPosition(), (String) ((Token)yyVals[-4+yyTop]).getValue()), ((ArgsNode)yyVals[-2+yyTop]), support.getCurrentScope(), ((Node)yyVals[-1+yyTop]));
                  support.popCurrentScope();
                  support.setInSingle(support.getInSingle() - 1);
              }
  break;
case 317:
					// line 1158 "DefaultRubyParser.y"
  {
                  yyVal = new BreakNode(((Token)yyVals[0+yyTop]).getPosition());
              }
  break;
case 318:
					// line 1161 "DefaultRubyParser.y"
  {
                  yyVal = new NextNode(((Token)yyVals[0+yyTop]).getPosition());
              }
  break;
case 319:
					// line 1164 "DefaultRubyParser.y"
  {
                  yyVal = new RedoNode(((Token)yyVals[0+yyTop]).getPosition());
              }
  break;
case 320:
					// line 1167 "DefaultRubyParser.y"
  {
                  yyVal = new RetryNode(((Token)yyVals[0+yyTop]).getPosition());
              }
  break;
case 321:
					// line 1171 "DefaultRubyParser.y"
  {
                  support.checkExpression(((Node)yyVals[0+yyTop]));
		  yyVal = ((Node)yyVals[0+yyTop]);
	      }
  break;
case 330:
					// line 1186 "DefaultRubyParser.y"
  {
/*mirko: support.union($<ISourcePositionHolder>1.getPosition(), getPosition($<ISourcePositionHolder>1)) ?*/
                  yyVal = new IfNode(getPosition(((Token)yyVals[-4+yyTop])), support.getConditionNode(((Node)yyVals[-3+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
              }
  break;
case 332:
					// line 1192 "DefaultRubyParser.y"
  {
                  yyVal = ((Node)yyVals[0+yyTop]);
              }
  break;
case 334:
					// line 1197 "DefaultRubyParser.y"
  {}
  break;
case 336:
					// line 1200 "DefaultRubyParser.y"
  {
                  yyVal = new ZeroArgNode(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])));
              }
  break;
case 337:
					// line 1203 "DefaultRubyParser.y"
  {
                  yyVal = new ZeroArgNode(((Token)yyVals[0+yyTop]).getPosition());
	      }
  break;
case 338:
					// line 1206 "DefaultRubyParser.y"
  {
                  yyVal = ((Node)yyVals[-1+yyTop]);

		  /* Include pipes on multiple arg type*/
                  if (((Node)yyVals[-1+yyTop]) instanceof MultipleAsgnNode) {
		      ((Node)yyVals[-1+yyTop]).setPosition(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
		  } 
              }
  break;
case 339:
					// line 1215 "DefaultRubyParser.y"
  {
                  support.pushBlockScope();
	      }
  break;
case 340:
					// line 1217 "DefaultRubyParser.y"
  {
                  yyVal = new IterNode(support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), support.getCurrentScope(), ((Node)yyVals[-1+yyTop]));
                  support.popCurrentScope();
              }
  break;
case 341:
					// line 1222 "DefaultRubyParser.y"
  {
	          if (((Node)yyVals[-1+yyTop]) != null && 
                      ((BlockAcceptingNode)yyVals[-1+yyTop]).getIterNode() instanceof BlockPassNode) {
                      throw new SyntaxException(getPosition(((Node)yyVals[-1+yyTop])), "Both block arg and actual block given.");
                  }
		  ((BlockAcceptingNode)yyVals[-1+yyTop]).setIterNode(((IterNode)yyVals[0+yyTop]));
		  ((Node)yyVals[-1+yyTop]).setPosition(support.union(((Node)yyVals[-1+yyTop]), ((IterNode)yyVals[0+yyTop])));
              }
  break;
case 342:
					// line 1230 "DefaultRubyParser.y"
  {
                  yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
              }
  break;
case 343:
					// line 1233 "DefaultRubyParser.y"
  {
                  yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
              }
  break;
case 344:
					// line 1237 "DefaultRubyParser.y"
  {
                  yyVal = support.new_fcall(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
              }
  break;
case 345:
					// line 1240 "DefaultRubyParser.y"
  {
                  yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
              }
  break;
case 346:
					// line 1243 "DefaultRubyParser.y"
  {
                  yyVal = support.new_call(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]), null);
              }
  break;
case 347:
					// line 1246 "DefaultRubyParser.y"
  {
                  yyVal = support.new_call(((Node)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]), null, null);
              }
  break;
case 348:
					// line 1249 "DefaultRubyParser.y"
  {
                  yyVal = support.new_super(((Node)yyVals[0+yyTop]), ((Token)yyVals[-1+yyTop]));
              }
  break;
case 349:
					// line 1252 "DefaultRubyParser.y"
  {
                  yyVal = new ZSuperNode(((Token)yyVals[0+yyTop]).getPosition());
              }
  break;
case 350:
					// line 1257 "DefaultRubyParser.y"
  {
                  support.pushBlockScope();
	      }
  break;
case 351:
					// line 1259 "DefaultRubyParser.y"
  {
                  yyVal = new IterNode(support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), support.getCurrentScope(), ((Node)yyVals[-1+yyTop]));
                  support.popCurrentScope();
              }
  break;
case 352:
					// line 1263 "DefaultRubyParser.y"
  {
                  support.pushBlockScope();
	      }
  break;
case 353:
					// line 1265 "DefaultRubyParser.y"
  {
                  yyVal = new IterNode(support.union(((Token)yyVals[-4+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop]), support.getCurrentScope(), ((Node)yyVals[-1+yyTop]));
                  ((ISourcePositionHolder)yyVals[-5+yyTop]).setPosition(support.union(((ISourcePositionHolder)yyVals[-5+yyTop]), ((ISourcePositionHolder)yyVal)));
                  support.popCurrentScope();
              }
  break;
case 354:
					// line 1271 "DefaultRubyParser.y"
  {
                  yyVal = new WhenNode(support.union(((Token)yyVals[-4+yyTop]), support.unwrapNewlineNode(((Node)yyVals[-1+yyTop]))), ((ListNode)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
              }
  break;
case 356:
					// line 1276 "DefaultRubyParser.y"
  {
                  yyVal = ((ListNode)yyVals[-3+yyTop]).add(new WhenNode(getPosition(((ListNode)yyVals[-3+yyTop])), ((Node)yyVals[0+yyTop]), null, null));
              }
  break;
case 357:
					// line 1279 "DefaultRubyParser.y"
  {
                  yyVal = new ArrayNode(getPosition(((Token)yyVals[-1+yyTop])), new WhenNode(getPosition(((Token)yyVals[-1+yyTop])), ((Node)yyVals[0+yyTop]), null, null));
              }
  break;
case 360:
					// line 1286 "DefaultRubyParser.y"
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
                  yyVal = new RescueBodyNode(getPosition(((Token)yyVals[-5+yyTop]), true), ((Node)yyVals[-4+yyTop]), node, ((RescueBodyNode)yyVals[0+yyTop]));
	      }
  break;
case 361:
					// line 1298 "DefaultRubyParser.y"
  {yyVal = null;}
  break;
case 362:
					// line 1300 "DefaultRubyParser.y"
  {
                  yyVal = new ArrayNode(((Node)yyVals[0+yyTop]).getPosition(), ((Node)yyVals[0+yyTop]));
	      }
  break;
case 365:
					// line 1306 "DefaultRubyParser.y"
  {
                  yyVal = ((Node)yyVals[0+yyTop]);
              }
  break;
case 367:
					// line 1311 "DefaultRubyParser.y"
  {
                  if (((Node)yyVals[0+yyTop]) != null) {
                      yyVal = ((Node)yyVals[0+yyTop]);
                  } else {
                      yyVal = new NilNode(getPosition(null));
                  }
              }
  break;
case 370:
					// line 1321 "DefaultRubyParser.y"
  {
                  yyVal = new SymbolNode(((Token)yyVals[0+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue());
              }
  break;
case 372:
					// line 1326 "DefaultRubyParser.y"
  {
                  if (((Node)yyVals[0+yyTop]) instanceof EvStrNode) {
                      yyVal = new DStrNode(getPosition(((Node)yyVals[0+yyTop]))).add(((Node)yyVals[0+yyTop]));
                  } else {
                      yyVal = ((Node)yyVals[0+yyTop]);
                  }
	      }
  break;
case 374:
					// line 1335 "DefaultRubyParser.y"
  {
                  yyVal = support.literal_concat(getPosition(((Node)yyVals[-1+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
              }
  break;
case 375:
					// line 1339 "DefaultRubyParser.y"
  {
                  yyVal = ((Node)yyVals[-1+yyTop]);
                  ((ISourcePositionHolder)yyVal).setPosition(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
		  int extraLength = ((String) ((Token)yyVals[-2+yyTop]).getValue()).length() - 1;

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
  break;
case 376:
					// line 1355 "DefaultRubyParser.y"
  {
                  ISourcePosition position = support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop]));

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
  break;
case 377:
					// line 1371 "DefaultRubyParser.y"
  {
		  int options = ((RegexpNode)yyVals[0+yyTop]).getOptions();
		  Node node = ((Node)yyVals[-1+yyTop]);

		  if (node == null) {
                      yyVal = new RegexpNode(getPosition(((Token)yyVals[-2+yyTop])), ByteList.create(""), options & ~ReOptions.RE_OPTION_ONCE);
		  } else if (node instanceof StrNode) {
                      yyVal = new RegexpNode(((Node)yyVals[-1+yyTop]).getPosition(), (ByteList) ((StrNode) node).getValue().clone(), options & ~ReOptions.RE_OPTION_ONCE);
		  } else if (node instanceof DStrNode) {
                      yyVal = new DRegexpNode(getPosition(((Token)yyVals[-2+yyTop])), (DStrNode) node, options, (options & ReOptions.RE_OPTION_ONCE) != 0);
		  } else {
		      yyVal = new DRegexpNode(getPosition(((Token)yyVals[-2+yyTop])), options, (options & ReOptions.RE_OPTION_ONCE) != 0).add(node);
                  }
	       }
  break;
case 378:
					// line 1386 "DefaultRubyParser.y"
  {
                   yyVal = new ZArrayNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
	       }
  break;
case 379:
					// line 1389 "DefaultRubyParser.y"
  {
		   yyVal = ((ListNode)yyVals[-1+yyTop]);
                   ((ISourcePositionHolder)yyVal).setPosition(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
	       }
  break;
case 380:
					// line 1394 "DefaultRubyParser.y"
  {
                   yyVal = new ArrayNode(getPosition(null));
	       }
  break;
case 381:
					// line 1397 "DefaultRubyParser.y"
  {
                   yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[-1+yyTop]) instanceof EvStrNode ? new DStrNode(getPosition(((ListNode)yyVals[-2+yyTop]))).add(((Node)yyVals[-1+yyTop])) : ((Node)yyVals[-1+yyTop]));
	       }
  break;
case 383:
					// line 1402 "DefaultRubyParser.y"
  {
                   yyVal = support.literal_concat(getPosition(((Node)yyVals[-1+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
	       }
  break;
case 384:
					// line 1406 "DefaultRubyParser.y"
  {
                   yyVal = new ZArrayNode(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
	       }
  break;
case 385:
					// line 1409 "DefaultRubyParser.y"
  {
		   yyVal = ((ListNode)yyVals[-1+yyTop]);
                   ((ISourcePositionHolder)yyVal).setPosition(support.union(((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
	       }
  break;
case 386:
					// line 1414 "DefaultRubyParser.y"
  {
                   yyVal = new ArrayNode(getPosition(null));
	       }
  break;
case 387:
					// line 1417 "DefaultRubyParser.y"
  {
                   yyVal = ((ListNode)yyVals[-2+yyTop]).add(((Node)yyVals[-1+yyTop]));
	       }
  break;
case 388:
					// line 1421 "DefaultRubyParser.y"
  {
                   yyVal = new StrNode(((Token)yyVals[0+yyTop]).getPosition(), ByteList.create(""));
	       }
  break;
case 389:
					// line 1424 "DefaultRubyParser.y"
  {
                   yyVal = support.literal_concat(getPosition(((Node)yyVals[-1+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
	       }
  break;
case 390:
					// line 1428 "DefaultRubyParser.y"
  {
		   yyVal = null;
	       }
  break;
case 391:
					// line 1431 "DefaultRubyParser.y"
  {
                   yyVal = support.literal_concat(getPosition(((Node)yyVals[-1+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop]));
	       }
  break;
case 392:
					// line 1435 "DefaultRubyParser.y"
  {
                   yyVal = ((Node)yyVals[0+yyTop]);
               }
  break;
case 393:
					// line 1438 "DefaultRubyParser.y"
  {
                   yyVal = lexer.getStrTerm();
		   lexer.setStrTerm(null);
		   lexer.setState(LexState.EXPR_BEG);
	       }
  break;
case 394:
					// line 1442 "DefaultRubyParser.y"
  {
		   lexer.setStrTerm(((StrTerm)yyVals[-1+yyTop]));
	           yyVal = new EvStrNode(support.union(((Token)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[0+yyTop]));
	       }
  break;
case 395:
					// line 1446 "DefaultRubyParser.y"
  {
		   yyVal = lexer.getStrTerm();
		   lexer.setStrTerm(null);
		   lexer.setState(LexState.EXPR_BEG);
	       }
  break;
case 396:
					// line 1450 "DefaultRubyParser.y"
  {
		   lexer.setStrTerm(((StrTerm)yyVals[-2+yyTop]));

		   yyVal = support.newEvStrNode(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])), ((Node)yyVals[-1+yyTop]));
	       }
  break;
case 397:
					// line 1456 "DefaultRubyParser.y"
  {
                   yyVal = new GlobalVarNode(((Token)yyVals[0+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue());
               }
  break;
case 398:
					// line 1459 "DefaultRubyParser.y"
  {
                   yyVal = new InstVarNode(((Token)yyVals[0+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue());
               }
  break;
case 399:
					// line 1462 "DefaultRubyParser.y"
  {
                   yyVal = new ClassVarNode(((Token)yyVals[0+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue());
               }
  break;
case 401:
					// line 1468 "DefaultRubyParser.y"
  {
                   lexer.setState(LexState.EXPR_END);
                   yyVal = ((Token)yyVals[0+yyTop]);
		   ((ISourcePositionHolder)yyVal).setPosition(support.union(((Token)yyVals[-1+yyTop]), ((Token)yyVals[0+yyTop])));
               }
  break;
case 406:
					// line 1476 "DefaultRubyParser.y"
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
case 408:
					// line 1503 "DefaultRubyParser.y"
  {
                   yyVal = ((FloatNode)yyVals[0+yyTop]);
               }
  break;
case 409:
					// line 1506 "DefaultRubyParser.y"
  {
                   yyVal = support.negateInteger(((Node)yyVals[0+yyTop]));
	       }
  break;
case 410:
					// line 1509 "DefaultRubyParser.y"
  {
                   yyVal = support.negateFloat(((FloatNode)yyVals[0+yyTop]));
	       }
  break;
case 416:
					// line 1515 "DefaultRubyParser.y"
  { 
		   yyVal = new Token("nil", ((Token)yyVals[0+yyTop]).getPosition());
               }
  break;
case 417:
					// line 1518 "DefaultRubyParser.y"
  {
		   yyVal = new Token("self", ((Token)yyVals[0+yyTop]).getPosition());
               }
  break;
case 418:
					// line 1521 "DefaultRubyParser.y"
  { 
		   yyVal = new Token("true", ((Token)yyVals[0+yyTop]).getPosition());
               }
  break;
case 419:
					// line 1524 "DefaultRubyParser.y"
  {
		   yyVal = new Token("false", ((Token)yyVals[0+yyTop]).getPosition());
               }
  break;
case 420:
					// line 1527 "DefaultRubyParser.y"
  {
		   yyVal = new Token("__FILE__", ((Token)yyVals[0+yyTop]).getPosition());
               }
  break;
case 421:
					// line 1530 "DefaultRubyParser.y"
  {
		   yyVal = new Token("__LINE__", ((Token)yyVals[0+yyTop]).getPosition());
               }
  break;
case 422:
					// line 1534 "DefaultRubyParser.y"
  {
		   yyVal = support.gettable((String) ((Token)yyVals[0+yyTop]).getValue(), ((Token)yyVals[0+yyTop]).getPosition());
               }
  break;
case 423:
					// line 1538 "DefaultRubyParser.y"
  {
                   yyVal = support.assignable(((Token)yyVals[0+yyTop]), null);
               }
  break;
case 426:
					// line 1544 "DefaultRubyParser.y"
  {
                   yyVal = null;
               }
  break;
case 427:
					// line 1547 "DefaultRubyParser.y"
  {
                   lexer.setState(LexState.EXPR_BEG);
               }
  break;
case 428:
					// line 1549 "DefaultRubyParser.y"
  {
                   yyVal = ((Node)yyVals[-1+yyTop]);
               }
  break;
case 429:
					// line 1552 "DefaultRubyParser.y"
  {
                   yyerrok();
                   yyVal = null;
               }
  break;
case 430:
					// line 1558 "DefaultRubyParser.y"
  {
                   yyVal = ((Node)yyVals[-2+yyTop]);
                   ((ISourcePositionHolder)yyVal).setPosition(support.union(((Token)yyVals[-3+yyTop]), ((Token)yyVals[0+yyTop])));
                   lexer.setState(LexState.EXPR_BEG);
               }
  break;
case 431:
					// line 1563 "DefaultRubyParser.y"
  {
                   yyVal = ((Node)yyVals[-1+yyTop]);
               }
  break;
case 432:
					// line 1567 "DefaultRubyParser.y"
  {
                   yyVal = new ArgsNode(support.union(((ListNode)yyVals[-5+yyTop]), ((BlockArgNode)yyVals[0+yyTop])), ((ListNode)yyVals[-5+yyTop]), ((ListNode)yyVals[-3+yyTop]), ((Integer) ((Token)yyVals[-1+yyTop]).getValue()).intValue(), ((BlockArgNode)yyVals[0+yyTop]));
               }
  break;
case 433:
					// line 1570 "DefaultRubyParser.y"
  {
                   yyVal = new ArgsNode(getPosition(((ListNode)yyVals[-3+yyTop])), ((ListNode)yyVals[-3+yyTop]), ((ListNode)yyVals[-1+yyTop]), -1, ((BlockArgNode)yyVals[0+yyTop]));
               }
  break;
case 434:
					// line 1573 "DefaultRubyParser.y"
  {
                   yyVal = new ArgsNode(support.union(((ListNode)yyVals[-3+yyTop]), ((BlockArgNode)yyVals[0+yyTop])), ((ListNode)yyVals[-3+yyTop]), null, ((Integer) ((Token)yyVals[-1+yyTop]).getValue()).intValue(), ((BlockArgNode)yyVals[0+yyTop]));
               }
  break;
case 435:
					// line 1576 "DefaultRubyParser.y"
  {
                   yyVal = new ArgsNode(((ISourcePositionHolder)yyVals[-1+yyTop]).getPosition(), ((ListNode)yyVals[-1+yyTop]), null, -1, ((BlockArgNode)yyVals[0+yyTop]));
               }
  break;
case 436:
					// line 1579 "DefaultRubyParser.y"
  {
                   yyVal = new ArgsNode(getPosition(((ListNode)yyVals[-3+yyTop])), null, ((ListNode)yyVals[-3+yyTop]), ((Integer) ((Token)yyVals[-1+yyTop]).getValue()).intValue(), ((BlockArgNode)yyVals[0+yyTop]));
               }
  break;
case 437:
					// line 1582 "DefaultRubyParser.y"
  {
                   yyVal = new ArgsNode(getPosition(((ListNode)yyVals[-1+yyTop])), null, ((ListNode)yyVals[-1+yyTop]), -1, ((BlockArgNode)yyVals[0+yyTop]));
               }
  break;
case 438:
					// line 1585 "DefaultRubyParser.y"
  {
                   yyVal = new ArgsNode(getPosition(((Token)yyVals[-1+yyTop])), null, null, ((Integer) ((Token)yyVals[-1+yyTop]).getValue()).intValue(), ((BlockArgNode)yyVals[0+yyTop]));
               }
  break;
case 439:
					// line 1588 "DefaultRubyParser.y"
  {
                   yyVal = new ArgsNode(getPosition(((BlockArgNode)yyVals[0+yyTop])), null, null, -1, ((BlockArgNode)yyVals[0+yyTop]));
               }
  break;
case 440:
					// line 1591 "DefaultRubyParser.y"
  {
                   yyVal = new ArgsNode(support.createEmptyArgsNodePosition(getPosition(null)), null, null, -1, null);
               }
  break;
case 441:
					// line 1595 "DefaultRubyParser.y"
  {
                   yyerror("formal argument cannot be a constant");
               }
  break;
case 442:
					// line 1598 "DefaultRubyParser.y"
  {
                   yyerror("formal argument cannot be an instance variable");
               }
  break;
case 443:
					// line 1601 "DefaultRubyParser.y"
  {
                   yyerror("formal argument cannot be a class variable");
               }
  break;
case 444:
					// line 1604 "DefaultRubyParser.y"
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
case 445:
					// line 1616 "DefaultRubyParser.y"
  {
                    yyVal = new ListNode(((ISourcePositionHolder)yyVals[0+yyTop]).getPosition());
                    ((ListNode) yyVal).add(new ArgumentNode(((ISourcePositionHolder)yyVals[0+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue()));
               }
  break;
case 446:
					// line 1620 "DefaultRubyParser.y"
  {
                   ((ListNode)yyVals[-2+yyTop]).add(new ArgumentNode(((ISourcePositionHolder)yyVals[0+yyTop]).getPosition(), (String) ((Token)yyVals[0+yyTop]).getValue()));
                   ((ListNode)yyVals[-2+yyTop]).setPosition(support.union(((ListNode)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])));
		   yyVal = ((ListNode)yyVals[-2+yyTop]);
               }
  break;
case 447:
					// line 1626 "DefaultRubyParser.y"
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
case 448:
					// line 1638 "DefaultRubyParser.y"
  {
                  yyVal = new BlockNode(getPosition(((Node)yyVals[0+yyTop]))).add(((Node)yyVals[0+yyTop]));
              }
  break;
case 449:
					// line 1641 "DefaultRubyParser.y"
  {
                  yyVal = support.appendToBlock(((ListNode)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]));
              }
  break;
case 452:
					// line 1647 "DefaultRubyParser.y"
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
case 453:
					// line 1658 "DefaultRubyParser.y"
  {
                  ((Token)yyVals[0+yyTop]).setValue(new Integer(-2));
                  yyVal = ((Token)yyVals[0+yyTop]);
              }
  break;
case 456:
					// line 1665 "DefaultRubyParser.y"
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
case 457:
					// line 1676 "DefaultRubyParser.y"
  {
                  yyVal = ((BlockArgNode)yyVals[0+yyTop]);
              }
  break;
case 458:
					// line 1679 "DefaultRubyParser.y"
  {
	          yyVal = null;
	      }
  break;
case 459:
					// line 1683 "DefaultRubyParser.y"
  {
                  if (!(((Node)yyVals[0+yyTop]) instanceof SelfNode)) {
		      support.checkExpression(((Node)yyVals[0+yyTop]));
		  }
		  yyVal = ((Node)yyVals[0+yyTop]);
              }
  break;
case 460:
					// line 1689 "DefaultRubyParser.y"
  {
                  lexer.setState(LexState.EXPR_BEG);
              }
  break;
case 461:
					// line 1691 "DefaultRubyParser.y"
  {
                  if (((Node)yyVals[-2+yyTop]) instanceof ILiteralNode) {
                      yyerror("Can't define single method for literals.");
                  }
		  support.checkExpression(((Node)yyVals[-2+yyTop]));
                  yyVal = ((Node)yyVals[-2+yyTop]);
              }
  break;
case 462:
					// line 1701 "DefaultRubyParser.y"
  { /* [!null]*/
                  yyVal = new ArrayNode(getPosition(null));
              }
  break;
case 463:
					// line 1704 "DefaultRubyParser.y"
  { /* [!null]*/
                  yyVal = ((ListNode)yyVals[-1+yyTop]);
              }
  break;
case 464:
					// line 1707 "DefaultRubyParser.y"
  {
                  if (((ListNode)yyVals[-1+yyTop]).size() % 2 != 0) {
                      yyerror("Odd number list for Hash.");
                  }
                  yyVal = ((ListNode)yyVals[-1+yyTop]);
              }
  break;
case 466:
					// line 1716 "DefaultRubyParser.y"
  { /* [!null]*/
                  yyVal = ((ListNode)yyVals[-2+yyTop]).addAll(((ListNode)yyVals[0+yyTop]));
              }
  break;
case 467:
					// line 1721 "DefaultRubyParser.y"
  { /* [!null]*/
                  yyVal = new ArrayNode(support.union(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), ((Node)yyVals[-2+yyTop])).add(((Node)yyVals[0+yyTop]));
              }
  break;
case 468:
					// line 1725 "DefaultRubyParser.y"
  {
                  yyVal = new ArrayNode(support.union(((Token)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])), new SymbolNode(((Token)yyVals[-2+yyTop]).getPosition(), (String) ((Token)yyVals[-2+yyTop]).getValue())).add(((Node)yyVals[0+yyTop]));
              }
  break;
case 488:
					// line 1737 "DefaultRubyParser.y"
  {
                  yyerrok();
              }
  break;
case 491:
					// line 1743 "DefaultRubyParser.y"
  {
                  yyerrok();
              }
  break;
case 492:
					// line 1747 "DefaultRubyParser.y"
  {
                  yyVal = null;
              }
  break;
case 493:
					// line 1751 "DefaultRubyParser.y"
  {  
                  yyVal = null;
	      }
  break;
					// line 7451 "-"
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

					// line 1756 "DefaultRubyParser.y"

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
					// line 7531 "-"
