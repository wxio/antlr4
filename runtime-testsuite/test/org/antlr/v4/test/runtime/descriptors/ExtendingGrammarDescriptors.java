package org.antlr.v4.test.runtime.descriptors;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.test.runtime.BaseCompositeParserTestDescriptor;
import org.antlr.v4.test.runtime.CommentHasStringValue;

public class ExtendingGrammarDescriptors {

	public static class RuleExtendsNonLeftRecursiveDelegate extends BaseCompositeParserTestDescriptor {
		public String input = "b+c";
		public String output = "BX C S.a\n";
		public String errors = null;
		public String startRule = "a";
		public String grammarName = "M";

		/**
grammar M;
import S;
bx options{ extends = b; } : 'c' {<write("\"BX C \"")>};
WS : (' '|'\n') -> skip ;
		 */
		@CommentHasStringValue
		public String grammar = "grammar M;\n" + 
				"import S;\n" + 
				"bx options{ extends = b; } : 'c' {<write(\"\\\"BX C \\\"\")>};\n" + 
				"WS : (' '|'\\n') -> skip ;\n" + 
				"";

/*
grammar M;
a : a '+' b {<write("\"S.a\"")>}
  | 'z'
;
b : 'b' ;
WS : (' '|'\n') -> skip ;
 */
		
		/**
parser grammar S;
a : a '+' b {<write("\"S.a\"")>}
  | 'b'
;
b : 'b' | 'x' ;
WS : (' '|'\n') -> skip ;
		 */
		@CommentHasStringValue
		public String slaveGrammarS="parser grammar S;\n" + 
				"a : a '+' b {<write(\"\\\"S.a\\\"\")>}\n" + 
				"  | 'b'\n" + 
				";\n" + 
				"b : 'b' | 'x' ;\n" + 
				"WS : (' '|'\\n') -> skip ;\n" + 
				"";

		@Override
		public List<Pair<String, String>> getSlaveGrammars() {
			List<Pair<String,String>> slaves = new ArrayList<Pair<String, String>>();
			slaves.add(new Pair<String, String>("S",stringIndentation(slaveGrammarS)));

			return slaves;
		}
	}

	public static class RuleExtendsLeftRecursiveDelegate extends BaseCompositeParserTestDescriptor {
		public String input = "c+b";
		public String output = "BX C S.a\n";
		public String errors = null;
		public String startRule = "a";
		public String grammarName = "M";

		/**
grammar M;
import S;
ax options{ extends = a; } : 'c' {<write("\"BX C \"")>};
WS : (' '|'\n') -> skip ;
		 */
		@CommentHasStringValue
		public String grammar = "grammar M;\n" + 
				"import S;\n" + 
				"ax options{ extends = a; } : 'c' {<write(\"\\\"BX C \\\"\")>};\n" + 
				"WS : (' '|'\\n') -> skip ;\n" + 
				"";

/*
grammar M;
a : a '+' b {<write("\"S.a\"")>}
  | 'z'
;
b : 'b' ;
WS : (' '|'\n') -> skip ;
 */
		
		/**
parser grammar S;
a : a '+' b {<write("\"S.a\"")>}
  | 'b'
;
b : 'b' | 'x' ;
WS : (' '|'\n') -> skip ;
		 */
		@CommentHasStringValue
		public String slaveGrammarS="parser grammar S;\n" + 
				"a : a '+' b {<write(\"\\\"S.a\\\"\")>}\n" + 
				"  | 'b'\n" + 
				";\n" + 
				"b : 'b' | 'x' ;\n" + 
				"WS : (' '|'\\n') -> skip ;\n" + 
				"";

		@Override
		public List<Pair<String, String>> getSlaveGrammars() {
			List<Pair<String,String>> slaves = new ArrayList<Pair<String, String>>();
			slaves.add(new Pair<String, String>("S",stringIndentation(slaveGrammarS)));

			return slaves;
		}
	}
	
	
	public static class DelegatorRuleExtendsDelegate extends BaseCompositeParserTestDescriptor {
		public String input = "c";
		public String output = "S.a\n";
		public String errors = null;
		public String startRule = "a";
		public String grammarName = "M";

		/**
grammar M;
import S;
bx options{ extends = b; } : 'c';
WS : (' '|'\n') -> skip ;
		 */
		@CommentHasStringValue
		public String grammar = "grammar M;\n" + 
				"import S;\n" + 
				"bx options{ extends = b; } : 'c';\n" + 
				"WS : (' '|'\\n') -> skip ;\n" + 
				"";

		/**
		parser grammar S;
		a : b {<write("\"S.a\"")>};
		b : 'b' ;
		 */
		@CommentHasStringValue
		public String slaveGrammarS="parser grammar S;\n" + 
				"a : b {<write(\"\\\"S.a\\\"\")>};\n" + 
				"b : B ;\n" + 
				"";

		@Override
		public List<Pair<String, String>> getSlaveGrammars() {
			List<Pair<String,String>> slaves = new ArrayList<Pair<String, String>>();
			slaves.add(new Pair<String, String>("S",stringIndentation(slaveGrammarS)));

			return slaves;
		}
	}

	public static class RuleToBeExtended extends BaseCompositeParserTestDescriptor {
		public String input = "b";
		public String output = "S.a\n";
		public String errors = null;
		public String startRule = "a";
		public String grammarName = "S";


		/**
grammar S;
a : b {<write("\"S.a\"")>};
b : 'b' # Bs;
		 */
		@CommentHasStringValue
		public String grammar="grammar S;\n" + 
				"a : b {<write(\"\\\"S.a\\\"\")>};\n" + 
				"b : 'b' # Bs;\n" + 
				"";

		@Override
		public List<Pair<String, String>> getSlaveGrammars() {
			return null;
		}
	}
	
	
	public static class DelegatorRuleExtendsNamedDelegate extends BaseCompositeParserTestDescriptor {
		public String input = "c";
		public String output = "S.a\n";
		public String errors = null;
		public String startRule = "a";
		public String grammarName = "M";

		/**
grammar M;
import S;
bx options{ extends = b; } : 'c' #Xs;
WS : (' '|'\n') -> skip ;
		 */
		@CommentHasStringValue
		public String grammar = "grammar M;\n" + 
				"import S;\n" + 
				"bx options{ extends = b; } : 'c' #Xs;\n" + 
				"WS : (' '|'\\n') -> skip ;\n" + 
				"";

		/**
parser grammar S;
a : b {<write("\"S.a\"")>};
b : 'b' # Bs;
		 */
		@CommentHasStringValue
		public String slaveGrammarS="parser grammar S;\n" + 
				"a : b {<write(\"\\\"S.a\\\"\")>};\n" + 
				"b : 'b' # Bs;\n" + 
				"";

		@Override
		public List<Pair<String, String>> getSlaveGrammars() {
			List<Pair<String,String>> slaves = new ArrayList<Pair<String, String>>();
			slaves.add(new Pair<String, String>("S",stringIndentation(slaveGrammarS)));

			return slaves;
		}
	}
	
}
