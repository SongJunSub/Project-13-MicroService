package org.msa.itemservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.msa.itemservice.domain.Item;
import org.msa.itemservice.dto.ItemDTO;
import org.msa.itemservice.feign.HistoryFeignClient;
import org.msa.itemservice.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final HistoryFeignClient historyFeignClient;

    public void insertItem(ItemDTO itemDTO, String accountId) {
        SimpleDateFormat form = new SimpleDateFormat("yyyyMMddHHmmss");
        final String date = form.format(new Date());

        Item item = Item.builder()
                .id(itemDTO.getId())
                .accountId(accountId)
                .name(itemDTO.getName())
                .description(itemDTO.getDescription())
                .itemType(itemDTO.getItemType())
                .count(itemDTO.getCount())
                .createdAt(date)
                .updatedAt(date)
                .build();

        itemRepository.save(item);

        HashMap<String, Object> historyMap = new HashMap<>();

        historyMap.put("accountId", accountId);
        historyMap.put("itemId", itemDTO.getId());

        log.info("Feign Result : {}", historyFeignClient.saveHistory(historyMap));
    }

}