package com.xiaomi.smartql.lineage;

import java.util.List;

/**
 * @author Hawick Mason
 * @date 2022/11/24
 */
public class FieldLineage {

    private String currentField;
    private List<LineageBO> lineageFields; // 来源血缘字段列表

    private FieldLineage(Builder builder) {
        setCurrentField(builder.currentField);
        setLineageFields(builder.lineageFields);
    }

    public String getCurrentField() {
        return currentField;
    }

    public void setCurrentField(String currentField) {
        this.currentField = currentField;
    }

    public List<LineageBO> getLineageFields() {
        return lineageFields;
    }

    public void setLineageFields(List<LineageBO> lineageFields) {
        this.lineageFields = lineageFields;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class LineageBO {
        private String fieldName;
        private String tableName;

        private LineageBO(LineageBOBuilder builder) {
            setFieldName(builder.fieldName);
            setTableName(builder.tableName);
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        @Override
        public String toString() {
            return "LineageBO{" +
                    "fieldName='" + fieldName + '\'' +
                    ", tableName='" + tableName + '\'' +
                    '}';
        }

        public static LineageBOBuilder builder() {
            return new LineageBOBuilder();
        }

        public static final class LineageBOBuilder {
            private String fieldName;
            private String tableName;

            public LineageBOBuilder() {
            }

            public LineageBOBuilder fieldName(String val) {
                fieldName = val;
                return this;
            }

            public LineageBOBuilder tableName(String val) {
                tableName = val;
                return this;
            }

            public LineageBO build() {
                return new LineageBO(this);
            }
        }
    }

    public static final class Builder {
        private String currentField;
        private List<LineageBO> lineageFields;

        public Builder() {
        }

        public Builder currentField(String val) {
            currentField = val;
            return this;
        }

        public Builder lineageFields(List<LineageBO> val) {
            lineageFields = val;
            return this;
        }

        public FieldLineage build() {
            return new FieldLineage(this);
        }
    }

    @Override
    public String toString() {
        return "FieldLineage{" +
                "currentField='" + currentField + '\'' +
                ", lineageFields=" + lineageFields +
                '}';
    }
}
