package org.src.etl.model.yit;

import java.util.List;

public class YitRequest {
    private int PageSize;
    private int StartPage;
    private int PageId;
    private String SiteId;
    private Filter Filter;

    // Getters and Setters
    public int getPageSize() {
        return PageSize;
    }

    public void setPageSize(int pageSize) {
        this.PageSize = pageSize;
    }

    public int getStartPage() {
        return StartPage;
    }

    public void setStartPage(int startPage) {
        this.StartPage = startPage;
    }

    public int getPageId() {
        return PageId;
    }

    public void setPageId(int pageId) {
        this.PageId = pageId;
    }

    public String getSiteId() {
        return SiteId;
    }

    public void setSiteId(String siteId) {
        this.SiteId = siteId;
    }

    public Filter getFilter() {
        return Filter;
    }

    public void setFilter(Filter filter) {
        this.Filter = filter;
    }

    public static class Filter {
        private String Field;
        private Object Value;
        private String Operator;
        private List<Condition> AndConditions;
        private List<Condition> OrConditions;

        // Getters and Setters
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
    }

    public static class Condition {
        private String Field;
        private Object Value;
        private String Operator;
        private List<Condition> AndConditions;
        private List<Condition> OrConditions;

        // Getters and Setters
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
    }
}
