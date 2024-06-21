package com.gm.assignment.service;

import com.gm.assignment.entity.Record;
import com.gm.assignment.entity.RecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class RecordServiceImplTest {
	private static final Record DEFAULT_RECORD = new Record(
		"code",
		"source",
		"codeListCode",
		"displayValue",
		"longDescription",
		LocalDate.of(2024,
			12,
			25
		),
		LocalDate.of(2024,
			12,
			26
		),
		1
	);

	@Autowired
	private RecordServiceImpl recordService;

	@Autowired
	private RecordRepository recordRepository;

	@BeforeEach
	public void setUp() {
		recordRepository.deleteAll();
	}

	private static Stream<List<Record>> saveRecordArguments() {
		return Stream.of(
			List.of(
				DEFAULT_RECORD),
			List.of(
				DEFAULT_RECORD,
				new Record(
					"code1",
					"source1",
					"codeListCode1",
					"displayValue1",
					"longDescription1",
					LocalDate.of(2024,
						12,
						25
					),
					LocalDate.of(2024,
						12,
						26
					),
					1
				)
			)
		);
	}

	@Test
	public void testUploadRecords() throws IOException {
		File csv = ResourceUtils.getFile("classpath:test.csv");
		try (InputStream inputStream = new FileInputStream(csv)) {
			recordService.uploadRecords(inputStream);

			List<Record> savedRecords = recordRepository.findAll();

			List<Record> expectedRecords = List.of(DEFAULT_RECORD,
				new Record(
					"code1",
					"source1",
					"codeListCode1",
					"displayValue1",
					"",
					LocalDate.of(2024,
						12,
						25
					),
					null,
					null
				));

			for(int i = 0; i < savedRecords.size(); i++) {
				assertEqualsRecord(expectedRecords.get(i), savedRecords.get(i));
			}
		}
	}

	@ParameterizedTest
	@MethodSource("saveRecordArguments")
	public void testSaveRecord(List<Record> records) {
		recordService.saveRecords(records);
		List<Record> savedRecords = recordRepository.findAll();
		assertEquals(records.size(), savedRecords.size());
		for(int i = 0; i < records.size(); i++) {
			assertEqualsRecord(records.get(i), savedRecords.get(i));
		}
	}

	@ParameterizedTest
	@MethodSource("saveRecordArguments")
	public void testDeleteAllRecords(List<Record> records) {
		recordRepository.saveAll(records);
		List<Record> savedRecords = recordRepository.findAll();
		assertEquals(records.size(), savedRecords.size());

		recordService.deleteAllRecords();
		assertTrue(recordService.getAllRecords().isEmpty());
	}

	@ParameterizedTest
	@MethodSource("saveRecordArguments")
	public void testGetRecordByCode(List<Record> records) {
		recordRepository.saveAll(records);

		Optional<Record> result = recordService.getRecordByCode(DEFAULT_RECORD.getCode());
		assertTrue(result.isPresent());
		assertEqualsRecord(DEFAULT_RECORD, result.get());
	}

	@ParameterizedTest
	@MethodSource("saveRecordArguments")
	public void testGetAllRecords(List<Record> records) {
		recordRepository.saveAll(records);

		List<Record> savedRecords = recordService.getAllRecords();
		assertEquals(records.size(), savedRecords.size());
		for(int i = 0; i < records.size(); i++) {
			assertEqualsRecord(records.get(i), savedRecords.get(i));
		}
	}



	private static void assertEqualsRecord(Record record,
	                                       Record actualRecord) {
		assertEquals(record.getCode(), actualRecord.getCode());
		assertEquals(record.getSource(), actualRecord.getSource());
		assertEquals(record.getCodeListCode(), actualRecord.getCodeListCode());
		assertEquals(record.getDisplayValue(), actualRecord.getDisplayValue());
		assertEquals(record.getLongDescription(), actualRecord.getLongDescription());
		assertEquals(record.getFromDate(), actualRecord.getFromDate());
		assertEquals(record.getToDate(), actualRecord.getToDate());
		assertEquals(record.getSortingPriority(), actualRecord.getSortingPriority());
	}
}
