package com.elice.post.service;


import com.elice.post.dto.BoardDTO;
import com.elice.post.entity.BoardEntity;
import com.elice.post.entity.BoardFileEntity;
import com.elice.post.repository.BoardFileRepository;
import com.elice.post.repository.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;

    public void save(BoardDTO boardDTO) throws IOException {
        // 파일 첨부 여부에 따라 로직 분리
        if (boardDTO.getBoardFile().isEmpty()) {
            // 첨부 파일 없음.
            BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
            boardRepository.save(boardEntity);
        } else {
            // 첨부 파일 있음.

            /*
                1. DTO에 담긴 파일을 꺼냄
                2. 파일의 이름 가져옴
                3. 서버 저장용 이름을 만듦
                // 내사진.jpg => 839798375892_내사진.jpg
                4. 저장 경로 설정
                5. 해당 경로에 파일 저장
                6. board_table에 해당 데이터 save 처리
                7. board_file_table에 해당 데이터 save 처리
             */

            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO);
            Long savedId = boardRepository.save(boardEntity).getId();
            BoardEntity board = boardRepository.findById(savedId).get();
            for (MultipartFile boardFile : boardDTO.getBoardFile()) {
                String originalFilename = boardFile.getOriginalFilename(); // 2.
                String storedFileName = System.currentTimeMillis() + "_" + originalFilename; // 3.
                String savePath = "/Users/codejomo99/springboot_img/" + storedFileName; // C:/springboot_img/9802398403948_내사진.jpg
                boardFile.transferTo(new File(savePath)); // 5.
                BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName);
                boardFileRepository.save(boardFileEntity);
            }

        }

    }


    @Transactional
    public BoardDTO findById(Long id) {
        return boardRepository.findById(id)
                .map(BoardDTO::toBoardDTO)
                .orElse(null);
    }

    @Transactional
    public void update(BoardDTO boardDTO) throws IOException {
        // 기존 게시물 엔티티를 가져옵니다.
        BoardEntity boardEntity = boardRepository.findById(boardDTO.getId()).orElseThrow(() -> new EntityNotFoundException("게시물을 찾을 수 없습니다."));

        // 게시물 정보를 업데이트합니다.
        BoardEntity updatedBoardEntity = BoardEntity.toFileUpdateEntity(boardDTO);
        // 업데이트된 게시물 정보를 저장합니다.
        boardRepository.save(updatedBoardEntity);

        // 기존 첨부 파일을 삭제합니다.
        List<BoardFileEntity> existingFiles = boardFileRepository.findByBoardEntity(boardEntity);

        for (BoardFileEntity existingFile : existingFiles) {
            // 파일 시스템에서 파일을 삭제합니다.
            String filePath = "/Users/codejomo99/springboot_img/" + existingFile.getStoredFileName();
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            // 데이터베이스에서 파일 정보를 삭제합니다.
            boardFileRepository.delete(existingFile);
        }

        // 새로운 첨부 파일을 업로드합니다.
        if (!boardDTO.getBoardFile().isEmpty()) {
            for (MultipartFile boardFile : boardDTO.getBoardFile()) {
                String originalFilename = boardFile.getOriginalFilename();
                String storedFileName = System.currentTimeMillis() + "_" + originalFilename;
                String savePath = "/Users/codejomo99/springboot_img/" + storedFileName;
                boardFile.transferTo(new File(savePath));
                // 게시물과 연결된 첨부 파일 엔티티를 생성하고 저장합니다.
                BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(boardEntity, originalFilename, storedFileName);
                boardFileRepository.save(boardFileEntity);
            }
        }
    }


    @Transactional
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    // 페이징
    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 6;
        Page<BoardEntity> boardEntities = boardRepository.findAll(PageRequest.of(page, pageLimit,
                Sort.by(Sort.Direction.DESC, "id")));

        Page<BoardDTO> boardDTOS = boardEntities.map(board -> new BoardDTO(board.getId(), board.getBoardPass(), board.getBoardWriter(),
                board.getBoardTitle(), board.getBoardHits(), board.getCreatedTime()));
        return boardDTOS;


    }

    // 페이징 + 검색
    @Transactional
    public Page<BoardDTO> findByBoardTitleContainingAndPaging(String keyword, Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 6;

        Page<BoardEntity> boardEntities = boardRepository.findByBoardTitleContaining(keyword, PageRequest.of(page, pageLimit,
                Sort.by(Sort.Direction.DESC, "id")));

        Page<BoardDTO> boardDTOS = boardEntities.map(board -> new BoardDTO(board.getId(), board.getBoardPass(), board.getBoardWriter(),
                board.getBoardTitle(), board.getBoardHits(), board.getCreatedTime()));

        return boardDTOS;
    }


}
