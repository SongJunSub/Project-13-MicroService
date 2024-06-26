package org.msa.historyservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.msa.historyservice.dto.HistoryDTO;
import org.msa.historyservice.dto.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "v1/history")
public class HistoryController {

    @PostMapping("/save")
    public ResponseEntity<ResponseDTO> history(HttpServletRequest request, @RequestBody HistoryDTO historyDTO) throws Exception {
        ResponseDTO.ResponseDTOBuilder responseBuilder = ResponseDTO.builder();

        log.info("History : {}", historyDTO.toString());

        responseBuilder.code("200").message("success");

        return ResponseEntity.ok(responseBuilder.build());
    }

}