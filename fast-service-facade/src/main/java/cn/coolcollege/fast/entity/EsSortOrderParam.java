package cn.coolcollege.fast.entity;

/**
 * @Author bai bin
 * @Date 2021/5/18 11:31
 */
public class EsSortOrderParam {

    private String sortName;

    private String sortOrder;

    public EsSortOrderParam() {
    }

    public EsSortOrderParam(String sortName, String sortOrder) {
        this.sortName = sortName;
        this.sortOrder = sortOrder;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public String toString() {
        return "EsSortOrderParam{" +
                "sortName='" + sortName + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                '}';
    }
}
