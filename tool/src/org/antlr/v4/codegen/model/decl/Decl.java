/*
 * Copyright (c) 2012-2016 The ANTLR Project. All rights reserved.
 * Use of this file is governed by the BSD 3-clause license that
 * can be found in the LICENSE.txt file in the project root.
 */

package org.antlr.v4.codegen.model.decl;

import org.antlr.v4.codegen.OutputModelFactory;
import org.antlr.v4.codegen.model.SrcOp;

/** */
public class Decl extends SrcOp {
	public String name;
	public String decl; 	// whole thing if copied from action
	public boolean isLocal; // if local var (not in RuleContext struct)
	public StructDecl ctx;  // which context contains us? set by addDecl
	final public boolean isImported;
	final public String prefix;


	public Decl(OutputModelFactory factory, String name, String decl, String prefix, boolean imported) {
		this(factory, name, prefix, imported);
		this.decl = decl;
	}

	public Decl(OutputModelFactory factory, String name, String prefix, boolean imported) {
		super(factory);
		this.name = name;
		this.isImported = imported;
		this.prefix = prefix;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	/** If same name, can't redefine, unless it's a getter */
	@Override
	public boolean equals(Object obj) {
		if ( this==obj ) return true;
		if ( !(obj instanceof Decl) ) return false;
		// A() and label A are different
		if ( obj instanceof ContextGetterDecl ) return false;
		return name.equals(((Decl) obj).name);
	}
}
