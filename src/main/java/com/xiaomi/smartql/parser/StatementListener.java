package com.xiaomi.smartql.parser;

import net.sf.jsqlparser.statement.Statement;

/**
 *
 * @author Tobias Warneke (t.warneke@gmx.net)
 */
public interface StatementListener {

    void accept(Statement statement);
}
