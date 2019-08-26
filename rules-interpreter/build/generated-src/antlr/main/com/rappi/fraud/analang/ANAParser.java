// Generated from com/rappi/fraud/analang/ANA.g4 by ANTLR 4.7.2

package com.rappi.fraud.analang;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ANAParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		DOT=10, COMMA=11, STAR=12, PLUS=13, MINUS=14, TILDE=15, LT=16, LT_EQ=17, 
		GT=18, GT_EQ=19, EQ=20, NOT_EQ1=21, NOT_EQ2=22, K_WORKFLOW=23, K_END=24, 
		K_ELSE=25, K_AND=26, K_OR=27, K_CONTAINS=28, K_NOT_CONTAINS=29, K_IS=30, 
		K_NOT=31, K_IS_NOT=32, K_IN=33, K_ANY=34, K_ALL=35, LIST_OP=36, ANY_OP=37, 
		ALL_OP=38, ID=39, NUMERIC_LITERAL=40, STRING_LITERAL=41, SINGLE_LINE_COMMENT=42, 
		MULTILINE_COMMENT=43, SPACES=44, UNEXPECTED_CHAR=45;
	public static final int
		RULE_parse = 0, RULE_error = 1, RULE_workflow = 2, RULE_stmt_list = 3, 
		RULE_stmt = 4, RULE_default_stmt = 5, RULE_cond = 6, RULE_result_value = 7, 
		RULE_any_name = 8, RULE_literal_value = 9;
	private static String[] makeRuleNames() {
		return new String[] {
			"parse", "error", "workflow", "stmt_list", "stmt", "default_stmt", "cond", 
			"result_value", "any_name", "literal_value"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'workflow'", "'ruleset'", "'end'", "'return'", "'default'", "'{'", 
			"'}'", "'('", "')'", "'.'", "','", "'*'", "'+'", "'-'", "'~'", "'<'", 
			"'<='", "'>'", "'>='", "'='", "'!='", "'<>'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, "DOT", "COMMA", 
			"STAR", "PLUS", "MINUS", "TILDE", "LT", "LT_EQ", "GT", "GT_EQ", "EQ", 
			"NOT_EQ1", "NOT_EQ2", "K_WORKFLOW", "K_END", "K_ELSE", "K_AND", "K_OR", 
			"K_CONTAINS", "K_NOT_CONTAINS", "K_IS", "K_NOT", "K_IS_NOT", "K_IN", 
			"K_ANY", "K_ALL", "LIST_OP", "ANY_OP", "ALL_OP", "ID", "NUMERIC_LITERAL", 
			"STRING_LITERAL", "SINGLE_LINE_COMMENT", "MULTILINE_COMMENT", "SPACES", 
			"UNEXPECTED_CHAR"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "ANA.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ANAParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ParseContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(ANAParser.EOF, 0); }
		public WorkflowContext workflow() {
			return getRuleContext(WorkflowContext.class,0);
		}
		public ErrorContext error() {
			return getRuleContext(ErrorContext.class,0);
		}
		public ParseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parse; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ANAListener ) ((ANAListener)listener).enterParse(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ANAListener ) ((ANAListener)listener).exitParse(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ANAVisitor ) return ((ANAVisitor<? extends T>)visitor).visitParse(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParseContext parse() throws RecognitionException {
		ParseContext _localctx = new ParseContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_parse);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(22);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__0:
				{
				setState(20);
				workflow();
				}
				break;
			case UNEXPECTED_CHAR:
				{
				setState(21);
				error();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(24);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ErrorContext extends ParserRuleContext {
		public Token UNEXPECTED_CHAR;
		public TerminalNode UNEXPECTED_CHAR() { return getToken(ANAParser.UNEXPECTED_CHAR, 0); }
		public ErrorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_error; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ANAListener ) ((ANAListener)listener).enterError(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ANAListener ) ((ANAListener)listener).exitError(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ANAVisitor ) return ((ANAVisitor<? extends T>)visitor).visitError(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ErrorContext error() throws RecognitionException {
		ErrorContext _localctx = new ErrorContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_error);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(26);
			((ErrorContext)_localctx).UNEXPECTED_CHAR = match(UNEXPECTED_CHAR);

			 throw new RuntimeException("UNEXPECTED_CHAR= (((ErrorContext)_localctx).UNEXPECTED_CHAR!=null?((ErrorContext)_localctx).UNEXPECTED_CHAR.getText():null)");

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WorkflowContext extends ParserRuleContext {
		public List<TerminalNode> STRING_LITERAL() { return getTokens(ANAParser.STRING_LITERAL); }
		public TerminalNode STRING_LITERAL(int i) {
			return getToken(ANAParser.STRING_LITERAL, i);
		}
		public Stmt_listContext stmt_list() {
			return getRuleContext(Stmt_listContext.class,0);
		}
		public WorkflowContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_workflow; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ANAListener ) ((ANAListener)listener).enterWorkflow(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ANAListener ) ((ANAListener)listener).exitWorkflow(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ANAVisitor ) return ((ANAVisitor<? extends T>)visitor).visitWorkflow(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WorkflowContext workflow() throws RecognitionException {
		WorkflowContext _localctx = new WorkflowContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_workflow);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(29);
			match(T__0);
			setState(30);
			match(STRING_LITERAL);
			setState(31);
			match(T__1);
			setState(32);
			match(STRING_LITERAL);
			setState(33);
			stmt_list();
			setState(34);
			match(T__2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Stmt_listContext extends ParserRuleContext {
		public Default_stmtContext default_stmt() {
			return getRuleContext(Default_stmtContext.class,0);
		}
		public List<StmtContext> stmt() {
			return getRuleContexts(StmtContext.class);
		}
		public StmtContext stmt(int i) {
			return getRuleContext(StmtContext.class,i);
		}
		public Stmt_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stmt_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ANAListener ) ((ANAListener)listener).enterStmt_list(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ANAListener ) ((ANAListener)listener).exitStmt_list(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ANAVisitor ) return ((ANAVisitor<? extends T>)visitor).visitStmt_list(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Stmt_listContext stmt_list() throws RecognitionException {
		Stmt_listContext _localctx = new Stmt_listContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_stmt_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(39);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LIST_OP || _la==ID) {
				{
				{
				setState(36);
				stmt();
				}
				}
				setState(41);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(42);
			default_stmt();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StmtContext extends ParserRuleContext {
		public CondContext cond() {
			return getRuleContext(CondContext.class,0);
		}
		public Result_valueContext result_value() {
			return getRuleContext(Result_valueContext.class,0);
		}
		public StmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ANAListener ) ((ANAListener)listener).enterStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ANAListener ) ((ANAListener)listener).exitStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ANAVisitor ) return ((ANAVisitor<? extends T>)visitor).visitStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StmtContext stmt() throws RecognitionException {
		StmtContext _localctx = new StmtContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(44);
			cond(0);
			setState(45);
			match(T__3);
			setState(46);
			result_value();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Default_stmtContext extends ParserRuleContext {
		public Result_valueContext result_value() {
			return getRuleContext(Result_valueContext.class,0);
		}
		public Default_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_default_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ANAListener ) ((ANAListener)listener).enterDefault_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ANAListener ) ((ANAListener)listener).exitDefault_stmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ANAVisitor ) return ((ANAVisitor<? extends T>)visitor).visitDefault_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Default_stmtContext default_stmt() throws RecognitionException {
		Default_stmtContext _localctx = new Default_stmtContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_default_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(48);
			match(T__4);
			setState(49);
			match(T__3);
			setState(50);
			result_value();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CondContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(ANAParser.ID, 0); }
		public Literal_valueContext literal_value() {
			return getRuleContext(Literal_valueContext.class,0);
		}
		public TerminalNode LT() { return getToken(ANAParser.LT, 0); }
		public TerminalNode LT_EQ() { return getToken(ANAParser.LT_EQ, 0); }
		public TerminalNode GT() { return getToken(ANAParser.GT, 0); }
		public TerminalNode GT_EQ() { return getToken(ANAParser.GT_EQ, 0); }
		public TerminalNode EQ() { return getToken(ANAParser.EQ, 0); }
		public TerminalNode NOT_EQ1() { return getToken(ANAParser.NOT_EQ1, 0); }
		public TerminalNode NOT_EQ2() { return getToken(ANAParser.NOT_EQ2, 0); }
		public TerminalNode K_IS() { return getToken(ANAParser.K_IS, 0); }
		public TerminalNode K_NOT() { return getToken(ANAParser.K_NOT, 0); }
		public TerminalNode K_IN() { return getToken(ANAParser.K_IN, 0); }
		public TerminalNode LIST_OP() { return getToken(ANAParser.LIST_OP, 0); }
		public List<CondContext> cond() {
			return getRuleContexts(CondContext.class);
		}
		public CondContext cond(int i) {
			return getRuleContext(CondContext.class,i);
		}
		public TerminalNode K_AND() { return getToken(ANAParser.K_AND, 0); }
		public TerminalNode K_OR() { return getToken(ANAParser.K_OR, 0); }
		public CondContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cond; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ANAListener ) ((ANAListener)listener).enterCond(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ANAListener ) ((ANAListener)listener).exitCond(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ANAVisitor ) return ((ANAVisitor<? extends T>)visitor).visitCond(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CondContext cond() throws RecognitionException {
		return cond(0);
	}

	private CondContext cond(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		CondContext _localctx = new CondContext(_ctx, _parentState);
		CondContext _prevctx = _localctx;
		int _startState = 12;
		enterRecursionRule(_localctx, 12, RULE_cond, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(72);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				{
				setState(53);
				match(ID);
				setState(54);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LT) | (1L << LT_EQ) | (1L << GT) | (1L << GT_EQ))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(55);
				literal_value();
				}
				break;
			case 2:
				{
				setState(56);
				match(ID);
				setState(64);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
				case 1:
					{
					setState(57);
					match(EQ);
					}
					break;
				case 2:
					{
					setState(58);
					match(NOT_EQ1);
					}
					break;
				case 3:
					{
					setState(59);
					match(NOT_EQ2);
					}
					break;
				case 4:
					{
					setState(60);
					match(K_IS);
					}
					break;
				case 5:
					{
					setState(61);
					match(K_IS);
					setState(62);
					match(K_NOT);
					}
					break;
				case 6:
					{
					setState(63);
					match(K_IN);
					}
					break;
				}
				setState(66);
				literal_value();
				}
				break;
			case 3:
				{
				setState(67);
				match(LIST_OP);
				setState(68);
				match(T__5);
				setState(69);
				cond(0);
				setState(70);
				match(T__6);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(82);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(80);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
					case 1:
						{
						_localctx = new CondContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_cond);
						setState(74);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(75);
						match(K_AND);
						setState(76);
						cond(3);
						}
						break;
					case 2:
						{
						_localctx = new CondContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_cond);
						setState(77);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(78);
						match(K_OR);
						setState(79);
						cond(2);
						}
						break;
					}
					} 
				}
				setState(84);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class Result_valueContext extends ParserRuleContext {
		public TerminalNode STRING_LITERAL() { return getToken(ANAParser.STRING_LITERAL, 0); }
		public Result_valueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_result_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ANAListener ) ((ANAListener)listener).enterResult_value(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ANAListener ) ((ANAListener)listener).exitResult_value(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ANAVisitor ) return ((ANAVisitor<? extends T>)visitor).visitResult_value(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Result_valueContext result_value() throws RecognitionException {
		Result_valueContext _localctx = new Result_valueContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_result_value);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(85);
			match(STRING_LITERAL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Any_nameContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(ANAParser.ID, 0); }
		public TerminalNode STRING_LITERAL() { return getToken(ANAParser.STRING_LITERAL, 0); }
		public Any_nameContext any_name() {
			return getRuleContext(Any_nameContext.class,0);
		}
		public Any_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_any_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ANAListener ) ((ANAListener)listener).enterAny_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ANAListener ) ((ANAListener)listener).exitAny_name(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ANAVisitor ) return ((ANAVisitor<? extends T>)visitor).visitAny_name(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Any_nameContext any_name() throws RecognitionException {
		Any_nameContext _localctx = new Any_nameContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_any_name);
		try {
			setState(93);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(87);
				match(ID);
				}
				break;
			case STRING_LITERAL:
				enterOuterAlt(_localctx, 2);
				{
				setState(88);
				match(STRING_LITERAL);
				}
				break;
			case T__7:
				enterOuterAlt(_localctx, 3);
				{
				setState(89);
				match(T__7);
				setState(90);
				any_name();
				setState(91);
				match(T__8);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Literal_valueContext extends ParserRuleContext {
		public TerminalNode NUMERIC_LITERAL() { return getToken(ANAParser.NUMERIC_LITERAL, 0); }
		public TerminalNode STRING_LITERAL() { return getToken(ANAParser.STRING_LITERAL, 0); }
		public Literal_valueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ANAListener ) ((ANAListener)listener).enterLiteral_value(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ANAListener ) ((ANAListener)listener).exitLiteral_value(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ANAVisitor ) return ((ANAVisitor<? extends T>)visitor).visitLiteral_value(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Literal_valueContext literal_value() throws RecognitionException {
		Literal_valueContext _localctx = new Literal_valueContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_literal_value);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(95);
			_la = _input.LA(1);
			if ( !(_la==NUMERIC_LITERAL || _la==STRING_LITERAL) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 6:
			return cond_sempred((CondContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean cond_sempred(CondContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 2);
		case 1:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3/d\4\2\t\2\4\3\t\3"+
		"\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\3\2"+
		"\3\2\5\2\31\n\2\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\7"+
		"\5(\n\5\f\5\16\5+\13\5\3\5\3\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\b\3\b"+
		"\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\5\bC\n\b\3\b\3\b\3\b\3\b\3\b"+
		"\3\b\5\bK\n\b\3\b\3\b\3\b\3\b\3\b\3\b\7\bS\n\b\f\b\16\bV\13\b\3\t\3\t"+
		"\3\n\3\n\3\n\3\n\3\n\3\n\5\n`\n\n\3\13\3\13\3\13\2\3\16\f\2\4\6\b\n\f"+
		"\16\20\22\24\2\4\3\2\22\25\3\2*+\2f\2\30\3\2\2\2\4\34\3\2\2\2\6\37\3\2"+
		"\2\2\b)\3\2\2\2\n.\3\2\2\2\f\62\3\2\2\2\16J\3\2\2\2\20W\3\2\2\2\22_\3"+
		"\2\2\2\24a\3\2\2\2\26\31\5\6\4\2\27\31\5\4\3\2\30\26\3\2\2\2\30\27\3\2"+
		"\2\2\31\32\3\2\2\2\32\33\7\2\2\3\33\3\3\2\2\2\34\35\7/\2\2\35\36\b\3\1"+
		"\2\36\5\3\2\2\2\37 \7\3\2\2 !\7+\2\2!\"\7\4\2\2\"#\7+\2\2#$\5\b\5\2$%"+
		"\7\5\2\2%\7\3\2\2\2&(\5\n\6\2\'&\3\2\2\2(+\3\2\2\2)\'\3\2\2\2)*\3\2\2"+
		"\2*,\3\2\2\2+)\3\2\2\2,-\5\f\7\2-\t\3\2\2\2./\5\16\b\2/\60\7\6\2\2\60"+
		"\61\5\20\t\2\61\13\3\2\2\2\62\63\7\7\2\2\63\64\7\6\2\2\64\65\5\20\t\2"+
		"\65\r\3\2\2\2\66\67\b\b\1\2\678\7)\2\289\t\2\2\29K\5\24\13\2:B\7)\2\2"+
		";C\7\26\2\2<C\7\27\2\2=C\7\30\2\2>C\7 \2\2?@\7 \2\2@C\7!\2\2AC\7#\2\2"+
		"B;\3\2\2\2B<\3\2\2\2B=\3\2\2\2B>\3\2\2\2B?\3\2\2\2BA\3\2\2\2CD\3\2\2\2"+
		"DK\5\24\13\2EF\7&\2\2FG\7\b\2\2GH\5\16\b\2HI\7\t\2\2IK\3\2\2\2J\66\3\2"+
		"\2\2J:\3\2\2\2JE\3\2\2\2KT\3\2\2\2LM\f\4\2\2MN\7\34\2\2NS\5\16\b\5OP\f"+
		"\3\2\2PQ\7\35\2\2QS\5\16\b\4RL\3\2\2\2RO\3\2\2\2SV\3\2\2\2TR\3\2\2\2T"+
		"U\3\2\2\2U\17\3\2\2\2VT\3\2\2\2WX\7+\2\2X\21\3\2\2\2Y`\7)\2\2Z`\7+\2\2"+
		"[\\\7\n\2\2\\]\5\22\n\2]^\7\13\2\2^`\3\2\2\2_Y\3\2\2\2_Z\3\2\2\2_[\3\2"+
		"\2\2`\23\3\2\2\2ab\t\3\2\2b\25\3\2\2\2\t\30)BJRT_";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}