package org.src.etl.model.yit;

public class YitRequestBody {
    private YitFilter Filter;
    private int PageSize;
    private int StartPage;
    private int PageId;
    private String SiteId;

    public YitFilter getFilter() {
        return Filter;
    }

    public void setFilter(YitFilter filter) {
        this.Filter = filter;
    }

    public int getPageSize() {
        return PageSize;
    }

    public void setPageSize(int pageSize) {
        PageSize = pageSize;
    }

    public int getStartPage() {
        return StartPage;
    }

    public void setStartPage(int startPage) {
        StartPage = startPage;
    }

    public int getPageId() {
        return PageId;
    }

    public void setPageId(int pageId) {
        PageId = pageId;
    }

    public String getSiteId() {
        return SiteId;
    }

    public void setSiteId(String siteId) {
        SiteId = siteId;
    }
}
