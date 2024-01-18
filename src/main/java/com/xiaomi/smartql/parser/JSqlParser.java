/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2024 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
package com.xiaomi.smartql.parser;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.statement.Statement;

import java.io.Reader;

public interface JSqlParser {

    Statement parse(Reader statementReader) throws JSQLParserException;
}
