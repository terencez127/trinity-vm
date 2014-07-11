package com.cs652;// Generated from /Users/terence/Dropbox/temp/mm/antlr/Lispy.g4 by ANTLR 4.x
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class LispyParser extends Parser {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		IDENT=1, NUMBER=2, BOOLEAN=3, TRUE=4, FALSE=5, LPAREN=6, RPAREN=7, WS=8;
	public static final String[] tokenNames = {
		"<INVALID>", "IDENT", "NUMBER", "BOOLEAN", "'#t'", "'#f'", "'('", "')'",
		"WS"
	};
	public static final int
		RULE_prog = 0, RULE_sexprlist = 1, RULE_sexpr = 2, RULE_sexprIdent = 3,
		RULE_sexprNumber = 4, RULE_sexprBoolean = 5;
	public static final String[] ruleNames = {
		"prog", "sexprlist", "sexpr", "sexprIdent", "sexprNumber", "sexprBoolean"
	};

	@Override
	public String getGrammarFileName() { return "Lispy.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public LispyParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ProgContext extends ParserRuleContext {
		public List<SexprContext> sexpr() {
			return getRuleContexts(SexprContext.class);
		}
		public SexprContext sexpr(int i) {
			return getRuleContext(SexprContext.class,i);
		}
		public ProgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prog; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LispyListener ) ((LispyListener)listener).enterProg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LispyListener ) ((LispyListener)listener).exitProg(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LispyVisitor ) return ((LispyVisitor<? extends T>)visitor).visitProg(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgContext prog() throws RecognitionException {
		ProgContext _localctx = new ProgContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_prog);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(15);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IDENT) | (1L << NUMBER) | (1L << BOOLEAN) | (1L << LPAREN))) != 0)) {
				{
				{
				setState(12); sexpr();
				}
				}
				setState(17);
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

	public static class SexprlistContext extends ParserRuleContext {
		public List<SexprContext> sexpr() {
			return getRuleContexts(SexprContext.class);
		}
		public TerminalNode RPAREN() { return getToken(LispyParser.RPAREN, 0); }
		public SexprContext sexpr(int i) {
			return getRuleContext(SexprContext.class,i);
		}
		public TerminalNode LPAREN() { return getToken(LispyParser.LPAREN, 0); }
		public SexprlistContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sexprlist; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LispyListener ) ((LispyListener)listener).enterSexprlist(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LispyListener ) ((LispyListener)listener).exitSexprlist(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LispyVisitor ) return ((LispyVisitor<? extends T>)visitor).visitSexprlist(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SexprlistContext sexprlist() throws RecognitionException {
		SexprlistContext _localctx = new SexprlistContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_sexprlist);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(18); match(LPAREN);
			setState(20);
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(19); sexpr();
				}
				}
				setState(22);
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IDENT) | (1L << NUMBER) | (1L << BOOLEAN) | (1L << LPAREN))) != 0) );
			setState(24); match(RPAREN);
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

	public static class SexprContext extends ParserRuleContext {
		public SexprlistContext sexprlist() {
			return getRuleContext(SexprlistContext.class,0);
		}
		public SexprIdentContext sexprIdent() {
			return getRuleContext(SexprIdentContext.class,0);
		}
		public SexprNumberContext sexprNumber() {
			return getRuleContext(SexprNumberContext.class,0);
		}
		public SexprBooleanContext sexprBoolean() {
			return getRuleContext(SexprBooleanContext.class,0);
		}
		public SexprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sexpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LispyListener ) ((LispyListener)listener).enterSexpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LispyListener ) ((LispyListener)listener).exitSexpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LispyVisitor ) return ((LispyVisitor<? extends T>)visitor).visitSexpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SexprContext sexpr() throws RecognitionException {
		SexprContext _localctx = new SexprContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_sexpr);
		try {
			setState(30);
			switch (_input.LA(1)) {
			case BOOLEAN:
				enterOuterAlt(_localctx, 1);
				{
				setState(26); sexprBoolean();
				}
				break;
			case IDENT:
				enterOuterAlt(_localctx, 2);
				{
				setState(27); sexprIdent();
				}
				break;
			case NUMBER:
				enterOuterAlt(_localctx, 3);
				{
				setState(28); sexprNumber();
				}
				break;
			case LPAREN:
				enterOuterAlt(_localctx, 4);
				{
				setState(29); sexprlist();
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

	public static class SexprIdentContext extends ParserRuleContext {
		public TerminalNode IDENT() { return getToken(LispyParser.IDENT, 0); }
		public SexprIdentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sexprIdent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LispyListener ) ((LispyListener)listener).enterSexprIdent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LispyListener ) ((LispyListener)listener).exitSexprIdent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LispyVisitor ) return ((LispyVisitor<? extends T>)visitor).visitSexprIdent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SexprIdentContext sexprIdent() throws RecognitionException {
		SexprIdentContext _localctx = new SexprIdentContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_sexprIdent);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(32); match(IDENT);
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

	public static class SexprNumberContext extends ParserRuleContext {
		public TerminalNode NUMBER() { return getToken(LispyParser.NUMBER, 0); }
		public SexprNumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sexprNumber; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LispyListener ) ((LispyListener)listener).enterSexprNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LispyListener ) ((LispyListener)listener).exitSexprNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LispyVisitor ) return ((LispyVisitor<? extends T>)visitor).visitSexprNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SexprNumberContext sexprNumber() throws RecognitionException {
		SexprNumberContext _localctx = new SexprNumberContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_sexprNumber);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(34); match(NUMBER);
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

	public static class SexprBooleanContext extends ParserRuleContext {
		public TerminalNode BOOLEAN() { return getToken(LispyParser.BOOLEAN, 0); }
		public SexprBooleanContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sexprBoolean; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LispyListener ) ((LispyListener)listener).enterSexprBoolean(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LispyListener ) ((LispyListener)listener).exitSexprBoolean(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LispyVisitor ) return ((LispyVisitor<? extends T>)visitor).visitSexprBoolean(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SexprBooleanContext sexprBoolean() throws RecognitionException {
		SexprBooleanContext _localctx = new SexprBooleanContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_sexprBoolean);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(36); match(BOOLEAN);
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

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\n)\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\3\2\7\2\20\n\2\f\2\16\2\23\13\2\3\3"+
		"\3\3\6\3\27\n\3\r\3\16\3\30\3\3\3\3\3\4\3\4\3\4\3\4\5\4!\n\4\3\5\3\5\3"+
		"\6\3\6\3\7\3\7\3\7\2\2\b\2\4\6\b\n\f\2\2\'\2\21\3\2\2\2\4\24\3\2\2\2\6"+
		" \3\2\2\2\b\"\3\2\2\2\n$\3\2\2\2\f&\3\2\2\2\16\20\5\6\4\2\17\16\3\2\2"+
		"\2\20\23\3\2\2\2\21\17\3\2\2\2\21\22\3\2\2\2\22\3\3\2\2\2\23\21\3\2\2"+
		"\2\24\26\7\b\2\2\25\27\5\6\4\2\26\25\3\2\2\2\27\30\3\2\2\2\30\26\3\2\2"+
		"\2\30\31\3\2\2\2\31\32\3\2\2\2\32\33\7\t\2\2\33\5\3\2\2\2\34!\5\f\7\2"+
		"\35!\5\b\5\2\36!\5\n\6\2\37!\5\4\3\2 \34\3\2\2\2 \35\3\2\2\2 \36\3\2\2"+
		"\2 \37\3\2\2\2!\7\3\2\2\2\"#\7\3\2\2#\t\3\2\2\2$%\7\4\2\2%\13\3\2\2\2"+
		"&\'\7\5\2\2\'\r\3\2\2\2\5\21\30 ";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}