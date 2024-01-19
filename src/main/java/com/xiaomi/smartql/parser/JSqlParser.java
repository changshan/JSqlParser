package com.xiaomi.smartql.parser;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.statement.Statement;

import java.io.Reader;

public interface JSqlParser {

    Statement parse(Reader statementReader) throws JSQLParserException;
}
