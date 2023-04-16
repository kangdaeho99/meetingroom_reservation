package com.room.reservation.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService {

    private final BoardRepository repository; //자동주입 final

    private final ReplyRepository replyRepository;
    @Override
    public void initBoardDataBase() {
        IntStream.rangeClosed(1, 12).forEach(i -> {
            Member member = Member.builder().email("user" + i + "@aaa.com").build();

            Board board = Board.builder()
                    .title("Title..."+i)
                    .content("Content..."+i)
                    .writer(member)
                    .build();

            repository.save(board);

        });
    }

    @Override
    public Long register(BoardDTO dto) {
        log.info(dto);
        Board board = dtoToEntity(dto);
        repository.save(board);
        return board.getBno();
    }

    /**
     * Description :
     * JPQL의 결과로 나오는 Object[]을 DTO 타입으로 변환하기 위하여 만들어놓은
     * EntityToDTO를 활용하여 PageResultDTO객체를 구성합니다.
     *
     */
    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
        log.info(pageRequestDTO);
        Function<Object[], BoardDTO> fn = (en -> entityToDTO((Board)en[0], (Member)en[1], (Long)en[2]));
//        Page<Object[]> result = repository.getBoardwithReplyCount(pageRequestDTO.getPageable(Sort.by("bno").descending()));
        Page<Object[]> result = repository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("bno").descending()) );

        return new PageResultDTO<>(result, fn);
    }

    /**
     * Description :
     * 조회는 getBoardByBno(bno) 가 Board엔티티, Member엔티티, 댓글의 수(Long)을 활용하여 처리
     */
    @Override
    public BoardDTO get(Long bno){
        Object result = repository.getBoardByBno(bno);
        Object[] arr = (Object[]) result;
        return entityToDTO((Board)arr[0], (Member)arr[1], (Long)arr[2]);
    }

    /**
     * Description : Board 삭제기능 구현, Reply 삭제기능구현
     * Transaction을 활용하여 reply와 board 둘다 삭제
     * @param bno
     */
    @Transactional
    @Override
    public void removeWithReplies(Long bno) {
        replyRepository.deleteByBno(bno);
        repository.deleteById(bno);
    }

    @Override
    public void modify(BoardDTO boardDTO){
        Optional<Board> result = repository.findById(boardDTO.getBno());
        if(result.isPresent()) {

            Board board = result.get();
            board.changeTitle(boardDTO.getTitle());
            board.changeContent(boardDTO.getContent());

            repository.save(board);
        }
    }


}
