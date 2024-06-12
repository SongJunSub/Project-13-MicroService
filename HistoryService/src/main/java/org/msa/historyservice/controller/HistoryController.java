package org.msa.historyservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.msa.historyservice.dto.HistoryDTO;
import org.msa.historyservice.dto.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "v1/history")
public class HistoryController {

    @GetMapping
    public ResponseEntity<ResponseDTO> history(HttpServletRequest request, @RequestBody HistoryDTO historyDTO) throws Exception {
        ResponseDTO.ResponseDTOBuilder responseBuilder = ResponseDTO.builder();

        log.info("History : {}", historyDTO.toString());

        responseBuilder.code("200").message("success");

        return ResponseEntity.ok(responseBuilder.build());
    }

}