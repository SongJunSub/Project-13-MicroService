package org.msa.itemservice.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.msa.itemservice.dto.ItemDTO;
import org.msa.itemservice.dto.ResponseDTO;
import org.msa.itemservice.service.ItemService;
import org.msa.itemservice.valid.ItemTypeValid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@OpenAPIDefinition(info = @Info(title = "물품 처리 요청 API", description = "물품 처리 요청 API", version = "v1"))
@RestController
@RequestMapping("v1/item")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemService itemService;

    @Operation(summary = "물품 등록 요청", description = "물폼 등록을 진행할 수 있다.", tags = { "addItem" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "501", description = "API Exception")
    })
    @PostMapping("/add/{itemType}")
    public ResponseEntity<ResponseDTO> addItem(HttpServletRequest request, @Valid @RequestBody ItemDTO itemDTO, @ItemTypeValid @PathVariable String itemType) throws Exception {
        ResponseDTO.ResponseDTOBuilder responseDTOBuilder = ResponseDTO.builder();

        String accountId = request.getHeader("accountId").toString().replace("[", "").replace("]", "");

        log.info("Account ID: {}", accountId);

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

        itemService.insertItem(itemDTO, accountId);
        log.debug("Request Add Item ID = {}", itemDTO.getId());

        responseDTOBuilder.code("200").message("Success");

        return ResponseEntity.ok(responseDTOBuilder.build());
    }

}