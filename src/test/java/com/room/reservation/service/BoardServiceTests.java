package com.room.reservation.service;

import com.room.reservation.dto.BoardDTO;
import com.room.reservation.dto.PageRequestDTO;
import com.room.reservation.dto.PageResultDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister(){
        BoardDTO dto = BoardDTO.builder()
                .title("Test.")
                .content("Test...")
                .writerEmail("user23@aaa.com")
                .build();

        Long bno = boardService.register(dto);
    }

    @Test
    public void testList(){
        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        PageResultDTO<BoardDTO, Object[]> result = boardService.getList(pageRequestDTO);
        for(BoardDTO boardDTO : result.getDtoList()){
            System.out.println(boardDTO);
        }
    }

    @Test
    public void testGet(){
        Long bno = 5L;
        BoardDTO boardDTO = boardService.get(bno);
        System.out.println(boardDTO);
    }

    @Test
    public void testRemove(){
//        testList();
        Long bno = 1L;
        boardService.removeWithReplies(bno);
//        testList();
    }

    @Test
    public void testModify(){
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(2L)
                .title("제목 변경요")
                .content("내용 변경요")
                .build();

        boardService.modify(boardDTO);
    }

}
