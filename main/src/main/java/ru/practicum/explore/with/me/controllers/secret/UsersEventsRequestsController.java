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
@RequestMapping(path = "/users/{userId}/events/{eventId}/requests")
public class UsersEventsRequestsController {

    private RequestsService requestsService;

    @GetMapping
    public List<RequestDto> getAll(@PathVariable Long userId, @PathVariable Long eventId) {
        return requestsService.getAll(userId, eventId);
    }

    @PatchMapping("/{reqId}/confirm")
    public RequestDto confirm(@PathVariable Long userId, @PathVariable Long eventId, @PathVariable Long reqId) {
        return requestsService.confirm(userId, eventId, reqId);
    }

    @PatchMapping("/{reqId}/reject")
    public RequestDto reject(@PathVariable Long userId, @PathVariable Long eventId, @PathVariable Long reqId) {
        return requestsService.reject(userId, eventId, reqId);
    }
}
