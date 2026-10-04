package org.example.music_api.controller;

import org.example.music_api.dto.request;
import org.example.music_api.dto.songresponse;
import org.example.music_api.service.songservice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    public List<songresponse> findAll() {
        return service.findAll();
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