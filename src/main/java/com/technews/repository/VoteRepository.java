package com.technews.repository;

import com.technews.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VoteRepository extends JpaRepository<Vote, Integer> {
    @Query("SELECT count(*) FROM VOTE v WHERE v.postId = :id")
    // use the id as a parameter and use the Integer id
    int countVotesByPostId(@Param("id") Integer id);
}
