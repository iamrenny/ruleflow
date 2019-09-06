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
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, DOT=8, COMMA=9, 
		STAR=10, PLUS=11, MINUS=12, TILDE=13, LT=14, LT_EQ=15, GT=16, GT_EQ=17, 
		EQ=18, NOT_EQ1=19, NOT_EQ2=20, R_BRACE=21, L_BRACE=22, K_WORKFLOW=23, 
		K_END=24, K_ELSE=25, K_AND=26, K_OR=27, K_CONTAINS=28, K_NOT_CONTAINS=29, 
		K_IS=30, K_NOT=31, K_IS_NOT=32, K_IN=33, K_ANY=34, K_ALL=35, K_COUNT=36, 
		K_AVERAGE=37, ID=38, NUMERIC_LITERAL=39, STRING_LITERAL=40, SINGLE_LINE_COMMENT=41, 
		MULTILINE_COMMENT=42, SPACES=43, UNEXPECTED_CHAR=44;
	public static final int
		RULE_parse = 0, RULE_error = 1, RULE_workflow = 2, RULE_stmt_list = 3, 
		RULE_stmt = 4, RULE_default_stmt = 5, RULE_cond = 6, RULE_id = 7, RULE_list_op = 8, 
		RULE_count = 9, RULE_average = 10, RULE_result_value = 11, RULE_any_name = 12, 
		RULE_literal_value = 13;
	private static String[] makeRuleNames() {
		return new String[] {
			"parse", "error", "workflow", "stmt_list", "stmt", "default_stmt", "cond", 
			"id", "list_op", "count", "average", "result_value", "any_name", "literal_value"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'workflow'", "'ruleset'", "'end'", "'return'", "'default'", "'('", 
			"')'", "'.'", "','", "'*'", "'+'", "'-'", "'~'", "'<'", "'<='", "'>'", 
			"'>='", "'='", "'!='", "'<>'", "'{'", "'}'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, "DOT", "COMMA", "STAR", 
			"PLUS", "MINUS", "TILDE", "LT", "LT_EQ", "GT", "GT_EQ", "EQ", "NOT_EQ1", 
			"NOT_EQ2", "R_BRACE", "L_BRACE", "K_WORKFLOW", "K_END", "K_ELSE", "K_AND", 
			"K_OR", "K_CONTAINS", "K_NOT_CONTAINS", "K_IS", "K_NOT", "K_IS_NOT", 
			"K_IN", "K_ANY", "K_ALL", "K_COUNT", "K_AVERAGE", "ID", "NUMERIC_LITERAL", 
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
			setState(30);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__0:
				{
				setState(28);
				workflow();
				}
				break;
			case UNEXPECTED_CHAR:
				{
				setState(29);
				error();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(32);
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
			setState(34);
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
			setState(37);
			match(T__0);
			setState(38);
			match(STRING_LITERAL);
			setState(39);
			match(T__1);
			setState(40);
			match(STRING_LITERAL);
			setState(41);
			stmt_list();
			setState(42);
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
			setState(47);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==ID) {
				{
				{
				setState(44);
				stmt();
				}
				}
				setState(49);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(50);
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
			setState(52);
			cond(0);
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
			setState(56);
			match(T__4);
			setState(57);
			match(T__3);
			setState(58);
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
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
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
		public List_opContext list_op() {
			return getRuleContext(List_opContext.class,0);
		}
		public CountContext count() {
			return getRuleContext(CountContext.class,0);
		}
		public AverageContext average() {
			return getRuleContext(AverageContext.class,0);
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
			setState(94);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(61);
				id();
				setState(62);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LT) | (1L << LT_EQ) | (1L << GT) | (1L << GT_EQ))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(63);
				match(NUMERIC_LITERAL);
				}
				break;
			case 2:
				{
				setState(65);
				id();
				setState(73);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
				case 1:
					{
					setState(66);
					match(EQ);
					}
					break;
				case 2:
					{
					setState(67);
					match(NOT_EQ1);
					}
					break;
				case 3:
					{
					setState(68);
					match(NOT_EQ2);
					}
					break;
				case 4:
					{
					setState(69);
					match(K_IS);
					}
					break;
				case 5:
					{
					setState(70);
					match(K_IS);
					setState(71);
					match(K_NOT);
					}
					break;
				case 6:
					{
					setState(72);
					match(K_IN);
					}
					break;
				}
				setState(75);
				match(STRING_LITERAL);
				}
				break;
			case 3:
				{
				setState(77);
				match(ID);
				setState(78);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LT) | (1L << LT_EQ) | (1L << GT) | (1L << GT_EQ))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(79);
				match(NUMERIC_LITERAL);
				}
				break;
			case 4:
				{
				setState(80);
				match(ID);
				setState(88);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
				case 1:
					{
					setState(81);
					match(EQ);
					}
					break;
				case 2:
					{
					setState(82);
					match(NOT_EQ1);
					}
					break;
				case 3:
					{
					setState(83);
					match(NOT_EQ2);
					}
					break;
				case 4:
					{
					setState(84);
					match(K_IS);
					}
					break;
				case 5:
					{
					setState(85);
					match(K_IS);
					setState(86);
					match(K_NOT);
					}
					break;
				case 6:
					{
					setState(87);
					match(K_IN);
					}
					break;
				}
				setState(90);
				match(STRING_LITERAL);
				}
				break;
			case 5:
				{
				setState(91);
				list_op();
				}
				break;
			case 6:
				{
				setState(92);
				count();
				}
				break;
			case 7:
				{
				setState(93);
				average();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(104);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(102);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
					case 1:
						{
						_localctx = new CondContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_cond);
						setState(96);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(97);
						match(K_AND);
						setState(98);
						cond(6);
						}
						break;
					case 2:
						{
						_localctx = new CondContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_cond);
						setState(99);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(100);
						match(K_OR);
						setState(101);
						cond(5);
						}
						break;
					}
					} 
				}
				setState(106);
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

	public static class IdContext extends ParserRuleContext {
		public List<TerminalNode> ID() { return getTokens(ANAParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(ANAParser.ID, i);
		}
		public List<TerminalNode> DOT() { return getTokens(ANAParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(ANAParser.DOT, i);
		}
		public IdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_id; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ANAListener ) ((ANAListener)listener).enterId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ANAListener ) ((ANAListener)listener).exitId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ANAVisitor ) return ((ANAVisitor<? extends T>)visitor).visitId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdContext id() throws RecognitionException {
		IdContext _localctx = new IdContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_id);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(107);
			match(ID);
			setState(110); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(108);
					match(DOT);
					setState(109);
					match(ID);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(112); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
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

	public static class List_opContext extends ParserRuleContext {
		public TerminalNode DOT() { return getToken(ANAParser.DOT, 0); }
		public TerminalNode R_BRACE() { return getToken(ANAParser.R_BRACE, 0); }
		public CondContext cond() {
			return getRuleContext(CondContext.class,0);
		}
		public TerminalNode L_BRACE() { return getToken(ANAParser.L_BRACE, 0); }
		public TerminalNode K_ANY() { return getToken(ANAParser.K_ANY, 0); }
		public TerminalNode K_ALL() { return getToken(ANAParser.K_ALL, 0); }
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public TerminalNode ID() { return getToken(ANAParser.ID, 0); }
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
		enterRule(_localctx, 16, RULE_list_op);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(116);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				{
				setState(114);
				id();
				}
				break;
			case 2:
				{
				setState(115);
				match(ID);
				}
				break;
			}
			setState(118);
			match(DOT);
			setState(119);
			_la = _input.LA(1);
			if ( !(_la==K_ANY || _la==K_ALL) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(120);
			match(R_BRACE);
			setState(121);
			cond(0);
			setState(122);
			match(L_BRACE);
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
		public TerminalNode DOT() { return getToken(ANAParser.DOT, 0); }
		public TerminalNode K_COUNT() { return getToken(ANAParser.K_COUNT, 0); }
		public TerminalNode R_BRACE() { return getToken(ANAParser.R_BRACE, 0); }
		public CondContext cond() {
			return getRuleContext(CondContext.class,0);
		}
		public TerminalNode L_BRACE() { return getToken(ANAParser.L_BRACE, 0); }
		public TerminalNode NUMERIC_LITERAL() { return getToken(ANAParser.NUMERIC_LITERAL, 0); }
		public TerminalNode LT() { return getToken(ANAParser.LT, 0); }
		public TerminalNode LT_EQ() { return getToken(ANAParser.LT_EQ, 0); }
		public TerminalNode GT() { return getToken(ANAParser.GT, 0); }
		public TerminalNode GT_EQ() { return getToken(ANAParser.GT_EQ, 0); }
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public TerminalNode ID() { return getToken(ANAParser.ID, 0); }
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
		enterRule(_localctx, 18, RULE_count);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(126);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				{
				setState(124);
				id();
				}
				break;
			case 2:
				{
				setState(125);
				match(ID);
				}
				break;
			}
			setState(128);
			match(DOT);
			setState(129);
			match(K_COUNT);
			setState(130);
			match(R_BRACE);
			setState(131);
			cond(0);
			setState(132);
			match(L_BRACE);
			setState(133);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LT) | (1L << LT_EQ) | (1L << GT) | (1L << GT_EQ))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(134);
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

	public static class AverageContext extends ParserRuleContext {
		public TerminalNode DOT() { return getToken(ANAParser.DOT, 0); }
		public TerminalNode K_AVERAGE() { return getToken(ANAParser.K_AVERAGE, 0); }
		public TerminalNode R_BRACE() { return getToken(ANAParser.R_BRACE, 0); }
		public CondContext cond() {
			return getRuleContext(CondContext.class,0);
		}
		public TerminalNode L_BRACE() { return getToken(ANAParser.L_BRACE, 0); }
		public TerminalNode NUMERIC_LITERAL() { return getToken(ANAParser.NUMERIC_LITERAL, 0); }
		public TerminalNode LT() { return getToken(ANAParser.LT, 0); }
		public TerminalNode LT_EQ() { return getToken(ANAParser.LT_EQ, 0); }
		public TerminalNode GT() { return getToken(ANAParser.GT, 0); }
		public TerminalNode GT_EQ() { return getToken(ANAParser.GT_EQ, 0); }
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public TerminalNode ID() { return getToken(ANAParser.ID, 0); }
		public AverageContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_average; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ANAListener ) ((ANAListener)listener).enterAverage(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ANAListener ) ((ANAListener)listener).exitAverage(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ANAVisitor ) return ((ANAVisitor<? extends T>)visitor).visitAverage(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AverageContext average() throws RecognitionException {
		AverageContext _localctx = new AverageContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_average);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(138);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(136);
				id();
				}
				break;
			case 2:
				{
				setState(137);
				match(ID);
				}
				break;
			}
			setState(140);
			match(DOT);
			setState(141);
			match(K_AVERAGE);
			setState(142);
			match(R_BRACE);
			setState(143);
			cond(0);
			setState(144);
			match(L_BRACE);
			setState(145);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LT) | (1L << LT_EQ) | (1L << GT) | (1L << GT_EQ))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(146);
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
		enterRule(_localctx, 22, RULE_result_value);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(148);
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
		enterRule(_localctx, 24, RULE_any_name);
		try {
			setState(156);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(150);
				match(ID);
				}
				break;
			case STRING_LITERAL:
				enterOuterAlt(_localctx, 2);
				{
				setState(151);
				match(STRING_LITERAL);
				}
				break;
			case T__5:
				enterOuterAlt(_localctx, 3);
				{
				setState(152);
				match(T__5);
				setState(153);
				any_name();
				setState(154);
				match(T__6);
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
		enterRule(_localctx, 26, RULE_literal_value);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(158);
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
			return precpred(_ctx, 5);
		case 1:
			return precpred(_ctx, 4);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3.\u00a3\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\3\2\3\2\5\2!\n\2\3\2\3\2\3\3\3"+
		"\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\7\5\60\n\5\f\5\16\5\63\13\5\3\5"+
		"\3\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3"+
		"\b\3\b\3\b\3\b\3\b\5\bL\n\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3"+
		"\b\3\b\3\b\5\b[\n\b\3\b\3\b\3\b\3\b\5\ba\n\b\3\b\3\b\3\b\3\b\3\b\3\b\7"+
		"\bi\n\b\f\b\16\bl\13\b\3\t\3\t\3\t\6\tq\n\t\r\t\16\tr\3\n\3\n\5\nw\n\n"+
		"\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\5\13\u0081\n\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\3\13\3\f\3\f\5\f\u008d\n\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f"+
		"\3\f\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\5\16\u009f\n\16\3\17\3\17\3"+
		"\17\2\3\16\20\2\4\6\b\n\f\16\20\22\24\26\30\32\34\2\5\3\2\20\23\3\2$%"+
		"\3\2)*\2\u00ae\2 \3\2\2\2\4$\3\2\2\2\6\'\3\2\2\2\b\61\3\2\2\2\n\66\3\2"+
		"\2\2\f:\3\2\2\2\16`\3\2\2\2\20m\3\2\2\2\22v\3\2\2\2\24\u0080\3\2\2\2\26"+
		"\u008c\3\2\2\2\30\u0096\3\2\2\2\32\u009e\3\2\2\2\34\u00a0\3\2\2\2\36!"+
		"\5\6\4\2\37!\5\4\3\2 \36\3\2\2\2 \37\3\2\2\2!\"\3\2\2\2\"#\7\2\2\3#\3"+
		"\3\2\2\2$%\7.\2\2%&\b\3\1\2&\5\3\2\2\2\'(\7\3\2\2()\7*\2\2)*\7\4\2\2*"+
		"+\7*\2\2+,\5\b\5\2,-\7\5\2\2-\7\3\2\2\2.\60\5\n\6\2/.\3\2\2\2\60\63\3"+
		"\2\2\2\61/\3\2\2\2\61\62\3\2\2\2\62\64\3\2\2\2\63\61\3\2\2\2\64\65\5\f"+
		"\7\2\65\t\3\2\2\2\66\67\5\16\b\2\678\7\6\2\289\5\30\r\29\13\3\2\2\2:;"+
		"\7\7\2\2;<\7\6\2\2<=\5\30\r\2=\r\3\2\2\2>?\b\b\1\2?@\5\20\t\2@A\t\2\2"+
		"\2AB\7)\2\2Ba\3\2\2\2CK\5\20\t\2DL\7\24\2\2EL\7\25\2\2FL\7\26\2\2GL\7"+
		" \2\2HI\7 \2\2IL\7!\2\2JL\7#\2\2KD\3\2\2\2KE\3\2\2\2KF\3\2\2\2KG\3\2\2"+
		"\2KH\3\2\2\2KJ\3\2\2\2LM\3\2\2\2MN\7*\2\2Na\3\2\2\2OP\7(\2\2PQ\t\2\2\2"+
		"Qa\7)\2\2RZ\7(\2\2S[\7\24\2\2T[\7\25\2\2U[\7\26\2\2V[\7 \2\2WX\7 \2\2"+
		"X[\7!\2\2Y[\7#\2\2ZS\3\2\2\2ZT\3\2\2\2ZU\3\2\2\2ZV\3\2\2\2ZW\3\2\2\2Z"+
		"Y\3\2\2\2[\\\3\2\2\2\\a\7*\2\2]a\5\22\n\2^a\5\24\13\2_a\5\26\f\2`>\3\2"+
		"\2\2`C\3\2\2\2`O\3\2\2\2`R\3\2\2\2`]\3\2\2\2`^\3\2\2\2`_\3\2\2\2aj\3\2"+
		"\2\2bc\f\7\2\2cd\7\34\2\2di\5\16\b\bef\f\6\2\2fg\7\35\2\2gi\5\16\b\7h"+
		"b\3\2\2\2he\3\2\2\2il\3\2\2\2jh\3\2\2\2jk\3\2\2\2k\17\3\2\2\2lj\3\2\2"+
		"\2mp\7(\2\2no\7\n\2\2oq\7(\2\2pn\3\2\2\2qr\3\2\2\2rp\3\2\2\2rs\3\2\2\2"+
		"s\21\3\2\2\2tw\5\20\t\2uw\7(\2\2vt\3\2\2\2vu\3\2\2\2wx\3\2\2\2xy\7\n\2"+
		"\2yz\t\3\2\2z{\7\27\2\2{|\5\16\b\2|}\7\30\2\2}\23\3\2\2\2~\u0081\5\20"+
		"\t\2\177\u0081\7(\2\2\u0080~\3\2\2\2\u0080\177\3\2\2\2\u0081\u0082\3\2"+
		"\2\2\u0082\u0083\7\n\2\2\u0083\u0084\7&\2\2\u0084\u0085\7\27\2\2\u0085"+
		"\u0086\5\16\b\2\u0086\u0087\7\30\2\2\u0087\u0088\t\2\2\2\u0088\u0089\7"+
		")\2\2\u0089\25\3\2\2\2\u008a\u008d\5\20\t\2\u008b\u008d\7(\2\2\u008c\u008a"+
		"\3\2\2\2\u008c\u008b\3\2\2\2\u008d\u008e\3\2\2\2\u008e\u008f\7\n\2\2\u008f"+
		"\u0090\7\'\2\2\u0090\u0091\7\27\2\2\u0091\u0092\5\16\b\2\u0092\u0093\7"+
		"\30\2\2\u0093\u0094\t\2\2\2\u0094\u0095\7)\2\2\u0095\27\3\2\2\2\u0096"+
		"\u0097\7*\2\2\u0097\31\3\2\2\2\u0098\u009f\7(\2\2\u0099\u009f\7*\2\2\u009a"+
		"\u009b\7\b\2\2\u009b\u009c\5\32\16\2\u009c\u009d\7\t\2\2\u009d\u009f\3"+
		"\2\2\2\u009e\u0098\3\2\2\2\u009e\u0099\3\2\2\2\u009e\u009a\3\2\2\2\u009f"+
		"\33\3\2\2\2\u00a0\u00a1\t\4\2\2\u00a1\35\3\2\2\2\16 \61KZ`hjrv\u0080\u008c"+
		"\u009e";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}