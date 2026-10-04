package org.example.music_api.repository;

import org.example.music_api.domain.Song;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class memoryrepository implements songrepository {
    private LinkedHashMap<Long, Song> store = new LinkedHashMap<>();
    private long sequence = 0;

    @Override
    public Song save(Song song) {
        sequence = sequence + 1;
        song.setId(sequence);
        store.put(sequence, song);

        return song;
    }

    @Override
    public List<Song> findAll() {
        List<Song> songs = new ArrayList<>();

        for (Song song : store.values()) {
            songs.add(song);
        }

        return songs;
    }

    @Override
    public Optional<Song> findById(Long id) {
        Song song = store.get(id);

        return Optional.ofNullable(song);
    }

    @Override
    public Song update(Song song) {
        Long id = song.getId();
        store.put(id, song);

        return song;
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }
}