package org.example.music_api.controller;

import org.example.music_api.dto.request;
import org.example.music_api.dto.songresponse;
import org.example.music_api.service.songservice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
public class songcontroller {

    private final songservice service;

    public songcontroller(songservice service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public songresponse create(@RequestBody request data) {
        return service.create(data);
    }

    @GetMapping
    public List<songresponse> findAll(
            @RequestParam(name = "title", required = false) String title
    ) {
        if (title == null || title.isBlank()) {
            return service.findAll();
        }

        return service.searchByTitle(title);
    }

    @GetMapping("/{id}")
    public songresponse findById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public songresponse update(
            @PathVariable("id") Long id,
            @RequestBody request data
    ) {
        return service.update(id, data);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}