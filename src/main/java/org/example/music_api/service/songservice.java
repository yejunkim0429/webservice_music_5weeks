package org.example.music_api.service;

import org.example.music_api.domain.Song;
import org.example.music_api.dto.request;
import org.example.music_api.dto.songresponse;
import org.example.music_api.repository.songrepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Service
public class songservice {

    private final songrepository repository;

    public songservice(songrepository repository) {
        this.repository = repository;
    }

    public songresponse create(request data) {
        validate(data);

        Song song = new Song(
                null,
                data.title(),
                data.artist(),
                data.album(),
                data.genre(),
                data.releaseYear()
        );

        Song savedSong = repository.save(song);

        return toResponse(savedSong);
    }

    public List<songresponse> findAll() {
        List<Song> songs = repository.findAll();
        List<songresponse> responses = new ArrayList<>();

        for (Song song : songs) {
            songresponse response = toResponse(song);
            responses.add(response);
        }

        return responses;
    }

    public songresponse findById(Long id) {
        Song song = findSong(id);

        return toResponse(song);
    }

    public songresponse update(Long id, request data) {
        Song song = findSong(id);
        validate(data);

        song.setTitle(data.title());
        song.setArtist(data.artist());
        song.setAlbum(data.album());
        song.setGenre(data.genre());
        song.setReleaseYear(data.releaseYear());

        Song updatedSong = repository.update(song);

        return toResponse(updatedSong);
    }

    public void delete(Long id) {
        findSong(id);

        repository.deleteById(id);
    }

    private Song findSong(Long id) {
        Song song = repository.findById(id).orElse(null);

        if (song == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Not found: " + id
            );
        }

        return song;
    }

    private songresponse toResponse(Song song) {
        return new songresponse(
                song.getId(),
                song.getTitle(),
                song.getArtist(),
                song.getAlbum(),
                song.getGenre(),
                song.getReleaseYear()
        );
    }

    private void validate(request data) {
        if (data.title() == null || data.title().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Name please: "
            );
        }

        if (data.artist() == null || data.artist().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Singer please: "
            );
        }

        if (data.releaseYear() == null || data.releaseYear() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "release year please: "
            );
        }
    }
    public List<songresponse> searchByTitle(String title) {
        List<Song> songs = repository.findAll();
        List<songresponse> responses = new ArrayList<>();

        for (Song song : songs) {
            if (song.getTitle().contains(title)) {
                songresponse response = toResponse(song);
                responses.add(response);
            }
        }

        return responses;
    }
}