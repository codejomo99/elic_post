package com.elice.post.repository;

import com.elice.post.entity.BoardEntity;
import com.elice.post.entity.BoardFileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardFileRepository extends JpaRepository<BoardFileEntity,Long> {

    List<BoardFileEntity> findByBoardEntity(BoardEntity boardEntity);
}
