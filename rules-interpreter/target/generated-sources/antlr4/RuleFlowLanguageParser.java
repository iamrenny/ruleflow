// Generated from RuleFlowLanguage.g4 by ANTLR 4.9.2

package com.github.iamrenny.ruleflow;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class RuleFlowLanguageParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		STRING_NOT_SPECIAL_CHARS=1, DOT=2, COMMA=3, ADD=4, MINUS=5, MULTIPLY=6, 
		DIVIDE=7, LT=8, LT_EQ=9, GT=10, GT_EQ=11, EQ_IC=12, EQ=13, NOT_EQ=14, 
		MINUTE=15, HOUR=16, DAY=17, CURRENT_DATE=18, DATE_DIFF=19, ABS=20, REGEX_STRIP=21, 
		MODULO=22, K_STARTS_WITH=23, K_LIST=24, L_BRACE=25, R_BRACE=26, L_PAREN=27, 
		R_PAREN=28, K_COLON=29, K_ACTION=30, K_WORKFLOW=31, K_RULESET=32, K_RETURN=33, 
		K_THEN=34, K_DEFAULT=35, K_WITH=36, K_END=37, K_ELSE=38, K_AND=39, K_OR=40, 
		K_CONTAINS=41, K_IS=42, K_NOT=43, K_IS_NOT=44, K_IN=45, K_ANY=46, K_NONE=47, 
		K_ALL=48, K_COUNT=49, K_AVERAGE=50, K_DISTINCT=51, K_NULL=52, DAY_OF_WEEK=53, 
		K_EXPR=54, NUMERIC_LITERAL=55, BOOLEAN_LITERAL=56, DQUOTA_STRING=57, SQUOTA_STRING=58, 
		ID=59, SINGLE_LINE_COMMENT=60, MULTILINE_COMMENT=61, SPACES=62, UNEXPECTED_CHAR=63;
	public static final int
		RULE_parse = 0, RULE_error = 1, RULE_workflow = 2, RULE_workflow_name = 3, 
		RULE_string_literal = 4, RULE_rulesets = 5, RULE_rules = 6, RULE_rule_body = 7, 
		RULE_name = 8, RULE_default_clause = 9, RULE_return_result = 10, RULE_state = 11, 
		RULE_actions = 12, RULE_action = 13, RULE_action_params = 14, RULE_param_pairs = 15, 
		RULE_param_pair = 16, RULE_expr = 17, RULE_listElems = 18, RULE_validValue = 19, 
		RULE_validProperty = 20;
	private static String[] makeRuleNames() {
		return new String[] {
			"parse", "error", "workflow", "workflow_name", "string_literal", "rulesets", 
			"rules", "rule_body", "name", "default_clause", "return_result", "state", 
			"actions", "action", "action_params", "param_pairs", "param_pair", "expr", 
			"listElems", "validValue", "validProperty"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, "'.'", "','", "'+'", "'-'", "'*'", "'/'", "'<'", "'<='", 
			"'>'", "'>='", "'='", "'=='", "'<>'", "'minute'", "'hour'", "'day'", 
			null, null, "'abs'", null, null, null, "'list'", "'{'", "'}'", "'('", 
			"')'", "':'", "'action'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "STRING_NOT_SPECIAL_CHARS", "DOT", "COMMA", "ADD", "MINUS", "MULTIPLY", 
			"DIVIDE", "LT", "LT_EQ", "GT", "GT_EQ", "EQ_IC", "EQ", "NOT_EQ", "MINUTE", 
			"HOUR", "DAY", "CURRENT_DATE", "DATE_DIFF", "ABS", "REGEX_STRIP", "MODULO", 
			"K_STARTS_WITH", "K_LIST", "L_BRACE", "R_BRACE", "L_PAREN", "R_PAREN", 
			"K_COLON", "K_ACTION", "K_WORKFLOW", "K_RULESET", "K_RETURN", "K_THEN", 
			"K_DEFAULT", "K_WITH", "K_END", "K_ELSE", "K_AND", "K_OR", "K_CONTAINS", 
			"K_IS", "K_NOT", "K_IS_NOT", "K_IN", "K_ANY", "K_NONE", "K_ALL", "K_COUNT", 
			"K_AVERAGE", "K_DISTINCT", "K_NULL", "DAY_OF_WEEK", "K_EXPR", "NUMERIC_LITERAL", 
			"BOOLEAN_LITERAL", "DQUOTA_STRING", "SQUOTA_STRING", "ID", "SINGLE_LINE_COMMENT", 
			"MULTILINE_COMMENT", "SPACES", "UNEXPECTED_CHAR"
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
	public String getGrammarFileName() { return "RuleFlowLanguage.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public RuleFlowLanguageParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ParseContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(RuleFlowLanguageParser.EOF, 0); }
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
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterParse(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitParse(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitParse(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParseContext parse() throws RecognitionException {
		ParseContext _localctx = new ParseContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_parse);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(44);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case K_WORKFLOW:
				{
				setState(42);
				workflow();
				}
				break;
			case UNEXPECTED_CHAR:
				{
				setState(43);
				error();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(46);
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
		public TerminalNode UNEXPECTED_CHAR() { return getToken(RuleFlowLanguageParser.UNEXPECTED_CHAR, 0); }
		public ErrorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_error; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterError(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitError(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitError(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ErrorContext error() throws RecognitionException {
		ErrorContext _localctx = new ErrorContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_error);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(48);
			((ErrorContext)_localctx).UNEXPECTED_CHAR = match(UNEXPECTED_CHAR);

			    throw new RuntimeException("UNEXPECTED_CHAR= " + (((ErrorContext)_localctx).UNEXPECTED_CHAR!=null?((ErrorContext)_localctx).UNEXPECTED_CHAR.getText():null));

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
		public TerminalNode K_WORKFLOW() { return getToken(RuleFlowLanguageParser.K_WORKFLOW, 0); }
		public Workflow_nameContext workflow_name() {
			return getRuleContext(Workflow_nameContext.class,0);
		}
		public Default_clauseContext default_clause() {
			return getRuleContext(Default_clauseContext.class,0);
		}
		public TerminalNode K_END() { return getToken(RuleFlowLanguageParser.K_END, 0); }
		public List<RulesetsContext> rulesets() {
			return getRuleContexts(RulesetsContext.class);
		}
		public RulesetsContext rulesets(int i) {
			return getRuleContext(RulesetsContext.class,i);
		}
		public WorkflowContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_workflow; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterWorkflow(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitWorkflow(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitWorkflow(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WorkflowContext workflow() throws RecognitionException {
		WorkflowContext _localctx = new WorkflowContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_workflow);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(51);
			match(K_WORKFLOW);
			setState(52);
			workflow_name();
			setState(56);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==K_RULESET) {
				{
				{
				setState(53);
				rulesets();
				}
				}
				setState(58);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(59);
			default_clause();
			setState(60);
			match(K_END);
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

	public static class Workflow_nameContext extends ParserRuleContext {
		public TerminalNode STRING_NOT_SPECIAL_CHARS() { return getToken(RuleFlowLanguageParser.STRING_NOT_SPECIAL_CHARS, 0); }
		public Workflow_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_workflow_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterWorkflow_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitWorkflow_name(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitWorkflow_name(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Workflow_nameContext workflow_name() throws RecognitionException {
		Workflow_nameContext _localctx = new Workflow_nameContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_workflow_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(62);
			match(STRING_NOT_SPECIAL_CHARS);
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

	public static class String_literalContext extends ParserRuleContext {
		public TerminalNode STRING_NOT_SPECIAL_CHARS() { return getToken(RuleFlowLanguageParser.STRING_NOT_SPECIAL_CHARS, 0); }
		public TerminalNode SQUOTA_STRING() { return getToken(RuleFlowLanguageParser.SQUOTA_STRING, 0); }
		public String_literalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_string_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterString_literal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitString_literal(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitString_literal(this);
			else return visitor.visitChildren(this);
		}
	}

	public final String_literalContext string_literal() throws RecognitionException {
		String_literalContext _localctx = new String_literalContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_string_literal);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(64);
			_la = _input.LA(1);
			if ( !(_la==STRING_NOT_SPECIAL_CHARS || _la==SQUOTA_STRING) ) {
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

	public static class RulesetsContext extends ParserRuleContext {
		public TerminalNode K_RULESET() { return getToken(RuleFlowLanguageParser.K_RULESET, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public List<RulesContext> rules() {
			return getRuleContexts(RulesContext.class);
		}
		public RulesContext rules(int i) {
			return getRuleContext(RulesContext.class,i);
		}
		public RulesetsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rulesets; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterRulesets(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitRulesets(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitRulesets(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RulesetsContext rulesets() throws RecognitionException {
		RulesetsContext _localctx = new RulesetsContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_rulesets);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(66);
			match(K_RULESET);
			setState(67);
			name();
			setState(71);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==STRING_NOT_SPECIAL_CHARS || _la==SQUOTA_STRING) {
				{
				{
				setState(68);
				rules();
				}
				}
				setState(73);
				_errHandler.sync(this);
				_la = _input.LA(1);
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

	public static class RulesContext extends ParserRuleContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public Rule_bodyContext rule_body() {
			return getRuleContext(Rule_bodyContext.class,0);
		}
		public RulesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rules; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterRules(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitRules(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitRules(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RulesContext rules() throws RecognitionException {
		RulesContext _localctx = new RulesContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_rules);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(74);
			name();
			setState(75);
			rule_body();
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

	public static class Rule_bodyContext extends ParserRuleContext {
		public ActionsContext then_result;
		public Return_resultContext result;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode K_THEN() { return getToken(RuleFlowLanguageParser.K_THEN, 0); }
		public TerminalNode K_RETURN() { return getToken(RuleFlowLanguageParser.K_RETURN, 0); }
		public ActionsContext actions() {
			return getRuleContext(ActionsContext.class,0);
		}
		public Return_resultContext return_result() {
			return getRuleContext(Return_resultContext.class,0);
		}
		public TerminalNode K_WITH() { return getToken(RuleFlowLanguageParser.K_WITH, 0); }
		public TerminalNode K_AND() { return getToken(RuleFlowLanguageParser.K_AND, 0); }
		public Rule_bodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rule_body; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterRule_body(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitRule_body(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitRule_body(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Rule_bodyContext rule_body() throws RecognitionException {
		Rule_bodyContext _localctx = new Rule_bodyContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_rule_body);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(77);
			expr(0);
			setState(88);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case K_THEN:
				{
				{
				setState(78);
				match(K_THEN);
				setState(80);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
				case 1:
					{
					setState(79);
					_la = _input.LA(1);
					if ( !(_la==K_WITH || _la==K_AND) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					break;
				}
				setState(82);
				((Rule_bodyContext)_localctx).then_result = actions();
				}
				}
				break;
			case K_RETURN:
				{
				{
				setState(83);
				match(K_RETURN);
				setState(84);
				((Rule_bodyContext)_localctx).result = return_result();
				setState(86);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << K_ACTION) | (1L << K_WITH) | (1L << K_AND) | (1L << ID))) != 0)) {
					{
					setState(85);
					actions();
					}
				}

				}
				}
				break;
			default:
				throw new NoViableAltException(this);
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

	public static class NameContext extends ParserRuleContext {
		public String_literalContext string_literal() {
			return getRuleContext(String_literalContext.class,0);
		}
		public NameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NameContext name() throws RecognitionException {
		NameContext _localctx = new NameContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(90);
			string_literal();
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

	public static class Default_clauseContext extends ParserRuleContext {
		public Return_resultContext default_result;
		public TerminalNode K_DEFAULT() { return getToken(RuleFlowLanguageParser.K_DEFAULT, 0); }
		public Return_resultContext return_result() {
			return getRuleContext(Return_resultContext.class,0);
		}
		public ActionsContext actions() {
			return getRuleContext(ActionsContext.class,0);
		}
		public TerminalNode K_THEN() { return getToken(RuleFlowLanguageParser.K_THEN, 0); }
		public TerminalNode K_RETURN() { return getToken(RuleFlowLanguageParser.K_RETURN, 0); }
		public Default_clauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_default_clause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterDefault_clause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitDefault_clause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitDefault_clause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Default_clauseContext default_clause() throws RecognitionException {
		Default_clauseContext _localctx = new Default_clauseContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_default_clause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(92);
			match(K_DEFAULT);
			setState(94);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_RETURN || _la==K_THEN) {
				{
				setState(93);
				_la = _input.LA(1);
				if ( !(_la==K_RETURN || _la==K_THEN) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(96);
			((Default_clauseContext)_localctx).default_result = return_result();
			setState(98);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << K_ACTION) | (1L << K_WITH) | (1L << K_AND) | (1L << ID))) != 0)) {
				{
				setState(97);
				actions();
				}
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

	public static class Return_resultContext extends ParserRuleContext {
		public StateContext state() {
			return getRuleContext(StateContext.class,0);
		}
		public ValidPropertyContext validProperty() {
			return getRuleContext(ValidPropertyContext.class,0);
		}
		public ValidValueContext validValue() {
			return getRuleContext(ValidValueContext.class,0);
		}
		public TerminalNode K_EXPR() { return getToken(RuleFlowLanguageParser.K_EXPR, 0); }
		public TerminalNode L_PAREN() { return getToken(RuleFlowLanguageParser.L_PAREN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode R_PAREN() { return getToken(RuleFlowLanguageParser.R_PAREN, 0); }
		public Return_resultContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_return_result; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterReturn_result(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitReturn_result(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitReturn_result(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Return_resultContext return_result() throws RecognitionException {
		Return_resultContext _localctx = new Return_resultContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_return_result);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(108);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				{
				setState(100);
				state();
				}
				break;
			case 2:
				{
				setState(101);
				validProperty();
				}
				break;
			case 3:
				{
				setState(102);
				validValue();
				}
				break;
			case 4:
				{
				setState(103);
				match(K_EXPR);
				setState(104);
				match(L_PAREN);
				setState(105);
				expr(0);
				setState(106);
				match(R_PAREN);
				}
				break;
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

	public static class StateContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(RuleFlowLanguageParser.ID, 0); }
		public StateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_state; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterState(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitState(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitState(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StateContext state() throws RecognitionException {
		StateContext _localctx = new StateContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_state);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(110);
			match(ID);
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

	public static class ActionsContext extends ParserRuleContext {
		public List<ActionContext> action() {
			return getRuleContexts(ActionContext.class);
		}
		public ActionContext action(int i) {
			return getRuleContext(ActionContext.class,i);
		}
		public List<TerminalNode> K_AND() { return getTokens(RuleFlowLanguageParser.K_AND); }
		public TerminalNode K_AND(int i) {
			return getToken(RuleFlowLanguageParser.K_AND, i);
		}
		public TerminalNode K_WITH() { return getToken(RuleFlowLanguageParser.K_WITH, 0); }
		public ActionsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_actions; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterActions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitActions(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitActions(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ActionsContext actions() throws RecognitionException {
		ActionsContext _localctx = new ActionsContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_actions);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(113);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_WITH || _la==K_AND) {
				{
				setState(112);
				_la = _input.LA(1);
				if ( !(_la==K_WITH || _la==K_AND) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(115);
			action();
			setState(120);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(116);
					match(K_AND);
					setState(117);
					action();
					}
					} 
				}
				setState(122);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
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

	public static class ActionContext extends ParserRuleContext {
		public String_literalContext param_value;
		public Token action_id;
		public TerminalNode K_ACTION() { return getToken(RuleFlowLanguageParser.K_ACTION, 0); }
		public TerminalNode L_PAREN() { return getToken(RuleFlowLanguageParser.L_PAREN, 0); }
		public TerminalNode R_PAREN() { return getToken(RuleFlowLanguageParser.R_PAREN, 0); }
		public String_literalContext string_literal() {
			return getRuleContext(String_literalContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(RuleFlowLanguageParser.COMMA, 0); }
		public Action_paramsContext action_params() {
			return getRuleContext(Action_paramsContext.class,0);
		}
		public TerminalNode ID() { return getToken(RuleFlowLanguageParser.ID, 0); }
		public ActionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_action; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterAction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitAction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitAction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ActionContext action() throws RecognitionException {
		ActionContext _localctx = new ActionContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_action);
		int _la;
		try {
			setState(139);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case K_ACTION:
				enterOuterAlt(_localctx, 1);
				{
				setState(123);
				match(K_ACTION);
				setState(124);
				match(L_PAREN);
				setState(125);
				((ActionContext)_localctx).param_value = string_literal();
				setState(128);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(126);
					match(COMMA);
					setState(127);
					action_params();
					}
				}

				setState(130);
				match(R_PAREN);
				}
				break;
			case ID:
				enterOuterAlt(_localctx, 2);
				{
				setState(132);
				((ActionContext)_localctx).action_id = match(ID);
				setState(137);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==L_PAREN) {
					{
					setState(133);
					match(L_PAREN);
					setState(134);
					action_params();
					setState(135);
					match(R_PAREN);
					}
				}

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

	public static class Action_paramsContext extends ParserRuleContext {
		public TerminalNode L_BRACE() { return getToken(RuleFlowLanguageParser.L_BRACE, 0); }
		public Param_pairsContext param_pairs() {
			return getRuleContext(Param_pairsContext.class,0);
		}
		public TerminalNode R_BRACE() { return getToken(RuleFlowLanguageParser.R_BRACE, 0); }
		public Action_paramsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_action_params; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterAction_params(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitAction_params(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitAction_params(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Action_paramsContext action_params() throws RecognitionException {
		Action_paramsContext _localctx = new Action_paramsContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_action_params);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(141);
			match(L_BRACE);
			setState(142);
			param_pairs();
			setState(143);
			match(R_BRACE);
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

	public static class Param_pairsContext extends ParserRuleContext {
		public List<Param_pairContext> param_pair() {
			return getRuleContexts(Param_pairContext.class);
		}
		public Param_pairContext param_pair(int i) {
			return getRuleContext(Param_pairContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(RuleFlowLanguageParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(RuleFlowLanguageParser.COMMA, i);
		}
		public Param_pairsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_param_pairs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterParam_pairs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitParam_pairs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitParam_pairs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Param_pairsContext param_pairs() throws RecognitionException {
		Param_pairsContext _localctx = new Param_pairsContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_param_pairs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(145);
			param_pair();
			setState(150);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(146);
				match(COMMA);
				setState(147);
				param_pair();
				}
				}
				setState(152);
				_errHandler.sync(this);
				_la = _input.LA(1);
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

	public static class Param_pairContext extends ParserRuleContext {
		public String_literalContext field_name;
		public ValidValueContext field_value;
		public TerminalNode K_COLON() { return getToken(RuleFlowLanguageParser.K_COLON, 0); }
		public String_literalContext string_literal() {
			return getRuleContext(String_literalContext.class,0);
		}
		public ValidValueContext validValue() {
			return getRuleContext(ValidValueContext.class,0);
		}
		public Param_pairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_param_pair; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterParam_pair(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitParam_pair(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitParam_pair(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Param_pairContext param_pair() throws RecognitionException {
		Param_pairContext _localctx = new Param_pairContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_param_pair);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(153);
			((Param_pairContext)_localctx).field_name = string_literal();
			setState(154);
			match(K_COLON);
			setState(155);
			((Param_pairContext)_localctx).field_value = validValue();
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

	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class DateDiffContext extends ExprContext {
		public ExprContext left;
		public ExprContext right;
		public TerminalNode DATE_DIFF() { return getToken(RuleFlowLanguageParser.DATE_DIFF, 0); }
		public TerminalNode L_PAREN() { return getToken(RuleFlowLanguageParser.L_PAREN, 0); }
		public List<TerminalNode> COMMA() { return getTokens(RuleFlowLanguageParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(RuleFlowLanguageParser.COMMA, i);
		}
		public TerminalNode R_PAREN() { return getToken(RuleFlowLanguageParser.R_PAREN, 0); }
		public TerminalNode HOUR() { return getToken(RuleFlowLanguageParser.HOUR, 0); }
		public TerminalNode DAY() { return getToken(RuleFlowLanguageParser.DAY, 0); }
		public TerminalNode MINUTE() { return getToken(RuleFlowLanguageParser.MINUTE, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public DateDiffContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterDateDiff(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitDateDiff(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitDateDiff(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RegexlikeContext extends ExprContext {
		public Token op;
		public ValidPropertyContext value;
		public Token regex;
		public TerminalNode L_PAREN() { return getToken(RuleFlowLanguageParser.L_PAREN, 0); }
		public TerminalNode COMMA() { return getToken(RuleFlowLanguageParser.COMMA, 0); }
		public TerminalNode R_PAREN() { return getToken(RuleFlowLanguageParser.R_PAREN, 0); }
		public TerminalNode REGEX_STRIP() { return getToken(RuleFlowLanguageParser.REGEX_STRIP, 0); }
		public ValidPropertyContext validProperty() {
			return getRuleContext(ValidPropertyContext.class,0);
		}
		public TerminalNode SQUOTA_STRING() { return getToken(RuleFlowLanguageParser.SQUOTA_STRING, 0); }
		public RegexlikeContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterRegexlike(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitRegexlike(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitRegexlike(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BinaryOrContext extends ExprContext {
		public ExprContext left;
		public Token op;
		public ExprContext right;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode K_OR() { return getToken(RuleFlowLanguageParser.K_OR, 0); }
		public BinaryOrContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterBinaryOr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitBinaryOr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitBinaryOr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BinaryAndContext extends ExprContext {
		public ExprContext left;
		public Token op;
		public ExprContext right;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode K_AND() { return getToken(RuleFlowLanguageParser.K_AND, 0); }
		public BinaryAndContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterBinaryAnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitBinaryAnd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitBinaryAnd(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AggregationContext extends ExprContext {
		public ExprContext value;
		public Token op;
		public ExprContext predicate;
		public TerminalNode DOT() { return getToken(RuleFlowLanguageParser.DOT, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode K_COUNT() { return getToken(RuleFlowLanguageParser.K_COUNT, 0); }
		public TerminalNode K_AVERAGE() { return getToken(RuleFlowLanguageParser.K_AVERAGE, 0); }
		public TerminalNode K_ANY() { return getToken(RuleFlowLanguageParser.K_ANY, 0); }
		public TerminalNode K_ALL() { return getToken(RuleFlowLanguageParser.K_ALL, 0); }
		public TerminalNode K_DISTINCT() { return getToken(RuleFlowLanguageParser.K_DISTINCT, 0); }
		public TerminalNode K_NONE() { return getToken(RuleFlowLanguageParser.K_NONE, 0); }
		public TerminalNode L_BRACE() { return getToken(RuleFlowLanguageParser.L_BRACE, 0); }
		public TerminalNode R_BRACE() { return getToken(RuleFlowLanguageParser.R_BRACE, 0); }
		public TerminalNode L_PAREN() { return getToken(RuleFlowLanguageParser.L_PAREN, 0); }
		public TerminalNode R_PAREN() { return getToken(RuleFlowLanguageParser.R_PAREN, 0); }
		public AggregationContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterAggregation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitAggregation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitAggregation(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UnaryContext extends ExprContext {
		public Token op;
		public ExprContext left;
		public TerminalNode L_PAREN() { return getToken(RuleFlowLanguageParser.L_PAREN, 0); }
		public TerminalNode R_PAREN() { return getToken(RuleFlowLanguageParser.R_PAREN, 0); }
		public TerminalNode ABS() { return getToken(RuleFlowLanguageParser.ABS, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public UnaryContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterUnary(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitUnary(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitUnary(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ListContext extends ExprContext {
		public ExprContext value;
		public Token not;
		public Token op;
		public ListElemsContext values;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ListElemsContext listElems() {
			return getRuleContext(ListElemsContext.class,0);
		}
		public TerminalNode K_CONTAINS() { return getToken(RuleFlowLanguageParser.K_CONTAINS, 0); }
		public TerminalNode K_IN() { return getToken(RuleFlowLanguageParser.K_IN, 0); }
		public TerminalNode K_STARTS_WITH() { return getToken(RuleFlowLanguageParser.K_STARTS_WITH, 0); }
		public TerminalNode K_NOT() { return getToken(RuleFlowLanguageParser.K_NOT, 0); }
		public ListContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitList(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ParenthesisContext extends ExprContext {
		public TerminalNode L_PAREN() { return getToken(RuleFlowLanguageParser.L_PAREN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode R_PAREN() { return getToken(RuleFlowLanguageParser.R_PAREN, 0); }
		public ParenthesisContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterParenthesis(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitParenthesis(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitParenthesis(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ComparatorContext extends ExprContext {
		public ExprContext left;
		public Token op;
		public ExprContext right;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode LT() { return getToken(RuleFlowLanguageParser.LT, 0); }
		public TerminalNode LT_EQ() { return getToken(RuleFlowLanguageParser.LT_EQ, 0); }
		public TerminalNode GT() { return getToken(RuleFlowLanguageParser.GT, 0); }
		public TerminalNode GT_EQ() { return getToken(RuleFlowLanguageParser.GT_EQ, 0); }
		public TerminalNode EQ() { return getToken(RuleFlowLanguageParser.EQ, 0); }
		public TerminalNode EQ_IC() { return getToken(RuleFlowLanguageParser.EQ_IC, 0); }
		public TerminalNode NOT_EQ() { return getToken(RuleFlowLanguageParser.NOT_EQ, 0); }
		public ComparatorContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterComparator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitComparator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitComparator(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DayOfWeekContext extends ExprContext {
		public Token op;
		public ExprContext left;
		public TerminalNode L_PAREN() { return getToken(RuleFlowLanguageParser.L_PAREN, 0); }
		public TerminalNode R_PAREN() { return getToken(RuleFlowLanguageParser.R_PAREN, 0); }
		public TerminalNode DAY_OF_WEEK() { return getToken(RuleFlowLanguageParser.DAY_OF_WEEK, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public DayOfWeekContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterDayOfWeek(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitDayOfWeek(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitDayOfWeek(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PropertyContext extends ExprContext {
		public ValidPropertyContext validProperty() {
			return getRuleContext(ValidPropertyContext.class,0);
		}
		public PropertyContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitProperty(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MathAddContext extends ExprContext {
		public ExprContext left;
		public Token op;
		public ExprContext right;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode ADD() { return getToken(RuleFlowLanguageParser.ADD, 0); }
		public TerminalNode MINUS() { return getToken(RuleFlowLanguageParser.MINUS, 0); }
		public MathAddContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterMathAdd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitMathAdd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitMathAdd(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MathMulContext extends ExprContext {
		public ExprContext left;
		public Token op;
		public ExprContext right;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode MULTIPLY() { return getToken(RuleFlowLanguageParser.MULTIPLY, 0); }
		public TerminalNode DIVIDE() { return getToken(RuleFlowLanguageParser.DIVIDE, 0); }
		public TerminalNode MODULO() { return getToken(RuleFlowLanguageParser.MODULO, 0); }
		public MathMulContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterMathMul(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitMathMul(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitMathMul(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ValueContext extends ExprContext {
		public ValidValueContext validValue() {
			return getRuleContext(ValidValueContext.class,0);
		}
		public ValueContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 34;
		enterRecursionRule(_localctx, 34, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(190);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case L_PAREN:
				{
				_localctx = new ParenthesisContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(158);
				match(L_PAREN);
				setState(159);
				expr(0);
				setState(160);
				match(R_PAREN);
				}
				break;
			case DATE_DIFF:
				{
				_localctx = new DateDiffContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(162);
				match(DATE_DIFF);
				setState(163);
				match(L_PAREN);
				setState(164);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << MINUTE) | (1L << HOUR) | (1L << DAY))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(165);
				match(COMMA);
				setState(166);
				((DateDiffContext)_localctx).left = expr(0);
				setState(167);
				match(COMMA);
				setState(168);
				((DateDiffContext)_localctx).right = expr(0);
				setState(169);
				match(R_PAREN);
				}
				break;
			case DAY_OF_WEEK:
				{
				_localctx = new DayOfWeekContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(171);
				((DayOfWeekContext)_localctx).op = match(DAY_OF_WEEK);
				setState(172);
				match(L_PAREN);
				setState(173);
				((DayOfWeekContext)_localctx).left = expr(0);
				setState(174);
				match(R_PAREN);
				}
				break;
			case REGEX_STRIP:
				{
				_localctx = new RegexlikeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(176);
				((RegexlikeContext)_localctx).op = match(REGEX_STRIP);
				setState(177);
				match(L_PAREN);
				setState(178);
				((RegexlikeContext)_localctx).value = validProperty();
				setState(179);
				match(COMMA);
				setState(180);
				((RegexlikeContext)_localctx).regex = match(SQUOTA_STRING);
				setState(181);
				match(R_PAREN);
				}
				break;
			case ABS:
				{
				_localctx = new UnaryContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(183);
				((UnaryContext)_localctx).op = match(ABS);
				setState(184);
				match(L_PAREN);
				setState(185);
				((UnaryContext)_localctx).left = expr(0);
				setState(186);
				match(R_PAREN);
				}
				break;
			case STRING_NOT_SPECIAL_CHARS:
			case CURRENT_DATE:
			case K_NULL:
			case NUMERIC_LITERAL:
			case BOOLEAN_LITERAL:
			case SQUOTA_STRING:
				{
				_localctx = new ValueContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(188);
				validValue();
				}
				break;
			case DOT:
			case ID:
				{
				_localctx = new PropertyContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(189);
				validProperty();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(226);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(224);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
					case 1:
						{
						_localctx = new MathMulContext(new ExprContext(_parentctx, _parentState));
						((MathMulContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(192);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(193);
						((MathMulContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << MULTIPLY) | (1L << DIVIDE) | (1L << MODULO))) != 0)) ) {
							((MathMulContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(194);
						((MathMulContext)_localctx).right = expr(14);
						}
						break;
					case 2:
						{
						_localctx = new MathAddContext(new ExprContext(_parentctx, _parentState));
						((MathAddContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(195);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(196);
						((MathAddContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==ADD || _la==MINUS) ) {
							((MathAddContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(197);
						((MathAddContext)_localctx).right = expr(13);
						}
						break;
					case 3:
						{
						_localctx = new ComparatorContext(new ExprContext(_parentctx, _parentState));
						((ComparatorContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(198);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(199);
						((ComparatorContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LT) | (1L << LT_EQ) | (1L << GT) | (1L << GT_EQ) | (1L << EQ_IC) | (1L << EQ) | (1L << NOT_EQ))) != 0)) ) {
							((ComparatorContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(200);
						((ComparatorContext)_localctx).right = expr(12);
						}
						break;
					case 4:
						{
						_localctx = new BinaryAndContext(new ExprContext(_parentctx, _parentState));
						((BinaryAndContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(201);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(202);
						((BinaryAndContext)_localctx).op = match(K_AND);
						setState(203);
						((BinaryAndContext)_localctx).right = expr(5);
						}
						break;
					case 5:
						{
						_localctx = new BinaryOrContext(new ExprContext(_parentctx, _parentState));
						((BinaryOrContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(204);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(205);
						((BinaryOrContext)_localctx).op = match(K_OR);
						setState(206);
						((BinaryOrContext)_localctx).right = expr(4);
						}
						break;
					case 6:
						{
						_localctx = new ListContext(new ExprContext(_parentctx, _parentState));
						((ListContext)_localctx).value = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(207);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(209);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==K_NOT) {
							{
							setState(208);
							((ListContext)_localctx).not = match(K_NOT);
							}
						}

						setState(211);
						((ListContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << K_STARTS_WITH) | (1L << K_CONTAINS) | (1L << K_IN))) != 0)) ) {
							((ListContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(212);
						((ListContext)_localctx).values = listElems();
						}
						break;
					case 7:
						{
						_localctx = new AggregationContext(new ExprContext(_parentctx, _parentState));
						((AggregationContext)_localctx).value = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(213);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(214);
						match(DOT);
						setState(215);
						((AggregationContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << K_ANY) | (1L << K_NONE) | (1L << K_ALL) | (1L << K_COUNT) | (1L << K_AVERAGE) | (1L << K_DISTINCT))) != 0)) ) {
							((AggregationContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(222);
						_errHandler.sync(this);
						switch (_input.LA(1)) {
						case L_BRACE:
							{
							setState(216);
							match(L_BRACE);
							setState(217);
							((AggregationContext)_localctx).predicate = expr(0);
							setState(218);
							match(R_BRACE);
							}
							break;
						case L_PAREN:
							{
							setState(220);
							match(L_PAREN);
							setState(221);
							match(R_PAREN);
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						}
						break;
					}
					} 
				}
				setState(228);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
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

	public static class ListElemsContext extends ParserRuleContext {
		public Token storedList;
		public String_literalContext literalList;
		public TerminalNode L_PAREN() { return getToken(RuleFlowLanguageParser.L_PAREN, 0); }
		public List<String_literalContext> string_literal() {
			return getRuleContexts(String_literalContext.class);
		}
		public String_literalContext string_literal(int i) {
			return getRuleContext(String_literalContext.class,i);
		}
		public TerminalNode R_PAREN() { return getToken(RuleFlowLanguageParser.R_PAREN, 0); }
		public TerminalNode K_LIST() { return getToken(RuleFlowLanguageParser.K_LIST, 0); }
		public List<TerminalNode> COMMA() { return getTokens(RuleFlowLanguageParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(RuleFlowLanguageParser.COMMA, i);
		}
		public ValidPropertyContext validProperty() {
			return getRuleContext(ValidPropertyContext.class,0);
		}
		public ListElemsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listElems; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterListElems(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitListElems(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitListElems(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ListElemsContext listElems() throws RecognitionException {
		ListElemsContext _localctx = new ListElemsContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_listElems);
		try {
			int _alt;
			setState(243);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case K_LIST:
				enterOuterAlt(_localctx, 1);
				{
				setState(229);
				((ListElemsContext)_localctx).storedList = match(K_LIST);
				setState(230);
				match(L_PAREN);
				setState(231);
				string_literal();
				setState(232);
				match(R_PAREN);
				}
				break;
			case STRING_NOT_SPECIAL_CHARS:
			case SQUOTA_STRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(234);
				((ListElemsContext)_localctx).literalList = string_literal();
				setState(239);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(235);
						match(COMMA);
						setState(236);
						string_literal();
						}
						} 
					}
					setState(241);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
				}
				}
				break;
			case DOT:
			case ID:
				enterOuterAlt(_localctx, 3);
				{
				setState(242);
				validProperty();
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

	public static class ValidValueContext extends ParserRuleContext {
		public String_literalContext string;
		public Token number;
		public Token booleanLiteral;
		public Token nullValue;
		public Token currentDate;
		public String_literalContext string_literal() {
			return getRuleContext(String_literalContext.class,0);
		}
		public TerminalNode NUMERIC_LITERAL() { return getToken(RuleFlowLanguageParser.NUMERIC_LITERAL, 0); }
		public TerminalNode BOOLEAN_LITERAL() { return getToken(RuleFlowLanguageParser.BOOLEAN_LITERAL, 0); }
		public TerminalNode K_NULL() { return getToken(RuleFlowLanguageParser.K_NULL, 0); }
		public TerminalNode CURRENT_DATE() { return getToken(RuleFlowLanguageParser.CURRENT_DATE, 0); }
		public ValidValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_validValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterValidValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitValidValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitValidValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValidValueContext validValue() throws RecognitionException {
		ValidValueContext _localctx = new ValidValueContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_validValue);
		try {
			setState(250);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING_NOT_SPECIAL_CHARS:
			case SQUOTA_STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(245);
				((ValidValueContext)_localctx).string = string_literal();
				}
				break;
			case NUMERIC_LITERAL:
				enterOuterAlt(_localctx, 2);
				{
				setState(246);
				((ValidValueContext)_localctx).number = match(NUMERIC_LITERAL);
				}
				break;
			case BOOLEAN_LITERAL:
				enterOuterAlt(_localctx, 3);
				{
				setState(247);
				((ValidValueContext)_localctx).booleanLiteral = match(BOOLEAN_LITERAL);
				}
				break;
			case K_NULL:
				enterOuterAlt(_localctx, 4);
				{
				setState(248);
				((ValidValueContext)_localctx).nullValue = match(K_NULL);
				}
				break;
			case CURRENT_DATE:
				enterOuterAlt(_localctx, 5);
				{
				setState(249);
				((ValidValueContext)_localctx).currentDate = match(CURRENT_DATE);
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

	public static class ValidPropertyContext extends ParserRuleContext {
		public Token root;
		public Token property;
		public Token nestedProperty;
		public List<TerminalNode> ID() { return getTokens(RuleFlowLanguageParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(RuleFlowLanguageParser.ID, i);
		}
		public List<TerminalNode> DOT() { return getTokens(RuleFlowLanguageParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(RuleFlowLanguageParser.DOT, i);
		}
		public ValidPropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_validProperty; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).enterValidProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RuleFlowLanguageListener ) ((RuleFlowLanguageListener)listener).exitValidProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RuleFlowLanguageVisitor ) return ((RuleFlowLanguageVisitor<? extends T>)visitor).visitValidProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValidPropertyContext validProperty() throws RecognitionException {
		ValidPropertyContext _localctx = new ValidPropertyContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_validProperty);
		int _la;
		try {
			int _alt;
			setState(266);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(253);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==DOT) {
					{
					setState(252);
					((ValidPropertyContext)_localctx).root = match(DOT);
					}
				}

				setState(255);
				((ValidPropertyContext)_localctx).property = match(ID);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(257);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==DOT) {
					{
					setState(256);
					((ValidPropertyContext)_localctx).root = match(DOT);
					}
				}

				setState(259);
				((ValidPropertyContext)_localctx).nestedProperty = match(ID);
				setState(262); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(260);
						match(DOT);
						setState(261);
						match(ID);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(264); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
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
		case 17:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 13);
		case 1:
			return precpred(_ctx, 12);
		case 2:
			return precpred(_ctx, 11);
		case 3:
			return precpred(_ctx, 4);
		case 4:
			return precpred(_ctx, 3);
		case 5:
			return precpred(_ctx, 10);
		case 6:
			return precpred(_ctx, 9);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3A\u010f\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\3\2\3\2\5\2/\n\2\3\2\3\2\3\3"+
		"\3\3\3\3\3\4\3\4\3\4\7\49\n\4\f\4\16\4<\13\4\3\4\3\4\3\4\3\5\3\5\3\6\3"+
		"\6\3\7\3\7\3\7\7\7H\n\7\f\7\16\7K\13\7\3\b\3\b\3\b\3\t\3\t\3\t\5\tS\n"+
		"\t\3\t\3\t\3\t\3\t\5\tY\n\t\5\t[\n\t\3\n\3\n\3\13\3\13\5\13a\n\13\3\13"+
		"\3\13\5\13e\n\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\5\fo\n\f\3\r\3\r\3\16"+
		"\5\16t\n\16\3\16\3\16\3\16\7\16y\n\16\f\16\16\16|\13\16\3\17\3\17\3\17"+
		"\3\17\3\17\5\17\u0083\n\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\5\17\u008c"+
		"\n\17\5\17\u008e\n\17\3\20\3\20\3\20\3\20\3\21\3\21\3\21\7\21\u0097\n"+
		"\21\f\21\16\21\u009a\13\21\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23"+
		"\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23"+
		"\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23"+
		"\5\23\u00c1\n\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23"+
		"\3\23\3\23\3\23\3\23\3\23\3\23\5\23\u00d4\n\23\3\23\3\23\3\23\3\23\3\23"+
		"\3\23\3\23\3\23\3\23\3\23\3\23\5\23\u00e1\n\23\7\23\u00e3\n\23\f\23\16"+
		"\23\u00e6\13\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\7\24\u00f0\n\24"+
		"\f\24\16\24\u00f3\13\24\3\24\5\24\u00f6\n\24\3\25\3\25\3\25\3\25\3\25"+
		"\5\25\u00fd\n\25\3\26\5\26\u0100\n\26\3\26\3\26\5\26\u0104\n\26\3\26\3"+
		"\26\3\26\6\26\u0109\n\26\r\26\16\26\u010a\5\26\u010d\n\26\3\26\3z\3$\27"+
		"\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*\2\13\4\2\3\3<<\4\2&&))"+
		"\3\2#$\3\2\21\23\4\2\b\t\30\30\3\2\6\7\3\2\n\20\5\2\31\31++//\3\2\60\65"+
		"\2\u0124\2.\3\2\2\2\4\62\3\2\2\2\6\65\3\2\2\2\b@\3\2\2\2\nB\3\2\2\2\f"+
		"D\3\2\2\2\16L\3\2\2\2\20O\3\2\2\2\22\\\3\2\2\2\24^\3\2\2\2\26n\3\2\2\2"+
		"\30p\3\2\2\2\32s\3\2\2\2\34\u008d\3\2\2\2\36\u008f\3\2\2\2 \u0093\3\2"+
		"\2\2\"\u009b\3\2\2\2$\u00c0\3\2\2\2&\u00f5\3\2\2\2(\u00fc\3\2\2\2*\u010c"+
		"\3\2\2\2,/\5\6\4\2-/\5\4\3\2.,\3\2\2\2.-\3\2\2\2/\60\3\2\2\2\60\61\7\2"+
		"\2\3\61\3\3\2\2\2\62\63\7A\2\2\63\64\b\3\1\2\64\5\3\2\2\2\65\66\7!\2\2"+
		"\66:\5\b\5\2\679\5\f\7\28\67\3\2\2\29<\3\2\2\2:8\3\2\2\2:;\3\2\2\2;=\3"+
		"\2\2\2<:\3\2\2\2=>\5\24\13\2>?\7\'\2\2?\7\3\2\2\2@A\7\3\2\2A\t\3\2\2\2"+
		"BC\t\2\2\2C\13\3\2\2\2DE\7\"\2\2EI\5\22\n\2FH\5\16\b\2GF\3\2\2\2HK\3\2"+
		"\2\2IG\3\2\2\2IJ\3\2\2\2J\r\3\2\2\2KI\3\2\2\2LM\5\22\n\2MN\5\20\t\2N\17"+
		"\3\2\2\2OZ\5$\23\2PR\7$\2\2QS\t\3\2\2RQ\3\2\2\2RS\3\2\2\2ST\3\2\2\2T["+
		"\5\32\16\2UV\7#\2\2VX\5\26\f\2WY\5\32\16\2XW\3\2\2\2XY\3\2\2\2Y[\3\2\2"+
		"\2ZP\3\2\2\2ZU\3\2\2\2[\21\3\2\2\2\\]\5\n\6\2]\23\3\2\2\2^`\7%\2\2_a\t"+
		"\4\2\2`_\3\2\2\2`a\3\2\2\2ab\3\2\2\2bd\5\26\f\2ce\5\32\16\2dc\3\2\2\2"+
		"de\3\2\2\2e\25\3\2\2\2fo\5\30\r\2go\5*\26\2ho\5(\25\2ij\78\2\2jk\7\35"+
		"\2\2kl\5$\23\2lm\7\36\2\2mo\3\2\2\2nf\3\2\2\2ng\3\2\2\2nh\3\2\2\2ni\3"+
		"\2\2\2o\27\3\2\2\2pq\7=\2\2q\31\3\2\2\2rt\t\3\2\2sr\3\2\2\2st\3\2\2\2"+
		"tu\3\2\2\2uz\5\34\17\2vw\7)\2\2wy\5\34\17\2xv\3\2\2\2y|\3\2\2\2z{\3\2"+
		"\2\2zx\3\2\2\2{\33\3\2\2\2|z\3\2\2\2}~\7 \2\2~\177\7\35\2\2\177\u0082"+
		"\5\n\6\2\u0080\u0081\7\5\2\2\u0081\u0083\5\36\20\2\u0082\u0080\3\2\2\2"+
		"\u0082\u0083\3\2\2\2\u0083\u0084\3\2\2\2\u0084\u0085\7\36\2\2\u0085\u008e"+
		"\3\2\2\2\u0086\u008b\7=\2\2\u0087\u0088\7\35\2\2\u0088\u0089\5\36\20\2"+
		"\u0089\u008a\7\36\2\2\u008a\u008c\3\2\2\2\u008b\u0087\3\2\2\2\u008b\u008c"+
		"\3\2\2\2\u008c\u008e\3\2\2\2\u008d}\3\2\2\2\u008d\u0086\3\2\2\2\u008e"+
		"\35\3\2\2\2\u008f\u0090\7\33\2\2\u0090\u0091\5 \21\2\u0091\u0092\7\34"+
		"\2\2\u0092\37\3\2\2\2\u0093\u0098\5\"\22\2\u0094\u0095\7\5\2\2\u0095\u0097"+
		"\5\"\22\2\u0096\u0094\3\2\2\2\u0097\u009a\3\2\2\2\u0098\u0096\3\2\2\2"+
		"\u0098\u0099\3\2\2\2\u0099!\3\2\2\2\u009a\u0098\3\2\2\2\u009b\u009c\5"+
		"\n\6\2\u009c\u009d\7\37\2\2\u009d\u009e\5(\25\2\u009e#\3\2\2\2\u009f\u00a0"+
		"\b\23\1\2\u00a0\u00a1\7\35\2\2\u00a1\u00a2\5$\23\2\u00a2\u00a3\7\36\2"+
		"\2\u00a3\u00c1\3\2\2\2\u00a4\u00a5\7\25\2\2\u00a5\u00a6\7\35\2\2\u00a6"+
		"\u00a7\t\5\2\2\u00a7\u00a8\7\5\2\2\u00a8\u00a9\5$\23\2\u00a9\u00aa\7\5"+
		"\2\2\u00aa\u00ab\5$\23\2\u00ab\u00ac\7\36\2\2\u00ac\u00c1\3\2\2\2\u00ad"+
		"\u00ae\7\67\2\2\u00ae\u00af\7\35\2\2\u00af\u00b0\5$\23\2\u00b0\u00b1\7"+
		"\36\2\2\u00b1\u00c1\3\2\2\2\u00b2\u00b3\7\27\2\2\u00b3\u00b4\7\35\2\2"+
		"\u00b4\u00b5\5*\26\2\u00b5\u00b6\7\5\2\2\u00b6\u00b7\7<\2\2\u00b7\u00b8"+
		"\7\36\2\2\u00b8\u00c1\3\2\2\2\u00b9\u00ba\7\26\2\2\u00ba\u00bb\7\35\2"+
		"\2\u00bb\u00bc\5$\23\2\u00bc\u00bd\7\36\2\2\u00bd\u00c1\3\2\2\2\u00be"+
		"\u00c1\5(\25\2\u00bf\u00c1\5*\26\2\u00c0\u009f\3\2\2\2\u00c0\u00a4\3\2"+
		"\2\2\u00c0\u00ad\3\2\2\2\u00c0\u00b2\3\2\2\2\u00c0\u00b9\3\2\2\2\u00c0"+
		"\u00be\3\2\2\2\u00c0\u00bf\3\2\2\2\u00c1\u00e4\3\2\2\2\u00c2\u00c3\f\17"+
		"\2\2\u00c3\u00c4\t\6\2\2\u00c4\u00e3\5$\23\20\u00c5\u00c6\f\16\2\2\u00c6"+
		"\u00c7\t\7\2\2\u00c7\u00e3\5$\23\17\u00c8\u00c9\f\r\2\2\u00c9\u00ca\t"+
		"\b\2\2\u00ca\u00e3\5$\23\16\u00cb\u00cc\f\6\2\2\u00cc\u00cd\7)\2\2\u00cd"+
		"\u00e3\5$\23\7\u00ce\u00cf\f\5\2\2\u00cf\u00d0\7*\2\2\u00d0\u00e3\5$\23"+
		"\6\u00d1\u00d3\f\f\2\2\u00d2\u00d4\7-\2\2\u00d3\u00d2\3\2\2\2\u00d3\u00d4"+
		"\3\2\2\2\u00d4\u00d5\3\2\2\2\u00d5\u00d6\t\t\2\2\u00d6\u00e3\5&\24\2\u00d7"+
		"\u00d8\f\13\2\2\u00d8\u00d9\7\4\2\2\u00d9\u00e0\t\n\2\2\u00da\u00db\7"+
		"\33\2\2\u00db\u00dc\5$\23\2\u00dc\u00dd\7\34\2\2\u00dd\u00e1\3\2\2\2\u00de"+
		"\u00df\7\35\2\2\u00df\u00e1\7\36\2\2\u00e0\u00da\3\2\2\2\u00e0\u00de\3"+
		"\2\2\2\u00e1\u00e3\3\2\2\2\u00e2\u00c2\3\2\2\2\u00e2\u00c5\3\2\2\2\u00e2"+
		"\u00c8\3\2\2\2\u00e2\u00cb\3\2\2\2\u00e2\u00ce\3\2\2\2\u00e2\u00d1\3\2"+
		"\2\2\u00e2\u00d7\3\2\2\2\u00e3\u00e6\3\2\2\2\u00e4\u00e2\3\2\2\2\u00e4"+
		"\u00e5\3\2\2\2\u00e5%\3\2\2\2\u00e6\u00e4\3\2\2\2\u00e7\u00e8\7\32\2\2"+
		"\u00e8\u00e9\7\35\2\2\u00e9\u00ea\5\n\6\2\u00ea\u00eb\7\36\2\2\u00eb\u00f6"+
		"\3\2\2\2\u00ec\u00f1\5\n\6\2\u00ed\u00ee\7\5\2\2\u00ee\u00f0\5\n\6\2\u00ef"+
		"\u00ed\3\2\2\2\u00f0\u00f3\3\2\2\2\u00f1\u00ef\3\2\2\2\u00f1\u00f2\3\2"+
		"\2\2\u00f2\u00f6\3\2\2\2\u00f3\u00f1\3\2\2\2\u00f4\u00f6\5*\26\2\u00f5"+
		"\u00e7\3\2\2\2\u00f5\u00ec\3\2\2\2\u00f5\u00f4\3\2\2\2\u00f6\'\3\2\2\2"+
		"\u00f7\u00fd\5\n\6\2\u00f8\u00fd\79\2\2\u00f9\u00fd\7:\2\2\u00fa\u00fd"+
		"\7\66\2\2\u00fb\u00fd\7\24\2\2\u00fc\u00f7\3\2\2\2\u00fc\u00f8\3\2\2\2"+
		"\u00fc\u00f9\3\2\2\2\u00fc\u00fa\3\2\2\2\u00fc\u00fb\3\2\2\2\u00fd)\3"+
		"\2\2\2\u00fe\u0100\7\4\2\2\u00ff\u00fe\3\2\2\2\u00ff\u0100\3\2\2\2\u0100"+
		"\u0101\3\2\2\2\u0101\u010d\7=\2\2\u0102\u0104\7\4\2\2\u0103\u0102\3\2"+
		"\2\2\u0103\u0104\3\2\2\2\u0104\u0105\3\2\2\2\u0105\u0108\7=\2\2\u0106"+
		"\u0107\7\4\2\2\u0107\u0109\7=\2\2\u0108\u0106\3\2\2\2\u0109\u010a\3\2"+
		"\2\2\u010a\u0108\3\2\2\2\u010a\u010b\3\2\2\2\u010b\u010d\3\2\2\2\u010c"+
		"\u00ff\3\2\2\2\u010c\u0103\3\2\2\2\u010d+\3\2\2\2\35.:IRXZ`dnsz\u0082"+
		"\u008b\u008d\u0098\u00c0\u00d3\u00e0\u00e2\u00e4\u00f1\u00f5\u00fc\u00ff"+
		"\u0103\u010a\u010c";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}