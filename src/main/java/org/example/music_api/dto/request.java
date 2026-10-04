package org.example.music_api.dto;

public record request(String title, String artist, String album, String genre, Integer releaseYear) {

}