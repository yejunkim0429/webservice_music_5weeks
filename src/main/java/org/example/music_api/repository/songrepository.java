package org.example.music_api.repository;

import org.example.music_api.domain.Song;

import java.util.List;
import java.util.Optional;

public interface songrepository {
    Song save(Song song);
    List<Song> findAll();
    Optional<Song> findById(Long id);
    Song update(Song song);
    void deleteById(Long id);
}
