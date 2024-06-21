package com.gm.assignment;

import java.util.List;
import java.util.Optional;

public interface RecordService {
    void saveRecords(List<Record> records);
    void deleteAllRecords();
    List<Record> getAllRecords();
    Optional<Record> getRecordByCode(String code);
}
