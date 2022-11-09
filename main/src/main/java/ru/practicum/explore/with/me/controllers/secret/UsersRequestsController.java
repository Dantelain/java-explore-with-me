package ru.practicum.explore.with.me.controllers.secret;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.requests.dto.RequestDto;
import ru.practicum.explore.with.me.requests.service.RequestsService;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
public class UsersRequestsController {

    private RequestsService requestsService;

    @GetMapping
    public List<RequestDto> getAll(@PathVariable Long userId) {
        return requestsService.getAll(userId);
    }

    @PostMapping
    public RequestDto add(@PathVariable Long userId, @RequestParam Long eventId) {
        return requestsService.add(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancel(@PathVariable Long userId, @PathVariable Long requestId) {
        return requestsService.cancel(userId, requestId);
    }

}
