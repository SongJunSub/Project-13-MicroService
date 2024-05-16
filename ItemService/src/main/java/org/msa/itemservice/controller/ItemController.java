package org.msa.itemservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.msa.itemservice.dto.ItemDTO;
import org.msa.itemservice.dto.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/item")
@Slf4j
public class ItemController {

    @PostMapping("/add")
    public ResponseEntity<ResponseDTO> addItem(@RequestBody ItemDTO itemDTO) {
        ResponseDTO.ResponseDTOBuilder responseDTOBuilder = ResponseDTO.builder();

        log.debug("Request Add Item ID = {}", itemDTO.getId());

        responseDTOBuilder.code("200").message("Success");

        return ResponseEntity.ok(responseDTOBuilder.build());
    }

}