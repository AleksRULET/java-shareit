package ru.practicum.shareit.Item.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.Item.comment.dto.CommentCreateDto;
import ru.practicum.shareit.client.BaseClient;

@Service
public class CommentClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public CommentClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    @CacheEvict(cacheNames = "items", condition = "#result.getStatusCode().is2xxSuccessful()", allEntries = true)
    public ResponseEntity<Object> addComment(long userId, Long itemId, CommentCreateDto commentCreateDto) {
        return post("/" + itemId + "/comment", userId, commentCreateDto);
    }
}
