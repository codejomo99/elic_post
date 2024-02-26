package com.elice.post.entity;


import com.elice.post.dto.BoardDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "board")
public class BoardEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false)
    private String boardWriter;

    @Column
    private String boardPass;

    @Column(length = 15)
    private String boardTitle;

    @Column(length = 1000)
    private String boardContents;

    @Column
    private int boardHits;

    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CommentEntity> commentEntityList = new ArrayList<>();


    public static BoardEntity toSaveEntity(BoardDTO boardDTO){
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardHits(0);
        return boardEntity;
    }

    // 기본 생성자 (JPA 요구)
    protected BoardEntity() {
    }

    // 게시물 업데이트 메서드
    public void update(BoardDTO boardDTO) {
        this.boardWriter = boardDTO.getBoardWriter();
        this.boardPass = boardDTO.getBoardPass();
        this.boardContents = boardDTO.getBoardContents();
        this.boardTitle = boardDTO.getBoardTitle();
        this.boardHits = boardDTO.getBoardHits();
    }

    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CommentEntity> commentEntityList = new ArrayList<>();

}
