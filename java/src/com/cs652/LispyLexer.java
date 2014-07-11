package com.cs652;// Generated from /Users/terence/Dropbox/temp/mm/antlr/Lispy.g4 by ANTLR 4.x
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class LispyLexer extends Lexer {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		IDENT=1, NUMBER=2, BOOLEAN=3, TRUE=4, FALSE=5, LPAREN=6, RPAREN=7, WS=8;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"IDENT", "NUMBER", "BOOLEAN", "'#t'", "'#f'", "'('", "')'", "WS"
	};
	public static final String[] ruleNames = {
		"IDENT", "NUMBER", "BOOLEAN", "SYMBOL", "DIGIT", "TRUE", "FALSE", "CHAR",
		"LPAREN", "RPAREN", "WS"
	};


	public LispyLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Lispy.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\nH\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\3\2\3\2\5\2\34\n\2\3\2\3\2\3\2\7\2!\n\2\f\2\16\2$\13\2\3\3"+
		"\5\3\'\n\3\3\3\6\3*\n\3\r\3\16\3+\3\4\3\4\5\4\60\n\4\3\5\3\5\3\6\3\6\3"+
		"\7\3\7\3\7\3\b\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\6\fC\n\f\r\f\16\f"+
		"D\3\f\3\f\2\2\r\3\3\5\4\7\5\t\2\13\2\r\6\17\7\21\2\23\b\25\t\27\n\3\2"+
		"\7\4\2--//\7\2##,-//\61\61>A\3\2\62;\4\2C\\c|\5\2\13\f\17\17\"\"L\2\3"+
		"\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\23\3\2\2\2"+
		"\2\25\3\2\2\2\2\27\3\2\2\2\3\33\3\2\2\2\5&\3\2\2\2\7/\3\2\2\2\t\61\3\2"+
		"\2\2\13\63\3\2\2\2\r\65\3\2\2\2\178\3\2\2\2\21;\3\2\2\2\23=\3\2\2\2\25"+
		"?\3\2\2\2\27B\3\2\2\2\31\34\5\21\t\2\32\34\5\t\5\2\33\31\3\2\2\2\33\32"+
		"\3\2\2\2\34\"\3\2\2\2\35!\5\13\6\2\36!\5\21\t\2\37!\5\t\5\2 \35\3\2\2"+
		"\2 \36\3\2\2\2 \37\3\2\2\2!$\3\2\2\2\" \3\2\2\2\"#\3\2\2\2#\4\3\2\2\2"+
		"$\"\3\2\2\2%\'\t\2\2\2&%\3\2\2\2&\'\3\2\2\2\')\3\2\2\2(*\5\13\6\2)(\3"+
		"\2\2\2*+\3\2\2\2+)\3\2\2\2+,\3\2\2\2,\6\3\2\2\2-\60\5\r\7\2.\60\5\17\b"+
		"\2/-\3\2\2\2/.\3\2\2\2\60\b\3\2\2\2\61\62\t\3\2\2\62\n\3\2\2\2\63\64\t"+
		"\4\2\2\64\f\3\2\2\2\65\66\7%\2\2\66\67\7v\2\2\67\16\3\2\2\289\7%\2\29"+
		":\7h\2\2:\20\3\2\2\2;<\t\5\2\2<\22\3\2\2\2=>\7*\2\2>\24\3\2\2\2?@\7+\2"+
		"\2@\26\3\2\2\2AC\t\6\2\2BA\3\2\2\2CD\3\2\2\2DB\3\2\2\2DE\3\2\2\2EF\3\2"+
		"\2\2FG\b\f\2\2G\30\3\2\2\2\n\2\33 \"&+/D\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}