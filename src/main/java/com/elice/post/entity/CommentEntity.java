package com.elice.post.entity;


import com.elice.post.dto.CommentDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name="comment_table2")
public class CommentEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long commentPass;

    @Column(length = 20, nullable = false)
    private String commentWriter;

    @Column
    private String commentContents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_id")
    private BoardEntity boardEntity;


    public CommentEntity(CommentDTO commentDTO, BoardEntity boardEntity){
        this.commentWriter = commentDTO.getCommentWriter();
        this.commentContents = commentDTO.getCommentContents();
        this.boardEntity = boardEntity;
    }

    protected CommentEntity(){

    }

    public void update(CommentDTO commentDTO){
        this.commentPass = commentDTO.getPass();
        this.commentWriter = commentDTO.getCommentWriter();
        this.commentContents = commentDTO.getCommentContents();
    }

}
