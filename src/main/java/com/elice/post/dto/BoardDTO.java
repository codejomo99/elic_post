package com.elice.post.dto;

import com.elice.post.entity.BoardEntity;
import com.elice.post.entity.BoardFileEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
    private Long id;
    private String boardWriter;
    private String boardPass;
    private String boardTitle;
    private String boardContents;
    private int boardHits;
    private LocalDateTime boardCreatedTime;
    private LocalDateTime boardUpdatedTime;

    private List<MultipartFile> boardFile; // save.html -> Controller 파일 담는 용도
    private List<String> originalFileName; // 원본 파일 이름
    private List<String> storedFileName; // 서버 저장용 파일 이름
    private int fileAttached; // 파일 첨부 여부(첨부 1, 미첨부 0)

    public BoardDTO(Long id, String boardPass, String boardWriter,  String boardTitle, int boardHits, LocalDateTime boardCreatedTime) {
        this.id = id;
        this.boardWriter = boardWriter;
        this.boardPass = boardPass;
        this.boardTitle = boardTitle;
        this.boardHits = boardHits;
        this.boardCreatedTime = boardCreatedTime;
    }

    public static BoardDTO toBoardDTO(BoardEntity boardEntity) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(boardEntity.getId());
        boardDTO.setBoardWriter(boardEntity.getBoardWriter());
        boardDTO.setBoardPass(boardEntity.getBoardPass());
        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
        boardDTO.setBoardContents(boardEntity.getBoardContents());
        boardDTO.setBoardHits(boardEntity.getBoardHits());
        boardDTO.setBoardCreatedTime(boardEntity.getCreatedTime());
        boardDTO.setBoardUpdatedTime(boardEntity.getUpdatedTime());

        // 파일 첨부 여부 확인
        if (boardEntity.getFileAttached() != null && boardEntity.getFileAttached() == 1) {
            // 파일이 첨부되어 있는 경우
            if (!boardEntity.getBoardFileEntityList().isEmpty()) {
                // BoardFileEntityList가 비어있지 않은 경우
                boardDTO.setFileAttached(1); // 파일 첨부 여부 설정
                // 첫 번째 파일 정보 설정
                List<String> originalFileNameList = new ArrayList<>();
                List<String> storedFileNameList = new ArrayList<>();
                for(BoardFileEntity boardFileEntity: boardEntity.getBoardFileEntityList()){
                    originalFileNameList.add(boardFileEntity.getOriginalFileName());
                    storedFileNameList.add(boardFileEntity.getStoredFileName());
                }
                boardDTO.setOriginalFileName(originalFileNameList);
                boardDTO.setStoredFileName(storedFileNameList);

            } else {
                // BoardFileEntityList가 비어있는 경우
                boardDTO.setFileAttached(0); // 파일 첨부 여부 설정
            }
        } else {
            // 파일이 첨부되어 있지 않은 경우
            boardDTO.setFileAttached(0); // 파일 첨부 여부 설정
        }

        return boardDTO;
    }



}
