package com.elice.post;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.elice.post.dto.BoardDTO;
import com.elice.post.entity.BoardEntity;
import com.elice.post.repository.BoardRepository;
import com.elice.post.service.BoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private BoardService boardService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAll() {
        // 테스트용 데이터 설정
        List<BoardEntity> mockEntityList = new ArrayList<>();
        mockEntityList.add(new BoardEntity());
        mockEntityList.add(new BoardEntity());

        // Mock Repository의 findAll 메서드가 호출될 때 테스트용 데이터 반환하도록 설정
        when(boardRepository.findAll()).thenReturn(mockEntityList);

        // 서비스 메서드 호출
        List<BoardDTO> result = boardService.findAll();

        // 결과 확인
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    // findById, update, delete 등의 테스트 메서드도 유사한 방식으로 작성할 수 있습니다.
}
