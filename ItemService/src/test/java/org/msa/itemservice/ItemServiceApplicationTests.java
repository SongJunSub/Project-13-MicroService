package org.msa.itemservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.msa.itemservice.controller.ItemController;
import org.msa.itemservice.dto.ItemDTO;
import org.msa.itemservice.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Slf4j
class ItemServiceApplicationTests {

    @Autowired
    private ItemService itemService;

    // HTTP 테스트
    @Autowired
    MockMvc mvc;

    @BeforeEach
    public void setup() {
        // MockMvc에 ItemController 클래스에 대해서 UTF-8 Charset 필터와 함께 테스트 대상 컨트롤러로 지정하는 부분이다.
        mvc = MockMvcBuilders.standaloneSetup(new ItemController(itemService))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Test
    @DisplayName("물품 등록 테스트")
    void test() throws Exception {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            log.info(objectMapper.writeValueAsString(getTestTeam() + ""));

            mvc.perform(MockMvcRequestBuilders.post("/v1/item/add/C")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("accountId", "admin")
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(getTestTeam())))
                    // 전송 진행 후 응답 데이터를 Console에 출력한다는 의미이다.
                    .andDo(MockMvcResultHandlers.print())
                    // HTTP 응답 상태 값이 200인지 검증하는 함수이다.
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
        catch (Exception e) {
            Assertions.fail(ExceptionUtils.getStackTrace(e));
        }
    }

    private ItemDTO getTestTeam() {
        ItemDTO itemDTO = new ItemDTO();

        itemDTO.setId("Test1");
        itemDTO.setName("상품명 테스트");
        itemDTO.setDescription("상품 설명 테스트");
        itemDTO.setCount(50L);

        return itemDTO;
    }

}
