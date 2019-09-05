// Generated from /Users/ivanvercinsky/Documents/rappi/fraud-rules-engine/rules-interpreter/src/main/antlr/com/rappi/fraud/analang/ANA.g4 by ANTLR 4.7.2

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
		COUNT=10, DOT=11, COMMA=12, STAR=13, PLUS=14, MINUS=15, TILDE=16, LT=17, 
		LT_EQ=18, GT=19, GT_EQ=20, EQ=21, NOT_EQ1=22, NOT_EQ2=23, K_WORKFLOW=24, 
		K_END=25, K_ELSE=26, K_AND=27, K_OR=28, K_CONTAINS=29, K_NOT_CONTAINS=30, 
		K_IS=31, K_NOT=32, K_IS_NOT=33, K_IN=34, K_ANY=35, K_ALL=36, K_COUNT=37, 
		DEEP_ID=38, ID=39, NUMERIC_LITERAL=40, STRING_LITERAL=41, SINGLE_LINE_COMMENT=42, 
		MULTILINE_COMMENT=43, SPACES=44, UNEXPECTED_CHAR=45;
	public static final int
		RULE_parse = 0, RULE_error = 1, RULE_workflow = 2, RULE_stmt_list = 3, 
		RULE_stmt = 4, RULE_default_stmt = 5, RULE_cond = 6, RULE_list_op = 7, 
		RULE_count = 8, RULE_result_value = 9, RULE_any_name = 10, RULE_literal_value = 11;
	private static String[] makeRuleNames() {
		return new String[] {
			"parse", "error", "workflow", "stmt_list", "stmt", "default_stmt", "cond", 
			"list_op", "count", "result_value", "any_name", "literal_value"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'workflow'", "'ruleset'", "'end'", "'return'", "'default'", "'{'", 
			"'}'", "'('", "')'", null, "'.'", "','", "'*'", "'+'", "'-'", "'~'", 
			"'<'", "'<='", "'>'", "'>='", "'='", "'!='", "'<>'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, "COUNT", 
			"DOT", "COMMA", "STAR", "PLUS", "MINUS", "TILDE", "LT", "LT_EQ", "GT", 
			"GT_EQ", "EQ", "NOT_EQ1", "NOT_EQ2", "K_WORKFLOW", "K_END", "K_ELSE", 
			"K_AND", "K_OR", "K_CONTAINS", "K_NOT_CONTAINS", "K_IS", "K_NOT", "K_IS_NOT", 
			"K_IN", "K_ANY", "K_ALL", "K_COUNT", "DEEP_ID", "ID", "NUMERIC_LITERAL", 
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
			setState(26);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__0:
				{
				setState(24);
				workflow();
				}
				break;
			case UNEXPECTED_CHAR:
				{
				setState(25);
				error();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(28);
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
			setState(30);
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
			setState(33);
			match(T__0);
			setState(34);
			match(STRING_LITERAL);
			setState(35);
			match(T__1);
			setState(36);
			match(STRING_LITERAL);
			setState(37);
			stmt_list();
			setState(38);
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
			setState(43);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << COUNT) | (1L << DEEP_ID) | (1L << ID))) != 0)) {
				{
				{
				setState(40);
				stmt();
				}
				}
				setState(45);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(46);
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
			setState(48);
			cond(0);
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
			setState(52);
			match(T__4);
			setState(53);
			match(T__3);
			setState(54);
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
		public TerminalNode DEEP_ID() { return getToken(ANAParser.DEEP_ID, 0); }
		public TerminalNode NUMERIC_LITERAL() { return getToken(ANAParser.NUMERIC_LITERAL, 0); }
		public TerminalNode LT() { return getToken(ANAParser.LT, 0); }
		public TerminalNode LT_EQ() { return getToken(ANAParser.LT_EQ, 0); }
		public TerminalNode GT() { return getToken(ANAParser.GT, 0); }
		public TerminalNode GT_EQ() { return getToken(ANAParser.GT_EQ, 0); }
		public TerminalNode STRING_LITERAL() { return getToken(ANAParser.STRING_LITERAL, 0); }
		public TerminalNode EQ() { return getToken(ANAParser.EQ, 0); }
		public TerminalNode NOT_EQ1() { return getToken(ANAParser.NOT_EQ1, 0); }
		public TerminalNode NOT_EQ2() { return getToken(ANAParser.NOT_EQ2, 0); }
		public TerminalNode K_IS() { return getToken(ANAParser.K_IS, 0); }
		public TerminalNode K_NOT() { return getToken(ANAParser.K_NOT, 0); }
		public TerminalNode K_IN() { return getToken(ANAParser.K_IN, 0); }
		public TerminalNode ID() { return getToken(ANAParser.ID, 0); }
		public CountContext count() {
			return getRuleContext(CountContext.class,0);
		}
		public List_opContext list_op() {
			return getRuleContext(List_opContext.class,0);
		}
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
			setState(87);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(57);
				match(DEEP_ID);
				setState(58);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LT) | (1L << LT_EQ) | (1L << GT) | (1L << GT_EQ))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(59);
				match(NUMERIC_LITERAL);
				}
				break;
			case 2:
				{
				setState(60);
				match(DEEP_ID);
				setState(68);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
				case 1:
					{
					setState(61);
					match(EQ);
					}
					break;
				case 2:
					{
					setState(62);
					match(NOT_EQ1);
					}
					break;
				case 3:
					{
					setState(63);
					match(NOT_EQ2);
					}
					break;
				case 4:
					{
					setState(64);
					match(K_IS);
					}
					break;
				case 5:
					{
					setState(65);
					match(K_IS);
					setState(66);
					match(K_NOT);
					}
					break;
				case 6:
					{
					setState(67);
					match(K_IN);
					}
					break;
				}
				setState(70);
				match(STRING_LITERAL);
				}
				break;
			case 3:
				{
				setState(71);
				match(ID);
				setState(72);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LT) | (1L << LT_EQ) | (1L << GT) | (1L << GT_EQ))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(73);
				match(NUMERIC_LITERAL);
				}
				break;
			case 4:
				{
				setState(74);
				match(ID);
				setState(82);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
				case 1:
					{
					setState(75);
					match(EQ);
					}
					break;
				case 2:
					{
					setState(76);
					match(NOT_EQ1);
					}
					break;
				case 3:
					{
					setState(77);
					match(NOT_EQ2);
					}
					break;
				case 4:
					{
					setState(78);
					match(K_IS);
					}
					break;
				case 5:
					{
					setState(79);
					match(K_IS);
					setState(80);
					match(K_NOT);
					}
					break;
				case 6:
					{
					setState(81);
					match(K_IN);
					}
					break;
				}
				setState(84);
				match(STRING_LITERAL);
				}
				break;
			case 5:
				{
				setState(85);
				count();
				}
				break;
			case 6:
				{
				setState(86);
				list_op();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(97);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(95);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
					case 1:
						{
						_localctx = new CondContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_cond);
						setState(89);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(90);
						match(K_AND);
						setState(91);
						cond(5);
						}
						break;
					case 2:
						{
						_localctx = new CondContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_cond);
						setState(92);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(93);
						match(K_OR);
						setState(94);
						cond(4);
						}
						break;
					}
					} 
				}
				setState(99);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
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

	public static class List_opContext extends ParserRuleContext {
		public TerminalNode DEEP_ID() { return getToken(ANAParser.DEEP_ID, 0); }
		public TerminalNode DOT() { return getToken(ANAParser.DOT, 0); }
		public TerminalNode K_ANY() { return getToken(ANAParser.K_ANY, 0); }
		public CondContext cond() {
			return getRuleContext(CondContext.class,0);
		}
		public List_opContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_list_op; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ANAListener ) ((ANAListener)listener).enterList_op(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ANAListener ) ((ANAListener)listener).exitList_op(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ANAVisitor ) return ((ANAVisitor<? extends T>)visitor).visitList_op(this);
			else return visitor.visitChildren(this);
		}
	}

	public final List_opContext list_op() throws RecognitionException {
		List_opContext _localctx = new List_opContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_list_op);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(100);
			match(DEEP_ID);
			setState(101);
			match(DOT);
			setState(102);
			match(K_ANY);
			setState(103);
			match(T__5);
			setState(104);
			cond(0);
			setState(105);
			match(T__6);
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

	public static class CountContext extends ParserRuleContext {
		public TerminalNode COUNT() { return getToken(ANAParser.COUNT, 0); }
		public CondContext cond() {
			return getRuleContext(CondContext.class,0);
		}
		public TerminalNode NUMERIC_LITERAL() { return getToken(ANAParser.NUMERIC_LITERAL, 0); }
		public TerminalNode LT() { return getToken(ANAParser.LT, 0); }
		public TerminalNode LT_EQ() { return getToken(ANAParser.LT_EQ, 0); }
		public TerminalNode GT() { return getToken(ANAParser.GT, 0); }
		public TerminalNode GT_EQ() { return getToken(ANAParser.GT_EQ, 0); }
		public CountContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_count; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ANAListener ) ((ANAListener)listener).enterCount(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ANAListener ) ((ANAListener)listener).exitCount(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ANAVisitor ) return ((ANAVisitor<? extends T>)visitor).visitCount(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CountContext count() throws RecognitionException {
		CountContext _localctx = new CountContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_count);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(107);
			match(COUNT);
			setState(108);
			match(T__5);
			setState(109);
			cond(0);
			setState(110);
			match(T__6);
			setState(111);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LT) | (1L << LT_EQ) | (1L << GT) | (1L << GT_EQ))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(112);
			match(NUMERIC_LITERAL);
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
		enterRule(_localctx, 18, RULE_result_value);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(114);
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
		enterRule(_localctx, 20, RULE_any_name);
		try {
			setState(122);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(116);
				match(ID);
				}
				break;
			case STRING_LITERAL:
				enterOuterAlt(_localctx, 2);
				{
				setState(117);
				match(STRING_LITERAL);
				}
				break;
			case T__7:
				enterOuterAlt(_localctx, 3);
				{
				setState(118);
				match(T__7);
				setState(119);
				any_name();
				setState(120);
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
		enterRule(_localctx, 22, RULE_literal_value);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(124);
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
			return precpred(_ctx, 4);
		case 1:
			return precpred(_ctx, 3);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3/\u0081\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\3\2\3\2\5\2\35\n\2\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\3\4\3\5\7\5,\n\5\f\5\16\5/\13\5\3\5\3\5\3\6\3\6\3\6\3\6\3"+
		"\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\5\bG\n"+
		"\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\5\bU\n\b\3\b\3\b\3"+
		"\b\5\bZ\n\b\3\b\3\b\3\b\3\b\3\b\3\b\7\bb\n\b\f\b\16\be\13\b\3\t\3\t\3"+
		"\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\f\3\f\3\f\3"+
		"\f\3\f\3\f\5\f}\n\f\3\r\3\r\3\r\2\3\16\16\2\4\6\b\n\f\16\20\22\24\26\30"+
		"\2\4\3\2\23\26\3\2*+\2\u0089\2\34\3\2\2\2\4 \3\2\2\2\6#\3\2\2\2\b-\3\2"+
		"\2\2\n\62\3\2\2\2\f\66\3\2\2\2\16Y\3\2\2\2\20f\3\2\2\2\22m\3\2\2\2\24"+
		"t\3\2\2\2\26|\3\2\2\2\30~\3\2\2\2\32\35\5\6\4\2\33\35\5\4\3\2\34\32\3"+
		"\2\2\2\34\33\3\2\2\2\35\36\3\2\2\2\36\37\7\2\2\3\37\3\3\2\2\2 !\7/\2\2"+
		"!\"\b\3\1\2\"\5\3\2\2\2#$\7\3\2\2$%\7+\2\2%&\7\4\2\2&\'\7+\2\2\'(\5\b"+
		"\5\2()\7\5\2\2)\7\3\2\2\2*,\5\n\6\2+*\3\2\2\2,/\3\2\2\2-+\3\2\2\2-.\3"+
		"\2\2\2.\60\3\2\2\2/-\3\2\2\2\60\61\5\f\7\2\61\t\3\2\2\2\62\63\5\16\b\2"+
		"\63\64\7\6\2\2\64\65\5\24\13\2\65\13\3\2\2\2\66\67\7\7\2\2\678\7\6\2\2"+
		"89\5\24\13\29\r\3\2\2\2:;\b\b\1\2;<\7(\2\2<=\t\2\2\2=Z\7*\2\2>F\7(\2\2"+
		"?G\7\27\2\2@G\7\30\2\2AG\7\31\2\2BG\7!\2\2CD\7!\2\2DG\7\"\2\2EG\7$\2\2"+
		"F?\3\2\2\2F@\3\2\2\2FA\3\2\2\2FB\3\2\2\2FC\3\2\2\2FE\3\2\2\2GH\3\2\2\2"+
		"HZ\7+\2\2IJ\7)\2\2JK\t\2\2\2KZ\7*\2\2LT\7)\2\2MU\7\27\2\2NU\7\30\2\2O"+
		"U\7\31\2\2PU\7!\2\2QR\7!\2\2RU\7\"\2\2SU\7$\2\2TM\3\2\2\2TN\3\2\2\2TO"+
		"\3\2\2\2TP\3\2\2\2TQ\3\2\2\2TS\3\2\2\2UV\3\2\2\2VZ\7+\2\2WZ\5\22\n\2X"+
		"Z\5\20\t\2Y:\3\2\2\2Y>\3\2\2\2YI\3\2\2\2YL\3\2\2\2YW\3\2\2\2YX\3\2\2\2"+
		"Zc\3\2\2\2[\\\f\6\2\2\\]\7\35\2\2]b\5\16\b\7^_\f\5\2\2_`\7\36\2\2`b\5"+
		"\16\b\6a[\3\2\2\2a^\3\2\2\2be\3\2\2\2ca\3\2\2\2cd\3\2\2\2d\17\3\2\2\2"+
		"ec\3\2\2\2fg\7(\2\2gh\7\r\2\2hi\7%\2\2ij\7\b\2\2jk\5\16\b\2kl\7\t\2\2"+
		"l\21\3\2\2\2mn\7\f\2\2no\7\b\2\2op\5\16\b\2pq\7\t\2\2qr\t\2\2\2rs\7*\2"+
		"\2s\23\3\2\2\2tu\7+\2\2u\25\3\2\2\2v}\7)\2\2w}\7+\2\2xy\7\n\2\2yz\5\26"+
		"\f\2z{\7\13\2\2{}\3\2\2\2|v\3\2\2\2|w\3\2\2\2|x\3\2\2\2}\27\3\2\2\2~\177"+
		"\t\3\2\2\177\31\3\2\2\2\n\34-FTYac|";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}