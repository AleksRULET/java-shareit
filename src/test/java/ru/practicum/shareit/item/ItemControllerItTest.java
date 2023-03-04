package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRefundDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRefundDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerItTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemService service;

    // метод add
    @Test
    void add_ok() throws Exception {
        Long userId = 1L;
        ItemRefundDto itemRefundDto = new ItemRefundDto(
                1L,
                "коробка",
                "коробка для перевозки вещей",
                true,
                null
        );
        ItemDto itemDto = new ItemDto(
                "коробка",
                "коробка для перевозки вещей",
                true,
                null
        );

        when(service.add(anyLong(), any())).thenReturn(itemRefundDto);

        mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("коробка"))
                .andExpect(jsonPath("$.description").value("коробка для перевозки вещей"))
                .andExpect(jsonPath("$.available").isBoolean())
                .andExpect(jsonPath("$.requestId").isEmpty());

        verify(service).add(anyLong(), any());
    }

    @Test
    void add_whenNameNotValid_Blank_thanReturnBadRequest() throws Exception {
        Long userId = 1L;
        ItemDto itemDto = new ItemDto(
                "",
                "коробка для перевозки вещей",
                true,
                null
        );

        when(service.add(anyLong(), any())).thenReturn(new ItemRefundDto());

        mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        verify(service, never()).add(anyLong(), any());
    }

    @Test
    void add_whenDescriptionNotValid_Null_thanReturnBadRequest() throws Exception {
        Long userId = 1L;
        ItemDto itemDto = new ItemDto(
                "коробка",
                null,
                true,
                null
        );

        when(service.add(anyLong(), any())).thenReturn(new ItemRefundDto());

        mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        verify(service, never()).add(anyLong(), any());
    }

    @Test
    void add_whenDescriptionNotValid_MoreMaxSize_thanReturnBadRequest() throws Exception {
        Long userId = 1L;
        ItemDto itemDto = new ItemDto(
                "Книга",
                "Книга знакомит читателя со всеми вопросами, " +
                        "связанными с природным газом. В ней приведены сведения о происхождении природного газа, " +
                        "в том числе методы разведки, бурения и заканчивания газовых скважин, история открытия и " +
                        "применения газа для освещения и отопления, включая его бытовое использование, коммерческое "+
                        "потребление, а также применение в промышленности и д/ш производства электроэнергии.Уделено внимание "+
                        "этапам развития газовой промышленности, транспортировке газа по трубам, эксплуатации газопроводов, " +
                        "а также хранению газа, маркетингу и сбыту. Читатель ознакомится с нормами государственного " +
                        "регулирования газовой промышленности и охраны окружающей среды.В книге проанализированы перспективы " +
                        "изменения спроса и предложения природного газа. Приведен подробный словарь терминов и предметный " +
                        "указатель. Книга рассчитана на широкий круг читателей.",
                true,
                null
        );

        when(service.add(anyLong(), any())).thenReturn(new ItemRefundDto());

        mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        verify(service, never()).add(anyLong(), any());
    }

    @Test
    void add_whenAvailableNotValid_Null_thanReturnBadRequest() throws Exception {
        Long userId = 1L;
        ItemDto itemDto = new ItemDto(
                "коробка",
                "для перевозки",
                null,
                null
        );

        when(service.add(anyLong(), any())).thenReturn(new ItemRefundDto());

        mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        verify(service, never()).add(anyLong(), any());
    }

    // метод edit
    @Test
    void edit_ok() throws Exception {
        Long userId = 1L;
        Long itemId = 1L;
        ItemDto itemDto = new ItemDto(
                "коробка",
                "коробка для перевозки вещей",
                true,
                null
        );

        when(service.edit(anyLong(), anyLong(), any())).thenReturn(new ItemRefundDto());

        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(service).edit(anyLong(), anyLong(), any());
    }

    @Test
    void edit_whenDescriptionNotValid_MoreMaxSize_thanReturnBadRequest() throws Exception {
        Long userId = 1L;
        Long itemId = 1L;
        ItemDto itemDto = new ItemDto(
                "Книга",
                "Книга знакомит читателя со всеми вопросами, " +
                        "связанными с природным газом. В ней приведены сведения о происхождении природного газа, " +
                        "в том числе методы разведки, бурения и заканчивания газовых скважин, история открытия и " +
                        "применения газа для освещения и отопления, включая его бытовое использование, коммерческое "+
                        "потребление, а также применение в промышленности и д/ш производства электроэнергии.Уделено внимание "+
                        "этапам развития газовой промышленности, транспортировке газа по трубам, эксплуатации газопроводов, " +
                        "а также хранению газа, маркетингу и сбыту. Читатель ознакомится с нормами государственного " +
                        "регулирования газовой промышленности и охраны окружающей среды.В книге проанализированы перспективы " +
                        "изменения спроса и предложения природного газа. Приведен подробный словарь терминов и предметный " +
                        "указатель. Книга рассчитана на широкий круг читателей.",
                true,
                null
        );

        when(service.edit(anyLong(), anyLong(), any())).thenReturn(new ItemRefundDto());

        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        verify(service, never()).edit(anyLong(), anyLong(), any());
    }

    // метод findById
    @Test
    void findById_ok() throws Exception {
        Long userId = 1L;
        Long itemId = 1L;

        when(service.findById(anyLong(), anyLong())).thenReturn(new ItemBookingDto());

        mockMvc.perform(get("/items/{itemId}", itemId)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(service).findById(anyLong(), anyLong());
    }

    // метод findAll
    @Test
    void findAll_ok() throws Exception {
        Long userId = 1L;
        int from = 0;
        int size = 2;

        when(service.findAllForUser(anyInt(), anyInt(), anyLong())).thenReturn(List.of());

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk());

        verify(service).findAllForUser(anyInt(), anyInt(), anyLong());
    }

    @Test
    void findAll_whenWithoutParam_ok() throws Exception {
        Long userId = 1L;

        when(service.findAllForUser(anyInt(), anyInt(), anyLong())).thenReturn(List.of());

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", "")
                        .param("size", ""))
                .andExpect(status().isOk());

        verify(service).findAllForUser(anyInt(), anyInt(), anyLong());
    }

    // метод search
    @Test
    void search_ok() throws Exception {
        Long userId = 1L;
        int from = 0;
        int size = 2;

        when(service.search(anyString(), anyInt(), anyInt())).thenReturn(List.of());

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", userId)
                        .param("text", "ко")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk());

        verify(service).search(anyString(), anyInt(), anyInt());
    }

    @Test
    void search_whenNoText_thenReturnInternalServerError() throws Exception {
        Long userId = 1L;
        int from = 0;
        int size = 2;

        when(service.search(anyString(), anyInt(), anyInt())).thenReturn(List.of());

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", userId)
                        .param("text", "")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk());

        verify(service, never()).search(anyString(), anyInt(), anyInt());
    }

    // метод addComment
    @Test
    void addComment_ok() throws Exception {
        Long userId = 1L;
        Long itemId = 1L;
        CommentDto commentDto = new CommentDto(
                null,
                "very good"
        );

        when(service.addComment(anyLong(), anyLong(), any())).thenReturn(new CommentRefundDto());

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(commentDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(service).addComment(anyLong(), anyLong(), any());
    }
}
