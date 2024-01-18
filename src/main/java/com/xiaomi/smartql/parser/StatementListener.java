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

import net.sf.jsqlparser.statement.Statement;

/**
 *
 * @author Tobias Warneke (t.warneke@gmx.net)
 */
public interface StatementListener {

    void accept(Statement statement);
}
