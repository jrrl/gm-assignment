package com.gm.assignment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;

import java.time.LocalDate;

@Entity
public class Record {
    @Id
    @Column(nullable = false)
    private String code;
    @Column(nullable = false)
    private String source;
    @Column(nullable = false)
    private String codeListCode;
    @Column(nullable = false)
    private String displayValue;
    @Column
    private String longDescription;
    @Column(nullable = false, columnDefinition = "DATE")
    private LocalDate fromDate;
    @Column(columnDefinition = "DATE")
    private LocalDate toDate;
    @Column
    private int sortingPriority;
    @Version
    private int version;

    public Record() {}

    public Record(
            String code,
            String source,
            String codeListCode,
            String displayValue,
            String longDescription,
            LocalDate fromDate,
            LocalDate toDate,
            int sortingPriority,
            int version) {
        this.code = code;
        this.source = source;
        this.codeListCode = codeListCode;
        this.displayValue = displayValue;
        this.longDescription = longDescription;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.sortingPriority = sortingPriority;
        this.version = version;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCodeListCode() {
        return codeListCode;
    }

    public void setCodeListCode(String codeListCode) {
        this.codeListCode = codeListCode;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public int getSortingPriority() {
        return sortingPriority;
    }

    public void setSortingPriority(int sortingPriority) {
        this.sortingPriority = sortingPriority;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
