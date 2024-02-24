package com.elice.post.service;


import com.elice.post.dto.CommentDTO;
import com.elice.post.entity.BoardEntity;
import com.elice.post.entity.CommentEntity;
import com.elice.post.repository.BoardRepository;
import com.elice.post.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public Long save(CommentDTO commentDTO){
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(commentDTO.getBoardId());

        if(optionalBoardEntity.isPresent()){
            BoardEntity boardEntity = optionalBoardEntity.get();
            CommentEntity commentEntity = CommentEntity.toSaveEntity(commentDTO, boardEntity);
            return commentRepository.save(commentEntity).getId();
        }else{
        return null;
        }

    }


}
