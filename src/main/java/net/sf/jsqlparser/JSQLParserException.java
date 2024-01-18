/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2024 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
package net.sf.jsqlparser;

import com.xiaomi.smart.exception.ErrorCode;
import com.xiaomi.smart.exception.SmartException;

public class JSQLParserException extends SmartException {

    private static final long serialVersionUID = -4200894355696788796L;

    public JSQLParserException() {
        super(ErrorCode.SMART_PARSER);
    }

    public JSQLParserException(String message, Throwable cause) {
        super(ErrorCode.SMART_PARSER, message, cause);
    }

    public JSQLParserException(String message) {
        super(ErrorCode.SMART_PARSER, message);
    }

    public JSQLParserException(Throwable cause) {
        super(ErrorCode.SMART_PARSER, cause == null ? null : cause.getMessage(), cause);
    }

}
