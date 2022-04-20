package game;

public record Level(String name, int needed_kills, long spawning_rate, double min_safe_distance){}
