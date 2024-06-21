package com.gm.assignment;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class RecordServiceImpl implements RecordService {
    private final RecordRepository recordRepository;

    RecordServiceImpl(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
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
