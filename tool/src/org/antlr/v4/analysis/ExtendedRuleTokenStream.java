package org.antlr.v4.analysis;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.Token;
import org.antlr.runtime.TokenSource;
import org.antlr.runtime.TokenStream;

/** A stream of tokens accessing tokens from a TokenSource */
public class ExtendedRuleTokenStream implements TokenStream {

	int offset;
	List<Token> tokens = new ArrayList<Token>();
	int pointer = 0;
	int lastMarker;
	
	public ExtendedRuleTokenStream() {}

	public ExtendedRuleTokenStream(int s) {
		this.offset = s;
	}

	public void add(Token t) {
		tokens.add(t); 
	}
	
	/** Get Token at current input pointer + i ahead where i=1 is next Token.
	 *  i&lt;0 indicates tokens in the past.  So -1 is previous token and -2 is
	 *  two tokens ago. LT(0) is undefined.  For i&gt;=n, return Token.EOFToken.
	 *  Return null for LT(0) and any index that results in an absolute address
	 *  that is negative.
	 */
	public Token LT(int k) { return tokens.get(pointer+k-offset); } 

	/** How far ahead has the stream been asked to look?  The return
	 *  value is a valid index from 0..n-1.
	 */
  	public int range() { return tokens.size() + offset; }
	
	/** Get a token at an absolute index i; 0..n-1.  This is really only
	 *  needed for profiling and debugging and token stream rewriting.
	 *  If you don't want to buffer up tokens, then this method makes no
	 *  sense for you.  Naturally you can't use the rewrite stream feature.
	 *  I believe DebugTokenStream can easily be altered to not use
	 *  this method, removing the dependency.
	 */
	public Token get(int i) { return tokens.get(i-offset); }

	/** Where is this stream pulling tokens from?  This is not the name, but
	 *  the object that provides Token objects.
	 */
	public TokenSource getTokenSource() { return null; }

	/** Return the text of all tokens from start to stop, inclusive.
	 *  If the stream does not buffer all the tokens then it can just
	 *  return "" or null;  Users should not access $ruleLabel.text in
	 *  an action of course in that case.
	 */
	public String toString(int start, int stop) {
		StringBuilder sb = new StringBuilder();
		for ( int i = start; i < stop; i++ ) {
			sb.append( tokens.get(i-offset).getText() );
		}
		return sb.toString(); 
	}

	/** Because the user is not required to use a token with an index stored
	 *  in it, we must provide a means for two token objects themselves to
	 *  indicate the start/end location.  Most often this will just delegate
	 *  to the other toString(int,int).  This is also parallel with
	 *  the TreeNodeStream.toString(Object,Object).
	 */
	public String toString(Token start, Token stop) { 
        if ( start!=null && stop!=null ) {
            return toString(start.getTokenIndex(), stop.getTokenIndex());
        }
        return null;
	}
	
	public void consume() {
		pointer++;
	} 

	/** Get int at current input pointer + i ahead where i=1 is next int.
	 *  Negative indexes are allowed.  LA(-1) is previous token (token
	 *  just matched).  LA(-i) where i is before first token should
	 *  yield -1, invalid char / EOF.
	 */
	public int LA(int i) {
		return LT(i).getType();
	}

	/** Tell the stream to start buffering if it hasn't already.  Return
     *  current input position, index(), or some other marker so that
	 *  when passed to rewind() you get back to the same spot.
	 *  rewind(mark()) should not affect the input cursor.  The Lexer
	 *  track line/col info as well as input index so its markers are
	 *  not pure input indexes.  Same for tree node streams.
     */
	public int mark() { 
		lastMarker = index();
		return lastMarker;
	}

	/** Return the current input symbol index 0..n where n indicates the
     *  last symbol has been read.  The index is the symbol about to be
	 *  read not the most recently read symbol.
     */
	public int index()  { 
		return pointer; 
	}

	/** Reset the stream so that next call to index would return marker.
	 *  The marker will usually be index() but it doesn't have to be.  It's
	 *  just a marker to indicate what state the stream was in.  This is
	 *  essentially calling release() and seek().  If there are markers
	 *  created after this marker argument, this routine must unroll them
	 *  like a stack.  Assume the state the stream was in when this marker
	 *  was created.
	 */
	public void rewind(int marker) {
        seek(marker);		
	}

	/** Rewind to the input position of the last marker.
	 *  Used currently only after a cyclic DFA and just
	 *  before starting a sem/syn predicate to get the
	 *  input position back to the start of the decision.
	 *  Do not "pop" the marker off the state.  mark(i)
	 *  and rewind(i) should balance still. It is
	 *  like invoking rewind(last marker) but it should not "pop"
	 *  the marker off.  It's like seek(last marker's input position).
	 */
	public void rewind() {
        seek(lastMarker);		
	}

	/** You may want to commit to a backtrack but don't want to force the
	 *  stream to keep bookkeeping objects around for a marker that is
	 *  no longer necessary.  This will have the same behavior as
	 *  rewind() except it releases resources without the backward seek.
	 *  This must throw away resources for all markers back to the marker
	 *  argument.  So if you're nested 5 levels of mark(), and then release(2)
	 *  you have to release resources for depths 2..5.
	 */
	public void release(int marker) {}

	/** Set the input cursor to the position indicated by index.  This is
	 *  normally used to seek ahead in the input stream.  No buffering is
	 *  required to do this unless you know your stream will use seek to
	 *  move backwards such as when backtracking.
	 *
	 *  This is different from rewind in its multi-directional
	 *  requirement and in that its argument is strictly an input cursor (index).
	 *
	 *  For char streams, seeking forward must update the stream state such
	 *  as line number.  For seeking backwards, you will be presumably
	 *  backtracking using the mark/rewind mechanism that restores state and
	 *  so this method does not need to update state when seeking backwards.
	 *
	 *  Currently, this method is only used for efficient backtracking using
	 *  memoization, but in the future it may be used for incremental parsing.
	 *
	 *  The index is 0..n-1.  A seek to position i means that LA(1) will
	 *  return the ith symbol.  So, seeking to 0 means LA(1) will return the
	 *  first element in the stream. 
	 */
	public void seek(int index) {
		pointer = index;
	}

	/** Only makes sense for streams that buffer everything up probably, but
	 *  might be useful to display the entire stream or for testing.  This
	 *  value includes a single EOF.
	 */
	public int size()  { return 0; }

	/** Where are you getting symbols from?  Normally, implementations will
	 *  pass the buck all the way to the lexer who can ask its input stream
	 *  for the file name or whatever.
	 */
	public String getSourceName()  { return null; }
}
