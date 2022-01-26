package com.test.common.myEnum;

/**
 * excel版本枚举
 */
public enum ExcelVersionEnum {
	
    /** xls */
    V2003("xls", 1000000, 100),
    /** xlsx */
    V2007("xlsx", 1000000, 100);

    private String suffix;

    private int maxRow;

    private int maxColumn;

    ExcelVersionEnum(String suffix, int maxRow, int maxColumn) {
        this.suffix = suffix;
        this.maxRow = maxRow;
        this.maxColumn = maxColumn;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public int getMaxRow() {
        return maxRow;
    }

    public void setMaxRow(int maxRow) {
        this.maxRow = maxRow;
    }

    public int getMaxColumn() {
        return maxColumn;
    }

    public void setMaxColumn(int maxColumn) {
        this.maxColumn = maxColumn;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

}
