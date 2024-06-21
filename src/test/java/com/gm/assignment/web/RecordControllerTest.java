package com.gm.assignment.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gm.assignment.entity.Record;
import com.gm.assignment.service.RecordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RecordControllerTest {
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

	@MockBean
	private RecordService recordService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void getAllRecords() throws Exception {
		when(recordService.getAllRecords()).thenReturn(List.of(DEFAULT_RECORD));

		mockMvc.perform(get("/records"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(objectMapper.writeValueAsString(List.of(DEFAULT_RECORD))));
	}

	@Test
	public void getAllRecords_EmptyRecords() throws Exception {
		when(recordService.getAllRecords()).thenReturn(List.of());

		mockMvc.perform(get("/records"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(objectMapper.writeValueAsString(List.of())));
	}

	@Test
	public void getRecordByCode() throws Exception {
		when(recordService.getRecordByCode(DEFAULT_RECORD.getCode())).thenReturn(Optional.of(DEFAULT_RECORD));

		mockMvc.perform(get("/records/{code}", DEFAULT_RECORD.getCode()))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(objectMapper.writeValueAsString(DEFAULT_RECORD)));
	}

	@Test
	public void getRecordByCode_NotFound() throws Exception {
		when(recordService.getRecordByCode(DEFAULT_RECORD.getCode())).thenReturn(Optional.empty());

		mockMvc.perform(get("/records/{code}", DEFAULT_RECORD.getCode()))
			.andExpect(status().isNotFound());
	}

	@Test
	public void uploadRecords() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "test.csv", "application/csv", "test".getBytes());

		doNothing().when(recordService).uploadRecords(file.getInputStream());

		mockMvc.perform(multipart("/records/upload")
				.file(file)
				.contentType("application/csv")
			)
			.andExpect(status().isOk());
	}

	@Test
	public void uploadRecords_InvalidFormat() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "test.csv", "application/csv", "test".getBytes());

		doThrow(new IOException("error")).when(recordService).uploadRecords(any());

		mockMvc.perform(multipart("/records/upload")
				.file(file)
				.contentType("application/csv")
			)
			.andExpect(status().isUnprocessableEntity());
	}

	@Test
	public void uploadRecords_GenericError() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "test.csv", "application/csv", "test".getBytes());

		doThrow(new RuntimeException("error")).when(recordService).uploadRecords(any());

		mockMvc.perform(multipart("/records/upload")
				.file(file)
				.contentType("application/csv")
			)
			.andExpect(status().is5xxServerError());
	}
}
