package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestRefundDto;
import ru.practicum.shareit.request.service.RequestService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RequestController.class)
class RequestControllerItTest {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    RequestService service;

    @Test
    void add_ok() throws Exception  {
        Long userId = 1L;
        ItemRequestDto requestDto = new ItemRequestDto("Ищу книгу");
        ItemRequestRefundDto requestRefundDto = new ItemRequestRefundDto(
                1L,
                "Ищу книгу",
                LocalDateTime.now(),
                List.of()
        );

        when(service.add(anyLong(), any())).thenReturn(requestRefundDto);

        mockMvc.perform(post("/requests")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(requestDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Ищу книгу"));

        verify(service).add(anyLong(), any());
    }

    @Test
    void add_RequestNotValid_whenDescriptionBlank_thanReturnBadRequest() throws Exception {
        Long userId = 1L;
        ItemRequestDto requestDto = new ItemRequestDto("");
        ItemRequestRefundDto requestRefundDto = new ItemRequestRefundDto();

        when(service.add(anyLong(), any())).thenReturn(requestRefundDto);

        mockMvc.perform(post("/requests")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(requestDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        verify(service, never()).add(anyLong(), any());
    }

    @Test
    void add_RequestNotValid_whenDescriptionMoreMaxSize_thanReturnBadRequest() throws Exception {
        Long userId = 1L;
        ItemRequestDto requestDto = new ItemRequestDto("Книга знакомит читателя со всеми вопросами, " +
                "связанными с природным газом. В ней приведены сведения о происхождении природного газа, " +
                "в том числе методы разведки, бурения и заканчивания газовых скважин, история открытия и " +
                "применения газа для освещения и отопления, включая его бытовое использование, коммерческое "+
                "потребление, а также применение в промышленности и д/ш производства электроэнергии.Уделено внимание "+
                "этапам развития газовой промышленности, транспортировке газа по трубам, эксплуатации газопроводов, " +
                "а также хранению газа, маркетингу и сбыту. Читатель ознакомится с нормами государственного " +
                "регулирования газовой промышленности и охраны окружающей среды.В книге проанализированы перспективы " +
                "изменения спроса и предложения природного газа. Приведен подробный словарь терминов и предметный " +
                "указатель. Книга рассчитана на широкий круг читателей.");
        ItemRequestRefundDto requestRefundDto = new ItemRequestRefundDto();

        when(service.add(anyLong(), any())).thenReturn(requestRefundDto);

        mockMvc.perform(post("/requests")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(requestDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        verify(service, never()).add(anyLong(), any());
    }

    @Test
    void findAllOwner_ok() throws Exception {
        Long userId = 1L;

        when(service.findAllOwner(anyLong())).thenReturn(List.of());

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(service).findAllOwner(anyLong());
    }

    @Test
    void findAllNotOwner_whenWithParam_ok() throws Exception {
        Long userId = 1L;
        int from = 0;
        int size = 2;

        when(service.findAllNotOwner(anyLong(), any(), anyInt())).thenReturn(List.of());

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk());


        verify(service).findAllNotOwner(anyLong(), any(), anyInt());
    }

    @Test
    void findAllNotOwner_whenWithParamNotValidPage_thanReturnBadRequest() throws Exception {
        Long userId = 1L;
        int from = 3;
        int size = 5;

        when(service.findAllNotOwner(anyLong(), any(), anyInt())).thenReturn(List.of());

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isInternalServerError());

        verify(service, never()).findAllNotOwner(anyLong(), any(), anyInt());
    }

    @Test
    void findAllNotOwner_whenWithoutParam_ok() throws Exception {
        Long userId = 1L;

        when(service.findAllNotOwner(anyLong(), any(), anyInt())).thenReturn(List.of());

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", "")
                        .param("size", ""))
                .andExpect(status().isOk());

        verify(service).findAllNotOwner(anyLong(), any(), anyInt());
    }

    @Test
    void findAllNotOwner_whenParamFromNotValid_Min_thanReturnBadRequest() throws Exception {
        Long userId = 1L;
        int from = -1;
        int size = 2;

        when(service.findAllNotOwner(anyLong(), any(), anyInt())).thenReturn(List.of());

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isBadRequest());

        verify(service, never()).findAllNotOwner(anyLong(), any(), anyInt());
    }

    @Test
    void findAllNotOwner_whenParamSizeNotValid_Min_thanReturnBadRequest() throws Exception {
        Long userId = 1L;
        int from = 0;
        int size = 0;

        when(service.findAllNotOwner(anyLong(), any(), anyInt())).thenReturn(List.of());

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isBadRequest());

        verify(service, never()).findAllNotOwner(anyLong(), any(), anyInt());
    }

    @Test
    void findAllNotOwner_whenParamSizeNotValid_Max_thanReturnBadRequest() throws Exception {
        Long userId = 1L;
        int from = -1;
        int size = 60;

        when(service.findAllNotOwner(anyLong(), any(), anyInt())).thenReturn(List.of());

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isBadRequest());

        verify(service, never()).findAllNotOwner(anyLong(), any(), anyInt());
    }

    @Test
    void findById_ok() throws Exception  {
        Long userId = 1L;

        when(service.findById(anyLong(), anyLong())).thenReturn(new ItemRequestRefundDto());

        mockMvc.perform(get("/requests/{requestId}", userId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(service).findById(anyLong(), anyLong());
    }
}
