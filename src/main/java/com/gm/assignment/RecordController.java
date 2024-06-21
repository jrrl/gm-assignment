package com.gm.assignment;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/records")
public class RecordController {
    private final RecordService recordService;

    RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Void> uploadRecords(@RequestParam("file") MultipartFile file) {
        try {
            recordService.uploadRecords(file.getInputStream());
            return ResponseEntity.ok().build();
        }
        catch (IOException e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<RecordDto> getAllRecords() {
        return recordService.getAllRecords()
          .stream()
          .map(RecordController::mapRecordToDto)
          .toList();
    }

    @GetMapping(value = "/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecordDto> getRecord(@PathVariable("code") String code) {
        return recordService.getRecordByCode(code)
          .map(record -> ResponseEntity.ok(mapRecordToDto(record)))
          .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllRecords() {
        recordService.deleteAllRecords();
    }

    private static RecordDto mapRecordToDto(Record record) {
        return new RecordDto(
          record.getCode(),
          record.getSource(),
          record.getCodeListCode(),
          record.getDisplayValue(),
          record.getLongDescription(),
          record.getFromDate(),
          record.getToDate(),
          record.getSortingPriority()
        );
    }
}
