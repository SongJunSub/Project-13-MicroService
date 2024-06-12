package org.msa.itemservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@FeignClient(name = "history-service")
public interface HistoryFeignClient {

    @PostMapping("/v1/history/save")
    String saveHistory(Map<String, Object> paramMap);

}