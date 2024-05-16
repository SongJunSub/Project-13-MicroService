package org.msa.itemservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.msa.itemservice.dto.ItemDTO;
import org.msa.itemservice.dto.ResponseDTO;
import org.msa.itemservice.dto.constant.ItemType;
import org.msa.itemservice.exception.ApiException;
import org.msa.itemservice.service.ItemService;
import org.msa.itemservice.valid.ItemTypeValid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/item")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/add/{itemType}")
    public ResponseEntity<ResponseDTO> addItem(@Valid @RequestBody ItemDTO itemDTO, @ItemTypeValid @PathVariable String itemType) throws Exception {
        ResponseDTO.ResponseDTOBuilder responseDTOBuilder = ResponseDTO.builder();

        /*
        log.debug("Item Type = {}", itemType);
        boolean hasItemType = false;
        ItemType[] itemTypes = ItemType.values();

        for(ItemType item : itemTypes) {
            hasItemType = item.hasItemCd(itemType);

            if(hasItemType) break;
        }

        if(!hasItemType) {
            responseDTOBuilder.code("500").message("Invalid Item Type : " + itemType);

            return ResponseEntity.ok(responseDTOBuilder.build());
        }
        else {
            itemDTO.setItemType(itemType);
        }
        */

        /*
        try {
            Integer.parseInt("TEST");
        }
        catch (Exception e){
            throw new ApiException("ApiException Test");
        }
        */

        itemDTO.setItemType(itemType);

        itemService.insertItem(itemDTO);
        log.debug("Request Add Item ID = {}", itemDTO.getId());

        responseDTOBuilder.code("200").message("Success");

        return ResponseEntity.ok(responseDTOBuilder.build());
    }

}