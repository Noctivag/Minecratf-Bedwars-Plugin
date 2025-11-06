package com.bedwars.models;

public enum GameState {
    WAITING,      // Waiting for players
    STARTING,     // Countdown started
    ACTIVE,       // Game in progress
    ENDING,       // Game ended, cleanup
    DISABLED      // Arena disabled
}
