package org.example.music_api.dto;

public record songresponse(Long id, String title, String artist, String album, String genre, Integer releaseYear) {}