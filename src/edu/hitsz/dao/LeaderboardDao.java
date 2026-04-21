package edu.hitsz.dao;

import edu.hitsz.model.ScoreRecord;

import java.util.List;

public interface LeaderboardDao {

    void insert(ScoreRecord scoreRecord);

    List<ScoreRecord> findAll();

    boolean delete(int index);
}
