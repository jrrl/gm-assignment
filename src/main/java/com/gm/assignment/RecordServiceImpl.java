package com.gm.assignment;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class RecordServiceImpl implements RecordService {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-uuuu");
    private final RecordRepository recordRepository;

    RecordServiceImpl(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @Override
    public void uploadRecords(InputStream csvInputStream) throws IOException {
        try (final Reader reader = new InputStreamReader(csvInputStream)) {
            List<Record> records = CSVFormat.DEFAULT.builder()
              .setHeader()
              .setSkipHeaderRecord(true)
              .build()
              .parse(reader)
              .stream()
              .map(RecordServiceImpl::csvRecordToRecord)
	            .toList();

						recordRepository.saveAll(records);
        }
    }

	private static Record csvRecordToRecord(CSVRecord csvRecord) {
			System.out.println(csvRecord);
		LocalDate toDate = csvRecord.isSet("toDate") && StringUtils.isNotBlank(csvRecord.get("toDate")) ?
			LocalDate.parse(csvRecord.get("toDate").strip(), DATE_FORMATTER) : null;
		String sortingPriorityOrig = csvRecord.get("sortingPriority").strip();
		Integer sortingPriority = csvRecord.isSet("sortingPriority")
			&& StringUtils.isNotBlank(sortingPriorityOrig)
			&& StringUtils.isNumericSpace(sortingPriorityOrig) ?
			Integer.parseInt(sortingPriorityOrig) : null;
		return new Record(
			csvRecord.get("code").strip(),
			csvRecord.get("source").strip(),
			csvRecord.get("codeListCode").strip(),
			csvRecord.get("displayValue").strip(),
			csvRecord.get("longDescription").strip(),
			LocalDate.parse(csvRecord.get("fromDate").strip(), DATE_FORMATTER),
			toDate,
			sortingPriority
		);
	}

	@Override
    public void saveRecords(List<Record> records) {
        recordRepository.saveAll(records);
    }

    @Override
    public void deleteAllRecords() {
        recordRepository.deleteAll();
    }

    @Override
    public List<Record> getAllRecords() {
        return recordRepository.findAll();
    }

    @Override
    public Optional<Record> getRecordByCode(String code) {
        return recordRepository.findById(code);
    }
}
