package org.src.etl.model.yit;

import com.google.gson.Gson;
import org.src.etl.model.RestFilter;

import java.util.List;

public class YitFilter implements RestFilter {
    static class Condition {
        private String Field;
        private Object Value; // Can be Boolean, String, or List<String>
        private String Operator;
        private List<Condition> AndConditions;  // Nested "AndConditions"
        private List<Condition> OrConditions;   // Nested "OrConditions"

        public String getField() {
            return Field;
        }

        public void setField(String field) {
            this.Field = field;
        }

        public Object getValue() {
            return Value;
        }

        public void setValue(Object value) {
            this.Value = value;
        }

        public String getOperator() {
            return Operator;
        }

        public void setOperator(String operator) {
            this.Operator = operator;
        }

        public List<Condition> getAndConditions() {
            return AndConditions;
        }

        public void setAndConditions(List<Condition> andConditions) {
            this.AndConditions = andConditions;
        }

        public List<Condition> getOrConditions() {
            return OrConditions;
        }

        public void setOrConditions(List<Condition> orConditions) {
            this.OrConditions = orConditions;
        }

        public String toString() {
            return new Gson().toJson(this);
        }

    }

    private String Field;
    private String Value;
    private String Operator;
    private List<Condition> AndConditions;
    private List<Condition> OrConditions;

    @Override
    public String serializeToJson() {
        return new Gson().toJson(this);
    }
}
