package com.gm.assignment.web;

import java.time.LocalDate;

public record RecordDto(
	String code,
	String source,
	String codeListCode,
	String displayValue,
	String longDescription,
	LocalDate fromDate,
	LocalDate toDate,
	Integer sortingPriority
) {
}
