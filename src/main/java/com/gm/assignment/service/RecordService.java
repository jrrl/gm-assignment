package com.gm.assignment.service;

import com.gm.assignment.entity.Record;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public interface RecordService {
    void uploadRecords(InputStream csvInputStream) throws IOException;
    void saveRecords(List<Record> records);
    void deleteAllRecords();
    List<Record> getAllRecords();
    Optional<Record> getRecordByCode(String code);
}
